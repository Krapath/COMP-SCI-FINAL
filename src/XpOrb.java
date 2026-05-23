import java.awt.Color;
public class XpOrb extends GameObject {
    Polygon game;

    public XpOrb(int enemyX, int enemyY, Polygon game) {
        this.game = game;
        setLocation(enemyX, enemyY); // update position
        setSize(10, 10);
        setColor(Color.YELLOW);
    }


    public void act() {
        // check for collision with player
        int xpX=getX();
        int xpY=getY();
        if ((Math.abs(game.player.getX() - xpX) <= 50) && (Math.abs(game.player.getY() - xpY) <= 50)){// only move towards the player if the xp orb is close to player
            if (xpX < game.player.getX()) xpX += 1;
            if (xpX > game.player.getX()) xpX -= 1;
            if (xpY < game.player.getY()) xpY += 1;
            if (xpY > game.player.getY()) xpY -= 1;

            setX(xpX);
            setY(xpY);
        } 
        
        if (collides(game.player)) {
            game.player.score += 1; // increase player score on collision
            game.remove(this); // remove xp orb after collision
        }
    }
}
