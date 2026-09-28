package com.caseStudy.E_commerce.Mapper;

import com.caseStudy.E_commerce.DTO.OrderItem.OrderItemRequestDTO;
import com.caseStudy.E_commerce.DTO.OrderItem.OrderItemResponseDTO;
import com.caseStudy.E_commerce.Entity.Order;
import com.caseStudy.E_commerce.Entity.OrderItem;
import com.caseStudy.E_commerce.Entity.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderItemMapper {

    public OrderItem mapToEntity(
            OrderItemRequestDTO request,
            Product product,
            Order order) {

        OrderItem orderItem = new OrderItem();

        orderItem.setProduct(product);
        orderItem.setOrder(order);
        orderItem.setQuantity(request.getQuantity());

        // Store the product price at the time of ordering
        orderItem.setPrice((product.getPrice()).
                multiply(BigDecimal.
                        valueOf(request.getQuantity())));

        return orderItem;
    }

    public OrderItemResponseDTO mapToResponseDTO(OrderItem orderItem) {

        OrderItemResponseDTO orderItemResponseDTO = new OrderItemResponseDTO();

        orderItemResponseDTO.setId(orderItem.getId());
        orderItemResponseDTO.setProductId(orderItem.getProduct().getId());
        orderItemResponseDTO.setProductName(orderItem.getProduct().getName());
        orderItemResponseDTO.setQuantity((int) orderItem.getQuantity());
        orderItemResponseDTO.setPrice(orderItem.getPrice());

        return orderItemResponseDTO;
    }

}
