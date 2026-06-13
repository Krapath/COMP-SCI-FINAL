
import java.awt.Color;
import java.util.Random;

public class Enemy extends GameObject {

    PolygonGame game;
    Random r;
    public int size;
    int type;
    int spawnType; //where the enemy should start from
    public double speed;
    public int health;
    public double angle; //angle at which bats shoot
    public double givenX, givenY;
    public int enemyDamage = 1;
    public int displayOld = 0; //used to see enemies that have been alive older
    boolean appearedOnGame = false;
    Color color; //color of the enemy.
    static Color damagedColor; //color to turn to when damaged
    public boolean damaged = false; //wether or not the enemy was recently damaged.
    int damagedTimer; //how long since the enemy was last damaged.
    public int xpDrop;
    boolean stunned = false;
    int stunnedCounter;
    int stunDuration = 0;
    static int healthMultiplier;

    public Enemy(PolygonGame game, int type, int spawn, int seed) {
        this.type = type;
        this.spawnType = spawn;
        healthMultiplier = (int) Math.pow(2.0, (double) (game.killCounter / 150));
        //System.out.println(healthMultiplier);
        r = new Random(seed);
        size = (game.getWindowWidth() + game.getWindowHeight()) / 80; // enemy size is 1/100 of the entire window
        speed = (game.getWindowWidth() + game.getWindowHeight()) / 500; // speed is 1/100 of the entire window size
        this.game = game;
        spawnEnemy(spawn);
        enemyType(type);
        damagedColor = Color.RED;
    }

    public Enemy(PolygonGame game, int type, int spawn, int seed, double givenX, double givenY) {
        this.type = type;
        this.spawnType = spawn;
        this.givenX = givenX;
        this.givenY = givenY;
        healthMultiplier = (int) Math.pow(2.0, (double) (game.killCounter / 500));
        //System.out.println(healthMultiplier); debug
        r = new Random(seed);
        size = (game.getWindowWidth() + game.getWindowHeight()) / 80; // enemy size is 1/100 of the entire window
        speed = (game.getWindowWidth() + game.getWindowHeight()) / 500; // speed is 1/100 of the entire window size
        this.game = game;
        spawnEnemy(spawn);
        enemyType(type);
        damagedColor = Color.RED;
    }

    public void act() {
        if (PolygonGame.gamePause) { //dont do anything when paused
            return;
        }

        avoidCollision(); //moves away from current enemy
        behaviour(type); //runs the behaviour of the enemy
        damagedAffects(); //applied effects if the enemy was damaged

    }

    /**
     * moved colliding enemies away from one another pre: existence of enemies
     * arraylist in game post: double x and y coordinates of enemies are moved
     * away from each other
     */
    public void avoidCollision() {
        for (int i = 0; i < PolygonGame.enemies.size(); i++) {
            Enemy other = PolygonGame.enemies.get(i);

            //if touching another enemy, moves this enemy away from the other one. bats only collide with one another
            if (collides(other) && other != this && ((other.type == 4 && this.type == 4) || (other.type != 4 && this.type != 4))) {
                knockBack(5.0, other);

            }
        }
    }

    /**
     * spawns in enemy based on given type of spawn method pre: existance of
     * arraylist enemies post: gives starting coordinates to the enemy object
     * created
     */
    public final void spawnEnemy(int type) {
        boolean collided;

        switch (type) {
            case 0://solo or hoard spawn
                do {
                    collided = false;
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

                    for (int i = 0; i < PolygonGame.enemies.size(); i++) { //ensures enemies dont spawn directly on top of each other
                        Enemy other = PolygonGame.enemies.get(i);
                        if (other != this && other.getX() < x + size && other.getX() + other.size > x && other.getY() < y + size && other.getY() + other.size > y) {
                            collided = true;
                        }
                    }
                } while (collided); //permits the spawn location if not on top of another enemy.
                break;
            case 2: //set spawn
                x = givenX;
                y = givenY;
                break;
            case 3: //spawn random on screen
                do {
                    x = r.nextInt(game.getWindowWidth()); //gets random x and y values somewhere on screen
                    y = r.nextInt(game.getWindowHeight());
                    collided = false;

                    for (int i = 0; i < PolygonGame.enemies.size(); i++) { //ensures enemies dont spawn directly on top of each other
                        Enemy other = PolygonGame.enemies.get(i);
                        if (other != this && other.getX() < x + size && other.getX() + other.size > x && other.getY() < y + size && other.getY() + other.size > y) {
                            collided = true;
                        }
                    }
                } while (collided);
                break;
            case 4: //hoard spawn, helps spawn on top of each other when seed is the same
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

        }

        setPosition(); //sets the starting position
    }

