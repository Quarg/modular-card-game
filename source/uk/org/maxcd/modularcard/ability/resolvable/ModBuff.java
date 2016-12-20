import java.util.ArrayList;

package uk.org.maxcd.modularcard.ability.resolvable;

public class ModBuff extends Resolvable
{
    public ModBuff(int selectorReference, int potency)
    {
        this.selectorReference = selectorReference;
        this.potency = potency;
    }

    public double getBaseValue()
    {
        return potency;
    }

    public String getString()
    {
        return "Give +" + potency + " health to [" + selectorReference + "].";
    }
}