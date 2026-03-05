package com.fpt.ecoversewasteitem.controllers;

import com.fpt.ecoversecommon.dto.ApiResponse;
import com.fpt.ecoversewasteitem.dto.request.WasteItemRequest;
import com.fpt.ecoversewasteitem.dto.response.WasteItemResponse;
import com.fpt.ecoversewasteitem.services.WasteItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/waste-items")
public class WasteItemController {

    @Autowired
    private WasteItemService wasteItemService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<WasteItemResponse>>> getAllWasteItems(
            @RequestParam(value = "binCode", required = false) String binCode,
            @RequestParam(value = "search", required = false) String search) {

        List<WasteItemResponse> items;

        if (binCode != null && !binCode.isEmpty()) {
            items = wasteItemService.getWasteItemsByBinCode(binCode);
        } else if (search != null && !search.isEmpty()) {
            items = wasteItemService.searchWasteItemsByName(search);
        } else {
            items = wasteItemService.getAllWasteItems();
        }

        return ResponseEntity.ok(ApiResponse.success("Waste items retrieved successfully", items));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WasteItemResponse>> getWasteItemById(@PathVariable("id") String id) {
        WasteItemResponse item = wasteItemService.getWasteItemById(id);
        return ResponseEntity.ok(ApiResponse.success("Waste item retrieved successfully", item));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<WasteItemResponse>> createWasteItem(
            @Valid @RequestBody WasteItemRequest request) {
        WasteItemResponse item = wasteItemService.createWasteItem(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Waste item created successfully", item));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<WasteItemResponse>> updateWasteItem(
            @PathVariable("id") String id,
            @Valid @RequestBody WasteItemRequest request) {
        WasteItemResponse item = wasteItemService.updateWasteItem(id, request);
        return ResponseEntity.ok(ApiResponse.success("Waste item updated successfully", item));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteWasteItem(@PathVariable("id") String id) {
        wasteItemService.deleteWasteItem(id);
        return ResponseEntity.ok(ApiResponse.success("Waste item deleted successfully", null));
    }
}
