import java.io.*;
import javax.sound.sampled.*;
import java.util.Random;

public class SoundEffects {
	private static Clip clip; // each sound effect has its own clip, loaded with its own sound file, this way it is less laggy.
	static Random r = new Random();

	public SoundEffects() {
		
	}

	
	public static void play(String fileName, float volume) {
		try {
			AudioInputStream audio = AudioSystem.getAudioInputStream(new File(fileName)); 
			clip = AudioSystem.getClip(); // open audio clip and load samples from the audio input stream.
			clip.open(audio);
			FloatControl gainControl =(FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(volume); // reduce the volume 

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (clip.isRunning()) {
			clip.stop();
			clip.close();// stops the player to prevent memory link
		}
		clip.setFramePosition(0); // begins form beginning for replaying purposes
		clip.start(); // Start playing
	}

}