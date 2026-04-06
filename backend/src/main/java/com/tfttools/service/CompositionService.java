package com.tfttools.service;

import com.tfttools.adapter.EngineConfigurationAdapter;
import com.tfttools.domain.Composition;
import com.tfttools.domain.EngineConfiguration;
import com.tfttools.dto.CompositionDTO;
import com.tfttools.dto.HorizontalDTO;
import com.tfttools.engine.TFTEngine;
import com.tfttools.mapper.CompositionMapper;
import com.tfttools.registry.UnitRegistry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompositionService {

    private final EngineConfigurationAdapter adapter;
    private final CompositionMapper compositionMapper;
    private final UnitRegistry unitRegistry;

    public CompositionService(EngineConfigurationAdapter adapter, CompositionMapper compositionMapper, UnitRegistry unitRegistry)
    {
        this.adapter = adapter;
        this.compositionMapper = compositionMapper;
        this.unitRegistry = unitRegistry;
    }

    public List<CompositionDTO> generateCompositions(HorizontalDTO horizontalDTO) {
        // Adapter handles validation and conversion
        EngineConfiguration config = adapter.adaptToEngineConfiguration(horizontalDTO);

        TFTEngine engine = new TFTEngine(config, unitRegistry.getAllUnits());

        return engine.buildCompositions().stream().map(compositionMapper).toList();
    }
}