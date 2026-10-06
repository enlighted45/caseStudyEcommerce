package com.caseStudy.E_commerce.Controller;


import com.caseStudy.E_commerce.DTO.Favourite.FavouriteResponseDTO;
import com.caseStudy.E_commerce.Service.FavouriteService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/favourites")
@PreAuthorize("@userAuthorizationService.isCurrentUser(#userId)")
public class FavouriteController {

    private final FavouriteService favouriteService;


    public FavouriteController(FavouriteService favouriteService) {
        this.favouriteService = favouriteService;
    }

    @PostMapping("/{productId}")
    public ResponseEntity<FavouriteResponseDTO> addFavourite(
            @PathVariable Long userId,
            @PathVariable Long productId) {

        FavouriteResponseDTO response =
                favouriteService.addFavourite(userId, productId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteFavourite(
            @PathVariable Long userId,
            @PathVariable Long productId) {

        favouriteService.deleteFavourite(userId, productId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<FavouriteResponseDTO>> getFavourites(
            @PathVariable Long userId,
            @ParameterObject Pageable pageable) {

        Page<FavouriteResponseDTO> response =
                favouriteService.getFavouriteByUserId(userId, pageable);

        return ResponseEntity.ok(response);
    }
}
