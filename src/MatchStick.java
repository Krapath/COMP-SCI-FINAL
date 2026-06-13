
import java.awt.Color;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.Graphics;
import java.util.Random;

/**
 * atchstick: a throwable match that aims then shoots and can return like a
 * boomerang, doing damage Author: Hugo and Mohammad
 */
public class MatchStick extends Weapon {

    Random r = new Random();
    int size = 50;
    PolygonGame game;
    private static final int DEFAULT_MATCHSTICK_DAMAGE = 3;
    static int damage = DEFAULT_MATCHSTICK_DAMAGE;
    int pierceCooldown = 60;
    Double angle = 0.20;
    int radius = 100;
    Double rotateSpeed = 0.02;
    double shootSpeed = 20;
    int pierceTimer = 0;
    static int aimingTimer = 30;

    public int rotationTimer = aimingTimer + r.nextInt(aimingTimer); // how long the match rotates around the player before flying off,
    // randomize a bit so not every match is the same
    double aimOffsetX;
    double aimOffsetY;
    double arrowCX;
    double arrowCY; // actual center of match in screen space
    Enemy closestTarget; // the enemy the match is currently aiming at, will be null if not aiming at
    // anything

    double targetAngle; // the angle the match will fly towards once it stops aiming, calculated when
    // the match finishes rotating around the player

    boolean returning = false; // whether the match is flying back to the player after going off screen, if it
    // goes off screen it will return to the player instead of disappearing so the
    // player can reuse it if they miss, this also gives it a cool boomerang effect

    double spriteAngle = 0; // angle at which the match tip is pointing
    ArrayList<Enemy> hitEnemies = new ArrayList<Enemy>();
    // arrow dimensions
    int shaftWidth;
    int shaftHeight;
    int tipWidth;
    int tipHeight;

    public MatchStick(PolygonGame game) {
        super(game, "Passive", "MatchStick");
        this.game = game;
        setSize(game.getWindowWidth(), game.getWindowHeight()); // full screen so nothing clips
        setColor(Color.YELLOW);
        arrowCX = game.player.getX() + 100;
        arrowCY = game.player.getY() + 100;
        Player.weapons.add(this);
        shaftWidth = (game.getWindowHeight() + game.getWindowWidth()) / 300;
        shaftHeight = (game.getWindowHeight() + game.getWindowWidth()) / 50;
        tipWidth = (game.getWindowHeight() + game.getWindowWidth()) / 200;
        tipHeight = (game.getWindowHeight() + game.getWindowWidth()) / 150;;

    }

    /**
     * draw the matchstick with shaft and tip using transforms.
     */
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();

        g2d.translate(arrowCX, arrowCY); // move origin to arrow center
        g2d.rotate(spriteAngle); // rotate around that point

        // shaft: centered on origin
        g2d.setColor(new Color(255, 223, 180));
        // negative shaftHeight so it extends upwards from the center
        g2d.fillRect(-shaftWidth / 2, -shaftHeight / 2, shaftWidth, shaftHeight);

        // tip: sits at top of shaft
        g2d.setColor(Color.RED);
        // negative tipHeight so it extends upwards from the top of the shaft
        g2d.fillRect(-tipWidth / 2, -shaftHeight / 2 - tipHeight, tipWidth, tipHeight);

