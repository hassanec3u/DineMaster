package com.example.demo.mapper;

import com.example.demo.dto.EvaluationDto;
import com.example.demo.entity.EvaluationEntity;

public class EvaluationMapper {


    public static EvaluationDto mapEntityToDto(EvaluationEntity evaluationEntity) {
        return EvaluationDto.builder()
                .auteur(evaluationEntity.getAuteur())
                .note(evaluationEntity.getNote())
                .commentaire(evaluationEntity.getCommentaire())
                .build();
    }


    public static EvaluationEntity mapDtoToEntity(EvaluationDto evaluationDto) {
        return EvaluationEntity.builder()
                .auteur(evaluationDto.getAuteur())
                .note(evaluationDto.getNote())
                .commentaire(evaluationDto.getCommentaire())
                .build();
    }

}