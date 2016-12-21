package uk.org.maxcd.modularcard.ability.selector;

import java.util.ArrayList;

import uk.org.maxcd.modularcard.ability.Selector;
import uk.org.maxcd.modularcard.ability.Restriction;

public class SimpleSelector extends Selector
{
    public SimpleSelector(int targetCount)
    {
        this.potency = targetCount;
        restrictionList = new ArrayList<Restriction>();
    }

    public int getTargetCount()
    {
        return potency;
    }

    public String getString(int i)
    {
        return "Target " + potency + "x [" + i + "].";
    }
}