package com.fpt.ecoversewasteitem.services;

import com.fpt.ecoversecommon.exception.BadRequestException;
import com.fpt.ecoversecommon.exception.NotFoundException;
import com.fpt.ecoversewasteitem.dto.request.WasteItemRequest;
import com.fpt.ecoversewasteitem.dto.response.WasteItemResponse;
import com.fpt.ecoversewasteitem.entities.WasteBin;
import com.fpt.ecoversewasteitem.entities.WasteItem;
import com.fpt.ecoversewasteitem.repositories.WasteBinRepository;
import com.fpt.ecoversewasteitem.repositories.WasteItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WasteItemServiceTest {

    @Mock
    private WasteItemRepository wasteItemRepository;

    @Mock
    private WasteBinRepository wasteBinRepository;

    @InjectMocks
    private WasteItemService wasteItemService;

    private WasteItem sampleItem;
    private WasteBin sampleBin;

    @BeforeEach
    void setUp() {
        sampleBin = new WasteBin();
        sampleBin.setId("wb001");
        sampleBin.setCode("ORGANIC");
        sampleBin.setDisplayName("Rác hữu cơ");
        sampleBin.setColorHex("#22C55E");

        sampleItem = new WasteItem();
        sampleItem.setId("wi001");
        sampleItem.setName("Vỏ chuối");
        sampleItem.setCorrectBinCode("ORGANIC");
        sampleItem.setDescription("Phế phẩm thực phẩm");
        sampleItem.setCreatedBy("ADMIN");
    }

    @Test
    void getAllWasteItems_shouldReturnList() {
        when(wasteItemRepository.findAll()).thenReturn(Arrays.asList(sampleItem));
        when(wasteBinRepository.findByCode("ORGANIC")).thenReturn(Optional.of(sampleBin));

        List<WasteItemResponse> result = wasteItemService.getAllWasteItems();

        assertEquals(1, result.size());
        assertEquals("Vỏ chuối", result.get(0).getName());
    }

    @Test
    void getWasteItemById_shouldReturnItem() {
        when(wasteItemRepository.findById("wi001")).thenReturn(Optional.of(sampleItem));
        when(wasteBinRepository.findByCode("ORGANIC")).thenReturn(Optional.of(sampleBin));

        WasteItemResponse result = wasteItemService.getWasteItemById("wi001");

        assertEquals("Vỏ chuối", result.getName());
        assertEquals("Rác hữu cơ", result.getCorrectBinName());
    }

    @Test
    void getWasteItemById_shouldThrowNotFound() {
        when(wasteItemRepository.findById("invalid")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> wasteItemService.getWasteItemById("invalid"));
    }

    @Test
    void createWasteItem_shouldCreateSuccessfully() {
        WasteItemRequest request = new WasteItemRequest();
        request.setName("Vỏ chuối");
        request.setCorrectBinCode("ORGANIC");
        request.setDescription("Test");
        request.setCreatedBy("ADMIN");

        when(wasteBinRepository.existsByCode("ORGANIC")).thenReturn(true);
        when(wasteItemRepository.save(any())).thenReturn(sampleItem);
        when(wasteBinRepository.findByCode("ORGANIC")).thenReturn(Optional.of(sampleBin));

        WasteItemResponse result = wasteItemService.createWasteItem(request);

        assertNotNull(result);
        assertEquals("Vỏ chuối", result.getName());
    }

    @Test
    void createWasteItem_shouldThrowBadRequest_invalidBinCode() {
        WasteItemRequest request = new WasteItemRequest();
        request.setName("Test");
        request.setCorrectBinCode("INVALID");
        request.setCreatedBy("ADMIN");

        when(wasteBinRepository.existsByCode("INVALID")).thenReturn(false);

        assertThrows(BadRequestException.class, () -> wasteItemService.createWasteItem(request));
    }

    @Test
    void deleteWasteItem_shouldDeleteSuccessfully() {
        when(wasteItemRepository.findById("wi001")).thenReturn(Optional.of(sampleItem));

        wasteItemService.deleteWasteItem("wi001");

        verify(wasteItemRepository, times(1)).delete(sampleItem);
    }
}
