
/**
 * ability that fires multiple arrows in a spread pattern simultaneously
 * Author: Hugo To;
 */
public class ArrowSpread extends Ability {

    PolygonGame game;

    Player player;

    // ability config
    static String name = "ArrowSpread";
    static int duration = 5;
    static int cooldown = 60;

    // arrow stats
    static int arrowCount = 5;
    static int damage = 1;

    public ArrowSpread(PolygonGame game, Player player) {
        super(name, duration, cooldown);
        this.game = game;
        this.player = player;
        Player.abilities.add(this);
    }

    public void act() {
        super.act();
        // continuously run the ability while it's active, the ability automatically
        // performs when the cooldown is off
        performAbility();

    }

    /**
     * creates a number of arrows based on arrowCount in a radial pattern around
     * player
     */
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
        // removes the spawned arrows when the ability time runs out
        PolygonGame.arrows.clear();
    }

}
