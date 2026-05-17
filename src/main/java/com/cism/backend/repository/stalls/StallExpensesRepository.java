package com.cism.backend.repository.stalls;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cism.backend.model.stalls.StallExpensesModel;
import com.cism.backend.model.admin.StallModel;

@Repository
public interface StallExpensesRepository extends JpaRepository<StallExpensesModel, Long> {
    
    List<StallExpensesModel> findByStallIdOrderByExpenseDateDesc(Long stallId);
    
    List<StallExpensesModel> findByStall(StallModel stall);
}
