package com.example.demo.Controller;

import com.example.demo.dto.EvaluationDto;
import com.example.demo.mapper.EvaluationMapper;
import com.example.demo.service.EvaluationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("restaurant/{id}/evaluation")
@RequiredArgsConstructor
public class EvaluationController{

    private final EvaluationService evaluationService;


    //Ajoute une évaluation sur un restaurant
    @PostMapping("")
    public void addEvaluationToRestaurant( @RequestBody EvaluationDto evaluationDto) {
            evaluationService.addEvaluationToRestaurant(evaluationDto);
    }

    //supprime une évaluation
    @DeleteMapping("/{evaluationId}")
    public void deleteEvaluation(@PathVariable Long evaluationId) {
        evaluationService.deleteEvaluation(evaluationId);
    }

    //Recupere les évaluations en fonction de l'id d'un restaurant
    @GetMapping("")
    public List<EvaluationDto> getEvaluationByRestaurantId(@PathVariable Long id) {
        return evaluationService.getEvaluationsByRestaurantId(id).stream()
                .map(EvaluationMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    //recupere les id des évaluations en fonction d'un mot clé avec l'indexation lucene
    @GetMapping("/search")
    public List<String> getEvaluationByDescriptionIndexing(@RequestParam String keyword) {
        return evaluationService.getEvaluationsByKeyword( keyword);
    }

}