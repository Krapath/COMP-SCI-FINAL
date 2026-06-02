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
    double hoverAngle=0.2;
    boolean wasHoveredLastFrame = false;
    boolean tiltLeft = true;
    boolean hovered;


        public DeathScreen(PolygonGame game, String buttonName, int w, int h,int x, int y) {
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

    public void youDied(){
        // sets up width and height for the death screen box based on the window size
        int w = (int) (game.getWindowWidth() / 1.5);
        int h = (int) (game.getWindowHeight() / 1.2);

        int x = (game.getWindowWidth() - w) / 2; // center the death screen horizontally
        int y = (game.getWindowHeight() - h) / 2; // center the death screen vertically

        // make the shift for the back button
        int setShift = (int) ((x + y) / 10);

        // make the frame for the death screen
        DeathScreen deathScreen = new DeathScreen(game, "DeathScreen", w, h, x, y);// change the image
        deathScreen.setColor(new Color(220, 20, 60));
        game.add(deathScreen);
        deathScreenButtons.add(deathScreen);

        // make the back button
        DeathScreen backButton = new DeathScreen(game, "Back", w / 4, h / 10, x - setShift, y - setShift);
        backButton.setColor(new Color(15, 82, 186));
        game.add(backButton);

    }

    public void act(){

    }
    
}
