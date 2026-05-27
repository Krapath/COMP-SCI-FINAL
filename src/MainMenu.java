import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class MainMenu extends GameObject {

    private Image boxImage;
    private String buttonName;
    Polygon game;

    // The constructor for the dummy object
    public MainMenu(Polygon game, String imagePath,String buttonName) {
        // Loads the specific image file path passed into it
        setSize(0, 0);
        this.boxImage = new ImageIcon(imagePath).getImage();
        this.game = game;
        this.buttonName = buttonName;
    }

    // Actual method to spawn the boxes
    public void spawnMyBoxes(Polygon game) {
        int centerX = game.getWindowWidth() / 2 - game.getWindowWidth() / 4; // center the boxes horizontally
        int ySlots = game.getWindowHeight() / 9; // gets even splits for the boxes
        int yShift = game.getWindowHeight() / 25; // shifts the boxes up a bit so they look better

        // sets up width and height for the boxes based on the window size
        int w = game.getWindowWidth() / 2;
        int h = game.getWindowWidth() / 10;

        // play button
        MainMenu playButton = new MainMenu(game, "Images\\MainMenu\\PlayButton.png","Play");
        playButton.setSize(w, h);
        playButton.setColor(Color.RED);
        playButton.setLocation(centerX, ySlots - yShift);
        game.add(playButton);

        // tutorial button
        MainMenu tutorialButton = new MainMenu(game, "Images\\MainMenu\\TutorialButton.png","Tutorial");
        tutorialButton.setSize(w, h);
        tutorialButton.setColor(Color.RED);
        tutorialButton.setLocation(centerX, ySlots * 3 - yShift);
        game.add(tutorialButton);

        // settings button
        MainMenu settings = new MainMenu(game, "Images\\MainMenu\\SettingsButton.png","Settings");
        settings.setSize(w, h);
        settings.setColor(Color.RED);
        settings.setLocation(centerX, ySlots * 5 - yShift);
        game.add(settings);

        // exit button
        MainMenu exitButton = new MainMenu(game, "Images\\MainMenu\\ExitButton.png","Exit");
        exitButton.setSize(w, h);
        exitButton.setColor(Color.RED);
        exitButton.setLocation(centerX, ySlots * 7 - yShift);
        game.add(exitButton);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); // paints the background first
        // adds the image on top of the background
        if (boxImage != null) {
            g.drawImage(boxImage, 0, 0, getWidth(), getHeight(), null);
        }
    }

    public boolean readyToApply = false;
    public boolean wasPressed = false;

    public void act() {
        if (!game.onMainMenu) {
            return; // only check for button clicks if we're on the main menu
        }
        int x = game.getMouseX();
        int y = game.getMouseY();
        if (!game.mouseLeftPressed()) {
            readyToApply = true; // the buff should be applied on mouse release if the mouse was pressed while hovering over this powerup
        }
        if (game.mouseLeftPressed() && contains(x, y) && readyToApply) {
            wasPressed = true;
        }
        //apply the buff and remove the powerups if the player clicks on a powerup and releases the mouse button while still hovering over the same powerup
        if (wasPressed && !game.mouseLeftPressed() && contains(x, y) && readyToApply&& buttonName.equals("Play")) {
            game.onMainMenu = false; // start the game if the play button is clicked
        }
        readyToApply = false; // reset readyToApply for the next time the player chooses a buff
        wasPressed = false; // reset wasPressed for the next time the player chooses a buff
    }
}