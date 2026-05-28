
/*
 * Contributors: Hugo To, Raymond Tan, Mohammad Sadeghi
 * Start date: 5/19/2026
 * End date: 
 * Project: Polygons - a roguelike top-down shooter.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Polygon extends Game {

    Random r = new Random();
    Player player;
    Projectile projectile;
    Enemy enemy;
    Glaive glaive;
    ChainLightning lightning;
    AtGMissileMk1 atgMissile;
    MainMenu menuController;
    public boolean gamePause = true;
    ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    public ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    ArrayList<XpOrb> xpOrbs = new ArrayList<XpOrb>();
    ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();
    HashMap<Enemy, Integer> hitEnemies = new HashMap<Enemy, Integer>(); // the enemies that have been hit and the timer for each enemy to be hit again
    ArrayList<MainMenu> removeTheButtons = new ArrayList<MainMenu>();


    public void setup() {

        //just a dummy object to hold the main menu background image and spawn the buttons, since the main menu is basically just a different "game state" of the same game rather than a separate class
        menuController = new MainMenu(this, "Images\\MainMenu\\MainMenuBackground.png","dummy");
        menuController.spawnMyBoxes(this);
        add(menuController);
        player = new Player(this);
        player.setLocation(335, 225); // middle
        add(player);
        player.setVisible(false);//kind of a bad way to do this, I want player to be gone while menu is open but wtvr

        add(new DisplayDebug(this));
        glaive = new Glaive(this);
        add(glaive);
        glaive.setVisible(false);//kind of a bad way to do this, I want player to be gone while menu is open but wtvr

        setDelay(16); // 60fps
        setTitle("Polygon");
    }

    public void act() {
        
        if (gamePause) {
            return; // pause the game while choosing a buff or on main menu(lock the game)
        }
        if (mouseLeftPressed()) {
            if (Player.attackDelay > 10) {
                Player.attackDelay = 0;
                projectile = new Projectile(this);
                add(projectile);
                projectiles.add(projectile);
            }
        }


        if (r.nextInt(300) < 20) { // 0.33% chance each tick to spawn an enemy
            enemy = new Enemy(this);
            add(enemy);
            enemies.add(enemy);
        }
        if (Player.health <= 0) { // stops if dies
            stopGame();
        }

        if (Player.score >= 5) { // spawns a powerup when the player reaches level 5
            gamePause = true;
            int centeredY = getWindowHeight() / 2 - getWindowHeight() / 6;
            powerUps.add(new PowerUp(getWindowWidth() / 4 - getWindowWidth() / 10, centeredY, this));
            powerUps.add(new PowerUp(getWindowWidth() / 4 * 2 - getWindowWidth() / 10, centeredY, this));
            powerUps.add(new PowerUp(getWindowWidth() / 4 * 3 - getWindowWidth() / 10, centeredY, this));
            for (PowerUp p : powerUps) {
                add(p);
            }
            Player.score = 0; // reset score after spawning powerup

        }

    }

    public static void main(String[] args) {
        Polygon game = new Polygon();
        game.setVisible(true);
        game.initComponents();
    }
}
