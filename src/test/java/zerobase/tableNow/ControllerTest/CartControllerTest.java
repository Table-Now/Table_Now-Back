//package zerobase.tableNow.ControllerTest;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import zerobase.tableNow.components.CartUtil;
//import zerobase.tableNow.domain.cart.dto.CartDto;
//import zerobase.tableNow.domain.cart.entity.CartEntity;
//import zerobase.tableNow.domain.cart.mapper.CartMapper;
//import zerobase.tableNow.domain.cart.repository.CartRepository;
//import zerobase.tableNow.domain.cart.service.CartServiceImpl;
//import zerobase.tableNow.domain.reservation.entity.ReservationEntity;
//import zerobase.tableNow.domain.reservation.repository.ReservationRepository;
//import zerobase.tableNow.domain.store.controller.menu.entity.MenuEntity;
//import zerobase.tableNow.domain.store.controller.menu.repository.MenuRepository;
//import zerobase.tableNow.domain.store.entity.StoreEntity;
//import zerobase.tableNow.domain.store.repository.StoreRepository;
//import zerobase.tableNow.domain.user.entity.UsersEntity;
//import zerobase.tableNow.domain.user.repository.UserRepository;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class CartControllerTest {
//
//    @InjectMocks
//    private CartServiceImpl cartService;
//
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private CartRepository cartRepository;
//    @Mock
//    private MenuRepository menuRepository;
//    @Mock
//    private StoreRepository storeRepository;
//    @Mock
//    private ReservationRepository reservationRepository;
//    @Mock
//    private CartMapper cartMapper;
//    @Mock
//    private CartUtil cartUtil;
//
//    @BeforeEach
//    public void setUp() {
//        // SecurityContext 설정
//        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//        Authentication authentication = new UsernamePasswordAuthenticationToken("ghdgptn12", "password");
//        securityContext.setAuthentication(authentication);
//        SecurityContextHolder.setContext(securityContext);
//    }
//
//    @Test
//    public void testAddCart() {
//        Long storeId = 1L;
//        CartDto cartDto = new CartDto();
//        cartDto.setMenuId(1L);
//        cartDto.setCount(2);
//
//        UsersEntity userEntity = new UsersEntity();
//        userEntity.setUser("ghdgptn12");
//        userEntity.setEmail("user@example.com");
//        userEntity.setPhone("010-1111-1111");
//
//        ReservationEntity reservationEntity = new ReservationEntity();
//        reservationEntity.setPhone("010-1111-1111");
//        reservationEntity.setWaitingNumber(1);
//
//        StoreEntity storeEntity = new StoreEntity();
//        storeEntity.setId(storeId);
//        storeEntity.setStore("대너리스");
//
//        MenuEntity menuEntity = new MenuEntity();
//        menuEntity.setId(1L);
//        menuEntity.setName("봉골레파스타");
//        menuEntity.setStoreId(storeEntity);
//
//        CartEntity cartEntity = new CartEntity();
//        cartEntity.setUserId(userEntity);
//        cartEntity.setMenuId(menuEntity);
//        cartEntity.setStore(storeEntity);
//        cartEntity.setCount();
//
//        // Mock repository responses
//        when(userRepository.findByUser("ghdgptn12")).thenReturn(Optional.of(userEntity));
//        when(reservationRepository.findByPhone("010-1111-1111")).thenReturn(Optional.of(reservationEntity));
//        when(storeRepository.findById(storeId)).thenReturn(Optional.of(storeEntity));
//        when(menuRepository.findById(1L)).thenReturn(Optional.of(menuEntity));
//        when(cartRepository.save(any(CartEntity.class))).thenReturn(cartEntity);
//
//        // Mock CartUtil responses
//        when(cartUtil.calculateItemPrice(any(CartEntity.class))).thenReturn(20000);
//        when(cartUtil.isValidCart(any(CartEntity.class))).thenReturn(true);
//
//        // Mock CartMapper response
//        CartDto expectedCartDto = new CartDto();
//        expectedCartDto.setMenuId(1L);
//        expectedCartDto.setCount(2);
//        expectedCartDto.setTotalPrice(20000);
//        when(cartMapper.toCartDto(any(CartEntity.class))).thenReturn(expectedCartDto);
//
//        // Execute the method
//        CartDto result = cartService.addCart(storeId, cartDto);
//
//        // Verify the results
//        assertNotNull(result);
//        assertEquals(1L, result.getMenuId());
//        assertEquals(2, result.getCount());
//        assertEquals(20000, result.getTotalPrice());
//
//        // Verify that the necessary methods were called
//        verify(userRepository).findByUser("ghdgptn12");
//        verify(reservationRepository).findByPhone("010-1111-1111");
//        verify(storeRepository).findById(storeId);
//        verify(menuRepository).findById(1L);
//        verify(cartRepository).save(any(CartEntity.class));
//        verify(cartUtil).calculateItemPrice(any(CartEntity.class));
//        verify(cartUtil).isValidCart(any(CartEntity.class));
//        verify(cartMapper).toCartDto(any(CartEntity.class));
//    }
//}
