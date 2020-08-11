import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Renderer extends JPanel{ //creating a jpanel
    private static final long serialVersionUID = 1L; // creates a default

    @Override
    protected void paintComponent(Graphics g) {
       super.paintComponent(g); //super. = calling parent code
       
       Main.clickyBird.repaint(g);  // passing through the Graphics g
    }
    
}
