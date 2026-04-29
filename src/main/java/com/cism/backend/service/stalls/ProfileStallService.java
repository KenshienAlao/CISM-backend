package com.cism.backend.service.stalls;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cism.backend.dto.stall.OwnerStallDto;
import com.cism.backend.exception.BadrequestException;
import com.cism.backend.model.admin.StallModel;
import com.cism.backend.model.stalls.StallDrinksModel;
import com.cism.backend.model.stalls.StallIncomesModel;
import com.cism.backend.model.stalls.StallMealsModel;
import com.cism.backend.model.stalls.StallSnacksModel;
import com.cism.backend.model.stalls.StallUsersModel;
import com.cism.backend.model.system.review.ReviewModel;
import com.cism.backend.repository.admin.CreateStallIncomesRepository;
import com.cism.backend.repository.admin.CreateStallRepository;
import com.cism.backend.util.CurrentUserLicence;

import jakarta.transaction.Transactional;

@Service
public class ProfileStallService {
    @Autowired
    CreateStallRepository createStallRepository;

    @Autowired
    CreateStallIncomesRepository createStallIncomesRepository;


    @Autowired
    CurrentUserLicence currentUserLicence;

    @Transactional
    public OwnerStallDto getUserService(){
        String licence = currentUserLicence.getCurrentUserLicence();

        StallModel stall = createStallRepository.findByLicence(licence).orElseThrow(() -> new BadrequestException("Stall not found", "STALL_NOT_FOUND"));

        return new OwnerStallDto(
            stall.getId(),
            stall.getUserList().stream().findFirst().map(this::mapUser).orElseThrow(() -> new BadrequestException("User not found", "USER_NOT_FOUND")),      
            stall.getMealList().stream().map(this::mapMeal).toList(),
            stall.getSnackList().stream().map(this::mapSnacks).toList(),
            stall.getDrinkList().stream().map(this::mapDrinks).toList(),
            stall.getReviewList().stream().map(this::mapReviews).toList(),
            stall.getIncomeList().stream().findFirst().map(this::mapIncomes).orElse(null),
            calculateRevenueTrend(stall)
        ); 
    }

    private OwnerStallDto.TrendDto calculateRevenueTrend(StallModel stall) {
        Instant now = Instant.now();
        Instant sevenDaysAgo = now.minus(java.time.Duration.ofDays(7));
        Instant fourteenDaysAgo = now.minus(java.time.Duration.ofDays(14));

        BigDecimal currentPeriod = createStallIncomesRepository
            .sumIncomeByStallAndDateRange(stall, sevenDaysAgo, now)
            .orElse(BigDecimal.ZERO);

        BigDecimal previousPeriod = createStallIncomesRepository
            .sumIncomeByStallAndDateRange(stall, fourteenDaysAgo, sevenDaysAgo)
            .orElse(BigDecimal.ZERO);

        double percentageChange = 0;
        String trend = "neutral";

        if (previousPeriod.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal diff = currentPeriod.subtract(previousPeriod);
            percentageChange = diff.divide(previousPeriod, 4, java.math.RoundingMode.HALF_UP).doubleValue() * 100;
        } else if (currentPeriod.compareTo(BigDecimal.ZERO) > 0) {
            percentageChange = 100.0;
        }

        if (percentageChange > 0) trend = "up";
        else if (percentageChange < 0) trend = "down";

        return new OwnerStallDto.TrendDto(
            currentPeriod,
            previousPeriod,
            Math.abs(percentageChange),
            trend
        );
    }

    private OwnerStallDto.UserModel mapUser(StallUsersModel u){
        return new OwnerStallDto.UserModel(
            u.getId(),
            u.getStall().getId(),
            u.getName(),
            u.getDescription(),
            u.getImage(),
            u.getStatus(),
            u.getOpenAt(),
            u.getCloseAt(),
            u.getCreatedAt(),
            u.getUpdatedAt()
        );
    }

    private OwnerStallDto.MealsModel mapMeal(StallMealsModel m){
        return new OwnerStallDto.MealsModel(
            m.getId(),
            m.getStall().getId(),
            m.getPrice(),
            m.getName(),
            m.getImage(),
            m.getStocks(),
            m.getSold(),
            m.getPreviousSold(),
            m.getCreatedAt(),
            m.getUpdatedAt()
        );
    }

    private OwnerStallDto.SnacksModel mapSnacks(StallSnacksModel s) {
        return new OwnerStallDto.SnacksModel(
            s.getId(),
            s.getStall().getId(),
            s.getPrice(),
            s.getName(),
            s.getImage(),
            s.getStocks(),
            s.getSold(),
            s.getPreviousSold(),
            s.getCreatedAt(),
            s.getUpdatedAt()
        );
    }

    private OwnerStallDto.DrinksModel mapDrinks(StallDrinksModel d) {
        return new OwnerStallDto.DrinksModel(
            d.getId(),
            d.getStall().getId(),
            d.getPrice(),
            d.getName(),
            d.getImage(),
            d.getStocks(),
            d.getSold(),
            d.getPreviousSold(),
            d.getCreatedAt(),
            d.getUpdatedAt()
        );
    }

    private OwnerStallDto.ReviewModel mapReviews(ReviewModel r) {
        return new OwnerStallDto.ReviewModel(
            r.getId(),
            r.getItemId(),
            r.getUsers().getId(),
            r.getStar(),
            r.getComment(),
            r.getCreateAt()
        );
    }

    private OwnerStallDto.IncomesModel mapIncomes(StallIncomesModel i) {
        return new OwnerStallDto.IncomesModel(
            i.getId(),
            i.getStall().getId(),
            i.getIncome(),
            i.getEarnedAt(),
            i.getCreatedAt()
        );
    }
}
