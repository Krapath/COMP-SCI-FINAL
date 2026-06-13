import java.awt.*;
import javax.swing.*;

/**
 * timed ability with duration and cooldown.
 * Author: Hugo To
 */
public class Ability {
    // maximum duration ticks for the ability.
    protected int maxDuration;
    // maximum cooldown ticks for the ability.
    protected int maxCooldown;

    protected String name;
    // active tickers counts down each tick
    protected int durationTimer = 0;
    /// remaining cooldown ticks before ability can be reused.
    protected int cooldownTimer = 0;
    // whether the ability is currently active.
    protected boolean isActive = false;

    /**
     * create an ability with a name, duration and cooldown.
     */
    public Ability(String name, int maxDur, int maxCool) {
        maxDuration = maxDur;
        maxCooldown = maxCool;
        this.name = name;
    }

    /**
     * activate the ability and reset timers.
     */
    public void startCooldown() {
        this.isActive = true;
        this.durationTimer = maxDuration; // reset duration
        this.cooldownTimer = maxCooldown; // reset cooldown
    }

    /**
     * return true if the ability is ready to be used.
     */
    public boolean canUse() {
        // can't use if on cooldown, already active, or game is paused
        return (cooldownTimer == 0 && !isActive && !PolygonGame.gamePause);
    }

    /**
     * trigger the ability if it can be used.
     */
    public void performAbility() {
        if (canUse()) {
            startCooldown();
        }
    }

    /**
     * tick down duration and cooldown timers each frame.
     */
    public void act() {
        if (cooldownTimer > 0) {
            cooldownTimer--; // tick down cooldown
        }

        if (isActive) {
            durationTimer--;
            if (durationTimer <= 0) {
                isActive = false;
                onDurationEnd(); // notify subclass
            }
        }
    }

    /**
     * called when ability duration ends
     */
    public void onDurationEnd() {

    }
}