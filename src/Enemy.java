
import java.awt.Color;
import java.util.Random;

public class Enemy extends GameObject {

    PolygonGame game;
    Random r;
    public int size;
    public int speed;
    public int health;
    public int enemyDamage = 1;
    public int displayOld = 0; //used to see enemies that have been alive older
    public double x, y;
    int healthMultiplier;

    public Enemy(PolygonGame game, int type, int spawn, int seed) {
        healthMultiplier = (int) Math.pow(2.0, (double) (game.spawnedEnemies / 100));
        System.out.println(healthMultiplier);
        game.spawnedEnemies++;
        r = new Random(seed);
        size = (game.getWindowWidth() + game.getWindowHeight()) / 80; // enemy size is 1/100 of the entire window
        speed = (game.getWindowWidth() + game.getWindowHeight()) / 500; // speed is 1/100 of the entire window size
        this.game = game;
        enemyType(type);
        spawnEnemy(spawn);
    }

    public void act() {
        if (PolygonGame.gamePause) {
            return;
        }
        x = getX();
        y = getY();

        avoidCollision();
        chase();
        setPosition(this, x, y);

        displayOld += 1;
        if (displayOld > 600) { // for debug
            displayOld = 0;
            setColor(Color.ORANGE);
        }

    }

    public void chase() {
        double playerAngle = Math.atan2(Player.y - y, Player.x - x);
        x += (Math.cos(playerAngle) * speed);
        y += (Math.sin(playerAngle) * speed);
    }

    public void avoidCollision() {
        for (int i = 0; i < game.enemies.size(); i++) {
            Enemy other = game.enemies.get(i);

            if (collides(other) && other != this) { //if touching another enemy, moves this enemy away from the other one.
                //setColor(Color.RED);
                double enemyX = game.enemies.get(i).x;
                double enemyY = game.enemies.get(i).y;
                double enemyAngle = Math.atan2(enemyY - y, enemyX - x);

                x -= (Math.cos(enemyAngle) * 10.0);
                y -= (Math.sin(enemyAngle) * 10.0);

            }
        }
    }

    public final void spawnEnemy(int type) {
        boolean collided = false;

        switch (type) {
            case 0://solo or hoard spawn
                do {
                    int side = r.nextInt(4); // randomly picks a side of the screen to spawn on
                    if (side == 0) { // top
                        x = r.nextInt(game.getWindowWidth() - size);
                        y = (int) (0 - game.getWindowHeight() * .1);
                    } else if (side == 1) { // right
                        x = (int) (game.getWindowWidth() * 1.1);
                        y = r.nextInt(game.getWindowHeight() - size);
                    } else if (side == 2) { // bottom
                        x = r.nextInt(game.getWindowHeight() - size);
                        y = (int) (game.getWindowWidth() * 1.1);
                    } else { // left
                        x = (int) (0 - game.getWindowHeight() * .1);
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
                break;
        }

        setLocation((int) x, (int) y);
    }

    public final void enemyType(int type) {
        switch (type) {
            case 0:
                health = 3 * healthMultiplier;
                setSize(size, size);
                setColor(Color.GREEN);
                break;
            case 1:
                health = 20 * healthMultiplier;
                size *= 5;
                speed /= 2;
                setSize(size, size);
                setColor(Color.GREEN);
                break;
        }
    }

}
