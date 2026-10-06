package com.caseStudy.E_commerce.DTO.Favourite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class FavouriteResponseDTO {

    private Long id;
    private Long productId;
    private String productName;
}
