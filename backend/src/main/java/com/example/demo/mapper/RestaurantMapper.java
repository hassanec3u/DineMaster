package com.example.demo.mapper;

import com.example.demo.dto.RestaurantDto;
import com.example.demo.entity.RestaurantEntity;

import java.util.stream.Collectors;

public class RestaurantMapper {

    public static RestaurantDto mapEntityToDto(RestaurantEntity restaurantEntity) {
        return RestaurantDto.builder()
                .id(restaurantEntity.getId())
                .nom(restaurantEntity.getNom())
                .moyenne(restaurantEntity.getMoyenne())
                .adresse(restaurantEntity.getAdresse())
                .evaluationEntity(restaurantEntity.getEvaluationEntity() != null ?
                        restaurantEntity.getEvaluationEntity().stream()
                                .map(EvaluationMapper::mapEntityToDto)
                                .collect(Collectors.toList()) : null).build();

    }

    public static RestaurantEntity mapDtoToEntity(RestaurantDto restaurantDto) {
       return RestaurantEntity.builder()
                .nom(restaurantDto.getNom())
                .adresse(restaurantDto.getAdresse())
                .build();
    }

}

