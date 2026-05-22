import java.awt.Color;

public class Player extends GameObject {

    static final int SIZE  = 30;
    static final int SPEED = 30;
    static int attackDelay = 0;
    int health = 20;

    Polygon game;

    public Player(Polygon game) {
        this.game = game;
        setSize(SIZE, SIZE);
        setColor(Color.BLUE);
    }

    public void act() {
        attackDelay++;
        int x = getX();
        int y = getY();

        if (game.AKeyPressed()) x -= SPEED;
        if (game.DKeyPressed()) x += SPEED;
        if (game.WKeyPressed()) y -= SPEED;
        if (game.SKeyPressed()) y += SPEED;

        // make sure it stays in bounds
        if (x < 0) x = 0;
        if (x > game.getFieldWidth() - SIZE) x = game.getFieldWidth() - SIZE;
        if (y < 0) y = 0;
        if (y > game.getFieldHeight() - SIZE) y = game.getFieldHeight() - SIZE;

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