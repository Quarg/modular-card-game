import java.util.ArrayList;

public class Match
{
    final int unit_cap;
    //final int deck1_size;
    //final int deck2_size;
        //Match Properties.

    PlayerSide player_1;
    PlayerSide player_2;

    public Match(int unit_cap, Card[] p1_deck, Card[] p2_deck)
    {
        this.unit_cap = unit_cap;

        player_1 = new PlayerSide(unit_cap, p1_deck);
        player_2 = new PlayerSide(unit_cap, p2_deck);
    }
}

class PlayerSide
{
    public PlayerSide(int unit_cap, Card[] deck)
    {
        this.deck = deck;
        units = new Unit[unit_cap];
    }

    public Card[] deck;
    public Unit[] units;

    public ArrayList<Integer> hand;
    public ArrayList<Integer> library;
    public ArrayList<Integer> discard;
    public ArrayList<Integer> inplay;

    public void printHand()
    {
        int i = 0;
        for (int card_id : hand) 
        {
            System.out.println("Card #"+i+":");
            deck[card_id].printOut();
            i++;
        }
    }

    public void printUnits()
    {
        /*
        int i = 0;
        for (int card_id : hand) 
        {
            System.out.println("Card #"+i+":");
            deck[card_id].printOut();
            i++;
        }
        */
    }
}

class Unit
{
    public static Unit createFromCard(Match Match, boolean is_player2, int card_id)
    {
        PlayerSide side;
        if(!is_player2)
            side = Match.player_1;
        else
            side = Match.player_2;
        CardUnit card = (CardUnit)side.deck[card_id];

        Unit unit = new Unit();
        unit.attack = card.attack;
        unit.health = card.health;
        unit.atrophy = 0;
        unit.damage = 0;
        return unit;
    }

        //Unit Stats
    int attack;
    int health;

        //Stat "Damage"
    int atrophy;    //damage to "attack"
    int damage;     //damage to "health"

    CardUnit card;

    public void printOut()
    {
        System.out.println(">~~--");
        System.out.println("| Play Cost: < " + card.evaluateCost() + " >");
        System.out.println("|~~--");
        
        for (Ability ability : card.triggeredAbilities)
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
        if(card.triggeredAbilities.size() == 0)
        {
            System.out.println("|");
            System.out.println("|~~--");
        }

        System.out.println("| { "+ (attack - atrophy) + "/" + attack +" } : { "+ (health - damage) + "/" + health +" }");

        System.out.println(">~~--");
    }
}