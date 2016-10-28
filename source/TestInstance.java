import java.util.ArrayList;

class TestInstance
{
    public static void main(String[] args)
    {
        Ability burn_1 = new Ability();
        burn_1.selectorList.add(new SimpleSelector(1));
        burn_1.resolvableList.add(new ModDamage(0, 1));

        Ability burn_2 = new Ability();
        burn_2.selectorList.add(new SimpleSelector(2));
        burn_2.resolvableList.add(new ModDamage(0, 2));

        SimpleSelector selfSelect = new SimpleSelector(1);
        SimpleSelector foeSelect = new SimpleSelector(3);
        selfSelect.restrictionList.add(new Restriction("SIDE", 0, 0));
        Ability burn_3 = new Ability();
        burn_3.selectorList.add(selfSelect);
        burn_3.selectorList.add(foeSelect);
        burn_3.resolvableList.add(new ModDamage(0, 3));
        burn_3.resolvableList.add(new ModDamage(1, 3));

        Ability buff_1 = new Ability();
        buff_1.selectorList.add(new SimpleSelector(2));
        buff_1.resolvableList.add(new ModBuff(0, 2));

        SimpleSelector selfSelect_2 = new SimpleSelector(2);
        SimpleSelector foeSelect_2 = new SimpleSelector(1);
        foeSelect_2.restrictionList.add(new Restriction("SIDE", 1, 1));
        Ability buff_2 = new Ability();
        buff_2.selectorList.add(selfSelect_2);
        buff_2.selectorList.add(foeSelect_2);

        buff_2.resolvableList.add(new ModBuff(0, 4));
        buff_2.resolvableList.add(new ModBuff(1, 3));

        System.out.println("burn_1 power: " + burn_1.evaluatePower());
        System.out.println("burn_2 power: " + burn_2.evaluatePower());
        System.out.println("burn_3 power: " + burn_3.evaluatePower());
        System.out.println("buff_1 power: " + buff_1.evaluatePower());
        System.out.println("buff_2 power: " + buff_2.evaluatePower());

        
    }
}

class SimpleSelector extends Selector
{
    int targetCount;
    public SimpleSelector(int targetCount)
    {
        this.targetCount = targetCount;
        restrictionList = new ArrayList<Restriction>();
    }

    public int getTargetCount()
    {
        return targetCount;
    }
}

class ModDamage extends Resolvable
{
    int potency;
    public ModDamage(int selectorReference, int potency)
    {
        this.selectorReference = selectorReference;
        this.potency = potency;
    }

    public double getBaseValue()
    {
        return -potency;
    }
}

class ModBuff extends Resolvable
{
    int potency;
    public ModBuff(int selectorReference, int potency)
    {
        this.selectorReference = selectorReference;
        this.potency = potency;
    }

    public double getBaseValue()
    {
        return potency;
    }
}