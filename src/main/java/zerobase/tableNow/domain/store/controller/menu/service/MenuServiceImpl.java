package zerobase.tableNow.domain.store.controller.menu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import zerobase.tableNow.components.S3UploadComponents;
import zerobase.tableNow.domain.constant.Role;
import zerobase.tableNow.domain.constant.Status;
import zerobase.tableNow.domain.store.controller.menu.dto.MenuDto;
import zerobase.tableNow.domain.store.controller.menu.dto.MenuUpdateDto;
import zerobase.tableNow.domain.store.controller.menu.entity.MenuEntity;
import zerobase.tableNow.domain.store.controller.menu.mapper.MenuMapper;
import zerobase.tableNow.domain.store.controller.menu.repository.MenuRepository;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.store.repository.StoreRepository;
import zerobase.tableNow.domain.user.entity.UsersEntity;
import zerobase.tableNow.domain.user.repository.UserRepository;
import zerobase.tableNow.exception.TableException;
import zerobase.tableNow.exception.type.ErrorCode;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class MenuServiceImpl implements MenuService{
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final MenuMapper menuMapper;
    private final StoreRepository storeRepository;
    private final S3UploadComponents s3UploadComponents;

    //매장 메뉴 등록
    @Override
    public MenuDto register(MenuDto menuDto, MultipartFile image) {
        // 현재 로그인한 사용자 ID 가져오기
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        // 사용자 정보 조회
        UsersEntity users = userRepository.findByUser(userId)
                .orElseThrow(() -> new TableException(ErrorCode.USER_NOT_FOUND));

        if (!users.getRole().equals(Role.MANAGER)){
            throw new TableException(ErrorCode.FORBIDDEN_ACCESS);
        }

        // storeId로 매장 조회
        StoreEntity store = storeRepository.findById(menuDto.getStoreId())
                .orElseThrow(() -> new TableException(ErrorCode.PRODUCT_NOT_FOUND));

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            try {
                imageUrl = s3UploadComponents.upload(image);
                menuDto.setImage(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("메뉴 이미지 업로드에 실패했습니다.", e);
            }
        }

        MenuEntity menu = menuMapper.toMenuEntity(menuDto,store);
        MenuEntity saveEntity = menuRepository.save(menu);



        return menuMapper.toMenuDto(saveEntity);
    }

    //메뉴 목록
    @Override
    @Transactional(readOnly = true)
//    @Cacheable(value = "menuCache", key = "#storeId")
    public List<MenuDto> list(Long storeId) {
        // 특정 storeId에 해당하는 메뉴 리스트 조회
        List<MenuEntity> menuEntities = menuRepository.findByStoreId_Id(storeId);

        if (menuEntities.isEmpty()) {
            throw new TableException(ErrorCode.PRODUCT_NOT_FOUND,"해당 매장의 메뉴가 없습니다.");
        }

        // MenuEntity를 MenuDto로 변환
        return menuEntities.stream()
                .map(menu -> MenuDto.builder()
                        .id(menu.getId())
                        .storeId(menu.getStoreId().getId())
                        .image(menu.getImage())
                        .name(menu.getName())
                        .price(menu.getPrice())
                        .status(menu.getStatus())
                        .build())
                .collect(Collectors.toList());
    }
    //메뉴삭제
    @Override
//    @CacheEvict(value = "menuCache", key = "#storeId")
    public void delete(Long menuId) {
        // 현재 로그인한 사용자 ID 가져오기
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        // 사용자 정보 조회
        UsersEntity users = userRepository.findByUser(userId)
                .orElseThrow(() -> new TableException(ErrorCode.USER_NOT_FOUND));

        if (!users.getRole().equals(Role.MANAGER)) {
            throw new TableException(ErrorCode.FORBIDDEN_ACCESS);
        }

        // 메뉴 엔티티 조회
        MenuEntity menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new TableException(ErrorCode.PRODUCT_NOT_FOUND));

        StoreEntity store = storeRepository.findById(menuId)
                .orElseThrow(() -> new TableException(ErrorCode.PRODUCT_NOT_FOUND));

        // 매장 소유자 확인 (매장의 사용자와 현재 로그인한 사용자 비교)
        if (!store.getUser().equals(users)) {
            throw new TableException(ErrorCode.FORBIDDEN_ACCESS);
        }

        // 메뉴 삭제
        menuRepository.delete(menu);
    }
    //메뉴수정
    @Override
    @Transactional
//    @CachePut(value = "menuCache", key = "#storeId")
    public void update(Long menuId, MenuUpdateDto menuUpdateDto) {
        // 현재 로그인한 사용자 ID 가져오기
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        // 사용자 정보 조회
        UsersEntity users = userRepository.findByUser(userId)
                .orElseThrow(() -> new TableException(ErrorCode.USER_NOT_FOUND));

        // 메뉴 엔티티 조회
        MenuEntity menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new TableException(ErrorCode.MENU_NOT_FOUND));

        // 메뉴가 속한 매장 정보 조회
        StoreEntity store = menu.getStoreId();

        // 매장 소유자 확인
        if (!store.getUser().equals(users)) {
            throw new TableException(ErrorCode.FORBIDDEN_ACCESS);
        }

        // 수정된 값이 있다면, 해당 값을 엔티티에 반영
        if (menuUpdateDto.getName() != null) {
            menu.setName(menuUpdateDto.getName());
        }
        if (menuUpdateDto.getImage() != null) {
            menu.setImage(menuUpdateDto.getImage());
        }
        if (menuUpdateDto.getPrice() != 0) {
            menu.setPrice(menuUpdateDto.getPrice());
        }

        // 수정된 메뉴 엔티티 저장
        menuRepository.save(menu);
    }

    // 메뉴 수정 -> 상태수정 (매진여부)
    @Override
    @Transactional
//    @CachePut(value = "menuCache", key = "#storeId")
    public void reStatus(Long menuId) {
        // 현재 로그인한 사용자 ID 가져오기
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        UsersEntity users = userRepository.findByUser(userId)
                .orElseThrow(() -> new TableException(ErrorCode.USER_NOT_FOUND));

        // 메뉴 조회
        MenuEntity menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new TableException(ErrorCode.MENUS_NOT_FOUND));

        // 메뉴 상태를 변경하는 로직
        if (menu.getStatus().equals(Status.ING)) {
            // 상태가 "ING"이면 "STOP"으로 변경
            menuRepository.updateMenuStatus(menuId, Status.STOP);
        } else if (menu.getStatus().equals(Status.STOP)) {
            // 상태가 "STOP"이면 "ING"으로 변경
            menuRepository.updateMenuStatus(menuId, Status.ING);
        } else {
            throw new TableException(ErrorCode.INVALID_REQUEST);
        }
    }
}
