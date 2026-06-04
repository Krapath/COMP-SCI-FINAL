
import java.awt.*;
import javax.swing.*;


public class AbilityReal {
    protected int maxDuration;
    protected int maxCooldown;
    
    protected String abilityType;
    // Active tickers
    protected int durationTimer = 0;
    protected int cooldownTimer = 0;
    protected boolean isActive = false;
    
	public AbilityReal(String type, int maxDur, int maxCool) {
		
		maxDuration=maxDur;
		maxCooldown=maxCool;
		abilityType = type;
	}
	
	public boolean canUse(){
		return (cooldownTimer == 0 &&!isActive);
	}
	
	
	public void doingThings(){
		
	}
	
	public void act(){
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
    


}
