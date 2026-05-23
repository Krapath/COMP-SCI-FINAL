import java.util.Random;
import java.awt.Color;
public class Enemy extends GameObject {
    Random r = new Random();
    public int size  = 25;
    public int speed = 2;
    public int health = 3;
    public int enemyDamage = 1;
    public int displayOld = 0; //used to see enemies that have been alive older

    Polygon game;

    public Enemy(Polygon game) {
        this.game = game;
        setSize(size, size);
        setColor(Color.GREEN);
        int x;
        int y;
        do {
            x = r.nextInt(game.getWindowWidth()-size);
            y = r.nextInt(game.getWindowHeight()-size);
        } while ((Math.abs(game.player.getX() - x) < (game.getWindowWidth()+game.getWindowHeight())/100) || (Math.abs(game.player.getY() - y) < (game.getWindowWidth()+game.getWindowHeight())/100)); // ensures that the enemy does not too close to the player

        setLocation(x, y);
    }

    public void act() {
        int x = getX();
        int y = getY();
        int playerX = game.player.getX();
        int playerY = game.player.getY();
        double playerAngle = game.getAngle(x, y, playerX, playerY);
        x += (int)(Math.cos(playerAngle) * speed);
        y += (int)(Math.sin(playerAngle) * speed);
        setX(x);
        setY(y);

        displayOld+=1;
		if (displayOld > 60) {	
			displayOld = 0;
            setColor(Color.ORANGE);
        }

    }
}
