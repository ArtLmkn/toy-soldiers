package util;

import static util.Const.Sounds.*;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.*;

/**
 * Game audio player.
 * Starts and stops background music and fx sounds.
 */
public class Audio {
    private final Clip[] songs; // game music
    private final Clip[] sounds; // fx sounds
    private int currentSong; // playing song at the moment

    /**
     * Constructor for the Audio player.
     * Loads all audio files, sets standard volume and starts main menu theme.
     */
    public Audio() {
        // Load background music
        songs = new Clip[SONGS.length];
        for (int i = 0; i < songs.length; i++)
            songs[i] = loadAudio(SONGS[i]);

        // Load effect sounds
        sounds = new Clip[SOUNDS.length];
        for (int i = 0; i < sounds.length; i++)
            sounds[i] = loadAudio(SOUNDS[i]);

        setSoundVolume();
        playSong(MENU);
    }

    /**
     * Loads audio files from resources folder and returns a Clip object.
     *
     * @param name name of the file to load, without the .wav extension.
     * @return Clip object containing the loaded audio.
     */
    private Clip loadAudio(String name) {
        URL url = getClass().getResource("/audio/" + name + ".wav");
        AudioInputStream audio;
        try { // Controls file existence
            assert url != null;
            audio = AudioSystem.getAudioInputStream(url);
            Clip c = AudioSystem.getClip();
            c.open(audio);
            return c;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ignored) {}
        return null;
    }

    /**
     * Plays the specified background music track.
     * If a song is already playing, stops it first before playing the new one.
     *
     * @param song index of the song to play.
     */
    public void playSong(int song) {
        if (songs[currentSong].isActive()) songs[currentSong].stop(); // Stop currently playing song
        currentSong = song;
        setSongVolume();
        songs[currentSong].setMicrosecondPosition(0);
        songs[currentSong].loop(Clip.LOOP_CONTINUOUSLY); // Loop the background song
    }

    /**
     * Plays the specified sound effect.
     *
     * @param sound index of the sound to play.
     */
    public void playSound(int sound) {
        if (sounds[sound].getMicrosecondPosition() > 0) sounds[sound].setMicrosecondPosition(0);
        sounds[sound].start();
    }

    /**
     * Sets the volume of the currently playing song to the default value.
     */
    private void setSongVolume() {
        FloatControl gainControl = (FloatControl) sounds[currentSong].getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * VOLUME) + gainControl.getMinimum();
        gainControl.setValue(gain);
    }

    /**
     * Sets the volume of the currently playing sound to the default value.
     */
    private void setSoundVolume() {
        for (Clip c : sounds) {
            FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * VOLUME) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }
}
