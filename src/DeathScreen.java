import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;

public class DeathScreen extends GameObject {
    // set variables
    private static ArrayList<DeathScreen> deathScreenButtons = new ArrayList<>(); // list of all things in the class
    private String buttonName;
    private Image deathImage;
    Random r = new Random();
    PolygonGame game;
    Font menuFont;
    double hoverAngle = 0.2;
    boolean wasHoveredLastFrame = false;
    boolean tiltLeft = true;
    boolean hovered;
    int gameOverOffsetX = 0;
    int gameOverOffsetY = 0;
    int gameOverWiggleRange = 10;
    int gameOverWiggleDelay = 0;
    Font gameOverFont;

    /**
     * dummy constructor for death screen variables
     */
    public DeathScreen(PolygonGame game, String buttonName, boolean button, int w, int h, int x, int y,
            ImageIcon image) {
        // define variables for each object
        this.game = game;
        this.buttonName = buttonName;
        setSize(w, h);
        setLocation(x, y);
        // set image
        if (image != null) {
            this.deathImage = image.getImage();
        } else {
            // fail-safe if image file is missing
            this.deathImage = null;
        }
        // set font
        try {
            java.io.File fontFile = new java.io.File("Fonts/ZeroCool.ttf");
            menuFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        } catch (Exception e) {
            // fallback to basic monospaced if the file is missing
            menuFont = new Font("Monospaced", Font.BOLD, 100);
            e.printStackTrace();
        }
        // scale font
        float scaleFactor = (game.getWindowHeight() + game.getWindowWidth()) / 3000f;
        if (scaleFactor <= 0)
            scaleFactor = 1.0f; // prevention fail-safe
        menuFont = menuFont.deriveFont(45 * scaleFactor);
        // scale game over font
        gameOverFont = menuFont.deriveFont(85 * scaleFactor);
    }

    public void youDied() {
        // pause the game
        PolygonGame.gamePause = true;

        // sets up width and height for the death screen box based on the window size
        int w = (int) (game.getWindowWidth() / 1.5);
        int h = (int) (game.getWindowHeight() / 1.2);

        int x = (game.getWindowWidth() - w) / 2; // center the death screen horizontally
        int y = (game.getWindowHeight() - h) / 2; // center the death screen vertically

        // pick a random image for death screen
        int rand = r.nextInt(4) + 1; // 1-2
        ImageIcon randomImage = new ImageIcon("Images/Death/Death" + rand + ".png");

        // make the frame for the death screen
        DeathScreen deathScreen = new DeathScreen(game, "doNotChange", false, w, h, x, y,
                randomImage);// change the image
        deathScreen.setColor(new Color(220, 20, 60));
        game.add(deathScreen);
        deathScreenButtons.add(deathScreen);

        // set buttons variables for the death screen
        int buttonW = w / 3;
        int buttonH = h / 8;

        int buttonY = (int) (y + h * 0.9 - buttonH);
        int retryButtonX = (int) (x + game.getWindowWidth() / 20);
        int mainMenuButtonX = (int) (x + w - game.getWindowWidth() / 20 - buttonW);

        // make the retry button
        DeathScreen retryButton = new DeathScreen(game, "Retry", true, buttonW, buttonH, retryButtonX,
                buttonY, null);
        retryButton.setColor(new Color(15, 82, 186));
        game.add(retryButton);
        deathScreenButtons.add(retryButton);

        // make the return to main menu button
        DeathScreen ReturnToMainMenu = new DeathScreen(game, "Main Menu", true, buttonW, buttonH,
                mainMenuButtonX, buttonY, null);
        ReturnToMainMenu.setColor(new Color(15, 82, 186));
        game.add(ReturnToMainMenu);
        deathScreenButtons.add(ReturnToMainMenu);
        SoundEffects.play("SFX/GAME_OVER.wav", -15.0f);
    }

