import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;

public class GameShell extends BasicGame
{
    final static int unitCap = 8;

        //match instance
    protected Match match;
    protected Card testcard;

    protected Image base_img_spell;
    protected Image base_img_unit;

    protected Image[] image_deck_1;
    protected Image[] image_deck_2;

    public GameShell(String gamename)
    {
        super(gamename);

        Card[] deck = new Card[10];
        //Card[] deck = new Card[10];
        deck[0] = Card.loadCardFromJSONFile("test_cards/spell/burn_1");
        deck[1] = Card.loadCardFromJSONFile("test_cards/spell/burn_2");
        deck[2] = Card.loadCardFromJSONFile("test_cards/spell/burn_2B");
        deck[3] = Card.loadCardFromJSONFile("test_cards/unit/wisp");
        deck[4] = Card.loadCardFromJSONFile("test_cards/unit/fire_wisp");
        deck[5] = Card.loadCardFromJSONFile("test_cards/unit/apprentice");
        deck[6] = Card.loadCardFromJSONFile("test_cards/unit/novice");
        deck[7] = Card.loadCardFromJSONFile("test_cards/unit/squire");
        deck[8] = Card.loadCardFromJSONFile("test_cards/unit/soldier");
        deck[9] = deck[8];

        testcard = deck[0];

        match = new Match(unitCap, deck, deck);
    }

    @Override
    public void init(GameContainer gc) throws SlickException 
    {
        base_img_spell = new Image("../res/test_card.png");
        base_img_unit = new Image("../res/test_card_2.png");

        image_deck_1 = new Image[match.player_1.deck.length];
        image_deck_2 = new Image[match.player_2.deck.length];

        for (int i = match.player_1.deck.length - 1; i >= 0; i--) 
        {
            image_deck_1[i] = createCardImage(match.player_1.deck[i]);
        }
        for (int i = match.player_2.deck.length - 1; i >= 0; i--) 
        {
            image_deck_2[i] = createCardImage(match.player_2.deck[i]);       
        }
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {}

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException
    {
        //g.setBackground(Color.white);
        g.drawString("Howdy!", 50, 50);

        g.drawImage(image_deck_1[0],  50, 100);
        g.drawImage(image_deck_1[3], 350, 100);
        g.drawImage(image_deck_1[5], 650, 100);
        g.drawImage(image_deck_1[8], 950, 100);
    }

    public Image createCardImage(Card card)
    {
        int height = 350;
        int width  = 250;

        try
        {
            Image image = new Image(width, height);
            Graphics ig = image.getGraphics();

            if(card instanceof CardSpell)
            {
                CardSpell cardSpell = (CardSpell) card;
                ig.drawImage(base_img_spell, 0, 0);

                Ability ability = cardSpell.ability;
                ig.setColor(Color.black);

                int line = 0;
                for (int selNum = 0; selNum < ability.selectorList.size(); selNum++) 
                {
                    Selector sel = ability.selectorList.get(selNum);
                    ig.drawString(sel.getString(selNum), 35, 155 + (line * 20));
                    line++;
                }
                for (Resolvable res : ability.resolvableList) 
                {
                    ig.drawString(res.getString(), 35, 155 + (line * 20));
                    line++;
                }
            }
            else
            {
                CardUnit cardUnit = (CardUnit) card;
                ig.drawImage(base_img_unit, 0, 0);

                ig.drawString(" "+cardUnit.attack+" ", 10, height - 35);
                ig.drawString(" "+cardUnit.health+" ", width - 40, height - 35);

                ig.setColor(Color.black);
            }
            ig.drawString(card.name, 65, 15);
            ig.setColor(Color.white);
            ig.drawString(" "+card.evaluateCost()+" ", 10, 15);
            ig.flush();
            return image;
        }
        catch (SlickException e)
        {

        }
        return null;
    }

    public void drawUnit(Unit unit, int x, int y)
    {
        //TODO
    }

    public static void main(String[] args)
    {
        try
        {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new GameShell("Simple Slick Game"));
            appgc.setDisplayMode(1280, 800, false);
            appgc.setTargetFrameRate(60);
            appgc.start();
        }
        catch (SlickException ex)
        {
            Logger.getLogger(GameShell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}