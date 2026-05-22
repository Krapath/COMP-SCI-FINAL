import java.util.Random;
import java.awt.Color;
public class Enemy extends GameObject {
    Random r = new Random();
    pub`lic int size  = 25;
    public int speed = 1;
    public int health = 3;

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

        if (x < game.player.getX()) x += speed;
        if (x > game.player.getX()) x -= speed;
        if (y < game.player.getY()) y += speed;
        if (y > game.player.getY()) y -= speed;

        setX(x);
        setY(y);


    }

}
