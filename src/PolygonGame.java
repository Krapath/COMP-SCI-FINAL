
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
    DeathAnimation deathAnimationController;
    DisplayDebug debug;
    Tutorial tutorialController;
    DeathScreen deathScreenController;
    GameBackground background;
    YonduArrow yonduArrow;
    //TODO: fix Blink blink;
    static boolean gamePause = true;
    static boolean choosingBuff = false;
    static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    static public ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    static ArrayList<XpOrb> xpOrbs = new ArrayList<XpOrb>();
    static ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();
    static ArrayList<Arrow> arrows = new ArrayList<Arrow>();

    HashMap<Enemy, Integer> hitEnemies = new HashMap<Enemy, Integer>(); //TODO: Change the enemies that have been hit and the timer
    // for each enemy to be hit again
    ArrayList<MainMenu> removeTheButtons = new ArrayList<MainMenu>();
    public int spawnedEnemies = 0;
    public int maxEnemiesSpawned = 75;
    public int enemySpawnSeed;

    public void setup() {
        // changes the game background
        background = new GameBackground(this);
        add(background);
        // creates a dummy constructor for the main menu buttons to use to spawn the buttons
        menuController = new MainMenu(this, "");
        menuController.spawnMyBoxes(this);
        // creates dummy consturctor for the tutorial buttons to use to spawn the buttons
        tutorialController = new Tutorial(this, "", 0, 0,0,0);
        //create dummy constructor for the death screen buttons to use to spawn the buttons
        deathScreenController = new DeathScreen(this, "",false, 0, 0,0,0,null);
        deathAnimationController = new DeathAnimation(this, 0, 0);

        setDelay(16); // 60fps
        setTitle("Polygon");
    }

    // spawns player object when method is called, only used in main menu
    public void spawnGame() {
        // creates the player
        player = new Player(this);
        add(player);

        
        ArrowSpread arrowSpread = new ArrowSpread(this,player);
        // creates debugger
        debug = new DisplayDebug(this);
        add(debug);

        //start animation
        SpawnAnimation spawnDummy = new SpawnAnimation(this, 0,0);
        spawnDummy.spawnAnimation(this);
        this.add(spawnDummy);

    }

    public void act() {

        if (gamePause) {
            return; // pause the game while choosing a buff or on main menu(lock the game)
        }
        
        for (int i = 0; i<Player.abilities.size();i++){
        	Player.abilities.get(i).act();

            if (spaceBarKeyPressed() && Player.abilities.get(i).name.equals("Dash")){
                Player.abilities.get(i).performAbility();
            }

        }
  
        
        // index 0 makes sure that debug is always on top of every other object
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

        if (r.nextInt(300) < 20 && enemies.size() < maxEnemiesSpawned) { // 0.33% chance each tick to spawn an enemy

            if ((spawnedEnemies + 1) % 50 == 0) { //hoard spawn
                enemySpawnSeed = r.nextInt();

                if (r.nextInt(2) == 0) {
                    for (int i = 0; i < 10; i++) {
                        enemy = new Enemy(this, 0, 0, enemySpawnSeed);
                        add(enemy);
                        enemies.add(enemy);
                    }
                } else {
                    System.out.println("trying to spawn");
                    for (int i = 0; i < 10; i++) {
                        enemy = new Enemy(this, 4, 0, enemySpawnSeed);
                        add(enemy);
                        enemies.add(enemy);
                    }
                }

                enemySpawnSeed++;
            } else if ((spawnedEnemies + 1) % 99 == 0) { //big boy
                enemy = new Enemy(this, 1, 0, r.nextInt());
                add(enemy);
                enemies.add(enemy);
            } else if ((spawnedEnemies + 1) % 10 == 0 && spawnedEnemies > 0) {

                if (r.nextInt(2) == 0) {
                    enemy = new Enemy(this, 2, 0, r.nextInt());
                } else {
                    enemy = new Enemy(this, 3, 0, r.nextInt());
                }

                add(enemy);
                enemies.add(enemy);
            } else { //normal enemy
                enemy = new Enemy(this, 0, 0, r.nextInt());
                add(enemy);
                enemies.add(enemy);
            }

            spawnedEnemies++;
        }

        if (Player.health <= 0 && this.player != null) { // stops if dies
            gamePause = true;
            deathAnimationController.deathAnimation(this);
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
            Player.xpReq = 0;

            //Player.xpReq = 5 * Player.level * Math.log(Player.level + 1);
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
