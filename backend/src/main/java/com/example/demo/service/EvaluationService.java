package com.example.demo.service;

import com.example.demo.dto.EvaluationDto;
import com.example.demo.entity.EvaluationEntity;
import com.example.demo.entity.RestaurantEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.EvaluationMapper;
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


    //ajouter une évaluation sur un un restaurant
    public void addEvaluationToRestaurant(EvaluationDto evaluationDto) {
        RestaurantEntity restaurantEntity = restaurantRepository.findById(evaluationDto.getRestauId()).orElseThrow(() -> new NotFoundException("restaurant with id " + evaluationDto.getRestauId() + " not found"));

        EvaluationEntity evaluation = EvaluationMapper.mapDtoToEntity(evaluationDto);
        evaluation.setRestaurant(restaurantEntity);

        //calcul la moyenne des notes du restaurant
        double moyenne = evaluationDto.getNote();
        for (EvaluationEntity e : evaluationRepository.findAll()) {
            if (e.getRestaurant().getId() == evaluationDto.getRestauId()) {
                moyenne += e.getNote();
            }
        }
        moyenne = moyenne / (evaluationRepository.findAll().size() + 1);

        //troncature à 2 chiffres après la virgule
        moyenne = Math.round(moyenne * 100.0) / 100.0;

        restaurantEntity.setMoyenne(moyenne);


        restaurantRepository.save(restaurantEntity);
        evaluationRepository.save(evaluation);
         indexService.indexReview(evaluation.getId(), evaluation.getCommentaire());
    }

    //récupérer les évaluations en fonction de l'id d'un restaurant
    public List<EvaluationEntity> getEvaluationsByRestaurantId(Long RestaurantId) {
        List<EvaluationEntity> evaluations = new ArrayList<>();
        for (EvaluationEntity evaluation : evaluationRepository.findAll()) {
            if (evaluation.getRestaurant().getId() == RestaurantId) {
                evaluations.add(evaluation);
            }
        }
        return evaluations;
    }

    //supprimer une évaluation
    public void deleteEvaluation(Long id) {
        evaluationRepository.deleteById(id);
    }


    //récupérer les évaluations en fonction d'un (ou plusieurs) mots clefs
    public List<String> getEvaluationsByKeyword(String keyword) {

        List<String> idFound =  indexService.searchReviewsByKeywords(keyword);

        //retrouve les évaluations en fonction des id trouvés, et renvoie des list contenant du contenu du comentaire avec son autreur
        List<String> evaluations = new ArrayList<>();
        for (String id : idFound) {
            EvaluationEntity evaluation = evaluationRepository.findById(Long.parseLong(id)).orElseThrow(() -> new NotFoundException("Evaluation with id " + id + " not found"));
            evaluations.add(evaluation.getAuteur() + " : " + evaluation.getCommentaire());
        }
        return evaluations;
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
