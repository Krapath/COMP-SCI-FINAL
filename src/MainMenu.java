import java.awt.Color;

public class MainMenu extends GameObject {
    
    // 1. ADD THIS CONSTRUCTOR SO 'new MainMenu(game)' WORKS
    public MainMenu(Polygon game) {
        setSize(100, 100);
        setColor(Color.RED);
    }
    
    // This is your method making the boxes
    public void spawnMyBoxes(Polygon game) {
        MainMenu box1 = new MainMenu(game);
        box1.setLocation(150, 200);
        game.add(box1);

        MainMenu box2 = new MainMenu(game);
        box2.setLocation(400, 400);
        game.add(box2);
    }

    public void act() {
    }
}