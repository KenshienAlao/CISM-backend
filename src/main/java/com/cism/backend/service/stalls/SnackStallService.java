package com.cism.backend.service.stalls;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cism.backend.dto.stall.RequestItemDto;
import com.cism.backend.dto.stall.ResponseItemDto;
import com.cism.backend.exception.BadrequestException;
import com.cism.backend.model.admin.StallModel;
import com.cism.backend.model.stalls.StallSnacksModel;
import com.cism.backend.repository.admin.CreateStallRepository;
import com.cism.backend.repository.stalls.StallSnacksRepository;
import com.cism.backend.util.CurrentUserLicence;
import com.cism.backend.util.IsBlack;

import jakarta.transaction.Transactional;

@Service
public class SnackStallService {
    @Autowired
    private CreateStallRepository createStallRepository;

    @Autowired
    private CurrentUserLicence currentUserLicence;

    @Autowired
    private StallSnacksRepository stallSnacksRepository;


    @Autowired
    private IsBlack isBlack;
    


    @Transactional
    public ResponseItemDto createNewSnackService(RequestItemDto entity) {
        String lincence = currentUserLicence.getCurrentUserLicence();
        StallModel stall = createStallRepository.findByLicence(lincence).orElseThrow(() -> new BadrequestException("Stall not found", "STALL_NOT_FOUND"));

        if (isBlack.isBlank(entity.name()) || isBlack.isBlankBigDecimal(entity.price())) {
            throw new BadrequestException("Snack name and price is required", "REQUIRED");
        } 


        StallSnacksModel snack = StallSnacksModel.builder()
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
            
        StallSnacksModel savedSnack = stallSnacksRepository.save(snack);

        return mapToResponseDto(savedSnack);
    }

    @Transactional
    public ResponseItemDto updateSnackService(Long id, RequestItemDto entity) throws Exception {
        StallSnacksModel snack = helperByIdAndEntity(id, entity);

        snack.setName(entity.name());
        snack.setPrice(entity.price());
        snack.setStocks(entity.stocks());
        snack.setImage(entity.image());
        snack.setUpdatedAt(Instant.now());

        StallSnacksModel updatedSnack = stallSnacksRepository.save(snack);

       return mapToResponseDto(updatedSnack);
    }

    @Transactional
    public ResponseItemDto deleteSnackService(Long id) throws Exception {
        
        StallSnacksModel snack = helperById(id);
        stallSnacksRepository.delete(snack);

        return mapToResponseDto(snack);
    }



    private StallSnacksModel helperById(Long id) throws Exception {
        String licence = currentUserLicence.getCurrentUserLicence();
        StallModel stall = createStallRepository.findByLicence(licence).orElseThrow(() -> new BadrequestException("Stall not found", "STALL_NOT_FOUND"));
        StallSnacksModel snack = stallSnacksRepository.findById(id).orElseThrow(() -> new BadrequestException("Snack not found", "SNACK_NOT_FOUND"));
        
        if (snack.getStall().getId() != stall.getId()) {
            throw new BadrequestException("You do not have permission to update this snack", "UNAUTHORIZED");
        }
        return snack;
    }

    private StallSnacksModel helperByIdAndEntity(Long id, RequestItemDto entity) throws Exception {
        StallSnacksModel snack = helperById(id);
        
        if (isBlack.isBlank(entity.name()) || isBlack.isBlankBigDecimal(entity.price())) {
            throw new BadrequestException("Snack name and price is required", "REQUIRED");
        }
        return snack;
    }

    private ResponseItemDto mapToResponseDto(StallSnacksModel entity) {
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




// drinks crud 