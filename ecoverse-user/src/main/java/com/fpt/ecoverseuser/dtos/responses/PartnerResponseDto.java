package com.fpt.ecoverseuser.dtos.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fpt.ecoverseuser.dtos.StatisticPartner;
import com.fpt.ecoverseuser.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartnerResponseDto {

    @JsonProperty("partnership_id")
    private String partnershipId;

    @JsonProperty("organization_name")
    private String organizationName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("contact_person")
    private String contactPerson;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("address")
    private String address;

    @JsonProperty("status")
    private Status status;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @JsonProperty("statistics")
    private StatisticPartner statistics;
}
