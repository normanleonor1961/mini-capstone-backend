package norman.backendminicapstone.service;

import norman.backendminicapstone.dto.PopularDTO;
import norman.backendminicapstone.entity.PopularEntity;
import norman.backendminicapstone.exception.UserAlreadyExist;
import norman.backendminicapstone.model.PopularRequest;
import norman.backendminicapstone.repository.PopularRepository;
import norman.backendminicapstone.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PopularService {

    private final PopularRepository popularRepository;
    private final ModelMapper modelMapper;
    private final DateTimeUtil dateTimeUtil;

    public List<PopularDTO> getAllPopularProducts() {

        // Get all data from database
        List<PopularEntity> allPopularProducts = popularRepository.findAll(Sort.by(Sort.Direction.ASC, "createdDate"));

        // Initialize dto
        List<PopularDTO> allPopularProductsDTO = new ArrayList<>();

        allPopularProducts.forEach(product -> {
            allPopularProductsDTO.add(modelMapper.map(product, PopularDTO.class));
        });

        return allPopularProductsDTO;
    }

    public List<PopularDTO> addPopular(PopularRequest newPopular) {

        // Save new blog to database
        popularRepository.save(PopularEntity
                .builder()
                .productId(UUID.randomUUID())
                .productName(newPopular.getProductName())
                .imageLink(null)
                .price(newPopular.getPrice())
                .type(newPopular.getType())
                .createdDate(dateTimeUtil.currentDate())
                .modifiedDate(dateTimeUtil.currentDate())
                .build());

        return getAllPopularProducts();
    }

    public List<PopularDTO> deletePopular(UUID productId) {

        // Get popular product
        PopularEntity popular = popularRepository.findByProductId(productId);

        // Check if popular product exist
        if(popular == null) throw new UserAlreadyExist("Popular product doesn't exist");

        // Delete popular product
        popularRepository.deleteByProductId(productId);

        return getAllPopularProducts();
    }
}

