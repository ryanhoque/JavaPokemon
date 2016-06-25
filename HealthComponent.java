
import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;


public class HealthComponent extends JComponent
//This is the component for the health bars in the battle sequence GUI.
{
    private int x, y, width, height;
    public HealthComponent(){
        x = 40;
        y = 0;
        width = 300;
        height = 30;
    }

    public void loseHealth(int remHealth, int totalHealth){
        width = (int) (((double) remHealth * 300) / (double) totalHealth);
        repaint();
    }

    public void restore(){
        width = 300;
        repaint();
    }

    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.drawRect(x,y,width,height);
        g2.setColor(Color.GREEN);
        if(width<=150) g2.setColor(Color.YELLOW);
        if(width<=30) g2.setColor(Color.RED);
        g2.fillRect(x,y,width,height);
    }

    public Dimension getPreferredSize() {
        return new Dimension(width,height);
    }
}
