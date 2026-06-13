
import java.util.ArrayList;
import java.awt.*;
import javax.swing.ImageIcon;
import java.awt.geom.AffineTransform;

/**
 * A rotating glaive that spins around the player and damages enemies hit.
 * Author: Mohammad
 */
public class Glaive extends Weapon {

    int size = (int) (Player.size / 1);
    PolygonGame game;
    Double angle;
    private static final int GLAIVE_ROTATION_FACTOR = 5;
    int framesPerRotation = Player.speed * GLAIVE_ROTATION_FACTOR; //numbers of acts for 1 full rotation
    Double speed = 2 * Math.PI / framesPerRotation;
    int radius;
    public int rotationTimer = 0; //how many acts the glaive had been rotating for
    static Image glaiveImage;

    ArrayList<Enemy> hitEnemies = new ArrayList<Enemy>();

    /**
     * creates a glaive rotating around the player starting from given radian
     * angle rperesenting position in circle
     */
    public Glaive(PolygonGame game, double startingAngle) {
        super(game, "Passive", "Glaive");
        this.game = game;

        //sets up starting values
        angle = startingAngle;
        radius = (game.getWindowHeight() + game.getWindowWidth()) / 30;
        x = (radius * Math.cos(angle) + game.player.x) - size / 2 + Player.size / 2;
        y = (radius * Math.sin(angle) + game.player.y) - size / 2 + Player.size / 2;
        setLocation((int) x, (int) y); // update position
        setSize(size, size); // size of the projectile

        setColor(Color.RED);
        Player.weapons.add(this);
        glaiveImage = new ImageIcon("Images/Sprites/GLAIVE_SPRITE.png").getImage();

    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();
        g2d.translate(getWidth() / 2.0, getHeight() / 2.0);
        g2d.rotate(spriteAngle);
        g2d.translate(-getWidth() / 2.0, -getHeight() / 2.0);

        // REMEMBER CHANGE THE GLAIVES SIZE
        if (glaiveImage != null) {
            g2d.drawImage(glaiveImage, 0, 0, (int) (size / 0.85), (int) (size / 0.85), null);
        }
        g2d.setTransform(old);

    }

    public void act() {
        if (PolygonGame.gamePause) {
            return;// projectiles do not move or collide with enemies while the player is choosing a buff
        }

        spriteAngle -= 1;
        if (rotationTimer == framesPerRotation) {//resets glaive after a full rotation to prevent bugs
            rotationTimer = 0;
            game.createGlaive(game.numberOfGlaives);
        }
        //scales the speed of the glaive based on the player
        x = (radius * Math.cos(-angle) + game.player.x) - size / 2 + Player.size / 2;
        y = (radius * Math.sin(-angle) + game.player.y) - size / 2 + Player.size / 2;
        setPosition();

        for (int i = 0; i < PolygonGame.enemies.size(); i++) {
            if (collides(PolygonGame.enemies.get(i))) {
                boolean hit = false;

                for (int j = 0; j < hitEnemies.size(); j++) { //if enemy already hit, dont hit again.
                    if (PolygonGame.enemies.get(i) == hitEnemies.get(j)) {
                        hit = true;
                    }
                }

                if (!hit) { //if not hit
                    hitEnemies.add(PolygonGame.enemies.get(i)); //count as hit from now on
                    PolygonGame.enemies.get(i).health -= damage; // reduce enemy health on collision
                    PolygonGame.enemies.get(i).damaged = true;

                    if (PolygonGame.enemies.get(i).health <= 0) {

                        hitEnemies.remove(PolygonGame.enemies.get(i));

                        game.remove(PolygonGame.enemies.get(i)); // remove enemy if health is depleted
                        PolygonGame.enemies.remove(i); // remove enemy from the list
                        i--;

                    }
                }
            } else {
                for (int j = 0; j < hitEnemies.size(); j++) { //if enemy already hit, dont hit again.
                    if (PolygonGame.enemies.get(i) == hitEnemies.get(j)) {
                        hitEnemies.remove(PolygonGame.enemies.get(i));
                    }
                }
            }

        }

        //rotates the glaive around the circle based on speed
        angle += speed;
        rotationTimer++;
    }
}
