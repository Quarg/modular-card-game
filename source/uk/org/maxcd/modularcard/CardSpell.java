import java.util.ArrayList;

package uk.org.maxcd.modularcard;

public class CardSpell extends Card
{
    Ability ability;

    public CardSpell(String name, Ability ability)
    {
        super(name);
        this.ability = ability;
    }

    public int evaluateCost()
    {
        return (int) Math.ceil(ability.evaluatePower()) - 1;
    }

    public void printOut()
    {
        System.out.println(">~~--");
        System.out.println("| Play Cost: < " + evaluateCost() + " >");
        System.out.println("|~~--");
        
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

        System.out.println(">~~--");
    }
}