
/*
 * Contributors: Hugo To, Raymond Tan, Mohammad Sadeghi
 * Start date: 5/19/2026
 * End date: 
 * Project: Polygons - a roguelike top-down shooter.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JLayeredPane;

public class Polygon extends Game {

    Random r = new Random();
    Player player;
    Projectile projectile;
    Enemy enemy;
    Glaive glaive;
    ChainLightning lightning;
    AtGMissileMk1 atgMissile;
    MainMenu menuController;
    GameBackground background;
    YonduArrow yonduArrow;
    public Methods method;
    static boolean gamePause = true;
    ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    public ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    ArrayList<XpOrb> xpOrbs = new ArrayList<XpOrb>();
    ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();
    HashMap<Enemy, Integer> hitEnemies = new HashMap<Enemy, Integer>(); // the enemies that have been hit and the timer for each enemy to be hit again
    ArrayList<MainMenu> removeTheButtons = new ArrayList<MainMenu>();


    public void setup() {
        //changes the game background
        background = new GameBackground();
        background.changeBackground(this, "Images/Background/BackgroundFrameOne.png");
        add(background);
        //creates the player
        player = new Player(this);
        player.setLocation(335, 225); // middle
       add(player);
        //just a dummy object to hold the main menu background image and spawn the buttons, since the main menu is basically just a different "game state" of the same game rather than a separate class
        menuController = new MainMenu(this, "Images\\MainMenu\\MainMenuBackground.png","dummy");
        menuController.spawnMyBoxes(this);
        add(menuController);
        //creates methods helper used by projectiles and other game logic
        method = new Methods(this);
        //creates debugger
        add(new DisplayDebug(this));
        //creates glaive
        glaive = new Glaive(this);
        add(glaive);
        //creates yondu arrow
        yonduArrow = new YonduArrow(this);
        add(yonduArrow);
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
