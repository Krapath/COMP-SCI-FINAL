
/** the actual player object which can be controlled
 * author: Hugo and Mohammad
 */
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class Player extends GameObject {

    Random r = new Random();

    static int size;
    static int speed;
    static int attackTimer = 0;
    static int attackCooldown = 10;
    static int health = 10;
    static int maxHealth = 10;
    static int xp = 0;
    static int level = 1;

    // boolea ncheck for specific buffs
    static boolean chainLightningActive = false;
    static boolean atgMissileActive = false;
    static boolean glaiveActive = false;
    static boolean matchStickActive = false;
    static boolean dashActive = false;
    static boolean arrowSpreadActive = false;

    static double xpReq = (int)Math.floor(2 * level * Math.log(level + 1));

    static boolean invulnerable = false;
    static int invulnerableDuration = 30;
    static int invulnerableTimer = 0;
    PolygonGame game;

    // store all weapons and abilities
    static ArrayList<Ability> abilities = new ArrayList<Ability>();
    static ArrayList<Weapon> weapons = new ArrayList<Weapon>();

    public Player(PolygonGame game) {
        this.game = game;
        size = (game.getWindowWidth() + game.getWindowHeight()) / 100; // player size is 1/100 of the entire window
        speed = (game.getWindowWidth() + game.getWindowHeight()) / 200; // speed is 1/100 of the entire window size
        setLocation(r.nextInt(game.getWindowWidth() / 2), r.nextInt(game.getWindowHeight() / 2)); // middle
        setSize(size, size);
        setColor(new Color(0, 0, 255, 0));
        x = getX();
        y = getY();
    }

    public void act() {
        if (PolygonGame.gamePause) {
            return;// player does not move or collide with enemies while the player is choosing a buff

        }
        attackTimer++; //the delay counter between attacks

        //vector math for find direction of motion, preventing faster movement on diagonals
        double up = 0.0, down = 0.0, left = 0.0, right = 0.0;
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

        //finds the angle play is moving towards and the vectors
        down -= up;
        right -= left;
        double angle = Math.atan2(down, right);

        if (!(down == 0 && right == 0)) { //when vectors exists, move play based on vectors
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

        setPosition();

        //damages player when collding with enemies and gives invulnerability framesd
        for (int i = 0; i < PolygonGame.enemies.size(); i++) {
            Enemy target = PolygonGame.enemies.get(i);
            if (collides(target)) {

                if (!invulnerable) {
                    Player.health -= target.enemyDamage; // reduce enemy health on collision
                    SoundEffects.play("SFX/DAMAGED.wav", -3.0f);
                    target.health -= 1;

                    if (target.health <= 0) {
                        game.remove(target); // remove enemy if health is depleted
                        PolygonGame.enemies.remove(target);
                    }
                    invulnerable = true;
                    break; // exit loop after collision
                }
            }
        }
        if (invulnerable) {
            setColor(Color.LIGHT_GRAY);
            invulnerableTimer++;
            if (invulnerableTimer == invulnerableDuration) {
                invulnerableTimer = 0;
                setColor(Color.BLUE);
                invulnerable = false;
            }
        }

    }

}
