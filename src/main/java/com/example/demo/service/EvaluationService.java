package com.example.demo.service;

import com.example.demo.entity.EvaluationEntity;
import com.example.demo.entity.RestaurantEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.EvaluationRepository;
import com.example.demo.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final RestaurantRepository restaurantRepository;
    private final S3ServicePlat s3ServiceRestaurant;
    private final IndexService indexService;

    //TODO
    //- La possibilité de récupérer les évaluations en fonction d'un (ou plusieurs) mots clefs


    //ajouter une évaluation sur un un restaurant
    public void addEvaluationToRestaurant(Long restaurantID, String auteur, String commentaire, int note) {
        RestaurantEntity restaurantEntity = restaurantRepository.findById(restaurantID).orElseThrow(() -> new NotFoundException("restaurant with id " + restaurantID + " not found"));
        EvaluationEntity evaluation = EvaluationEntity.builder()
                .auteur(auteur)
                .commentaire(commentaire)
                .note(note)
                .restaurant(restaurantEntity)
                .build();

        evaluationRepository.save(evaluation);
        indexService.indexReview(evaluation.getId(), commentaire);
    }

    //supprimer une évaluation
    public void deleteEvaluation(Long id) {
        evaluationRepository.deleteById(id);
    }


    //récupérer les évaluations en fonction d'un (ou plusieurs) mots clefs
    public List<String> getEvaluationsByKeyword(String keyword) {

        List<String> evaluationIds = indexService.searchReviewsByKeywords(keyword);

        return evaluationIds;
        //cast to long
        //  List<Long> evaluationIdsLong = new ArrayList<>();
        //for (String id : evaluationIds) {
//evaluationIdsLong.add(Long.parseLong(id));
        //  }

       // System.out.println("result: " + evaluationIds);

        //renvoie la liste des évaluations correspondant aux ids trouvés sous forme de Entity
        //   return evaluationRepository.findAllById(evaluationIdsLong);
    }

    public String getCoverByIdToDownloadPlat(Long idRestau, Long idPlat) {
        var evaluation = evaluationRepository.findById(idPlat)
                .orElseThrow(() -> new NotFoundException("Restaurant with id " + idPlat + " not found"));
        var str = s3ServiceRestaurant.getUrlPostCoverByIDForPlat(evaluation.getId());
        evaluation.sePlatImage(str);
        return str;
    }

    public String getCoverByIdToUploadPlat(Long idRestau, Long idPlat) {
        var restaurant = restaurantRepository.findById(idPlat)
                .orElseThrow(() -> new NotFoundException("Restaurant with id " + idPlat + " not found"));
        return s3ServiceRestaurant.getUrlGetCoverByIDForPlat(restaurant.getId());
    }

}
