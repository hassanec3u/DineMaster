package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Random;


@Entity(name = "Restaurant")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RestaurantEntity {


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


    @OneToMany(mappedBy = "restaurant", fetch = FetchType.EAGER,cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<EvaluationEntity> evaluationEntity;

    @Column(name = "moyenne")
    private double moyenne;

}
