//import com.sparta.spring26.domain.cart.dto.CartListDto;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//
//@Service
//public class CartService {
//
//    public CartListDto addCart(CartDto cartDto, long storeId, String storeName, HttpSession session) {
//        CartListDto cartListDto = (CartListDto) session.getAttribute("cartList");
//
//        cartDto.totalPriceCalc();
//        // Session에 저장된 장바구니 목록이 없을 시
//        if (cartListDto == null) {
//            List<CartDto> newCart = new ArrayList<>();
//            newCart.add(cartDto);
//            cartListDto = new CartListDto(storeId, storeName, cartDto.getTotalPrice(), newCart);
//        } else {
//            List<CartDto> prevCart = cartListDto.getCartDto();
//            int prevCartTotal = cartListDto.getCartTotal();
//            cartListDto.setCartTotal(prevCartTotal + cartDto.getTotalPrice());
//
//            // 이미 장바구니에 추가된 메뉴일 때
//            if (prevCart.contains(cartDto)) {
//                int cartIndex = prevCart.indexOf(cartDto);
//                int amount = cartDto.getAmount();
//
//                CartDto newCart = prevCart.get(cartIndex);
//                int newAmount = newCart.getAmount() + amount;
//
//                newCart.setAmount(newAmount);
//                newCart.totalPriceCalc();
//                prevCart.set(cartIndex, newCart);
//            } else { // 장바구니에 추가되어 있지 않은 메뉴일 때
//                prevCart.add(cartDto);
//            }
//        }
//
//        session.setAttribute("cartList", cartListDto);
//        return cartListDto;
//    }
//
//    public CartListDto getCartList(HttpSession session) {
//        return (CartListDto) session.getAttribute("cartList");
//    }
//
//    public void deleteAllCart(HttpSession session) {
//        session.removeAttribute("cartList");
//    }
//
//    public CartListDto deleteOneCart(int index, HttpSession session) {
//        CartListDto cartList = (CartListDto) session.getAttribute("cartList");
//        if (cartList == null) {
//            return null;
//        }
//
//        int cartTotal = cartList.getCartTotal();
//        List<CartDto> cart = cartList.getCartDto();
//        int removeCartPrice = cart.get(index).getTotalPrice();
//
//        cart.remove(index);
//
//        if (cart.size() == 0) {
//            session.removeAttribute("cartList");
//            return null;
//        }
//
//        cartTotal -= removeCartPrice;
//        cartList.setCartTotal(cartTotal);
//        return cartList;
//    }
//}