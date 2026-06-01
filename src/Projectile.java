
import java.awt.Color;
import java.util.ArrayList;

public class Projectile extends GameObject {

    PolygonGame game;
    double xVel;
    double yVel;
    int distanceTraveled = 0;
    int damage = 1;
    int pierceCount = 2;

    public ArrayList<Enemy> hitEnemies = new ArrayList<Enemy>();

    public Projectile(PolygonGame game) {
        this.game = game;
        setLocation(game.player.getX() + Player.size / 3, game.player.getY() + Player.size / 3); // update
        // position
        setSize(10, 10); // size of the projectile
        setColor(Color.YELLOW);

        double angle = game.getAngle(game.player.getX() + Player.size / 2,
                game.player.getY() + Player.size / 2, game.getMouseX(), game.getMouseY());
        double speed = 20; // adjust as needed
        xVel = speed * Math.cos(angle);
        yVel = speed * Math.sin(angle);

    }

    public void act() {
        if (PolygonGame.gamePause) {
            return;// projectiles do not move or collide with enemies while the player is choosing
            // a buff

        }
        distanceTraveled += Math.sqrt(xVel * xVel + yVel * yVel); // update distance traveled
        setLocation(getX() + (int) xVel, getY() + (int) yVel); // update position

        if (distanceTraveled > 400) { // remove projectile after it has traveled a certain distance
            game.remove(this);
            distanceTraveled = 0; // reset distance traveled for the next projectile
        }

        if (game.method.damage(this, hitEnemies, pierceCount, damage))//runs damage code
        {
            pierceCount--;
        }

        if (pierceCount == 0) {
            game.remove(this);             // Remove projectile after it hits an enemy
            game.projectiles.remove(this); // Remove projectile from the list
            // Exit loop after collision
        }
        // move the projectile according to its velocity
    }

}
