package com.cism.backend.service.stalls;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cism.backend.dto.stall.RequestItemDto;
import com.cism.backend.dto.stall.ResponseItemDto;
import com.cism.backend.exception.BadrequestException;
import com.cism.backend.exception.UnauthorizedException;
import com.cism.backend.model.admin.StallModel;
import com.cism.backend.model.stalls.StallDrinksModel;
import com.cism.backend.repository.admin.CreateStallRepository;
import com.cism.backend.repository.stalls.StallDrinksRepository;
import com.cism.backend.util.CurrentUserLicence;
import com.cism.backend.util.IsBlack;

import jakarta.transaction.Transactional;

@Service
public class DrinkStallService {
    @Autowired
    private CurrentUserLicence currentUserLicence;

    @Autowired
    private CreateStallRepository createStallRepository;

    @Autowired
    private StallDrinksRepository stallDrinksRepository;

    @Autowired
    private IsBlack isBlack;

    @Transactional
    public ResponseItemDto createNewDrinkService(RequestItemDto entity) throws Exception {
        String licence = currentUserLicence.getCurrentUserLicence();
        StallModel stall = createStallRepository.findByLicence(licence).orElseThrow(() -> new BadrequestException("Stall not found", "STALL_NOT_FOUND"));
        

        if (isBlack.isBlank(entity.name()) || isBlack.isBlankBigDecimal(entity.price())) {
            throw new BadrequestException("Please fill in all the fields", "FILL_ALL_FIELDS");
        }

        StallDrinksModel drink = StallDrinksModel.builder()
            .stall(stall)
            .name(entity.name())
            .price(entity.price())
            .stocks(entity.stocks())
            .image(entity.image())
            .sold(0)
            .previousSold(0)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();


        StallDrinksModel savedDrink = stallDrinksRepository.save(drink);

        return mapToResponseDto(savedDrink);
    }

    @Transactional
    public ResponseItemDto updateDrinkService(Long id, RequestItemDto entity) throws Exception {
        StallDrinksModel drink = helperByIdAndEntity(id, entity);

        drink.setName(entity.name());
        drink.setPrice(entity.price());
        drink.setStocks(entity.stocks());
        drink.setImage(entity.image());
        drink.setUpdatedAt(Instant.now());

        StallDrinksModel updatedDrink = stallDrinksRepository.save(drink);

        return mapToResponseDto(updatedDrink);
    }

    @Transactional
    public ResponseItemDto deleteDrinkService(Long id) throws Exception {
        StallDrinksModel drink = helperById(id);
        stallDrinksRepository.delete(drink);
        return mapToResponseDto(drink);
    }




    private StallDrinksModel helperById(Long id) throws Exception {
        String licence = currentUserLicence.getCurrentUserLicence();
        StallModel stall = createStallRepository.findByLicence(licence).orElseThrow(() -> new BadrequestException("Stall not found", "STALL_NOT_FOUND"));
        StallDrinksModel drink = stallDrinksRepository.findById(id).orElseThrow(() -> new BadrequestException("Drink not found", "DRINK_NOT_FOUND"));

        if (stall.getId() != drink.getStall().getId()) {
            throw new UnauthorizedException("You are not authorized", "UNAUTHORIZED");
        }

        return drink;
    }

    private StallDrinksModel helperByIdAndEntity(Long id, RequestItemDto entity) throws Exception {
        StallDrinksModel drink = helperById(id);

        if (isBlack.isBlank(entity.name()) || isBlack.isBlankBigDecimal(entity.price())) {
            throw new BadrequestException("Please fill in all the fields", "FILL_ALL_FIELDS");
        }

        return drink;
    }

    private ResponseItemDto mapToResponseDto(StallDrinksModel entity) {
        return new ResponseItemDto(
            entity.getId(),
            entity.getStall().getId(),
            entity.getName(),
            entity.getPrice(),
            entity.getImage(),
            entity.getStocks(),
            entity.getSold(),
            entity.getPreviousSold(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
