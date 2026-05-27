
import java.awt.Color;

public class Player extends GameObject {

    public int size;
    public int speed;

    public int attackDelay = 0;
    public int health = 20;
    public int score = 0;

    Polygon game;

    public Player(Polygon game) {
        this.game = game;
        size = (game.getWindowWidth() + game.getWindowHeight()) / 100; // player size is 1/100 of the entire window
        speed = (game.getWindowWidth() + game.getWindowHeight()) / 200; // speed is 1/100 of the entire window size
        setSize(size, size);
        setColor(Color.BLUE);
    }

    public void act() {
        if (game.choosingBuff) return;// player does not move or collide with enemies while the player is choosing a buff
        attackDelay++;
        int x = getX();
        int y = getY();

        //setSize(size, size);
        if (game.AKeyPressed()) {
            x -= speed;
        }
        if (game.DKeyPressed()) {
            x += speed;
        }
        if (game.WKeyPressed()) {
            y -= speed;
        }
        if (game.SKeyPressed()) {
            y += speed;
        }

        // make sure it stays in bounds
        if (x < 0) {
            x = 0;
        }
        if (x > game.getFieldWidth() - size) {
            x = game.getFieldWidth() - size;
        }
        if (y < 0) {
            y = 0;
        }
        if (y > game.getFieldHeight() - size) {
            y = game.getFieldHeight() - size;
        }

        setX(x);
        setY(y);
        for (int i = 0; i < game.enemies.size(); i++) {
            if (collides(game.enemies.get(i))) {
                game.player.health -= game.enemies.get(i).enemyDamage; // reduce enemy health on collision
                game.remove(game.enemies.get(i)); // remove enemy if health is depleted
                game.enemies.remove(i);

                break; // exit loop after collision
            }
        }

        if (game.player.health <= 0) {
            game.player.setColor(Color.GRAY); // change player color to gray when health is depleted
        }
    }

}
