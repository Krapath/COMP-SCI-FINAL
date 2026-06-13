
/*
 * Contributors: Hugo To, Raymond Tan, Mohammad Sadeghi
 * Start date: 5/19/2026
 * End date: 
 * Project: PolygonGame - a roguelike top-down shooter.
 */

// Sources
// 2d line graphics: https://stackoverflow.com/questions/16995308
/* Hexagon background 
 * https://stackoverflow.com/questions/49262773/creating-a-hexagonal-grid-pattern
 * https://drive.google.com/file/d/1hu-CrhEEepS_7jTaFt0i4S8nrw0vqM9J/view?usp=sharing
 * https://docs.oracle.com/javase/8/docs/api/java/awt/Polygon.html
 */

/* Text wiggle, image rotation, (arrow, missile, match stick)
 * https://math.stackexchange.com/questions/190111/how-to-check-if-a-point-is-inside-a-rectangle
 * https://docs.oracle.com/javase/8/docs/api/java/awt/geom/AffineTransform.html
 */

// circle calculation: https://math.libretexts.org/Bookshelves/Precalculus/Book:_Precalculus__An_Investigation_of_Functions_(Lippman_and_Rasmussen)/05:_Trigonometric_Functions_of_Angles/5.03:_Points_on_Circles_Using_Sine_and_Cosine
// text file: https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/io/File.html

/* Audio:
 * https://kenney.nl/assets/interface-sounds
 * //stackoverflow.com/questions/953598/audio-volume-control-increase-or-decrease-in-java
 * https://opengameart.org/art-search?keys=8bit
 * https://pixabay.com/sound-effects/search/8-bit/?pagi=7
 * https://stackoverflow.com/questions/2792977/do-i-need-to-close-an-audio-clip
 */

 /* Inspiration for game
  * https://store.steampowered.com/app/632360/Risk_of_Rain_2/
  * https://store.steampowered.com/app/1794680/Vampire_Survivors/
  * https://store.steampowered.com/app/3405340/Megabonk/
  */

 
