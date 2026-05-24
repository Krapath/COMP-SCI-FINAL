import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends Polygon implements ActionListener {
    private JFrame frame;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem newMenuItem;
    private JMenuItem saveMenuItem;
    private JMenuItem exitMenuItem;

    public MainMenu(Polygon game) {

    }

    public void setUp() {
        frame = new JFrame();
        // set up
        frame.setTitle("Polygon");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);// smaller than the game window so you can actually see
        frame.setLayout(new BorderLayout(10, 10));

        Font f = new Font("Arial", Font.BOLD, 24);
        UIManager.put("Menu.font", f);
        UIManager.put("MenuItem.font", f);
        UIManager.put("CheckBoxMenuItem.font", f);
        UIManager.put("RadioButtonMenuItem.font", f);
        UIManager.put("Label.foreground", java.awt.Color.WHITE);
        UIManager.put("Button.foreground", java.awt.Color.WHITE);

        menuBar = new JMenuBar();

        fileMenu = new JMenu("File");
        newMenuItem = new JMenuItem("New Game");
        saveMenuItem = new JMenuItem("Save Game");
        exitMenuItem = new JMenuItem("Exit");

        fileMenu.add(newMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        // handle action
    }
}