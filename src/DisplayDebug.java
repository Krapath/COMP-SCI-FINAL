import java.awt.Color;
import java.awt.Graphics; 
import java.awt.Font;
import java.awt.FontMetrics;

public class DisplayDebug extends GameObject{
    Polygon game;
    static int []xPoints;
    static int []yPoints;
    static int nPoints;
    static int posX;
    static int posY;
    static double radius;
    private Font pixelFont;
    public DisplayDebug(Polygon game) {
        this.game = game; //hi this is a test test test
        setSize(game.getWindowWidth(), game.getWindowHeight());  // size of the text area
        radius = (game.getWindowWidth()+game.getWindowHeight())/75;

        try {
            java.io.File fontFile = new java.io.File("Fonts/PressStart2P-Regular.ttf"); 
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont((int)radius/2f);
        } catch (Exception e) {
            // Fallback to basic monospaced if the file is missing
            pixelFont = new Font("Monospaced", Font.BOLD, 100);
            e.printStackTrace();
        }
    }

    public void act() {
        // reposition every tick so text stays in corner
   	 double angle = Math.PI/2;
	     xPoints = new int [Player.health+2];
	     yPoints = new int [Player.health+2];
	     posX = (int) (game.getWindowWidth() - radius*(2));
	     posY = (int) (0 + radius*(2));
    	for (int i =0; i < xPoints.length;i++){
    	 
         double x = (radius * Math.cos(angle)+ posX );
         double y = (radius * Math.sin(angle)+ posY);
         
        
         
         xPoints[i]= (int) Math.round(x+0.0001);
         yPoints[i]= (int) Math.round(y+0.0001);
        // System.out.println(" values:" + yPoints[i]);
        // System.out.println(y);
        // System.out.println(angle);
         angle+= Math.PI*2/(Player.health+2);
         
    	}
    	
    	for (int i =0; i < xPoints.length;i++){
       	 
            
       	}
         nPoints = Player.health+2;
         
        repaint();  // redraws this object every tick
        
    }

    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        
        String health = String.valueOf(Player.health);

        // draw mouse and player coordinates
        g.drawString("MOUSE    X:" + game.getMouseX() + "  Y:" + game.getMouseY(), 10, 20);
        g.drawString("PLAYER   X:" + game.player.getX() + "  Y:" + game.player.getY(), 10, 40);

        // finds the angle between the player and the mouse cursor and displays it in degrees
        double angle = game.getAngle(game.player.getX(), game.player.getY(),game.getMouseX(), game.getMouseY()) * (180/Math.PI);
        angle =-angle; // increase counterclockwise, follows standard unit circle convention
        if (angle < 0){ // display angle as a positive value between 0 and 360
        	angle+=360;
        }
        
        // display the number of enemies and hp
        g.drawString("ENEMIES: " + game.enemies.size(), 10, 80);
        g.drawString("HP: " + Player.health, 10, 100);

        // display the players current score
        g.drawString("SCORE: " + Player.score, 10, 120);
        
        // display the mouse cursor as a red rectangle 
        g.drawString("ANGLE:" + angle , 10, 60);
        g.setColor(Color.RED);
        g.drawRect(game.getMouseX(),game.getMouseY(),15,15);        
      
        // draw healthbar
        if (xPoints != null && yPoints != null) {
            g.drawPolygon(xPoints, yPoints, nPoints);
            g.setFont(pixelFont);
            FontMetrics metrics = g.getFontMetrics(pixelFont);
            

            int textWidth = metrics.stringWidth(health);
            int textHeight = metrics.getAscent();
            
            int healthX = posX - textWidth/2 + 1;
            int healthY = posY + textHeight/2;
            
            //System.out.println(healthX + " " + healthY);
            
            g.drawString(health,healthX,healthY);
        }


    }
}



