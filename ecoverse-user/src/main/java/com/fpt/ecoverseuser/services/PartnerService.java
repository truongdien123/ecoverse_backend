package com.fpt.ecoverseuser.services;

import com.fpt.ecoverseuser.dtos.requests.PartnerRegisterRequestDto;
import com.fpt.ecoverseuser.dtos.requests.PartnerUpdateRequestDto;
import com.fpt.ecoverseuser.dtos.responses.BulkCreateReportResponse;
import com.fpt.ecoverseuser.dtos.responses.PartnerResponseDto;
import com.fpt.ecoverseuser.dtos.responses.StudentResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface PartnerService {
    PartnerResponseDto createPartner(PartnerRegisterRequestDto request);
    PartnerResponseDto getDetailPartner(String partnerId);
    PartnerResponseDto updatePartner(String partnerId, PartnerUpdateRequestDto request);
    BulkCreateReportResponse bulkCreate(MultipartFile file, String partnerId);
    StudentResponseDto getStudentDetail(String partnerId, String studentId);
}
