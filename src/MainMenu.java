import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.util.Random;

import javax.swing.ImageIcon;

public class MainMenu extends GameObject {
    private static ArrayList<MainMenu> menuButtons = new ArrayList<>(); // list for the actual buttons, shared aross the
                                                                        // entire class
    private Image logoImage;
    private String buttonName;
    PolygonGame game;
    private Font menuFont;
    double hoverAngle;
    boolean hovered;
    Random r = new Random();
    private boolean wasHoveredLastFrame = false;
    boolean tiltLeft = true;

    static int menuOffsetX;
    static int menuOffsetY;
    static double sizeMultiplier = 1.7; // higher is actually lower
    // The constructor for the dummy object

    int logoW;
    int logoH;
    int logoX;
    int logoY;
    static int logoOffsetX;
    static int logoOffsetY;
    static double logoSizeMultiplier = 0.5; // higher = smaller
    public MainMenu(PolygonGame game, String buttonName) {
    
        this.game = game;
        this.buttonName = buttonName;
        try {
            java.io.File fontFile = new java.io.File("Fonts/ZeroCool.ttf"); 
            menuFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(((90f/(float)sizeMultiplier)));
        } catch (Exception e) {
            // Fallback to basic monospaced if the file is missing
            menuFont = new Font("Monospaced", Font.BOLD, 100);
            e.printStackTrace();
        }
            menuOffsetX =-game.getWindowHeight()/2;
            menuOffsetY =game.getWindowWidth()/5;
            logoImage = new ImageIcon("Images/MainMenu/PolygonLogo.png").getImage();
            logoOffsetX =game.getWindowWidth()/4;
            logoOffsetY =game.getWindowHeight()/12;


    }

    /**
     * Spawns the boxes for the main menu.
     */
    public void spawnMyBoxes(PolygonGame game) {
        // sets up width and height for the boxes based on the window size
        int w = (int)(game.getWindowWidth() / 2 / sizeMultiplier);
        int h = (int)(game.getWindowHeight() / 6 / sizeMultiplier);

        int centerX = (game.getWindowWidth() - w) / 2; // center the boxes horizontally
        int ySlots = (int)(game.getWindowHeight() / 9 / sizeMultiplier); // gets even splits for the boxes
        int yShift = (int)(-(game.getWindowHeight() / 25.0 * ((sizeMultiplier - 1) * 2))); // shifts the boxes up a bit so they look better


        // play button
        MainMenu playButton = new MainMenu(game,  "Play");

        playButton.setSize(w, h);
        playButton.setColor(new Color(220, 20, 60));
        playButton.setLocation(centerX + menuOffsetX, ySlots - yShift + menuOffsetY);
        game.add(playButton);
        menuButtons.add(playButton);

        // tutorial button
        MainMenu tutorialButton = new MainMenu(game, "Tutorial");
        tutorialButton.setSize(w, h);
        tutorialButton.setColor(new Color(220, 20, 60));
        tutorialButton.setLocation(centerX + menuOffsetX, ySlots * 3 - yShift + menuOffsetY);
        game.add(tutorialButton);
        menuButtons.add(tutorialButton);

        // settings button
        MainMenu settings = new MainMenu(game, "Settings");
        settings.setSize(w, h);
        settings.setColor(new Color(220, 20, 60));
        settings.setLocation(centerX + menuOffsetX, ySlots * 5 - yShift + menuOffsetY);
        game.add(settings);
        menuButtons.add(settings);

        // exit button
        MainMenu exitButton = new MainMenu(game,  "Exit");
        exitButton.setSize(w, h);
        exitButton.setColor(new Color(220, 20, 60));
        exitButton.setLocation(centerX + menuOffsetX, ySlots * 7 - yShift + menuOffsetY);
        game.add(exitButton);
        menuButtons.add(exitButton);

        MainMenu logoPolygon = new MainMenu(game, "");
        
        int logoW = (int)(game.getWindowWidth() / 3 / logoSizeMultiplier);
        int logoH = (int)(game.getWindowHeight() / 4 / logoSizeMultiplier);
        int logoX = (game.getWindowWidth() - logoW) / 2 + logoOffsetX;
        int logoY = game.getWindowHeight() / 10 + logoOffsetY;

        logoPolygon.setLocation(logoX, logoY);
        logoPolygon.setSize(logoW, logoH);

        logoPolygon.setLocation(logoX, logoY);
        game.add(logoPolygon);
        menuButtons.add(logoPolygon);
        
    }

    @Override
    public void paint(Graphics g) {
            if (buttonName.equals("")) {
                if (logoImage != null) {
                    g.drawImage(logoImage, 0, 0, getWidth(), getHeight(), null);
                }
                return; // skip everything else
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
            if (hovered){
                AffineTransform old = g2d.getTransform();
                g2d.rotate(hoverAngle, textX + textWidth / 2.0, textY - textHeight / 2.0);
                g2d.drawString(buttonName, textX, textY);
                g2d.setTransform(old);
            } else {
                hoverAngle = 0;
                g2d.drawString(buttonName, textX, textY);
            }

            if (buttonName.equals("") && logoImage != null) {
                g.drawImage(logoImage, 0, 0, getWidth(), getHeight(), null);
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

        //glow when hovered over

        hovered = contains(game.getMouseX(), game.getMouseY());
        
        if (hovered) {
            setColor(Color.RED); 
        } else {
            setColor(new Color(220, 20, 60));
        }
        
        if (hovered && !wasHoveredLastFrame) {
            if(tiltLeft){
            hoverAngle = r.nextDouble() * 0.05+0.1; 
            tiltLeft= false;
            }else if (!tiltLeft) {
                hoverAngle = -(r.nextDouble() * 0.05+0.1); 
                tiltLeft = true;
            }
        }
        wasHoveredLastFrame = hovered;



        if (wasPressed && !game.mouseLeftPressed() && contains(x, y) && readyToApply) {

            if (buttonName.equals("Play")) {
                for (MainMenu m : menuButtons) { // removes all the buttons in the list from game
                    game.remove(m);
                }
                menuButtons.clear(); // clears the entire list
                //start the game
                PolygonGame.gamePause = false; // resumes the game
                //add player and abilities
                game.spawnGame();
                //start animation
                SpawnAnimation spawnDummy = new SpawnAnimation(game, 0,0);
                spawnDummy.spawnAnimation(game);
                game.add(spawnDummy);
            }
            else if (buttonName.equals("Tutorial")) {
                for (MainMenu m : menuButtons) { // removes all the buttons in the list from game
                    game.remove(m);
                }
                menuButtons.clear(); // clears the entire list
                //tutorial setup
                game.tutorialController.spawnTutorial(game);
            } else if (buttonName.equals("Settings")) {
                
            }
            else if (buttonName.equals("Exit")) {
                System.exit(0);
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
