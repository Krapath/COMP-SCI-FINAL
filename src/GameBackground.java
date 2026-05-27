/* yo this shit is actually so hard idk how the fuck to do it.
import java.awt.Graphics;
import javax.swing.ImageIcon;

public class GameBackground extends GameObject {
    private Polygon game;
    private ImageIcon backgroundImage;

    public GameBackground(Polygon game) {
        this.game = game;
        setLocation(0, 0);
        setSize(game.getWindowWidth(), game.getWindowHeight());
        backgroundImage = new ImageIcon("Images/Background/Background_One.png");
    }

    public void setup() {
    }

    public void act() {
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (backgroundImage != null) {
            backgroundImage.paintIcon(this, g, 0, 0);
        }
    }
}
*/