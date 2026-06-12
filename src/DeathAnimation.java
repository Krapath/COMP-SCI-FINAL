
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class DeathAnimation extends GameObject {
    // array of all objects in the class
    private static ArrayList<DeathAnimation> dyingParticles = new ArrayList<>();
    // set animation variables + objects
    static boolean dyingAnimation = false;
    static boolean playDeathSound2 = true;
    PolygonGame game;
    int size;
    // set particple varialbes
    public int timer = 0;
    public int particleTransparency = 255;
    public int duration = 30;
    // create random object
    Random r = new Random();

    /**
     * Dummy constructor to build death particles
     */
    public DeathAnimation(PolygonGame game, int x, int y) {
        // assign values to object's variables
        this.game = game;
        size = Player.size / 2;
        setSize(size, size);
        setPosition(x, y);
        setColor(new Color(0, 0, 255, particleTransparency));
    }

    /**
     * runs the death animation
     */
    public void deathAnimation(PolygonGame game) {
        // make player invisible
        game.player.setColor(new Color(0, 0, 255, 0));
        // put dots closed to the player with some randomization. Number of dots is
        // based on player level
        for (int i = 0; i < Player.level * 2; i++) {
            int x = (int) (game.player.x + Player.size / 2 + r.nextInt(Player.size + 1) - Player.size / 2);
            int y = (int) (game.player.y + Player.size / 2 + r.nextInt(Player.size + 1) - Player.size / 2);
            addParticle(game, x, y);
        }
        dyingAnimation = true;
    }

    /**
     * helper method to add a particle to the game and the list
     */
    private void addParticle(PolygonGame game, int x, int y) {
        DeathAnimation particle = new DeathAnimation(game, x, y);
        game.add(particle);
        dyingParticles.add(particle);
    }

    public void act() {
        if (!dyingAnimation) {
            return; // if death animation isn't running skip code
        }
        int speed = Player.size / 5;
        int x = getX();
        int y = getY();
        if (timer < duration) { // make the particles wiggle
            x += Math.ceil(r.nextInt(speed * 2 + 1) - speed);
            y += Math.ceil(r.nextInt(speed * 2 + 1) - speed);
            setPosition(x, y);
            timer++;
        } else if (timer < duration * 2) { // make the wiggle further
            x += Math.ceil(r.nextInt(speed * 3 + 1) - speed * 1.5);
            y += Math.ceil(r.nextInt(speed * 3 + 1) - speed * 1.5);
            setPosition(x, y);
            timer++;
        } else if (timer < (int) (duration * 2.16)) { // make the particles fly outwards
            double playerAngle = Math.atan2(game.player.y + Player.size / 2 - y, game.player.x + Player.size / 2 - x);
            x += (Math.cos(playerAngle + Math.PI) * speed * 5);
            y += (Math.sin(playerAngle + Math.PI) * speed * 5);
            setPosition(x, y);
            if (playDeathSound2) { // play death sound
                SoundEffects.play("SFX/DEATH_SOUND2.wav", 5.0f);
                playDeathSound2 = false;
            }
            timer++;
        } else if (particleTransparency > 20) { // make the particles fade out
            this.setColor(new Color(0, 0, 255, particleTransparency));
            particleTransparency -= 7;
        } else { // remove the particles after the animation is done
            for (DeathAnimation p : dyingParticles) {
                game.remove(p);
            }
            dyingParticles.clear();
            dyingAnimation = false;
            // reset the death screen for next time
            playDeathSound2 = true;
            timer = 0;
            // open the death menu
            game.deathScreenController.youDied();
        }
    }
}
