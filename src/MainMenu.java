import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class MainMenu extends GameObject {
    private static ArrayList<MainMenu> menuButtons = new ArrayList<>(); // list for the actual buttons, shared aross the
                                                                        // entire class
    private Image boxImage;
    private String buttonName;
    PolygonGame game;

    // The constructor for the dummy object
    public MainMenu(PolygonGame game, String imagePath, String buttonName) {
        this.boxImage = new ImageIcon(imagePath).getImage();
        this.game = game;
        this.buttonName = buttonName;
    }

    /**
     * Spawns the boxes for the main menu.
     */
    public void spawnMyBoxes(PolygonGame game) {
        // sets up width and height for the boxes based on the window size
        int w = game.getWindowWidth() / 2;
        int h = game.getWindowHeight() / 6;

        int centerX = (game.getWindowWidth() - w) / 2; // center the boxes horizontally
        int ySlots = game.getWindowHeight() / 9; // gets even splits for the boxes
        int yShift = game.getWindowHeight() / 25; // shifts the boxes up a bit so they look better



        // play button
        MainMenu playButton = new MainMenu(game, "Images\\MainMenu\\PlayButton.png", "Play");
        playButton.setSize(w, h);
        playButton.setColor(Color.RED);
        playButton.setLocation(centerX, ySlots - yShift);
        game.add(playButton);
        menuButtons.add(playButton);

        // tutorial button
        MainMenu tutorialButton = new MainMenu(game, "Images\\MainMenu\\TutorialButton.png", "Tutorial");
        tutorialButton.setSize(w, h);
        tutorialButton.setColor(Color.RED);
        tutorialButton.setLocation(centerX, ySlots * 3 - yShift);
        game.add(tutorialButton);
        menuButtons.add(tutorialButton);

        // settings button
        MainMenu settings = new MainMenu(game, "Images\\MainMenu\\SettingsButton.png", "Settings");
        settings.setSize(w, h);
        settings.setColor(Color.RED);
        settings.setLocation(centerX, ySlots * 5 - yShift);
        game.add(settings);
        menuButtons.add(settings);

        // exit button
        MainMenu exitButton = new MainMenu(game, "Images\\MainMenu\\ExitButton.png", "Exit");
        exitButton.setSize(w, h);
        exitButton.setColor(Color.RED);
        exitButton.setLocation(centerX, ySlots * 7 - yShift);
        game.add(exitButton);
        menuButtons.add(exitButton);

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); // paints the background first of the button first

        // adds the image on top of the background
        if (boxImage != null) {
            g.drawImage(boxImage, 0, 0, getWidth(), getHeight(), null);
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

        if (wasPressed && !game.mouseLeftPressed() && contains(x, y) && readyToApply) {

            if (buttonName.equals("Play")) {
                for (MainMenu m : menuButtons) { // removes all the buttons in the list from game
                    game.remove(m);
                }
                menuButtons.clear(); // clears the entire list
                PolygonGame.gamePause = false; // resumes the game
                //add player and abilities
                game.spawnGame();
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
    }
}
