
import java.util.ArrayList;

import javax.swing.ImageIcon;

import java.awt.*;

import javax.swing.ImageIcon;

import java.awt.geom.AffineTransform;

public class Glaive extends Weapon {

    int size = (int) (Player.size / 1);
    PolygonGame game;
    //double xVel;
    //double yVel;
    //int distanceTraveled = 0;
    int pierceCooldown = 60; // the amount of frames between acts until it can pierce, ensures that it doesnt hit enemies multiple times
    Double angle;
    Double startingAngle;
    int radius = 100;
    int framesPerRotation = Player.speed * 5;
    Double speed = 2 * Math.PI / framesPerRotation;
    int pierceTimer = 0; // the current timer for pierce.
    static int damage = 1;
    static int glaiveCount = 0;
    public int rotationTimer = 0;
    static Image glaiveImage;

    ArrayList<Enemy> hitEnemies = new ArrayList<Enemy>(); 

    public Glaive(PolygonGame game, double startingAngle) {
        super(game, "Passive", "Glaive");
        this.game = game;
        angle = startingAngle;
        this.startingAngle = startingAngle;
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
        if (rotationTimer == framesPerRotation) {
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
                    PolygonGame.enemies.get(i).damaged=true;

                    int enemyX = PolygonGame.enemies.get(i).getX();
                    int enemyY = PolygonGame.enemies.get(i).getY();

                    if (PolygonGame.enemies.get(i).health <= 0) {

                        XpOrb xp = new XpOrb(enemyX, enemyY, game); // create an xp orb at the location of the defeated enemy
                        game.add(xp);// add the xp orb to the game
                        PolygonGame.xpOrbs.add(xp); // add the xp orb to the list

                        //if (hitEnemies.contains(game.enemies.get(i))) {
                        hitEnemies.remove(PolygonGame.enemies.get(i));
                        // }

                        game.remove(PolygonGame.enemies.get(i)); // remove enemy if health is depleted
                        PolygonGame.enemies.remove(i); // remove enemy from the list

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
