
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import javax.swing.*;

public class Arrow extends Weapon {

	PolygonGame game;
	public double targetAngle;
	public double speed;
	public int pierce = 1;

	double arrowCX;
	double arrowCY;

	private static final double IMAGE_ORIENTATION_OFFSET = Math.PI / 4;

	static Image arrowImage;
	int spriteSize;
	int size;
	int shaftWidth;
	int shaftHeight;
	int projSize;
	ArrayList<Enemy> hitEnemies = new ArrayList<Enemy>();

    public Arrow(PolygonGame game, double angle) {
        super(game, "Cast", "Arrow");
        this.game = game;

        targetAngle = angle;
        size = (game.getWindowWidth() + game.getWindowHeight()) / 250; // projectile size is 1/250 of the entire window
        projSize = size * 5;
        shaftWidth = (int) (size * 1.5);
        shaftHeight = (int) (size * 3);
        speed = (game.getWindowHeight() + game.getWindowWidth()) / 1000;

        setSize(projSize, projSize);
        setColor(Color.BLUE);
        x = game.player.x - projSize / 4;
        y = game.player.y - projSize / 4;
        Player.weapons.add(this);

        setPosition();

		arrowImage = new ImageIcon("Images/Sprites/Arrow.png").getImage();
		
		spriteSize = projSize;
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform old = g2d.getTransform();
		g2d.translate(getWidth() / 2.0, getHeight() / 2.0);
		g2d.rotate(spriteAngle);
		g2d.translate(-getWidth() / 2.0, -getHeight() / 2.0);

		if (arrowImage != null) {
			g2d.drawImage(arrowImage, 0, 0, spriteSize, spriteSize, null);
		}
		g2d.setTransform(old);
	}

	/**
	 * Returns true if the given enemy's center intersects the arrow's rotated hitbox.
	 *
	 * The arrow sprite is rendered with an orientation offset, so the collision math
	 * must use the same offset to remain consistent with the visible sprite.
	 */
	public boolean arrowHits(Enemy e) {
		double ex = e.getX() + e.size / 2.0;
		double ey = e.getY() + e.size / 2.0;

		// Enemy center relative to the arrow center.
		double localX = ex - arrowCX;
		double localY = ey - arrowCY;

		// Rotate the enemy point into the arrow's local hitbox coordinate frame.
		double checkAngle = -(spriteAngle + IMAGE_ORIENTATION_OFFSET);
		double cos = Math.cos(checkAngle);
		double sin = Math.sin(checkAngle);
		double rotX = localX * cos - localY * sin;
		double rotY = localX * sin + localY * cos;

		// Check against the rotated rectangular shaft hitbox.
		return Math.abs(rotY) <= shaftWidth / 2.0
			&& rotX >= -shaftHeight / 2.0
			&& rotX <= shaftHeight / 2.0;
	}

	public void act() {
		if(PolygonGame.gamePause){
			return;
		}
		// The arrow sprite image is drawn with a 45-degree native orientation.
		// Apply the same offset here so the rendered arrow and hitbox stay aligned
		// with the intended target angle.
		spriteAngle = targetAngle - IMAGE_ORIENTATION_OFFSET;
		shoot(speed, targetAngle);
        setPosition();
        
		// Calculate arrow center with full precision
		arrowCX = x + projSize / 2.0;
		arrowCY = y + projSize / 2.0;
		
		for (int i = 0; i < PolygonGame.enemies.size(); i++) { //for every enemy in the game
			if (arrowHits(PolygonGame.enemies.get(i))) { //if it collides with an enemy
				boolean hit = false;
				for (int j = 0; j < hitEnemies.size(); j++) { //for every enemy already hit
					if (PolygonGame.enemies.get(i) == hitEnemies.get(j)) { //if this enemy has already been hit, sets hit to true
						hit = true;
					}
				}

				if (!hit) { // if this enemy has not already been hit by this
							// arrow
					hitEnemies.add(PolygonGame.enemies.get(i));
					PolygonGame.enemies.get(i).health -= damage;
					PolygonGame.enemies.get(i).damaged = true;
					
					pierce--;
					
					if(pierce == 0){
						game.remove(this);
						PolygonGame.arrows.remove(this);
					}

					int enemyX = PolygonGame.enemies.get(i).getX();
					int enemyY = PolygonGame.enemies.get(i).getY();

					if (PolygonGame.enemies.get(i).health <= 0) {
						XpOrb xp = new XpOrb(enemyX, enemyY, game);
						game.add(xp);
						PolygonGame.xpOrbs.add(xp);
						hitEnemies.remove(PolygonGame.enemies.get(i));
						game.remove(PolygonGame.enemies.get(i));
						PolygonGame.enemies.remove(i);
					}
				}
			}
		}
		// remove when offscreen
		if (x < -projSize || x > game.getFieldWidth() + projSize || y < -projSize || y > game.getFieldHeight() + projSize) {
			game.remove(this);
			PolygonGame.arrows.remove(this);
		}

    }

}
