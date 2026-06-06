import java.awt.Color;
import java.util.Random;
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

	static int arrowCount = 5;
	static int damage = 1;
	
	Random r = new Random();
	
	
	public ArrowSpread(PolygonGame game, Player player) {
		super(name,duration,cooldown);
		this.game=game;
		this.player=player;
		game.player.abilities.add(this);
	}
	
	public void act(){
		super.act();
		performAbility(); // continuously run the ability while it's active, so that it shoots arrows when ability is active

	}

	public void performAbility(){
		if (canUse()){
			startCooldown();
			
				for(int i = 0; i<arrowCount; i ++){
					
			    double angleInRadians = r.nextInt(361)*Math.PI/180;
			    Arrow arrow = new Arrow(game, angleInRadians); 
			    game.add(arrow); 
				game.arrows.add(arrow);
				}


			
		}
	}
	

	
	
	public void onDurationEnd(){
		
		game.arrows.clear();
	}

}
