package com.example.demo.service;

import com.example.demo.entity.RestaurantEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantsService {

    private final RestaurantRepository restaurantRepository;
    private final IndexService indexService;
    private final S3ServiceRestaurant s3ServiceRestaurant;

    public void addRestaurant(String nom, String adresse, String image) {
        var restaurant = RestaurantEntity.builder().nom(nom).adresse(adresse).image(image).build();
        this.restaurantRepository.save(restaurant);
    }


    public List<RestaurantEntity> getRestaurants() {
        return restaurantRepository.findAll();
    }

    public RestaurantEntity getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant with id " + id + " not found"));
    }

    public void updateRestaurant(Long id, String nom, String adresse) {
        var restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant with id " + id + " not found"));
        restaurant.setNom(nom);
        restaurant.setAdresse(adresse);
        restaurantRepository.save(restaurant);
    }


    public String getCoverByIdToUpload(Long id) {
        var restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant with id " + id + " not found"));
        var str = s3ServiceRestaurant.getUrlPostCoverByIDForRestaurant(restaurant.getId());
        restaurant.setImage(str);
        return str;
    }

    public String getCoverByIdToDownload(Long id) {
        var restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant with id " + id + " not found"));
        return s3ServiceRestaurant.getUrlGetCoverByIDForRestaurant(restaurant.getId());
    }

}