import java.awt.Color;
import java.awt.Font; 
import java.awt.Graphics; 

public class MainMenu extends GameObject {

    // The constructor for the dummy object
    public MainMenu(Polygon game) {
        // Keeps the dummy object completely invisible and empty
        setSize(0, 0);
    }

    // Actual method to spawn the boxes
    public void spawnMyBoxes(Polygon game) {
        int centerX = game.getWindowWidth() / 2 - game.getWindowWidth() / 4; // center the boxes horizontally
        int ySlots = game.getWindowHeight() / 9; // gets even splits for the boxes
        int yShift = game.getWindowHeight() / 25; // shifts the boxes up a bit so they look better

        int w = game.getWindowWidth() / 2;
        int h = game.getWindowWidth() / 10;
        
        MainMenu playButton = new MainMenu(game);
        playButton.setSize(w, h);
        playButton.setColor(Color.RED);
        playButton.setLocation(centerX, ySlots - yShift);
        game.add(playButton);

        MainMenu tutorialButton = new MainMenu(game);
        tutorialButton.setSize(w, h);
        tutorialButton.setColor(Color.RED);
        tutorialButton.setLocation(centerX, ySlots * 3 - yShift);
        game.add(tutorialButton);

        MainMenu settings = new MainMenu(game);
        settings.setSize(w, h);
        settings.setColor(Color.RED);
        settings.setLocation(centerX, ySlots * 5 - yShift);
        game.add(settings);

        MainMenu exitButton = new MainMenu(game);
        exitButton.setSize(w, h);
        exitButton.setColor(Color.RED);
        exitButton.setLocation(centerX, ySlots * 7 - yShift);
        game.add(exitButton);
    }

    public void act() {
    }
}