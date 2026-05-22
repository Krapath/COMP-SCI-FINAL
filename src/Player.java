import java.awt.Color;

public class Player extends GameObject {

    static final int SIZE  = 30;
    static final int SPEED = 30;
    static int attackDelay = 0;

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
    }
}