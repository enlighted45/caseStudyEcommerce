package com.caseStudy.E_commerce.Service;

import com.caseStudy.E_commerce.DTO.Favourite.FavouriteResponseDTO;
import com.caseStudy.E_commerce.Entity.Favourite;
import com.caseStudy.E_commerce.Entity.Product;
import com.caseStudy.E_commerce.Entity.User;
import com.caseStudy.E_commerce.ExceptionHandler.DuplicateResourceException;
import com.caseStudy.E_commerce.ExceptionHandler.ResourceNotFoundException;
import com.caseStudy.E_commerce.Mapper.FavouriteMapper;
import com.caseStudy.E_commerce.Repository.FavouriteRepository;
import com.caseStudy.E_commerce.Repository.ProductRepository;
import com.caseStudy.E_commerce.Repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavouriteService {

    private final FavouriteRepository favouriteRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final FavouriteMapper favouriteMapper;

    public FavouriteService(
            FavouriteRepository favouriteRepository,
            UserRepository userRepository,
            ProductRepository productRepository,
            FavouriteMapper favouriteMapper) {

        this.favouriteRepository = favouriteRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.favouriteMapper = favouriteMapper;
    }
    @Transactional
    public FavouriteResponseDTO addFavourite(
            Long userId,
            Long productId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User with Id : " + userId + " doesn't exist"
                        ));

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product with Id : " + productId + " doesn't exist"
                        ));

        boolean alreadyFavourite =
                favouriteRepository.existsByUserIdAndProductId(
                        userId,
                        productId
                );

        if (alreadyFavourite) {
            throw new DuplicateResourceException(
                    "Product is already in favourites"
            );
        }

        Favourite favourite = new Favourite();

        favourite.setUser(user);
        favourite.setProduct(product);

        favourite = favouriteRepository.save(favourite);

        return favouriteMapper.mapToResponseDTO(favourite);
    }

    @Transactional(readOnly = true)
    public Page<FavouriteResponseDTO> getFavouriteByUserId(Long userId, Pageable pageable){
        // check whether user exist or not

        userRepository.findById(userId).orElseThrow(
                ()->new ResourceNotFoundException("User with Id :" + userId + "Don't exist"));
        // pageable validator

        return favouriteRepository.
                findByUserId(userId,pageable).
                map(favouriteMapper::mapToResponseDTO);

    }

    @Transactional
    public void deleteFavourite(
            Long userId,
            Long productId) {

        Favourite favourite = favouriteRepository
                .findByUserIdAndProductId(userId, productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Favourite doesn't exist for User with Id : "
                                        + userId
                                        + " and Product with Id : "
                                        + productId
                        ));

        favouriteRepository.delete(favourite);
    }



}
