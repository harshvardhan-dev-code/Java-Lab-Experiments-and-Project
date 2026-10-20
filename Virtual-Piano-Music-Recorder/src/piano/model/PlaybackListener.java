package piano.model;

/**
 * Receives notifications while a recording or MIDI file is playing,
 * so the view can light up the keys that are sounding.
 */
public interface PlaybackListener {

    void noteOn(int note);

    void noteOff(int note);

    void playbackFinished();
}
