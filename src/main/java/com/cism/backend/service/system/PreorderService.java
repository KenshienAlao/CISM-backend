package com.cism.backend.service.system;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cism.backend.dto.system.preorder.PreorderRequest;
import com.cism.backend.dto.system.preorder.PreorderResponse;
import com.cism.backend.exception.BadrequestException;
import com.cism.backend.model.stalls.ItemVariationsModel;
import com.cism.backend.model.stalls.StallItemModel;
import com.cism.backend.model.system.preorder.PreorderModel;
import com.cism.backend.model.system.preorder.PreorderSuccessModel;
import com.cism.backend.model.users.AuthModel;
import com.cism.backend.repository.stalls.ItemvariationsRepository;
import com.cism.backend.repository.stalls.StallItemRepository;
import com.cism.backend.repository.system.PreorderRepository;
import com.cism.backend.repository.system.PreorderSuccessRepository;
import com.cism.backend.util.CurrentUserLicence;

import jakarta.transaction.Transactional;

@Service
public class PreorderService {

    @Autowired
    private PreorderRepository preorderRepository;

    @Autowired
    private PreorderSuccessRepository preorderSuccessRepository;

    @Autowired
    private StallItemRepository stallItemRepository;

    @Autowired
    private ItemvariationsRepository itemvariationsRepository;

    @Autowired
    private CurrentUserLicence currentUserLicence;

    @Transactional
    public PreorderResponse addPreorder(PreorderRequest request) {
        AuthModel user = currentUserLicence.getCurrentUser();

        StallItemModel item = stallItemRepository.findById(request.itemId())
                .orElseThrow(() -> new BadrequestException("Item not found", "ITEM_NOT_FOUND"));

        ItemVariationsModel variation = null;
        if (request.variationId() != null && request.variationId() != 0) {
            variation = itemvariationsRepository.findById(request.variationId())
                    .orElseThrow(() -> new BadrequestException("Variation not found", "VARIATION_NOT_FOUND"));
        }

        int currentStock = (variation != null)
                ? (variation.getStock() != null ? variation.getStock() : 0)
                : (item.getStocks() != null ? item.getStocks() : 0);

        Optional<PreorderModel> existing = (variation != null)
                ? preorderRepository.findByUser_IdAndItem_IdAndVariation_Id(user.getId(), item.getId(), variation.getId())
                : preorderRepository.findByUser_IdAndItem_IdAndVariationIsNull(user.getId(), item.getId());

        PreorderModel preorder;
        if (existing.isPresent()) {
            preorder = existing.get();
            preorder.setQuantity(request.quantity());
            preorder.setInitialStock(currentStock);
        } else {
            preorder = PreorderModel.builder()
                    .user(user)
                    .stall(item.getStall())
                    .item(item)
                    .variation(variation)
                    .initialStock(currentStock)
                    .quantity(request.quantity())
                    .build();
        }

        preorder = preorderRepository.save(preorder);
        return mapToPreorderResponse(preorder);
    }

    public List<PreorderResponse> getUserPreorders() {
        AuthModel user = currentUserLicence.getCurrentUser();
        return preorderRepository.findByUser_Id(user.getId())
                .stream()
                .map(this::mapToPreorderResponse)
                .toList();
    }

    public List<PreorderResponse> getUserPreordersSuccess() {
        AuthModel user = currentUserLicence.getCurrentUser();
        return preorderSuccessRepository.findByUser_Id(user.getId())
                .stream()
                .map(this::mapToSuccessResponse)
                .toList();
    }

    @Transactional
    public void deletePreorder(Long itemId, Long variationId) {
        AuthModel user = currentUserLicence.getCurrentUser();
        if (variationId != null && variationId != 0) {
            preorderRepository.deleteByUser_IdAndItem_IdAndVariation_Id(user.getId(), itemId, variationId);
            preorderSuccessRepository.deleteByUser_IdAndItem_IdAndVariation_Id(user.getId(), itemId, variationId);
        } else {
            preorderRepository.deleteByUser_IdAndItem_IdAndVariationIsNull(user.getId(), itemId);
            preorderSuccessRepository.deleteByUser_IdAndItem_IdAndVariationIsNull(user.getId(), itemId);
        }
    }

    @Transactional
    public void handleItemRestocked(Long itemId, Long variationId) {
        List<PreorderModel> activePreorders;
        if (variationId != null && variationId != 0) {
            activePreorders = preorderRepository.findByItem_IdAndVariation_Id(itemId, variationId);
        } else {
            activePreorders = preorderRepository.findByItem_IdAndVariationIsNull(itemId);
        }

        for (PreorderModel pre : activePreorders) {
            PreorderSuccessModel successModel = PreorderSuccessModel.builder()
                    .user(pre.getUser())
                    .stall(pre.getStall())
                    .item(pre.getItem())
                    .variation(pre.getVariation())
                    .initialStock(pre.getInitialStock())
                    .quantity(pre.getQuantity())
                    .build();
            preorderSuccessRepository.save(successModel);
        }

        if (!activePreorders.isEmpty()) {
            preorderRepository.deleteAll(activePreorders);
        }
    }

    private PreorderResponse mapToPreorderResponse(PreorderModel preorder) {
        String stallName = (preorder.getStall().getUserList() != null && !preorder.getStall().getUserList().isEmpty())
                ? preorder.getStall().getUserList().get(0).getName()
                : "Stall " + preorder.getStall().getLicence();

        BigDecimal price = preorder.getVariation() != null 
                ? preorder.getVariation().getPrice() 
                : preorder.getItem().getPrice();

        return new PreorderResponse(
                preorder.getId(),
                preorder.getItem().getId(),
                preorder.getItem().getName(),
                price,
                preorder.getVariation() != null ? preorder.getVariation().getId() : null,
                preorder.getVariation() != null ? preorder.getVariation().getName() : null,
                preorder.getStall().getId(),
                stallName,
                preorder.getInitialStock(),
                preorder.getQuantity(),
                preorder.getCreatedAt()
        );
    }

    private PreorderResponse mapToSuccessResponse(PreorderSuccessModel preorder) {
        String stallName = (preorder.getStall().getUserList() != null && !preorder.getStall().getUserList().isEmpty())
                ? preorder.getStall().getUserList().get(0).getName()
                : "Stall " + preorder.getStall().getLicence();

        BigDecimal price = preorder.getVariation() != null 
                ? preorder.getVariation().getPrice() 
                : preorder.getItem().getPrice();

        return new PreorderResponse(
                preorder.getId(),
                preorder.getItem().getId(),
                preorder.getItem().getName(),
                price,
                preorder.getVariation() != null ? preorder.getVariation().getId() : null,
                preorder.getVariation() != null ? preorder.getVariation().getName() : null,
                preorder.getStall().getId(),
                stallName,
                preorder.getInitialStock(),
                preorder.getQuantity(),
                preorder.getCreatedAt()
        );
    }
}