    /**
     * defines the health, size, speed color and other aspects of given enemy
     * based on type. pre: existence of int health, Color colo, int size, int
     * speed and double angle post: the health, size, speed color and other
     * aspects of the enemy get defined and applied.
     */
    public final void enemyType(int type) {
        switch (type) {
            case 0: //normal enemy
                health = 3 * healthMultiplier;
                color = Color.GREEN;
                setColor(color);
                xpDrop = 1;
                break;
            case 1: //miniboss enemy (big guy)
                health = 50 * healthMultiplier;
                size *= 5;
                speed /= 2;
                color = Color.GREEN;
                setColor(color);
                xpDrop = 10;
                break;
            case 2: //gunner (shoots then runs)
                health = 5 * healthMultiplier;
                color = Color.GRAY;
                setColor(Color.GRAY);
                xpDrop = 4;
                break;
            case 3: //throwing (randomly spawns in bats)
                health = 2 * healthMultiplier;
                color = Color.orange;
                setColor(color);
                xpDrop = 4;
                break;
            case 4: //bat (shoot in a certain direction)
                health = 5;
                size /= 2;
                speed *= 4;
                angle = getRealAngle(game.player.x + (double) Player.size / 2, game.player.y + (double) Player.size / 2);
                color = Color.pink;
                setColor(color);
                xpDrop = 0;
                break;
        }
        setSize(size, size);
    }

    /**
     * applies affects that should apply when enemy gets damaheg pre: existence
     * of boolean damage and int damagedTimer post: enemy turns red temporarily
     * after getting damaged
     */
    public void damagedAffects() {

        if (damaged) {//changes color to damaged
            stunned = true;
            stunnedCounter = 0;
            setColor(damagedColor);
            damagedTimer++;
        }

        if (damagedTimer != 0 && damagedTimer % 10 == 0) {//resets colour back down to normal
            damagedTimer = 0;
            damaged = false;
            setColor(color);
            damagedColor = Color.RED;
        }
    }

    /**
     * gets sent backward from the target.pre: given GameObject with applicabale
     * x and y values post: the cooridnates of the current GameObject is moved
     * away from the given GameObject based on amount.
     */
    public void knockBack(double amount, GameObject target) {
        double playerAngle = Math.atan2(target.y - y, target.x - x);
        if (target.y == y && target.x == x) { //ensures knockback works when enemies fully on top of each other
            x += r.nextInt(20);
            y += r.nextInt(20);
        } else { //push away
            x -= (Math.cos(playerAngle) * amount);
            y -= (Math.sin(playerAngle) * amount);
        }
    }

    /**
     * runs the behaviour and actions of the enemy for every act pre: existence
     * of boolean stunned, int stunndedCounter and int displayOld post: runs the
     * movent of the enemy, wether or not it should be stunned and their attacks
     */
    public void behaviour(int type) {
        if (stunned) { //stuns the enemy 
            if (stunnedCounter == stunDuration) {
                stunned = false;
            }
            stunnedCounter++;
        } else {
            //behaviour of enemies
            switch (type) {
                case 0: //normal and miniboss have same behaviour
                case 1: //miniboss bahaviour
                    //chases after the player
                    chase(speed, game.player);
                    setPosition();

                    break;
                case 2: //gunner behaviour

                    //chases the player for set time
                    if (displayOld < 75) {
                        chase(speed, game.player);
                        setPosition();
                    } else if (displayOld == 100) { //shoots and teleports away after set time
                        game.projectile = new Projectile(game, this.x, this.y);
                        game.add(game.projectile);
                        PolygonGame.projectiles.add(game.projectile);
                        spawnEnemy(3);

                    } else if (displayOld >= 100) { //blinks between cyan and color
                        if (displayOld % 10 == 0) {
                            setColor(Color.CYAN);
                        } else if (displayOld % 5 == 0) {
                            setColor(color);
                        }

                        if (displayOld == 190) { //resets
                            displayOld = 0;
                            setColor(color);
                        }
                    }

                    displayOld++;
                    break;
                case 3: //throwing goblin behaviour
                    //chases the player for set time so as to enter the screen
                    if (displayOld < 75) {
                        displayOld++;
                        chase(speed, game.player);
                        setPosition();
                    } else if (x + size / 2 < 0 || y + size / 2 < 0) { //ensures center of the enmy is on screen
                        chase(speed, game.player);
                        setPosition();
                    } else { //after being on sreen, randomly spawns bats that shoot at the enemy.

                        if (r.nextInt(300) < 5) {
                            game.enemy = new Enemy(game, 4, 2, game.enemySpawnSeed, this.x, this.y);
                            game.add(game.enemy);
                            PolygonGame.enemies.add(game.enemy);
                        }
                    }

                    break;
                case 4: //bat behaviour
                    //shoots at the players location from when it was created
                    shoot(speed, angle);
                    setPosition();

                    //ensures enemy appeared on screen
                    if (!appearedOnGame && x > 0 && y > 0) {
                        appearedOnGame = true;
                    }
                    //destory enemy when enters offscreen after having been onscreen
                    if (appearedOnGame && (x < 0 || x > game.getFieldWidth() || y < 0 || y > game.getFieldHeight())) {
                        game.remove(this); // Remove enemy if health is depleted
                        PolygonGame.enemies.remove(this); // Remove enemy from the list
                    }
                    break;
            }
        }
    }

}
