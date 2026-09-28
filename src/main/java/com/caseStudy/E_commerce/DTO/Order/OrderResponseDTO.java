package com.caseStudy.E_commerce.DTO.Order;

import com.caseStudy.E_commerce.DTO.OrderItem.OrderItemResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
    private Long id;

    private Integer totalQuantity;

    private BigDecimal totalAmount;

    private LocalDateTime createdAt;

    private Long userId;

    private List<OrderItemResponseDTO> items;
}
