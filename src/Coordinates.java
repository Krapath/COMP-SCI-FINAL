import java.awt.Color;
import java.awt.Graphics;

public class Coordinates extends GameObject {

    Polygon game;

    public Coordinates(Polygon game) {
        this.game = game;
        setSize(200, 30);  // size of the text area
    }

    public void act() {
        // reposition every tick so text stays in corner
        repaint();  // redraws this object every tick
        setLocation(10, 10);
    }

    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("X:" + game.getMouseX() + "  Y:" + game.getMouseY(), 0, 20);
    }
}