import java.util.ArrayList;

import java.io.FileReader;
import java.io.FileNotFoundException;

import javax.json.*;

import org.newdawn.slick.Image;

abstract class Card
{
    public final String name;
    public Image image;

    public abstract int evaluateCost();
    public abstract void printOut();

    public Card(String name)
    {
        this.name = name;
    }

    public static Card loadCardFromJSONFile(String jsonFilePath)
    {
        try
        {
            JsonReader reader = Json.createReader(new FileReader("../data/cards/"+jsonFilePath+".json"));
            JsonObject obj = reader.readObject();

            String name = obj.getString("name");

            String type = obj.getString("type");
            if("spell".equals(type))
            {
                JsonObject ability_obj = obj.getJsonObject("ability");
                Ability ability = loadAbilityFromJSONObject(ability_obj, false);
                return new CardSpell(name, ability);
            }
            else if("unit".equals(type))
            {
                JsonArray abilities = obj.getJsonArray("abilities");

                ArrayList<Ability> triggeredAbilities = new ArrayList<Ability>();

                for (int i = 0; i < abilities.size(); i++) 
                {
                    triggeredAbilities.add(loadAbilityFromJSONObject(abilities.getJsonObject(i), true));
                }

                return new CardUnit(name, obj.getInt("attack", 0), obj.getInt("health", 0), triggeredAbilities);
            }
            else
            {
                //TODO: ERROR HANDLING
                System.out.println("Card loaded ("+jsonFilePath+") was neither a SPELL nor a UNIT.");
            }
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Could not find file '"+jsonFilePath+"' for card loading.");
        }
        return null;
    }

    public static Ability loadAbilityFromJSONObject(JsonObject ability_obj, boolean triggered)
    {
        Ability ability = new Ability();

        if(triggered)
        {
            ability.triggerCondition = EnumTriggerCondition.valueOf(ability_obj.getString("trigger"));
        }

        JsonArray selectors = ability_obj.getJsonArray("selectors");
        for (int i = 0; i < selectors.size(); i++) 
        {
            JsonObject sel_obj = selectors.getJsonObject(i);

            ability.selectorList.add(
                ModuleRegistry.createSelector(sel_obj.getString("type"), 
                    sel_obj.getInt("potency", 0), 
                    sel_obj.getInt("range", 0)));
        }
        JsonArray resolvables = ability_obj.getJsonArray("resolvables");
        for (int i = 0; i < resolvables.size(); i++) 
        {
            JsonObject res_obj = resolvables.getJsonObject(i);

            ability.resolvableList.add(
                ModuleRegistry.createResolvable(res_obj.getString("type"), 
                    res_obj.getInt("target", 0), 
                    res_obj.getInt("potency", 0)));
        }

        return ability;
    }
}

class CardUnit extends Card
{
    int attack;
    int health;

    ArrayList<Ability> triggeredAbilities;

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

class CardSpell extends Card
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

class Ability
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

abstract class Selector
{
    public Selector(){}
    public int potency;    //json-param: "potency"
    public int range;      //json-param: "range"

    ArrayList<Restriction> restrictionList;

    public abstract int getTargetCount();
    public abstract String getString(int identifier);
}

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
    ENTER_PLAY(1, "On Enter:", true),
    LEAVE_PLAY(0.8, "On Leave:", false),
    TURN_START(2, "On Turn Start:", true),
    TURN_END(2.5, "On Turn End:", false);

    public final double triggerValue;
    public final String triggerText;
    public final boolean synchronous;

    EnumTriggerCondition(double triggerValue, String triggerText, boolean synchronous)
    {
        this.triggerValue = triggerValue;
        this.triggerText = triggerText;
        this.synchronous = synchronous;
    }
}

abstract class ModuleRegistry
{
    public static Selector createSelector(String typeName, int potency, int range)
    {
        switch (typeName)
        {
            case "s_simple":
                return new SimpleSelector(potency);
        }
        return null;
    }

    public static Resolvable createResolvable(String typeName, int selector_reference, int potency)
    {
        switch (typeName)
        {
            case "r_damage":
                return new ModDamage(selector_reference, potency);
        }
        return null;
    }

    public static Selector createRestriction(String typeName, int min, int max)
    {
            //TODO!    
        return null;
    }
}