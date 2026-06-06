
import java.awt.Color;
import java.util.ArrayList;

public class Glaive extends Weapon {

    int size = (int) (Player.size / 1.5);
    PolygonGame game;
    //double xVel;
    //double yVel;
    //int distanceTraveled = 0;
    int pierceCooldown = 60; // the amount of frames between acts until it can pierce, ensures that it doesnt hit enemies multiple times
    Double angle = 0.0;
    int radius = 100;
    Double speed = 1.0;
    int pierceTimer = 0; // the current timer for pierce.
    static int damage = 1;
    static int glaiveCount = 0;
    
    //TODO: right now pierce system is disabled and will hit enemies more than once
    ArrayList<Enemy> hitEnemies = new ArrayList<Enemy>(); // TODO: maybe make universal for other buffs

    public Glaive(PolygonGame game, double startingAngle) {
        super(game, "Passive", "Glaive");
        this.game = game;
        angle = startingAngle;
        setLocation(game.player.getX() + 80, game.player.getY() + 80); // update position
        setSize(size, size); // size of the projectile
        setColor(Color.RED);
        game.player.weapons.add(this);
    }

    public void act() {
        if (PolygonGame.gamePause) {
            return;// projectiles do not move or collide with enemies while the player is choosing a buff
        }

        //scales the speed of the glaive based on the player
        angle += speed * Player.speed / 100.0;

        double x = (radius * Math.cos(angle) + game.player.getX()) - size / 2 + Player.size / 2;
        double y = (radius * Math.sin(angle) + game.player.getY()) - size / 2 + Player.size / 2;
        setX((int) (x + 0.5));
        setY((int) (y + 0.5));

        for (int i = 0; i < game.enemies.size(); i++) {
            if (collides(game.enemies.get(i))) {
                boolean hit = false;

                for (int j = 0; j < hitEnemies.size(); j++) { //if enemy already hit, dont hit again.
                    if (game.enemies.get(i) == hitEnemies.get(j)) {
                        hit = true;
                    }
                }

                if (!hit) { //if not hit
                    hitEnemies.add(game.enemies.get(i)); //count as hit from now on
                    game.enemies.get(i).health -= damage; // reduce enemy health on collision
                    game.enemies.get(i).setColor(Color.RED);

                    int enemyX = game.enemies.get(i).getX();
                    int enemyY = game.enemies.get(i).getY();

                    if (game.enemies.get(i).health <= 0) {

                        XpOrb xp = new XpOrb(enemyX, enemyY, game); // create an xp orb at the location of the defeated enemy
                        game.add(xp);// add the xp orb to the game
                        game.xpOrbs.add(xp); // add the xp orb to the list

                        //if (hitEnemies.contains(game.enemies.get(i))) {
                        hitEnemies.remove(game.enemies.get(i));
                        // }

                        game.remove(game.enemies.get(i)); // remove enemy if health is depleted
                        game.enemies.remove(i); // remove enemy from the list

                    }
                }
            }

            if (pierceTimer == pierceCooldown) { // if the pierce cooldown has been reached, reset the pierce timer and clear the list of hit enemies
                pierceTimer = 0;
                hitEnemies.clear();
            } else {
                pierceTimer++;
            }
        }

        // move the projectile according to its velocity
    }
}
