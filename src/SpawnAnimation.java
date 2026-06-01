import java.util.ArrayList;
import java.util.Random;

public class SpawnAnimation extends GameObject {
    // set variables
    boolean spawning = false;
    PolygonGame game;
    int size;
    private static ArrayList<SpawnAnimation> spawnParticles = new ArrayList<>();

    public SpawnAnimation(PolygonGame game, int x, int y) {
        this.game = game;
        size = (int) ((game.getWindowWidth() + game.getWindowHeight()) / 100);// THIS CODE HIGHLY NEEDS TO BE TWEAKED IF
                                                                              // PLAYER SZIE CHANGED
        setSize(size, size);
    }

    public void spawnAnimation(PolygonGame game) {
        // add particle in top left
        SpawnAnimation topLeft = new SpawnAnimation(game, 0 - size, 0 - size);
        game.add(topLeft);
        spawnParticles.add(topLeft);
        // add particle in top middle
        SpawnAnimation topMiddle = new SpawnAnimation(game, (game.getWindowWidth() + size) / 2, 0 - size);
        game.add(topMiddle);
        spawnParticles.add(topMiddle);
        // add particle in top right
        SpawnAnimation topRight = new SpawnAnimation(game, game.getWindowWidth() + size, 0 - size);
        game.add(topRight);
        spawnParticles.add(topRight);
        spawnParticles.add(topLeft);
        // add particle in left middle
        SpawnAnimation leftMiddle = new SpawnAnimation(game, 0 - size, (game.getWindowHeight() + size) / 2);
        game.add(leftMiddle);
        spawnParticles.add(leftMiddle);
        // add particle in right middle
        SpawnAnimation rightMiddle = new SpawnAnimation(game, game.getWindowWidth() + size,
        (game.getWindowHeight() + size) / 2);
        game.add(rightMiddle);
        spawnParticles.add(rightMiddle);
        // add particle in bottom left
        SpawnAnimation bottomLeft = new SpawnAnimation(game, 0 - size, game.getWindowHeight() + size);
        game.add(bottomLeft);
        spawnParticles.add(bottomLeft);
        // add particle in bottom middle
        SpawnAnimation bottomMiddle = new SpawnAnimation(game, (game.getWindowWidth() + size) / 2,
                game.getWindowHeight() + size);
        game.add(bottomMiddle);
        spawnParticles.add(bottomMiddle);
        // add particle in bottom right
        SpawnAnimation bottomRight = new SpawnAnimation(game, game.getWindowWidth() + size,
                game.getWindowHeight() + size);
        game.add(bottomRight);
        spawnParticles.add(bottomRight);
        // toggle animation on
        spawning = true;
        // end goal
        SpawnAnimation endGoal = new SpawnAnimation(game, game.getWindowWidth() / 2, game.getWindowHeight() / 2);
        endGoal.setSize(0, 0);
        game.add(endGoal);
        spawnParticles.add(endGoal);
    }

    public void act() {
        // optimize
        if (!spawning) {
            return;
        }
        Random r = new Random();
        int speed = (game.getWindowWidth() + game.getWindowHeight()) / 100; // speed of the particles
            //get particle position
            int x = getX();
            int y = getY();
            //get angle to player
            double playerAngle = Math.atan2(Player.y - y, Player.x - x);
            //move towards player
            x += (Math.cos(playerAngle) * speed) + (r.nextInt(speed*2) - speed);
            y += (Math.sin(playerAngle) * speed) + (r.nextInt(speed*2) - speed);
            setPosition(this, x, y);
    }

}