        g2d.setTransform(old); // restore
    }

    /**
     * matchStickHits: return true if the enemy is inside the rotated match
     * hitbox works by reversing the rotation on the rectangle/ applying it to
     * the enemy then chceking if the enemy is within it custom collision
     * detection for rotating rectangle hitbox, does not use the built in
     * collides
     */
    boolean matchStickHits(Enemy e) {

        double ex = e.getRealX() + e.size / 2.0;
        double ey = e.getRealY() + e.size / 2.0;

        // get the enemies position relative to arrow center
        double localX = ex - arrowCX;
        double localY = ey - arrowCY;

        // subtract the PI/2 offset since spriteAngle always has it added
        // 2d rotation matrix
        // essentially unrotate the enemy by the negative of the arrow's angle so that the arrow is axis aligned
        // then check if the enemy's coordinates are within the bounds of the arrow's hitbox as if it were not rotated
        double checkAngle = -(spriteAngle - Math.PI / 2);
        double rotX = localX * Math.cos(checkAngle) - localY * Math.sin(checkAngle);
        double rotY = localX * Math.sin(checkAngle) + localY * Math.cos(checkAngle);

        // find radius to account for
        double enemyRadius = e.size / 2.0;

        // check if its inside the rectangle (expanded on all sides by enemyRadius)
        return Math.abs(rotX) <= (tipWidth / 2.0 + enemyRadius)
                && rotY >= (-shaftHeight / 2.0 - tipHeight - enemyRadius)
                && rotY <= (shaftHeight / 2.0 + enemyRadius);
    }

    public void act() {
        if (PolygonGame.gamePause) {
            return;
        }

        repaint();

        if (rotationTimer > 0) {
            --rotationTimer;
            angle += rotateSpeed;
            // keep arrow centered on player while rotating

            arrowCX = radius * Math.cos(angle) + game.player.getX() + Player.size / 2.0;
            arrowCY = radius * Math.sin(angle) + game.player.getY() + Player.size / 2.0;

            spriteAngle = angle + Math.PI;

            if (rotationTimer == 0) {

                // stores offset from player the moment it starts aiming
                aimOffsetX = arrowCX - game.player.getX();
                aimOffsetY = arrowCY - game.player.getY();

                // find closest target 
                closestTarget = null;
                double closestDist = Double.MAX_VALUE;
                for (Enemy e : PolygonGame.enemies) {
                    double dist = Math.sqrt(Math.pow(e.getX() - arrowCX, 2) + Math.pow(e.getY() - arrowCY, 2));
                    if (dist < closestDist) {
                        closestDist = dist;
                        closestTarget = e;
                    }
                }
            }

        } else if (aimingTimer > 0) {
            --aimingTimer;
            // keep arrow centered on player while aiming, but rotate to face the closest enemy
            arrowCX = game.player.getX() + aimOffsetX;
            arrowCY = game.player.getY() + aimOffsetY;

            // if target does not exist, invalidate it
            if (closestTarget != null && !PolygonGame.enemies.contains(closestTarget)) {
                closestTarget = null;
            }

            // get a new target
            if (closestTarget == null) {
                double closestDist = Double.MAX_VALUE;
                for (Enemy e : PolygonGame.enemies) {
                    double dx = e.getX() - arrowCX;
                    double dy = e.getY() - arrowCY;
                    double dist = dx * dx + dy * dy; // takes square value, since not actually using the actual distance, just comparing values 
                    if (dist < closestDist) {
                        closestDist = dist;
                        closestTarget = e;
                    }
                }
            }

            // point at the target
            if (closestTarget != null) {

                double dx = closestTarget.getX() + closestTarget.size / 2.0 - arrowCX;
                double dy = closestTarget.getY() + closestTarget.size / 2.0 - arrowCY;
                // since the arrow sprite is pointing up by default, add pi/2 to the angle so its angle relative to math conventions is 0
                spriteAngle = Math.atan2(dy, dx) + Math.PI / 2;
            }

            if (aimingTimer == 0 && closestTarget != null) {
                //save the target angle from teh enemy to the arrow
                targetAngle = Math.atan2(
                        closestTarget.getY() + closestTarget.size / 2.0 - arrowCY,
                        closestTarget.getX() + closestTarget.size / 2.0 - arrowCX);
            }

        } else {
            if (!returning) {
                arrowCX += Math.cos(targetAngle) * shootSpeed;
                arrowCY += Math.sin(targetAngle) * shootSpeed;
                spriteAngle = targetAngle + Math.PI / 2;
            }

            // fly back to player if it goes off screen
            if (arrowCX < 0 || arrowCX > game.getWindowWidth() || arrowCY < 0 || arrowCY > game.getWindowHeight()) {
                returning = true;
            }

            if (returning) {
                // take the angle between the player and the arrow
                double playerAngle = Math.atan2(
                        game.player.getY() + Player.size / 2.0 - arrowCY,
                        game.player.getX() + Player.size / 2.0 - arrowCX);
                //move the arrow back to the player
                arrowCX += Math.cos(playerAngle) * shootSpeed;
                arrowCY += Math.sin(playerAngle) * shootSpeed;
                spriteAngle = playerAngle + Math.PI / 2;
                // if the arrow has reached the player, reset it so it can be fired again
                if (arrowCX > game.player.getX() - size && arrowCX < game.player.getX() + Player.size + size
                        && arrowCY > game.player.getY() - size && arrowCY < game.player.getY() + Player.size + size) {

                    returning = false;
                    rotationTimer = 200 + r.nextInt(100);
                    aimingTimer = 120;
                    hitEnemies.clear();

                }
            }
        }
        // hitbox using rotating rectangle
        boolean canHit = (aimingTimer == 0 && rotationTimer == 0); // can only hit while shooting or returning

        for (int i = 0; i < PolygonGame.enemies.size(); i++) {
            Enemy target = PolygonGame.enemies.get(i);
            if (canHit && matchStickHits(target)) {
                boolean hit = false;

                for (int j = 0; j < hitEnemies.size(); j++) {
                    if (target == hitEnemies.get(j)) {
                        hit = true;
                    }
                }

                if (!hit) { // if this enemy has not already been hit by this arrow
                    hitEnemies.add(target);
                    target.health -= damage;
                    target.damaged = true;

                    if (target.health <= 0) {
                        hitEnemies.remove(target);
                    }
                }
            }
            // if the arrow has pierced through enough enemies, reset pierce so it can hit
            // the same enemy again

        }
        if (pierceTimer == pierceCooldown) { // resets pierce cooldown and allows enemy to be hit by same object again
            pierceTimer = 0;
            hitEnemies.clear();
        } else {
            pierceTimer++;
        }
    }

}
