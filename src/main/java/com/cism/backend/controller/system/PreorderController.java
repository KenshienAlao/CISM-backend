package com.cism.backend.controller.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cism.backend.dto.common.Api;
import com.cism.backend.dto.system.preorder.PreorderRequest;
import com.cism.backend.dto.system.preorder.PreorderResponse;
import com.cism.backend.service.system.PreorderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customer/preorder")
public class PreorderController {

    @Autowired
    private PreorderService preorderService;

    @PostMapping("/add")
    public ResponseEntity<Api<PreorderResponse>> addPreorder(@Valid @RequestBody PreorderRequest request) {
        PreorderResponse success = preorderService.addPreorder(request);
        return ResponseEntity.ok(Api.ok("Pre-order added successfully", "PREORDER_ADDED", success));
    }

    @GetMapping("/my-preorders")
    public ResponseEntity<Api<List<PreorderResponse>>> getMyPreorders() {
        List<PreorderResponse> success = preorderService.getUserPreorders();
        return ResponseEntity.ok(Api.ok("Pre-orders retrieved successfully", "PREORDERS_RETRIEVED", success));
    }

    @GetMapping("/my-preorders-success")
    public ResponseEntity<Api<List<PreorderResponse>>> getMyPreordersSuccess() {
        List<PreorderResponse> success = preorderService.getUserPreordersSuccess();
        return ResponseEntity
                .ok(Api.ok("Successful Pre-orders retrieved successfully", "PREORDERS_SUCCESS_RETRIEVED", success));
    }

    @DeleteMapping("/delete/{itemId}")
    public ResponseEntity<Api<String>> deletePreorder(
            @PathVariable Long itemId,
            @RequestParam(required = false) Long variationId) {
        preorderService.deletePreorder(itemId, variationId);
        return ResponseEntity.ok(Api.ok("Pre-order deleted successfully", "PREORDER_DELETED", "SUCCESS"));
    }
}
