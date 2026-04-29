package com.cism.backend.controller.stall;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cism.backend.dto.common.Api;
import com.cism.backend.dto.stall.RequestItemDto;
import com.cism.backend.dto.stall.ResponseItemDto;
import com.cism.backend.service.stalls.DrinkStallService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/owner/stall/drink")
public class DrinkStallController {


    @Autowired
    private DrinkStallService drinkStallService;

    @PostMapping("/create-new-drink")
    public ResponseEntity<Api<ResponseItemDto>> createNewDrink(@ModelAttribute RequestItemDto entity) throws Exception {

        ResponseItemDto success = drinkStallService.createNewDrinkService(entity);

        return ResponseEntity.ok(Api.ok("Create new drink success", "CREATE_DRINK_SUCCESS", success));
    }

    @PutMapping("/update-drink/{id}")
    public ResponseEntity<Api<ResponseItemDto>> updateDrink(@PathVariable Long id, @ModelAttribute RequestItemDto entity) throws Exception {
        ResponseItemDto success = drinkStallService.updateDrinkService(id, entity);
        return ResponseEntity.ok(Api.ok("Update drink success", "UPDATE_DRINK_SUCCESS", success));
    
    }

    @DeleteMapping("/delete-drink/{id}")
    public ResponseEntity<Api<String>> deleteDrink(@PathVariable Long id) throws Exception {
        drinkStallService.deleteDrinkService(id);
        return ResponseEntity.ok(Api.ok("Drink successfully deleted", "SUCCESS_DELETED", null));
    }


    
}

