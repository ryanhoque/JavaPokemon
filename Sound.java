import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import java.applet.Applet;
import java.applet.AudioClip;


public class Sound
//Contains all the sound files for the game.
{
    public static final Sound wildbattle = new Sound("/wildbattle.wav");
    public static final Sound trainerbattle = new Sound("/trainerbattle.wav");
    public static final Sound exploring = new Sound("/exploring.wav");
    public static final Sound victory = new Sound("/victory.wav");
    public static final Sound death = new Sound("/death.wav");
    private AudioClip clip;
    public Sound(String filename)
    {
        try{//try catch statement
            clip = Applet.newAudioClip(Sound.class.getResource(filename));
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void play()
    {
        try{
            new Thread(){
                public void run(){
                    clip.loop();
                }
            }.start();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void stop()
    {
        clip.stop();//use stop method in the imported packages
    }

    public void restart()
    {
        clip.stop();
        clip.play();//stop then play the AudioClip
    }
}