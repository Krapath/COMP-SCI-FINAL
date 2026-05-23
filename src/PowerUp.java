import java.util.Random;
import java.awt.Color;
import java.awt.Graphics;

public class PowerUp extends GameObject {
    Random r = new Random();
    Polygon game;

    public PowerUp(Polygon game) {
        this.game = game;
        setSize(50, 100);
        setColor(Color.BLUE);

    }

    public void act() {
        if (collides(game.player)) {
            game.player.health += 1; // increase player health on collision
            game.remove(this); // remove powerup after collision
        }
    }

}
