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
	int shaftWidth; 
	int shaftHeight;
	int projSize;
	ArrayList<Enemy> hitEnemies = new ArrayList<Enemy>();

	public Arrow(PolygonGame game, double angle) {
		super(game, "Cast", "Arrow");
		this.game = game;

		targetAngle = angle;
        size = (game.getWindowWidth() + game.getWindowHeight()) / 250; // projectile size is 1/250 of the entire window
        projSize =size*5;
		shaftWidth = (int)(size * 1.5);  
		shaftHeight = (int)(size * 3); 
		speed=10.0;
		setSize(projSize, projSize);
		setColor(Color.BLUE);
		x = game.player.x;
		y = game.player.y;
	
		setPosition();

		//TODO: replace with actual arrow sprite placeholder for
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

			// draw hitbox
			g2d.setColor(Color.GREEN);
			g2d.setStroke(new BasicStroke(1));
			// draw the rectangle in unrotated space, then rotate it with the arrow
			g2d.translate(getWidth() / 2.0, getHeight() / 2.0);
			g2d.rotate(spriteAngle + Math.PI/4); // undo the unrotation 
			g2d.drawRect(-shaftHeight/2, -shaftWidth/2, shaftHeight, shaftWidth);
			g2d.setTransform(old);

		}
		// still buggy and i think broken, takes logic from yondu arrow
		public boolean arrowHits(Enemy e) { // same logic as the yondu arrow
		double ex = e.getX() + e.size / 2.0;
		double ey = e.getY() + e.size / 2.0;

		// get the enemies position relative to arrow center
		double localX = ex - arrowCX;
		double localY = ey - arrowCY;

		// 2d rotation matrix


		// The missile image points right by default, which matches math convention angle of 0
		// The hitbox is a horizontal rectangle since the arrow travels along the X axis.
		double checkAngle = -(spriteAngle+Math.PI/4);
		double rotX = localX * Math.cos(checkAngle) - localY * Math.sin(checkAngle);
		double rotY = localX * Math.sin(checkAngle) + localY * Math.cos(checkAngle);

		// shaft is horizontal since image points right, so width and height are swapped
		return (Math.abs(rotY) <= shaftWidth / 2.0 && rotX >= -shaftHeight / 2.0 && rotX <= shaftHeight / 2.0);
	}

	public void act() {
		if(game.gamePause){
			return;
		}
		spriteAngle = targetAngle-Math.PI/4;
		shoot(speed, targetAngle);
        setPosition();
        
		//make sure it rounds
		arrowCX = (int)(x + projSize / 2.0 + 0.5);
		arrowCY = (int)(y + projSize / 2.0 + 0.5);
		
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
					
					if(pierce == 0){
						game.remove(this);
						game.arrows.remove(this);
					}

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
		// remove when offscreen
		if (x < -projSize || x > game.getFieldWidth() + projSize || y < -projSize || y > game.getFieldHeight() + projSize) {
			game.remove(this);
			game.arrows.remove(this);
		}

	}
	
	


}
