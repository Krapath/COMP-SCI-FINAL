import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Tutorial extends GameObject {
    private static ArrayList<Tutorial> tutorialButtons = new ArrayList<>(); // list of all things in the class
    // set variables
    private Image boxImage;
    private String buttonName;
    PolygonGame game;

    // set up dummy consturctor for tutorial buttons to spawn
    public Tutorial(PolygonGame game, String imagePath, String buttonName) {
        setSize(0, 0);
        this.boxImage = new ImageIcon(imagePath).getImage();
        this.game = game;
        this.buttonName = buttonName;
    }

    public void spawnTutorial(PolygonGame game) {
        // sets up width and height for the tutorial box based on the window size
        int w = (int) (game.getWindowWidth() / 1.5);
        int h = (int) (game.getWindowHeight() / 1.2);

        int x = (game.getWindowWidth() - w) / 2; // center the tutorial horizontally
        int y = (game.getWindowHeight() - h) / 2; // center the tutorial vertically

        // make the shift for the back button
        int setShift = (int) ((x + y) / 10);

        // make the frame for the tutorial
        Tutorial tutorial = new Tutorial(game, "Images\\MainMenu\\playButton.png", "Tutorial");// change the image
        tutorial.setSize(w, h);
        tutorial.setColor(Color.RED);
        tutorial.setLocation(x, y);
        game.add(tutorial);
        tutorialButtons.add(tutorial);

        // make the back button
        Tutorial backButton = new Tutorial(game, "Images\\MainMenu\\playButton.png", "Back");
        backButton.setSize(w / 4, h / 10);
        backButton.setColor(Color.BLUE);
        backButton.setLocation(x - setShift, y - setShift);
        game.add(backButton);
        tutorialButtons.add(backButton);
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

            if (buttonName.equals("Back")) {
                for (Tutorial m : tutorialButtons) { // removes all the buttons in the list from game
                    game.remove(m);
                }
                tutorialButtons.clear(); // clears the entire list
                // returns to the main menu
                MainMenu menuController = new MainMenu(game, "", "");
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
