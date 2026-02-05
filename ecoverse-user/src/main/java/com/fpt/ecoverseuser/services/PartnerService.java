package com.fpt.ecoverseuser.services;

import com.fpt.ecoverseuser.dtos.requests.PartnerRegisterRequestDto;
import com.fpt.ecoverseuser.dtos.requests.PartnerUpdateRequestDto;
import com.fpt.ecoverseuser.dtos.responses.PartnerResponseDto;

public interface PartnerService {
    PartnerResponseDto createPartner(PartnerRegisterRequestDto request);
    PartnerResponseDto getDetailPartner(String partnerId);
    PartnerResponseDto updatePartner(String partnerId, PartnerUpdateRequestDto request);
}
