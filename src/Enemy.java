
import java.awt.Color;
import java.util.Random;

public class Enemy extends GameObject {

    PolygonGame game;
    Random r;
    public int size;
    int type;
    int spawnType;
    public double speed;
    public int health;
    public double angle;
    public double givenX, givenY;
    public int enemyDamage = 1;
    public int displayOld = 0; //used to see enemies that have been alive older
    boolean appearedOnGame = false;

    static int healthMultiplier;


    //TODO: enemies exist for one extra frame which can cause problems
    public Enemy(PolygonGame game, int type, int spawn, int seed) {
        this.type = type;
        this.spawnType = spawn;
        healthMultiplier = (int) Math.pow(2.0, (double) (game.spawnedEnemies / 500));
        //System.out.println(healthMultiplier);
        r = new Random(seed);
        size = (game.getWindowWidth() + game.getWindowHeight()) / 80; // enemy size is 1/100 of the entire window
        speed = (game.getWindowWidth() + game.getWindowHeight()) / 500; // speed is 1/100 of the entire window size
        this.game = game;
        spawnEnemy(spawn);
        enemyType(type);

    }

    public Enemy(PolygonGame game, int type, int spawn, int seed, double givenX, double givenY) {
        this.type = type;
        this.spawnType = spawn;
        this.givenX = givenX;
        this.givenY = givenY;
        healthMultiplier = (int) Math.pow(2.0, (double) (game.spawnedEnemies / 500));
        System.out.println(healthMultiplier);
        r = new Random(seed);
        size = (game.getWindowWidth() + game.getWindowHeight()) / 80; // enemy size is 1/100 of the entire window
        speed = (game.getWindowWidth() + game.getWindowHeight()) / 500; // speed is 1/100 of the entire window size
        this.game = game;
        spawnEnemy(spawn);
        enemyType(type);

    }

    public void act() {
        if (PolygonGame.gamePause) {
            return;
        }

        avoidCollision();

        switch (type) {
            case 0:
            case 1:
                chase(speed);
                setPosition();
                checkDeath();
                break;
            case 2:
                displayOld++;
                if (displayOld < 75) {
                    chase(speed);
                    setPosition();
                } else {

                    if (displayOld / 100 > 0) {
                        displayOld = 0;
                        game.projectile = new Projectile(game, this.x, this.y);
                        game.add(game.projectile);
                        game.projectiles.add(game.projectile);
                    }
                }
                checkDeath();
                break;
            case 3:
                if (displayOld < 75) {
                    displayOld++;
                    chase(speed);
                    setPosition();
                } else {

                    if (r.nextInt(300) < 5) {
                        game.enemy = new Enemy(game, 4, 2, game.enemySpawnSeed, this.x, this.y);
                        game.add(game.enemy);
                        game.enemies.add(game.enemy);
                    }
                }
                checkDeath();
                break;
            case 4:
                shoot(speed, angle);
                setPosition();
                checkDeath();
                if (!appearedOnGame && x > 0 && y > 0) {
                    appearedOnGame = true;
                }

                if (appearedOnGame && (x < 0 || x > game.getFieldWidth() || y < 0 || y > game.getFieldHeight())) {
                    game.remove(this); // Remove enemy if health is depleted
                    game.enemies.remove(this); // Remove enemy from the list
                }
                break;
        }

    }

    public void avoidCollision() {
        for (int i = 0; i < game.enemies.size(); i++) {
            Enemy other = game.enemies.get(i);

            if (collides(other) && other != this && other.type != 4) { //if touching another enemy, moves this enemy away from the other one.
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
                        x = r.nextInt(game.getWindowWidth() - size);
                        y = (int) (game.getWindowHeight() * 1.1);
                    } else { // left
                        x = (int) (0 - game.getWindowWidth() * .1);
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
            case 2:
                x = givenX;
                y = givenY;
                break;
        }

        setLocation((int) x, (int) y);
    }

    public final void enemyType(int type) {
        switch (type) {
            case 0: //normal
                health = 3 * healthMultiplier;
                setColor(Color.GREEN);
                break;
            case 1: //miniboss
                health = 50 * healthMultiplier;
                size *= 5;
                speed /= 2;
                setColor(Color.GREEN);
                break;
            case 2: //gunner
                health = 5 * healthMultiplier;
                setColor(Color.GRAY);
                break;
            case 3: //throwing goblin
                health = 2 * healthMultiplier;
                setColor(Color.orange);
                break;
            case 4: //bat 
                health = 5;
                size /= 2;
                speed *= 4;
                angle = getRealAngle(Player.x + (double) Player.size / 2, Player.y + (double) Player.size / 2);
                setColor(Color.pink);
                break;
        }
        setSize(size, size);
    }

    public void checkDeath() {
        if (health <= 0) {
            // Create an XP orb at the location of the defeated enemy
            XpOrb xp = new XpOrb((int) x, (int) y, game);

            game.add(xp);          // Add the xp orb to the game
            game.xpOrbs.add(xp);   // Add the xp orb to the list
            game.remove(this); // Remove enemy if health is depleted
            game.enemies.remove(this); // Remove enemy from the list
        }

    }

}
