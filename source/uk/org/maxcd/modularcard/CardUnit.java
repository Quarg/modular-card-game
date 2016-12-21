package uk.org.maxcd.modularcard;

import java.util.ArrayList;

import uk.org.maxcd.modularcard.*;
import uk.org.maxcd.modularcard.ability.*;

public class CardUnit extends Card
{
    public int attack;
    public int health;

    public ArrayList<Ability> triggeredAbilities;

    public CardUnit(String name, int attack, int health, ArrayList<Ability> triggeredAbilities)
    {
        super(name);
        this.attack = attack;
        this.health = health;
        this.triggeredAbilities = triggeredAbilities; 
    }

    public int evaluateCost()
    {
        double value = (attack + health - 2) / 2;

        for (Ability ability : triggeredAbilities) 
        {
            value += ability.evaluatePower() * ability.triggerCondition.triggerValue;
        }

        return (int) Math.ceil(value);
    }

    public void printOut()
    {
        System.out.println(">~~--");
        System.out.println("| Play Cost: < " + evaluateCost() + " >");
        System.out.println("|~~--");
        
        for (Ability ability : triggeredAbilities)
        {
            System.out.println("| " + ability.triggerCondition.triggerText );
            int i = 0;
            for (Selector selector : ability.selectorList) 
            {
                System.out.println("| " + selector.getString(i) );
                i++;
            }

            for (Resolvable resolvable : ability.resolvableList) 
            {
                System.out.println("| " + resolvable.getString() );
            }

            System.out.println("|~~--");
        }
        if(triggeredAbilities.size() == 0)
        {
            System.out.println("|");
            System.out.println("|~~--");
        }

        System.out.println("| { "+ attack +" / "+ health +" }");

        System.out.println(">~~--");
    }
}