
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.io.*;

public class Highscores extends GameObject {

    private static ArrayList<Highscores> highscoresButtons = new ArrayList<>(); // list of all things in the class
    // set variables
    private static final File highscores = new File("highscore.txt");
    private Image boxImage;
    private String buttonName;
    PolygonGame game;
    Font menuFont;
    Font textFont;
    double hoverAngle = 0.2;
    boolean wasHoveredLastFrame = false;
    boolean tiltLeft = true;
    boolean hovered;
    public int[] scores = new int[5];
    public static int w, h, x, y;

    

    Random r = new Random();

    // set up dummy consturctor for highscores buttons to spawn
    public Highscores(PolygonGame game, String buttonName, int w, int h, int x, int y) {
		if (!highscores.exists()){
			try
		     {
		       highscores.createNewFile();

		     }
		     catch(IOException ex)
		     {
		    	 System.out.println("No file");
		     }
		}
		
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
        
        
        try {
            java.io.File fontFile = new java.io.File("Fonts/ZeroCool.ttf");
            textFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(((100f)));
        } catch (Exception e) {
            // Fallback to basic monospaced if the file is missing
            textFont = new Font("Monospaced", Font.BOLD, 200);
            e.printStackTrace();
        }
        
        
    }
    
    
    /*
    public void setScores (int[] scores, int newScore) {
    static FileWriter fw = new FileWriter(highscores);
    static BufferedWriter bw = new BufferedWriter(fw);
    	int first = 0, second = 0, third = 0, fourth = 0, fifth = 0;
    	
    	for (int i = 0; i < scores.length; i++) {
    		if (scores[i] > first) 
    			first = scores[i];
    	}
    	
    }
    
    */

    public void spawnHighscores(PolygonGame game) {
        // sets up width and height for the highscores box based on the window size
        w = (int) (game.getWindowWidth() / 1.5);
        h = (int) (game.getWindowHeight() / 1.2);

        x = (game.getWindowWidth() - w) / 2; // center the highscores horizontally
        y = (game.getWindowHeight() - h) / 2; // center the highscores vertically

        // make the shift for the back button
        int setShift = (int) ((x + y) / 10);

        // make the frame for the highscores
        Highscores highscores = new Highscores(game, "Highscores", w, h, x, y);
        highscores.setColor(new Color(220, 20, 60));
        game.add(highscores);
        highscoresButtons.add(highscores);

        // make the back button
        Highscores backButton = new Highscores(game, "Back", w / 4, h / 10, x - setShift, y - setShift);
        backButton.setColor(new Color(15, 82, 186));
        game.add(backButton);
        highscoresButtons.add(backButton);
        

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

        super.paint(g);
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
        
        g2d.setStroke(new BasicStroke(4));
        g2d.setColor(Color.CYAN);
        g2d.setFont(textFont);

        g.drawString("1. ", w/8, h/5);

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
                for (Highscores m : highscoresButtons) { // removes all the buttons in the list from game
                    game.remove(m);
                }
                highscoresButtons.clear(); // clears the entire list
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
