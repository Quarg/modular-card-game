package uk.org.maxcd.modularcard.ability;

import java.util.ArrayList;

import uk.org.maxcd.modularcard.*;

public abstract class Selector
{
    public Selector(){}
    public int potency;    //json-param: "potency"
    public int range;      //json-param: "range"

    public ArrayList<Restriction> restrictionList;

    public abstract int getTargetCount();
    public abstract String getString(int identifier);

    //public abstract float mergeEvaluations();
}