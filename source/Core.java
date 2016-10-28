/* Implementation Assumptions:

    1:  A Resolvable's effect only affects a single target, and as such, scales linearly with each target affected.


    Notable Cheat Cases:

    * 
*/

import java.util.ArrayList;

abstract class Card
{
    public abstract int evaluateCost();
}

class CardUnit extends Card
{
    int attack;
    int health;

        //This implementation only allows for one triggered ability per unit, nor does it allow for triggered abilities to be attached by other abilities.
        //IDEA: Include trigger condition in Ability, and just ignore it when it's irrelevent (which is basically only on spells)

    ArrayList<Ability> triggeredAbilities;

    public int evaluateCost()
    {
        double value = (attack + health - 2) / 2;

        for (Ability ability : triggeredAbilities) 
        {
            value += ability.evaluatePower() * ability.triggerCondition.triggerValue;
        }

        return (int) value;
    }
}

class CardSpell extends Card
{
    Ability ability;

    public CardSpell(Ability ability)
    {
        this.ability = ability;
    }

    public int evaluateCost()
    {
        return (int) Math.ceil(ability.evaluatePower());
    }
}

class Ability
{
    EnumTriggerCondition triggerCondition;

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

abstract class Selector
{
    ArrayList<Restriction> restrictionList;

    public abstract int getTargetCount();
}

abstract class Resolvable
{
    public int selectorReference;
        //A POSITIVE value indicates an effect benificial to the target.
        //A NEGATIVE value indicates an effect harmful to the target.
    public abstract double getBaseValue();
}

class Restriction
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

enum EnumTriggerCondition
{
    ENTER_PLAY(1),
    LEAVE_PLAY(0.8),
    TURN_START(2),
    TURN_END(2.2);

    final double triggerValue;

    EnumTriggerCondition(double triggerValue)
    {
        this.triggerValue = triggerValue;
    }
}