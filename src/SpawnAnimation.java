import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;

public class SpawnAnimation extends GameObject {
    // set variables
    static boolean spawningAnimation = false;
    PolygonGame game;
    int size;
    public int lifespan;
    public int particleTransparency = 10;
    static Random r = new Random();
    static int playerTransparency = 0;
    private static ArrayList<SpawnAnimation> spawnParticles = new ArrayList<>();

    // dummy constructor for the spawn particles, not used to actually spawn the
    // animation
    public SpawnAnimation(PolygonGame game, int x, int y) {
        this.game = game;
        size = Player.size / 2;
        setSize(size, size);
        setPosition(this, x, y);
        setColor(new Color(0, 0, 255, particleTransparency));
        lifespan = r.nextInt(10) + 10;
    }

    // method to spawn particles
    public void spawnAnimation(PolygonGame game) {
        int w = game.getWindowWidth();
        int h = game.getWindowHeight();

        // Top edge particles
        addParticle(game, 0 - size, 0 - size); // Top Left
        addParticle(game, (w + size) / 2, 0 - size); // Top Middle
        addParticle(game, w + size, 0 - size); // Top Right

        // Middle edge particles
        addParticle(game, 0 - size, (h + size) / 2); // Left Middle
        addParticle(game, w + size, (h + size) / 2); // Right Middle

        // Bottom edge particles
        addParticle(game, 0 - size, h + size); // Bottom Left
        addParticle(game, (w + size) / 2, h + size); // Bottom Middle
        addParticle(game, w + size, h + size); // Bottom Right

        spawningAnimation = true;
    }

    // helper method to add a particle to the game and the list
    private void addParticle(PolygonGame game, int x, int y) {
        SpawnAnimation particle = new SpawnAnimation(game, x, y);
        game.add(particle);
        spawnParticles.add(particle);
    }

    public void act() {
        // optimize
        if (!spawningAnimation) {
            return;
        }
        int speed = (game.getWindowWidth() + game.getWindowHeight()) / 100; // speed of the particles
        // get particle position
        int x = getX();
        int y = getY();
        // get angle to player
        double playerAngle = Math.atan2(Player.y - y, Player.x - x);
        // move towards player
        x += (Math.cos(playerAngle) * speed) + (r.nextInt(speed * 2) - speed);
        y += (Math.sin(playerAngle) * speed) + (r.nextInt(speed * 2) - speed);
        setPosition(this, x, y);
        if (collides(game.player)) { // if collides with player, remove from game and list
            lifespan--;
            if (lifespan <= 0) {
                game.remove(this);
                spawnParticles.remove(this);
                playerTransparency += (int) (255 / 8 - 10); // increase player transparency
                game.player.setColor(new Color(0, 0, 255, playerTransparency));
            }
        }
        if (particleTransparency < 250) { // fade in
            particleTransparency += 5;
            setColor(new Color(0, 0, 255, particleTransparency));
        }
        if (spawnParticles.size() == 0) { // if all particles are gone, stop spawning
            // ensures that all particles are removed from the game
            for (SpawnAnimation m : spawnParticles) {
                game.remove(m);
            }
            spawnParticles.clear();
            // set the player to full color
            game.player.setColor(new Color(0, 0, 255, 255));
            spawningAnimation = false;
        }
    }

}
