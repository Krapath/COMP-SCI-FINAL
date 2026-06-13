import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Random;

/**
 * draws background with polygons of random darkness levels
 * very mutuable.
 */
public class GameBackground extends GameObject {
	// set variables
	Random r = new Random();
	PolygonGame game;
	private Image backgroundImage;

	ArrayList<Polygon> hexagons = new ArrayList<>();
	ArrayList<Integer> hexagonBrightness = new ArrayList<>();

	int polygonSize;

	public GameBackground(PolygonGame game) {
		this.game = game;
		polygonSize = game.getWindowWidth() / 25;
		// make the background image the entire window size so it can cover the
		// whole
		// background and more
		setSize(game.getWindowWidth() + polygonSize * 4, game.getWindowHeight() + polygonSize * 4);
		int offsetY = (int) (-polygonSize * 1.5);
		int offsetX = (int) (-polygonSize * 1.5);
		setLocation(offsetX, offsetY);
		drawGrid();
		drawGrid();
		// random brightness for each hexagon
		for (Polygon p : hexagons) {
			hexagonBrightness.add(r.nextInt(10) + 30);
		}
	}

	public void act() {
		repaint();
	}

	// draw the grid of hexagons across the entire background
	public void drawGrid() {
		// vertical distance between hexagons, slightly overlapping due to
		// rounding errors, but very minor
		double verticalDistance = (polygonSize * Math.sqrt(3) / 2);
		double rowCount = 0;
		for (double i = 0; i <= game.getWindowHeight() + polygonSize * 4; i += verticalDistance) {

			double xOffset = (rowCount % 2 == 0) ? 0 : (int) (polygonSize * 1.5);

			for (double j = 0; j <= game.getWindowWidth() + polygonSize * 4; j += (polygonSize * 3)) {
				hexagons.add(drawHexagon((int) (j + xOffset), (int) i, polygonSize));
			}

			rowCount++;
		}
	}

	// method to draw a hexagon given the center coordinates and size
	public Polygon drawHexagon(int x, int y, int size) {
		// find the vertical distance from the center using special triangle
		// rules
		int verticalDistance = (int) Math.round(size * Math.sqrt(3) / 2);
		int halfSize = (int) Math.round(size / 2.0);
		int[] xPoints = { x - size, x - halfSize, x + halfSize, x + size, x + halfSize, x - halfSize };
		int[] yPoints = { y, y - verticalDistance, y - verticalDistance, y, y + verticalDistance,
				y + verticalDistance };
		return new Polygon(xPoints, yPoints, 6);

	}

	@Override
	public void paint(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		for (int i = 0; i < hexagons.size(); i++) {
			Polygon p = hexagons.get(i);
			// set the color of the hexagon with random shade of black
			g2d.setColor(new Color(hexagonBrightness.get(i), hexagonBrightness.get(i), hexagonBrightness.get(i)));
			g2d.fillPolygon(p);
		}
	}
}
