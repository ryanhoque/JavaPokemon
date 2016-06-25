
import javax.swing.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.border.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.io.*;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import java.awt.image.*;


public class Engine
//Has the main method for the program and manages the key listener.
{
    public static Game game = new Game();
    public static GUI gui = new GUI();
    public static Database database = new Database();
    public static void main(String[] args){       
        class KeyPressListener implements KeyListener//inner class KeyPressListener implements interface KeyListener
        {
            public void keyPressed(KeyEvent e)//override keyPressed method
            {
                int x = e.getKeyCode();//assign the value of e.getKeyCode() to x
                if (x==KeyEvent.VK_UP && GUI.isBattling==false) gui.s.moveU();
                if (x==KeyEvent.VK_RIGHT && GUI.isBattling==false) gui.s.moveR();
                if (x==KeyEvent.VK_LEFT && GUI.isBattling==false) gui.s.moveL();
                if (x==KeyEvent.VK_DOWN && GUI.isBattling==false) gui.s.moveD();
                if (x==KeyEvent.VK_A && GUI.isBattling==false) game.switchPokemon();
                if (x==KeyEvent.VK_S && GUI.isBattling==false) game.save();
            }

            public void keyReleased(KeyEvent e)
            {}

            public void keyTyped(KeyEvent e)
            {}
        }
        KeyPressListener kpl = new KeyPressListener();
        gui.frame.addKeyListener(kpl);
        game.run();
    }
}