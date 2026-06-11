
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.util.*;

public class Highscores extends GameObject {

    private static ArrayList<Highscores> highscoresButtons = new ArrayList<>(); // list of all things in the class
    // set variables
    private static final File highscores = new File("highscores.txt");
    private Image boxImage;
    private String buttonName;
    PolygonGame game;
    Font menuFont;
    Font textFont;
    double hoverAngle = 0.2;
    boolean wasHoveredLastFrame = false;
    boolean tiltLeft = true;
    boolean hovered;
    public int[] levelScores = new int[5];
    public int[] killScores = new int[5];
    public static int w, h, x, y;
    public Scanner scan;
    public FileWriter fw;
    public BufferedWriter bw;

    Random r = new Random();

    // set up dummy consturctor for highscores buttons to spawn
    public Highscores(PolygonGame game, String buttonName, int w, int h, int x, int y) {

        this.game = game;
        this.buttonName = buttonName;
        setSize(w, h);
        setLocation(x, y);

        try {
            java.io.File fontFile = new java.io.File("Fonts/ZeroCool.ttf");
            menuFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(((45f)));
            textFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(((100f)));
            
        } catch (Exception e) {
            // Fallback to basic monospaced if the file is missing
            menuFont = new Font("Monospaced", Font.BOLD, 100);
            textFont = new Font("Monospaced", Font.BOLD, 200);
            e.printStackTrace();
        }

    }

    /**
     * scans the scores of the highscores textfile and puts them into levelScores and killScores
     * array
     *
     * pre: existence of text File highscores with 10 intgers, int[] levelScores, int[] killScores and Scanner scan
     * post: scores will be filled with integers from the text file in order.
     * scan will become a scanner with the text file inputted.
     */
    public void scanScores() {
        try {
            scan = new Scanner(highscores); //scanner for scores, based on text file
        } catch (IOException ex) {
            System.out.println("failed");
        }

        for (int i = 0; i < levelScores.length; i++) { //puts all scores into the array.
            levelScores[i] = scan.nextInt();
        }
        
        for (int i = 0; i < killScores.length; i++) { //puts all scores into the array.
            killScores[i] = scan.nextInt();
        }
        
    }

    /**
     * saves the levelScores and killScores  to the text file
     *
     * pre: existence of text File highscores, int[] levelScores, int[] killScores, Filewrite fw and
     * BufferedWriter bw post: scores will be saved to the text file, with each
     * score on a seperate line, starting with levelScores and then killScores
     */
    public void writeScores() {
        try {
            fw = new FileWriter(highscores); //set up for writers
            bw = new BufferedWriter(fw);

        } catch (IOException ex) {
            System.out.println("No file");
        }

        
        
            try {
            	for (int i = 0; i < levelScores.length; i++) { //for every score, write it and jumps to next line
            		bw.write(Integer.toString(levelScores[i]));//need to convert to string manually
            		bw.newLine();
            	}
            	
            	for (int i = 0; i < killScores.length; i++) { //for every score, write it and jumps to next line
                    bw.write(Integer.toString(killScores[i]));//need to convert to string manually to prevent error
                    bw.newLine();
                } 	
            	
            	
                bw.close(); //closes the writers to finish changing the file and save
                fw.close();
            } catch (IOException ex) {
                System.out.println("failed");
            }
    
      
    }

    /**
     * places inputted scores within the array's levelScores and killScores based on greatest to least
     *
     * pre: existence of int[] levelScores, int[] killScores 
     * post: newLevelScore will be inputted into the levelScores array and newKillScore will be inputted into the killScores array, position based on greatest to least
     */
    public void orderScores(int newLevelScore, int newKillScore) {
        int order = -1; //position of newscore in array, -1 means not in array.
        int placeholder; //used to temporary store values.

        for (int i = 0; i < levelScores.length; i++) { //finds the position of the newscore in the array
            if (levelScores[i] < newLevelScore) {
                order = i;
                break;
            }
        }

        for (int i = order; order != -1 && i < levelScores.length; i++) {//moves everything down one.
            placeholder = levelScores[i];
            levelScores[i] = newLevelScore;
            newLevelScore = placeholder;
        }
        
        
        
        order = -1; //position of newscore in array, -1 means not in array.
        placeholder = 0; //used to temporary store values.

        for (int i = 0; i < killScores.length; i++) { //finds the position of the newscore in the array
            if (killScores[i] < newKillScore) {
                order = i;
                break;
            }
        }

        for (int i = order; order != -1 && i < killScores.length; i++) {//moves everything down one.
            placeholder = killScores[i];
            killScores[i] = newKillScore;
            newKillScore = placeholder;
        }
        
        

    }
    
    
    /** creates a new highscores file with the elements of levelScores and killScores
     * 
     * pre: existance of int[] levelScores, int[] killScores
     * post: new highscores.txt file created with 10 scores, each on seperate lines, starting with levelScores and then killScores
     */
    public void createFile () {
    	if (!highscores.exists()) {
    		try {
    			highscores.createNewFile();
    			writeScores();
    		} catch (IOException ex) {
    			System.out.println("failed");
    		}
    	}
    }
    
    
    
    
    

