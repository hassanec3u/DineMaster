package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Builder
@AllArgsConstructor
public class EvaluationDto {

    //on renvoie l'id de l'évaluation
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "L'id de l'évaluation est obligatoire")
    private Long RestauId;

    @NotBlank(message = "Le nom de l'auteur est obligatoire")
    private String auteur;

    @NotBlank(message = "La note est obligatoire")
    @Size(min = 0, max = 3, message = "La note doit être entre 0 et 3")
    private Integer note;

    @NotBlank(message = "Le commentaire est obligatoire")
    @Size(max = 255, message = "Le commentaire ne doit pas dépasser 255 caractères")
    private String commentaire;


}