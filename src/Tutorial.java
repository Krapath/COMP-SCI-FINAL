import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Tutorial extends GameObject {
    private static ArrayList<Tutorial> tutorialButtons = new ArrayList<>(); // list of all things in the class
    // set variables
    private Image boxImage;
    PolygonGame game;

    //set up dummy consturctor for tutorial buttons to spawn
    public Tutorial(PolygonGame game, String imagePath) {
        setSize(0, 0);
        this.boxImage = new ImageIcon(imagePath).getImage();
        this.game = game;
    }

    public void spawnTutorial(PolygonGame game) {
        // sets up width and height for the boxes based on the window size
        int w = (int) (game.getWindowWidth() / 1.5);
        int h = (int) (game.getWindowHeight() / 1.2);

        int x = (game.getWindowWidth() - w) / 2; // center the boxes horizontally
        int y = (game.getWindowHeight() - h) / 2; // gets even splits for the boxes

        // make the frame for the tutorial
        Tutorial tutorial = new Tutorial(game, "Images\\MainMenu\\BackButton.png");
        tutorial.setSize(w, h);
        tutorial.setColor(Color.RED);
        tutorial.setLocation(x, y);
        game.add(tutorial);
        tutorialButtons.add(tutorial);

    }

    public void act() {

    }

}