    public void spawnHighscores(PolygonGame game) {
        // sets up width and height for the highscores box based on the window size
        w = (int) (game.getWindowWidth() / 2);
        h = (int) (game.getWindowHeight() / 1.2);

        x = (game.getWindowWidth() - w) / 2; // center the highscores horizontally
        y = (game.getWindowHeight() - h) / 2; // center the highscores vertically

        // make the shift for the back button
        int setShift = (int) ((x + y) / 15);

        // make the frame for the highscores
        Highscores highscores = new Highscores(game, "Highscores", w, h, x, y);
        highscores.setColor(new Color(220, 20, 60));
        game.add(highscores);
        highscoresButtons.add(highscores);

        // make the back button
        Highscores backButton = new Highscores(game, "Back", w / 4, h / 10, x - setShift, y - setShift);
        backButton.setColor(new Color(15, 82, 186));
        game.add(backButton);
        highscoresButtons.add(backButton);

    }

    @Override
    public void paint(Graphics g) {
        if (!PolygonGame.gamePause) {
            return; // only check if we're on the main menu
        }
        super.paint(g); // paints the background first of the button first

        // adds the image on top of the background WILL USED LATER
        if (boxImage != null) {
            g.drawImage(boxImage, 0, 0, getWidth(), getHeight(), null);
        }

        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(menuFont);
        FontMetrics metrics = g2d.getFontMetrics(menuFont);
        hovered = contains(game.getMouseX(), game.getMouseY());

        // center the text in the button
        int textWidth = metrics.stringWidth(buttonName);
        int textHeight = metrics.getAscent();
        int textX = getWidth() / 2 - textWidth / 2;
        int textY = getHeight() / 2 + textHeight / 2 - metrics.getDescent();

        g2d.setColor(Color.BLACK);
        if (hovered && buttonName.equals("Back")) {
            AffineTransform old = g2d.getTransform();
            g2d.rotate(hoverAngle, textX + textWidth / 2.0, textY - textHeight / 2.0);
            g2d.drawString(buttonName, textX, textY);
            g2d.setTransform(old);
        } else if (buttonName.equals("Back")) {
            hoverAngle = 0;
            g2d.drawString(buttonName, textX, textY);
        }


        
        if(!buttonName.equals("Back")) {
            g2d.setFont(textFont);
            g2d.setStroke(new BasicStroke(4));
            
            int wIndex = w/100;
            int hIndex = h/100;
            FontMetrics leaderboard = g2d.getFontMetrics(textFont);
            
        	g.drawString("LEVELS:  KILLS:", 5*wIndex, 25*hIndex);
       

        
        scanScores(); //scans scores to ensure being up to date
        for (int i = 0; i < levelScores.length; i++) { //draws every score
            
        	switch(i){//sets colors for gold, silver and bronze
        	case 0: 
        		 g2d.setColor(new Color(239, 191, 4));
        		 break;
        	case 1:
        		g2d.setColor(new Color(196, 196, 196));
       		 	break;
        	case 2:
        		g2d.setColor(new Color(206, 137, 70));
       		 	break;
       		default:
       			g2d.setColor(Color.BLACK);
       		 	break;
        	}
        	g.drawString(i+1 + ". ", 10*wIndex, 35*hIndex + i * 10*hIndex); //print order
        	g.drawString(i+1 + ". ", 60*wIndex, 35*hIndex + i * 10*hIndex); //print order
            
            g2d.setColor(Color.WHITE);//resets colour to cyan for actual score

            g.drawString(Integer.toString(levelScores[i]), 10*wIndex+150, 35*hIndex + i * 10*hIndex); //prints score
            g.drawString(Integer.toString(killScores[i]), 60*wIndex+150, 35*hIndex + i * 10*hIndex); //prints score
        }
        
        }


    }



    public void act() {
        if (!PolygonGame.gamePause) {
            return; // only check for button clicks if we're on the main menu
        }
        int mouseX = game.getMouseX();
        int mouseY = game.getMouseY();
        // ensure that clicking works properly

        if (hovered && !wasHoveredLastFrame) {
        	SoundEffects("SFX/HOVER.wav",-5.0f);

            if (tiltLeft) {
                hoverAngle = r.nextDouble() * 0.05 + 0.1;
                tiltLeft = false;
            } else if (!tiltLeft) {
                hoverAngle = -(r.nextDouble() * 0.05 + 0.1);
                tiltLeft = true;
            }
        }

        if (hovered && buttonName.equals("Back")) {
            setColor(Color.BLUE);
        } else if (buttonName.equals("Back")) {
            setColor(new Color(15, 82, 186));
        }

        wasHoveredLastFrame = hovered;



        if (isClickedAndReleased(game, mouseX, mouseY)) {
        	SoundEffects("SFX/CLICK.wav",5.0f);

            if (buttonName.equals("Back")) {
                for (Highscores m : highscoresButtons) { // removes all the buttons in the list from game
                    game.remove(m);
                }
                highscoresButtons.clear(); // clears the entire list
                // returns to the main menu
                MainMenu menuController = new MainMenu(game, "");
                menuController.spawnMyBoxes(game);
                add(menuController);
            }
 
        }

    }

}
