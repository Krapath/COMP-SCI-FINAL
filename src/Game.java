
/*
 * This code is protected under the Gnu General Public License (Copyleft), 2005 by
 * IBM and the Computer Science Teachers of America organization. It may be freely
 * modified and redistributed under educational fair use.
 */

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.awt.MouseInfo;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.Timer;
import java.awt.Toolkit;
import java.awt.Dimension;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
@SuppressWarnings("unused")

/**
 * An abstract Game class which can be built into Pong.<br>
 * <br>
 * The default controls are for "Player 1" to move left and right with the 'Z'
 * and 'X' keys, and "Playr 2" to move left and right with the 'N' and 'M'
 * keys.<br>
 * <br>
 * Before the Game begins, the <code>setup</code> method is executed. This will
 * allow the programmer to add any objects to the game and set them up. When the
 * game begins, the <code>act</code> method is executed every millisecond. This
 * will allow the programmer to check for user input and respond to it.
 * 
 * @see GameObject
 */
public abstract class Game extends JFrame {
	private boolean _isSetup = false;
	private boolean _initialized = false;
	private ArrayList _ObjectList = new ArrayList();
	private Timer _t;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int windowWidth = (int) screenSize.getWidth();
	private int windowHeight = (int) screenSize.getHeight();
	/**
	 * <code>true</code> if the 'A' key is being held down
	 */
	private boolean leftKey = false;

	/**
	 * <code>true</code> if the 'D' key is being held down.
	 */
	private boolean rightKey = false;

	/**
	 * <code>true</code> if the 'W' key is being held down.
	 */
	private boolean upKey = false;

	/**
	 * <code>true</code> if the 'S' key is being held down.
	 */
	private boolean downKey = false;
	/**
	 * <code>true</code> if the mouse left button is being held down or clicked.
	 */
	private boolean mouseLeft = false;
	private int mouseX;
	private int mouseY;

	public boolean mouseLeftPressed() {
		return this.mouseLeft;

	}

	public int getWindowWidth() {
		return windowWidth;
	}

	public int getWindowHeight() {
		return windowHeight;
	}

	/**
	 * Returns <code>true</code> if the 'A' key is being pressed down
	 * 
	 * @return <code>true</code> if the 'A' key is being pressed down
	 */
	public boolean AKeyPressed() {
		return leftKey;
	}

	/**
	 * Returns <code>true</code> if the 'D' key is being pressed down
	 * 
	 * @return <code>true</code> if the 'D' key is being pressed down
	 */
	public boolean DKeyPressed() {
		return rightKey;
	}

	/**
	 * Returns <code>true</code> if the 'W' key is being pressed down
	 * 
	 * @return <code>true</code> if the 'W' key is being pressed down
	 */
	public boolean WKeyPressed() {
		return upKey;
	}

	/**
	 * Returns <code>true</code> if the 'S' key is being pressed down
	 * 
	 * @return <code>true</code> if the 'S' key is being pressed down
	 */
	public boolean SKeyPressed() {
		return downKey;
	}

	public int getMouseX() {
		return mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}

	/**
	 * When implemented, this will allow the programmer to initialize the game
	 * before it begins running
	 * Adding objects to the game and setting their initial positions should be
	 * done here.
	 * 
	 * @see GameObject
	 */
	public abstract void setup();

	/**
	 * When the game begins, this method will automatically be executed every
	 * millisecond
	 * This may be used as a control method for checking user input and
	 * collision between any game objects
	 */
	public abstract void act();

	/**
	 * Sets up the game and any objects
	 * This method should never be called by anything other than a
	 * <code>main</code> method after the frame becomes visible.
	 */
	public void initComponents() {
		getContentPane().setBackground(Color.black);
		setup();
		for (int i = 0; i < _ObjectList.size(); i++) {
			GameObject o = (GameObject) _ObjectList.get(i);
			o.repaint();
		}
		_t.start();
	}

