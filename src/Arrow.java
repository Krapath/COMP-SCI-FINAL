import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import javax.swing.*;

public class Arrow extends Weapon {

	PolygonGame game;
	public double targetAngle;
	public double speed;
	public int pierce = 2;

	int arrowCX;
	int arrowCY;

	Image arrowImage;
	int spriteSize;
	int size;

	int shaftWidth = 10;
	int shaftHeight = 60;
	ArrayList<Enemy> hitEnemies = new ArrayList<Enemy>();

	public Arrow(PolygonGame game, double angle) {
		super(game, "Cast", "Arrow");
		this.game = game;

		targetAngle = angle;
        size = (game.getWindowWidth() + game.getWindowHeight()) / 250; // projectile size is 1/100 of the entire window
		speed=5.0;
		setSize(size*10, size*10);
		setColor(Color.BLUE);
		x = game.player.x;
		y = game.player.y;
		
		arrowCX = (int)(game.player.x+game.player.size/2.0);
		
		arrowCY = (int)(game.player.y + game.player.size/2.0);
		
		setPosition(this,arrowCX,arrowCY);
		arrowImage = new ImageIcon("Images/Sprites/Missile.png").getImage();
		spriteSize = 5 * size;
	}

	public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();

   

            int offset = (spriteSize - getWidth()) / 2; // center it on the hitbox

            g2d.translate(getWidth() / 2.0, getHeight() / 2.0);
            g2d.rotate(spriteAngle);
            g2d.translate(-getWidth() / 2.0, -getHeight() / 2.0);

            if (arrowImage != null) {
                g2d.drawImage(arrowImage, -offset, -offset, spriteSize, spriteSize, null);
            } 

            g2d.setTransform(old);
	} 

	public boolean arrowHits(Enemy e) {
		double ex = e.getX() + e.size / 2.0;
		double ey = e.getY() + e.size / 2.0;

		// get the enemies position relative to arrow center
		double localX = ex - arrowCX;
		double localY = ey - arrowCY;

		// subtract the PI/2 offset since spriteAngle always has it added
		// 2d rotation matrix
		double checkAngle = -(spriteAngle - Math.PI / 2);
		double rotX = localX * Math.cos(checkAngle) - localY * Math.sin(checkAngle);
		double rotY = localX * Math.sin(checkAngle) + localY * Math.cos(checkAngle);

		// check if its inside the rectangle
		return (Math.abs(rotX) <= shaftWidth / 2.0 && rotY >= -shaftHeight / 2.0 && rotY <= shaftHeight / 2.0);
	}

	public void act() {
		spriteAngle = targetAngle;
		shoot(speed, targetAngle);
        setPosition();
        
	
		for (int i = 0; i < game.enemies.size(); i++) { //for every enemy in the game
			if (arrowHits(game.enemies.get(i))) { //if it collides with an enemy
				boolean hit = false;
				for (int j = 0; j < hitEnemies.size(); j++) { //for every enemy already hit
					if (game.enemies.get(i) == hitEnemies.get(j)) { //if this enemy has already been hit, sets hit to true
						hit = true;
					}
				}

				if (!hit) { // if this enemy has not already been hit by this
							// arrow
					hitEnemies.add(game.enemies.get(i));
					game.enemies.get(i).health -= damage;
					game.enemies.get(i).setColor(Color.RED);
					
					pierce--;
					
					if(pierce == 0)
						game.remove(this);

					int enemyX = game.enemies.get(i).getX();
					int enemyY = game.enemies.get(i).getY();

					if (game.enemies.get(i).health <= 0) {
						XpOrb xp = new XpOrb(enemyX, enemyY, game);
						game.add(xp);
						game.xpOrbs.add(xp);
						hitEnemies.remove(game.enemies.get(i));
						game.remove(game.enemies.get(i));
						game.enemies.remove(i);
					}
				}
			}
		}

	}
}
