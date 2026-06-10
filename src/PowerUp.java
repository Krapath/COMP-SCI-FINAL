
import java.awt.*;
import java.util.Random;
import javax.swing.*;

@SuppressWarnings("unused")

// TODO: maybe just use paint
public class PowerUp extends GameObject {

    Random r = new Random();
    PolygonGame game;
    public int buffType;
    public boolean readyToApply = false; // whether the buff should be applied
    // on mouse release
    public boolean wasPressed = false; // whether the mouse was pressed while hovering over this powerup (used to prevent applying the buff if the player clicks on a powerup and then drags the mouse away before releasing)
    public int[] xPointsBuff;
    public int[] yPointsBuff;
    public int posXBuff;
    public int posYBuff;
    public int nPointsBuff;

    public double radius;
    private Font pixelFont;
    String buff;
    static protected int numBuffs = 9;
    static int[] buffArray = new int[numBuffs]; 
    // keeps track of how many times the player has gotten each buff, used to determine how many sides the polygon for each buff should have and what number to display on the buff
    private Font descriptionFont;

    public int width;
    public int height;

    Image buffIcon;

    public int imageSize;
    static String[] buffNames = {"Health", "Speed", "AttackSpeed", "Lightning", "Missile", "Glaive", "MatchStick", "Dash",
        "ArrowSpread"};
    static Color[] buffColors = {Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.ORANGE, Color.MAGENTA,
        Color.YELLOW, Color.GRAY, Color.WHITE};
    String[] buffDescriptions = {"+5 Max Health", // Case 0 (RED / Health)
        "+1 Movement Speed", // Case 1 (GREEN / Speed)
        "+1 Attack Speed", // Case 2 (BLUE / Attack Speed)
        "Unlocks Chain Lightning", // Case 3 (CYAN / Lightning)
        "Unlocks Missile strikes", // Case 4 (ORANGE / Missile)
        "Spawns orbiting Glaives", // Case 5 (MAGENTA / Glaive)
        "Summons Matchstick", // Case 6 (YELLOW / MatchStick)
        "Press Space to Dash", // Case 7 (GRAY / Dash)
        "Unlocks Arrow Spread" // Case 8 (WHITE / ArrowSpread)
};

    String[] buffStackDescriptions = {"+5 Max Health [Per Stack]", "+1 Movement Speed [Per Stack]",
        "+1 Attack Speed [Per Stack]", "+1 Chain, +1 Damage [Per Stack]", "+2 Missile Damage [Per Stack]",
        " TBD[Per Stack]", "+1 Attack Speed, +1 Damage [Per Stack]", " TBD [Per Stack]",
        "+1 Arrow, +1 Damage [Per Stack]"};

    public PowerUp(int x, int y, PolygonGame game) {

        this.game = game;
        width = game.getWindowWidth() / 5;
        height = game.getWindowHeight() / 3;
        setSize(game.getWindowWidth() / 5, game.getWindowHeight() / 3);
        radius = (game.getWindowWidth() + game.getWindowHeight()) / 75;
        posXBuff = (game.getWindowWidth() / 5) / 2;
        posYBuff = (int) ((game.getWindowHeight() / 3) / 1.6 + radius * 2);

        setLocation(x, y);
        buffType = r.nextInt(buffNames.length);
        setColor(buffColors[buffType]);
        imageSize = (int) (width / 2.5);

        try {
            java.io.File fontFile = new java.io.File("Fonts/PressStart2P-Regular.ttf");
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont((int) radius / 2f);
            descriptionFont = pixelFont;
        } catch (Exception e) {
            // Fallback to basic monospaced if the file is missing
            pixelFont = new Font("Monospaced", Font.BOLD, 100);
            e.printStackTrace();
        }

        buffIcon = new ImageIcon("Images/BuffIcon/" + buffNames[buffType] + ".png").getImage();

    }

    // TODO: Change buffs to a method
    public void createBuff(int buffType) {
        buffArray[buffType]++;
        switch (buffType) {
            case 0:
                Player.health += 5;
                break;

            case 1:
                Player.speed += 1;
                break;

            case 2: // attack speed
                Player.attackCooldown -= 1;
                break;

            case 3: // lightning
                if (!Player.chainLightningActive) {
                    Player.chainLightningActive = true;
                } else {
                    ChainLightning.chainCount++;
                    ChainLightning.damage++;
                }
                break;

            case 4: // missile
                if (!Player.atgMissileActive) {
                    Player.atgMissileActive = true;
                } else {
                    AtGMissileMk1.damage += 2;
                }
                break;

            case 5:
                if (!Player.glaiveActive) {
                    game.numberOfGlaives = 3;
                    game.createGlaive(game.numberOfGlaives);
                    Player.glaiveActive = true;
                } else {
                    game.numberOfGlaives++;
                    game.createGlaive(game.numberOfGlaives);
                }
                break;
            case 6: // match stick
                if (!Player.matchStickActive) {
                    Player.matchStickActive = true;
                    game.matchStick = new MatchStick(game);
                    game.add(game.matchStick);
                } else {
                    Player.attackTimer += 1;
                    MatchStick.damage++;
                }
                break;

            case 7: // dash
                if (!Player.dashActive) {
                    Player.dashActive = true;
                    Dash dash = new Dash(game, game.player);
                }
                break;

            case 8: // arrow spread
                if (!Player.arrowSpreadActive) {
                    Player.arrowSpreadActive = true;
                    ArrowSpread arrowSpread = new ArrowSpread(game, game.player);
                } else {
                    ArrowSpread.arrowCount++;
                    ArrowSpread.damage++;
                }
                break;
        }
    }

