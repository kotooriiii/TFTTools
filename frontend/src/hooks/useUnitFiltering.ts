import { useState, useEffect } from 'react';
import { Unit } from '../types/unitTypes';
import { SelectedItem } from '../types/searchTypes';
import {filterService} from "../services/filterService.ts";

export const useUnitFiltering = (selectedItems: SelectedItem[]) => {
    const [filteredUnits, setFilteredUnits] = useState<Unit[]>([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const fetchFilteredUnits = async () => {
            setIsLoading(true);

            try {
                const selectedChampions = selectedItems
                    .filter(item => item.type === 'champion')
                    .map(selectedChampion => selectedChampion.name);

                const selectedTraits = selectedItems
                    .filter(item => item.type === 'trait')
                    .map(selectedTrait => selectedTrait.name);
                const units = await filterService.filterUnits(selectedChampions, selectedTraits);
                setFilteredUnits(units);
            } catch (error) {
                console.error('Error filtering units:', error);
                setFilteredUnits([]);
            } finally {
                setIsLoading(false);
            }
        };

        fetchFilteredUnits();
    }, [selectedItems]);


    return {
        filteredUnits,
        isLoading,
    };
};