/* Additional sources without explicit usages
 * https://stackoverflow.com/questions/54344597/pass-any-class-as-a-parameter-for-a-method
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
    Glaive glaive;
    ChainLightning lightning;
    AtGMissileMk1 atgMissile;
    MainMenu menuController;
    DeathAnimation deathAnimationController;
    DisplayGUI debug;
    Tutorial tutorialController;
    Highscores highscoresController;
    DeathScreen deathScreenController;
    GameBackground background;
    MatchStick matchStick;
    static boolean gamePause = true;
    static boolean choosingBuff = false;
    static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    static public ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    static ArrayList<XpOrb> xpOrbs = new ArrayList<XpOrb>();
    static ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();
    static ArrayList<Arrow> arrows = new ArrayList<Arrow>();
    static ArrayList<Glaive> glaives = new ArrayList<Glaive>();
    public int numberOfGlaives;
    public int killCounter;
    boolean minibossSpawned = false;

    HashMap<Enemy, Integer> hitEnemies = new HashMap<Enemy, Integer>(); 
    // for each enemy to be hit again
    ArrayList<MainMenu> removeTheButtons = new ArrayList<MainMenu>();
    public int maxEnemiesSpawned = 200;
    public int enemySpawnSeed;

    public void setup() {
        // changes the game background
        background = new GameBackground(this);
        add(background);
        // creates a dummy constructor for the main menu buttons to use to spawn the buttons
        menuController = new MainMenu(this, "");
        menuController.spawnMyBoxes(this);
        // creates dummy consturctor for the tutorial buttons to use to spawn the buttons
        tutorialController = new Tutorial(this, "", 0, 0, 0, 0, null);
        // creates dummy consturctor for the highscores buttons to use to spawn the buttons
        highscoresController = new Highscores(this, "", 0, 0, 0, 0);
        //create dummy constructor for the death screen buttons to use to spawn the buttons
        deathScreenController = new DeathScreen(this, "", false, 0, 0, 0, 0, null);
        deathAnimationController = new DeathAnimation(this, 0, 0);
        highscoresController.createFile(); //creates a highscores file if there isnt one

        highscoresController.scanScores();//fills up the dummy constructor with the current highscores

        setDelay(16); // 60fps
        setTitle("Polygon");
    }

    // spawns player object when method is called, only used in main menu
    public void spawnGame() {
        // creates the player
        player = new Player(this);
        add(player);

        // creates /GUI
        debug = new DisplayGUI(this);
        add(debug);

        //start animation
        SpawnAnimation spawnDummy = new SpawnAnimation(this, 0, 0);
        spawnDummy.spawnAnimation(this);
        this.add(spawnDummy);
    }

    public void act() {

        if (gamePause) {
            return; // pause the game while choosing a buff or on main menu(lock the game)
        }

        if (enemies.size() < maxEnemiesSpawned) //spawns enemies when less than maximum
        {
            enemySpawning();
        }

        for (int i = 0; i < Player.abilities.size(); i++) {
            Player.abilities.get(i).act();

            if (spaceBarKeyPressed() && Player.abilities.get(i).name.equals("Dash")) {
                Player.abilities.get(i).performAbility();
            }

        }

        // index 0 makes sure that debug is always on top of every other object
        if (debug != null) {
            getContentPane().setComponentZOrder(debug, 0);
        }
        if (mouseLeftPressed()) {
            if (Player.attackTimer > Player.attackCooldown) {
                Player.attackTimer = 0;
                projectile = new Projectile(this);
                add(projectile);
                projectiles.add(projectile);
                DisplayGUI.borderWidth++;
            }
        }

        if (Player.health <= 0 && this.player != null) { // stops if dies
            SoundEffects.play("SFX/DEATH_SOUND1.wav", 1.0f);
            highscoresController.orderScores(Player.level, killCounter);
            highscoresController.writeScores(); //saves the highscores
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
            Player.xpReq = Math.round(Math.floor(2 * Player.level * Math.log(Player.level + 1)));
            // 0; // debug
            Player.health = Math.min((int)Math.ceil(0.1*Player.maxHealth+Player.health),Player.maxHealth);
            choosingBuff = true;

        }

        checkDeath();

    }

    public static void main(String[] args) {
        PolygonGame game = new PolygonGame();
        game.setVisible(true);
        game.initComponents();
        ImageIcon icon = new ImageIcon("Images/MainMenu/PolygonLogo.png");
        game.setIconImage(icon.getImage());
    }

    /**
     * checks all enemies to see which ones have no health and should be
     * removed, then spawns exp.
     *
     * pre: enemies arraylist is filled with all enemies in the game post: all
     * enemies with 0 or less health or removed from the game and enemies
     * arraylist, xp orb spawns below killed enemies.
     */
    public void checkDeath() {
        for (int i = 0; i < enemies.size(); i++) {
            Enemy other = enemies.get(i);
            if (other.health <= 0) {
                killCounter++;
                XpOrb xp;

                // Create an XP orb at the location of the defeated enemy
                for (int j = 0; j < other.xpDrop; j++) {
                    xp = new XpOrb((int) other.x, (int) other.y, other.size, this);
                    this.add(xp);          // Add the xp orb to the game
                    xpOrbs.add(xp);   // Add the xp orb to the list

                }

                this.remove(other); // Remove enemy if health is depleted
                enemies.remove(other); // Remove enemy from the list
                i--;
            }
        }

    }

    /**
     * creates a set number of glaives orbiting at equidistant spacing around
     * the player pre: existence of class Glaive and arraylist glaives post: old
     * glaives removed from game and galives arralist, inputted number of
     * glaives created around the player and put into game and glaives arraylist
     *
     */
    public void createGlaive(int numberOfGlaives) {
        for (Glaive g : glaives) {
            this.remove(g);
        }
        glaives.removeAll(glaives);
        for (int i = 0; i < numberOfGlaives; i++) {
            glaive = new Glaive(this, i * 2 * Math.PI / numberOfGlaives);
            glaives.add(glaive);
            this.add(glaive);
        }
    }

    /**
     * spawns the enemies for the game based on an algorithm pre: ecistence of
     * int enemySpawnSeed, int maxEnemiesSpawned, arraylist enemies, object
     * enemy and class Enemy post: creates a certain number and types of enemies
     * based on the algorithmn
     */
    public void enemySpawning() {
    	//spawns normal enemy, becoming less likely as more enemies die, min 20/10000 chance
        if (r.nextInt(10000) < Math.max(300 - 2 * killCounter, 10)) { 
            enemy = new Enemy(this, 0, 0, r.nextInt());
            add(enemy);
            enemies.add(enemy);
        }
        
      //spawn big boy every 80 enemy killed
        if (!minibossSpawned && (killCounter + 1) % 50 == 0) { 
            minibossSpawned = true;
            enemy = new Enemy(this, 1, 0, r.nextInt());
            add(enemy);
            enemies.add(enemy);
        } else if ((killCounter + 1) % 50 != 0) { //prevents continously spawning miniboss
            minibossSpawned = false;
        }

      //spawns hoard of normal enemies, becoming more likely over time, max 50/10000 chance
        if (r.nextInt(10000) < Math.min(10 * (killCounter / 25), 100)) {
            enemySpawnSeed = r.nextInt();
            for (int i = 0; i < 10; i++) {
                enemy = new Enemy(this, 0, 4, enemySpawnSeed);
                add(enemy);
                enemies.add(enemy);
            }
        }

      //spawns wave of normal enemies, becoming more likely over time, max 50/10000 chance
        if (r.nextInt(10000) < Math.min(10 * (killCounter / 25), 150)) {
            for (int i = 0; i < 10; i++) {
                enemy = new Enemy(this, 0, 4, r.nextInt());
                add(enemy);
                enemies.add(enemy);
            }
        }
        
      //spawns a bat hoards when over 40 enemies killed, 20/10000 chance
        if (killCounter > 40 && r.nextInt(10000) < 20+killCounter/50) { 
            for (int i = 0; i < 10; i++) {
                enemy = new Enemy(this, 4, 0, r.nextInt());
                add(enemy);
                enemies.add(enemy);
            }
        }

      //spawns gunner or throwing goblin when more than 80 kills, max 80/10000 chance
        if (killCounter > 40 && r.nextInt(10000) < Math.min(10 * (killCounter / 30), 500)) { 
            if (r.nextInt(2) == 0) {
                enemy = new Enemy(this, 2, 0, r.nextInt());
            } else {
                enemy = new Enemy(this, 3, 0, r.nextInt());
            }

            add(enemy);
            enemies.add(enemy);
        }
    }

}


