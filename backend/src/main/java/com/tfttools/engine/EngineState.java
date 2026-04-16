package com.tfttools.engine;

import com.tfttools.domain.*;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public class EngineState
{
    private final Composition currentComp;
    private final EngineConfiguration engineConfiguration;

    public EngineState(Composition comp, EngineConfiguration engineConfiguration)
    {
        this.currentComp = comp;
        this.engineConfiguration = engineConfiguration;
    }
}
