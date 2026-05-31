import java.awt.Color;
@SuppressWarnings("unused")

public class XpOrb extends GameObject {
    PolygonGame game;

    // TODO: scale to window size instead of hardcoding values
    int speed = 5; // maybe accelerate as it gets closer to player
    int distanceAttraction = 100; // the distance at which the xp orb starts moving towards the player, can be adjusted for better gameplay
    public XpOrb(int enemyX, int enemyY, PolygonGame game) {
        this.game = game;
        setLocation(enemyX, enemyY); // update position
        setSize(10, 10);
        setColor(Color.YELLOW);
    }


    public void act() {
        if (PolygonGame.gamePause) return; // xp orbs do not move or collide with the player while the player is choosing a buff
        // check for collision with player
        int xpX=getX();
        int xpY=getY();
        if ((Math.abs(game.player.getX() - xpX) <= distanceAttraction) && (Math.abs(game.player.getY() - xpY) <= distanceAttraction)){// only move towards the player if the xp orb is close to player
            if (xpX < game.player.getX()) xpX += speed;
            if (xpX > game.player.getX()) xpX -= speed;
            if (xpY < game.player.getY()) xpY += speed;
            if (xpY > game.player.getY()) xpY -= speed;

            setX(xpX);
            setY(xpY);
        } 
        
        if (collides(game.player)) {
            Player.score += 1; // increase player score on collision
            Player.xp+=1;
            game.remove(this); // remove xp orb after collision
        }
    }
}
