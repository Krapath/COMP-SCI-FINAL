
import java.awt.Color;
import java.util.Random;

public class Enemy extends GameObject {
    PolygonGame game;

    Random r = new Random();
    public int size;
    public int speed;
    public int health=3;
    public int enemyDamage = 1;
    public int displayOld = 0; //used to see enemies that have been alive older
    double x, y;


    public Enemy(PolygonGame game) {

        this.game = game;
        size =(game.getWindowWidth() + game.getWindowHeight()) / 80; // enemy size is 1/100 of the entire window
        speed =(game.getWindowWidth() + game.getWindowHeight()) / 500; // speed is 1/100 of the entire window size
        setSize(size, size);
        setColor(Color.GREEN);
        int x;
        int y;
        boolean collided = false;
        do {
            /* old system remove if you wish
            do {
                x = r.nextInt(game.getWindowWidth() - size);
                y = r.nextInt(game.getWindowHeight() - size);
            } while ((Math.abs(game.player.getX() - x) < (game.getWindowWidth() + game.getWindowHeight()) / 100) || (Math.abs(game.player.getY() - y) < (game.getWindowWidth() + game.getWindowHeight()) / 100)); // ensures that the enemy does not too close to the player
            */            
           int side = r.nextInt(4); // randomly picks a side of the screen to spawn on
            if (side == 0) { // top
                x = r.nextInt(game.getWindowWidth() - size);
                y = (int) (0 - game.getWindowHeight()*.1);
            } else if (side == 1) { // right
                x = (int)(game.getWindowWidth()*1.1);
                y = r.nextInt(game.getWindowHeight() - size);
            } else if (side == 2) { // bottom
                x = r.nextInt(game.getWindowHeight() - size);
                y = (int)(game.getWindowWidth()*1.1);
            } else { // left
                x = (int) (0 - game.getWindowHeight()*.1);
                y = r.nextInt(game.getWindowHeight() - size);
            }
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
        if (PolygonGame.gamePause) {
            return;
        }
        x = getX();
        y = getY();
        boolean collided = false;
        int playerX = game.player.getX();
        int playerY = game.player.getY();
        double playerAngle = Math.atan2(Player.y-y, Player.x-x);
        

        for (int i = 0; i < game.enemies.size(); i++) {
            Enemy other = game.enemies.get(i);

            if (collides(other) && other != this) { //if touching another enemy, moves this enemy away from the other one.
                //setColor(Color.RED);
                double enemyX = game.enemies.get(i).x;
                double enemyY = game.enemies.get(i).y;
                double enemyAngle = Math.atan2(enemyY-y, enemyX-x);
                
                x -= (Math.cos(enemyAngle) * 10.0);
                y -= (Math.sin(enemyAngle) * 10.0);
                collided = true;
                break;
            }
        }
        // if (!collided) { //if not touching another enemy, chase player.
        x += (Math.cos(playerAngle) * speed);
        y += (Math.sin(playerAngle) * speed);
        //}

        setX((int)(x + 0.5));
        setY((int)(y + 0.5));

        displayOld += 1;
        if (displayOld > 600) { // for debug
            displayOld = 0;
            setColor(Color.ORANGE);
        }

    }
}
