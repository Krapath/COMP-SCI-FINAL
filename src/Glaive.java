
import java.awt.Color;
import java.util.ArrayList;

public class Glaive extends GameObject {

    int size = 20;
    Polygon game;
    //double xVel;
    //double yVel;
    //int distanceTraveled = 0;
    int damage = 1;
    int pierceCount = 60;
    Double angle = 0.05;
    int radius = 80;
    Double speed = 0.05;
    int pierceDelay = 0;
    ArrayList<Enemy> hitEnemies = new ArrayList<Enemy>();

    //ArrayList<Enemy> hitEnemies = new ArrayList<Enemy>();
    public Glaive(Polygon game) {
        this.game = game;
        setLocation(game.player.getX() + 80, game.player.getY() + 80); // update position
        setSize(size, size); // size of the projectile
        setColor(Color.RED);
    }

    public void act() {
        if (game.choosingBuff) {
            return;// projectiles do not move or collide with enemies while the player is choosing a buff
        }

        angle += speed;

        int x = (int) (radius * Math.cos(angle) + game.player.getX()) - size / 2;
        int y = (int) (radius * Math.sin(angle) + game.player.getY()) - size / 2;
        setX(x);
        setY(y);

        //distanceTraveled += Math.sqrt(xVel * xVel + yVel * yVel); // update distance traveled
        //setLocation(getX() + (int) xVel, getY() + (int) yVel); // update position
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

            if (pierceDelay == pierceCount) {
                pierceDelay = 0;
                hitEnemies.clear();
            } else {
                pierceDelay++;
            }
        }

        // move the projectile according to its velocity
    }
}
