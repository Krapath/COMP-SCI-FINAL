import java.util.Random;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
@SuppressWarnings("unused")

//TODO: maybe just use paint 
public class PowerUp extends GameObject {
    Random r = new Random();
    PolygonGame game;
    public int buffType;
    public boolean readyToApply = false; // whether the buff should be applied on mouse release
    public boolean wasPressed = false; // whether the mouse was pressed while hovering over this powerup (used to prevent applying the buff if the player clicks on a powerup and then drags the mouse away before releasing)
    public int[] xPointsBuff;
    public int[] yPointsBuff;
    public int posXBuff;
    public int posYBuff;
    public int nPointsBuff;
    public double radius;
    private Font pixelFont;
    String buff;
    int [] buffArray = new int[7];
    

    public PowerUp(int x, int y, PolygonGame game) {


        this.game = game;
        setSize(game.getWindowWidth() / 5, game.getWindowHeight() / 3);
        radius = (game.getWindowWidth()+game.getWindowHeight())/75;
        posXBuff = (game.getWindowWidth() / 5) / 2;
        posYBuff = (int)((game.getWindowHeight() / 3) / 2+radius);
        setColor(Color.BLUE);
        setLocation(x, y);

        try {
            java.io.File fontFile = new java.io.File("Fonts/PressStart2P-Regular.ttf"); 
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont((int)radius/2f);
        } catch (Exception e) {
            // Fallback to basic monospaced if the file is missing
            pixelFont = new Font("Monospaced", Font.BOLD, 100);
            e.printStackTrace();
        }


        switch (r.nextInt(7)) {
            case 0:
                setColor(Color.RED);
                buffType = 0; // health buff
                break;
            case 1:
                setColor(Color.GREEN);
                buffType = 1; // speed buff
                break;
            case 2:
                setColor(Color.BLUE);
                buffType = 2; // attack speed buff
                break;
            case 3:
                setColor(Color.YELLOW);
                buffType = 3; // lightning buff
                break;
            case 4:
                setColor(Color.ORANGE);
                buffType = 4;
                break;
            case 5:
                setColor(Color.MAGENTA);
                buffType =5;
                break;
            case 6:
                setColor(Color.CYAN);
                buffType = 6;
                break;
        }
        buff = String.valueOf(buffArray[buffType]);

    }
  
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g; // cast to Graphics2D to use thicker lines
   
        if (xPointsBuff != null && yPointsBuff != null) {
            g2d.setStroke(new BasicStroke(4));
            Color color = getColor().darker();
            g2d.setColor(color);
            g2d.drawPolygon(xPointsBuff, yPointsBuff, nPointsBuff);

            g2d.setFont(pixelFont);
            FontMetrics metrics = g.getFontMetrics(pixelFont);

            int textWidth = metrics.stringWidth(buff);
            int textHeight = metrics.getAscent();

            int buffX = posXBuff - textWidth / 2 + 1;
            int buffY = posYBuff + textHeight / 2;

            g.drawString(buff, buffX, buffY);
    }


    }


    
    public void act() {
                        
        if (!PolygonGame.gamePause)
            return; // powerups only work while the player is choosing a buff (optimize)
        
        double buff1 = Math.PI / 2;

        double buff1Angle = Math.PI / 2;
        
        int x = game.getMouseX();
        int y = game.getMouseY();

        int sides;
        if(contains(x,y)){
            sides = buffArray[buffType] + 2 + 1;
            buff = String.valueOf(buffArray[buffType]+1);
        } else {
            sides = buffArray[buffType] + 2;
            buff = String.valueOf(buffArray[buffType]);
        } 


        xPointsBuff = new int[sides];
        yPointsBuff = new int[sides];

        for (int i = 0; i < sides; i++) {
            int centerX = posXBuff;
            int centerY = posYBuff;

            double x1 = radius * Math.cos(buff1Angle) + centerX;
            double y1 = radius * Math.sin(buff1Angle) + centerY;

            xPointsBuff[i] = (int) Math.round(x1 + 0.0001);
            yPointsBuff[i] = (int) Math.round(y1 + 0.0001);

            buff1Angle += Math.PI * 2 / sides;
        }

        nPointsBuff = sides;


        if (!game.mouseLeftPressed()) {
            readyToApply = true; // the buff should be applied on mouse release if the mouse was pressed while hovering over this powerup
        }
        if (game.mouseLeftPressed() && contains(x, y) && readyToApply) {
            wasPressed = true;
        }  
        

        

        //apply the buff and remove the powerups if the player clicks on a powerup and releases the mouse button while still hovering over the same powerup
        if (wasPressed && !game.mouseLeftPressed() && contains(x, y) && readyToApply) {  // TODO: turn into a method in game class for other application besides powerups (ex. clicking a "start game" button on the main menu)
            buffArray[buffType]++;
            if (buffType == 0)
                Player.health += 5;
            else if (buffType == 1)
                Player.speed += 1;
            else if (buffType == 2)
                Player.attackDelay += 1;
            else if (buffType == 3)
                if (!Player.chainLightningActive){
                Player.chainLightningActive = true; // set chain lightning active for all projectiles, will be reset to false at the end of the next time the player chooses a buff
                }
            else if (buffType == 4){
                if(!Player.atgMissileActive){
                Player.atgMissileActive = true;
                }
            }
            else if (buffType == 5){
                if (!Player.glaiveActive){
                game.glaive = new Glaive(game);
                add(game.glaive);
                }
            }   
            else if (buffType == 6){
                game.yonduArrow = new YonduArrow(game);
                add(game.yonduArrow);
            }   

            for (int i = 0; i < game.powerUps.size(); i++) { // remove all powerups from the game
                game.remove(game.powerUps.get(i));
            }

            game.powerUps.clear(); // clear the list of powerups
            PolygonGame.gamePause = false; // allow the game to continue
            PolygonGame.choosingBuff = false;
            readyToApply = false; // reset readyToApply for the next time the player chooses a buff
            wasPressed = false; // reset wasPressed for the next time the player chooses a buff

        }
    

        if (!game.mouseLeftPressed()) {
            wasPressed = false;
        }
    }
}
