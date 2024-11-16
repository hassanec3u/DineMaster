package com.example.demo.service;


import com.example.demo.entity.RestaurantEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestService {

    @InjectMocks
    private RestaurantsService restaurantsService;

    @Mock
    private RestaurantRepository restaurantRepository;


    @Test
    void testAddRestaurant_Ok() {
        var name = "Test Restaurant";
        var address = "Test Address";

        restaurantsService.addRestaurant(name, address, "Test Image");

        verify(restaurantRepository, times(1)).save(any());

    }

    @Test
    void testUpdateRestaurant_Ok() {
        var oldName = "Old Name";
        var newName = "Updated Name";

        var restaurantInDB = RestaurantEntity.builder().nom(oldName).id(1L).build();

        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(restaurantInDB));
        when(restaurantRepository.save(any())).thenReturn(restaurantInDB);

        restaurantsService.updateRestaurant(1L, newName, "New Address");

        assertEquals(newName, restaurantsService.getRestaurantById(1L).getNom());
        verify(restaurantRepository, times(1)).save(any());
    }

    @Test
    void testUpdateRestaurant_name_too_long() {
        var newName = "Very Very Very Very Very Very Very Very Very Very Very Very Very Very Very Very Very Very Very Long Restaurant Name";

        assertThrows(Exception.class, () -> restaurantsService.updateRestaurant(1L, newName, "New Address"));
    }

    @Test
    void testUpdateRestaurant_name_is_blank() {
        String newName = "";


        assertThrows(Exception.class, () -> restaurantsService.updateRestaurant(1L, newName, "New Address"));
    }

    @Test
    void testUpdateRestaurant_restaurant_not_found() {
        var newName = "New Name";

        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> restaurantsService.updateRestaurant(1L, newName, "New Address"));
    }

    @Test
    void testRestaurant_title() {
        var restaurantInDB = RestaurantEntity.builder().nom("Title Test Restaurant").id(1L).build();

        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(restaurantInDB));

        var result = restaurantsService.getRestaurantById(1L).getNom();

        assertEquals("Title Test Restaurant", result);
        verify(restaurantRepository, times(1)).findById(anyLong());
    }
    
}