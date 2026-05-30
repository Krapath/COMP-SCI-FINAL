import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;


/*
 * THIS BACKGROUND CLASS IS NOT USED ANYMORE, IT HAS BEEN REPLACED BY GAMEBACKGROUNGGOOD, 
 * DRAWS ALL OF THE IMAGES NATIVELY
 */

public class GameBackground extends GameObject {
    //set variables
    //Image[] backgroundImages = new Image[5];
    PolygonGame game;
    private Image backgroundImage;
    public void changeBackground(PolygonGame game,String imagePath) {
        this.game = game;
        this.backgroundImage = new ImageIcon(imagePath).getImage();
        setSize(game.getWindowWidth(), game.getWindowHeight());
    }
    public void act() {
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g); 
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } 
    }
}