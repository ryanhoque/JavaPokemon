
import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.event.*;


public class SpriteComponent extends JComponent
//This is the component for the little moveable character on the screen.
{
    private int x = 486;
    private BufferedImage sprite;
    private int y = 240;
    private BufferedImage map;
    public SpriteComponent(){
        try{
            sprite = ImageIO.read(new File("Images/Sprite.png"));
            map = ImageIO.read(new File("Images/map.png"));
        }catch(IOException e){}
    }

    public void moveR()
    {
        if(x<900) {
            x += 64;
            repaint();
            Engine.database.findWildPokemon(Game.avl);
        }
    }

    public void moveL()
    {
        if(x>150) {
            x -= 64;
            repaint();
            Engine.database.findWildPokemon(Game.avl);
        }
        else if(x>100 && y>200 && y<270) {
            x-=64;
            repaint();
            int reply = JOptionPane.showConfirmDialog(GUI.frame, "Do you want to battle the trainer?", "Rival Trainer Wants to Fight!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,new ImageIcon("Images/Pokeball.png"));
            if(reply==JOptionPane.YES_OPTION) Engine.game.trainerBattle();
        }
    }

    public void moveU()
    {
        if(y>0 && x>100) {
            y -= 64;
            repaint();
            Engine.database.findWildPokemon(Game.avl);
        }
    }

    public void moveD()
    {
        if(y<450 && x>100) {
            y += 64;
            repaint();
            Engine.database.findWildPokemon(Game.avl);
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        repaint();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(map,0,0,null);
        g.drawImage(sprite,x,y,null);
    }

    public Dimension getPreferredSize() {
        if(sprite==null) return new Dimension(1110,550);
        else return new Dimension(map.getWidth(null),map.getHeight(null));
    }
}