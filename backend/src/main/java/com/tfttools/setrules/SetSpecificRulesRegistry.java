package com.tfttools.setrules;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Collects every registered {@link SetSpecificRules} bean and looks one up by set number.
 * Sets with no registered rules get a no-op default (generic variant-name pattern, no trait
 * count overrides), so this is safe to consult unconditionally regardless of which set is active.
 */
@Component
public class SetSpecificRulesRegistry
{
    private static final SetSpecificRules NO_OP = new SetSpecificRules()
    {
        @Override
        public String getSetNumber()
        {
            return null;
        }
    };

    private final Map<String, SetSpecificRules> rulesBySetNumber;

    public SetSpecificRulesRegistry(List<SetSpecificRules> rules)
    {
        this.rulesBySetNumber = new HashMap<>();
        for (SetSpecificRules rule : rules)
        {
            this.rulesBySetNumber.put(rule.getSetNumber(), rule);
        }
    }

    /**
     * Gets the rules registered for the given set number, or a no-op ruleset if none is registered.
     */
    public SetSpecificRules getRulesFor(String setNumber)
    {
        return this.rulesBySetNumber.getOrDefault(setNumber, NO_OP);
    }
}
