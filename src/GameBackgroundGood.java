import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Random;

public class GameBackgroundGood extends GameObject {
    // set variables
    Random r = new Random();
    PolygonGame game;
    private Image backgroundImage;

    ArrayList<Polygon> hexagons = new ArrayList<>();
    ArrayList<Integer> hexagonBrightness = new ArrayList<>();
    ArrayList<Boolean> fading = new ArrayList<>();

    boolean dynamicBackground = false; // can be set to false for a static background, true for a dynamic background with fading hexagons
    int polygonSize;
    int fadeTimer = 0; // timer to control fade effect
    int fadeDelay = 4; // can be adjusted for a faster or slower fade effect

    public GameBackgroundGood(PolygonGame game) {
        this.game = game;
        polygonSize = game.getWindowWidth() / 25;
        // make the background image the entire window size so it can cover the whole background
        setSize(game.getWindowWidth() + polygonSize * 4, game.getWindowHeight() + polygonSize * 4); 
        int offsetY = (int) (-polygonSize * 1.5);
        int offsetX = (int) (-polygonSize * 1.5);
        setLocation(offsetX, offsetY);
        drawGrid();
        for (Polygon p : hexagons) {
            hexagonBrightness.add(r.nextInt(10)+30); // random brightness for each hexagon, can be adjusted for a more or less transparent background
            fading.add(false); // initialize fading status for each hexagon
        }

    }

    public void act() {
        repaint();
    }

    // draw the grid of hexagons across the entire background, can be adjusted for a more or less dense background
    public void drawGrid() {
        // the distance between the centers of two adjacent hexagons is equal to the size of the hexagon multiplied by the square root of 3, can be adjusted for a more or less dense background
        double verticalDistance = (polygonSize * Math.sqrt(3) / 2); // vertical distance between hexagons, can be adjusted for a more or less dense background, the -1 is to account for rounding issues with hexagon drawing
        double rowCount = 0;
        for (double i = 0; i <= game.getWindowHeight() + polygonSize * 4; i += verticalDistance) {

            double xOffset = (rowCount % 2 == 0) ? 0 : (int) (polygonSize * 1.5);

            for (double j = 0; j <= game.getWindowWidth() + polygonSize * 4; j += (polygonSize * 3)) {
                hexagons.add(drawHexagon((int)(j + xOffset), (int)i, polygonSize));
            }

            rowCount++;
        }
    }
    // method to draw a hexagon given the center coordinates and size
    public Polygon drawHexagon(int x, int y, int size) {
        // find the vertical distance from the center using special triangle rules
        int verticalDistance = (int) Math.round(size * Math.sqrt(3) / 2); 
        int halfSize = (int) Math.round(size / 2.0);
        int[] xPoints = { x - size, x - halfSize, x + halfSize, x + size, x + halfSize, x - halfSize };
        int[] yPoints = { y, y - verticalDistance, y - verticalDistance, y, y + verticalDistance, y + verticalDistance };
        return new Polygon(xPoints, yPoints, 6);
        
    }
    

    @Override
    public void paint(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        fadeTimer++;
        boolean canFade = true;
        if (dynamicBackground){

            if (fadeTimer > fadeDelay) {
                fadeTimer = 0; // cap fade amount to prevent it from being too fast
            } else {
                canFade = false;
            }
            
            for (int i = 0; i < hexagons.size(); i++) {
                Polygon p = hexagons.get(i);

                //black and white
                if (canFade) {
                    if (fading.get(i)) {
                        if (hexagonBrightness.get(i) < 50) {
                            hexagonBrightness.set(i, hexagonBrightness.get(i) + 1); // increase opacity of hexagon until it reaches the maximum opacity, can be adjusted for a faster or slower fade in effect
                        } else {
                            fading.set(i, false);
                        }
                    } else {
                        if (hexagonBrightness.get(i) > 40) {
                            hexagonBrightness.set(i, hexagonBrightness.get(i) - 1); // decrease opacity of hexagon until it reaches the minimum opacity, can be adjusted for a faster or slower fade out effect
                        } else {
                            fading.set(i, true);
                        }
                    }

                }
                    
                g2d.setColor(new Color(hexagonBrightness.get(i), hexagonBrightness.get(i), hexagonBrightness.get(i))); // set color of hexagon with varying opacity
                g2d.fillPolygon(p);
                }
        

        } else {
            for (int i = 0; i < hexagons.size(); i++) {
                Polygon p = hexagons.get(i);
                g2d.setColor(new Color(hexagonBrightness.get(i), hexagonBrightness.get(i), hexagonBrightness.get(i))); // set color of hexagon with varying opacity
                g2d.fillPolygon(p);
            }
        }
    }
}