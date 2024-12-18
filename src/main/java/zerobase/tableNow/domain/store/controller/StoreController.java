package zerobase.tableNow.domain.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zerobase.tableNow.domain.constant.SortType;
import zerobase.tableNow.domain.store.controller.menu.dto.MenuDto;
import zerobase.tableNow.domain.store.controller.menu.service.MenuService;
import zerobase.tableNow.domain.store.dto.StoreDto;
import zerobase.tableNow.domain.store.service.StoreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store/")
@Slf4j
public class StoreController {
    private final StoreService storeService;
    private final MenuService menuService;

    //상점 등록
    @PostMapping("register")
    public ResponseEntity<StoreDto> register(
            @RequestPart(value = "storeDto") StoreDto storeDto,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "menuDtos") List<MenuDto> menuDtos
    ) {
        StoreDto savedStore = storeService.register(storeDto, image);
        for (MenuDto menuDto : menuDtos) {
            log.info(String.valueOf(savedStore.getId())); //여기가 null임
            menuDto.setStoreId(savedStore.getId());  // 상점 ID 설정
            menuService.register(menuDto);  // 메뉴 등록
        }
        return ResponseEntity.ok(savedStore);  // 성공적으로 등록된 상점 정보 반환
    }

//    @PostMapping ("register")
//    public ResponseEntity<StoreDto> register(
//            @RequestPart(value = "dto") StoreDto storeDto,
//            @RequestPart(value = "image", required = false) MultipartFile image
//    ){
//        return ResponseEntity.ok().body(storeService.register(storeDto, image));
//    }

    // 상점 목록
    @GetMapping("list")
    public ResponseEntity<List<StoreDto>> list(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "sortType", required = false, defaultValue = "DISTANCE") String sortType,
            @RequestParam(name = "userLat", required = false) Double userLat,
            @RequestParam(name = "userLon", required = false) Double userLon
    ) {
        SortType sortTypeEnum = sortType != null ? SortType.valueOf(sortType) : SortType.DISTANCE;
        return ResponseEntity.ok().body(
                storeService.getAllStores(keyword, sortTypeEnum, userLat, userLon)
        );
    }

    //상점 수정
    @PutMapping("update")
    public ResponseEntity<StoreDto> update(@RequestParam(name = "id") Long id ,
                                           @RequestBody StoreDto storeDto){
        return ResponseEntity.ok().body(storeService.update(id,storeDto));

    }
    //상점 상세정보
    @GetMapping("detail")
    public ResponseEntity<StoreDto> detail(@RequestParam(name = "id") Long id){
        return ResponseEntity.ok().body(storeService.detail(id));
    }
    //상점 삭제
    @DeleteMapping("delete")
    public ResponseEntity<Void> delete(@RequestParam(name = "id") Long id) {
        storeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
