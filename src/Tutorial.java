import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;
public class Tutorial extends GameObject {
    private static ArrayList<Tutorial> tutorialButtons = new ArrayList<>(); // list for the actual buttons, shared aross the
                                                                        // entire class
    private Image boxImage;
    PolygonGame game;

    public Tutorial(PolygonGame game, String imagePath) {
        setSize(0, 0);
        this.boxImage = new ImageIcon(imagePath).getImage();
        this.game = game;
    }

    public void spawnTutorial(PolygonGame game) {
        // sets up width and height for the boxes based on the window size
        int w = (int) (game.getWindowWidth() / 1.5);
        int h = (int) (game.getWindowWidth() / 10);

        int centerX = (game.getWindowWidth() - w) / 2; // center the boxes horizontally
        int ySlots = game.getWindowHeight() / 9; // gets even splits for the boxes
        int yShift = game.getWindowHeight() / 25; // shifts the boxes up a bit so they look better

        // back button
        Tutorial backButton = new Tutorial(game, "Images\\MainMenu\\BackButton.png");
        backButton.setSize(w, h);
        backButton.setColor(Color.RED);
        backButton.setLocation(centerX, ySlots * 5 - yShift);
        game.add(backButton);
        tutorialButtons.add(backButton);

    }

    public void act() {

    }

}

