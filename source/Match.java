import java.util.ArrayList;
import java.util.Random;

public class Match
{
    final int field_width;
    final int field_height;

    public Unit[][] field;

    public PlayerSide player_1;
    public PlayerSide player_2;

    public Match(int field_width, int field_height, Card[] p1_deck, Card[] p2_deck)
    {
        this.field_width = field_width;
        this.field_height = field_height;

        field = new Unit[field_width][field_height];

        player_1 = new PlayerSide(p1_deck);
        player_2 = new PlayerSide(p2_deck);
    }
}

class PlayerSide
{
    //Referring to cards by reference IDs may be necessary for net-code.

    public int cardsInDeck;
    public Card[] deck;

    public ArrayList<Card> hand;

    public Unit[] units;

    public PlayerSide(Card[] deck)
    {
        this.deck = deck;
        hand = new ArrayList<Card>();
        cardsInDeck = deck.length;

        shuffleDeck();
        shuffleDeck();
        shuffleDeck();
    }

    public void shuffleDeck()
    {
        //TODO: Improve Random Number Generation for this shuffle, 
        //          as it simply cannot create all sequences of card shuffles without a far greater bit depth.

        Random rand = new Random();

        for (int i = cardsInDeck - 1; i > 0; i--) 
        {
            int j = rand.nextInt(i + 1);
            if(j != i)
            {
                Card swap = deck[i];
                deck[i] = deck[j];
                deck[j] = swap;   
            }
        }
    }

    public boolean drawCard()
    {
        if(cardsInDeck <= 0)
            return false;

        hand.add(deck[cardsInDeck - 1]);
        deck[cardsInDeck - 1] = null;
        cardsInDeck--;

        return true;
    }
}

class Unit
{
    public static Unit createFromCard(Match match, boolean is_player2, CardUnit card)
    {
        PlayerSide side;
        if(!is_player2)
            side = match.player_1;
        else
            side = match.player_2;

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

    boolean player_side;

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