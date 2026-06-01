
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

@SuppressWarnings("unused")

//TODO: maybe just use paint 
public class PowerUp extends GameObject {

    Random r = new Random();
    PolygonGame game;
    public int buffType;
    public boolean readyToApply = false; // whether the buff should be applied on mouse release
    public boolean wasPressed = false; // whether the mouse was pressed while hovering over this powerup (used to prevent applying the buff if the player clicks on a powerup and then drags the mouse away before releasing)
    public int[] xPointsBuff;
    public int[] yPointsBuff;
    public int posXBuff;
    public int posYBuff;
    public int nPointsBuff;

    public double radius;
    private Font pixelFont;
    String buff;
    static int[] buffArray = new int[7];

    static String[] buffNames = {"Health", "Speed", "Attack Speed", "Lightning", "Missile", "Glaive", "Arrow"};
    static Color[] buffColors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE, Color.MAGENTA, Color.CYAN};
    String[] buffDescriptions = {
    	    "+5 Max Health",                                 // Case 0 (RED / Health)
    	    "+1 Movement Speed",                             // Case 1 (GREEN / Speed)
    	    "+1 Attack Speed (Decreases Attack Delay)",      // Case 2 (BLUE / Attack Speed)
    	    "Unlocks Chain Lightning attacks",               // Case 3 (YELLOW / Lightning)
    	    "Unlocks ATG Missile strikes",                   // Case 4 (ORANGE / Missile)
    	    "Spawns 3 orbiting Glaives around the player",   // Case 5 (MAGENTA / Glaive)
    	    "Summons the controllable Yondu Arrow"           // Case 6 (CYAN / Arrow)
    	};
    
    public PowerUp(int x, int y, PolygonGame game) {

        this.game = game;
        setSize(game.getWindowWidth() / 5, game.getWindowHeight() / 3);
        radius = (game.getWindowWidth() + game.getWindowHeight()) / 75;
        posXBuff = (game.getWindowWidth() / 5) / 2;
        posYBuff = (int) ((game.getWindowHeight() / 3) / 2 + radius);

        setLocation(x, y);
        buffType = r.nextInt(buffNames.length);
        setColor(buffColors[buffType]);

        try {
            java.io.File fontFile = new java.io.File("Fonts/PressStart2P-Regular.ttf");
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont((int) radius / 2f);

        } catch (Exception e) {
            // Fallback to basic monospaced if the file is missing
            pixelFont = new Font("Monospaced", Font.BOLD, 100);
            e.printStackTrace();
        }

    }

    //TODO: Change buffs to a method
    public void applyBuff(int buffType) {
        buffArray[buffType]++;
        switch (buffType) {
            case 0:
                Player.health += 5;
                break;

            case 1:
                Player.speed += 1;
                break;

            case 2:
                Player.attackDelay += 1;
                break;

            case 3:
                if (!Player.chainLightningActive) {
                    Player.chainLightningActive = true;
                }
                break;

            case 4:
                if (!Player.atgMissileActive) {
                    Player.atgMissileActive = true;
                }
                break;

            case 5:
                if (!Player.glaiveActive) {
                    game.glaive0 = new Glaive(game, 0.0);
                    game.add(game.glaive0);
                    game.glaive1 = new Glaive(game, 2 * Math.PI / 3);
                    game.add(game.glaive1);
                    game.glaive2 = new Glaive(game, 4 * Math.PI / 3);
                    game.add(game.glaive2);
                }
                break;

            case 6:
                if (!Player.yonduArrowActive) {
                    game.yonduArrow = new YonduArrow(game);
                    game.add(game.yonduArrow);
                }
                break;
        }
    }

    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g; // cast to Graphics2D to use thicker lines

        if (xPointsBuff != null && yPointsBuff != null) {
            g2d.setStroke(new BasicStroke(4));
            Color color = getColor().darker();
            g2d.setColor(color);
            g2d.drawPolygon(xPointsBuff, yPointsBuff, nPointsBuff);

            g2d.setFont(pixelFont);
            FontMetrics metrics = g.getFontMetrics(pixelFont);

            int textWidth = metrics.stringWidth(buff);
            int textHeight = metrics.getAscent();

            int buffX = posXBuff - textWidth / 2 + 1;
            int buffY = posYBuff + textHeight / 2;

            g.drawString(buff, buffX, buffY);
        }
        
        g.drawString(buffDescriptions[buffType],posXBuff/2,posYBuff);

    }

    public void act() {

        if (!PolygonGame.gamePause) {
            return; // powerups only work while the player is choosing a buff (optimize)
        }
        double buff1 = Math.PI / 2;

        double buff1Angle = Math.PI / 2;

        int x = game.getMouseX();
        int y = game.getMouseY();

        int sides;
        if (contains(x, y)) {
            sides = buffArray[buffType] + 2 + 1;
            buff = String.valueOf(buffArray[buffType] + 1);
        } else {
            sides = buffArray[buffType] + 2;
            buff = String.valueOf(buffArray[buffType]);
        }

        xPointsBuff = new int[sides];
        yPointsBuff = new int[sides];

        for (int i = 0; i < sides; i++) {
            int centerX = posXBuff;
            int centerY = posYBuff;

            double x1 = radius * Math.cos(buff1Angle) + centerX;
            double y1 = radius * Math.sin(buff1Angle) + centerY;

            xPointsBuff[i] = (int) Math.round(x1 + 0.0001);
            yPointsBuff[i] = (int) Math.round(y1 + 0.0001);

            buff1Angle += Math.PI * 2 / sides;
        }

        nPointsBuff = sides;

        if (!game.mouseLeftPressed()) {
            readyToApply = true; // the buff should be applied on mouse release if the mouse was pressed while hovering over this powerup
        }
        if (game.mouseLeftPressed() && contains(x, y) && readyToApply) {
            wasPressed = true;
        }

        //apply the buff and remove the powerups if the player clicks on a powerup and releases the mouse button while still hovering over the same powerup
        if (wasPressed && !game.mouseLeftPressed() && contains(x, y) && readyToApply) {  // TODO: turn into a method in game class for other application besides powerups (ex. clicking a "start game" button on the main menu)
            applyBuff(buffType);

            for (int i = 0; i < game.powerUps.size(); i++) { // remove all powerups from the game
                game.remove(game.powerUps.get(i));
            }

            game.powerUps.clear(); // clear the list of powerups
            PolygonGame.gamePause = false; // allow the game to continue
            PolygonGame.choosingBuff = false;
            readyToApply = false; // reset readyToApply for the next time the player chooses a buff
            wasPressed = false; // reset wasPressed for the next time the player chooses a buff

        }

        if (!game.mouseLeftPressed()) {
            wasPressed = false;
        }
    }
}
