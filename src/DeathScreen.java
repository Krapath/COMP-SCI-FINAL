import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.FontMetrics;
import java.awt.Font;
import java.util.*;

public class DeathScreen extends GameObject {
    private static ArrayList<DeathScreen> deathScreenButtons = new ArrayList<>(); // list of all things in the class
    // set variables
    private String buttonName;
    PolygonGame game;
    Font menuFont;
    double hoverAngle = 0.2;
    boolean wasHoveredLastFrame = false;
    boolean tiltLeft = true;
    boolean hovered;
    Random r = new Random();

    public DeathScreen(PolygonGame game, String buttonName, boolean button, int w, int h, int x, int y) {
        this.game = game;
        this.buttonName = buttonName;
        setSize(w, h);
        setLocation(x, y);
        try {
            java.io.File fontFile = new java.io.File("Fonts/ZeroCool.ttf");
            menuFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(((45f)));
        } catch (Exception e) {
            // Fallback to basic monospaced if the file is missing
            menuFont = new Font("Monospaced", Font.BOLD, 100);
            e.printStackTrace();
        }
    }

    public void youDied() {
        // pause the game
        PolygonGame.gamePause = true;

        // sets up width and height for the death screen box based on the window size
        int w = (int) (game.getWindowWidth() / 1.5);
        int h = (int) (game.getWindowHeight() / 1.2);

        int x = (game.getWindowWidth() - w) / 2; // center the death screen horizontally
        int y = (game.getWindowHeight() - h) / 2; // center the death screen vertically

        // make the frame for the death screen
        DeathScreen deathScreen = new DeathScreen(game, "", false, w, h, x, y);// change the image
        deathScreen.setColor(new Color(220, 20, 60));
        game.add(deathScreen);
        deathScreenButtons.add(deathScreen);

        // set buttons for the death screen
        int buttonW = w / 3;
        int buttonH = h / 8;

        int buttonY = (int) (y + h * 0.9 - buttonH);
        int retryButtonX = (int) (x + game.getWindowWidth() / 20);
        int mainMenuButtonX = (int) (x + w - game.getWindowWidth() / 20 - buttonW);

        // make the retry button
        DeathScreen retryButton = new DeathScreen(game, "Retry", true, buttonW, buttonH, retryButtonX,
                buttonY);
        retryButton.setColor(new Color(15, 82, 186));
        game.add(retryButton);
        deathScreenButtons.add(retryButton);

        // make the return to main menu button
        DeathScreen ReturnToMainMenu = new DeathScreen(game, "Main Menu", true, buttonW, buttonH,
                mainMenuButtonX, buttonY);
        ReturnToMainMenu.setColor(new Color(15, 82, 186));
        game.add(ReturnToMainMenu);
        deathScreenButtons.add(ReturnToMainMenu);
    }

    // clears everything and sets the game to how it would start
    public void returnToZero() {
       //clear all enemies, projectiles, XP orbs, and power-ups
        for (Enemy e : PolygonGame.enemies) {
            game.remove(e);
        }
        PolygonGame.enemies.clear();
        for (Projectile p : PolygonGame.projectiles) {
            game.remove(p);
        }
        PolygonGame.projectiles.clear();
        for(PowerUp p : PolygonGame.powerUps) {
            game.remove(p);
        }
        PolygonGame.powerUps.clear();
        for(XpOrb x : PolygonGame.xpOrbs) {
            game.remove(x);
        }
        PolygonGame.xpOrbs.clear();
        //remove scaling for the enemies
        Enemy.healthMultiplier = 1;

        // reset player stats
        Player.size = (game.getWindowWidth() + game.getWindowHeight()) / 100;
        Player.speed = (game.getWindowWidth() + game.getWindowHeight()) / 200;
        Player.attackDelay = 0;
        Player.health = 20;
        Player.maxHealth = 20;
        Player.score = 0;
        Player.xp = 0;
        Player.level = 1;
        Player.chainLightningActive = false; // static so all projectiles have property
        Player.atgMissileActive = true; // static so all projectiles have property
        Player.glaiveActive = false;
        Player.yonduArrowActive = false;
        Player.invulnerableDuration = 30;
        
        //reset player
        game.remove(game.player);
        // reset debugger
        game.remove(game.debug);
    }

    @Override
    public void paint(Graphics g) {
        if (!PolygonGame.gamePause) {
            return; // only check for button clicks if we're on the main menu
        }
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(menuFont);
        FontMetrics metrics = g2d.getFontMetrics(menuFont);

        // center the text in the button
        int textWidth = metrics.stringWidth(buttonName);
        int textHeight = metrics.getAscent();
        int textX = getWidth() / 2 - textWidth / 2;
        int textY = getHeight() / 2 + textHeight / 2 - metrics.getDescent();

        g2d.setColor(Color.BLACK);
        if (hovered) {
            AffineTransform old = g2d.getTransform();
            g2d.rotate(hoverAngle, textX + textWidth / 2.0, textY - textHeight / 2.0);
            g2d.drawString(buttonName, textX, textY);
            g2d.setTransform(old);
        } else {
            hoverAngle = 0;
            g2d.drawString(buttonName, textX, textY);
        }
    }

    boolean readyToApply = false;
    boolean wasPressed = false;

    public void act() {
        if (!PolygonGame.gamePause) {
            return; // only check for button clicks if we're on the main menu
        }
        int x = game.getMouseX();
        int y = game.getMouseY();
        // ensure that clicking works properly
        if (!game.mouseLeftPressed()) {
            readyToApply = true;
        }
        if (game.mouseLeftPressed() && contains(x, y) && readyToApply) {
            wasPressed = true;
        }

        // glow when hovered over

        hovered = contains(game.getMouseX(), game.getMouseY());

        if (hovered) {
            setColor(Color.BLUE);
        } else {
            setColor(new Color(15, 82, 186));
        }

        if (hovered && !wasHoveredLastFrame) {
            if (tiltLeft) {
                hoverAngle = r.nextDouble() * 0.05 + 0.1;
                tiltLeft = false;
            } else if (!tiltLeft) {
                hoverAngle = -(r.nextDouble() * 0.05 + 0.1);
                tiltLeft = true;
            }
        }
        wasHoveredLastFrame = hovered;

        if (wasPressed && !game.mouseLeftPressed() && contains(x, y) && readyToApply) {

            if (buttonName.equals("Main Menu")) {
                for (DeathScreen m : deathScreenButtons) { // removes all the buttons in the list from game
                    game.remove(m);
                }
                deathScreenButtons.clear(); // clears the entire list
                game.menuController.spawnMyBoxes(game); // spawns the main menu buttons
                returnToZero();
            }
            readyToApply = false;
            wasPressed = false;
        }
        if (!game.mouseLeftPressed()) {
            wasPressed = false;
        }
        repaint();
    }
}
