package piano.model;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

/**
 * Model: all the sound logic. Uses Java's built-in MIDI synthesizer
 * to play notes, change instruments, record and play back music.
 * Contains no GUI code.
 */
public class PianoModel {

    public static final String[] INSTRUMENT_NAMES = {
            "Grand Piano", "Electric Piano", "Harpsichord", "Vibraphone",
            "Church Organ", "Acoustic Guitar", "Steel Guitar", "Bass Guitar",
            "Violin", "Cello", "Trumpet", "Saxophone",
            "Clarinet", "Flute", "Synth Lead", "Steel Drums"
    };

    // General MIDI program numbers matching INSTRUMENT_NAMES
    private static final int[] INSTRUMENT_PROGRAMS = {
            0, 4, 6, 11,
            19, 24, 25, 32,
            40, 42, 56, 65,
            71, 73, 80, 114
    };

    private static final int VELOCITY = 100;
    private static final int CC_VOLUME = 7;
    private static final int CC_SUSTAIN = 64;

    private final Synthesizer synthesizer;
    private final MidiChannel[] channels;
    private final MidiChannel channel;          // channel 0 is used for live playing
    private final Recorder recorder = new Recorder();
    private final boolean[] activeNotes = new boolean[128];

    private int program = 0;
    private int volume = 100;
    private boolean sustain;

    private Sequencer sequencer;
    private PlaybackListener playbackListener;

    public PianoModel() throws MidiUnavailableException {
        synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();
        channels = synthesizer.getChannels();
        channel = channels[0];
        setInstrument(0);
        setVolume(volume);
    }

    // ---------------------------------------------------------------- live playing

    public void noteOn(int note) {
        if (note < 0 || note > 127 || activeNotes[note]) return;
        activeNotes[note] = true;
        channel.noteOn(note, VELOCITY);
        recorder.noteOn(note, VELOCITY);
    }

    public void noteOff(int note) {
        if (note < 0 || note > 127 || !activeNotes[note]) return;
        activeNotes[note] = false;
        channel.noteOff(note);
        recorder.noteOff(note);
    }

    public void allNotesOff() {
        for (int n = 0; n < activeNotes.length; n++) noteOff(n);
    }

    public void setInstrument(int index) {
        program = INSTRUMENT_PROGRAMS[index];
        channel.programChange(program);
        recorder.programChange(program);
    }

    /** Volume from 0 to 127. */
    public void setVolume(int volume) {
        this.volume = volume;
        for (MidiChannel c : channels) {
            if (c != null) c.controlChange(CC_VOLUME, volume);
        }
    }

    public void setSustain(boolean on) {
        sustain = on;
        channel.controlChange(CC_SUSTAIN, on ? 127 : 0);
        recorder.controlChange(CC_SUSTAIN, on ? 127 : 0);
    }

    // ---------------------------------------------------------------- recording

    public void startRecording() {
        recorder.start(program);
        if (sustain) recorder.controlChange(CC_SUSTAIN, 127);
    }

    public void stopRecording() {
        // close any notes still held so the recording does not end with hanging notes
        for (int n = 0; n < activeNotes.length; n++) {
            if (activeNotes[n]) recorder.noteOff(n);
        }
        recorder.stop();
    }

    public boolean isRecording() {
        return recorder.isRecording();
    }

    public boolean hasRecording() {
        return recorder.hasRecording();
    }

    public int getRecordedNoteCount() {
        return recorder.getNoteCount();
    }

    public long getRecordingMillis() {
        return recorder.getElapsedMillis();
    }

    public void saveRecording(File file) throws InvalidMidiDataException, IOException {
        recorder.save(file);
    }

    // ---------------------------------------------------------------- playback

    public void playRecording(PlaybackListener listener)
            throws MidiUnavailableException, InvalidMidiDataException {
        play(recorder.toSequence(), listener);
    }

    public void playDemo(PlaybackListener listener)
            throws MidiUnavailableException, InvalidMidiDataException {
        play(DemoSong.odeToJoy(program), listener);
    }

    public void playFile(File file, PlaybackListener listener)
            throws MidiUnavailableException, InvalidMidiDataException, IOException {
        play(MidiSystem.getSequence(file), listener);
    }

    public boolean isPlaying() {
        return sequencer != null && sequencer.isRunning();
    }

    public void stopPlayback() {
        if (sequencer != null && sequencer.isRunning()) {
            sequencer.stop();
        }
        resetChannels();
    }

    private void play(Sequence sequence, PlaybackListener listener)
            throws MidiUnavailableException, InvalidMidiDataException {
        stopPlayback();
        allNotesOff();
        if (sequencer == null) {
            sequencer = MidiSystem.getSequencer(false);
            sequencer.open();
            sequencer.getTransmitter().setReceiver(new TapReceiver(synthesizer.getReceiver()));
            sequencer.addMetaEventListener(this::onMetaEvent);
        }
        playbackListener = listener;
        sequencer.setSequence(sequence);
        sequencer.setTickPosition(0);
        sequencer.start();
    }

    private void onMetaEvent(MetaMessage meta) {
        if (meta.getType() == 47) {          // 47 = End of Track
            resetChannels();
            if (playbackListener != null) playbackListener.playbackFinished();
        }
    }

    /** Silences every channel and restores the live instrument, volume and sustain. */
    private void resetChannels() {
        for (MidiChannel c : channels) {
            if (c != null) {
                c.allNotesOff();
                c.controlChange(CC_SUSTAIN, 0);
            }
        }
        channel.programChange(program);
        channel.controlChange(CC_SUSTAIN, sustain ? 127 : 0);
        setVolume(volume);
    }

    public void close() {
        if (sequencer != null) sequencer.close();
        synthesizer.close();
    }

    /**
     * Forwards playback messages to the synthesizer and tells the listener
     * which notes are sounding (drum channel 9 is ignored).
     */
    private class TapReceiver implements Receiver {
        private final Receiver target;

        TapReceiver(Receiver target) {
            this.target = target;
        }

        @Override
        public void send(MidiMessage message, long timeStamp) {
            target.send(message, timeStamp);
            if (!(message instanceof ShortMessage) || playbackListener == null) return;

            ShortMessage sm = (ShortMessage) message;
            if (sm.getChannel() == 9) return;
            int command = sm.getCommand();
            if (command == ShortMessage.NOTE_ON && sm.getData2() > 0) {
                playbackListener.noteOn(sm.getData1());
            } else if (command == ShortMessage.NOTE_OFF
                    || (command == ShortMessage.NOTE_ON && sm.getData2() == 0)) {
                playbackListener.noteOff(sm.getData1());
            }
        }

        @Override
        public void close() {
        }
    }

    /** Returns the name of a MIDI note, e.g. 60 -> "C4". */
    public static String noteName(int note) {
        String[] names = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
        return names[note % 12] + (note / 12 - 1);
    }
}
