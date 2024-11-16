package com.example.demo.Controller;

import com.example.demo.dto.EvaluationDto;
import com.example.demo.dto.RestaurantDto;
import com.example.demo.entity.EvaluationEntity;
import com.example.demo.mapper.RestaurantMapper;
import com.example.demo.service.EvaluationService;
import com.example.demo.service.RestaurantsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("restaurant")
@RequiredArgsConstructor
public class RestaurantControler {

    private final RestaurantsService restaurantsService;
    private final EvaluationService evaluationService;


    //retourne la liste des restaurants
    @GetMapping()
    public List<RestaurantDto> getRestaurants() {
        return restaurantsService.getRestaurants().stream()
                .map(RestaurantMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }


    //retourne un restaurant en particulier
    @GetMapping("/{id}")
    public RestaurantDto getRestaurantById(@PathVariable Long id) {
        return RestaurantMapper.mapEntityToDto(restaurantsService.getRestaurantById(id));
    }

    //ajoute un restaurant
    @PostMapping
    public void addRestaurant(@Valid @RequestBody RestaurantDto restaurantDto) {
        restaurantsService.addRestaurant(restaurantDto.getNom(), restaurantDto.getAdresse(), restaurantDto.getImage());
    }


    //recuper l'url du cover de l'image d'un restaurant a uploader
    @GetMapping("/{id}/cover/urlToUpload")
    public String getCoverById(@PathVariable Long id) {
        return restaurantsService.getCoverByIdToUpload(id);
    }

    //Recuper l'url du cover de l'image d'un restaurant a telecharger
    @GetMapping("/{id}/cover/urlToDownload")
    public String getCoverByIdToDownload(@PathVariable Long id) {
        return restaurantsService.getCoverByIdToDownload(id);
    }


    //recuper l'url du cover de l'image d'un plat d'un restaurant a telecharger
    @GetMapping("/{id}/plat/{idPlat}/cover/urlToDownload")
    public String getCoverByIdToDownloadPlat(@PathVariable Long idRestau, @PathVariable Long idPlat) {
        return evaluationService.getCoverByIdToDownloadPlat(idRestau, idPlat);
    }

    //recuper l'url du cover de l'image d'un plat d'un restaurant a uploader
    @GetMapping("/{id}/plat/{idPlat}/cover/urlToUpload")
    public String getCoverByIdToUploadPlat(@PathVariable Long idRestau, @PathVariable Long idPlat) {
        return evaluationService.getCoverByIdToUploadPlat(idRestau, idPlat);
    }

    //met à jour un restaurant
    @PutMapping("/{id}")
    public void updateRestaurant(@PathVariable Long id, @Valid @RequestBody RestaurantDto restaurantDto) {
        restaurantsService.updateRestaurant(id, restaurantDto.getNom(), restaurantDto.getAdresse());
    }


    //Ajoute une évaluation sur un restaurant
    @PostMapping("/{id}/evaluation")
    public void addEvaluationToRestaurant( @Valid @PathVariable Long id, @RequestBody EvaluationDto evaluationDto) {
        evaluationService.addEvaluationToRestaurant(id, evaluationDto.getAuteur(),evaluationDto.getCommentaire(), evaluationDto.getNote());
    }

    //recuper les id des évaluations en fonction d'un mot clé
    @GetMapping("/evaluation/search")
    public List<String> getEvaluationByDescriptionIndexing( @RequestParam String keyword) {
        return evaluationService.getEvaluationsByKeyword( keyword);
    }

    //supprime une évaluation
    @DeleteMapping("/evaluation/{id}")
    public void deleteEvaluation(@PathVariable Long id) {
        evaluationService.deleteEvaluation(id);
    }



}
