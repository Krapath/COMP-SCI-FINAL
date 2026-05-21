import java.util.Random;
import java.util.ArrayList;
public class Polygon extends Game {
	Random r = new Random();
	Player player;
	Projectile projectile;
	Enemy enemy;
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	public void setup() {
		setDelay(16); // 60fps
		setTitle("Polygon");
		setSize(1000, 1000);
		player = new Player(this);
		player.setLocation(335, 225); // middle
		add(player);
		add(new Coordinates(this));
	
	}


	public void act() {
		if (mouseLeftPressed()) {
			projectile = new Projectile(this);
			add(projectile);
			projectiles.add(projectile);
		}

		if (r.nextInt(300) < 1) { // 0.33% chance each tick to spawn an enemy
			enemy = new Enemy(this);
			add(enemy);
			enemies.add(enemy);
		}

	}

	public static void main(String[] args) {
		Polygon game = new Polygon();
		game.setVisible(true);
		game.initComponents();
	}
}
