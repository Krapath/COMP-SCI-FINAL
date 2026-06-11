
import java.awt.Color;
import java.io.File;
import java.util.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;

import javax.sound.sampled.*;
import java.awt.*;

import java.util.Random; 
@SuppressWarnings("unused")

public class XpOrb extends GameObject {

	Random r = new Random();
    PolygonGame game;

    // TODO: scale to window size instead of hardcoding values
    double speed = 20.0; // maybe accelerate as it gets closer to player
    int distanceAttraction = 100; // the distance at which the xp orb starts moving towards the player, can be adjusted for better gameplay
    boolean chasing = false;
    Image xpOrb;
    public XpOrb(int enemyX, int enemyY, PolygonGame game) {
        this.game = game;
        setLocation(enemyX, enemyY); // update position
      
        int width = (game.getWindowHeight()+game.getWindowWidth())/300;
        int height = width;
        setSize(width, height);
        setColor(Color.YELLOW);
        x = getX();
        y = getY();
        xpOrb = new ImageIcon("Images/Sprites/XpOrb.png").getImage();

    }

    
    public void paint(Graphics g){
            if (xpOrb != null) {
                g.drawImage(xpOrb, 0, 0, getWidth(), getHeight(), null);
            }
    }
    

	
    public void act() {
        if (PolygonGame.gamePause) {
            return; // xp orbs do not move or collide with the player while the player is choosing a buff
        }        // check for collision with player

        if ((Math.abs(game.player.x - x) <= distanceAttraction) && (Math.abs(game.player.y - y) <= distanceAttraction)) {// only move towards the player if the xp orb is close to player
            chasing = true;
        }


        // if it sees the player once it will continuously chase even if the player moves out the initial range
        if (chasing) {
            chase(speed, game.player);
            setPosition();
        }

        if (collides(game.player)) {
        	
        	int randNum = r.nextInt(10)+1;
        	
        	SoundEffects.play("SFX/XP/xp"+randNum+".wav",-20.0f);
            Player.score += 1; // increase player score on collision
            Player.xp += 1;
            game.remove(this); // remove xp orb after collision
        }
    }
}
