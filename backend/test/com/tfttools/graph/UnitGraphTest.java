package com.tfttools.graph;

import com.tfttools.domain.Champion;
import com.tfttools.registry.UnitRegistry;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UnitGraphTest {

    @Test
    public void testGetNeighbors() {
        UnitGraph test = new UnitGraph(new UnitRegistry());
        Set<Champion> ziggsNeighbors = new HashSet<>(Arrays.asList(Champion.POPPY, Champion.VEIGAR, Champion.KOBUKO,
                Champion.EKKO, Champion.LEBLANC, Champion.YUUMI, Champion.NEEKO));


        assert ziggsNeighbors.equals(test.getNeighbors(Champion.ZIGGS));
    }

    @Test
    public void testGetAllChampions() {
        UnitGraph test = new UnitGraph(new UnitRegistry());
        Set<Champion> allChamps = Arrays.stream(Champion.values()).collect(Collectors.toSet());

        assert allChamps.equals(test.getAllChampions());
    }
}
