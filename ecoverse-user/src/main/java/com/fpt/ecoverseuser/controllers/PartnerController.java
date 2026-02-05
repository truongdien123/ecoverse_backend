package com.fpt.ecoverseuser.controllers;

import com.fpt.ecoversecommon.dto.ApiResponse;
import com.fpt.ecoverseuser.dtos.requests.PartnerRegisterRequestDto;
import com.fpt.ecoverseuser.dtos.requests.PartnerUpdateRequestDto;
import com.fpt.ecoverseuser.dtos.responses.PartnerResponseDto;
import com.fpt.ecoverseuser.services.PartnerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/partnerships")
public class PartnerController {
    private final PartnerService partnerService;

    public PartnerController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> createPartner(@Valid @ModelAttribute PartnerRegisterRequestDto request) {
        PartnerResponseDto response = partnerService.createPartner(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Create partner successfully", response));
    }

    @GetMapping("/{partnership_id}")
    public ResponseEntity<ApiResponse<?>> getDetailPartner(@PathVariable("partnership_id") String partnerId) {
        PartnerResponseDto response = partnerService.getDetailPartner(partnerId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Get detail partner successfully", response));
    }

    @PutMapping(value = "{partnership_id}/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> updatePartner(@PathVariable("partnership_id") String partnerId, @ModelAttribute PartnerUpdateRequestDto request) {
        PartnerResponseDto response = partnerService.updatePartner(partnerId, request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Update partner successfully", response));
    }
}
