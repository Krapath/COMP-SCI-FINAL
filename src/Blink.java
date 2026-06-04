/*import java.awt.Color;

public class Blink extends Ability {

	Player player;
	Color originalColor;
	double speedMult = 4;
	boolean wasInvulneranble = false;
	int originalSpeed;
	
	static int duration=5;
	static int cooldown=60;

	public Blink(PolygonGame game, Player player) {
		super(game, duration, cooldown);
		this.player = player;

	}

	
	public void act() {
		super.act();

		if (PolygonGame.gamePause) {
			return;
		}

		if (game.spaceBarKeyPressed() && canUse()) {
			startCooldown();
			originalColor = Color.BLUE;
			originalSpeed = Player.speed;
			Player.speed = (int) (Player.speed * speedMult);
			if (!Player.invulnerable) {
				Player.invulnerable = true;
			}else if (Player.invulnerable){
			  wasInvulneranble = true;
			}
		}

	}

	public void onDurationEnd() {
		Player.speed = originalSpeed;

		if (!wasInvulneranble){
		Player.invulnerable = false;
		}
		wasInvulneranble =false;
		game.player.setColor(originalColor);

		

	}
}
*/