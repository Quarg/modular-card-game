package uk.org.maxcd.modularcard.ability;

import java.util.ArrayList;

import uk.org.maxcd.modularcard.*;

public class Restriction
{
    public String parameterName;
    public int maxValue;
    public int minValue;

    public Restriction(String parameterName, int maxValue, int minValue)
    {
        this.parameterName = parameterName;
        this.maxValue = maxValue;
        this.minValue = minValue;
    }

    public double getMatchProbability()
    {
        return 1;
    }
}