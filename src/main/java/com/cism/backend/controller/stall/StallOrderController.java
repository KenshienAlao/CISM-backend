package com.cism.backend.controller.stall;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cism.backend.dto.common.Api;
import com.cism.backend.dto.system.order.OrderResponse;
import com.cism.backend.service.system.OrderService;

@RestController
@RequestMapping("/api/stall/order")
public class StallOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/stall/{stallId}")
    public ResponseEntity<Api<List<OrderResponse>>> getStallOrders(@PathVariable Long stallId) {
        List<OrderResponse> success = orderService.getStallOrders(stallId);
        return ResponseEntity.ok(Api.ok("Stall orders retrieved successfully", "STALL_ORDERS_RETRIEVED", success));
    }

    @PostMapping("/update-status/{id}")
    public ResponseEntity<Api<OrderResponse>> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        OrderResponse success = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(Api.ok("Order status updated successfully", "ORDER_STATUS_UPDATED", success));
    }
}
