
import java.awt.Color;
import java.util.ArrayList;

public class Methods extends GameObject {

    PolygonGame game;

    public Methods (PolygonGame game) {
        this.game = game;
    }

    public void act() {
        // Method body is currently empty
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

                    if (game.enemies.get(i).health <= 0) {
                        // Create an XP orb at the location of the defeated enemy
                        XpOrb xp = new XpOrb(enemyX, enemyY, game); 
                        game.add(xp);          // Add the xp orb to the game
                        game.xpOrbs.add(xp);   // Add the xp orb to the list

                        hitEnemies.remove(game.enemies.get(i));

                        game.remove(game.enemies.get(i)); // Remove enemy if health is depleted
                        game.enemies.remove(i);            // Remove enemy from the list
                    }
                }
            }
            
        }
        return hit;
    }
    
    
    
    
    
    
}
