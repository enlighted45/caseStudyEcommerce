package com.caseStudy.E_commerce.Mapper;

import com.caseStudy.E_commerce.DTO.Order.OrderRequestDTO;
import com.caseStudy.E_commerce.DTO.Order.OrderResponseDTO;
import com.caseStudy.E_commerce.Entity.Order;
import com.caseStudy.E_commerce.Entity.User;
import com.caseStudy.E_commerce.Enum.OrderStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class OrderMapper {

    public OrderItemMapper orderItemMapper;

    public OrderMapper(OrderItemMapper orderItemMapper){
        this.orderItemMapper = orderItemMapper;
    }

    public Order mapToEntity(OrderRequestDTO orderRequestDTO, User user) {
        Order order = new Order();
        // id totalQuantity , totalAmount , User ,  OrderItem,  CreatedAt , OrderStatus;
        order.setTotalQuantity(0);
        order.setTotalAmount(BigDecimal.ZERO);
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PLACED);
        return order;
    }

    public OrderResponseDTO mapToResponseDTO(Order order){
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setCreatedAt(order.getCreatedAt());
        orderResponseDTO.setId(order.getId());
        orderResponseDTO.setTotalAmount(order.getTotalAmount());
        orderResponseDTO.setTotalQuantity(order.getTotalQuantity());
        orderResponseDTO.setUserId(order.getUser().getId());
        orderResponseDTO.setItems(
            order.getOrderItems().stream().
                    map(
                            orderItem -> orderItemMapper.mapToResponseDTO(orderItem)
                    ).
                    toList()
        );
        return orderResponseDTO;
    }

}
