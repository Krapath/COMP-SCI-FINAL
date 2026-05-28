import java.awt.Color;
import java.awt.Graphics; 

public class DisplayDebug extends GameObject{
    Polygon game;

    public DisplayDebug(Polygon game) {
        this.game = game; //hi this is a test test test
        setSize(game.getWindowWidth(), game.getWindowHeight());  // size of the text area
    }

    public void act() {
        // reposition every tick so text stays in corner
        repaint();  // redraws this object every tick
        
    }

    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        
        // draw mouse and player coordinates
        g.drawString("MOUSE    X:" + game.getMouseX() + "  Y:" + game.getMouseY(), 10, 20);
        g.drawString("PLAYER   X:" + game.player.getX() + "  Y:" + game.player.getY(), 10, 40);

        // finds the angle between the player and the mouse cursor and displays it in degrees
        double angle = game.getAngle(game.player.getX(), game.player.getY(),game.getMouseX(), game.getMouseY()) * (180/Math.PI);
        angle =-angle; // increase counterclockwise, follows standard unit circle convention
        if (angle < 0){ // display angle as a positive value between 0 and 360
        	angle+=360;
        }
        
        // display the number of enemies and hp
        g.drawString("ENEMIES: " + game.enemies.size(), 10, 80);
        g.drawString("HP: " + Player.health, 10, 100);

        // display the players current score
        g.drawString("SCORE: " + Player.score, 10, 120);
        
        // display the mouse cursor as a red rectangle 
        g.drawString("ANGLE:" + angle , 10, 60);
        g.setColor(Color.RED);
        g.drawRect(game.getMouseX(),game.getMouseY(),15,15);        
      
        // draw healthbar
        
        g.drawRect(10, 140+((Player.maxHealth-Player.health)*5), 20, 5*Player.health);


    }
}
