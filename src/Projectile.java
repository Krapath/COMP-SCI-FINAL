import java.awt.Color;
public class Projectile extends GameObject {

    Polygon game;
    double xVel;
    double yVel;
    int distanceTraveled = 0;
    public Projectile(Polygon game) {
        this.game = game;
        setLocation(game.player.getX(), game.player.getY()); // update position
        setSize(10, 10); // size of the projectile
        setColor(Color.YELLOW);
        if (game.mouseLeftPressed()){
                double angle = game.getAngle(game.player.getX()-15, game.player.getY()-15,game.getMouseX(), game.getMouseY());
                double speed = 5; // adjust as needed
                xVel = speed * Math.cos(angle);
                yVel = speed * Math.sin(angle);
        }   
    }

    public void act() { 
        distanceTraveled += Math.sqrt(xVel * xVel + yVel * yVel); // update distance traveled
        setLocation(getX() + (int)xVel, getY() + (int)yVel); // update position

        if (distanceTraveled > 200) { // remove projectile after it has traveled a certain distance
            game.remove(this);
            distanceTraveled = 0; // reset distance traveled for the next projectile
        }

        for (int i = 0; i < game.enemies.size(); i++) {
            if (collides(game.enemies.get(i))) {
                game.remove(game.enemies.get(i)); // remove enemy if hit by projectile
                game.remove(this); // remove projectile after it hits an enemy
                game.projectiles.remove(this); // remove projectile from the list
                break; // exit loop after collision
            }
        }
  

        // move the projectile according to its velocity

    }

}
