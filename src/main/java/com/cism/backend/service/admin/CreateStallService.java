package com.cism.backend.service.admin;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cism.backend.dto.admin.CreateUserDto;
import com.cism.backend.dto.admin.StallListResponse;
import com.cism.backend.exception.BadrequestException;
import com.cism.backend.model.admin.StallModel;
import com.cism.backend.model.stalls.StallIncomesModel;
import com.cism.backend.model.stalls.StallUsersModel;
import com.cism.backend.repository.admin.CreateStallIncomesRepository;
import com.cism.backend.repository.admin.CreateStallRepository;
import com.cism.backend.repository.admin.CreateStallUsersRepository;
import com.cism.backend.service.users.FileStorageService;

import jakarta.transaction.Transactional;

@Service
public class CreateStallService {

    
    @Autowired
    private CreateStallRepository createStallRepository;

    @Autowired
    private CreateStallUsersRepository createStallUsersRepository;

    @Autowired
    private CreateStallIncomesRepository createStallIncomesRepository;



    @Autowired
    private FileStorageService fileStorageService;

    CreateStallService(CreateStallUsersRepository createStallUsersRepository, FileStorageService fileStorageService) {
        this.createStallUsersRepository = createStallUsersRepository;
        this.fileStorageService = fileStorageService;
    }
    
    @Transactional
    public CreateUserDto createUserStall(CreateUserDto entity, StallModel stall) throws IOException {

        String name = entity.name();
        String description = entity.description();
        MultipartFile image = entity.image();
        String openAt = entity.openAt();
        String closeAt = entity.closeAt();

        if (isBlank(name) || isBlank(openAt) || isBlank(closeAt)) {
            throw new BadrequestException("All fields are required", "ALL_FIELDS_REQUIRED");
        }

        String stallImage = null;
        if (!isBlankMultipartFile(image)) {
            stallImage = fileStorageService.stallImage(image);
        }

        StallUsersModel response = StallUsersModel.builder()
                .stall(stall)
                .name(name)
                .description(description)
                .image(stallImage)
                .openAt(openAt)
                .closeAt(closeAt)
                .status(false)
                .build();

        StallIncomesModel res = StallIncomesModel.builder()
                .stall(stall)
                .income(BigDecimal.ZERO)
                .earnedAt(Instant.now())
                .createdAt(Instant.now())
                .build();

        createStallUsersRepository.save(response);
        createStallIncomesRepository.save(res);
        
        return new CreateUserDto(name, description, image, openAt, closeAt);
    }


    @Transactional  
    public StallModel createStall(){

        String licence = String.format("LIC-%06d", new Random().nextInt(1000000));
        String password = String.format("STALL-%06d", new Random().nextInt(1000000));
     

        StallModel stall = StallModel.builder()
            .licence(licence)
            .password(password)
            .build();

        return createStallRepository.save(stall);
    }

    @Transactional
    public List<StallListResponse> getAllStalls() {
        return createStallRepository.findAll().stream().map(stall -> {
            
            StallListResponse.UserModel userDto = stall.getUserList().stream().findFirst()
                .map(u -> new StallListResponse.UserModel(
                    u.getId(), stall.getId(), u.getName(), u.getDescription(), 
                    u.getImage(), u.getStatus(), u.getOpenAt(), u.getCloseAt(), 
                    u.getCreatedAt(), u.getUpdatedAt()
                )).orElse(null);

            StallListResponse.IncomesModel incomeDto = stall.getIncomeList().stream().findFirst()
                .map(i -> new StallListResponse.IncomesModel(
                    i.getId(), stall.getId(), i.getIncome(), i.getEarnedAt(), i.getCreatedAt()
                )).orElse(null);

                
            return new StallListResponse(
                stall.getId(), 
                stall.getLicence(), 
                stall.getPassword(), 
                userDto, 
                incomeDto
            );
        }).toList();
    }



    public boolean isBlank(String entity){
        return entity == null || entity.isBlank();
    }

    public boolean isBlankMultipartFile(MultipartFile entity){
        return entity == null || entity.isEmpty();
    }

}
