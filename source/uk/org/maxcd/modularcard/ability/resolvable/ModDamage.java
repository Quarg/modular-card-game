package uk.org.maxcd.modularcard.ability.resolvable;

import java.util.ArrayList;

import uk.org.maxcd.modularcard.ability.Resolvable;

public class ModDamage extends Resolvable
{
    public ModDamage(int selectorReference, int potency)
    {
        this.selectorReference = selectorReference;
        this.potency = potency;
    }

    public double getBaseValue()
    {
        return -potency;
    }

    public String getString()
    {
        return "Do " + potency + " damage to [" + selectorReference + "].";
    }
}