
import java.awt.Color;
import java.util.ArrayList;

public class Projectile extends GameObject {

    PolygonGame game;
    double xVel;
    double yVel;
    int distanceTraveled = 0;
    int damage = 1;
    int pierceCount = 2;

    public ArrayList<Enemy> hitEnemies = new ArrayList<Enemy>();

    public Projectile(PolygonGame game) {
        this.game = game;
        setLocation(game.player.getX() + Player.size / 3, game.player.getY() + Player.size / 3); // update
        // position
        setSize(10, 10); // size of the projectile
        setColor(Color.YELLOW);

        double angle = game.getAngle(game.player.getX() + Player.size / 2,
                game.player.getY() + Player.size / 2, game.getMouseX(), game.getMouseY());
        double speed = 20; // adjust as needed
        xVel = speed * Math.cos(angle);
        yVel = speed * Math.sin(angle);
        

    }

    public void act() {
        if (PolygonGame.gamePause) {
            return;// projectiles do not move or collide with enemies while the player is choosing
            // a buff

        }

        
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

                    game.enemies.get(i).setColor(Color.RED);

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
