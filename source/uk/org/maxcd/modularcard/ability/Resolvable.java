import java.util.ArrayList;

package uk.org.maxcd.modularcard.ability;

abstract class Resolvable
{
    public Resolvable(){}
    public int selectorReference;   //json-param: "target"
    public int potency;             //json-param: "potency"

        //A POSITIVE value indicates an effect benificial to the target.
        //A NEGATIVE value indicates an effect harmful to the target.
    public abstract double getBaseValue();
    public abstract String getString();
}