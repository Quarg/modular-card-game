import java.util.ArrayList;

import java.io.FileReader;
import java.io.FileNotFoundException;

import javax.json.*;

import org.newdawn.slick.Image;

package uk.org.maxcd.modularcard;

public abstract class Card
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