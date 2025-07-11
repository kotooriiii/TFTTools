package com.tfttools.graph;

import com.tfttools.domain.Champion;
import com.tfttools.domain.Trait;
import com.tfttools.domain.Unit;
import com.tfttools.registry.UnitRegistry;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.jgrapht.graph.Multigraph;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Custom graph class to represent graphs for units
 */
@Component
public class UnitGraph
{

    private final Graph<Champion, TraitEdge> graph;

    /**
     * Initialize new graph with given registry
     * @param registry Champion pool to create graph from
     */
    public UnitGraph(UnitRegistry registry)
    {
        this.graph = new Multigraph<>(TraitEdge.class);

        // Add all units as vertices
        for (Unit unit : registry.getAllUnits())
        {
            graph.addVertex(unit.getChampion());
        }

        // Add edges between champions that share the same trait
        for (Map.Entry<Trait, List<Unit>> entry : registry.getUnitsByTrait().entrySet())
        {

            Trait trait = entry.getKey();

            List<Unit> units = entry.getValue();
            Champion[] champions = units.stream()
                    .map(Unit::getChampion)
                    .toArray(Champion[]::new);

            for (int i = 0; i < champions.length; i++)
            {
                for (int j = i + 1; j < champions.length; j++)
                {
                    Champion c1 = champions[i];
                    Champion c2 = champions[j];
                    graph.addEdge(c1, c2, new TraitEdge(trait));

                }
            }
        }
    }

    /**
     * Gets all neighbors of given champion
     * @param champion Champion to find neighbors from
     * @return Set of Champions that share a trait with given Champion
     */
    public Set<Champion> getNeighbors(Champion champion)
    {
        return graph.containsVertex(champion)
                ? new HashSet<>(Graphs.neighborListOf(graph, champion))
                : Collections.emptySet();
    }

    /**
     * Get all champions in graph
     * @return Set of all champions in the graph
     */
    public Set<Champion> getAllChampions()
    {
        return graph.vertexSet();
    }
}
