
import java.awt.Color;

/**
 * an ability that grants temporary invulnerability and increased movement speed
 * to help escape from danger and synergise with certain abilites Author: Hugo
 * To
 */
public class Dash extends Ability {

    PolygonGame game;
    Player player;

    // ability configuration
    static String name = "Dash";
    static int duration = 5;
    static int cooldown = 60;

    // keep variables to restore player state after the dash ends
    Color originalColor;
    double speedMult = 4;
    boolean wasInvulnerable = false;
    int originalSpeed;

    public Dash(PolygonGame game, Player player) {
        super(name, duration, cooldown);
        this.game = game;
        this.player = player;
        Player.abilities.add(this); // registers this ability to the player
    }

    public void act() {
        super.act();
    }

    /*
	 * activates the dash, boosting speed and giving invulnerability
	 * while keeping up the player's original stats.
     */
    public void performAbility() {
        if (canUse()) {
            startCooldown();

            // save the current state before changing anything
            originalColor = player.getColor();
            originalSpeed = Player.speed;
            wasInvulnerable = Player.invulnerable;

            // apply the dash buffs
            Player.speed *= speedMult;
            Player.invulnerable = true;
        }
    }

    public void onDurationEnd() {
        // reset player stats back to how they were before the dash
        Player.speed = originalSpeed;
        Player.invulnerable = wasInvulnerable;
        wasInvulnerable = false;
        game.player.setColor(originalColor);
    }
}
