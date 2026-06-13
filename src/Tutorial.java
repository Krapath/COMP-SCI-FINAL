
/** A menu page with explainations of how to play the game
 * author: Raymond
 */
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

    // array list of all objects in the class
    private static ArrayList<Tutorial> tutorialButtons = new ArrayList<>();
    // set dummy constructor variables + objects
    private String buttonName;
    private Image boxImage;
    PolygonGame game;
    // set font variables
    Font menuFont;
    double hoverAngle = 0.2;
    boolean wasHoveredLastFrame = false;
    boolean tiltLeft = true;
    boolean hovered;
    // create random object
    Random r = new Random();

    /**
     * Set up a dummy constructor to build the boxes
     */
    public Tutorial(PolygonGame game, String buttonName, int w, int h, int x, int y, Image boxImage) {
        // assign values to variables of objects
        this.game = game;
        this.buttonName = buttonName;
        if (boxImage != null) {
            this.boxImage = boxImage;
        }
        setSize(w, h);
        setLocation(x, y);
        try {
            java.io.File fontFile = new java.io.File("Fonts/ZeroCool.ttf");
            menuFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        } catch (Exception e) {
            // Fallback to basic monospaced if the file is missing
            menuFont = new Font("Monospaced", Font.BOLD, 100);
            e.printStackTrace();
        }
        // scale text to window size
        float scaleFactor = (game.getWindowHeight() + game.getWindowWidth()) / 3000f;
        if (scaleFactor <= 0) {
            scaleFactor = 1.0f; // Prevention fail-safe

                }menuFont = menuFont.deriveFont(45 * scaleFactor);
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
        // tutorial button
        Tutorial tutorial = new Tutorial(game, "Tutorial", w, h, x, y,
                new ImageIcon("Images\\Tutorial\\Tutorial.png").getImage());
        tutorial.setColor(new Color(220, 20, 60));
        game.add(tutorial);
        tutorialButtons.add(tutorial);
        // back button
        Tutorial backButton = new Tutorial(game, "Back", w / 4, h / 10, x - setShift, y - setShift, null);
        backButton.setColor(new Color(15, 82, 186));
        game.add(backButton);
        tutorialButtons.add(backButton);
    }

    @Override
    public void paint(Graphics g) {
        if (!PolygonGame.gamePause) {
            return; // only run if on tutorial
        }
        super.paint(g); // paints background on the bottom
        // draws image on box
        if (boxImage != null) {
            g.drawImage(boxImage, 0, 0, getWidth(), getHeight(), null);

            return; // skip other changes object has an image
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
        // change the back button when mouse is hovering over it
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

    public void act() {
        if (!PolygonGame.gamePause) {
            return; // only run if game is paused
        }
        int mouseX = game.getMouseX();
        int mouseY = game.getMouseY();
        // if hovering over back button make a sound and tilt the text
        if (buttonName.equals("Back")) {
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
            if (hovered) {
                setColor(Color.BLUE);
            } else {
                setColor(new Color(15, 82, 186));
            }
        }
        wasHoveredLastFrame = hovered;
        // make a sound effect for when the back button is clicked then return to main
        // menu
        if (isClickedAndReleased(game, mouseX, mouseY)) {
            SoundEffects("SFX/CLICK.wav", 5.0f);
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
        }
    }
}
