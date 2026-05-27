
/*
 * Contributors: Hugo To, Raymond Tan, Mohammad Sadeghi
 * Start date: 5/19/2026
 * End date: 
 * Project: Polygons - a roguelike top-down shooter.
 */

import java.util.ArrayList;
import java.util.Random;

public class Polygon extends Game {

    Random r = new Random();
    Player player;
    Projectile projectile;
    Enemy enemy;
    public boolean choosingBuff = false;
    ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    ArrayList<XpOrb> xpOrbs = new ArrayList<XpOrb>();
    ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();

    public void setup() {
        setDelay(16); // 60fps
        setTitle("Polygon");
        player = new Player(this);
        player.setLocation(335, 225); // middle
        add(player);
        add(new DisplayDebug(this));
    }

    public void act() {
        if (choosingBuff) {
            return; // pause the game while choosing a buff(lock the game)
        }
        if (mouseLeftPressed()) {
            if (player.attackDelay > 5) {
                player.attackDelay = 0;
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
        if (player.health <= 0) { // stops if dies
            stopGame();
        }

        if (player.score >= 5) { // spawns a powerup when the player reaches level 5
            choosingBuff = true;
            int centeredY = getWindowHeight() / 2 - getWindowHeight() / 6;
            powerUps.add(new PowerUp(getWindowWidth() / 4 - getWindowWidth() / 10, centeredY, this));
            powerUps.add(new PowerUp(getWindowWidth() / 4 * 2 - getWindowWidth() / 10, centeredY, this));
            powerUps.add(new PowerUp(getWindowWidth() / 4 * 3 - getWindowWidth() / 10, centeredY, this));
            for (PowerUp p : powerUps) {
                add(p);
            }
            player.score = 0; // reset score after spawning powerup

        }

    }

    public static void main(String[] args) {
        Polygon game = new Polygon();//
        game.setVisible(true);
        game.initComponents();
    }
}
