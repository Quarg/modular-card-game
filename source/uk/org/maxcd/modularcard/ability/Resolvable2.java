package uk.org.maxcd.modularcard.ability;

import java.util.ArrayList;

public abstract class Resolvable2
{
    public Resolvable2(){}
    public int selectorReference;   //json-param: "target"
    public int potency;             //json-param: "potency"

        //A POSITIVE value indicates an effect benificial to the target.
        //A NEGATIVE value indicates an effect harmful to the target.
    public abstract double getBaseValue();
    public abstract String getString();

    //public abstract double getValue()
}