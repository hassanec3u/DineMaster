package com.example.demo.mapper;

import com.example.demo.dto.RestaurantDto;
import com.example.demo.entity.RestaurantEntity;

import java.util.stream.Collectors;

public class RestaurantMapper {

    public static RestaurantDto mapEntityToDto(RestaurantEntity restaurantEntity) {
        return RestaurantDto.builder()
                .nom(restaurantEntity.getNom())
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

/*    public static SerieDto mapSerieToDto(SerieEntity serieEntity) {
        return SerieDto.builder()
                .description(serieEntity.getDescription())
                .jaquette(serieEntity.getJaquette())
                .dateSortie(serieEntity.getDate_sortie())
                .commentIds(serieEntity.getCommentEntity() != null ?
                        serieEntity.getCommentEntity().stream()
                                .map(CommentMapper::mapEntityToDto)
                                .collect(Collectors.toList()) : null)
                .build();
    }


    public static SerieEntity mapDtoToSerie(SerieDto serieDto) {
        return SerieEntity.builder()
                .description(serieDto.getDescription())
                .jaquette(serieDto.getJaquette())
                .date_sortie(serieDto.getDateSortie())
                .build();
    }*/
}
