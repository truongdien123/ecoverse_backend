package com.fpt.ecoversewasteitem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WasteItemRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Correct bin code is required")
    @Size(max = 50, message = "Bin code must not exceed 50 characters")
    private String correctBinCode;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private String imageUrl;

    private String createdBy;
}
