import java.util.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.*;


public class Database
//Contains a database of all fifty pokemon in the game.
{
    public static ArrayList<Pokemon> pokemon = new ArrayList<Pokemon>();
    /**
     * Constructor for objects of class Database
     */
    public Database()
    {
        try(BufferedReader buff = new BufferedReader(new FileReader("Database.txt"))){
            for(int i = 0; i < 49; i++){
                String name = buff.readLine();
                int health = Integer.parseInt(buff.readLine());
                int attack = Integer.parseInt(buff.readLine());
                int defense = Integer.parseInt(buff.readLine());
                int speed = Integer.parseInt(buff.readLine());
                int evlevel = Integer.parseInt(buff.readLine());
                int nextevlevel = Integer.parseInt(buff.readLine());
                double catchrate = Double.parseDouble(buff.readLine());
                int appearancerate = Integer.parseInt(buff.readLine());
                String type1 = buff.readLine();
                String type2 = buff.readLine();
                int index = i;
                int evolution = Integer.parseInt(buff.readLine());
                Attack[] basicattacks = new Attack[4];
                for(int j = 0; j < 4; j++){
                    String line = buff.readLine();
                    String atkname = line.substring(0,line.indexOf(","));
                    String type = line.substring(line.indexOf(",")+1,line.indexOf("/"));
                    int power = Integer.parseInt(line.substring(line.indexOf("/")+1));
                    basicattacks[j] = new Attack(atkname, type, power);
                }
                int size = Integer.parseInt(buff.readLine());
                Attack[] learnset = new Attack[size];
                int[] thresholds = new int[size];
                for(int j = 0; j < size; j++){
                    String line = buff.readLine();
                    String atkname = line.substring(0,line.indexOf(","));
                    String type = line.substring(line.indexOf(",")+1,line.indexOf("/"));
                    int power = Integer.parseInt(line.substring(line.indexOf("/")+1));
                    learnset[j] = new Attack(atkname, type, power);
                }
                for(int j = 0; j < size; j++){
                    thresholds[j] = Integer.parseInt(buff.readLine());
                }
                buff.readLine();
                Pokemon p = new Pokemon(name, health, attack, defense, speed, evlevel, nextevlevel, catchrate, appearancerate, type1, type2, evolution, index, basicattacks, learnset, thresholds);
                pokemon.add(p);
            }
        }catch(IOException e){}
        addRayquaza();
    }

    public void addRayquaza(){
        Attack[] basicattacks = {new Attack("Extreme Speed", "Normal", 80), new Attack("Fly", "Flying", 90),
                new Attack("Crunch", "Dark", 80), new Attack("Dragon Pulse", "Dragon", 85)};
        Attack[] learnset = {new Attack("Hyper Beam", "Normal", 150), new Attack("Outrage", "Dragon", 120),
                new Attack("Hurricane", "Flying", 110), new Attack("Aqua Tail", "Water", 90),
                new Attack("Iron Tail", "Steel", 100), new Attack("Earthquake", "Ground", 100),
                new Attack("Blizzard", "Ice", 120), new Attack("Thunder", "Electric", 120),
                new Attack("Solar Beam", "Grass", 120), new Attack("Overheat", "Fire", 140),
                new Attack("Draco Meteor", "Dragon", 130), new Attack("Sky Attack", "Flying", 140)};
        int[] thresholds = {5,10,15,20,25,30,35,40,45,50,55,60};
        pokemon.add(new Pokemon("MegaRayquaza", 210, 360, 200, 230, 1, 101, 5.0, 2, "Dragon", "Flying", 101, 49, basicattacks, learnset, thresholds));
        //BufferedImage img = new ImgUtils().scaleImage(200,200,"Images/Pokemon/MegaRayquaza.png");
        //JOptionPane.showMessageDialog(GUI.frame, "Congratulations!\nSince you have trained a pokemon all the way to level 100,\nthe powerful MegaRayquaza can now be found in the wild!", "New Rare Pokemon", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(img));
    }

    public void findWildPokemon(int averageLevel){
        Random gen = new Random();
        int x = gen.nextInt(7);
        int dlevel = 0;
        switch(x){
            case 0: dlevel-=3; break;
            case 1: dlevel-=2; break;
            case 2: dlevel-=1; break;
            case 4: dlevel+=1; break;
            case 5: dlevel+=2; break;
            case 6: dlevel+=3; break;
            default: break;
        }
        int c = gen.nextInt(100);
        Pokemon p = null;
        if(c<1) 
            p = getRarePokemon(averageLevel+dlevel);
        else if (c<20) p = getCommonPokemon(averageLevel+dlevel);
        if(c<20){
            String file = "Images/Pokemon/" + p.getName() + ".png";
            BufferedImage img = new ImgUtils().scaleImage(75,75,file);
            int reply = JOptionPane.showConfirmDialog(GUI.frame, "You found a wild "+p.getName()+"!\nFight?", "Wild Pokemon Found!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,new ImageIcon(img));
            if(reply==JOptionPane.YES_OPTION) Engine.game.wildBattle(p);
        }
    }

    public ArrayList<Pokemon> getTrainerPokemon(int averageLevel, int teamSize){
        Random gen = new Random();
        ArrayList<Pokemon> enemyteam = new ArrayList<Pokemon>();
        for(int i = 0; i < teamSize; i++){
            int y = gen.nextInt(4);
            int dlevel = 0;
            switch(y){
                case 0: dlevel-=2; break;
                case 1: dlevel-=1; break;
                case 2: dlevel+=1; break;
                default: break;
            }
            int c = gen.nextInt(100);
            boolean highLevel = true;
            Pokemon p = null;
            if(c<10) {
                while(highLevel){//ensure that enemy does not have a overpowered pokemon
                    p = getRarePokemon(averageLevel+dlevel);
                    if(p.getLevel()<=averageLevel+1) highLevel = false;
                }
                enemyteam.add(p);
            }
            else {
                while(highLevel){
                    p = getCommonPokemon(averageLevel+dlevel);
                    if(p.getLevel()<=averageLevel+1) highLevel = false;
                }
                enemyteam.add(p);
            }
        }
        return enemyteam;
    }

    public Pokemon getCommonPokemon(int level)
    {
        ArrayList<Pokemon> common = new ArrayList<Pokemon>();
        for(Pokemon p: pokemon){
            if(p.getAppearanceRate()==1) common.add(p);
        }
        int i = new Random().nextInt(common.size());
        common.get(i).setLevel(level);
        return common.get(i);
    }

    public Pokemon getRarePokemon(int level){//returns a random pokemon with appearancerate of 2
        ArrayList<Pokemon> rare = new ArrayList<Pokemon>();
        for(Pokemon p: pokemon){
            if(p.getAppearanceRate()==2) rare.add(p);
        }
        int i = new Random().nextInt(rare.size());
        rare.get(i).setLevel(level);
        return rare.get(i);
    }
}
