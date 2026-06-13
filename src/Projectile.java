
import java.awt.Color;
import java.util.ArrayList;

/**
 * A projectile weapon that launches in a certain direction, damaging things it
 * hits 
 * Author: Mohammad Sadeghi and Hugo To
 */
public class Projectile extends Weapon {

    PolygonGame game;
    double xVel;
    double yVel;
    int distanceTraveled = 0;
    private static final int DEFAULT_PROJECTILE_DAMAGE = 1;
    int damage = DEFAULT_PROJECTILE_DAMAGE;
    int pierceCount = 2;
    boolean friendly = true;
    double angle;
    double speed;

    public ArrayList<Enemy> hitEnemies = new ArrayList<Enemy>();

    public Projectile(PolygonGame game) {
        super(game, "Cast", "Projectile");
        this.game = game;

        setLocation(game.player.getX() + Player.size / 3, game.player.getY() + Player.size / 3); // update
        // position

        int width = (game.getWindowHeight() + game.getWindowWidth()) / 300;
        int height = width;
        setSize(width, height); // size of the projectile
        setColor(Color.YELLOW);

        angle = game.getAngle(game.player.getX() + Player.size / 2,
                game.player.getY() + Player.size / 2, game.getMouseX(), game.getMouseY());
        double speed = (game.getWindowHeight() + game.getWindowWidth()) / 150; // adjust as needed
        xVel = speed * Math.cos(angle);
        yVel = speed * Math.sin(angle);

        Player.weapons.add(this);
        SoundEffects.play("SFX/PROJECTILE.wav", -14.0f);

    }

    /**
     * create an unfriendly projectile spawned at given coordinates. givenX
     * initial x coordinate givenY initial y coordinate
     */
    public Projectile(PolygonGame game, double givenX, double givenY) {
        super(game, "Cast", "Projectile");
        this.game = game;
        friendly = false;
        setLocation((int) (givenX + 0.5), (int) (givenY + 0.5)); // update
        x = givenX;
        y = givenY;
        // position
        setSize(15, 15); // size of the projectile
        setColor(Color.RED);

        angle = getRealAngle(game.player.x + (double) Player.size / 2, game.player.y + (double) Player.size / 2);
        speed = 40.0; // adjust as needed
        Player.weapons.add(this);

    }

    public void act() {
        if (PolygonGame.gamePause) {
            return;// projectiles do not move or collide with enemies while the player is choosing
            // a buff

        }
        if (friendly) {
            distanceTraveled += Math.sqrt(xVel * xVel + yVel * yVel); // update distance traveled
            setLocation(getX() + (int) xVel, getY() + (int) yVel); // update position

            if (distanceTraveled > (game.getWindowHeight() + game.getWindowWidth()) / 6) { // remove projectile after it has traveled a certain distance
                game.remove(this);
                distanceTraveled = 0; // reset distance traveled for the next projectile
            }

            if (damage(this, hitEnemies, pierceCount, damage))//runs damage code
            {
                pierceCount--;
            }

            if (pierceCount == 0) {
                game.remove(this);             // Remove projectile after it hits an enemy
                PolygonGame.projectiles.remove(this); // Remove projectile from the list
                // Exit loop after collision
            }
            // move the projectile according to its velocity
        } else {
            shoot(speed, angle);
            setPosition();
            if (collides(game.player)) {
                Player.health -= 1;
                SoundEffects.play("SFX/DAMAGED.wav", -3.0f);
                game.remove(this);
                PolygonGame.projectiles.remove(this);
            }
            if (x < 0 || x > game.getFieldWidth() || y < 0 || y > game.getFieldHeight()) {
                game.remove(this); // Remove enemy if health is depleted
                PolygonGame.projectiles.remove(this);
            }
        }

    }

    /**
     * 
     * apply damage to enemies collided with the attacker and handles pierce
     * pre: none
     * post: deals damage to enemies, adds enemies to arraylist hitEnemies, decreases pierce count, and activates missiles and lightning
     */
    public boolean damage(GameObject attacker, ArrayList<Enemy> hitEnemies, int pierceCount, int damage) {

        boolean hit = false;
        for (int i = 0; i < PolygonGame.enemies.size(); i++) {
            Enemy target = PolygonGame.enemies.get(i);
            if (attacker.collides(target)) {
                boolean alreadyHit = false;

                // Check if enemy was already hit, if so, don't hit again
                for (int j = 0; j < hitEnemies.size(); j++) {
                    if (target == hitEnemies.get(j)) {
                        alreadyHit = true;
                    }
                }

                if (!alreadyHit) {
                    hitEnemies.add(target); // Count as hit from now on
                    hit = true;
                    target.health -= damage; // Reduce enemy health on collision
                    target.knockBack(10.0, game.player);

                    target.damaged = true;

                    // If chain lightning is active and this is the first enemy hit, activate it
                    if (Player.chainLightningActive && pierceCount == 2) {
                        game.lightning = new ChainLightning(target, game);
                        game.add(game.lightning);
                    }

                    // If ATG missile is active, spawn a missile when the first enemy is hit
                    if (Player.atgMissileActive && pierceCount == 2) {
                        game.atgMissile = new AtGMissileMk1(game);
                        game.add(game.atgMissile);
                    }

                }
            }

        }
        return hit;
    }

}
