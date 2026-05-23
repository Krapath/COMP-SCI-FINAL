import java.util.Random;
import java.awt.Color;
public class Enemy extends GameObject {
    Random r = new Random();
    public int size  = 25;
    public int speed = 2;
    public int health = 3;
    public int enemyDamage = 1;

    Polygon game;

    public Enemy(Polygon game) {
        this.game = game;
        setSize(size, size);
        setColor(Color.GREEN);
        int x = r.nextInt(game.getWindowWidth()-size);
        int y = r.nextInt(game.getWindowHeight()-size);
        setLocation(x, y);
    }

    public void act() {
        int x = getX();
        int y = getY();
        int playerX = game.player.getX();
        int playerY = game.player.getY();
        double playerAngle = game.getAngle(x, y, playerX, playerY);
        x += (int)Math.ceil(Math.cos(playerAngle) * speed);
        y += (int)Math.ceil(Math.sin(playerAngle) * speed);
        setX(x);
        setY(y);


    }

}
