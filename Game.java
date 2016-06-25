//This class actually runs the game and therefore has the most code.
import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import java.io.*;
public class Game
{
    public ArrayList<Pokemon> team = new ArrayList<Pokemon>();
    public static int avl = 5;
    public String savefile = null;
    public void run(){
        ImageIcon icon = new ImageIcon("Images/Pokeball.png");
        JOptionPane.showMessageDialog(GUI.frame, "Welcome to the game of Java Pokemon!\nCopyright Ryan Hoque 2015", "Welcome!", JOptionPane.INFORMATION_MESSAGE, icon);
        JOptionPane.showMessageDialog(GUI.frame, "Move with the arrow keys.\nMove in the grass to find wild pokemon.\nMove to the left clearing to battle a trainer.\nOnly wild pokemon can be caught.\nOnly trainer battles give experience.\nPress A to change the order of your team.\nPress S to save the game.", "Instructions", JOptionPane.INFORMATION_MESSAGE, icon);
        String[] c = new String[2];
        c[0] = "Load";
        c[1] = "New Game";
        String ch = (String) JOptionPane.showInputDialog(GUI.frame, "Do you want to load a save file or start a new game?", "", JOptionPane.QUESTION_MESSAGE, icon, c, c[1]);
        if(c[1].equals(ch) || ch==null){
            String[] choices = new String[3];
            choices[0] = "Bulbasaur, the Grass Pokemon";
            choices[1] = "Charmander, the Fire Pokemon";
            choices[2] = "Squirtle, the Water Pokemon";
            String choice = (String) JOptionPane.showInputDialog(GUI.frame, "Which starter pokemon would you like?","Choose a Starter",JOptionPane.QUESTION_MESSAGE,icon,choices,choices[0]);
            switch(choice){
                case "Bulbasaur, the Grass Pokemon": team.add(new Pokemon(0)); break;
                case "Charmander, the Fire Pokemon": team.add(new Pokemon(3)); break;
                case "Squirtle, the Water Pokemon": team.add(new Pokemon(6)); break;
                default:break;
            }
            team.get(0).setLevel(5);
            BufferedImage img = new ImgUtils().scaleImage(85,85,"Images/Pokemon/"+team.get(0).getName()+".png");
            JOptionPane.showMessageDialog(GUI.frame, "Congratulations! You got a new " + team.get(0).getName() + "!", "New Team Member!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(img));
            GUI.pokemonlist.removeAll();
            GUI.pokemonlist.add(new JLabel(new ImageIcon(img)));
            GUI.pokemonlist.revalidate();
        }
        else{
            boolean success = false;
            while(!success){
                success = load();
            }
        }
    }

    public void save(){
        WriteFile writer = new WriteFile();
        if(savefile==null) {
            savefile = JOptionPane.showInputDialog(GUI.frame, "What is your name?");
            JOptionPane.showMessageDialog(GUI.frame, "Your data has been saved.");
        }
        else{
            JOptionPane.showMessageDialog(GUI.frame, "Your data has been saved.");
        }
        writer.writeInput(savefile);
        writer.writeInput(team.size()+"");
        for(int i = 0; i < team.size(); i++){
            Pokemon p = team.get(i);
            writer.writeInput(p.getIndex()+"");
            writer.writeInput(p.getLevel()+"");
            writer.writeInput(p.getXP()+"");
            writer.writeInput(p.getLastXP()+"");
            writer.writeInput(p.getHealth()+"");
            writer.writeInput(p.getAttack()+"");
            writer.writeInput(p.getDefense()+"");
            writer.writeInput(p.getSpeed()+"");
            writer.writeInput(p.getAttacks()[0].toString());
            writer.writeInput(p.getAttacks()[1].toString());
            writer.writeInput(p.getAttacks()[2].toString());
            writer.writeInput(p.getAttacks()[3].toString());
        }
        writer.writeInput("");
    }

    public boolean load(){
        String name = JOptionPane.showInputDialog(GUI.frame, "Please enter your name.");
        String line2;
        try(BufferedReader in = new BufferedReader(new FileReader("SaveFiles.txt"))){
            while((line2=in.readLine())!=null){
                if(line2.equals(name)){
                    team.clear();
                    int size = Integer.parseInt(in.readLine());
                    for(int i = 0; i < size; i++){
                        int index = Integer.parseInt(in.readLine());
                        int level = Integer.parseInt(in.readLine());
                        int xp = Integer.parseInt(in.readLine());
                        int lastxp = Integer.parseInt(in.readLine());
                        int health = Integer.parseInt(in.readLine());
                        int attack = Integer.parseInt(in.readLine());
                        int defense = Integer.parseInt(in.readLine());
                        int speed = Integer.parseInt(in.readLine());
                        Attack[] attacks = new Attack[4];
                        for(int j = 0; j < 4; j++){
                            String line = in.readLine();
                            String atkname = line.substring(0,line.indexOf(","));
                            String type = line.substring(line.indexOf(",")+1,line.indexOf("/"));
                            int power = Integer.parseInt(line.substring(line.indexOf("/")+1));
                            attacks[j] = new Attack(atkname, type, power);
                        }
                        team.add(new Pokemon(index,level,xp,lastxp,health,attack,defense,speed,attacks));
                    }
                }
            }
        }catch(IOException e){}
        if(team.size()==0) {
            JOptionPane.showMessageDialog(GUI.frame, "There is no save file with this name. \nPlease try again or restart the program to start a new game.");
            return false;
        }
        else {
            JOptionPane.showMessageDialog(GUI.frame, "The file was successfully loaded.");
            savefile = name;
            GUI.pokemonlist.removeAll();
            for(Pokemon po: team){
                BufferedImage img = new ImgUtils().scaleImage(85,85,"Images/Pokemon/"+po.getName()+".png");
                GUI.pokemonlist.add(new JLabel(new ImageIcon(img)));
            }
            GUI.pokemonlist.revalidate();
            int sum = 0;
            for(Pokemon po: team) sum+=po.getLevel();
            avl = sum/team.size();
            return true;
        }
    }

    public void wildBattle(Pokemon p){
        Sound.exploring.stop();
        Sound.wildbattle.play();
        ArrayList<Pokemon> tempTeam = new ArrayList<Pokemon>();
        for(int i = 0; i < team.size(); i++){
            tempTeam.add(team.get(i));
            tempTeam.get(i).setTempHealth(tempTeam.get(i).getHealth());
        }
        Pokemon player = tempTeam.get(0);
        Engine.gui.battleDisplay(player,p);
        int enemyRemHealth = p.getHealth();
        boolean continueBattle = true;
        ImageIcon icon = new ImageIcon("Images/Pokeball.png");
        JOptionPane.showMessageDialog(GUI.frame, "Go " + player.getName() + "!", "", JOptionPane.INFORMATION_MESSAGE, icon);
        while(continueBattle){
            String[] choices = new String[4];
            choices[0] = "Attack";
            choices[1] = "Throw Pokeball";
            choices[2] = "Switch Pokemon";
            choices[3] = "Run";
            String choice = (String) JOptionPane.showInputDialog(GUI.pokemonlist, "What would you like to do?","Your Turn",JOptionPane.QUESTION_MESSAGE,icon,choices,choices[0]);
            if(choices[0].equals(choice)) {
                String[] atkchoice = new String[4];
                Attack[] attacks = player.getAttacks();
                atkchoice[0] = attacks[0].getName() + " (Power: " + attacks[0].getPower() + ", Type: " + attacks[0].getType() + ")";
                atkchoice[1] = attacks[1].getName() + " (Power: " + attacks[1].getPower() + ", Type: " + attacks[1].getType() + ")";
                atkchoice[2] = attacks[2].getName() + " (Power: " + attacks[2].getPower() + ", Type: " + attacks[2].getType() + ")";
                atkchoice[3] = attacks[3].getName() + " (Power: " + attacks[3].getPower() + ", Type: " + attacks[3].getType() + ")";
                String atkc = (String) JOptionPane.showInputDialog(GUI.pokemonlist, "Which attack would you like to use?","Attack Choice",JOptionPane.QUESTION_MESSAGE,icon,atkchoice,atkchoice[0]);
                if(atkc==null) {}
                else{
                    if(player.getSpeed() > p.getSpeed()){
                        //user's turn
                        GUI.attackimg.setIcon(new ImageIcon("Images/Attacks/"+atkc.substring(atkc.indexOf("Type:")+6, atkc.indexOf(")"))+"Attack.png"));
                        int damage = getDamage(player.getLevel(), player.getAttack(), p.getDefense(), Integer.parseInt(atkc.substring(atkc.indexOf("Power:")+7, atkc.indexOf(","))));
                        if(Integer.parseInt(atkc.substring(atkc.indexOf("Power:")+7, atkc.indexOf(",")))==0) damage = 0;
                        else if(damage < 1) damage = 1;
                        String damageConstant = "";
                        if(atkchoice[0].equals(atkc)) damageConstant = attacks[0].getAttackEffectiveness2(p.getType1(), p.getType2());
                        else if(atkchoice[1].equals(atkc)) damageConstant = attacks[1].getAttackEffectiveness2(p.getType1(), p.getType2());
                        else if(atkchoice[2].equals(atkc)) damageConstant = attacks[2].getAttackEffectiveness2(p.getType1(), p.getType2());
                        else if(atkchoice[3].equals(atkc)) damageConstant = attacks[3].getAttackEffectiveness2(p.getType1(), p.getType2());
                        double dmgConstant = 1;
                        String message = "";
                        switch(damageConstant){
                            case "ultra effective": dmgConstant = 4; message = "It's ultra effective!"; break;
                            case "super effective": dmgConstant = 2; message = "It's super effective!"; break;
                            case "not very effective": message = "It's not very effective..."; dmgConstant = 0.5; break;
                            case "ineffective": message = "It doesn't affect " + p.getName() + "..."; dmgConstant = 0; break;
                            default: dmgConstant = 1; break;
                        }
                        damage = (int) (dmgConstant*damage);
                        if(damageConstant.equals("not very effective") && damage==0 && Integer.parseInt(atkc.substring(atkc.indexOf("Power:")+7, atkc.indexOf(",")))!=0) damage = 1;
                        enemyRemHealth -= damage;
                        if(enemyRemHealth < 0) enemyRemHealth = 0;
                        GUI.ehtext.setText("HP: " + enemyRemHealth + "/" + p.getHealth());
                        GUI.eh.loseHealth(enemyRemHealth, p.getHealth());
                        JOptionPane.showMessageDialog(GUI.pokemonlist, player.getName()+" used " + atkc.substring(0,atkc.indexOf("(")-1) + "!\n"+message+"\n" + player.getName() + " did " + damage + " damage.", "", JOptionPane.INFORMATION_MESSAGE, icon);
                        GUI.attackimg.setIcon(null);
                        //enemy's turn
                        if(enemyRemHealth>0){
                            Attack[] enemyAtks = p.getAttacks();
                            int a = new Random().nextInt(4);
                            Attack eatk = enemyAtks[a];
                            GUI.attackimg.setIcon(new ImageIcon("Images/Attacks/"+eatk.getType()+"AttackI.png"));
                            damage = getDamage(p.getLevel(), p.getAttack(), player.getDefense(), eatk.getPower());
                            if(eatk.getPower()==0) damage = 0;
                            else if(damage < 1) damage = 1;
                            damageConstant = eatk.getAttackEffectiveness2(player.getType1(), player.getType2());
                            switch(damageConstant){
                                case "ultra effective": dmgConstant = 4; message = "It's ultra effective!"; break;
                                case "super effective": dmgConstant = 2; message = "It's super effective!"; break;
                                case "not very effective": message = "It's not very effective..."; dmgConstant = 0.5; break;
                                case "ineffective": message = "It doesn't affect " + player.getName() + "..."; dmgConstant = 0; break;
                                default: dmgConstant = 1; break;
                            }
                            damage = (int) (dmgConstant*damage);
                            if(damageConstant.equals("not very effective") && damage==0 && eatk.getPower()!=0 && eatk.getPower()!=0) damage = 1;
                            player.setTempHealth(player.getTempHealth()-damage);
                            if(player.getTempHealth() <= 0) {
                                player.setTempHealth(0);
                            }
                            GUI.phtext.setText("HP: " + player.getTempHealth() + "/" + player.getHealth());
                            GUI.ph.loseHealth(player.getTempHealth(), player.getHealth());
                            JOptionPane.showMessageDialog(GUI.pokemonlist, p.getName()+" used "+ eatk.getName() + "!\n"+message+"\n"+p.getName()+" did " + damage + " damage.", "", JOptionPane.INFORMATION_MESSAGE, icon);
                            GUI.attackimg.setIcon(null);
                            if(player.getTempHealth()==0){
                                tempTeam.remove(player);
                                JOptionPane.showMessageDialog(GUI.pokemonlist, player.getName() + " fainted!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                                if(tempTeam.size()>0) {
                                    //switch pokemon
                                    String[] others = new String[tempTeam.size()];
                                    for(int i = 0; i < tempTeam.size(); i++) others[i] = tempTeam.get(i).getName();
                                    String sw = (String) JOptionPane.showInputDialog(GUI.pokemonlist, "Which pokemon would you like to switch in?","",JOptionPane.QUESTION_MESSAGE,icon,others,others[0]);
                                    for(int i = 0; i < tempTeam.size(); i++) {
                                        if(tempTeam.get(i).getName().equals(sw)) {
                                            player = tempTeam.get(i);
                                            GUI.pimg.setIcon(new ImageIcon("Images/Pokemon/" + sw + ".png"));
                                            GUI.pname.setText(sw + " (Level " + player.getLevel() + ")");
                                            GUI.phtext.setText("HP: " + player.getTempHealth() + "/" + player.getHealth());
                                            GUI.ph.loseHealth(player.getTempHealth(), player.getHealth());
                                            JOptionPane.showMessageDialog(GUI.pokemonlist, "Go " + player.getName() + "!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                                        }
                                    }
                                }
                                else JOptionPane.showMessageDialog(GUI.pokemonlist, "The wild " + p.getName() + " got away!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                            }
                        }
                        else JOptionPane.showMessageDialog(GUI.pokemonlist, "The wild " + p.getName() + " fainted!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                        GUI.attackimg.setIcon(null);
                    }
                    else{//enemy attacks first
                        Attack[] enemyAtks = p.getAttacks();
                        int a = new Random().nextInt(4);
                        Attack eatk = enemyAtks[a];
                        GUI.attackimg.setIcon(new ImageIcon("Images/Attacks/"+eatk.getType()+"AttackI.png"));
                        int damage = getDamage(p.getLevel(), p.getAttack(), player.getDefense(), eatk.getPower());
                        if(eatk.getPower()==0) damage = 0;
                        else if(damage < 1) damage = 1;
                        String damageConstant = eatk.getAttackEffectiveness2(player.getType1(), player.getType2());
                        double dmgConstant = 1;
                        String message = "";
                        switch(damageConstant){
                            case "ultra effective": dmgConstant = 4; message = "It's ultra effective!"; break;
                            case "super effective": dmgConstant = 2; message = "It's super effective!"; break;
                            case "not very effective": message = "It's not very effective..."; dmgConstant = 0.5; break;
                            case "ineffective": message = "It doesn't affect " + player.getName() + "..."; dmgConstant = 0; break;
                            default: dmgConstant = 1; break;
                        }
                        damage = (int) (dmgConstant*damage);
                        if(damageConstant.equals("not very effective") && damage==0 && eatk.getPower()!=0) damage = 1;
                        player.setTempHealth(player.getTempHealth()-damage);
                        if(player.getTempHealth() <= 0) {
                            player.setTempHealth(0);
                            tempTeam.remove(player);
                        }
                        GUI.phtext.setText("HP: " + player.getTempHealth() + "/" + player.getHealth());
                        GUI.ph.loseHealth(player.getTempHealth(), player.getHealth());
                        JOptionPane.showMessageDialog(GUI.pokemonlist, p.getName()+" used "+ eatk.getName() + "!\n"+message+"\n"+p.getName()+" did " + damage + " damage.", "", JOptionPane.INFORMATION_MESSAGE, icon);
                        GUI.attackimg.setIcon(null);
                        if(player.getTempHealth()>0){//user's turn
                            GUI.attackimg.setIcon(new ImageIcon("Images/Attacks/"+atkc.substring(atkc.indexOf("Type:")+6, atkc.indexOf(")"))+"Attack.png"));
                            damage = getDamage(player.getLevel(), player.getAttack(), p.getDefense(), Integer.parseInt(atkc.substring(atkc.indexOf("Power:")+7, atkc.indexOf(","))));
                            if(Integer.parseInt(atkc.substring(atkc.indexOf("Power:")+7, atkc.indexOf(",")))==0) damage = 0;
                            else if(damage < 1) damage = 1;
                            damageConstant = "";
                            if(atkchoice[0].equals(atkc)) damageConstant = attacks[0].getAttackEffectiveness2(p.getType1(), p.getType2());
                            else if(atkchoice[1].equals(atkc)) damageConstant = attacks[1].getAttackEffectiveness2(p.getType1(), p.getType2());
                            else if(atkchoice[2].equals(atkc)) damageConstant = attacks[2].getAttackEffectiveness2(p.getType1(), p.getType2());
                            else if(atkchoice[3].equals(atkc)) damageConstant = attacks[3].getAttackEffectiveness2(p.getType1(), p.getType2());
                            dmgConstant = 1;
                            message = "";
                            switch(damageConstant){
                                case "ultra effective": dmgConstant = 4; message = "It's ultra effective!"; break;
                                case "super effective": dmgConstant = 2; message = "It's super effective!"; break;
                                case "not very effective": message = "It's not very effective..."; dmgConstant = 0.5; break;
                                case "ineffective": message = "It doesn't affect " + p.getName() + "..."; dmgConstant = 0; break;
                                default: dmgConstant = 1; break;
                            }
                            damage = (int) (dmgConstant*damage);
                            if(damageConstant.equals("not very effective") && damage==0 && Integer.parseInt(atkc.substring(atkc.indexOf("Power:")+7, atkc.indexOf(",")))!=0) damage = 1;
                            enemyRemHealth -= damage;
                            if(enemyRemHealth < 0) enemyRemHealth = 0;
                            GUI.ehtext.setText("HP: " + enemyRemHealth + "/" + p.getHealth());
                            GUI.eh.loseHealth(enemyRemHealth, p.getHealth());
                            JOptionPane.showMessageDialog(GUI.pokemonlist, player.getName()+" used " + atkc.substring(0,atkc.indexOf("(")-1) + "!\n"+message+"\n" + player.getName() + " did " + damage + " damage.", "", JOptionPane.INFORMATION_MESSAGE, icon);
                            GUI.attackimg.setIcon(null);
                            if(enemyRemHealth==0) JOptionPane.showMessageDialog(GUI.pokemonlist, "The wild " + p.getName() + " fainted!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                        }
                        else {
                            tempTeam.remove(player);
                            JOptionPane.showMessageDialog(GUI.pokemonlist, player.getName() + " fainted!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                            if(tempTeam.size()>0) {
                                //switch pokemon
                                String[] others = new String[tempTeam.size()];
                                for(int i = 0; i < tempTeam.size(); i++) others[i] = tempTeam.get(i).getName();
                                String sw = (String) JOptionPane.showInputDialog(GUI.pokemonlist, "Which pokemon would you like to switch in?","",JOptionPane.QUESTION_MESSAGE,icon,others,others[0]);
                                for(int i = 0; i < tempTeam.size(); i++) {
                                    if(tempTeam.get(i).getName().equals(sw)) {
                                        player = tempTeam.get(i);
                                        GUI.pimg.setIcon(new ImageIcon("Images/Pokemon/" + sw + ".png"));
                                        GUI.pname.setText(sw + " (Level " + player.getLevel() + ")");
                                        GUI.phtext.setText("HP: " + player.getTempHealth() + "/" + player.getHealth());
                                        GUI.ph.loseHealth(player.getTempHealth(), player.getHealth());
                                        JOptionPane.showMessageDialog(GUI.pokemonlist, "Go " + player.getName() + "!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                                    }
                                }
                            }
                            else JOptionPane.showMessageDialog(GUI.pokemonlist, "The wild " + p.getName() + " got away!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                        }                  
                    }
                    if(tempTeam.size()==0 || enemyRemHealth==0) continueBattle = false;
                }
            }
            else if(choices[1].equals(choice)) {
                GUI.attackimg.setIcon(new ImageIcon(new ImgUtils().scaleImage(100, 100, "Images/Pokeball.png")));
                JOptionPane.showMessageDialog(GUI.pokemonlist, "You threw a Poké Ball!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                double chance = p.getCatchRate();
                if(enemyRemHealth <= p.getHealth()/2) chance*=2;
                else if(enemyRemHealth <=p.getHealth()/10) chance*=4;
                int x = new Random().nextInt(100);
                GUI.attackimg.setIcon(null);
                if (x<=chance) {
                    //caught
                    GUI.eimg.setIcon(icon);
                    Sound.wildbattle.stop();
                    Sound.victory.play();
                    JOptionPane.showMessageDialog(GUI.pokemonlist, "Congratulations! You caught the wild " + p.getName() + "!", "Captured!", JOptionPane.INFORMATION_MESSAGE, icon);
                    team.add(p);
                    if(team.size()==7) {
                        String[] releases = new String[7];
                        for(int i = 0; i < 7; i++) releases[i] = team.get(i).getName();
                        String r = (String) JOptionPane.showInputDialog(GUI.pokemonlist, "Oh no! You have too many Pokemon!\nWhich pokemon do you want to release?","",JOptionPane.QUESTION_MESSAGE,icon,releases,releases[0]);
                        for(int i = 0; i < 7; i++) {
                            if(r.equals(team.get(i).getName())) {
                                team.remove(i);
                                JOptionPane.showMessageDialog(GUI.pokemonlist, "You released " + r + ".\nBye bye, " + r + "!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                                break;
                            }
                        }
                    }
                    int sum = 0;
                    for(Pokemon po: team) sum+=po.getLevel();
                    avl = sum/team.size();
                    continueBattle = false;
                }
                else{
                    JOptionPane.showMessageDialog(GUI.pokemonlist, "Oh no! The wild " + p.getName() + " broke free!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                    Attack[] enemyAtks = p.getAttacks();
                    int a = new Random().nextInt(4);
                    Attack eatk = enemyAtks[a];
                    GUI.attackimg.setIcon(new ImageIcon("Images/Attacks/"+eatk.getType()+"AttackI.png"));
                    int damage = getDamage(p.getLevel(), p.getAttack(), player.getDefense(), eatk.getPower());
                    if(eatk.getPower()==0) damage = 0;
                    else if(damage < 1) damage = 1;
                    String damageConstant = eatk.getAttackEffectiveness2(player.getType1(), player.getType2());
                    double dmgConstant = 1;
                    String message = "";
                    switch(damageConstant){
                        case "ultra effective": dmgConstant = 4; message = "It's ultra effective!"; break;
                        case "super effective": dmgConstant = 2; message = "It's super effective!"; break;
                        case "not very effective": message = "It's not very effective..."; dmgConstant = 0.5; break;
                        case "ineffective": message = "It doesn't affect " + player.getName() + "..."; dmgConstant = 0; break;
                        default: dmgConstant = 1; break;
                    }
                    damage = (int) (dmgConstant*damage);
                    if(damageConstant.equals("not very effective") && damage==0 && eatk.getPower()!=0) damage = 1;
                    player.setTempHealth(player.getTempHealth()-damage);
                    if(player.getTempHealth() <= 0) {
                        player.setTempHealth(0);
                    }
                    GUI.phtext.setText("HP: " + player.getTempHealth() + "/" + player.getHealth());
                    GUI.ph.loseHealth(player.getTempHealth(), player.getHealth());
                    JOptionPane.showMessageDialog(GUI.pokemonlist, p.getName()+" used "+ eatk.getName() + "!\n"+message+"\n"+p.getName()+" did " + damage + " damage.", "", JOptionPane.INFORMATION_MESSAGE, icon);
                    GUI.attackimg.setIcon(null);
                    if(player.getTempHealth()==0){
                        tempTeam.remove(player);
                        JOptionPane.showMessageDialog(GUI.pokemonlist, player.getName() + " fainted!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                        if(tempTeam.size()>0) {
                            //switch pokemon
                            String[] others = new String[tempTeam.size()];
                            for(int i = 0; i < tempTeam.size(); i++) others[i] = tempTeam.get(i).getName();
                            String sw = (String) JOptionPane.showInputDialog(GUI.pokemonlist, "Which pokemon would you like to switch in?","",JOptionPane.QUESTION_MESSAGE,icon,others,others[0]);
                            for(int i = 0; i < tempTeam.size(); i++) {
                                if(tempTeam.get(i).getName().equals(sw)) {
                                    player = tempTeam.get(i);
                                    GUI.pimg.setIcon(new ImageIcon("Images/Pokemon/" + sw + ".png"));
                                    GUI.pname.setText(sw + " (Level " + player.getLevel() + ")");
                                    GUI.phtext.setText("HP: " + player.getTempHealth() + "/" + player.getHealth());
                                    GUI.ph.loseHealth(player.getTempHealth(), player.getHealth());
                                    JOptionPane.showMessageDialog(GUI.pokemonlist, "Go " + player.getName() + "!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                                }
                            }
                        }
                        else {
                            continueBattle = false;
                            JOptionPane.showMessageDialog(GUI.pokemonlist, "The wild " + p.getName() + " got away!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                        }
                    }
                }
            }
            else if(choices[2].equals(choice)) {
                ArrayList<String> others3 = new ArrayList<String>();
                for(int i = 0; i < tempTeam.size(); i++) 
                    if(!player.getName().equals(tempTeam.get(i).getName())) others3.add(tempTeam.get(i).getName());
                String[] others = new String[tempTeam.size()-1];
                others = others3.toArray(others);
                String sw = "";
                if(others.length>0) sw = (String) JOptionPane.showInputDialog(GUI.pokemonlist, "Which pokemon would you like to switch in?","",JOptionPane.QUESTION_MESSAGE,icon,others,others[0]);
                for(int i = 0; i < tempTeam.size(); i++) {
                    if(tempTeam.get(i).getName().equals(sw)) {
                        player = tempTeam.get(i);
                        GUI.pimg.setIcon(new ImageIcon("Images/Pokemon/" + sw + ".png"));
                        GUI.pname.setText(sw + " (Level " + player.getLevel() + ")");
                        GUI.phtext.setText("HP: " + player.getTempHealth() + "/" + player.getHealth());
                        GUI.ph.loseHealth(player.getTempHealth(), player.getHealth());
                        JOptionPane.showMessageDialog(GUI.pokemonlist, "Go " + player.getName() + "!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                    }
                }
                if(tempTeam.size()>1) {
                    //enemy attacks
                    Attack[] enemyAtks = p.getAttacks();
                    int a = new Random().nextInt(4);
                    Attack eatk = enemyAtks[a];
                    GUI.attackimg.setIcon(new ImageIcon("Images/Attacks/"+eatk.getType()+"AttackI.png"));
                    int damage = getDamage(p.getLevel(), p.getAttack(), player.getDefense(), eatk.getPower());
                    if(eatk.getPower()==0) damage = 0;
                    else if(damage < 1) damage = 1;
                    String damageConstant = eatk.getAttackEffectiveness2(player.getType1(), player.getType2());
                    double dmgConstant = 1;
                    String message = "";
                    switch(damageConstant){
                        case "ultra effective": dmgConstant = 4; message = "It's ultra effective!"; break;
                        case "super effective": dmgConstant = 2; message = "It's super effective!"; break;
                        case "not very effective": message = "It's not very effective..."; dmgConstant = 0.5; break;
                        case "ineffective": message = "It doesn't affect " + player.getName() + "..."; dmgConstant = 0; break;
                        default: dmgConstant = 1; break;
                    }
                    damage = (int) (dmgConstant*damage);
                    if(damageConstant.equals("not very effective") && damage==0 && eatk.getPower()!=0) damage = 1;
                    player.setTempHealth(player.getTempHealth()-damage);
                    if(player.getTempHealth() <= 0) {
                        player.setTempHealth(0);
                    }
                    GUI.phtext.setText("HP: " + player.getTempHealth() + "/" + player.getHealth());
                    GUI.ph.loseHealth(player.getTempHealth(), player.getHealth());
                    JOptionPane.showMessageDialog(GUI.pokemonlist, p.getName()+" used "+ eatk.getName() + "!\n"+message+"\n"+p.getName()+" did " + damage + " damage.", "", JOptionPane.INFORMATION_MESSAGE, icon);
                    GUI.attackimg.setIcon(null);
                    if(player.getTempHealth()==0){
                        tempTeam.remove(player);
                        JOptionPane.showMessageDialog(GUI.pokemonlist, player.getName() + " fainted!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                        if(tempTeam.size()>0) {
                            //switch pokemon
                            String[] others2 = new String[tempTeam.size()];
                            for(int i = 0; i < tempTeam.size(); i++) others2[i] = tempTeam.get(i).getName();
                            String sw2 = (String) JOptionPane.showInputDialog(GUI.pokemonlist, "Which pokemon would you like to switch in?","",JOptionPane.QUESTION_MESSAGE,icon,others,others[0]);
                            for(int i = 0; i < tempTeam.size(); i++) {
                                if(tempTeam.get(i).getName().equals(sw2)) {
                                    player = tempTeam.get(i);
                                    GUI.pimg.setIcon(new ImageIcon("Images/Pokemon/" + sw + ".png"));
                                    GUI.pname.setText(sw2 + " (Level " + player.getLevel() + ")");
                                    GUI.phtext.setText("HP: " + player.getTempHealth() + "/" + player.getHealth());
                                    GUI.ph.loseHealth(player.getTempHealth(), player.getHealth());
                                    JOptionPane.showMessageDialog(GUI.pokemonlist, "Go " + player.getName() + "!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                                }
                            }
                        }
                        else {
                            continueBattle = false;
                            JOptionPane.showMessageDialog(GUI.pokemonlist, "The wild " + p.getName() + " got away!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                        }
                    }
                }
                else JOptionPane.showMessageDialog(GUI.pokemonlist, "You have no other team members left!", "", JOptionPane.INFORMATION_MESSAGE, icon);
            }
            else if(choices[3].equals(choice)) {
                JOptionPane.showMessageDialog(GUI.pokemonlist, "Got away safely!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                continueBattle = false;
            }
        }
        Engine.gui.mainDisplay();
        GUI.pokemonlist.removeAll();
        for(Pokemon po: team){
            BufferedImage img = new ImgUtils().scaleImage(85,85,"Images/Pokemon/"+po.getName()+".png");
            GUI.pokemonlist.add(new JLabel(new ImageIcon(img)));
        }
        GUI.pokemonlist.revalidate();

        int sum = 0;
        for(Pokemon po: team) sum+=po.getLevel();
        avl = sum/team.size();
    }

    public void trainerBattle(){
        Sound.exploring.stop();
        Sound.trainerbattle.play();
        ArrayList<Pokemon> tempTeam = new ArrayList<Pokemon>();
        ArrayList<Pokemon> enemyTeam = Engine.database.getTrainerPokemon(avl,team.size());
        Pokemon p = enemyTeam.get(0);
        for(int i = 0; i < team.size(); i++){
            tempTeam.add(team.get(i));
            tempTeam.get(i).setTempHealth(tempTeam.get(i).getHealth());
        }
        Pokemon player = tempTeam.get(0);
        Engine.gui.battleDisplay(player,p);
        int enemyRemHealth = p.getHealth();
        boolean continueBattle = true;
        ImageIcon icon = new ImageIcon("Images/Pokeball.png");
        JOptionPane.showMessageDialog(GUI.frame, "Go " + player.getName() + "!", "", JOptionPane.INFORMATION_MESSAGE, icon);
        JOptionPane.showMessageDialog(GUI.pokemonlist, "The rival trainer has " + team.size() + " pokemon.", "", JOptionPane.INFORMATION_MESSAGE, icon);
        while(continueBattle){
            String[] choices = new String[2];
            choices[0] = "Attack";
            choices[1] = "Switch Pokemon";
            String choice = (String) JOptionPane.showInputDialog(GUI.pokemonlist, "What would you like to do?","Your Turn",JOptionPane.QUESTION_MESSAGE,icon,choices,choices[0]);
            if(choices[0].equals(choice)) {
                String[] atkchoice = new String[4];
                Attack[] attacks = player.getAttacks();
                atkchoice[0] = attacks[0].getName() + " (Power: " + attacks[0].getPower() + ", Type: " + attacks[0].getType() + ")";
                atkchoice[1] = attacks[1].getName() + " (Power: " + attacks[1].getPower() + ", Type: " + attacks[1].getType() + ")";
                atkchoice[2] = attacks[2].getName() + " (Power: " + attacks[2].getPower() + ", Type: " + attacks[2].getType() + ")";
                atkchoice[3] = attacks[3].getName() + " (Power: " + attacks[3].getPower() + ", Type: " + attacks[3].getType() + ")";
                String atkc = (String) JOptionPane.showInputDialog(GUI.pokemonlist, "Which attack would you like to use?","Attack Choice",JOptionPane.QUESTION_MESSAGE,icon,atkchoice,atkchoice[0]);
                if(atkc==null) {}
                else{
                    if(player.getSpeed() > p.getSpeed()){
                        //user's turn
                        GUI.attackimg.setIcon(new ImageIcon("Images/Attacks/"+atkc.substring(atkc.indexOf("Type:")+6, atkc.indexOf(")"))+"Attack.png"));
                        int damage = getDamage(player.getLevel(), player.getAttack(), p.getDefense(), Integer.parseInt(atkc.substring(atkc.indexOf("Power:")+7, atkc.indexOf(","))));
                        if(Integer.parseInt(atkc.substring(atkc.indexOf("Power:")+7, atkc.indexOf(",")))==0) damage = 0;
                        else if(damage < 1) damage = 1;
                        String damageConstant = "";
                        if(atkchoice[0].equals(atkc)) damageConstant = attacks[0].getAttackEffectiveness2(p.getType1(), p.getType2());
                        else if(atkchoice[1].equals(atkc)) damageConstant = attacks[1].getAttackEffectiveness2(p.getType1(), p.getType2());
                        else if(atkchoice[2].equals(atkc)) damageConstant = attacks[2].getAttackEffectiveness2(p.getType1(), p.getType2());
                        else if(atkchoice[3].equals(atkc)) damageConstant = attacks[3].getAttackEffectiveness2(p.getType1(), p.getType2());
                        double dmgConstant = 1;
                        String message = "";
                        switch(damageConstant){
                            case "ultra effective": dmgConstant = 4; message = "It's ultra effective!"; break;
                            case "super effective": dmgConstant = 2; message = "It's super effective!"; break;
                            case "not very effective": message = "It's not very effective..."; dmgConstant = 0.5; break;
                            case "ineffective": message = "It doesn't affect " + p.getName() + "..."; dmgConstant = 0; break;
                            default: dmgConstant = 1; break;
                        }
                        damage = (int) (dmgConstant*damage);
                        if(damageConstant.equals("not very effective") && damage==0 && Integer.parseInt(atkc.substring(atkc.indexOf("Power:")+7, atkc.indexOf(",")))!=0) damage = 1;
                        enemyRemHealth -= damage;
                        if(enemyRemHealth < 0) enemyRemHealth = 0;
                        GUI.ehtext.setText("HP: " + enemyRemHealth + "/" + p.getHealth());
                        GUI.eh.loseHealth(enemyRemHealth, p.getHealth());
                        JOptionPane.showMessageDialog(GUI.pokemonlist, player.getName()+" used " + atkc.substring(0,atkc.indexOf("(")-1) + "!\n"+message+"\n" + player.getName() + " did " + damage + " damage.", "", JOptionPane.INFORMATION_MESSAGE, icon);
                        GUI.attackimg.setIcon(null);
                        //enemy's turn
                        if(enemyRemHealth>0){
                            Attack[] enemyAtks = p.getAttacks();
                            int a = new Random().nextInt(4);
                            Attack eatk = enemyAtks[a];
                            GUI.attackimg.setIcon(new ImageIcon("Images/Attacks/"+eatk.getType()+"AttackI.png"));
                            damage = getDamage(p.getLevel(), p.getAttack(), player.getDefense(), eatk.getPower());
                            if(eatk.getPower()==0) damage = 0;
                            else if(damage < 1) damage = 1;
                            damageConstant = eatk.getAttackEffectiveness2(player.getType1(), player.getType2());
                            switch(damageConstant){
                                case "ultra effective": dmgConstant = 4; message = "It's ultra effective!"; break;
                                case "super effective": dmgConstant = 2; message = "It's super effective!"; break;
                                case "not very effective": message = "It's not very effective..."; dmgConstant = 0.5; break;
                                case "ineffective": message = "It doesn't affect " + player.getName() + "..."; dmgConstant = 0; break;
                                default: dmgConstant = 1; break;
                            }
                            damage = (int) (dmgConstant*damage);
                            if(damageConstant.equals("not very effective") && damage==0 && eatk.getPower()!=0) damage = 1;
                            player.setTempHealth(player.getTempHealth()-damage);
                            if(player.getTempHealth() <= 0) {
                                player.setTempHealth(0);
                            }
                            GUI.phtext.setText("HP: " + player.getTempHealth() + "/" + player.getHealth());
                            GUI.ph.loseHealth(player.getTempHealth(), player.getHealth());
                            JOptionPane.showMessageDialog(GUI.pokemonlist, p.getName()+" used "+ eatk.getName() + "!\n"+message+"\n"+p.getName()+" did " + damage + " damage.", "", JOptionPane.INFORMATION_MESSAGE, icon);
                            GUI.attackimg.setIcon(null);
                            if(player.getTempHealth()==0){
                                tempTeam.remove(player);
                                JOptionPane.showMessageDialog(GUI.pokemonlist, player.getName() + " fainted!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                                if(tempTeam.size()>0) {
                                    //switch pokemon
                                    String[] others = new String[tempTeam.size()];
                                    for(int i = 0; i < tempTeam.size(); i++) others[i] = tempTeam.get(i).getName();
                                    String sw = (String) JOptionPane.showInputDialog(GUI.pokemonlist, "Which pokemon would you like to switch in?","",JOptionPane.QUESTION_MESSAGE,icon,others,others[0]);
                                    for(int i = 0; i < tempTeam.size(); i++) {
                                        if(tempTeam.get(i).getName().equals(sw)) {
                                            player = tempTeam.get(i);
                                            GUI.pimg.setIcon(new ImageIcon("Images/Pokemon/" + sw + ".png"));
                                            GUI.pname.setText(sw + " (Level " + player.getLevel() + ")");
                                            GUI.phtext.setText("HP: " + player.getTempHealth() + "/" + player.getHealth());
                                            GUI.ph.loseHealth(player.getTempHealth(), player.getHealth());
                                            JOptionPane.showMessageDialog(GUI.pokemonlist, "Go " + player.getName() + "!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                                        }
                                    }
                                }
                                else {
                                    Sound.trainerbattle.stop();
                                    Sound.death.play();
                                    JOptionPane.showMessageDialog(GUI.pokemonlist, "You lost.", "", JOptionPane.INFORMATION_MESSAGE, icon);
                                }
                            }
                        }
                        else {
                            JOptionPane.showMessageDialog(GUI.pokemonlist, p.getName() + " fainted!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                            player.gainXP(p.getLevel());
                            if(enemyTeam.size()==1){
                                enemyTeam.remove(p);
                                Sound.trainerbattle.stop();
                                Sound.victory.play();
                                JOptionPane.showMessageDialog(GUI.pokemonlist, "Congratulations! You defeated the enemy trainer!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                                continueBattle = false;
                            }
                            else{
                                enemyTeam.remove(p);
                                p = enemyTeam.get(0);
                                enemyRemHealth = p.getHealth();
                                GUI.eimg.setIcon(new ImageIcon("Images/Pokemon/" + p.getName() + ".png"));
                                GUI.ename.setText(p.getName() + " (Level " + p.getLevel() + ")");
                                GUI.ehtext.setText("HP: " + enemyRemHealth + "/" + p.getHealth());
                                GUI.eh.loseHealth(enemyRemHealth, p.getHealth());
                                JOptionPane.showMessageDialog(GUI.pokemonlist, "The rival trainer sent out " + p.getName() + "!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                                JOptionPane.showMessageDialog(GUI.pokemonlist, "The rival trainer has " + enemyTeam.size() + " pokemon left.", "", JOptionPane.INFORMATION_MESSAGE, icon);
                            }
                        }
                        GUI.attackimg.setIcon(null);
                    }
                    else{
                        Attack[] enemyAtks = p.getAttacks();
                        int a = new Random().nextInt(4);
                        Attack eatk = enemyAtks[a];
                        GUI.attackimg.setIcon(new ImageIcon("Images/Attacks/"+eatk.getType()+"AttackI.png"));
                        int damage = getDamage(p.getLevel(), p.getAttack(), player.getDefense(), eatk.getPower());
                        if(eatk.getPower()==0) damage = 0;
                        else if(damage < 1) damage = 1;
                        String damageConstant = eatk.getAttackEffectiveness2(player.getType1(), player.getType2());
                        double dmgConstant = 1;
                        String message = "";
                        switch(damageConstant){
                            case "ultra effective": dmgConstant = 4; message = "It's ultra effective!"; break;
                            case "super effective": dmgConstant = 2; message = "It's super effective!"; break;
                            case "not very effective": message = "It's not very effective..."; dmgConstant = 0.5; break;
                            case "ineffective": message = "It doesn't affect " + player.getName() + "..."; dmgConstant = 0; break;
                            default: dmgConstant = 1; break;
                        }
                        damage = (int) (dmgConstant*damage);
                        if(damageConstant.equals("not very effective") && damage==0 && eatk.getPower()!=0) damage = 1;
                        player.setTempHealth(player.getTempHealth()-damage);
                        if(player.getTempHealth() <= 0) {
                            player.setTempHealth(0);
                            tempTeam.remove(player);
                        }
                        GUI.phtext.setText("HP: " + player.getTempHealth() + "/" + player.getHealth());
                        GUI.ph.loseHealth(player.getTempHealth(), player.getHealth());
                        JOptionPane.showMessageDialog(GUI.pokemonlist, p.getName()+" used "+ eatk.getName() + "!\n"+message+"\n"+p.getName()+" did " + damage + " damage.", "", JOptionPane.INFORMATION_MESSAGE, icon);
                        GUI.attackimg.setIcon(null);
                        if(player.getTempHealth()>0){//user's turn
                            GUI.attackimg.setIcon(new ImageIcon("Images/Attacks/"+atkc.substring(atkc.indexOf("Type:")+6, atkc.indexOf(")"))+"Attack.png"));
                            damage = getDamage(player.getLevel(), player.getAttack(), p.getDefense(), Integer.parseInt(atkc.substring(atkc.indexOf("Power:")+7, atkc.indexOf(","))));
                            if(Integer.parseInt(atkc.substring(atkc.indexOf("Power:")+7, atkc.indexOf(",")))==0) damage = 0;
                            else if(damage < 1) damage = 1;
                            damageConstant = "";
                            if(atkchoice[0].equals(atkc)) damageConstant = attacks[0].getAttackEffectiveness2(p.getType1(), p.getType2());
                            else if(atkchoice[1].equals(atkc)) damageConstant = attacks[1].getAttackEffectiveness2(p.getType1(), p.getType2());
                            else if(atkchoice[2].equals(atkc)) damageConstant = attacks[2].getAttackEffectiveness2(p.getType1(), p.getType2());
                            else if(atkchoice[3].equals(atkc)) damageConstant = attacks[3].getAttackEffectiveness2(p.getType1(), p.getType2());
                            dmgConstant = 1;
                            message = "";
                            switch(damageConstant){
                                case "ultra effective": dmgConstant = 4; message = "It's ultra effective!"; break;
                                case "super effective": dmgConstant = 2; message = "It's super effective!"; break;
                                case "not very effective": message = "It's not very effective..."; dmgConstant = 0.5; break;
                                case "ineffective": message = "It doesn't affect " + p.getName() + "..."; dmgConstant = 0; break;
                                default: dmgConstant = 1; break;
                            }
                            damage = (int) (dmgConstant*damage);
                            if(damageConstant.equals("not very effective") && damage==0 && Integer.parseInt(atkc.substring(atkc.indexOf("Power:")+7, atkc.indexOf(",")))!=0) damage = 1;
                            enemyRemHealth -= damage;
                            if(enemyRemHealth < 0) enemyRemHealth = 0;
                            GUI.ehtext.setText("HP: " + enemyRemHealth + "/" + p.getHealth());
                            GUI.eh.loseHealth(enemyRemHealth, p.getHealth());
                            JOptionPane.showMessageDialog(GUI.pokemonlist, player.getName()+" used " + atkc.substring(0,atkc.indexOf("(")-1) + "!\n"+message+"\n" + player.getName() + " did " + damage + " damage.", "", JOptionPane.INFORMATION_MESSAGE, icon);
                            GUI.attackimg.setIcon(null);
                            if(enemyRemHealth==0) {
                                JOptionPane.showMessageDialog(GUI.pokemonlist, p.getName() + " fainted!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                                player.gainXP(p.getLevel());
                                if(enemyTeam.size()==1){
                                    enemyTeam.remove(p);
                                    Sound.trainerbattle.stop();
                                    Sound.victory.play();
                                    JOptionPane.showMessageDialog(GUI.pokemonlist, "Congratulations! You defeated the enemy trainer!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                                    continueBattle = false;
                                }
                                else{
                                    enemyTeam.remove(p);
                                    p = enemyTeam.get(0);
                                    enemyRemHealth = p.getHealth();
                                    GUI.eimg.setIcon(new ImageIcon("Images/Pokemon/" + p.getName() + ".png"));
                                    GUI.ename.setText(p.getName() + " (Level " + p.getLevel() + ")");
                                    GUI.ehtext.setText("HP: " + enemyRemHealth + "/" + p.getHealth());
                                    GUI.eh.loseHealth(enemyRemHealth, p.getHealth());
                                    JOptionPane.showMessageDialog(GUI.pokemonlist, "The rival trainer sent out " + p.getName() + "!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                                    JOptionPane.showMessageDialog(GUI.pokemonlist, "The rival trainer has " + enemyTeam.size() + " pokemon left.", "", JOptionPane.INFORMATION_MESSAGE, icon);
                                }
                            }
                        }
                        else {
                            tempTeam.remove(player);
                            JOptionPane.showMessageDialog(GUI.pokemonlist, player.getName() + " fainted!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                            if(tempTeam.size()>0) {
                                //switch pokemon
                                String[] others = new String[tempTeam.size()];
                                for(int i = 0; i < tempTeam.size(); i++) others[i] = tempTeam.get(i).getName();
                                String sw = (String) JOptionPane.showInputDialog(GUI.pokemonlist, "Which pokemon would you like to switch in?","",JOptionPane.QUESTION_MESSAGE,icon,others,others[0]);
                                for(int i = 0; i < tempTeam.size(); i++) {
                                    if(tempTeam.get(i).getName().equals(sw)) {
                                        player = tempTeam.get(i);
                                        GUI.pimg.setIcon(new ImageIcon("Images/Pokemon/" + sw + ".png"));
                                        GUI.pname.setText(sw + " (Level " + player.getLevel() + ")");
                                        GUI.phtext.setText("HP: " + player.getTempHealth() + "/" + player.getHealth());
                                        GUI.ph.loseHealth(player.getTempHealth(), player.getHealth());
                                        JOptionPane.showMessageDialog(GUI.pokemonlist, "Go " + player.getName() + "!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                                    }
                                }
                            }
                            else {
                                Sound.trainerbattle.stop();
                                Sound.death.play();
                                JOptionPane.showMessageDialog(GUI.pokemonlist, "You lost.", "", JOptionPane.INFORMATION_MESSAGE, icon);
                            }
                        }                  
                    }
                    if(tempTeam.size()==0 || enemyTeam.size()==0) continueBattle = false;
                }
            }
            else if(choices[1].equals(choice)) {
                ArrayList<String> others3 = new ArrayList<String>();
                for(int i = 0; i < tempTeam.size(); i++) 
                    if(!player.getName().equals(tempTeam.get(i).getName())) others3.add(tempTeam.get(i).getName());
                String[] others = new String[tempTeam.size()-1];
                others = others3.toArray(others);
                String sw = "";
                if(others.length>0) sw = (String) JOptionPane.showInputDialog(GUI.pokemonlist, "Which pokemon would you like to switch in?","",JOptionPane.QUESTION_MESSAGE,icon,others,others[0]);
                for(int i = 0; i < tempTeam.size(); i++) {
                    if(tempTeam.get(i).getName().equals(sw)) {
                        player = tempTeam.get(i);
                        GUI.pimg.setIcon(new ImageIcon("Images/Pokemon/" + sw + ".png"));
                        GUI.pname.setText(sw + " (Level " + player.getLevel() + ")");
                        GUI.phtext.setText("HP: " + player.getTempHealth() + "/" + player.getHealth());
                        GUI.ph.loseHealth(player.getTempHealth(), player.getHealth());
                    }
                }
                if(tempTeam.size()>1) {
                    JOptionPane.showMessageDialog(GUI.pokemonlist, "Go " + sw + "!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                    //enemy attacks
                    Attack[] enemyAtks = p.getAttacks();
                    int a = new Random().nextInt(4);
                    Attack eatk = enemyAtks[a];
                    GUI.attackimg.setIcon(new ImageIcon("Images/Attacks/"+eatk.getType()+"AttackI.png"));
                    int damage = getDamage(p.getLevel(), p.getAttack(), player.getDefense(), eatk.getPower());
                    if(eatk.getPower()==0) damage = 0;
                    else if(damage < 1) damage = 1;
                    String damageConstant = eatk.getAttackEffectiveness2(player.getType1(), player.getType2());
                    double dmgConstant = 1;
                    String message = "";
                    switch(damageConstant){
                        case "ultra effective": dmgConstant = 4; message = "It's ultra effective!"; break;
                        case "super effective": dmgConstant = 2; message = "It's super effective!"; break;
                        case "not very effective": message = "It's not very effective..."; dmgConstant = 0.5; break;
                        case "ineffective": message = "It doesn't affect " + player.getName() + "..."; dmgConstant = 0; break;
                        default: dmgConstant = 1; break;
                    }
                    damage = (int) (dmgConstant*damage);
                    if(damageConstant.equals("not very effective") && damage==0 && eatk.getPower()!=0) damage = 1;
                    player.setTempHealth(player.getTempHealth()-damage);
                    if(player.getTempHealth() <= 0) {
                        player.setTempHealth(0);
                    }
                    GUI.phtext.setText("HP: " + player.getTempHealth() + "/" + player.getHealth());
                    GUI.ph.loseHealth(player.getTempHealth(), player.getHealth());
                    JOptionPane.showMessageDialog(GUI.pokemonlist, p.getName()+" used "+ eatk.getName() + "!\n"+message+"\n"+p.getName()+" did " + damage + " damage.", "", JOptionPane.INFORMATION_MESSAGE, icon);
                    GUI.attackimg.setIcon(null);
                    if(player.getTempHealth()==0){
                        tempTeam.remove(player);
                        JOptionPane.showMessageDialog(GUI.pokemonlist, player.getName() + " fainted!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                        if(tempTeam.size()>0) {
                            //switch pokemon
                            String[] others2 = new String[tempTeam.size()];
                            for(int i = 0; i < tempTeam.size(); i++) others2[i] = tempTeam.get(i).getName();
                            String sw2 = (String) JOptionPane.showInputDialog(GUI.pokemonlist, "Which pokemon would you like to switch in?","",JOptionPane.QUESTION_MESSAGE,icon,others,others[0]);
                            for(int i = 0; i < tempTeam.size(); i++) {
                                if(tempTeam.get(i).getName().equals(sw2)) {
                                    player = tempTeam.get(i);
                                    GUI.pimg.setIcon(new ImageIcon("Images/Pokemon/" + sw + ".png"));
                                    GUI.pname.setText(sw2 + " (Level " + player.getLevel() + ")");
                                    GUI.phtext.setText("HP: " + player.getTempHealth() + "/" + player.getHealth());
                                    GUI.ph.loseHealth(player.getTempHealth(), player.getHealth());
                                    JOptionPane.showMessageDialog(GUI.pokemonlist, "Go " + player.getName() + "!", "", JOptionPane.INFORMATION_MESSAGE, icon);
                                }
                            }
                        }
                        else {
                            continueBattle = false;
                            Sound.trainerbattle.stop();
                            Sound.death.play();
                            JOptionPane.showMessageDialog(GUI.pokemonlist, "You lost.", "", JOptionPane.INFORMATION_MESSAGE, icon);  
                        }
                    }
                }
                else JOptionPane.showMessageDialog(GUI.pokemonlist, "You have no other team members left!", "", JOptionPane.INFORMATION_MESSAGE, icon);
            }
        }
        Engine.gui.mainDisplay();
        GUI.pokemonlist.removeAll();
        for(Pokemon po: team){
            BufferedImage img = new ImgUtils().scaleImage(85,85,"Images/Pokemon/"+po.getName()+".png");
            GUI.pokemonlist.add(new JLabel(new ImageIcon(img)));
        }
        GUI.pokemonlist.revalidate();

        int sum = 0;
        for(Pokemon po: team) sum+=po.getLevel();
        avl = sum/team.size();
    }

    public int getDamage(int level, int attack, int defense, int power){
        return (int) ((2*level+10)/250.0 * attack/(double)defense * power + 2);
    }

    public void switchPokemon(){
        String[] others = new String[team.size()-1];
        for(int i = 0; i < team.size()-1; i++) others[i] = team.get(i+1).getName();
        String sw = "";
        if(others.length>0) sw = (String) JOptionPane.showInputDialog(GUI.frame, "Which pokemon would you like to switch into first place?","",JOptionPane.QUESTION_MESSAGE,new ImageIcon("Images/Pokeball.png"),others,others[0]);
        for(int i = 1; i < team.size(); i++) {
            if(team.get(i).getName().equals(sw)) {
                Pokemon temp = team.get(i);
                team.set(i,team.get(0));
                team.set(0,temp);
            }
        }
        GUI.pokemonlist.removeAll();
        for(Pokemon po: team){
            BufferedImage img = new ImgUtils().scaleImage(85,85,"Images/Pokemon/"+po.getName()+".png");
            GUI.pokemonlist.add(new JLabel(new ImageIcon(img)));
        }
        GUI.pokemonlist.revalidate();
    }
}