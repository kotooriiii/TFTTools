package com.tfttools.domain;

import java.util.List;

public class Trait implements Namable{
    private final String traitName;
    private final List<int[]> thresholds;

    public Trait(String traitName, List<int[]> thresholds) {
        this.traitName = traitName;
        this.thresholds = thresholds;
    }

    public String getTraitName() {
        return traitName;
    }

    public List<int[]> getThresholds() {
        return thresholds;
    }

    @Override
    public String getDisplayName() {
        return traitName;
    }

    @Override
    public String toString() {
        return this.traitName;
    }
}
