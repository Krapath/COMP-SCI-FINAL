import java.awt.*;
import javax.swing.*;

public class Ability {
    protected int maxDuration;
    protected int maxCooldown;
    
    protected String name;
    // active tickers counts down each tick
    protected int durationTimer = 0;
    protected int cooldownTimer = 0;
    protected boolean isActive = false;
    
	public Ability(String name, int maxDur, int maxCool) {
		
		maxDuration=maxDur;
		maxCooldown=maxCool;
		this.name = name;
	}
	
    public void startCooldown() {
        this.isActive = true;
        this.durationTimer = maxDuration;  // reset duration
        this.cooldownTimer = maxCooldown;  // reset cooldown
    }


	public boolean canUse(){
        // can't use if on cooldown, already active, or game is paused
		return (cooldownTimer == 0 &&!isActive && !PolygonGame.gamePause);
	}
	
	public void performAbility(){
        if (canUse()){
            startCooldown();
        }
    }


	public void act(){
        if (cooldownTimer > 0) {
            cooldownTimer--;  // tick down cooldown
        }

        if (isActive) {
            durationTimer--;
            if (durationTimer <= 0) {
                isActive = false;
                onDurationEnd();  // notify subclass
            }
		}
	}
	
    public void onDurationEnd() {
    	// override in subclass
    }
}