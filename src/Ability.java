
import java.awt.*;
import javax.swing.*;


public class Ability {
    protected int maxDuration;
    protected int maxCooldown;
    
    protected String name;
    // Active tickers
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
        this.durationTimer = maxDuration;
        this.cooldownTimer = maxCooldown;
    }


	public boolean canUse(){
		return (cooldownTimer == 0 &&!isActive && !PolygonGame.gamePause);
	}
	



	public void performAbility(){
        if (canUse()){
            startCooldown();
        }
    }

	public void act(){
        if (cooldownTimer > 0) {
            cooldownTimer--;
        }

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
    


}
