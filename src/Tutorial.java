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

public class Tutorial extends GameObject {
    private static ArrayList<Tutorial> tutorialButtons = new ArrayList<>(); // list of all things in the class
    // set variables
    private String buttonName;
    private Image boxImage;
    PolygonGame game;
    Font menuFont;
    double hoverAngle = 0.2;
    boolean wasHoveredLastFrame = false;
    boolean tiltLeft = true;
    boolean hovered;

    Random r = new Random();

    // set up dummy consturctor for tutorial buttons to spawn
    public Tutorial(PolygonGame game, String buttonName, int w, int h, int x, int y, Image boxImage) {
        this.game = game;
        this.buttonName = buttonName;
        if (boxImage != null) {
            this.boxImage = boxImage;
        }
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

    public void spawnTutorial(PolygonGame game) {
        // sets up width and height for the tutorial box based on the window size
        int w = (int) (game.getWindowWidth() / 1.5);
        int h = (int) (game.getWindowHeight() / 1.2);

        int x = (game.getWindowWidth() - w) / 2; // center the tutorial horizontally
        int y = (game.getWindowHeight() - h) / 2; // center the tutorial vertically

        boxImage = new ImageIcon("Images\\Tutorial\\Tutorial.png").getImage();

        // make the shift for the back button
        int setShift = (int) ((x + y) / 10);

        // make the frame for the tutorial
        Tutorial tutorial = new Tutorial(game, "Tutorial", w, h, x, y,
                new ImageIcon("Images\\Tutorial\\Tutorial.png").getImage());
        tutorial.setColor(new Color(220, 20, 60));
        game.add(tutorial);
        tutorialButtons.add(tutorial);

        // make the back button
        Tutorial backButton = new Tutorial(game, "Back", w / 4, h / 10, x - setShift, y - setShift, null);
        backButton.setColor(new Color(15, 82, 186));
        game.add(backButton);
        tutorialButtons.add(backButton);
    }

    @Override
    public void paint(Graphics g) {
        if (!PolygonGame.gamePause) {
            return; // only check if we're on the main menu
        }
        super.paint(g); // paints the background first of the button first

        // adds the image on top of the background WILL USED LATER
        if (boxImage != null) {
            g.drawImage(boxImage, 0, 0, getWidth(), getHeight(), null);
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(menuFont);
        FontMetrics metrics = g2d.getFontMetrics(menuFont);
        hovered = contains(game.getMouseX(), game.getMouseY());

        // center the text in the button
        int textWidth = metrics.stringWidth(buttonName);
        int textHeight = metrics.getAscent();
        int textX = getWidth() / 2 - textWidth / 2;
        int textY = getHeight() / 2 + textHeight / 2 - metrics.getDescent();

        g2d.setColor(Color.BLACK);
        if (hovered && buttonName.equals("Back")) {
            AffineTransform old = g2d.getTransform();
            g2d.rotate(hoverAngle, textX + textWidth / 2.0, textY - textHeight / 2.0);
            g2d.drawString(buttonName, textX, textY);
            g2d.setTransform(old);
        } else if (buttonName.equals("Back")) {
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

        if (hovered && !wasHoveredLastFrame) {
            if (tiltLeft) {
                hoverAngle = r.nextDouble() * 0.05 + 0.1;
                tiltLeft = false;
            } else if (!tiltLeft) {
                hoverAngle = -(r.nextDouble() * 0.05 + 0.1);
                tiltLeft = true;
            }
        }

        if (hovered && buttonName.equals("Back")) {
            setColor(Color.BLUE);
        } else if (buttonName.equals("Back")) {
            setColor(new Color(15, 82, 186));
        }

        wasHoveredLastFrame = hovered;

        if (!game.mouseLeftPressed()) {
            readyToApply = true;
        }
        if (game.mouseLeftPressed() && contains(x, y) && readyToApply) {
            wasPressed = true;
        }

        if (wasPressed && !game.mouseLeftPressed() && contains(x, y) && readyToApply) {

            if (buttonName.equals("Back")) {
                for (Tutorial m : tutorialButtons) { // removes all the buttons in the list from game
                    game.remove(m);
                }
                tutorialButtons.clear(); // clears the entire list
                // returns to the main menu
                MainMenu menuController = new MainMenu(game, "");
                menuController.spawnMyBoxes(game);
                add(menuController);
            }
            readyToApply = false;
            wasPressed = false;
        }
        if (!game.mouseLeftPressed()) {
            wasPressed = false;
        }
    }

}
