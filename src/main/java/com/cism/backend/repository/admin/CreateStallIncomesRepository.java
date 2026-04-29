package com.cism.backend.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cism.backend.model.stalls.StallIncomesModel;

import com.cism.backend.model.admin.StallModel;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface CreateStallIncomesRepository extends JpaRepository<StallIncomesModel, Long> {
    
    @Query("SELECT SUM(i.income) FROM StallIncomesModel i WHERE i.stall = :stall AND i.earnedAt BETWEEN :start AND :end")
    Optional<BigDecimal> sumIncomeByStallAndDateRange(
        @Param("stall") StallModel stall, 
        @Param("start") Instant start, 
        @Param("end") Instant end
    );
}
