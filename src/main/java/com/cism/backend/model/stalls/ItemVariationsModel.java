package com.cism.backend.model.stalls;

import java.math.BigDecimal;
import java.time.Instant;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "item_variations")
public class ItemVariationsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "item_id", nullable = false)
    @JsonBackReference
    private StallItemModel stallitem;

    private String name;

    private BigDecimal price;

    private Integer stock;

    private String image;
    
    @Builder.Default
    private Integer sold = 0;
    
    @Builder.Default
    private Integer previousSold = 0;

    private Instant createdAt;
    private Instant updatedAt;

}
