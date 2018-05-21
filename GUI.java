//This class manages the graphics of the program and switches between the map display and battle display.
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
public class GUI
{
    public static JFrame frame;
    public static JPanel centerpanel = new JPanel();
    public static JPanel pokemonlist = new JPanel();
    public static JLabel attackimg, phtext, ehtext, pimg, eimg, pname, ename;
    public static HealthComponent ph, eh;
    public static boolean isBattling = false;
    public static final SpriteComponent s = new SpriteComponent();
    public GUI() {
        frame = new JFrame();
        frame.setSize(1150,750);
        frame.setTitle("Pokemon");
        mainDisplay();
    }

    public void mainDisplay(){
        Sound.wildbattle.stop();
        Sound.trainerbattle.stop();
        Sound.victory.stop();
        Sound.death.stop();
        Sound.exploring.play();        
        isBattling = false;
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        centerpanel.removeAll();
        centerpanel.revalidate();
        centerpanel.add(s);
        frame.setFocusable(true);

        pokemonlist.removeAll();
        pokemonlist.revalidate();
        centerpanel.repaint();
        pokemonlist.repaint();
        frame.add(centerpanel, BorderLayout.CENTER);
        pokemonlist.setLayout(new GridLayout(1,6));
        BufferedImage img = new ImgUtils().scaleImage(85,85,"Images/Pokeball.png");
        pokemonlist.add(new JLabel(new ImageIcon(img)));
        frame.add(pokemonlist, BorderLayout.SOUTH);
        Font g = new Font(Font.SANS_SERIF, Font.BOLD, 15);
        centerpanel.setBorder(new TitledBorder(new MatteBorder(15,15,15,15,new ImageIcon(new ImgUtils().scaleImage(15,15,"Images/Pokeball.png"))), "Map",0,0,g,Color.BLUE));
        pokemonlist.setBorder(new TitledBorder(new MatteBorder(15,15,15,15,new ImageIcon(new ImgUtils().scaleImage(15,15,"Images/Pokeball.png"))), "Your Team",0,0,g,Color.BLUE));

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void battleDisplay(Pokemon p, Pokemon e){
        ImgUtils iu = new ImgUtils();
        isBattling = true;
        centerpanel.removeAll();
        centerpanel.revalidate();
        pokemonlist.removeAll();
        pokemonlist.revalidate();
        Font g = new Font(Font.SANS_SERIF, Font.BOLD, 15);
        centerpanel.setBorder(new TitledBorder(new MatteBorder(15,15,15,15,new ImageIcon(iu.scaleImage(15,15,"Images/Pokeball.png"))), "Battle",0,0,g,Color.BLUE));
        pokemonlist.setBorder(new TitledBorder(new MatteBorder(15,15,15,15,new ImageIcon(iu.scaleImage(15,15,"Images/Pokeball.png"))), "Event Log",0,0,g,Color.BLUE));
        centerpanel.repaint();
        pokemonlist.repaint();
        pokemonlist.add(new JLabel(new ImageIcon(iu.scaleImage(275,275,"Images/Pokeball.png"))));
        centerpanel.setLayout(new GridLayout(1,3));
        JPanel playerpokemon = new JPanel();
        attackimg = new JLabel();
        JPanel enemypokemon = new JPanel();
        playerpokemon.setLayout(new BorderLayout());
        enemypokemon.setLayout(new BorderLayout());        
        JPanel phealth = new JPanel();
        phealth.setLayout(new BorderLayout());
        ph = new HealthComponent();
        eh = new HealthComponent();
        phtext = new JLabel("HP: " + p.getHealth() + "/" + p.getHealth());
        phtext.setFont(g);
        pimg = new JLabel(new ImageIcon(iu.strToURL("Images/Pokemon/" + p.getName() + ".png")));
        eimg = new JLabel(new ImageIcon(iu.strToURL("Images/Pokemon/" + e.getName() + ".png")));
        JPanel ehealth = new JPanel();
        ehealth.setLayout(new BorderLayout());
        ehtext = new JLabel("HP: " + e.getHealth() + "/" + e.getHealth());
        ehtext.setFont(g);
        phealth.add(ph, BorderLayout.CENTER);
        phealth.add(phtext, BorderLayout.SOUTH);
        ehealth.add(eh, BorderLayout.CENTER);
        ehealth.add(ehtext, BorderLayout.SOUTH);
        pname = new JLabel(p.getName() + " (Level " + p.getLevel() + ")");
        pname.setFont(g);
        ename = new JLabel(e.getName() + " (Level " + e.getLevel() + ")");
        ename.setFont(g);
        playerpokemon.add(phealth, BorderLayout.NORTH);
        playerpokemon.add(pimg, BorderLayout.CENTER);
        playerpokemon.add(pname, BorderLayout.SOUTH);
        enemypokemon.add(ehealth, BorderLayout.NORTH);
        enemypokemon.add(eimg, BorderLayout.CENTER);
        enemypokemon.add(ename, BorderLayout.SOUTH);
        phtext.setHorizontalAlignment(SwingConstants.CENTER);
        ehtext.setHorizontalAlignment(SwingConstants.CENTER);
        pname.setHorizontalAlignment(SwingConstants.CENTER);
        ename.setHorizontalAlignment(SwingConstants.CENTER);
        centerpanel.add(playerpokemon);
        centerpanel.add(attackimg);
        centerpanel.add(enemypokemon);
    }
}
