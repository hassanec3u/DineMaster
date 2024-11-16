package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity(name = "Restaurant")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RestaurantEntity {


    //Un **restaurant** est caractérisé par :
    //
    //- un identifiant unique (un nombre entier positif)
    //- Un nom (longueur max de 90 caractères)
    //- Une adresse (longueur max de 255 caractères)
    //- Une liste d'**évaluations**
    //- une image présentant le restaurant

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "image")
    private String image;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.EAGER)
    private List<EvaluationEntity> evaluationEntity;



}
