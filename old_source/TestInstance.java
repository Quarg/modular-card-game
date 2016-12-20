import java.util.ArrayList;

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

        //burn_1.printOut();
        //burn_2.printOut();
        //burn_2b.printOut();
    }
}

class SimpleSelector extends Selector
{
    public SimpleSelector(int targetCount)
    {
        this.potency = targetCount;
        restrictionList = new ArrayList<Restriction>();
    }

    public int getTargetCount()
    {
        return potency;
    }

    public String getString(int i)
    {
        return "Target " + potency + "x [" + i + "].";
    }
}

class ModDamage extends Resolvable
{
    public ModDamage(int selectorReference, int potency)
    {
        this.selectorReference = selectorReference;
        this.potency = potency;
    }

    public double getBaseValue()
    {
        return -potency;
    }

    public String getString()
    {
        return "Do " + potency + " damage to [" + selectorReference + "].";
    }
}

class ModBuff extends Resolvable
{
    public ModBuff(int selectorReference, int potency)
    {
        this.selectorReference = selectorReference;
        this.potency = potency;
    }

    public double getBaseValue()
    {
        return potency;
    }

    public String getString()
    {
        return "Give +" + potency + " health to [" + selectorReference + "].";
    }
}