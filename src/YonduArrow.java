import java.awt.Color;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.Graphics;
import java.util.Random;

public class YonduArrow extends GameObject {
    Random r = new Random();
    int size = 50;
    PolygonGame game;
    int damage = 1;
    int pierceCooldown = 60;
    Double angle = 0.20;
    int radius = 100;
    Double rotateSpeed = 0.02;
    double shootSpeed = 20;
    int pierceTimer = 0;
    int rotationTimer = 200 + r.nextInt(100); // how long the arrow rotates around the player before flying off,
                                              // randomize a bit so not every arrow is the same
    int aimingTimer = 120;
    double aimOffsetX;
    double aimOffsetY;
    double arrowCX;
    double arrowCY; // actual center of arrow in screen space
    Enemy closestTarget; // the enemy the arrow is currently aiming at, will be null if not aiming at
                         // anything

    double targetAngle; // the angle the arrow will fly towards once it stops aiming, calculated when
                        // the arrow finishes rotating around the player

    boolean returning = false; // whether the arrow is flying back to the player after going off screen, if it
                               // goes off screen it will return to the player instead of disappearing so the
                               // player can reuse it if they miss, this also gives it a cool boomerang effect

    double spriteAngle=0; // angle at which the arrow tip is pointing
    ArrayList<Enemy> hitEnemies = new ArrayList<Enemy>();
    // arrow dimensions
    int shaftWidth = 10;
    int shaftHeight = 60;
    int tipWidth = 15;
    int tipHeight = 20;

    public YonduArrow(PolygonGame game) {
        this.game = game;
        setSize(game.getWindowWidth(), game.getWindowHeight()); // full screen so nothing clips
        setLocation(0, 0);
        setColor(Color.YELLOW);
        arrowCX = game.player.getX() + 100;
        arrowCY = game.player.getY() + 100;

    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();

        g2d.translate(arrowCX, arrowCY); // move origin to arrow center
        g2d.rotate(spriteAngle); // rotate around that point

        // shaft: centered on origin
        g2d.setColor(Color.WHITE);
        // negative shaftHeight so it extends upwards from the center
        g2d.fillRect(-shaftWidth / 2, -shaftHeight / 2, shaftWidth, shaftHeight);

        // tip: sits at top of shaft
        g2d.setColor(Color.RED);
        // negative tipHeight so it extends upwards from the top of the shaft
        g2d.fillRect(-tipWidth / 2, -shaftHeight / 2 - tipHeight, tipWidth, tipHeight);

        // arrow hitbox for debugging
        g2d.setColor(Color.RED);
        g2d.drawRect(-tipWidth / 2, -shaftHeight / 2 - tipHeight, tipWidth, shaftHeight + tipHeight);

        g2d.setTransform(old); // restore
    }

    public void act() {
        if (PolygonGame.gamePause)
            return;

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
                for (Enemy e : game.enemies) {
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
            if(closestTarget != null && !game.enemies.contains(closestTarget)){
                closestTarget = null;
            }

            // get a new target
            if (closestTarget == null) {
                double closestDist = Double.MAX_VALUE;
                for (Enemy e : game.enemies) {
                    double dx= e.getX()-arrowCX;
                    double dy= e.getY()-arrowCY;
                    double dist = dx*dy+dy*dy; // takes square value, since not actually using the actual distance, just comparing values 
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
                spriteAngle = Math.atan2(dy, dx) + Math.PI / 2;
            }

            if (aimingTimer == 0 && closestTarget !=null) {
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
                if (arrowCX > game.player.getX() - size && arrowCX < game.player.getX() + Player.size + size &&
                        arrowCY > game.player.getY() - size && arrowCY < game.player.getY() + Player.size + size) {

                    returning = false;
                    rotationTimer = 200 + r.nextInt(100);
                    aimingTimer = 120;
                    hitEnemies.clear();

                }
            }
        }
        // hitbox using rotating rectangle
        boolean canHit = (aimingTimer == 0 && rotationTimer == 0); // can only hit while shooting or returning

        for (int i = 0; i < game.enemies.size(); i++) {

            if (canHit && arrowHits(game.enemies.get(i))) {
                boolean hit = false;

                for (int j = 0; j < hitEnemies.size(); j++) {
                    if (game.enemies.get(i) == hitEnemies.get(j)) {
                        hit = true;
                    }
                }

                if (!hit) { // if this enemy has not already been hit by this arrow
                    hitEnemies.add(game.enemies.get(i));
                    game.enemies.get(i).health -= damage;
                    game.enemies.get(i).setColor(Color.RED);

                    int enemyX = game.enemies.get(i).getX();
                    int enemyY = game.enemies.get(i).getY();

                    if (game.enemies.get(i).health <= 0) {
                        XpOrb xp = new XpOrb(enemyX, enemyY, game);
                        game.add(xp);
                        game.xpOrbs.add(xp);
                        hitEnemies.remove(game.enemies.get(i));
                        game.remove(game.enemies.get(i));
                        game.enemies.remove(i);
                    }
                }
            }
            // if the arrow has pierced through enough enemies, reset pierce so it can hit
            // the same enemy again

        }
        if (pierceTimer == pierceCooldown) {
            pierceTimer = 0;
            hitEnemies.clear();
        } else {
            pierceTimer++;
        }
    }

    // works by reversing the rotation on the rectangle/ applying it to the enemy then chceking if the enemy is within it

    // custom collision detection for rotating rectangle hitbox, does not use the built in collides function since that is just a bounding box and does not rotate with the arrow
    boolean arrowHits(Enemy e) {
        double ex = e.getX() + e.size / 2.0;
        double ey = e.getY() + e.size / 2.0;

        // get the enemies position relative to arrow center

        double localX = ex - arrowCX;
        double localY = ey - arrowCY;

        // subtract the PI/2 offset since spriteAngle always has it added
        // 2d rotatio nmatrix
        double checkAngle = -(spriteAngle - Math.PI / 2);
        double rotX = localX * Math.cos(checkAngle) - localY * Math.sin(checkAngle);
        double rotY = localX * Math.sin(checkAngle) + localY * Math.cos(checkAngle);
        
        // check if its inside the rectangle
        return Math.abs(rotX) <= tipWidth / 2.0
                && rotY >= -shaftHeight / 2.0 - tipHeight
                && rotY <= shaftHeight / 2.0;
    }
}