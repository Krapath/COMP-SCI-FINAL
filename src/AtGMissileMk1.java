import java.util.ArrayList;
import java.util.Random;

import java.awt.*;

import javax.swing.ImageIcon;

import java.awt.geom.AffineTransform;



/**
 * atgmissilemk1: a homing missile with an initial spiral then tracking behavior.
 */
public class AtGMissileMk1 extends Weapon {
    Random r = new Random();
    PolygonGame game;
    public double randomAngle = r.nextDouble() * Math.PI * 2;
    int size;
    int radius;
    double spiralAngle = 0.1;
    double spiralSpeed = 0.2;
    double velX;
    double velY;
    int pivotX;
    int pivotY;
    int spiralDuration = r.nextInt(30) + 30; // the duration of the initial spiral movement, can be adjusted for a longer or shorter spiral
    int randDirectionDuration = 15; // random direction movement
    int explosionDuration =17;
    int speedReduction = 2;
    int spriteSize;
    public int randomEnemy;
    boolean canDamage = true;
    
    Enemy target;
    
    static Image missileImage;
    private static final int DEFAULT_ATG_MISSILE_DAMAGE = 1;
    static int damage = DEFAULT_ATG_MISSILE_DAMAGE;

    /**
     * create a new atg missile
     */
    public AtGMissileMk1(PolygonGame game) {
        super(game, "Cast", "AtGMissileMk1");
        this.game = game;
        size = (game.getWindowWidth() + game.getWindowHeight()) / 250; // projectile size is 1/100 of the entire window
        radius = size; // the radius of the spiral, can be adjusted for a tighter or looser spiral
        setColor(new Color(255, 165,0,15*explosionDuration));
        pivotX = game.player.getX() - size / 2;
        pivotY = game.player.getY() - size / 2;
        setLocation(pivotX, pivotY);
        spriteSize = size * 4;  
        setSize(spriteSize, spriteSize);
        if (PolygonGame.enemies.size() > 0) {
            randomEnemy = r.nextInt(PolygonGame.enemies.size());
            target = PolygonGame.enemies.get(randomEnemy);
        }
        missileImage = new ImageIcon("Images/Sprites/MISSILE_SPRITE.png").getImage();

        Player.weapons.add(this);
    }


    /**
     * paint: render the missile sprite and explosion visuals.
     */
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();

   
            if(!canDamage){
                g2d.setColor(new Color(255, 215,0,15*explosionDuration));
                g2d.fillOval(0,0, getWidth(), getHeight());
                return;
            }
            int offset = (spriteSize - getWidth()) / 2; // center it on the hitbox

            g2d.translate(getWidth() / 2.0, getHeight() / 2.0);
            g2d.rotate(spriteAngle);
            g2d.translate(-getWidth() / 2.0, -getHeight() / 2.0);

            if (missileImage != null) {
                g2d.drawImage(missileImage, -offset, -offset, spriteSize, spriteSize, null);
            } 

            g2d.setTransform(old);
    }
    /**
     * update missile movement, targeting, and explosion state each frame.
     */
    public void act() {

        // derived from the visual rotation from velocity so it always faces the direction it is moving
        // this way it actively tracks the target unlike Yondu arrow and arrow
        spriteAngle = Math.atan2(velY, velX);


        if (PolygonGame.gamePause)
            return; // projectiles do not move or collide with enemies while the player is choosing
                    // a buff

        
        if (target != null && PolygonGame.enemies.contains(target) && canDamage) {

            //spiral phase
            if (spiralDuration > 0) { // spiral around the player for a short duration after being fired
                spiralSpeed += 0.0001;
                spiralAngle += spiralSpeed;
                spiralDuration--;
                int x = (int) (radius * Math.cos(spiralAngle) + pivotX);
                int y = (int) (radius * Math.sin(spiralAngle) + pivotY);

                velX = x - getX();
                velY = y - getY();
                setX(x);
                setY(y);
                radius += 1;
            //random direction phase
            } else if (randDirectionDuration > 0) { // move in random direction after spiraling
                randDirectionDuration--;
                double speed = 25;
                velX = speed * Math.cos(randomAngle);
                velY = speed * Math.sin(randomAngle);
                setX(getX() + (int) velX);
                setY(getY() + (int) velY);
            //attacking phase
            } else if (randDirectionDuration <= 0 && spiralDuration <= 0) { // after spiraling and moving in a random
                                                                            // direction, move towards the random enemy
                                                                            // target
                int x = getX();
                int y = getY();
                if (PolygonGame.enemies.contains(target)) {// if the target enemy was destroyed before the missile could
                                                         // reach it, choose a new target if there are any enemies left
     
                    int enemyX = target.getX();
                    int enemyY = target.getY();
                    double targetAngle = game.getAngle(x, y, enemyX, enemyY);
                    double speed =25;
                    velX = speed * Math.cos(targetAngle);
                    velY = speed * Math.sin(targetAngle);
                    setX(getX() + (int) velX);
                    setY(getY() + (int) velY);

                    if (collides(target) && canDamage) { // if collides with target, damage the target and remove the projectile
                        target.health -= damage;
                        setSize((int)(size*3.5),(int)(size*3.5));


                        explosionDuration--;
                        setColor(new Color(255, 215,0));
    					target.damaged = true;
                        target.speed=target.speed/speedReduction;
                        canDamage = false;

                    }

                }

            }
        // if the enemy was damaged before the arrow shoots, will find a new target
        } else if (canDamage) {
            if (PolygonGame.enemies.size() > 0) {
                randomEnemy = r.nextInt(PolygonGame.enemies.size());
                target = PolygonGame.enemies.get(randomEnemy);
            } else { // continue spiraling if no enemy
                target = null;
                int x = (int) (radius * Math.cos(spiralAngle) + pivotX);
                int y = (int) (radius * Math.sin(spiralAngle) + pivotY);

                setX(x);
                setY(y);
                radius += 1;
            }

        } else if (!canDamage){
            explosionDuration--;
            setColor(new Color(255, 215,0,15*explosionDuration));
            repaint();
            if (explosionDuration ==0){
            	game.remove(this);
            	target.speed=target.speed*speedReduction;
            }
        } 
        

    }

}
