package com.cism.backend.controller.stall;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cism.backend.dto.common.Api;
import com.cism.backend.model.admin.StallModel;
import com.cism.backend.model.stalls.StallExpensesModel;
import com.cism.backend.repository.stalls.StallExpensesRepository;
import com.cism.backend.util.CurrentUserLicence;

@RestController
@RequestMapping("/api/stall/expense")
public class StallExpenseController {

    @Autowired
    private StallExpensesRepository stallExpensesRepository;

    @Autowired
    private CurrentUserLicence currentUserLicence;

    @GetMapping
    public ResponseEntity<Api<List<StallExpensesModel>>> getMyExpenses() {
        Long stallId = currentUserLicence.getStall().getId();
        List<StallExpensesModel> expenses = stallExpensesRepository.findByStallIdOrderByExpenseDateDesc(stallId);
        return ResponseEntity.ok(Api.ok("Expenses retrieved successfully", "EXPENSES_RETRIEVED", expenses));
    }

    @PostMapping
    public ResponseEntity<Api<StallExpensesModel>> addExpense(
            @RequestParam String name,
            @RequestParam BigDecimal amount,
            @RequestParam String category,
            @RequestParam(required = false) String date) {
        StallModel stall = currentUserLicence.getStall();
        Instant expDate = date != null && !date.trim().isEmpty() ? Instant.parse(date) : Instant.now();

        StallExpensesModel expense = StallExpensesModel.builder()
                .stall(stall)
                .name(name)
                .amount(amount)
                .category(category)
                .expenseDate(expDate)
                .createdAt(Instant.now())
                .build();

        StallExpensesModel saved = stallExpensesRepository.save(expense);
        return ResponseEntity.ok(Api.ok("Expense logged successfully", "EXPENSE_CREATED", saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Api<String>> deleteExpense(@PathVariable Long id) {
        stallExpensesRepository.deleteById(id);
        return ResponseEntity.ok(Api.ok("Expense log deleted successfully", "EXPENSE_DELETED", null));
    }
}
