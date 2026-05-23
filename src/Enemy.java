import java.util.Random;
import java.awt.Color;
public class Enemy extends GameObject {
    Random r = new Random();
    public int size;
    public int speed;
    public int health = 3;
    public int enemyDamage = 1;
    public int displayOld = 0; //used to see enemies that have been alive older

    Polygon game;

    public Enemy(Polygon game) {
        this.game = game;
        size = (game.getWindowWidth()+game.getWindowHeight())/125; // enemy size is 1/100 of the entire window
        speed = (game.getWindowWidth()+game.getWindowHeight())/2000; // speed
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

        if (x < game.player.getX()) x += speed;
        if (x > game.player.getX()) x -= speed;
        if (y < game.player.getY()) y += speed;
        if (y > game.player.getY()) y -= speed;

        setX(x);
        setY(y);

        displayOld+=1;
		if (displayOld > 60) {	
			displayOld = 0;
            setColor(Color.ORANGE);
        }

    }
}
