
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Projectile extends Weapon {

    PolygonGame game;
    double xVel;
    double yVel;
    int distanceTraveled = 0;
    int damage = 1;
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
        double speed = 20; // adjust as needed
        xVel = speed * Math.cos(angle);
        yVel = speed * Math.sin(angle);

        SoundEffects.play("SFX/PROJECTILE.wav", -14.0f);

    }

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

        angle = getRealAngle(game.player.x + (double) Player.size / 2, game.player.y + (double) game.player.size / 2);
        speed = 40.0; // adjust as needed

    }

    public void act() {
        if (PolygonGame.gamePause) {
            return;// projectiles do not move or collide with enemies while the player is choosing
            // a buff

        }

        if (friendly) {
            distanceTraveled += Math.sqrt(xVel * xVel + yVel * yVel); // update distance traveled
            setLocation(getX() + (int) xVel, getY() + (int) yVel); // update position

            if (distanceTraveled > 400) { // remove projectile after it has traveled a certain distance
                game.remove(this);
                distanceTraveled = 0; // reset distance traveled for the next projectile
            }

            if (damage(this, hitEnemies, pierceCount, damage))//runs damage code
            {
                pierceCount--;
            }

            if (pierceCount == 0) {
                game.remove(this);             // Remove projectile after it hits an enemy
                game.projectiles.remove(this); // Remove projectile from the list
                // Exit loop after collision
            }
            // move the projectile according to its velocity
        } else {
            shoot(speed, angle);
            setPosition();
            if (collides(game.player)) {
                Player.health -= 1;
                game.remove(this);
                game.projectiles.remove(this);
            }
            if (x < 0 || x > game.getFieldWidth() || y < 0 || y > game.getFieldHeight()) {
                game.remove(this); // Remove enemy if health is depleted
                game.projectiles.remove(this);
            }
        }

    }

    public boolean damage(GameObject attacker, ArrayList<Enemy> hitEnemies, int pierceCount, int damage) {

        boolean hit = false;
        for (int i = 0; i < game.enemies.size(); i++) {
            if (attacker.collides(game.enemies.get(i))) {
                boolean alreadyHit = false;

                // Check if enemy was already hit, if so, don't hit again
                for (int j = 0; j < hitEnemies.size(); j++) {
                    if (game.enemies.get(i) == hitEnemies.get(j)) {
                        alreadyHit = true;
                    }
                }

                if (!alreadyHit) {
                    hitEnemies.add(game.enemies.get(i)); // Count as hit from now on
                    hit = true;
                    game.enemies.get(i).health -= damage; // Reduce enemy health on collision

                    game.enemies.get(i).damaged = true;

                    int enemyX = game.enemies.get(i).getX();
                    int enemyY = game.enemies.get(i).getY();

                    // If chain lightning is active and this is the first enemy hit, activate it
                    if (Player.chainLightningActive && pierceCount == 2) {
                        game.lightning = new ChainLightning(game.enemies.get(i), game);
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
