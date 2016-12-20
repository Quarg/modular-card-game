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
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class GameShell extends BasicGame
{
        //match instance
    protected Match match;

    protected Input in;

    protected Image img_background;
    protected Image img_highlight;
    protected Image base_img_spell;
    protected Image base_img_unit;
    protected Image img_unit;

    protected TrueTypeFont font_large;
    protected TrueTypeFont font_small;

    protected int mx;
    protected int my;

    protected int sh;
    protected int sw;


    protected Unit test_unit;

    protected int card_highlight;
    protected int card_select;
    protected EnumInputState input_state;

    public GameShell(String gamename, int screenWidth, int screenHeight)
    {
        super(gamename);

        Card[] deck = new Card[10];
        Card[] deck2 = new Card[10];
        deck2[0] = deck[0] = Card.loadCardFromJSONFile("test_cards/spell/burn_1");
        deck2[1] = deck[1] = Card.loadCardFromJSONFile("test_cards/spell/burn_2");
        deck2[2] = deck[2] = Card.loadCardFromJSONFile("test_cards/spell/burn_2B");
        deck2[3] = deck[3] = Card.loadCardFromJSONFile("test_cards/unit/wisp");
        deck2[4] = deck[4] = Card.loadCardFromJSONFile("test_cards/unit/fire_wisp");
        deck2[5] = deck[5] = Card.loadCardFromJSONFile("test_cards/unit/apprentice");
        deck2[6] = deck[6] = Card.loadCardFromJSONFile("test_cards/unit/novice");
        deck2[7] = deck[7] = Card.loadCardFromJSONFile("test_cards/unit/squire");
        deck2[8] = deck[8] = Card.loadCardFromJSONFile("test_cards/unit/soldier");
        deck2[9] = deck[9] = Card.loadCardFromJSONFile("test_cards/unit/giant");

        match = new Match(6, 5, deck, deck2);
        in = new Input(screenHeight);

        sh = screenHeight;
        sw = screenWidth;

        test_unit = new Unit();
        test_unit.attack = 3;
        test_unit.health = 6;
        test_unit.damage = 2;

        input_state = EnumInputState.CARD_SELECT;
        card_select = card_highlight = -1;
    }

    @Override
    public void init(GameContainer gc) throws SlickException 
    {
        base_img_spell = new Image("../res/img/test_card.png");
        base_img_unit = new Image("../res/img/test_card_2.png");

        img_highlight = new Image("../res/img/card_highlight.png");
        img_background = new Image("../res/img/match_layout_sketch.png");
        img_unit = new Image("../res/img/test_unit_s.png");

        Font font = new Font("Verdana", Font.BOLD, 32);
        font_large = new TrueTypeFont(font, true);
        font = new Font("Verdana", Font.BOLD, 16);
        font_small = new TrueTypeFont(font, true);

        for (Card card : match.player_1.deck) 
        {
            card.image = createCardImage(card);
        }
        for (Card card : match.player_1.deck) 
        {
            card.image = createCardImage(card);   
        }
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException 
    {
        in.poll(sw, sh);
        //Update Hand Highlight
        mx = in.getMouseX();
        my = in.getMouseY();

        if(input_state == EnumInputState.CARD_SELECT || input_state == EnumInputState.TARGET_SELECT)
        {
            if(my > 550)
            {
                float f = ((mx - 240) / 800f) * match.player_1.hand.size();
                if(f < 0 || f >= match.player_1.hand.size())
                {
                    f = -1;
                }
                card_highlight = (int)f;
            }
            else
            {
                card_highlight = -1;
            }

            if(in.isMousePressed(0) || in.isMousePressed(1) || in.isMousePressed(2) || in.isMousePressed(3))
            {
                System.out.println("BUTTON PRESSED");
                if(card_highlight >= 0)
                {
                    input_state = EnumInputState.TARGET_SELECT;
                    card_select = card_highlight;
                }
            }
        }
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException
    {
        g.setAntiAlias(true);
        //g.drawImage(img_background, 0, 0);

        int epsilon = 5;
        int tile_width = 150;
        int tile_height = 100;

        for (int x = 0; x < match.field_width; x++) 
        {
            for (int y = 0; y < match.field_height; y++) 
            {
                g.drawRect(
                    x * tile_width - (match.field_width * tile_width / 2) + 640 + epsilon,
                    y * tile_height - (match.field_height * tile_height / 2) + 275 + epsilon, 
                    tile_width - epsilon * 2, 
                    tile_height - epsilon *2); 

                if(match.field[x][y] != null)
                {
                    drawUnit(g, match.field[x][y], 
                        x * tile_width - (match.field_width * tile_width / 2) + 640, 
                        y * tile_height - (match.field_height * tile_height / 2) + 275);
                }

                /*
                if(x == 1 && y == 1)
                {
                    drawUnit(g, test_unit, 
                        x * tile_width - (match.field_width * tile_width / 2) + 640, 
                        y * tile_height - (match.field_height * tile_height / 2) + 275);

                    /*
                    g.drawImage(img_unit,
                        x * tile_width - (match.field_width * tile_width / 2) + 640,
                        y * tile_height - (match.field_height * tile_height / 2) + 275);
                    g.setFont(font_large);
                    g.drawString("5",
                        x * tile_width - (match.field_width * tile_width / 2) + 640 + 10, 
                        y * tile_height - (match.field_height * tile_height / 2) + 275 + 55);
                    g.drawString("3",
                        x * tile_width - (match.field_width * tile_width / 2) + 640 + 115, 
                        y * tile_height - (match.field_height * tile_height / 2) + 275 + 55);
                    g.resetFont();
                    ///
                }
                */
            }                
        }

        g.pushTransform();
          g.translate(640, 700);
          drawHand(g, match.player_1);
        g.popTransform();

        g.drawString("| - ["+mx+", "+my+"]", mx, my);

        g.drawString("1: Draw Card", 20, 30);
        g.drawString("2: Shuffle left card into deck.", 20, 50);
    }

    public void drawHand(Graphics g, PlayerSide side)
    {
        int handsize = side.hand.size();
        float angleSpread = 15;

        float xSpread = 600;

        if(handsize <= 4)
        {
            xSpread = 150 * handsize;
        }

        int i = 0;
        g.pushTransform();
        g.scale(0.6f, 0.6f);
        for (Card card : side.hand) 
        {
            g.pushTransform();

            float x_delta = 2 * (((handsize - 1) / 2f) - i) / handsize;

            g.translate(-xSpread * x_delta, - 50 + 50 * x_delta * x_delta);

            if(i == card_select)
            {
                if(i == card_highlight)
                    g.drawImage(img_highlight, - img_highlight.getWidth()/2f, - img_highlight.getHeight()/2f, Color.cyan);
                else
                    g.drawImage(img_highlight, - img_highlight.getWidth()/2f, - img_highlight.getHeight()/2f, Color.blue);
            }
            else if (i == card_highlight)
            {
                g.drawImage(img_highlight, - img_highlight.getWidth()/2f, - img_highlight.getHeight()/2f, Color.white);
            }

            g.drawImage(card.image, -card.image.getWidth()/2f, -card.image.getHeight()/2f);
            g.drawString("["+x_delta+"]", -50, -200);

            g.popTransform();
            i++;
        }
        g.popTransform();
    }

    public void drawUnit(Graphics g, Unit unit, int x, int y)
    {
        int x_delta = 20;
        int y_delta = 15;

        g.drawImage(img_unit, x,y);
        g.setFont(font_large);
          g.drawString(Integer.toString(unit.attack - unit.atrophy), x + 5, y + 55);
          g.drawString(Integer.toString(unit.health - unit.damage), x + 110, y + 55);
        g.setFont(font_small);
          g.drawString("/" + unit.attack, x + 5 + x_delta, y + 55 + y_delta);
          g.drawString("/" + unit.health, x + 110 + x_delta, y + 55 + y_delta);
        g.resetFont();
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

    @Override
    public void keyPressed(int key, char c)
    {
        if(c == '1')
        {
            match.player_1.drawCard();
        }
        if(c == '2')
        {
            if(match.player_1.hand.size() > 0)
            {
                Card card = match.player_1.hand.get(0);
                match.player_1.hand.remove(0);
                match.player_1.deck[match.player_1.cardsInDeck] = card;
                match.player_1.cardsInDeck++;
                match.player_1.shuffleDeck();
            }
        }
    }

    public static void main(String[] args)
    {
        try
        {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new GameShell("Simple Slick Game", 1280, 800));
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

enum EnumInputState
{
    OFF_TURN,
    CARD_SELECT,
    TARGET_SELECT;
} 