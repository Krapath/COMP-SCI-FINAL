import java.awt.Color;
public class Projectile extends GameObject {

    Polygon game;
    double xVel;
    double yVel;
    int distanceTraveled = 0;
    int damage = 1;

    public Projectile(Polygon game) {
        this.game = game;
        setLocation(game.player.getX()+game.player.size/3, game.player.getY()+game.player.size/3); // update position
        setSize(10, 10); // size of the projectile
        setColor(Color.YELLOW);
        if (game.mouseLeftPressed()){
                double angle = game.getAngle(game.player.getX()+game.player.size/2, game.player.getY()+game.player.size/2,game.getMouseX(), game.getMouseY());
                double speed = 20; // adjust as needed
                xVel = speed * Math.cos(angle);
                yVel = speed * Math.sin(angle);
        }   
    }

    public void act() { 
        distanceTraveled += Math.sqrt(xVel * xVel + yVel * yVel); // update distance traveled
        setLocation(getX() + (int)xVel, getY() + (int)yVel); // update position

        if (distanceTraveled > 400) { // remove projectile after it has traveled a certain distance
            game.remove(this);
            distanceTraveled = 0; // reset distance traveled for the next projectile
        }

        for (int i = 0; i < game.enemies.size(); i++) {
            if (collides(game.enemies.get(i))) {
                game.enemies.get(i).health -= damage; // reduce enemy health on collision

                game.enemies.get(i).setColor(Color.RED);

                int enemyX = game.enemies.get(i).getX();
                int enemyY = game.enemies.get(i).getY();
                if (game.enemies.get(i).health <= 0) {


                    XpOrb xp = new XpOrb(enemyX, enemyY, game); // create an xp orb at the location of the defeated enemy
                    game.add(xp);// add the xp orb to the game
                    game.xpOrbs.add(xp); // add the xp orb to the list
                    
                    game.remove(game.enemies.get(i)); // remove enemy if health is depleted
                    game.enemies.remove(i); // remove enemy from the list
                }

                game.remove(this); // remove projectile after it hits an enemy
                game.projectiles.remove(this); // remove projectile from the list
                break; // exit loop after collision
            }
        }
  

        // move the projectile according to its velocity

    }

}
