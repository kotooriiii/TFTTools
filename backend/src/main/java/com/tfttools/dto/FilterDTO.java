package com.tfttools.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class FilterDTO {
    private List<String> units = new ArrayList<>();
    private List<String> traits = new ArrayList<>();
}
