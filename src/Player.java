
import java.awt.Color;
import java.util.Random;
public class Player extends GameObject {
	
	Random r = new Random();
	
    static int size;
    static int speed;
    static int attackDelay = 0;
    static int health = 20;
    static int maxHealth = 20;
    static int score = 0;
    static int xp = 0;
    static double x, y;
    static int level = 1;
    static boolean chainLightningActive = false; // static so all projectiles have property
    static boolean atgMissileActive = false; // static so all projectiles have property
    static boolean glaiveActive = false;
    static boolean yonduArrowActive = false;
    static double xpReq = 5 * level * Math.log(level + 1);

    PolygonGame game;

    public Player(PolygonGame game) {
        this.game = game;
        size = (game.getWindowWidth() + game.getWindowHeight()) / 100; // player size is 1/100 of the entire window
        speed = (game.getWindowWidth() + game.getWindowHeight()) / 200; // speed is 1/100 of the entire window size
        setLocation(r.nextInt(game.getWindowWidth()), r.nextInt(game.getWindowHeight())); // middle
        setSize(size, size);
        setColor(Color.BLUE);
        x = getX();
        y = getY();
    }

    public void act() {
        if (PolygonGame.gamePause) {
            return;// player does not move or collide with enemies while the player is choosing a buff

                }attackDelay++;

        double up = 0.0, down = 0.0, left = 0.0, right = 0.0;

        //setSize(size, size);
        if (game.AKeyPressed()) {
            left += 1;
        }
        if (game.DKeyPressed()) {
            right += 1;
        }
        if (game.WKeyPressed()) {
            up += 1;

        }
        if (game.SKeyPressed()) {
            down += 1;
        }

        down -= up;
        right -= left;

        double angle = Math.atan2(down, right);

        if (!(down == 0 && right == 0)) {
            x += Math.cos(angle) * speed;
            y += Math.sin(angle) * speed;
        }

        // make sure it stays in bounds
        if (x < 0) {
            x = 0;
        }
        if (x > game.getFieldWidth() - size) {
            x = game.getFieldWidth() - size;
        }
        if (y < 0) {
            y = 0;
        }
        if (y > game.getFieldHeight() - size) {
            y = game.getFieldHeight() - size;
        }

        setX((int) x);
        setY((int) y);

        for (int i = 0; i < game.enemies.size(); i++) {
            if (collides(game.enemies.get(i))) {
                Player.health -= game.enemies.get(i).enemyDamage; // reduce enemy health on collision
                game.enemies.get(i).health -= 1;

                if (game.enemies.get(i).health == 1) {
                    game.remove(game.enemies.get(i)); // remove enemy if health is depleted
                    game.enemies.remove(i);
                }
                break; // exit loop after collision
            }
        }

        if (Player.health <= 0) {
            game.player.setColor(Color.GRAY); // change player color to gray when health is depleted
        }
    }

}
