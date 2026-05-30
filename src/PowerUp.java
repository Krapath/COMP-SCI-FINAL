import java.util.Random;
import java.awt.Color;
import java.awt.Graphics;

@SuppressWarnings("unused")

//TODO: maybe just use paint 
public class PowerUp extends GameObject {
    Random r = new Random();
    Polygon game;
    public int buffType;
    public boolean readyToApply = false; // whether the buff should be applied on mouse release
    public boolean wasPressed = false; // whether the mouse was pressed while hovering over this powerup (used to prevent applying the buff if the player clicks on a powerup and then drags the mouse away before releasing)

    public PowerUp(int x, int y, Polygon game) {

        this.game = game;
        setSize(game.getWindowWidth() / 5, game.getWindowHeight() / 3);
        setColor(Color.BLUE);
        setLocation(x, y);

        switch (r.nextInt(4)) {
            case 0:
                setColor(Color.RED);
                buffType = 0; // health buff
                break;
            case 1:
                setColor(Color.GREEN);
                buffType = 1; // speed buff
                break;
            case 2:
                setColor(Color.BLUE);
                buffType = 2; // attack speed buff
                break;
            case 3:
                setColor(Color.YELLOW);
                buffType = 3; // size buff
                break;
        }

    }

    public void act() {
        if (!Polygon.gamePause)
            return; // powerups only work while the player is choosing a buff (optimize)

        int x = game.getMouseX();
        int y = game.getMouseY();
        if (!game.mouseLeftPressed()) {
            readyToApply = true; // the buff should be applied on mouse release if the mouse was pressed while hovering over this powerup
        }
        if (game.mouseLeftPressed() && contains(x, y) && readyToApply) {
            wasPressed = true;
        }
        //apply the buff and remove the powerups if the player clicks on a powerup and releases the mouse button while still hovering over the same powerup
        if (wasPressed && !game.mouseLeftPressed() && contains(x, y) && readyToApply) {  // TODO: turn into a method in game class for other application besides powerups (ex. clicking a "start game" button on the main menu)
            if (buffType == 0)
                Player.health += 5;
            else if (buffType == 1)
                Player.speed += 1;
            else if (buffType == 2)
                Player.attackDelay += 5;
            else if (buffType == 3)
                Projectile.chainLightningActive = true; // set chain lightning active for all projectiles, will be reset to false at the end of the next time the player chooses a buff

            for (int i = 0; i < game.powerUps.size(); i++) { // remove all powerups from the game
                game.remove(game.powerUps.get(i));
            }

            game.powerUps.clear(); // clear the list of powerups
            Polygon.gamePause = false; // allow the game to continue
            readyToApply = false; // reset readyToApply for the next time the player chooses a buff
            wasPressed = false; // reset wasPressed for the next time the player chooses a buff

        }

        if (!game.mouseLeftPressed()) {
            wasPressed = false;
        }
    }
}
