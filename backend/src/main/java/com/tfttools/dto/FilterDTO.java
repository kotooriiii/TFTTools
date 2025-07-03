package com.tfttools.dto;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class FilterDTO {
    private List<ChampionDTO> champions = new ArrayList<>();
    private List<TraitDTO> traits = new ArrayList<>();
}
