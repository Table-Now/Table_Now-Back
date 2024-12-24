package zerobase.tableNow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import zerobase.tableNow.domain.cart.dto.CartDto;
import zerobase.tableNow.domain.cart.entity.CartEntity;
import zerobase.tableNow.domain.cart.mapper.CartMapper;
import zerobase.tableNow.domain.cart.repository.CartRepository;
import zerobase.tableNow.domain.cart.service.CartServiceImpl;
import zerobase.tableNow.domain.reservation.entity.ReservationEntity;
import zerobase.tableNow.domain.reservation.repository.ReservationRepository;
import zerobase.tableNow.domain.store.controller.menu.entity.MenuEntity;
import zerobase.tableNow.domain.store.controller.menu.repository.MenuRepository;
import zerobase.tableNow.domain.store.entity.StoreEntity;
import zerobase.tableNow.domain.store.repository.StoreRepository;
import zerobase.tableNow.domain.user.entity.UsersEntity;
import zerobase.tableNow.domain.user.repository.UserRepository;
import zerobase.tableNow.exception.TableException;
import zerobase.tableNow.exception.type.ErrorCode;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class CartControllerTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private CartMapper cartMapper;

    @InjectMocks
    private CartServiceImpl cartService; // 테스트 대상 클래스

    @Test
    @DisplayName("장바구니 추가 - 사용자가 로그인되어 있을 때 장바구니가 정상적으로 추가된다.")
    public void testAddCart_whenUserIsLoggedIn() {
        // Given: Mock 데이터 설정
        // 현재 로그인한 사용자 ID를 SecurityContext에 설정
        String mockUserId = "testUser";
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(mockUserId, null));

        // 사용자 생성
        UsersEntity mockUser = new UsersEntity();
        mockUser.setUser(mockUserId);
        mockUser.setPhone("123-456-7890");

        // 예약 정보 생성 (대기번호 포함)
        ReservationEntity mockReservation = new ReservationEntity();
        mockReservation.setWaitingNumber(1);

        // Mock 매장 정보 생성
        StoreEntity mockStore = new StoreEntity();
        mockStore.setId(1L);

        // Mock 메뉴 정보 생성
        MenuEntity mockMenu = new MenuEntity();
        mockMenu.setId(1L);
        mockMenu.setPrice(10000);
        mockMenu.setStore(mockStore);

        // 클라이언트 요청으로 받을 장바구니 DTO 생성
        CartDto cartDtoRequest = new CartDto();
        cartDtoRequest.setMenuId(1L);
        cartDtoRequest.setTotalCount(2);

        // 저장된 장바구니 엔티티
        CartEntity savedCartEntity = new CartEntity();
        savedCartEntity.setTotalAmount(20000);
        // 저장 후 반환할 장바구니 DTO
        CartDto cartDtoResponse = new CartDto();
        cartDtoResponse.setTotalAmount(20000);


        // **Mock 리포지토리 및 매퍼 행동 설정**
        Mockito.when(userRepository.findByUser(mockUserId)).thenReturn(Optional.of(mockUser));
        Mockito.when(reservationRepository.findByPhone(mockUser.getPhone())).thenReturn(Optional.of(mockReservation));
        Mockito.when(storeRepository.findById(1L)).thenReturn(Optional.of(mockStore));
        Mockito.when(menuRepository.findById(cartDtoRequest.getMenuId())).thenReturn(Optional.of(mockMenu));
        Mockito.when(cartRepository.save(Mockito.any(CartEntity.class))).thenReturn(savedCartEntity);
        Mockito.when(cartMapper.toCartDto(savedCartEntity)).thenReturn(cartDtoResponse);

        // **When: 장바구니 등록 서비스 메서드 호출**
        CartDto result = cartService.addCart(1L, cartDtoRequest);

        // Then: 결과 검증
        // 반환된 DTO의 총 금액이 기대값과 같은지 검증
        assertEquals(20000, result.getTotalAmount());

        // 리포지토리 호출 횟수를 검증
        Mockito.verify(userRepository, times(1)).findByUser(mockUserId);
        Mockito.verify(cartRepository, times(1)).save(Mockito.any(CartEntity.class));
    }

    @Test
    @DisplayName("장바구니 목록 조회 - 사용자가 소유한 장바구니 목록을 정상적으로 반환한다.")
    public void testCartList_whenUserHasCartItems() {
        // Mock 사용자 ID
        String mockUserId = "testUser";

        // Mock 장바구니 엔티티 목록 생성
        CartEntity cartEntity1 = new CartEntity();
        cartEntity1.setId(1L);
        cartEntity1.setTotalAmount(10000);

        CartEntity cartEntity2 = new CartEntity();
        cartEntity2.setId(2L);
        cartEntity2.setTotalAmount(20000);

        List<CartEntity> mockCartEntities = Arrays.asList(cartEntity1, cartEntity2);

        // Mock 반환될 DTO
        CartDto cartDto1 = new CartDto();
        cartDto1.setId(1L);
        cartDto1.setTotalAmount(10000);

        CartDto cartDto2 = new CartDto();
        cartDto2.setId(2L);
        cartDto2.setTotalAmount(20000);

        List<CartDto> mockCartDtos = Arrays.asList(cartDto1, cartDto2);

        // **Mock 리포지토리 및 매퍼 행동 설정**
        Mockito.when(cartRepository.findByUserId_User(mockUserId)).thenReturn(mockCartEntities);
        Mockito.when(cartMapper.toCartDto(cartEntity1)).thenReturn(cartDto1);
        Mockito.when(cartMapper.toCartDto(cartEntity2)).thenReturn(cartDto2);

        // **When: 장바구니 리스트 메서드 호출**
        List<CartDto> result = cartService.cartList(mockUserId);

        // **Then: 결과 검증**

        // 반환된 장바구니 목록 크기가 기대값과 같은지 검증
        assertEquals(2, result.size());

        // 반환된 장바구니 목록의 세부 값 검증
        assertEquals(1L, result.get(0).getId());
        assertEquals(10000, result.get(0).getTotalAmount());
        assertEquals(2L, result.get(1).getId());
        assertEquals(20000, result.get(1).getTotalAmount());

        // 리포지토리가 올바르게 호출되었는지 확인
        Mockito.verify(cartRepository, times(1)).findByUserId_User(mockUserId);

        // 매퍼가 엔티티를 DTO로 매핑한 횟수를 검증
        Mockito.verify(cartMapper, times(2)).toCartDto(Mockito.any(CartEntity.class));
    }
    @Test
    @DisplayName("장바구니 삭제 - 사용자가 소유자인 경우 정상적으로 삭제되어야 한다.")
    public void testCartDelete_whenUserIsOwner() {
        // **Given: Mock 데이터 설정**
        String mockUserId = "testUser";
        Long mockCartId = 1L;

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(mockUserId, null));

        UsersEntity mockUser = new UsersEntity();
        mockUser.setUser(mockUserId);

        CartEntity mockCart = new CartEntity();
        mockCart.setId(mockCartId);
        mockCart.setUser(mockUser);

        // **Mock 행동 설정**
        Mockito.when(userRepository.findByUser(mockUserId)).thenReturn(Optional.of(mockUser));
        Mockito.when(cartRepository.findById(mockCartId)).thenReturn(Optional.of(mockCart));

        // **When: 장바구니 삭제 메서드 호출**
        cartService.cartDelete(mockCartId);

        // **Then: 결과 검증**
        Mockito.verify(cartRepository, times(1)).delete(mockCart);
    }

    @Test
    @DisplayName("장바구니 삭제 - 사용자가 소유자가 아닌 경우 예외가 발생해야 한다.")
    public void testCartDelete_whenUserIsNotOwner_shouldThrowException() {
        // **Given: Mock 데이터 설정**
        String mockUserId = "testUser";
        Long mockCartId = 1L;

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(mockUserId, null));

        UsersEntity mockUser = new UsersEntity();
        mockUser.setUser(mockUserId);

        UsersEntity anotherUser = new UsersEntity();
        anotherUser.setUser("anotherUser");

        CartEntity mockCart = new CartEntity();
        mockCart.setId(mockCartId);
        mockCart.setUser(anotherUser);

        // **Mock 행동 설정**
        Mockito.when(userRepository.findByUser(mockUserId)).thenReturn(Optional.of(mockUser));
        Mockito.when(cartRepository.findById(mockCartId)).thenReturn(Optional.of(mockCart));

        // **When & Then: 예외 검증**
        TableException exception = assertThrows(TableException.class, () -> {
            cartService.cartDelete(mockCartId);
        });

        assertEquals(ErrorCode.FORBIDDEN_ACCESS, exception.getErrorCode());
        Mockito.verify(cartRepository, times(0)).delete(Mockito.any(CartEntity.class));
    }

    @Test
    @DisplayName("장바구니 수정 - 장바구니가 존재하면 수량만 수정된다.")
    public void testUpdateCart_whenUserIsOwner() {
        // Given: Mock 데이터 설정
        String mockUserId = "testUser";
        CartDto cartDtoRequest = new CartDto();
        cartDtoRequest.setMenuId(1L);  // 메뉴 ID
        cartDtoRequest.setTotalCount(3);  // 새로운 수량

        MenuEntity mockMenu = new MenuEntity();
        mockMenu.setPrice(10000);
        mockMenu.setId(1L);

        CartEntity mockCart = new CartEntity();
        mockCart.setUser(new UsersEntity());
        mockCart.setTotalAmount(20000);  // 기존 금액
        mockCart.setTotalCount(2);  // 기존 수량

        // Mock 행동 설정: 해당 메뉴가 장바구니에 존재하는 경우
        Mockito.when(menuRepository.findById(cartDtoRequest.getMenuId())).thenReturn(Optional.of(mockMenu));
        Mockito.when(cartRepository.findByUser_UserAndMenu_Id(mockUserId, cartDtoRequest.getMenuId()))
                .thenReturn(Optional.of(mockCart));

        // When: 장바구니 수정 메서드 호출
        cartService.updateCart(mockUserId, cartDtoRequest);

        // Then: 장바구니 수량만 수정되었고, save가 한 번 호출되었는지 확인
        Mockito.verify(cartRepository, times(0)).save(mockCart);  // save() 호출이 없어야 함
        assertEquals(30000, mockCart.getTotalAmount());  // 10000 * 3 (수정된 총액)
        assertEquals(3, mockCart.getTotalCount());  // 수정된 수량
    }

    @Test
    @DisplayName("장바구니 수정 - 장바구니가 존재하지 않으면 예외가 발생해야 한다.")
    public void testUpdateCart_whenCartNotFound_shouldThrowException() {
        // Given: Mock 데이터 설정
        String mockUserId = "testUser";
        CartDto cartDtoRequest = new CartDto();
        cartDtoRequest.setMenuId(1L);
        cartDtoRequest.setTotalCount(3);

        // Mock 행동 설정
        Mockito.when(cartRepository.findByUser_UserAndMenu_Id(mockUserId, cartDtoRequest.getMenuId())).thenReturn(Optional.empty());

        // When & Then: 예외 검증
        TableException exception = assertThrows(TableException.class, () -> {
            cartService.updateCart(mockUserId, cartDtoRequest);
        });

        assertEquals(ErrorCode.CART_NOT_FOUND, exception.getErrorCode());
    }

}

