import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
@SuppressWarnings("unused")

public class AtGMissileMk1 extends GameObject {
    Random r = new Random();
    PolygonGame game;
    public double randomAngle = r.nextDouble() * Math.PI * 2;
    int size;
    int damage = 1;
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
    public int randomEnemy;
    boolean canDamage = true;
    Enemy target;

    public AtGMissileMk1(PolygonGame game) {
        this.game = game;
        size = (game.getWindowWidth() + game.getWindowHeight()) / 250; // projectile size is 1/100 of the entire window
        radius = size; // the radius of the spiral, can be adjusted for a tighter or looser spiral
        setSize(size, size);
        setColor(new Color(255, 165,0,15*explosionDuration));
        pivotX = game.player.getX() - size / 2;
        pivotY = game.player.getY() - size / 2;
        setLocation(pivotX, pivotY);

        if (game.enemies.size() > 0) {
            randomEnemy = r.nextInt(game.enemies.size());
            target = game.enemies.get(randomEnemy);
        }
    }

    public void act() {

        if (PolygonGame.gamePause)
            return; // projectiles do not move or collide with enemies while the player is choosing
                    // a buff

        if (target != null && game.enemies.contains(target) && canDamage) {

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
            } else if (randDirectionDuration > 0) { // move in random direction after spiraling
                randDirectionDuration--;
                double speed = 25;
                velX = speed * Math.cos(randomAngle);
                velY = speed * Math.sin(randomAngle);
                setX(getX() + (int) velX);
                setY(getY() + (int) velY);
            } else if (randDirectionDuration <= 0 && spiralDuration <= 0) { // after spiraling and moving in a random
                                                                            // direction, move towards the random enemy
                                                                            // target
                int x = getX();
                int y = getY();
                if (game.enemies.contains(target)) {// if the target enemy was destroyed before the missile could
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
                        setSize(size*3,size*3);
                        explosionDuration--;
                        setColor(new Color(255, 215,0));
                        target.speed=target.speed/speedReduction;
                        canDamage = false;

                    }

                }

            }

        } else if (canDamage) {
            if (game.enemies.size() > 0) {
                randomEnemy = r.nextInt(game.enemies.size());
                target = game.enemies.get(randomEnemy);
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
            if (explosionDuration ==0){
            	game.remove(this);
            	target.speed=target.speed*speedReduction;
            }
        } 
        

    }

}
