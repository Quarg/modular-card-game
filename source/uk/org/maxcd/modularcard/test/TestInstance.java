import java.util.ArrayList;

import uk.org.maxcd.modularcard.*;
import uk.org.maxcd.modularcard.ability.*;

class TestInstance
{
    public static void main(String[] args)
    {
        CardSpell burn_1 = (CardSpell)Card.loadCardFromJSONFile("test_cards/spell/burn_1");
        CardSpell burn_2 = (CardSpell)Card.loadCardFromJSONFile("test_cards/spell/burn_2");
        CardSpell burn_2b = (CardSpell)Card.loadCardFromJSONFile("test_cards/spell/burn_2b");

        System.out.println("burn_1  power: " + burn_1.ability.evaluatePower());
        System.out.println("burn_2  power: " + burn_2.ability.evaluatePower());
        System.out.println("burn_2b power: " + burn_2b.ability.evaluatePower());
    }
}