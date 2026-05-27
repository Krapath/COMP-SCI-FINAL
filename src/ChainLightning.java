import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Graphics2D; // so i can have thicker lines for the chain lightning
import java.awt.BasicStroke;


public class ChainLightning extends GameObject {


    /* two main array lists
     * 1. hitEnemies: to keep track of which enemies have already been hit by the chain lightning so they won't be hit again in the same chain
     * 2. enemyDisplay: to keep track of the enemies that the lightning should be drawn
     */

    Random r = new Random();
    double randomAngleStatic = r.nextDouble();
    Polygon game;   
    int damage = 1;
    int chainCount = 3;
    int chainRange;
    int durationVisible = 10; // the amount of frames the chain lightning is visible for
    int lightningSize = 5; // the thickness of the lightning, will be used in the paint method to set the stroke of the graphics object
    
    
    ArrayList<Enemy> enemyDisplay = new ArrayList<Enemy>(); // to keep track of the enemies that the lightning should be drawn to, will be used in the paint method to draw lines between these enemies
    
    
    // will handle the math and logic for the chain lightning buff, only incremments number values and checks for collisions, the actual drawing of the lightning will be handled in the draw method
    public ChainLightning(Enemy enemy, Polygon game) {
        this.game = game;
        chainRange = (game.getWindowWidth() + game.getWindowHeight()) / 2 / 10; // the range that the chain lightning can jump to the next target, can be adjusted for more or less range
        setSize(game.getWindowWidth(), game.getWindowHeight()); // make the canvas size of the entire window so it can draw lightning anywhere
        Enemy initialTarget = enemy; // the first target of the chain lightning is the enemy that was hit by the projectile
        ArrayList<Enemy> hitEnemies = new ArrayList<Enemy>();
        hitEnemies.add(initialTarget); // Add the first hit enemy
        enemyDisplay.add(initialTarget); // Add the first hit enemy to the display list for drawing the lightning
       
        for (int i = 0; i < chainCount; i++) {
            Enemy target = null;
            double closestDistance = Double.MAX_VALUE;

            for (int j = 0; j < game.enemies.size(); j++) {
                Enemy potentialTarget = game.enemies.get(j);
                
                if (!hitEnemies.contains(potentialTarget)) { // find the closest enemy that has not already been hit and is within range
                    double distance = Math.sqrt(Math.pow(game.enemies.get(j).getX() - initialTarget.getX(), 2) + Math.pow(game.enemies.get(j).getY() - initialTarget.getY(), 2));
                    if (distance < closestDistance && distance <= chainRange) {
                        closestDistance = distance;
                        target = potentialTarget;
                    }
                }
            }
           if (target != null) {
            target.health -= damage; 
            target.setColor(Color.CYAN);
            
            hitEnemies.add(target); // mark this enemy as hit so it won't be hit again in this chain
            initialTarget = target;   // chain lightning jumps to the next target
            enemyDisplay.add(target); // Add the target to the display list for drawing the lightning
            } else {
            break; // no more valid targets, end the chain early
            }
        } 
    } 
    

    

    
    // will handle the drawing of the chain lightning, will draw a line from the enemy to the first target, then from each target to the next target in the chain
    public void paint(Graphics g) {
        if (game.choosingBuff) return;// projectiles do not move or collide with enemies while the player is choosing a buff
        
        //Link for 2d line graphics: https://stackoverflow.com/questions/16995308
        Graphics2D g2d = (Graphics2D) g; // cast to Graphics2D to use thicker lines
        g2d.setStroke(new BasicStroke(lightningSize)); // set line thickness for the lightning
        Color lightningColor = new Color(0, 255, 255, 15*durationVisible); // make the lightning more transparent as it gets closer to disappearing
        g2d.setColor(lightningColor);
        for (int i = 0; i < enemyDisplay.size() - 1; i++) {
            Enemy first = enemyDisplay.get(i);
            Enemy second = enemyDisplay.get(i + 1);
            // store coordinates of the two enemies 
            int x1 = first.getX() + first.size / 2;
            int y1 = first.getY() + first.size / 2;
            int x2 = second.getX() + second.size / 2;
            int y2 = second.getY() + second.size / 2;
            // find distance between enemies
            double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
            // if the distance is great enough, draw lighting with random offset
            if (distance > 20){
                double randomAngle = r.nextDouble() * 2 * Math.PI; // random angle for the offset
                int randomness = (int) (distance/2); // the maximum distance of the random offset, can be adjusted for more or less randomness
                
                // TODO: decide on two types of lightning
                

                int offsetX = (int) (Math.cos(randomAngle) * randomness); // random offset in the x direction 
                int offsetY = (int) (Math.sin(randomAngle) * randomness); // random offset in the y 

               
                g2d.drawLine(x1, y1, x1 + offsetX, y1 + offsetY); // draw the first half of the lightning with a random offset
                g2d.drawLine(x1 + offsetX, y1 + offsetY, x2, y2); // draw the second half of the lightning from the offset point to the target

            } else {
            g2d.drawLine(x1, y1, x2, y2);
            }
        

        }
    }
    

    public void act() { // no need for movement code since the chain lightning jumps from enemy to enemy, immediately
        if (game.choosingBuff) {
            return;// projectiles do not move or collide with enemies while the player is choosing a buff
        }

        repaint(); // repaint to update the lightning drawing each frame
        durationVisible--;
        if (durationVisible <= 0) {
            game.remove(this);// remove the chain lightning from the game after it has been visible for a certain duration
        }




    }

}