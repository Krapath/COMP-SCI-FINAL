import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.ImageIcon;

public class GameBackground extends JPanel {
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon backgroundOne = new ImageIcon("C:\\Users\\magic\\OneDrive\\Documents\\GitHub\\COMP-SCI-FINAL\\Images\\Background\\Background_One.png");
        backgroundOne.paintIcon(this,g, 0, 0);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
public void setup() {
    }

    public void act() {
    }
}