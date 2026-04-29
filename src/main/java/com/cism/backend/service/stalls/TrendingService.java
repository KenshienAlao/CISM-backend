package com.cism.backend.service.stalls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cism.backend.repository.stalls.StallDrinksRepository;
import com.cism.backend.repository.stalls.StallMealsRepository;
import com.cism.backend.repository.stalls.StallSnacksRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TrendingService {

    @Autowired
    private StallMealsRepository stallMealsRepository;

    @Autowired
    private StallDrinksRepository stallDrinksRepository;

    @Autowired
    private StallSnacksRepository stallSnacksRepository;

    /**
     * Snapshots the current 'sold' count into 'previousSold' and resets 'sold' to 0.
     * This is scheduled to run once a week (every Sunday at midnight).
     * Cron: 0 0 0 * * SUN
     */
    @Scheduled(cron = "0 0 0 * * SUN")
    @Transactional
    public void snapshotWeeklySales() {
        log.info("Starting weekly sales snapshot...");

        // Snapshot Meals
        stallMealsRepository.findAll().forEach(meal -> {
            meal.setPreviousSold(meal.getSold());
            meal.setSold(0);
            stallMealsRepository.save(meal);
        });

        // Snapshot Drinks
        stallDrinksRepository.findAll().forEach(drink -> {
            drink.setPreviousSold(drink.getSold());
            drink.setSold(0);
            stallDrinksRepository.save(drink);
        });

        // Snapshot Snacks
        stallSnacksRepository.findAll().forEach(snack -> {
            snack.setPreviousSold(snack.getSold());
            snack.setSold(0);
            stallSnacksRepository.save(snack);
        });

        log.info("Weekly sales snapshot completed.");
    }

    /**
     * Manual snapshot for testing purposes.
     */
    @Transactional
    public void manualSnapshot() {
        snapshotWeeklySales();
    }
}
