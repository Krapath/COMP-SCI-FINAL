/*import java.awt.Graphics;

public class Ability extends GameObject {

    protected PolygonGame game;
    
    protected int maxDuration;
    protected int maxCooldown;
    
    // Active tickers
    protected int durationTimer = 0;
    protected int cooldownTimer = 0;
    protected boolean isActive = false;

    public Ability(PolygonGame game, int maxDuration, int maxCooldown) {
        this.game = game;
        this.maxDuration = maxDuration;
        this.maxCooldown = maxCooldown;
    }

    public boolean canUse() {
        return cooldownTimer == 0 && !isActive;
    }

    public void startCooldown() {
        this.isActive = true;
        this.durationTimer = maxDuration;
        this.cooldownTimer = maxCooldown;
    }

    
    public void act() {
        if (PolygonGame.gamePause) {
            return;
        }

        // Tick down cooldown
        if (cooldownTimer > 0) {
            cooldownTimer--;
        }

        // Tick down active duration
        if (isActive) {
            durationTimer--;
            if (durationTimer <= 0) {
                isActive = false;
                onDurationEnd();
            }
        }
    }

    public void onDurationEnd() {
    }

    public void paint(Graphics g) {
    }
}

*/