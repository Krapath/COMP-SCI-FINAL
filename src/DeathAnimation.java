
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class DeathAnimation extends GameObject {

    // set variables
    static boolean dyingAnimation = false;
    PolygonGame game;
    int size;
    public int timer = 0;
    public int particleTransparency = 255;
    static Random r = new Random();
    public int duration =30;
    
    static boolean playDeathSound2 = true;
    private static ArrayList<DeathAnimation> dyingParticles = new ArrayList<>();

    // dummy constructor for the death particles, not used to actually spawn the
    // animation
    public DeathAnimation(PolygonGame game, int x, int y) {
        this.game = game;
        size = Player.size / 2;
        setSize(size, size);
        setPosition(this, x, y);
        setColor(new Color(0, 0, 255, particleTransparency));
    }

    // method to spawn particles
    public void deathAnimation(PolygonGame game) {
        game.player.setColor(new Color(0, 0, 255, 0)); // make player invisible
        for (int i = 0; i < 8; i++) {
            int x = (int) (game.player.x + Player.size / 2 + r.nextInt(Player.size + 1) - Player.size / 2);
            int y = (int) (game.player.y + Player.size / 2 + r.nextInt(Player.size + 1) - Player.size / 2);
            // put the dots in a place close to the player, with some randomization
            addParticle(game, x, y);
        }
        dyingAnimation = true;
    }

    // helper method to add a particle to the game and the list
    private void addParticle(PolygonGame game, int x, int y) {
        DeathAnimation particle = new DeathAnimation(game, x, y);
        game.add(particle);
        dyingParticles.add(particle);
    }

    public void act() {

        // optimize
        if (!dyingAnimation) {
            return;
        }
        int speed = Player.size / 5;
        int x = getX();
        int y = getY();
        if (timer < duration) {// make the particles wiggle
            x += Math.ceil(r.nextInt(speed * 2 + 1) - speed);
            y += Math.ceil(r.nextInt(speed * 2 + 1) - speed);
            setPosition(this, x, y);
            timer++;
        } else if (timer < duration*2) {// make the wiggle further

            x += Math.ceil(r.nextInt(speed * 3 + 1) - speed * 1.5);
            y += Math.ceil(r.nextInt(speed * 3 + 1) - speed * 1.5);

            setPosition(this, x, y);
            timer++;
        } else if (timer < (int)(duration*2.16)) { // make the particles fly outwards
            double playerAngle = Math.atan2(game.player.y + Player.size / 2 - y, game.player.x + Player.size / 2 - x);
            x += (Math.cos(playerAngle + Math.PI) * speed * 5);
            y += (Math.sin(playerAngle + Math.PI) * speed * 5);
            setPosition(this, x, y);
            if (playDeathSound2){
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
            timer = 0;
            // set up death screen
            playDeathSound2 = true; // make true again for later if you retry
            game.deathScreenController.youDied();
        }

    }

}
