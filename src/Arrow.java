
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import javax.swing.*;

/**
 * a projectile weapon that travels in a straight line based on given angle
 * Author: Hugo To
 */
public class Arrow extends Weapon {

    PolygonGame game;
    public double targetAngle;
    public double speed;

    // arrow stats
    public int pierce = 1;
    private static final int DEFAULT_ARROW_DAMAGE = 1;
    static int damage = DEFAULT_ARROW_DAMAGE;
    double arrowCenterX;
    double arrowCenterY;

    private static final double IMAGE_ORIENTATION_OFFSET = Math.PI / 4.0;

    static Image arrowImage;
    int spriteSize;
    int size;
    int shaftWidth;
    int shaftHeight;
    int projSize;
    ArrayList<Enemy> hitEnemies = new ArrayList<Enemy>();

    public Arrow(PolygonGame game, double angle) {
        super(game, "Cast", "Arrow");
        this.game = game;

        targetAngle = angle;
        size = (game.getWindowWidth() + game.getWindowHeight()) / 200; // projectile size is 1/250 of the entire window
        projSize = size * 5;
        shaftWidth = (int) (size * 1.5);
        shaftHeight = (int) (size * 3);
        speed = (game.getWindowHeight() + game.getWindowWidth()) / 750;

        setSize(projSize, projSize);
        setColor(Color.BLUE);
        x = game.player.x - projSize / 4;
        y = game.player.y - projSize / 4;
        Player.weapons.add(this);

        setPosition();

        arrowImage = new ImageIcon("Images/Sprites/Arrow.png").getImage();

        spriteSize = projSize;
    }

    // render the arrow sprite rotated to its sprite angle.
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();
        g2d.translate(getWidth() / 2.0, getHeight() / 2.0);
        g2d.rotate(spriteAngle);
        g2d.translate(-getWidth() / 2.0, -getHeight() / 2.0);

        if (arrowImage != null) {
            g2d.drawImage(arrowImage, 0, 0, spriteSize, spriteSize, null);
        }
        g2d.setTransform(old);
    }

    /**
     * return true if the given enemy's center intersects the arrow's rotated
     * hitbox The arrow has an orientation offset, so the collision math must
     * use the same offset to remain consistent with the visible sprite. derived
     * from the original match hitbox calculations
     */
    public boolean arrowHits(Enemy e) {
        // get positions directly using real double precision properties
        double ex = e.getRealX() + e.size / 2.0;
        double ey = e.getRealY() + e.size / 2.0;

        double localX = ex - arrowCenterX;
        double localY = ey - arrowCenterY;

        // unrotate the relative tracking space back to flat alignment angles
        double cos = Math.cos(-targetAngle);
        double sin = Math.sin(-targetAngle);
        double rotX = localX * cos - localY * sin;
        double rotY = localX * sin + localY * cos;

        // include enemy radius bounds so arrows register hits on edges
        double enemyRadius = e.size / 2.0;

        return Math.abs(rotY) <= (shaftWidth / 2.0 + enemyRadius)
                && rotX >= -(shaftHeight / 2.0 + enemyRadius)
                && rotX <= (shaftHeight / 2.0 + enemyRadius);
    }

    public void act() {
        if (PolygonGame.gamePause) {
            return;
        }
        // The arrow sprite image is drawn with a 45-degree native orientation.
        // Apply the same offset here so the rendered arrow and hitbox stay aligned
        // with the intended target angle.
        spriteAngle = targetAngle - IMAGE_ORIENTATION_OFFSET;
        shoot(speed, targetAngle);
        setPosition();

        // calculate arrow center with full precision
        arrowCenterX = x + projSize / 2.0;
        arrowCenterY = y + projSize / 2.0;

        for (int i = 0; i < PolygonGame.enemies.size(); i++) { // for every enemy in the game
            Enemy target = PolygonGame.enemies.get(i);
            if (arrowHits(target)) { // if it collides with an enemy
                boolean hit = false;
                for (int j = 0; j < hitEnemies.size(); j++) { // for every enemy already hit
                    if (target == hitEnemies.get(j)) { // if this enemy has already been hit, sets hit to true
                        hit = true;
                    }
                }

                if (!hit) { // if this enemy has not already been hit by this
                    // arrow
                    hitEnemies.add(target);
                    target.health -= damage;
                    target.damaged = true;

                    pierce--;

                    if (pierce == 0) {
                        game.remove(this);
                        PolygonGame.arrows.remove(this);
                    }

                    if (target.health <= 0) {
                        hitEnemies.remove(target);
                    }
                }
            }
        }
        // remove when offscreen
        if (x < -projSize || x > game.getFieldWidth() + projSize || y < -projSize
                || y > game.getFieldHeight() + projSize) {
            game.remove(this);
            PolygonGame.arrows.remove(this);
        }

    }

}
