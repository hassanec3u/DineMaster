package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "Le nom du restaurant est obligatoire")
    @Size(max = 90, message = "Le nom du restaurant ne doit pas dépasser 90 caractères")
    private String nom;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double moyenne;

    @NotBlank(message = "L'adresse du restaurant est obligatoire")
    @Size(max = 255, message = "L'adresse du restaurant ne doit pas dépasser 255 caractères")
    private String adresse;

    private String image;

    @JsonProperty("evaluationIds")
    private List<EvaluationDto> evaluationEntity;

}
