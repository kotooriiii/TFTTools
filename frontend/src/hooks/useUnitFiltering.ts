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
                const selectedUnits = selectedItems
                    .filter(item => item.type === 'unit')
                    .map(selectedUnit => selectedUnit.displayName);

                const selectedTraits = selectedItems
                    .filter(item => item.type === 'trait')
                    .map(selectedTrait => selectedTrait.displayName);
                const units = await filterService.filterUnits(selectedUnits, selectedTraits);
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