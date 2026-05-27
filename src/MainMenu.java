import java.awt.Color;

public class MainMenu extends GameObject {
    
    // The constructor for the dummy object
    public MainMenu(Polygon game) {
        // Keeps the dummy object completely invisible and empty
        setSize(0, 0); 
    }
    
    // Actual method to spawn the boxes
    public void spawnMyBoxes(Polygon game) {
        int centerX = game.getWindowWidth() / 2 - game.getWindowWidth() / 4; // center the boxes horizontally
        int ySlots = game.getWindowHeight() / 10; //positions the boxes in 5 vertical slots
        //play button
        MainMenu box1 = new MainMenu(game);
        box1.setSize(game.getWindowWidth() / 2, game.getWindowWidth() / 10);
        box1.setColor(Color.RED);
        box1.setLocation(centerX, ySlots);
        game.add(box1); // This adds the first real red box

        MainMenu box2 = new MainMenu(game);
        box2.setSize(game.getWindowWidth() / 2, game.getWindowWidth() / 10);
        box2.setColor(Color.RED);
        box2.setLocation(centerX, ySlots*3);
        game.add(box2); // This adds the second real red box
    }

    public void act() {
    }
}