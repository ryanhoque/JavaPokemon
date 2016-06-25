import java.io.*;
import java.util.*;


public class WriteFile
//This class allows writing to a text file.
{
    private String path;
    private boolean append_to_file = true;
    public WriteFile(){
        path = "SaveFiles.txt";
    }

    public void writeInput(String input){
        try{
            writeToFile(input);
        } catch(IOException e){
            System.out.println("Error: writing to file has failed");
        }
    }

    public void writeToFile(String textLine) throws IOException{
        FileWriter write = new FileWriter(path, append_to_file);
        PrintWriter print_line = new PrintWriter(write);
        print_line.println(textLine);
        print_line.close();
    }
}
