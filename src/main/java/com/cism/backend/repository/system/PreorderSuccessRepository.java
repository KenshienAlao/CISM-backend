package com.cism.backend.repository.system;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cism.backend.model.system.preorder.PreorderSuccessModel;

@Repository
public interface PreorderSuccessRepository extends JpaRepository<PreorderSuccessModel, Long> {
    List<PreorderSuccessModel> findByUser_Id(Long userId);
    Optional<PreorderSuccessModel> findByUser_IdAndItem_IdAndVariation_Id(Long userId, Long itemId, Long variationId);
    Optional<PreorderSuccessModel> findByUser_IdAndItem_IdAndVariationIsNull(Long userId, Long itemId);
    void deleteByUser_IdAndItem_IdAndVariation_Id(Long userId, Long itemId, Long variationId);
    void deleteByUser_IdAndItem_IdAndVariationIsNull(Long userId, Long itemId);
}
