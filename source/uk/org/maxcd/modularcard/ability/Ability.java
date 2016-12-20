import java.util.ArrayList;

package uk.org.maxcd.modularcard.ability;

public class Ability
{
    EnumTriggerCondition triggerCondition;
        //Ignore this for spells.

    ArrayList<Selector> selectorList;
    ArrayList<Resolvable> resolvableList;

    public Ability()
    {
        selectorList = new ArrayList<Selector>();
        resolvableList = new ArrayList<Resolvable>();
    }

    public double evaluatePower()
    {
        double rollingEvaluation = 0;

        for (Resolvable resolvable : resolvableList) 
        {
            Selector selector = selectorList.get(resolvable.selectorReference);
            
            double probability = 1;

            boolean self_allowed = true;
            boolean foes_allowed = true;

            for (Restriction restriction : selector.restrictionList) 
            {
                probability *= restriction.getMatchProbability();

                    //Special Restriction type, to specify side requirement.
                    // 0-0 is SELF, 1-1 is FOES
                if(restriction.parameterName == "SIDE")
                {
                    if(restriction.minValue != 0)
                        self_allowed = false;

                    if(restriction.maxValue != 1)
                        foes_allowed = false;
                }
            }

            double base = resolvable.getBaseValue();

            if( (base < 0 && self_allowed && !foes_allowed) )
            {
                rollingEvaluation += resolvable.getBaseValue() * selector.getTargetCount() / probability;
                //System.out.println("SELF: base: " + resolvable.getBaseValue() + ", count:" + selector.getTargetCount() + ", probability:" + probability);
            }
            else if( (base > 0 && !self_allowed && foes_allowed) )
            {
                rollingEvaluation -= resolvable.getBaseValue() * selector.getTargetCount() / probability;
                //System.out.println("FOES: base: " + resolvable.getBaseValue() + ", count:" + selector.getTargetCount() + ", probability:" + probability);
            }
            else
            {
                rollingEvaluation += Math.abs(resolvable.getBaseValue()) * selector.getTargetCount() * probability;
                //System.out.println("BOTH: base: " + resolvable.getBaseValue() + ", count:" + selector.getTargetCount() + ", probability:" + probability);
            }
        }

        return rollingEvaluation;
    }
}