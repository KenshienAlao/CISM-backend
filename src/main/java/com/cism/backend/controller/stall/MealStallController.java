package com.cism.backend.controller.stall;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cism.backend.dto.common.Api;
import com.cism.backend.dto.stall.RequestItemDto;
import com.cism.backend.dto.stall.ResponseItemDto;
import com.cism.backend.service.stalls.MealStallService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/owner/stall/meal")
public class MealStallController {


    @Autowired
    private MealStallService mealStallService;
    
    @PostMapping("/create-new-meal")
    public ResponseEntity<Api<ResponseItemDto>> createNewMeal(@ModelAttribute RequestItemDto entity) throws Exception {

        System.out.println("Rendered: " + entity);

        ResponseItemDto success = mealStallService.createNewMealService(entity);

        return ResponseEntity.ok(Api.ok("Create new meal success", "CREATE_MEAL_SUCCESS", success));
    }

    @PutMapping("/update-meal/{id}")
    public ResponseEntity<Api<ResponseItemDto>> updateMeal(@PathVariable Long id, @ModelAttribute RequestItemDto entity) throws Exception {
        ResponseItemDto success = mealStallService.updateMealService(id, entity);
        return ResponseEntity.ok(Api.ok("Update meal success", "UPDATE_MEAL_SUCCESS", success));
    
    }

    @DeleteMapping("/delete-meal/{id}")
    public ResponseEntity<Api<String>> deleteMeal(@PathVariable Long id) throws Exception {
        mealStallService.deleteMealService(id);
        return ResponseEntity.ok(Api.ok("Meal successfully deleted", "SUCCESS_DELETED", null));
    }

}
