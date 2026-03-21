package com.fpt.ecoversewasteitem.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.ecoversewasteitem.dto.request.WasteItemRequest;
import com.fpt.ecoversewasteitem.dto.response.WasteItemResponse;
import com.fpt.ecoversewasteitem.services.WasteItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WasteItemController.class)
class WasteItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WasteItemService wasteItemService;

    @Autowired
    private ObjectMapper objectMapper;

    private WasteItemResponse sampleResponse;

    @BeforeEach
    void setUp() {
        sampleResponse = WasteItemResponse.builder()
                .id("wi001")
                .name("Vỏ chuối")
                .correctBinCode("ORGANIC")
                .correctBinName("Rác hữu cơ")
                .correctBinColor("#22C55E")
                .description("Phế phẩm thực phẩm")
                .createdBy("ADMIN")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void getAllWasteItems_shouldReturnList() throws Exception {
        List<WasteItemResponse> items = Arrays.asList(sampleResponse);
        when(wasteItemService.getAllWasteItems()).thenReturn(items);

        mockMvc.perform(get("/api/waste-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].name").value("Vỏ chuối"));
    }

    @Test
    void getWasteItemById_shouldReturnItem() throws Exception {
        when(wasteItemService.getWasteItemById("wi001")).thenReturn(sampleResponse);

        mockMvc.perform(get("/api/waste-items/wi001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Vỏ chuối"));
    }

    @Test
    void createWasteItem_shouldReturnCreated() throws Exception {
        WasteItemRequest request = new WasteItemRequest();
        request.setName("Vỏ chuối");
        request.setCorrectBinCode("ORGANIC");
        request.setDescription("Test");

        when(wasteItemService.createWasteItem(any())).thenReturn(sampleResponse);

        mockMvc.perform(post("/api/waste-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Vỏ chuối"));
    }
}
