package com.fpt.ecoverseuser.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RowInput<T> {
    private final int excelRowNumber;
    private final T dto;
}
