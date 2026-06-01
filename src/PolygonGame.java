
/*
 * Contributors: Hugo To, Raymond Tan, Mohammad Sadeghi
 * Start date: 5/19/2026
 * End date: 
 * Project: Polygons - a roguelike top-down shooter.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javax.swing.*;



public class PolygonGame extends Game {

    Random r = new Random();
    Player player;
    Projectile projectile;
    Enemy enemy;
    Glaive glaive0;
    Glaive glaive1;
    Glaive glaive2;
    ChainLightning lightning;
    AtGMissileMk1 atgMissile;
    MainMenu menuController;
    DisplayDebug debug;
    Tutorial tutorialController;
    GameBackground background;
    YonduArrow yonduArrow;
    static boolean gamePause = true;
    static boolean choosingBuff = false;
    ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    public ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    ArrayList<XpOrb> xpOrbs = new ArrayList<XpOrb>();
    ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();
    HashMap<Enemy, Integer> hitEnemies = new HashMap<Enemy, Integer>(); // the enemies that have been hit and the timer
    // for each enemy to be hit again
    ArrayList<MainMenu> removeTheButtons = new ArrayList<MainMenu>();
    int spawnedEnemies = 0;

    public void setup() {
        // changes the game background
        background = new GameBackground(this);
        add(background);
        // creates a dummy constructor for the main menu buttons to use to spawn the buttons
        menuController = new MainMenu(this, "");
        menuController.spawnMyBoxes(this);
        add(menuController);
        // creates dummy consturctor for the tutorial buttons to use to spawn the buttons
        tutorialController = new Tutorial(this, "");
        add(tutorialController);

        setDelay(16); // 60fps
        setTitle("Polygon");
    }

    // spawns player object when method is called, only used in main menu
    public void spawnGame() {
        // creates the player
        player = new Player(this);
        add(player);
   
        // creates debugger
        debug = new DisplayDebug(this);
        add(debug);

    }

    public void act() {

        if (gamePause) {
            return; // pause the game while choosing a buff or on main menu(lock the game)
        }
        if (debug != null) {
            getContentPane().setComponentZOrder(debug, 0);
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

            if ((spawnedEnemies + 1) % 30 == 0) { //hoard spawn
                int enemySpawnSeed = r.nextInt();
                for (int i = 0; i < 10; i++) {
                    enemy = new Enemy(this, 0, 0, enemySpawnSeed);
                    add(enemy);
                    enemies.add(enemy);

                }
                enemySpawnSeed++;
            } else if ((spawnedEnemies + 1) % 50 == 0) { //big boy
                enemy = new Enemy(this, 1, 0, r.nextInt());
                add(enemy);
                enemies.add(enemy);
            } else { //normal enemy
                enemy = new Enemy(this, 0, 0, r.nextInt());
                add(enemy);
                enemies.add(enemy);
            }
        }

        if (Player.health <= 0) { // stops if dies
            stopGame();
        }

        if (Player.xp >= Player.xpReq) { // spawns a powerup when the player reaches level 5
            gamePause = true;
            int centeredY = getWindowHeight() / 2 - getWindowHeight() / 6;
            powerUps.add(new PowerUp(getWindowWidth() / 4 - getWindowWidth() / 10, centeredY, this));
            powerUps.add(new PowerUp(getWindowWidth() / 4 * 2 - getWindowWidth() / 10, centeredY, this));
            powerUps.add(new PowerUp(getWindowWidth() / 4 * 3 - getWindowWidth() / 10, centeredY, this));
            for (PowerUp p : powerUps) {
                add(p);
            }
            Player.xp = 0; // reset score after spawning powerup
            Player.level += 1;
            Player.xpReq = 5 * Player.level * Math.log(Player.level + 1);
            choosingBuff = true;

        }

    }

    public static void main(String[] args) {
        PolygonGame game = new PolygonGame();
        game.setVisible(true);
        game.initComponents();
        ImageIcon icon = new ImageIcon("Images/MainMenu/PolygonLogo.png");
        game.setIconImage(icon.getImage());
    }
}
