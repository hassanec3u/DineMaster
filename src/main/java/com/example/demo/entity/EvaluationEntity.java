package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "Evaluation")
public class EvaluationEntity {

    //- un identifiant unique (un nombre entier positif)
    //- Le nom de l'évaluateur (longueur max de 50 caractères)
    //- Le commentaire (longueur max de 255 caractères)
    //- Le nombre d'étoiles recommandé (0,1,2 ou 3) appellée "note"



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @Column(name = "auteur")
    private String auteur;

    @Column(name = "commentaire")
    private String commentaire;

    @Column(name = "note")
    private int note;

    @Column(name = "picture")
    private int picture;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private RestaurantEntity restaurant;

    public void sePlatImage(String str) {
    }



}
