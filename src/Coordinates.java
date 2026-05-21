import java.awt.Color;
import java.awt.Graphics; 

public class Coordinates extends GameObject {

    Polygon game;

    public Coordinates(Polygon game) {
        this.game = game;
        setSize(1000, 1000);  // size of the text area
    }

    public void act() {
        // reposition every tick so text stays in corner
        repaint();  // redraws this object every tick
        setLocation(10, 10);
        
    }

    public void paint(Graphics g) {wfwfwfw
        g.setColor(Color.WHITE);
        g.drawString("X:" + game.getMouseX() + "  Y:" + game.getMouseY(), 0, 20);
        double angle = game.getAngle(game.player.getX(), game.player.getY(),game.getMouseX(), game.getMouseY()) * (180/Math.PI);
        angle =-angle;
        if (angle < 0){
        	angle+=360;
        }
        g.drawString("Angle:" + angle , 0, 30);
        g.setColor(Color.RED);

        g.drawRect(game.getMouseX(),game.getMouseY(),4,4);        
      

    }
}
