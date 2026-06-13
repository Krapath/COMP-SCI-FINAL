import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import java.awt.Graphics2D; // so i can have thicker lines for the chain lightning
import java.io.File;
import java.awt.BasicStroke;

/**
 * chain lightning: visual and logic for a chaining lightning effect that
 * damages nearby enemies.
 */
public class ChainLightning extends Weapon {

    Random r = new Random();
    double randomAngleStatic = r.nextDouble();
    PolygonGame game;

    private static final int CHAIN_COUNT_DEFAULT = 3;
    static int chainCount = CHAIN_COUNT_DEFAULT;
    static int damage = 1;
    int chainRange;
    private static final int DURATION_VISIBLE_DEFAULT = 10;
    int durationVisible = DURATION_VISIBLE_DEFAULT; // the amount of frames the chain lightning is visible for
    int lightningSize; // the thickness of the lightning,

    // keep track of the enemies that the lightning should be drawn
    ArrayList<Enemy> enemyDisplay = new ArrayList<Enemy>();
    // keep track of which enemies have already been hit by the chain lightning so
    // they won't be hit again in the same chain
    ArrayList<Enemy> hitEnemies = new ArrayList<Enemy>();

    /**
     * Creates chain lightning starting from an initial enemy.
     * Unlike normal weapons, all the damage and branching math runs instantly
     * right here in the constructor. The act() method is just used for visuals.
     */
    public ChainLightning(Enemy enemy, PolygonGame game) {
        super(game, "Cast", "Chain Lightning");
        this.game = game;

        chainRange = (game.getWindowWidth() + game.getWindowHeight()) / 20; //
        setSize(game.getWindowWidth(), game.getWindowHeight());
        // the first target of the chain lightning is the enemy that was hit by the
        // projectile
        Enemy initialTarget = enemy;

        hitEnemies.add(initialTarget);
        // add the first hit enemy to the display list for drawing the lightning
        enemyDisplay.add(initialTarget);
        lightningSize = (game.getWindowWidth() + game.getWindowHeight()) / 2 / 300;
        for (int i = 0; i < chainCount; i++) {
            Enemy target = null;
            double closestDistance = Double.MAX_VALUE;

            for (int j = 0; j < PolygonGame.enemies.size(); j++) {
                Enemy potentialTarget = PolygonGame.enemies.get(j);

                if (!hitEnemies.contains(potentialTarget)) { // find the closest enemy that has not already been hit and
                                                             // is within range
                    double distance = Math.sqrt(Math.pow(PolygonGame.enemies.get(j).getX() - initialTarget.getX(), 2)
                            + Math.pow(PolygonGame.enemies.get(j).getY() - initialTarget.getY(), 2));
                    if (distance < closestDistance && distance <= chainRange) {
                        closestDistance = distance;
                        target = potentialTarget;
                    }
                }
            }
            if (target != null) {
                target.health -= damage;
                Enemy.damagedColor = Color.CYAN;
                target.damaged = true;

                hitEnemies.add(target); // mark this enemy as hit so it won't be hit again in this chain
                initialTarget = target; // chain lightning jumps to the next target
                enemyDisplay.add(target); // Add the target to the display list for drawing the lightning
            } else {
                break; // no more valid targets, end the chain early
            }
        }
        Player.weapons.add(this);

    }

    public void paint(Graphics g) {
        if (PolygonGame.gamePause)
            return;

        Graphics2D g2d = (Graphics2D) g; // cast to Graphics2D to use thicker lines
        g2d.setStroke(new BasicStroke(lightningSize)); // set line thickness for the lightning
        // make the lightning more transparent as it gets closer to disappearing
        Color lightningColor = new Color(0, 255, 255, 15 * durationVisible); 
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
            if (distance > 20) {
                double randomAngle = r.nextDouble() * 2 * Math.PI; // random angle for the offset
                int randomness = (int) (distance / 2); // the maximum distance of the random offset

                int offsetX = (int) (Math.cos(randomAngle) * randomness); // random offset in the x direction
                int offsetY = (int) (Math.sin(randomAngle) * randomness); // random offset in the y
               
                // draw the first half of the lightning with a randomoffset
                g2d.drawLine(x1, y1, x1 + offsetX, y1 + offsetY);

                // draw the second half of the lightning from the offset point to the target
                g2d.drawLine(x1 + offsetX, y1 + offsetY, x2, y2);

            } else {
                g2d.drawLine(x1, y1, x2, y2);
            }

        }
    }

    // no need for movement code since the chain lightning jumps from enemy to
    // instantenously
    public void act() {
        if (PolygonGame.gamePause) {
            return;
        }

        repaint();
        durationVisible--;
        if (durationVisible <= 0) {
            game.remove(this);
        }

    }

}