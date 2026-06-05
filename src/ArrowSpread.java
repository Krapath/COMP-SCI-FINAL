import java.awt.Color;

public class ArrowSpread extends Ability{
	
	
	PolygonGame game;
	
	static String name = "ArrowSpread";
	Player player;
	Color originalColor;
	double speedMult = 4;
	boolean wasInvulnerable = false;
	int originalSpeed;
	
	static int duration=5;
	static int cooldown=60;
	
	
	public ArrowSpread(PolygonGame game, Player player) {
		super(name,duration,cooldown);
		this.game=game;
		this.player=player;
		game.player.abilities.add(this);
	}
	
	public void act(){
		super.act();
		performAbility();

	}

	public void performAbility(){
		if (canUse()){
			startCooldown();
			
			    double angleInRadians = Math.PI/2.0;
			    Arrow arrow = new Arrow(game, angleInRadians); 
			    game.add(arrow); 



			
		}
	}
	

	
	
	public void onDurationEnd(){
		
		game.arrows.clear();
	}

}
