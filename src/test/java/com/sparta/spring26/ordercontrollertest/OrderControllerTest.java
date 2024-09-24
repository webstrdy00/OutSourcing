//package com.sparta.spring26.ordercontrollertest;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.sparta.spring26.domain.order.OrderStatus;
//import com.sparta.spring26.domain.order.controller.OrderController;
//import com.sparta.spring26.domain.order.dto.request.OrderCreateRequestDto;
//import com.sparta.spring26.domain.order.dto.response.OrderResponseDto;
//import com.sparta.spring26.domain.order.service.OrderService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.util.ReflectionTestUtils;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(value = OrderController.class)
//public class OrderControllerTest {
//
//    @InjectMocks
//    private OrderController orderController;
//
//    @MockBean
//    private OrderService orderService;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
//        objectMapper = new ObjectMapper();
//    }
//
//    @Test
//    void 주문_등록_성공() throws Exception {
//        // Given
//        OrderCreateRequestDto requestDto = new OrderCreateRequestDto();
//        ReflectionTestUtils.setField(requestDto, "totalPrice", 12000);
//
//        OrderResponseDto responseDto = new OrderResponseDto(
//                1L,  // orderId
//                1L,  // menuId
//                1L,  // restaurantId
//                "Test Restaurant", // restaurantName
//                12000, // totalAmount
//                "123 Test Street", // address
//                OrderStatus.ORDER_RECEIVED // status
//        );
//
//
//        when(orderService.createOrder(any(OrderCreateRequestDto.class))).thenReturn(responseDto);
//
//        // When & Then
//        mockMvc.perform(MockMvcRequestBuilders.post("/delivery/order")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.orderId").value(1L))
//                .andExpect(jsonPath("$.menuId").value(1L))
//                .andExpect(jsonPath("$.restaurantId").value(1L))
//                .andExpect(jsonPath("$.restaurantName").value("Test Restaurant"))
//                .andExpect(jsonPath("$.totalAmount").value(12000))
//                .andExpect(jsonPath("$.address").value("123 Test Street"))
//                .andExpect(jsonPath("$.status").value(OrderStatus.ORDER_RECEIVED.toString()));
//    }
//}