    // clears everything and sets the game to how it would start
    public void returnToZero() {
        // remove all death screen buttons
        for (DeathScreen m : deathScreenButtons) {
            game.remove(m);
        }
        deathScreenButtons.clear();

        // clear all enemies, projectiles, XP orbs, and power-ups
        for (Enemy e : PolygonGame.enemies) {
            game.remove(e);
        }
        PolygonGame.enemies.clear();
        for (Weapon w : Player.weapons) {
            game.remove(w);
        }
        Player.weapons.clear();
        Player.abilities.clear();
        PowerUp.buffArray = new int[PowerUp.numBuffs]; // reset the buff array to all 0s
        for (XpOrb x : PolygonGame.xpOrbs) {
            game.remove(x);
        }
        PolygonGame.xpOrbs.clear();

        // reset scaling for the enemies
        Enemy.healthMultiplier = 1;

        // reset player stats
        Player.size = (game.getWindowWidth() + game.getWindowHeight()) / 100;
        Player.speed = (game.getWindowWidth() + game.getWindowHeight()) / 200;
        Player.attackTimer = 0;
        Player.health = 10;
        Player.maxHealth = 10;
        Player.score = 0;
        Player.level = 1;
        Player.xpReq = Math.floor(2 * Player.level * Math.log(Player.level + 1));
        Player.xp = 0;
        Player.chainLightningActive = false;
        Player.atgMissileActive = false;
        Player.dashActive = false;
        Player.arrowSpreadActive = false;
        Player.glaiveActive = false;
        Player.matchStickActive = false;
        Player.invulnerable = false;
        Player.invulnerableDuration = 30;
        game.killCounter = 0;
        SpawnAnimation.playerTransparency = 0;
        ChainLightning.chainCount = 3;
        ChainLightning.damage = 1;
        AtGMissileMk1.damage = 1;
        Glaive.damage = 1;
        Glaive.glaiveCount = 0;
        MatchStick.damage = 1;
        ArrowSpread.arrowCount = 5;
        ArrowSpread.damage = 1;

        // reset player
        game.remove(game.player);
        // reset debugger
        game.remove(game.debug);
    }

    @Override
    public void paint(Graphics g) {
        if (!PolygonGame.gamePause) {
            return; // only change images if on death screen
        }
        super.paint(g);
        if (buttonName.equals("doNotChange")) {
            // paint the death image
            if (deathImage != null) {
                int imgW = (int) (getWidth() / 1.8);
                int imgH = (int) (getHeight() / 2.5);
                int imgX = (getWidth() - imgW) / 2;
                int imgY = (int) (getHeight() * 0.25); // lower on the panel
                g.drawImage(deathImage, imgX, imgY, imgW, imgH, null);
            }
            Graphics2D g2d = (Graphics2D) g;
            g2d.setFont(gameOverFont);
            FontMetrics metrics = g2d.getFontMetrics(gameOverFont);
            String gameOverText = "GAME OVER";
            int textWidth = metrics.stringWidth(gameOverText);
            int textHeight = metrics.getAscent();
            int textX = getWidth() / 2 - textWidth / 2 + gameOverOffsetX;
            int textY = (int) (getHeight() * 0.12) + textHeight / 2 + gameOverOffsetY; // keep text above the image

            g2d.setColor(Color.BLACK);
            g2d.drawString(gameOverText, textX + 2, textY + 2);

            return; // don't draw text for the background box
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(menuFont);
        FontMetrics metrics = g2d.getFontMetrics(menuFont);

        // center the text in the button
        int textWidth = metrics.stringWidth(buttonName);
        int textHeight = metrics.getAscent();
        int textX = getWidth() / 2 - textWidth / 2;
        int textY = getHeight() / 2 + textHeight / 2 - metrics.getDescent();

        g2d.setColor(Color.BLACK);
        // make the buttons rotate if hovered on
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
        if (buttonName.equals("doNotChange")) {
            gameOverWiggleDelay++;
            if (gameOverWiggleDelay >= 3) {
                gameOverWiggleDelay = 0;
                gameOverOffsetX = r.nextInt(gameOverWiggleRange * 2 + 1) - gameOverWiggleRange;
                gameOverOffsetY = 2 * r.nextInt(gameOverWiggleRange * 2 + 1) - gameOverWiggleRange;
            }
            return; // don't check for clicks on the background box
        }
        int mouseX = game.getMouseX();
        int mouseY = game.getMouseY();
        hovered = contains(mouseX, mouseY);
        // change color if hovered on
        if (hovered) {
            setColor(Color.BLUE);
        } else {
            setColor(new Color(15, 82, 186));
        }
        // play and sound effect and tilt the fram if hovered on
        if (hovered && !wasHoveredLastFrame) {
            SoundEffects("SFX/HOVER.wav", -5.0f);
            if (tiltLeft) {
                hoverAngle = r.nextDouble() * 0.05 + 0.1;
                tiltLeft = false;
            } else if (!tiltLeft) {
                hoverAngle = -(r.nextDouble() * 0.05 + 0.1);
                tiltLeft = true;
            }
        }
        wasHoveredLastFrame = hovered;

        if (isClickedAndReleased(game, mouseX, mouseY)) {
            SoundEffects("SFX/CLICK.wav", 5.0f);
            // main menu button
            if (buttonName.equals("Main Menu")) {
                returnToZero();
                game.menuController.spawnMyBoxes(game); // spawns the main menu buttons

            }
            // retry button
            else if (buttonName.equals("Retry")) {
                SoundEffects("SFX/SPAWN_SOUND.wav", -4.0f);
                returnToZero();
                game.spawnGame(); // start the game again
                PolygonGame.gamePause = false;
            }
        }
        repaint();
    }
}