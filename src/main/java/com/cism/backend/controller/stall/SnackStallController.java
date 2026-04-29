package com.cism.backend.controller.stall;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cism.backend.dto.common.Api;
import com.cism.backend.dto.stall.RequestItemDto;
import com.cism.backend.dto.stall.ResponseItemDto;
import com.cism.backend.service.stalls.SnackStallService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/owner/stall/snack")
public class SnackStallController {
    

    @Autowired
    private SnackStallService snackStallService;


    @PostMapping("/create-new-snack")
    public ResponseEntity <Api<ResponseItemDto>> createNewSnack(@ModelAttribute RequestItemDto entity) throws Exception {
        ResponseItemDto success = snackStallService.createNewSnackService(entity); 

        return ResponseEntity.ok(Api.ok("Success Creating Snack", "SUCCESS_CREATING_SNACK", success));
    }
    

    @PutMapping("/update-snack/{id}")
    public ResponseEntity<Api<ResponseItemDto>> updateSnack(@PathVariable Long id, @ModelAttribute RequestItemDto entity) throws Exception {
        ResponseItemDto success = snackStallService.updateSnackService(id, entity);
        return ResponseEntity.ok(Api.ok("Update snack success", "UPDATE_SNACK_SUCCESS", success));
    }

    @DeleteMapping("/delete-snack/{id}")
    public ResponseEntity<Api<String>> deleteSnack(@PathVariable Long id) throws Exception {
        snackStallService.deleteSnackService(id);
        return ResponseEntity.ok(Api.ok("Delete snack success", "DELETE_SNACK_SUCCESS", null));
    }   
}
