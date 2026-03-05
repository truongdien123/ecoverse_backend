package com.fpt.ecoversewasteitem.services;

import com.fpt.ecoversecommon.exception.BadRequestException;
import com.fpt.ecoversecommon.exception.NotFoundException;
import com.fpt.ecoversewasteitem.dto.request.WasteItemRequest;
import com.fpt.ecoversewasteitem.dto.response.WasteItemResponse;
import com.fpt.ecoversewasteitem.entities.WasteBin;
import com.fpt.ecoversewasteitem.entities.WasteItem;
import com.fpt.ecoversewasteitem.repositories.WasteBinRepository;
import com.fpt.ecoversewasteitem.repositories.WasteItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WasteItemService {

    @Autowired
    private WasteItemRepository wasteItemRepository;

    @Autowired
    private WasteBinRepository wasteBinRepository;

    public List<WasteItemResponse> getAllWasteItems() {
        List<WasteItem> items = wasteItemRepository.findAll();
        return items.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<WasteItemResponse> getWasteItemsByBinCode(String binCode) {
        List<WasteItem> items = wasteItemRepository.findByCorrectBinCode(binCode);
        return items.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<WasteItemResponse> searchWasteItemsByName(String name) {
        List<WasteItem> items = wasteItemRepository.findByNameContainingIgnoreCase(name);
        return items.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public WasteItemResponse getWasteItemById(String id) {
        WasteItem item = wasteItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Waste item not found with id: " + id));
        return toResponse(item);
    }

    public WasteItemResponse createWasteItem(WasteItemRequest request) {
        validateBinCode(request.getCorrectBinCode());
        validateCreatedBy(request.getCreatedBy());

        WasteItem item = new WasteItem();
        item.setName(request.getName());
        item.setCorrectBinCode(request.getCorrectBinCode());
        item.setDescription(request.getDescription());
        item.setImageUrl(request.getImageUrl());
        item.setCreatedBy(request.getCreatedBy() != null ? request.getCreatedBy() : "PARTNERSHIP");

        WasteItem savedItem = wasteItemRepository.save(item);
        return toResponse(savedItem);
    }

    public WasteItemResponse updateWasteItem(String id, WasteItemRequest request) {
        WasteItem item = wasteItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Waste item not found with id: " + id));

        validateBinCode(request.getCorrectBinCode());

        item.setName(request.getName());
        item.setCorrectBinCode(request.getCorrectBinCode());
        item.setDescription(request.getDescription());
        item.setImageUrl(request.getImageUrl());

        if (request.getCreatedBy() != null) {
            validateCreatedBy(request.getCreatedBy());
            item.setCreatedBy(request.getCreatedBy());
        }

        WasteItem updatedItem = wasteItemRepository.save(item);
        return toResponse(updatedItem);
    }

    public void deleteWasteItem(String id) {
        WasteItem item = wasteItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Waste item not found with id: " + id));
        wasteItemRepository.delete(item);
    }

    private void validateBinCode(String binCode) {
        if (!wasteBinRepository.existsByCode(binCode)) {
            throw new BadRequestException("Invalid bin code: " + binCode
                    + ". Valid codes are: ORGANIC, RECYCLE, INORGANIC, HAZARDOUS");
        }
    }

    private void validateCreatedBy(String createdBy) {
        if (createdBy != null && !createdBy.equals("PARTNERSHIP") && !createdBy.equals("ADMIN")) {
            throw new BadRequestException("createdBy must be either 'PARTNERSHIP' or 'ADMIN'");
        }
    }

    private WasteItemResponse toResponse(WasteItem item) {
        String binDisplayName = null;
        String binColorHex = null;
        WasteBin bin = item.getWasteBin();
        if (bin != null) {
            binDisplayName = bin.getDisplayName();
            binColorHex = bin.getColorHex();
        } else {
            var binOpt = wasteBinRepository.findByCode(item.getCorrectBinCode());
            if (binOpt.isPresent()) {
                binDisplayName = binOpt.get().getDisplayName();
                binColorHex = binOpt.get().getColorHex();
            }
        }

        return WasteItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .correctBinCode(item.getCorrectBinCode())
                .correctBinName(binDisplayName)
                .correctBinColor(binColorHex)
                .description(item.getDescription())
                .imageUrl(item.getImageUrl())
                .createdBy(item.getCreatedBy())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }
}
