import java.util.ArrayList;


public class Attack
//An Attack object is a standard attack in a Pokémon game with a name, type, and power. The Attack class also deals with all the complicated type effectiveness.
{
    public String name;
    public String type;
    public int power;
    public ArrayList<String> supereffective = new ArrayList<String>();
    public ArrayList<String> notveryeffective = new ArrayList<String>();
    public ArrayList<String> ineffective = new ArrayList<String>();
    public ArrayList<String> normaleffective = new ArrayList<String>();
    /**
     * Constructor for objects of class Attack
     */
    public Attack(String name, String type, int power)
    {
        this.name = name;
        this.type = type;
        this.power = power;
        if(type.equals("Normal")){ 
            String[] nve = {"Rock", "Steel"};
            String[] ie = {"Ghost"};
            String[] ne = {"Normal", "Fire", "Water", "Grass", "Electric", "Ice", "Fighting", "Poison", "Ground", "Flying", "Psychic", "Bug", "Dragon", "Dark"};
            addToArrayList(notveryeffective,nve);
            addToArrayList(ineffective,ie);
            addToArrayList(normaleffective,ne);}
        else if(type.equals("Fire")){
            String[] se = {"Grass", "Ice", "Bug", "Steel"};
            String[] nve = {"Fire", "Water", "Rock", "Dragon"};
            String[] ne = {"Normal", "Electric", "Fighting", "Poison", "Ground", "Flying", "Psychic", "Ghost", "Dark"};
            addToArrayList(supereffective,se);
            addToArrayList(notveryeffective,nve);
            addToArrayList(normaleffective,ne);
        }
        else if(type.equals("Water")){
            String[] se = {"Fire", "Ground", "Rock"};
            String[] nve = {"Water", "Grass", "Dragon"};
            String[] ne = {"Normal", "Electric", "Ice", "Fighting", "Poison", "Flying", "Psychic", "Bug", "Ghost", "Dark", "Steel"};
            addToArrayList(supereffective,se);
            addToArrayList(notveryeffective,nve);
            addToArrayList(normaleffective,ne);
        }
        else if(type.equals("Grass")){
            String[] se = {"Water", "Ground", "Rock"};
            String[] nve = {"Fire", "Grass", "Poison", "Flying", "Bug", "Dragon", "Steel"};
            String[] ne = {"Normal", "Electric", "Ice", "Fighting", "Psychic", "Ghost", "Dark"};
            addToArrayList(supereffective,se);
            addToArrayList(notveryeffective,nve);
            addToArrayList(normaleffective,ne);
        }
        else if(type.equals("Electric")){
            String[] se = {"Water", "Flying"};
            String[] nve = {"Grass", "Electric", "Dragon"};
            String[] ie = {"Ground"};
            String[] ne = {"Normal", "Fire", "Ice", "Fighting", "Poison", "Psychic", "Bug", "Rock", "Ghost", "Dark", "Steel"};
            addToArrayList(supereffective,se);
            addToArrayList(notveryeffective,nve);
            addToArrayList(ineffective,ie);
            addToArrayList(normaleffective,ne);
        }
        else if(type.equals("Ice")){
            String[] se = {"Grass", "Ground", "Flying", "Dragon"};
            String[] nve = {"Fire", "Water", "Ice", "Steel"};
            String[] ne = {"Normal", "Electric", "Fighting", "Poison", "Psychic", "Bug", "Rock", "Ghost", "Dark"};
            addToArrayList(supereffective,se);
            addToArrayList(notveryeffective,nve);
            addToArrayList(normaleffective,ne);
        } 
        else if(type.equals("Fighting")){
            String[] se = {"Normal", "Ice", "Rock", "Steel"};
            String[] nve = {"Poison", "Flying", "Psychic", "Bug"};
            String[] ie = {"Ghost"};
            String[] ne = {"Fire", "Water", "Grass", "Electric", "Fighting", "Ground", "Dragon", "Dark"};
            addToArrayList(supereffective,se);
            addToArrayList(notveryeffective,nve);
            addToArrayList(ineffective,ie);
            addToArrayList(normaleffective,ne);
        }
        else if(type.equals("Poison")){
            String[] se = {"Grass"};
            String[] nve = {"Poison", "Ground", "Rock", "Ghost"};
            String[] ie = {"Steel"};
            String[] ne = {"Normal", "Fire", "Water", "Electric", "Ice", "Fighting", "Flying", "Psychic", "Bug", "Dragon", "Dark"};
            addToArrayList(supereffective,se);
            addToArrayList(notveryeffective,nve);
            addToArrayList(ineffective,ie);
            addToArrayList(normaleffective,ne);
        }
        else if(type.equals("Ground")){
            String[] se = {"Fire", "Electric", "Poison", "Rock", "Steel"};
            String[] nve = {"Grass", "Bug"};
            String[] ie = {"Flying"};
            String[] ne = {"Normal", "Water", "Ice", "Fighting", "Ground", "Psychic", "Ghost", "Dragon", "Dark"};
            addToArrayList(supereffective,se);
            addToArrayList(notveryeffective,nve);
            addToArrayList(ineffective,ie);
            addToArrayList(normaleffective,ne);
        }
        else if(type.equals("Flying")){
            String[] se = {"Grass", "Fighting", "Bug"};
            String[] nve = {"Electric", "Rock", "Steel"};
            String[] ne = {"Normal", "Fire", "Water", "Ice", "Poison", "Ground", "Flying", "Psychic", "Ghost", "Dragon", "Dark"};
            addToArrayList(supereffective,se);
            addToArrayList(notveryeffective,nve);
            addToArrayList(normaleffective,ne);
        }
        else if(type.equals("Psychic")){
            String[] se = {"Fighting", "Poison"};
            String[] nve = {"Psychic", "Steel"};
            String[] ie = {"Dark"};
            String[] ne = {"Normal", "Fire", "Water", "Grass", "Electric", "Ice", "Ground", "Flying", "Bug", "Rock", "Ghost", "Dragon"};
            addToArrayList(supereffective,se);
            addToArrayList(notveryeffective,nve);
            addToArrayList(ineffective,ie);
            addToArrayList(normaleffective,ne);
        }
        else if(type.equals("Bug")){
            String[] se = {"Grass", "Psychic", "Dark"};
            String[] nve = {"Fire", "Fighting", "Poison", "Flying", "Ghost", "Steel"};
            String[] ne = {"Normal", "Water", "Electric", "Ice", "Ground", "Bug", "Rock", "Dragon"};
            addToArrayList(supereffective,se);
            addToArrayList(notveryeffective,nve);
            addToArrayList(normaleffective,ne);
        }
        else if(type.equals("Rock")){
            String[] se = {"Fire", "Ice", "Flying", "Bug"};
            String[] nve = {"Fighting", "Ground", "Steel"};
            String[] ne = {"Normal", "Water", "Grass", "Electric", "Poison", "Psychic", "Rock", "Ghost", "Dragon", "Dark"};
            addToArrayList(supereffective,se);
            addToArrayList(notveryeffective,nve);
            addToArrayList(normaleffective,ne);
        }
        else if(type.equals("Ghost")){
            String[] se = {"Psychic", "Ghost"};
            String[] nve = {"Dark", "Steel"};
            String[] ie = {"Normal"};
            String[] ne = {"Fire", "Water", "Grass", "Electric", "Ice", "Fighting", "Poison", "Ground", "Flying", "Bug", "Rock", "Dragon"};
            addToArrayList(supereffective,se);
            addToArrayList(notveryeffective,nve);
            addToArrayList(ineffective,ie);
            addToArrayList(normaleffective,ne);
        }
        else if(type.equals("Dragon")){
            String[] se = {"Dragon"};
            String[] nve = {"Steel"};
            String[] ne = {"Normal", "Fire", "Water", "Grass", "Electric", "Ice", "Fighting", "Poison", "Ground", "Flying", "Psychic", "Bug", "Rock", "Ghost", "Dark"};
            addToArrayList(supereffective,se);
            addToArrayList(notveryeffective,nve);
            addToArrayList(normaleffective,ne);
        }
        else if(type.equals("Dark")){
            String[] se = {"Psychic", "Ghost"};
            String[] nve = {"Fighting", "Dark", "Steel"};
            String[] ne = {"Normal", "Fire", "Water", "Grass", "Electric", "Ice", "Poison", "Ground", "Flying", "Bug", "Rock", "Dragon"};
            addToArrayList(supereffective,se);
            addToArrayList(notveryeffective,nve);
            addToArrayList(normaleffective,ne);
        }
        else if(type.equals("Steel")){
            String[] se = {"Ice", "Rock"};
            String[] nve = {"Fire", "Water", "Electric", "Steel"};
            String[] ne = {"Normal", "Grass", "Fighting", "Poison", "Ground", "Flying", "Psychic", "Bug", "Ghost", "Dragon", "Dark"};
            addToArrayList(supereffective,se);
            addToArrayList(notveryeffective,nve);
            addToArrayList(normaleffective,ne);
        }
    }

    public void addToArrayList(ArrayList<String> list, String[] values){
        for(String s: values) list.add(s);
    }

    public String getName(){return name;}

    public String getType() {return type;}

    public int getPower() {return power;}

    private String getAttackEffectiveness1(String defense){//for pokemon with only 1 type
        for(String s: supereffective){
            if(defense.equals(s)) return "super effective";
        }
        for(String s: notveryeffective){
            if(defense.equals(s)) return "not very effective";
        }
        for(String s: ineffective){
            if(defense.equals(s)) return "ineffective";
        }
        for(String s: normaleffective){
            if(defense.equals(s)) return "effective";
        }
        return "ineffective";
    }

    public String getAttackEffectiveness2(String defense1, String defense2){
        if(defense2==null) return getAttackEffectiveness1(defense1);
        boolean supereffective1 = false, supereffective2 = false, notveryeffective1 = false, notveryeffective2 = false, normaleffective1 = false, normaleffective2 = false;
        for(String s: supereffective){
            if(defense1.equals(s)) supereffective1 = true;
        }
        for(String s: ineffective){
            if(defense1.equals(s)) return "ineffective";
        }
        for(String s: notveryeffective){
            if(defense1.equals(s)) notveryeffective1 = true;
        }
        for(String s: normaleffective){
            if(defense1.equals(s)) normaleffective1 = true;
        }
        for(String s: supereffective){
            if(defense2.equals(s)) supereffective2 = true;
        }
        for(String s: ineffective){
            if(defense2.equals(s)) return "ineffective";
        }
        for(String s: notveryeffective){
            if(defense2.equals(s)) notveryeffective2 = true;
        }
        for(String s: normaleffective){
            if(defense2.equals(s)) normaleffective2 = true;
        }
        if(supereffective1&&supereffective2) return "ultra effective";
        else if(supereffective1&&normaleffective2) return "super effective";
        else if(supereffective2&&normaleffective1) return "super effective";
        else if(supereffective1&&notveryeffective2) return "effective";
        else if(supereffective2&&notveryeffective1) return "effective";
        else if(normaleffective1&&normaleffective2) return "effective";
        else if(normaleffective1&&notveryeffective2) return "not very effective";
        else if(normaleffective2&&notveryeffective1) return "not very effective";
        else return "not very effective";
    }

    public String toString(){
        return name+","+type+"/"+power;
    }
}