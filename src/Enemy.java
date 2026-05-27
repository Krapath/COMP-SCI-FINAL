
import java.awt.Color;
import java.util.Random;

public class Enemy extends GameObject {

    Random r = new Random();
    public int size = 50;
    public int speed = 5;
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
        boolean collided = false;
        do {
            do {
                x = r.nextInt(game.getWindowWidth() - size);
                y = r.nextInt(game.getWindowHeight() - size);
            } while ((Math.abs(game.player.getX() - x) < (game.getWindowWidth() + game.getWindowHeight()) / 100) || (Math.abs(game.player.getY() - y) < (game.getWindowWidth() + game.getWindowHeight()) / 100)); // ensures that the enemy does not too close to the player

            //this code does not work with enemies of different sizes. Fix if variable enemy sizes.
            for (int i = 0; i < game.enemies.size(); i++) { //ensures enemies dont spawn directly on top of each other
                Enemy other = game.enemies.get(i);
                if (other != this && other.getY() > y + size && other.getY() < y && other.getX() > x + size && other.getX() < x) {
                    collided = true;
                }
            }
        } while (collided); //permits the spawn location if not on top of another enemy.

        setLocation(x, y);

    }

    public void act() {
        if (game.choosingBuff) {
            return;
        }
        int x = getX();
        int y = getY();
        boolean collided = false;
        int playerX = game.player.getX();
        int playerY = game.player.getY();
        double playerAngle = game.getAngle(x, y, playerX, playerY);

        for (int i = 0; i < game.enemies.size(); i++) {
            Enemy other = game.enemies.get(i);

            if (collides(other) && other != this) { //if touching another enemy, moves this enemy away from the other one.
                //setColor(Color.RED);
                int enemyX = game.enemies.get(i).getX();
                int enemyY = game.enemies.get(i).getY();
                double enemyAngle = game.getAngle(x, y, enemyX, enemyY);
                x -= (int) (Math.cos(enemyAngle) * 10);
                y -= (int) (Math.sin(enemyAngle) * 10);
                collided = true;
                break;
            }
        }
        // if (!collided) { //if not touching another enemy, chase player.
        x += (int) (Math.cos(playerAngle) * speed);
        y += (int) (Math.sin(playerAngle) * speed);
        //}

        setX(x);
        setY(y);

        displayOld += 1;
        if (displayOld > 60) {
            displayOld = 0;
            setColor(Color.ORANGE);
        }

    }
}
