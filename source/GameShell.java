import java.util.logging.Level;
import java.util.logging.Logger;

import java.awt.Font;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
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

    protected TrueTypeFont font_large;
    protected TrueTypeFont font_small;

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
        deck[9] = Card.loadCardFromJSONFile("test_cards/unit/giant");

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

        Font font = new Font("Verdana", Font.BOLD, 32);
        font_large = new TrueTypeFont(font, true);
        font = new Font("Verdana", Font.BOLD, 16);
        font_small = new TrueTypeFont(font, true);

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
        //g.drawString("Howdy!", 50, 50);

        g.drawImage(image_deck_1[0],  50, 30);
        g.drawImage(image_deck_1[3], 350, 30);
        g.drawImage(image_deck_1[4], 650, 30);
        g.drawImage(image_deck_1[5], 950, 30);

        g.drawImage(image_deck_1[2],  50, 400);
        g.drawImage(image_deck_1[7], 350, 400);
        g.drawImage(image_deck_1[8], 650, 400);
        g.drawImage(image_deck_1[9], 950, 400);
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
                ig.setFont(font_small);
                ig.setColor(Color.black);

                int line = 0;
                for (int selNum = 0; selNum < ability.selectorList.size(); selNum++) 
                {
                    Selector sel = ability.selectorList.get(selNum);
                    ig.drawString(sel.getString(selNum), 32, 155 + (line * 20));
                    line++;
                }
                for (Resolvable res : ability.resolvableList) 
                {
                    ig.drawString(res.getString(), 32, 155 + (line * 20));
                    line++;
                }
            }
            else
            {
                CardUnit cardUnit = (CardUnit) card;
                ig.drawImage(base_img_unit, 0, 0);

                ig.setFont(font_large);
                String var = Integer.toString(cardUnit.attack);
                ig.drawString(var, 25 - (font_large.getWidth(var)/2f), 323 - (font_large.getHeight(var)/2f));
                var = Integer.toString(cardUnit.health);
                ig.drawString(var, 225 - (font_large.getWidth(var)/2f), 323 - (font_large.getHeight(var)/2f));

                ig.setFont(font_small);
                ig.setColor(Color.black);

                int line = 0;
                for (Ability ability : cardUnit.triggeredAbilities) 
                {
                    ig.drawString(ability.triggerCondition.triggerText, 32, 155 + (line * 20));
                    line++;
                    for (int selNum = 0; selNum < ability.selectorList.size(); selNum++) 
                    {
                        Selector sel = ability.selectorList.get(selNum);
                        ig.drawString(sel.getString(selNum), 45, 155 + (line * 20));
                        line++;
                    }
                    for (Resolvable res : ability.resolvableList) 
                    {
                        ig.drawString(res.getString(), 45, 155 + (line * 20));
                        line++;
                    }
                }

            }
            ig.drawString(card.name, 65, 15);

            ig.setFont(font_large);
            ig.setColor(Color.white);
            String cost = Integer.toString(card.evaluateCost());
            ig.drawString(cost, 25 - (font_large.getWidth(cost)/2f), 23 - (font_large.getHeight(cost)/2f));
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