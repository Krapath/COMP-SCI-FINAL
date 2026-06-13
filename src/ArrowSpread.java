import java.awt.Color;
import java.util.Random;

/**
 * arrowspread: an ability that fires multiple arrows in a spread pattern
 * simultaneously to cover a wider area.
 */
public class ArrowSpread extends Ability {

	PolygonGame game;

	static String name = "ArrowSpread";
	Player player;
	Color originalColor;
	double speedMult = 4;
	boolean wasInvulnerable = false;
	int originalSpeed;

	static int duration = 5;
	static int cooldown = 60;

	static int arrowCount = 5;
	static int damage = 1;

	Random r = new Random();

	public ArrowSpread(PolygonGame game, Player player) {
		super(name, duration, cooldown);
		this.game = game;
		this.player = player;
		Player.abilities.add(this);
	}

	public void act() {
		super.act();
		performAbility(); // continuously run the ability while it's active, the ability automatically performs when teh cooldown is off

	}

	public void performAbility() {
		if (canUse()) {
			startCooldown();

			// spawns in a radial pattern scaling based on the arrowCount
			for (int i = 0; i < arrowCount; i++) {

			    double angleInRadians = (i * 2 * Math.PI) / arrowCount;
				Arrow arrow = new Arrow(game, angleInRadians);
				game.add(arrow);
				PolygonGame.arrows.add(arrow);
			}

		}
	}

	public void onDurationEnd() {

		PolygonGame.arrows.clear();
	}

}
