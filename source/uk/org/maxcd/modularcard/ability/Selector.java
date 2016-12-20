import java.util.ArrayList;

package uk.org.maxcd.modularcard.ability;

public abstract class Selector
{
    public Selector(){}
    public int potency;    //json-param: "potency"
    public int range;      //json-param: "range"

    ArrayList<Restriction> restrictionList;

    public abstract int getTargetCount();
    public abstract String getString(int identifier);
}