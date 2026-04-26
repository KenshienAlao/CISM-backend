package com.cism.backend.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cism.backend.dto.admin.CreateStallDto;
import com.cism.backend.dto.admin.CreateUserDto;
import com.cism.backend.dto.admin.StallListResponse;
import com.cism.backend.dto.common.Api;
import com.cism.backend.service.admin.CreateStallService;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.cism.backend.model.admin.StallModel;
import com.cism.backend.dto.admin.StallResponseDto;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/admin")
public class StallsController {
    
    private final CreateStallService createStallService;

    public StallsController(CreateStallService createStallService) {
        this.createStallService = createStallService;
    }

    @PostMapping("/create-stall")
    public ResponseEntity <Api<StallResponseDto>> createStall(@ModelAttribute CreateUserDto entity) throws IOException {

        StallModel stall = createStallService.createStall();
        CreateUserDto details = createStallService.createUserStall(entity, stall);

        CreateStallDto account = new CreateStallDto(stall.getPassword(), stall.getLicence());
        StallResponseDto response = new StallResponseDto(account, details);

        return ResponseEntity.ok(Api.ok("Stall created", "STALL_CREATED", response));
    }

    @GetMapping("/get-stalls")
    public ResponseEntity<Api<List<StallListResponse>>> getStalls() {

        List<StallListResponse> stallList = createStallService.getAllStalls();

        return ResponseEntity.ok(Api.ok("Stalls fetched", "STALLS_FETCHED", stallList));
    }
    
}   
