
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class SpawnAnimation extends GameObject {
    // array list holding all the objects
    private static ArrayList<SpawnAnimation> spawnParticles = new ArrayList<>();
    // set variables for dummy constructor
    static boolean spawningAnimation = false;
    PolygonGame game;
    int size;
    // intialize object variables
    public int lifespan;
    public int particleTransparency = 10;
    public static int playerTransparency = 0;
    static boolean playSpawnSound = true;

    // make random object
    static Random r = new Random();

    /**
     * create dummy constructor for spawn particles
     */
    public SpawnAnimation(PolygonGame game, int x, int y) {
        // set value for object variables
        this.game = game;
        size = Player.size / 2;
        setSize(size, size);
        setPosition(x, y);
        setColor(new Color(0, 0, 255, particleTransparency));
        lifespan = r.nextInt(10) + 10;
        spawnParticles.add(this); // extra cube
    }

    /**
     * the method used to spawn the particles for the cube spawning animation
     */
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

        // play spawning sound effect
        if (playSpawnSound) {
            SoundEffects("SFX/SPAWN_SOUND.wav", -4.0f);
            playSpawnSound = false;
        }
        spawningAnimation = true;
    }

    /**
     * helper method to add a particle to the game and the list
     */
    private void addParticle(PolygonGame game, int x, int y) {
        SpawnAnimation particle = new SpawnAnimation(game, x, y);
        game.add(particle);
        spawnParticles.add(particle);

    }

    public void act() {
        if (!spawningAnimation) {
            return; // skips over all the code if not during the spawning animation
        }
        int speed = (game.getWindowWidth() + game.getWindowHeight()) / 100; // speed of the particles
        // get particle position
        int x = getX();
        int y = getY();
        // get angle to player
        double playerAngle = Math.atan2(game.player.y + Player.size / 2 - y, game.player.x + Player.size / 2 - x);
        // move towards player
        x += (Math.cos(playerAngle) * speed) + (r.nextInt(speed * 2) - speed);
        y += (Math.sin(playerAngle) * speed) + (r.nextInt(speed * 2) - speed);
        setPosition(x, y);
        if (collides(game.player)) { // if collides with player reduce lifespan
            lifespan--;
            if (lifespan <= 0) {
                game.remove(this); // if lifespan hits zero remove particle
                spawnParticles.remove(this);
                playerTransparency += (int) (255 / 8 - 10); // increase player transparency
                game.player.setColor(new Color(0, 0, 255, playerTransparency));// Pretty sure the code bricks here if
                                                                               // the player is shot while spawning
            }
        }
        if (particleTransparency < 250) { // make particles fade it
            particleTransparency += 15;
            setColor(new Color(0, 0, 255, particleTransparency));
        }

        if (spawnParticles.size() <= 1) {
            // ensures that all particles are removed from the game
            for (SpawnAnimation m : spawnParticles) {
                game.remove(m);
            }
            spawnParticles.clear();
            game.remove(this);
            // set the player to full color
            game.player.setColor(new Color(0, 0, 255, 255));
            // turn off the spawning animation
            spawningAnimation = false;
        }
    }

}
