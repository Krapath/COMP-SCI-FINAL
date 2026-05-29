import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class GameBackground extends GameObject {
    //set variables
    Image[] backgroundImages = new Image[5];
    Polygon game;
    boolean testerrrr = false;
    
    public void changeBackground(Polygon game){
        this.game = game;
    }

    public void act() {


    }
    

    @Override
    public void paint(Graphics g) {
        super.paint(g); 
    }
}