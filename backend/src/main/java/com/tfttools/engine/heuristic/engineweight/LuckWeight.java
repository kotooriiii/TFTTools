package com.tfttools.engine.heuristic.engineweight;

import com.tfttools.domain.RollingChances;
import com.tfttools.domain.Unit;
import com.tfttools.engine.EngineState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LuckWeight implements EngineWeight{
    private final EngineState engineState;
    private final int tactitionLevel;
    private final float luck;
    private int weight;

    public LuckWeight(EngineState engineState, float luck) {
        this.engineState = engineState;
        this.tactitionLevel = engineState.getTactitionLevel();
        this.luck = luck;
        this.weight = 0;
    }

    @Override
    public int getWeight(Unit unit) {
        int cost = unit.getCost();

        List<Float> reweightedProbabilities = reweight(RollingChances.PROBABILITIES.getProbability(tactitionLevel), 2);

        this.weight = Math.round(reweightedProbabilities.get(cost - 1) * 10);

        return weight;
    }

    private List<Float> reweight(List<Float> probabilities, float strength) {
        int n = probabilities.size();
        float[] t = new float[n];

        for (int i = 0; i < n; i++) {
            t[i] = -1.0f + 2.0f * i / (n - 1.0f);
        }

        float kappa = strength * (2 * luck - 1);
        float[] w = new float[n];
        float Z = 0.0f;

        for (int i = 0; i < n; i++) {
            w[i] = (float) Math.exp(kappa * t[i]); Z += probabilities.get(i) * w[i];
        }

        List<Float> q = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            q.add((probabilities.get(i) * w[i]) / Z);
        }

        return q;
    }
}
