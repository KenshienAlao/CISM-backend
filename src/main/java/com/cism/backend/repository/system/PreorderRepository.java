package com.cism.backend.repository.system;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cism.backend.model.system.preorder.PreorderModel;

@Repository
public interface PreorderRepository extends JpaRepository<PreorderModel, Long> {
    List<PreorderModel> findByUser_Id(Long userId);
    Optional<PreorderModel> findByUser_IdAndItem_IdAndVariation_Id(Long userId, Long itemId, Long variationId);
    Optional<PreorderModel> findByUser_IdAndItem_IdAndVariationIsNull(Long userId, Long itemId);
    List<PreorderModel> findByItem_IdAndVariation_Id(Long itemId, Long variationId);
    List<PreorderModel> findByItem_IdAndVariationIsNull(Long itemId);
    void deleteByUser_IdAndItem_IdAndVariation_Id(Long userId, Long itemId, Long variationId);
    void deleteByUser_IdAndItem_IdAndVariationIsNull(Long userId, Long itemId);
}
