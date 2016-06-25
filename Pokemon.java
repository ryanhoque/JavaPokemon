
import javax.swing.*;
import java.awt.image.BufferedImage;


public class Pokemon
//A Pokémon object simply describes your typical Pokémon, like Pikachu.
{
    public int health, attack, defense, speed, level, xp, evolutionlevel, nextevolutionlevel, lastxp;
    public String name;
    public double catchrate;
    public int appearancerate; //appearancerate is 0 (something above first evolution that never appears) 1 (common) 
    //or 2 (rare - starters and legendaries)
    public String type1, type2;
    public int index; //its position in the database
    public int evolution;//represents another Pokemon in the Pokemon Database with its index
    public int temphealth;
    public Attack[] attacks;
    public Attack[] levelattacks;
    public int[] thresholds;
    public ImageIcon image;
    public Pokemon(String name, int basehealth, int baseattack, int basedefense, int basespeed, int evolutionlevel, 
    int nextevolutionlevel, double catchrate, int appearancerate, String type1, String type2, int evolution, 
    int index, Attack[] startAttacks, Attack[] levelattacks, int[] thresholds){
        this.name = name;
        String imgfilepath = "Images/Pokemon/" + name + ".png";
        image = new ImageIcon(imgfilepath);
        health = basehealth;
        attack = baseattack;
        defense = basedefense;
        speed = basespeed;
        level = evolutionlevel;
        xp = 0;
        lastxp = 0;
        this.evolutionlevel = evolutionlevel;
        this.nextevolutionlevel = nextevolutionlevel;
        this.catchrate = catchrate;
        this.appearancerate = appearancerate;
        this.type1 = type1;
        this.index = index;
        if(type2.equals("null")) this.type2 = null;
        else this.type2 = type2;
        this.evolution = evolution;
        attacks = startAttacks;
        this.levelattacks = levelattacks;
        this.thresholds = thresholds;
    }

    public Pokemon(int index){//to generate a Pokemon from the database that can be manipulated without affecting the database
        this.name = Database.pokemon.get(index).getName();
        this.image = new ImageIcon("Images/" + name + ".png");
        this.evolutionlevel = Database.pokemon.get(index).getEvolutionLevel();
        this.nextevolutionlevel = Database.pokemon.get(index).getNextEvolutionLevel();
        this.catchrate = Database.pokemon.get(index).getCatchRate();
        this.appearancerate = Database.pokemon.get(index).getAppearanceRate();
        this.type1 = Database.pokemon.get(index).getType1();
        this.type2 = Database.pokemon.get(index).getType2();
        this.evolution = Database.pokemon.get(index).getEvolution();
        this.levelattacks = Database.pokemon.get(index).getLevelAttacks();
        this.thresholds = Database.pokemon.get(index).getThresholds();
        this.index = index;
        this.level = Database.pokemon.get(index).getEvolutionLevel();
        xp = 0;
        lastxp = 0;
        this.health = Database.pokemon.get(index).getHealth();
        this.attack = Database.pokemon.get(index).getAttack();
        this.defense = Database.pokemon.get(index).getDefense();
        this.speed = Database.pokemon.get(index).getSpeed();
        this.attacks = Database.pokemon.get(index).getAttacks();
    }

    public Pokemon(int index, int level, int xp, int lastxp, int health, int attack, int defense,
    int speed, Attack[] attacks){//construct a pokemon from a save file with its mutable stats
        this.name = Database.pokemon.get(index).getName();
        this.image = new ImageIcon("Images/" + name + ".png");
        this.evolutionlevel = Database.pokemon.get(index).getEvolutionLevel();
        this.nextevolutionlevel = Database.pokemon.get(index).getNextEvolutionLevel();
        this.catchrate = Database.pokemon.get(index).getCatchRate();
        this.appearancerate = Database.pokemon.get(index).getAppearanceRate();
        this.type1 = Database.pokemon.get(index).getType1();
        this.type2 = Database.pokemon.get(index).getType2();
        this.evolution = Database.pokemon.get(index).getEvolution();
        this.levelattacks = Database.pokemon.get(index).getLevelAttacks();
        this.thresholds = Database.pokemon.get(index).getThresholds();
        this.index = index;
        this.level = level;
        this.xp = xp;
        this.lastxp = lastxp;
        this.health = health;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.attacks = attacks;
    }

    public void gainXP(int xp){
        this.xp += xp;
        if(level<100) JOptionPane.showMessageDialog(GUI.pokemonlist, name + " gained " + xp + " XP!", "", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Images/Pokeball.png"));
        if(level<5){
            if(this.xp-lastxp>=1) levelUp();        
        }
        else if(level<10){
            if(this.xp-lastxp>10) levelUp();
        }
        else if(level<50){
            if(this.xp-lastxp>30) levelUp();
        }
        else if(level<100){
            if(this.xp-lastxp>100) levelUp();
        }
    }

    public void levelUp(){
        lastxp = xp;
        level++;
        GUI.pname.setText(name+" (Level " + level + ")");
        Sound.trainerbattle.stop();
        Sound.victory.play();
        JOptionPane.showMessageDialog(GUI.pokemonlist, "Congratulations!\n" + name + " leveled up to level " + level + "!", name+" leveled up!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Images/Pokeball.png"));
        if(level<32){
            attack+=1;
            defense+=1;
            speed+=1;
            health+=1;
            JOptionPane.showMessageDialog(GUI.pokemonlist, "Attack + 1\nDefense + 1\nSpeed + 1\nHealth + 1", "Stat Changes", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Images/Pokeball.png"));
        }
        else{
            attack+=4;
            defense+=4;
            speed+=4;
            health+=4;
            JOptionPane.showMessageDialog(GUI.pokemonlist, "Attack + 4\nDefense + 4\nSpeed + 4\nHealth + 4", "Stat Changes", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Images/Pokeball.png"));
        }
        for(int i = 0; i < thresholds.length; i++){
            if(thresholds[i]==level) learnAttack(levelattacks[i]);
        }
        if(level==nextevolutionlevel) evolve();
        Sound.victory.stop();
        Sound.trainerbattle.play();
    }

    public void learnAttack(Attack a){
        String[] names = new String[5];
        names[0] = "Replace " + attacks[0].getName() + " (Power: " + attacks[0].getPower() + ", Type: " + attacks[0].getType() + ")";
        names[1] = "Replace " + attacks[1].getName() + " (Power: " + attacks[1].getPower() + ", Type: " + attacks[1].getType() + ")";
        names[2] = "Replace " + attacks[2].getName() + " (Power: " + attacks[2].getPower() + ", Type: " + attacks[2].getType() + ")";
        names[3] = "Replace " + attacks[3].getName() + " (Power: " + attacks[3].getPower() + ", Type: " + attacks[3].getType() + ")";
        names[4] = "Do Not Learn this Attack";
        String choice = (String) JOptionPane.showInputDialog(GUI.pokemonlist, name+" is trying to learn "+a.getName()+"!"+"\nPower: " + a.getPower() 
                + "\nType: " + a.getType(),"New Attack!",JOptionPane.QUESTION_MESSAGE,new ImageIcon("Images/Pokeball.png"),names,names[0]);
        if(names[0].equals(choice)) attacks[0] = a;
        else if(names[1].equals(choice)) attacks[1] = a;
        else if(names[2].equals(choice)) attacks[2] = a;
        else if(names[3].equals(choice)) attacks[3] = a;
        JOptionPane.showMessageDialog(GUI.pokemonlist, name + " learned " + a.getName() + "!", "Success!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Images/Pokeball.png"));
    }

    public void evolve(){
        String tempName = name;
        Pokemon e = Database.pokemon.get(evolution);
        name = e.getName();
        index = e.getIndex();
        image = e.getImage();
        health = e.getHealth();
        attack = e.getAttack();
        defense = e.getDefense();
        speed = e.getSpeed();
        level = e.getEvolutionLevel();
        evolutionlevel = e.getEvolutionLevel();
        nextevolutionlevel = e.getNextEvolutionLevel();
        catchrate = e.getCatchRate();
        appearancerate = e.getAppearanceRate();
        type1 = e.getType1();
        type2 = e.getType2();
        evolution = e.getEvolution();
        levelattacks = e.getLevelAttacks();
        thresholds = e.getThresholds();
        String file = "Images/Pokemon/" + name + ".png";
        BufferedImage img = new ImgUtils().scaleImage(100,100,file);
        GUI.pimg.setIcon(new ImageIcon(file));
        GUI.pname.setText(name + " (Level " + level + ")");
        JOptionPane.showMessageDialog(GUI.pokemonlist, "Congratulations!\n" + tempName + " evolved into " + name + "!", tempName+" evolved!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(img));
    }

    public void increaseCatchRate(int x){
        if(catchrate+x>100) catchrate = 100;
        else catchrate+=x;
    }

    public int getHealth() {return health;}

    public int getAttack() {return attack;}

    public int getDefense() {return defense;}

    public int getSpeed() {return speed;}

    public int getXP() {return xp;}

    public int getLastXP() {return lastxp;}

    public void die() {xp=lastxp;}

    public int getLevel() {return level;}

    public ImageIcon getImage() {return image;}

    public int getIndex() {return index;}

    public void setLevel(int level) {
        if(level>100) this.level = 100;
        else if(level>=evolutionlevel) this.level = level;
        else {
            this.level = evolutionlevel;
        }
        for(int i = evolutionlevel; i < this.level; i++){
            if(i<32){
                attack+=1;
                defense+=1;
                speed+=1;
                health+=1;
            }
            else{
                attack+=4;
                defense+=4;
                speed+=4;
                health+=4;
            }
        }
        int count = 1;
        for(int i = evolutionlevel; i > 1; i--){
            if(count==5) break;
            for(int x = 0; x < thresholds.length; x++){
                if(thresholds[x]==i) {
                    attacks[count] = levelattacks[x];
                    count++;
                }
            }
        }
    }

    public void setTempHealth(int health) {temphealth = health;}

    public int getTempHealth() {return temphealth;}

    public void restoreHealth() {temphealth = health;}

    public int getEvolutionLevel() {return evolutionlevel;}

    public void setName(String name) {this.name = name;}

    public int getNextEvolutionLevel() {return nextevolutionlevel;}

    public String getName() {return name;}

    public double getCatchRate() {return catchrate;}

    public int getAppearanceRate() {return appearancerate;}

    public String getType1() {return type1;}

    public String getType2() {return type2;}

    public boolean hasType2() {
        if(type2==null) return false;
        else return true;
    }

    public int getEvolution() {return evolution;}

    public boolean hasEvolution() {
        if(nextevolutionlevel<101) return true;
        else return false;
    }

    public boolean isFirstEvolution() {
        if(evolutionlevel==1) return true;
        else return false;
    }

    public Attack[] getAttacks() {return attacks;}

    public Attack[] getLevelAttacks() {return levelattacks;}

    public int[] getThresholds() {return thresholds;}

    public String toString(){
        return index + " " + level + " " + xp + " " + lastxp + " " + health + " " + attack + " " + defense + " " + speed + " " + attacks[0] + " " + attacks[1] + " " + attacks[2] + " " + attacks[3];
    }
}
