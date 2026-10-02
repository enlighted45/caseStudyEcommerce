package com.caseStudy.E_commerce.Controller;


import com.caseStudy.E_commerce.DTO.Order.OrderRequestDTO;
import com.caseStudy.E_commerce.DTO.Order.OrderResponseDTO;
import com.caseStudy.E_commerce.Service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/orders")
public class OrderController {


    private OrderService orderService;

    public  OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(
            @PathVariable("userId") Long userId ,
            @RequestBody @Valid OrderRequestDTO orderRequestDTO
            ){
        OrderResponseDTO orderResponseDTO = orderService.
                createOrder(userId,orderRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> getOrderHistory
            (@PathVariable("userId") Long userId, Pageable pageable){

        Page<OrderResponseDTO> pageResponse = orderService.
                getOrderHistory(userId,pageable);
        return ResponseEntity.status(HttpStatus.OK).body(pageResponse);

    }
    // now what to do
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderById
    (@PathVariable("userId") Long userId,
     @PathVariable("orderId") Long orderId){

        OrderResponseDTO orderResponseDTO = orderService.
                getOrderById(userId,orderId);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDTO);

    }

}
