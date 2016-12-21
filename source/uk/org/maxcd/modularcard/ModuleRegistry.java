package uk.org.maxcd.modularcard;

import java.util.ArrayList;

import uk.org.maxcd.modularcard.*;
import uk.org.maxcd.modularcard.ability.*;
import uk.org.maxcd.modularcard.ability.resolvable.*;
import uk.org.maxcd.modularcard.ability.selector.*;

abstract class ModuleRegistry
{
    public static Selector createSelector(String typeName, int potency, int range)
    {
        switch (typeName)
        {
            case "s_simple":
                return new SimpleSelector(potency);
        }
        return null;
    }

    public static Resolvable createResolvable(String typeName, int selector_reference, int potency)
    {
        switch (typeName)
        {
            case "r_damage":
                return new ModDamage(selector_reference, potency);
        }
        return null;
    }

    public static Selector createRestriction(String typeName, int min, int max)
    {
            //TODO!    
        return null;
    }
}