package com.fpt.ecoverseuser.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartnerUpdateRequestDto {

    private String organizationName;
    private String phoneNumber;
    private String contactPerson;
    private MultipartFile avatar;
}
