package com.tfttools.domain;

import lombok.Getter;

import java.util.List;

@Getter
public class Trait implements Namable{
    private final String traitName;
    private final List<int[]> thresholds;

    public Trait(String traitName, List<int[]> thresholds) {
        this.traitName = traitName;
        this.thresholds = thresholds;
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
