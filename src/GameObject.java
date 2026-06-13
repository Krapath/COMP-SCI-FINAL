/*
 * This code is protected under the Gnu General Public License (Copyleft), 2005 by
 * IBM and the Computer Science Teachers of America organization. It may be freely
 * modified and redistributed under educational fair use.
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.util.Random;
import javax.sound.sampled.*;
import javax.swing.JComponent;

/**
 * An abstract class for an object which can be added to an instance of
 * <code>Game</code><br>
 * <br>
 * The <code>act</code> method can be implemented to provide a behavior for the
 * object, and will be called every millisecond automatically by a <code>
 * Game</code> it has been added to.<br>
 * Author: Hugo To, Mohammad Sadeghi, Raymond Tan
 *
 * @see Game#add
 */
public abstract class GameObject extends JComponent {

    private Random r = new Random();

    Color c = Color.white;
    public double spriteAngle = 0;
    public double x, y;
    boolean readyToApply = false;
    protected boolean wasPressed = false;

    /**
     * Sets the pixel width and height of the object
     *
     * @param width	a width in pixels
     * @param height	a height in pixels
     */
    public void setSize(int width, int height) {
        super.setSize(width, height);
    }

    /**
     * Gets the x component of the coordinate of the upper left corner of this
     * object
     *
     * The coordinate is relative to the playing field, with <code>0</code>
     * being the far left of the field, and positive values moving toward the
     * right of the field
     */
    public int getX() {
        return getLocation().x;
    }

    public double getRealX() {
        return x;
    }

    /**
     * Gets the y component of the coordinate of the upper left corner of this
     * object
     *
     * The coordinate is relative to the playing field, with <code>0</code>
     * being the top of the field, and positive values moving toward the bottom
     * of the field
     */
    public int getY() {
        return getLocation().y;
    }

    public double getRealY() {
        return y;
    }


    /*
	 * Checks if the object contains a point
	 * 
	 * @param x		the x coordinate of the point
	 * @param y		the y coordinate of the point
	 * @return		<code>true</code> if the object contains the point
     */
    public boolean contains(int x, int y) {
        return getBounds().contains(x, y);
    }

    /**
     * Sets the x (horizontal) position of this object
     *
     * Setting the x position will not affect the y position
     *
     * @param x	the x position of the upper left corner of this object
     */
    public void setX(int x) {
        super.setLocation(x, getLocation().y);
    }

    /**
     * Sets the y (vertical) position of this object
     *
     * Setting the y position will not affect the x position
     *
     * @param y	the y position of the upper left corner of this object
     */
    public void setY(int y) {
        super.setLocation(getLocation().x, y);
    }

    /**
     * Sets the color of this object
     *
     * @param c	the color of this object
     * @see	java.awt.Color
     */
    public void setColor(Color c) {
        this.c = c;
    }

    /*getter for color
     */
    public Color getColor() {
        return c;
    }

    /**
     * Paints the object on the screen. This is called automatically.
     *
     * Child classes should not implement or override this method.
     */
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform at = new AffineTransform();
        at.rotate(spriteAngle, getWidth() / 2.0, getHeight() / 2.0);
        g2d.transform(at);
        g2d.setColor(c);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Returns <code>true</code> if this object collides with another
     * <code>GameObject</code>
     *
     * @param o	the <code>GameObject</code> to test for collision
     * @return		<code>true</code> if collision occurs
     */
    public boolean collides(GameObject o) {
        return getBounds().intersects(o.getBounds());
    }

    /**
     * This method should be implemented to provide a <i>behavior</i>
     * for this object.
     *
     * The <code>Game</code> will automatically call this method every
     * millisecond. It can be implemented to provide basic behavior for an
     * object, such as movement.
     */
    public abstract void act();

    // hugo method
    public boolean isClickedAndReleased(PolygonGame game, int mouseX, int mouseY) {
        boolean currentlyPressed = game.mouseLeftPressed();
        boolean isHovering = this.contains(mouseX, mouseY);
        boolean clicked = false;

        //find exact moment the mouse goes from not clicked to clicked
        if (currentlyPressed && !wasPressed) {
            // only check as ready if that initial click is inside the object
            readyToApply = isHovering;
        }

        // find the exact moment the mouse is released, clicked to not clicked
        if (!currentlyPressed && wasPressed) {
            // trigger only if the click started on this object and was released on it
            if (readyToApply && isHovering) {
                clicked = true;
            }
            // reset after finished
            readyToApply = false;
        }

        // update the tracking variable
        wasPressed = currentlyPressed;

        return clicked;
    }

    public void SoundEffects(String fileName, float volume) {

        File soundFile = new File(fileName);

        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volume); // Reduce volume by 10 decibels.

            // Source - https://stackoverflow.com/a/6707971
            // Posted by datahaki, modified by community. See post 'Timeline' for change history
            // Retrieved 2026-06-11, License - CC BY-SA 3.0
            clip.start();
        } catch (Exception e) {
            System.err.println("Unsupported audio format for file: " + soundFile.getName());
            e.printStackTrace();
        }

    }

    //overloaded for random file selection
    //mohammads method's
    /**
     * sets the position of the object by rounding its double x and y values.
     * pre: x and y double values no overriden post: gameobject's coordiantes
     * are moved to the rounded version of its double x and y values
     *
     */
    public void setPosition() {
        setX((int) (x + 0.5));
        setY((int) (y + 0.5));
    }

    /**
     * sets the position of the object based on given int x and y values. pre:
     * none post: gameobject's coordiantes are moved to given int x and y values
     *
     */
    public void setPosition(int x, int y) {
        setX(x);
        setY(y);
    }

    /**
     * GameObject chases after inputted tager GameObject with given speed pre:
     * given GameObject has applicabale x and y values post: the cooridnates of
     * the current GameObject is moved towards the given GameObject based on
     * speed.
     */
    public void chase(double speed, GameObject target) {
        double playerAngle = Math.atan2(target.y - y, target.x - x);
        x += (Math.cos(playerAngle) * speed);
        y += (Math.sin(playerAngle) * speed);
    }

    /**
     * GameObject shoots towards the given x and y double values with given
     * speed
     *
     * pre: none post: GameObjects coordiantes get changed in the direction of
     * given double coordiantes based on speed.
     *
     */
    public void shoot(double speed, double targetX, double targetY) {
        double targetAngle = Math.atan2(targetY - y, targetX - x);
        x += (Math.cos(targetAngle) * speed);
        y += (Math.sin(targetAngle) * speed);
    }

    /**
     * GameObject shoots towards the given double radians angle value with given
     * speed
     *
     * pre: none post: GameObjects coordiantes get changed in the direction of
     * given radian angle based on speed.
     *
     */
    public void shoot(double speed, double targetAngle) {
        x += (Math.cos(targetAngle) * speed);
        y += (Math.sin(targetAngle) * speed);
    }

    /**
     * gives the angle from this GameObject to given coordinates in radians pre:
     * none post: gives the radian angle of this object as the origin in
     * compriosn to given coordiantes, given as a double radians value
     */
    public double getRealAngle(double targetX, double targetY) {
        return Math.atan2(targetY - y, targetX - x);
    }

}