	/**
	 * Adds a game object to the screen
	 * Any added objects will have their <code>act</code> method called every
	 * millisecond
	 * 
	 * @param o
	 *          the <code>GameObject</code> to add.
	 * @see GameObject#act()
	 */
	public void add(GameObject o) {
		_ObjectList.add(o);
		getContentPane().add(o);
	}

	/**
	 * Removes a game object from the screen
	 * 
	 * @param o
	 *          the <code>GameObject</code> to remove
	 * @see GameObject
	 */
	public void remove(GameObject o) {
		_ObjectList.remove(o);
		getContentPane().remove(o);
	}

	public double getAngle(int x1, int y1, int x2, int y2) {
		return Math.atan2(y2 - y1, x2 - x1);
	}

	public double getDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
	}

	/**
	 * Sets the millisecond delay between calls to <code>act</code> methods.
	 * 
	 * Increasisg the delay will make the game run "slower." The default delay
	 * is 1 millisecond.
	 * 
	 * @param delay
	 *              the number of milliseconds between calls to <code>act</code>
	 * @see Game#act()
	 * @see GameObject#act()
	 */
	public void setDelay(int delay) {
		_t.setDelay(delay);
	}

	/**
	 * Sets the background color of the playing field
	 * The default color is black
	 * 
	 * @see java.awt.Color
	 */
	public void setBackground(Color c) {
		getContentPane().setBackground(c);
	}

	/**
	 * The default constructor for the game.
	 * The default window size is 400x400
	 */

	public Game() {
		setSize(getWindowWidth(), getWindowHeight());
		getContentPane().setBackground(Color.black);
		getContentPane().setLayout(null);
		setUndecorated(true);// removes title bar and borders
		setTitle("Polygon");

		// Add window listener.\]
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		_t = new Timer(1, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				act();
				for (int i = 0; i < _ObjectList.size(); i++) {
					GameObject o = (GameObject) _ObjectList.get(i);
					o.act();
				}
			}
		});
		addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				char pressed = Character.toUpperCase(e.getKeyChar());
				switch (pressed) {
					case 'A':
						leftKey = true;
						break;
					case 'D':
						rightKey = true;
						break;
					case 'W':
						upKey = true;
						break;
					case 'S':
						downKey = true;
						break;
					case 27: // escape to close game
						System.exit(0);
					case 'P':
						Polygon.gamePause = true;
						break;
					case 'L':
						Polygon.gamePause = false;
						break;

				}

			}

			public void keyReleased(KeyEvent e) {
				char released = Character.toUpperCase(e.getKeyChar());
				switch (released) {
					case 'A':
						leftKey = false;
						break;
					case 'D':
						rightKey = false;
						break;
					case 'W':
						upKey = false;
						break;
					case 'S':
						downKey = false;
						break;
				}
			}

		});
		addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {

			}

			public void mousePressed(MouseEvent e) {
				if (e.getButton() == 1) {
					Game.this.mouseLeft = true;
				}

			}

			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == 1) {
					Game.this.mouseLeft = false;
				}

			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

		});
		addMouseMotionListener(new MouseMotionListener() {

			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}

			public void mouseDragged(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});

	}

	/**
	 * Starts updates to the game
	 *
	 * The game should automatically start.
	 * 
	 * @see Game#stopGame()
	 */
	public void startGame() {
		_t.start();
	}

	/**
	 * Stops updates to the game
	 *
	 * This can act like a "pause" method
	 * 
	 * @see Game#startGame()
	 */
	public void stopGame() {
		_t.stop();
	}

	/**
	 * Displays a dialog that says "Player 1 Wins!"
	 *
	 */

	/**
	 * Gets the pixel width of the visible playing field
	 * 
	 * @return a width in pixels
	 */
	public int getFieldWidth() {
		return getContentPane().getBounds().width;
	}

	/**
	 * Gets the pixel height of the visible playing field
	 * 
	 * @return a height in pixels
	 */
	public int getFieldHeight() {
		return getContentPane().getBounds().height;
	}

}