    public void drawOutline(Graphics g, String text, int x, int y) {
        // Draw outline/shadow in black
        g.setColor(new Color(0, 0, 0, 255));
        g.drawString(text, x - 2, y);
        g.drawString(text, x + 2, y);
        g.drawString(text, x, y - 1);
        g.drawString(text, x, y + 1);

    }

    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g; // cast to Graphics2D to use thicker
        // lines
        Color color = getColor().darker();
        Color textColor = getColor().darker();
        int borderSize = getWidth()/50;

        g2d.setColor(color);
        g2d.fillRect(0, 0, width, borderSize);
        g2d.fillRect(0, height - borderSize, width, borderSize);
        g2d.fillRect(0, 0, borderSize, height);
        g2d.fillRect(width - borderSize, 0, borderSize, height);

        //SHADOW
        g.setColor(Color.BLACK);

        if (xPointsBuff != null && yPointsBuff != null) {
            g2d.setStroke(new BasicStroke(4));
            g2d.setColor(color);
            g2d.drawPolygon(xPointsBuff, yPointsBuff, nPointsBuff);

            g2d.setFont(pixelFont);
            FontMetrics metrics = g.getFontMetrics(pixelFont);

            int textWidth = metrics.stringWidth(buff);
            int textHeight = metrics.getAscent();

            int buffX = posXBuff - textWidth / 2 + 1;
            int buffY = posYBuff + textHeight / 2;
            g.drawString(buff, buffX + 2, buffY + 2);
            g.drawString(buff, buffX, buffY);
        }

        // shrink font until text fits within 90% of the powerup width
        float fontSize = (int) radius / 3f;
        Font testFont;
        FontMetrics descriptionMetrics;
        do {
            testFont = pixelFont.deriveFont(fontSize);
            descriptionMetrics = g.getFontMetrics(testFont);
            descriptionMetrics = g.getFontMetrics(descriptionFont);
            if (descriptionMetrics.stringWidth(buffDescriptions[buffType]) <= getWidth() * 0.9) {
                break;
            }
            fontSize -= 0.5f;
        } while (fontSize > 4); // just so it doesnt get too small, at that
        // point the text is basically unreadable anyway
        // and thus overflwo would be better

        descriptionFont = testFont;
        g.setFont(descriptionFont);

        // center the description text slightly below middle of powerup, center
        // it by adding width and subtracting half the text width
        drawOutline(g, buffDescriptions[buffType],
                getWidth() / 2 - (descriptionMetrics.stringWidth(buffDescriptions[buffType]) / 2),
                (int) (posYBuff / 1.3));
        g.setColor(textColor);
        g.drawString(buffDescriptions[buffType],
                0 + getWidth() / 2 - (descriptionMetrics.stringWidth(buffDescriptions[buffType]) / 2),
                (int) (posYBuff / 1.3));

        // TODO: could make method but very annoying and only used twice buit
        // also same for outline
        // same thing but for the buff stack descriptions
        float fontSizeStack = (int) radius / 3f;
        Font testFontStack;
        FontMetrics descriptionMetricsStack;
        do {
            testFontStack = pixelFont.deriveFont(fontSizeStack);
            descriptionMetricsStack = g.getFontMetrics(testFontStack);
            if (descriptionMetricsStack.stringWidth(buffStackDescriptions[buffType]) <= getWidth() * 0.9) {
                break;
            }
            fontSizeStack -= 0.5f;
        } while (fontSizeStack > 4);

        g.setFont(testFontStack);

        drawOutline(g, buffStackDescriptions[buffType],
                getWidth() / 2 - (descriptionMetricsStack.stringWidth(buffStackDescriptions[buffType]) / 2),
                (int) (posYBuff / 1.2));
        g.setColor(textColor);
        g.drawString(buffStackDescriptions[buffType],
                0 + getWidth() / 2 - (descriptionMetricsStack.stringWidth(buffStackDescriptions[buffType]) / 2),
                (int) (posYBuff / 1.2));

        g.setColor(color);

        g2d.drawImage(buffIcon, width / 2 - imageSize / 2, (height / 3 - imageSize / 2), imageSize, imageSize,null);

    }

    public void act() {

        if (!PolygonGame.gamePause) {
            return; // powerups only work while the player is choosing a buff
            // (optimize)
        }
        double buff1 = Math.PI / 2;

        double buff1Angle = Math.PI / 2;

        int mouseX = game.getMouseX();
        int mouseY = game.getMouseY();

        int sides;
        if (contains(mouseX, mouseY)) {
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
            readyToApply = true; // the buff should be applied on mouse release
            // if the mouse was pressed while hovering
            // over this powerup
        }
        if (game.mouseLeftPressed() && contains(mouseX, mouseY) && readyToApply) {
            wasPressed = true;
        }

        // apply the buff and remove the powerups if the player clicks on a
        // powerup and releases the mouse button while still hovering over the
        // same powerup
        if (wasPressed && !game.mouseLeftPressed() && contains(mouseX, mouseY) && readyToApply) { // TODO: turn into a method in game class for other application besides powerups (ex. clicking a "start game" button on the main menu)

            createBuff(buffType);

            for (int i = 0; i < game.powerUps.size(); i++) { // remove all
                // powerups from
                // the game
                game.remove(game.powerUps.get(i));
            }

            game.powerUps.clear(); // clear the list of powerups
            PolygonGame.gamePause = false; // allow the game to continue
            PolygonGame.choosingBuff = false;
            readyToApply = false; // reset readyToApply for the next time the
            // player chooses a buff
            wasPressed = false; // reset wasPressed for the next time the player
            // chooses a buff

        }

        if (!game.mouseLeftPressed()) {
            wasPressed = false;
        }
    }
}
