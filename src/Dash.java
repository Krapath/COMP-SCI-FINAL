import java.awt.Color;

/**
 * dash: an ability that grants temporary invulnerability and increased movement speed
 * to quickly escape from danger.
 */
public class Dash extends Ability{
	
	
	PolygonGame game;
	
	static String name = "Dash";
	Player player;
	Color originalColor;
	double speedMult = 4;
	boolean wasInvulnerable = false;
	int originalSpeed;
	
	static int duration=5;
	static int cooldown=60;

	public Dash(PolygonGame game, Player player) {
		super(name,duration,cooldown);
		this.game=game;
		this.player=player;
		Player.abilities.add(this);
	}
	
	public void act(){
		super.act();

	}
	
	/*
	 * 
	 * 
	 */
	public void performAbility(){
		if (canUse()){
			startCooldown();
			originalColor = player.getColor();
			originalSpeed = Player.speed;
			Player.speed *= speedMult;
			wasInvulnerable = Player.invulnerable; // save prior state
        	Player.invulnerable = true;

			
		}
	}
	

	
	
	public void onDurationEnd(){
		Player.speed = originalSpeed;

		Player.invulnerable = wasInvulnerable; // restore prior state
		wasInvulnerable = false;
		game.player.setColor(originalColor);
	}

}
