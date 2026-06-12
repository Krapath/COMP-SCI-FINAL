
import java.awt.*;
import java.util.Random;
import javax.swing.ImageIcon;


public class XpOrb extends GameObject {

    Random r = new Random();
    PolygonGame game;

    double speed = 20.0; // maybe accelerate as it gets closer to the player
    int distanceAttraction;
    boolean chasing = false;
    Image xpOrb;
    /**
     * A constructor used for when eneimies drop xp orbs
     */
    public XpOrb(int enemyX, int enemyY, int size, PolygonGame game) {
        //assign values to object variables
        this.game = game;
        this.distanceAttraction = (game.getWindowHeight() + game.getWindowWidth()) / 30;
        setLocation(r.nextInt(size + 1) + enemyX, r.nextInt(size + 1) + enemyY); // update position
        int width = (game.getWindowHeight() + game.getWindowWidth()) / 300;
        int height = width;
        setSize(width, height);
        setColor(Color.YELLOW);
        x = getX();
        y = getY();
        xpOrb = new ImageIcon("Images/Sprites/XPORB_SPRITE.png").getImage();
    }

    public void paint(Graphics g) {
        //draw image on xp orb
        if (xpOrb != null) {
            g.drawImage(xpOrb, 0, 0, getWidth(), getHeight(), null);
        }
    }

    public void act() {
        if (PolygonGame.gamePause) {
            return; // xp orbs do not move or collide with the player while the player is choosing a buff
        }        // check for collision with player
        //calculates if player is close to exp orb to decide if it should move towards it
        if ((Math.abs(game.player.x - x) <= distanceAttraction) && (Math.abs(game.player.y - y) <= distanceAttraction)) {
            chasing = true;
        }

        // if it sees the player once it will continuously chase even if the player moves out the initial range
        if (chasing) {
            chase(speed, game.player);
            setPosition();
        }

        if (collides(game.player)) {
            // random number for sound effect
            int randNum = r.nextInt(10) + 1;

            SoundEffects.play("SFX/XP/xp" + randNum + ".wav", -20.0f);
            Player.xp += 1; // increa player xp
            game.remove(this); // remove xp orb after collision
            PolygonGame.xpOrbs.remove(this);
        }
    }
}
