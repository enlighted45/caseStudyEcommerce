package com.caseStudy.E_commerce.Mapper;

import com.caseStudy.E_commerce.DTO.Favourite.FavouriteResponseDTO;
import com.caseStudy.E_commerce.Entity.Favourite;
import org.springframework.stereotype.Component;

@Component
public class FavouriteMapper {

    public FavouriteResponseDTO mapToResponseDTO(Favourite favourite) {

        FavouriteResponseDTO dto = new FavouriteResponseDTO();

        dto.setId(favourite.getId());
        dto.setProductId(favourite.getProduct().getId());
        dto.setProductName(favourite.getProduct().getName());

        return dto;
    }
}
