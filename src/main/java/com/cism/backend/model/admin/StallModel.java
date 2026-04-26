package com.cism.backend.model.admin;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.cism.backend.model.stalls.StallDrinksModel;
import com.cism.backend.model.stalls.StallIncomesModel;
import com.cism.backend.model.stalls.StallMealsModel;
import com.cism.backend.model.stalls.StallSnacksModel;
import com.cism.backend.model.stalls.StallUsersModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "stall")
public class StallModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private long id;

    @OneToMany(mappedBy = "stall", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<StallMealsModel> mealList;

    @OneToMany(mappedBy = "stall", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<StallDrinksModel> drinkList;

    @OneToMany(mappedBy = "stall", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<StallSnacksModel> snackList;

    @OneToMany(mappedBy = "stall", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<StallUsersModel> userList;

    @OneToMany(mappedBy = "stall", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<StallIncomesModel> incomeList;

    @Column(unique = true, nullable = false) private String licence;
    @Column(unique = true, nullable = false) private String password;
    
    @Column(nullable = false) @CreationTimestamp private Instant createdAt;
    
}
