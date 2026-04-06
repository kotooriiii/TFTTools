package com.tfttools.engine.engine_terminator;

import com.tfttools.domain.Composition;

public interface EngineTerminator {
    boolean shouldTerminate(Composition comp);
}
