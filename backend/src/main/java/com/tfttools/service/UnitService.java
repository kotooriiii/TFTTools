package com.tfttools.service;

import com.tfttools.domain.Unit;
import com.tfttools.dto.UnitDTO;
import com.tfttools.mapper.UnitMapper;
import com.tfttools.registry.UnitRegistry;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Business logic for Units
 */
@Service
public class UnitService {

    private final UnitRegistry unitRegistry;
    private final UnitMapper unitMapper;

    public UnitService(UnitRegistry unitRegistry, UnitMapper unitMapper) {
        this.unitRegistry = unitRegistry;
        this.unitMapper = unitMapper;
    }

    /**
     * Gets all units from {@link UnitRegistry} sanitizes it for the requestor
     * @return List of {@link UnitDTO}
     */
    public List<UnitDTO> getAllUnits() {
        return unitRegistry.getAllUnits().stream().map(unitMapper).collect(Collectors.toList());
    }
}
