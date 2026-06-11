import java.io.*;
import javax.sound.sampled.*;
import java.util.Random;

public class SoundEffects {
	private static Clip clip; // Each sound effect has its own clip, loaded with its own sound file.
	static Random r = new Random();

	public SoundEffects() {
		
	}
	// Play or Re-play the sound effect from the beginning, by rewinding.
	/**
	   * plays sound file
	   * pre: valid sound file in aud folder must exist
	   * post: sound file is played
	   */
	public static void play(String fileName, float volume) {
		try {
			AudioInputStream audio = AudioSystem.getAudioInputStream(new File(fileName)); // Get a clip resource.
			clip = AudioSystem.getClip(); // Open audio clip and load samples from the audio input stream.
			clip.open(audio);
			FloatControl gainControl =(FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(volume); // Reduce volume by 10 decibels.

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (clip.isRunning()) {
			clip.stop();
			clip.close();// Stop the player if it is still running
		}
		clip.setFramePosition(0); // rewind to the beginning
		clip.start(); // Start playing
	}
	
	//overloaded for random numbers
	public static void play(String fileName, float volume, int randomRange ) {
		int randNum = r.nextInt(randomRange)+1;

		try {
			AudioInputStream audio = AudioSystem.getAudioInputStream(new File(fileName)); // Get a clip resource.

			clip = AudioSystem.getClip(); // Open audio clip and load samples from the audio input stream.
			clip.open(audio);
			FloatControl gainControl =(FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(volume); // Reduce volume by 10 decibels.
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (clip.isRunning()) {
			clip.stop();
			clip.close();// Stop the player if it is still running
		}
		clip.setFramePosition(0); // rewind to the beginning
		clip.start(); // Start playing
	}
}