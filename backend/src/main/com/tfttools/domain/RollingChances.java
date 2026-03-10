package com.tfttools.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum RollingChances {

    PROBABILITIES(new HashMap<>(Map.of(
            1, List.of(1f, 0f, 0f, 0f, 0f),
            2, List.of(1f, 0f, 0f, 0f, 0f),
            3, List.of(0.75f, 0.25f, 0f, 0f, 0f),
            4, List.of(0.55f, 0.3f, 0.15f, 0f, 0f),
            5, List.of(0.45f, 0.33f, 0.2f, 0.02f, 0f),
            6, List.of(0.3f, 0.4f, 0.25f, 0.05f, 0f),
            7, List.of(0.19f, 0.3f, 0.40f, 0.1f, 0.01f),
            8, List.of(0.18f, 0.25f, 0.32f, 0.22f, 0.03f),
            9, List.of(0.1f, 0.2f, 0.25f, 0.35f, 0.1f),
            10, List.of(0.05f, 0.1f, 0.2f, 0.4f, 0.25f)
    )));


    private final HashMap<Integer, List<Float>> probabilities;
    RollingChances(HashMap<Integer, List<Float>> probabilities) {
        this.probabilities = probabilities;
    }

    public HashMap<Integer, List<Float>> getProbabilities() {
        return probabilities;
    }

    public List<Float> getProbability(int tactitionLevel) {
        return probabilities.get(tactitionLevel);
    }
}