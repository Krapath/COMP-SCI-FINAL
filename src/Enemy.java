import java.awt.Color;
public class Enemy extends GameObject {

    static final int SIZE  = 10;
    static final int SPEED = 1;

    Polygon game;

    public Enemy(Polygon game) {
        this.game = game;
        setSize(SIZE, SIZE);
        setColor(Color.RED);
    }

    public void act() {
        int x = getX();
        int y = getY();

        if (x < game.player.getX()) x += SPEED;
        if (x > game.player.getX()) x -= SPEED;
        if (y < game.player.getY()) y += SPEED;
        if (y > game.player.getY()) y -= SPEED;

        setX(x);
        setY(y);


    }

}
