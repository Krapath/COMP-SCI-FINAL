
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class DisplayGUI extends GameObject {

    PolygonGame game;
    static int[] xPointsHealth;
    static int[] yPointsHealth;
    static int[] xPointsLevel;
    static int[] yPointsLevel;

    static ArrayList<DisplayGUI> levelBar = new ArrayList<DisplayGUI>();
    static int barDefinitionLevel = 100; // controls how many sections the bar is split into

    static int nPointsHealth;
    static int nPointsLevel;
    static int posXHealth;
    static int posYHealth;
    static int posXLevel;
    static int posYLevel;
    static double radius;

    private Font pixelFont;

    static int borderWidth = 4;

    public DisplayGUI(PolygonGame game) {
        this.game = game;
        setSize(game.getWindowWidth(), game.getWindowHeight()); // size of the text area
        radius = (game.getWindowWidth() + game.getWindowHeight()) / 35;

        try {
            java.io.File fontFile = new java.io.File("Fonts/PressStart2P-Regular.ttf");
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont((int) radius / 2f);
        } catch (Exception e) {
            // Fallback to basic monospaced if the file is missing
            pixelFont = new Font("Monospaced", Font.BOLD, 100);
            e.printStackTrace();
        }
    }


    public void act() {
        // reposition every tick so text stays in corner
        double healthAngle = Math.PI / 2;
        xPointsHealth = new int[Player.health + 2];
        yPointsHealth = new int[Player.health + 2];
        posXHealth = (int) (0 + radius * (2));
        posYHealth = (int) (game.getWindowHeight() - radius * 1.5);
        for (int i = 0; i < xPointsHealth.length; i++) {

            double x = (radius * Math.cos(healthAngle) + posXHealth);
            double y = (radius * Math.sin(healthAngle) + posYHealth);

            xPointsHealth[i] = (int) Math.round(x + 0.0001);
            yPointsHealth[i] = (int) Math.round(y + 0.0001);

    
            healthAngle += Math.PI * 2 / (Player.health + 2);

        }

        nPointsHealth = Player.health + 2;

        double levelAngle = Math.PI / 2;
        xPointsLevel = new int[Player.level + 2];
        yPointsLevel = new int[Player.level + 2];
        posXLevel = (int) (game.getWindowWidth() - radius * (2));
        posYLevel = (int) (game.getWindowHeight() - radius * 1.5);
        for (int i = 0; i < xPointsLevel.length; i++) {

            double x = (radius * Math.cos(levelAngle) + posXLevel);
            double y = (radius * Math.sin(levelAngle) + posYLevel);

            xPointsLevel[i] = (int) Math.round(x + 0.0001);
            yPointsLevel[i] = (int) Math.round(y + 0.0001);
 

            
            levelAngle += Math.PI * 2 / (Player.level + 2);
        }

        nPointsLevel = Player.level + 2;

        repaint(); // redraws this object every tick

    }

    public void paint(Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g; // cast to Graphics2D to use thicker lines

        g.setColor(Color.WHITE);
        String health = String.valueOf(Player.health);
        String level = String.valueOf(Player.level);

        // draw mouse and player coordinates
        g.drawString("MOUSE    X:" + game.getMouseX() + "  Y:" + game.getMouseY(), 10, 20);
        g.drawString("PLAYER   X:" + game.player.getX() + "  Y:" + game.player.getY(), 10, 40);

        // finds the angle between the player and the mouse cursor and displays it in
        // degrees
        double angle = game.getAngle(game.player.getX(), game.player.getY(), game.getMouseX(), game.getMouseY())
                * (180 / Math.PI);
        angle = -angle; // increase counterclockwise, follows standard unit circle convention
        if (angle < 0) { // display angle as a positive value between 0 and 360
            angle += 360;
        }

        // display the number of enemies and hp
        g.drawString("ENEMIES: " + PolygonGame.enemies.size(), 10, 80);
        g.drawString("HP: " + Player.health, 10, 100);

        // display the players current score
        g.drawString("SCORE: " + Player.score, 10, 120);
        // display the players xp and xp required to level up
        g.drawString("XP: " + Player.xp + "/" + Player.xpReq, 10, 140);

        // display the players level
        g.drawString("LEVEL: " + Player.level, 10, 160);

        // display the mouse cursor as a red rectangle
        g.drawString("ANGLE:" + angle, 10, 60);
        g.setColor(Color.RED);
        g.drawRect(game.getMouseX(), game.getMouseY(), 15, 15);

        // draw healthbar
        if (xPointsHealth != null && yPointsHealth != null) {
            g2d.setStroke(new BasicStroke(4));
            g2d.drawPolygon(xPointsHealth, yPointsHealth, nPointsHealth);
            g2d.setFont(pixelFont);
            FontMetrics metrics = g.getFontMetrics(pixelFont);

            int textWidth = metrics.stringWidth(health);
            int textHeight = metrics.getAscent();

            int healthX = posXHealth - textWidth / 2 + 1;
            int healthY = posYHealth + textHeight / 2;

            g.drawString(health, healthX, healthY);
        }
        // draws exp Bar
        if (xPointsLevel != null && yPointsLevel != null) {
            g2d.setStroke(new BasicStroke(4));
            g2d.setColor(Color.CYAN);
            g2d.drawPolygon(xPointsLevel, yPointsLevel, nPointsLevel);
            g2d.setFont(pixelFont);
            FontMetrics metrics = g.getFontMetrics(pixelFont);

            int textWidth = metrics.stringWidth(level);
            int textHeight = metrics.getAscent();

            int levelX = posXLevel - textWidth / 2 + 1;
            int levelY = posYLevel + textHeight / 2;

            g.drawString(level, levelX, levelY);
        }
        if (PolygonGame.choosingBuff) {
            g2d.setColor(new Color(0, 0, 0, 120));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

    }
}
