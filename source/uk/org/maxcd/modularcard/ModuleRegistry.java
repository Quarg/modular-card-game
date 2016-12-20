import java.util.ArrayList;

package uk.org.maxcd.modularcard;

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