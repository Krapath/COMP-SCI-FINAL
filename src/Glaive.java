
import java.awt.Color;
import java.util.ArrayList;

public class Glaive extends Weapon {

    int size = (int) (Player.size / 1.5);
    PolygonGame game;
    //double xVel;
    //double yVel;
    //int distanceTraveled = 0;
    int pierceCooldown = 60; // the amount of frames between acts until it can pierce, ensures that it doesnt hit enemies multiple times
    Double angle;
    Double startingAngle;
    int radius = 100;
    Double speed = 2 * Math.PI / 100;
    int pierceTimer = 0; // the current timer for pierce.
    static int damage = 1;
    static int glaiveCount = 0;
    public int rotationTimer = 0;

    //TODO: right now pierce system is disabled and will hit enemies more than once
    ArrayList<Enemy> hitEnemies = new ArrayList<Enemy>(); // TODO: maybe make universal for other buffs

    public Glaive(PolygonGame game, double startingAngle) {
        super(game, "Passive", "Glaive");
        this.game = game;
        angle = startingAngle;
        this.startingAngle = startingAngle;
        x = (radius * Math.cos(angle) + game.player.x) - size / 2 + game.player.size / 2;
        y = (radius * Math.sin(angle) + game.player.y) - size / 2 + game.player.size / 2;
        setLocation((int) x, (int) y); // update position
        setSize(size, size); // size of the projectile
        setColor(Color.RED);
        game.player.weapons.add(this);
    }
    
    
    

    public void act() {
        if (PolygonGame.gamePause) {
            return;// projectiles do not move or collide with enemies while the player is choosing a buff
        }

        if (rotationTimer == 100) {
            rotationTimer = 0;
            game.createGlaive(game.numberOfGlaives);
        }
        //scales the speed of the glaive based on the player
        x = (radius * Math.cos(angle) + game.player.x) - size / 2 + game.player.size / 2;
        y = (radius * Math.sin(angle) + game.player.y) - size / 2 + game.player.size / 2;
        setPosition();
        
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
                    game.enemies.get(i).damaged=true;

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
        
        angle += speed;
        rotationTimer++;
        // move the projectile according to its velocity
    }
}
