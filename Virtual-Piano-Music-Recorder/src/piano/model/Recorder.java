package piano.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

/**
 * Records the notes played on the piano with their timing,
 * and converts them into a MIDI Sequence that can be played back or saved.
 */
public class Recorder {

    // 500 ticks per quarter note at 120 BPM  =>  1 tick = 1 millisecond
    private static final int TICKS_PER_QUARTER = 500;

    /** One recorded MIDI message and the time (ms) it happened after recording started. */
    private static class RecordedEvent {
        final int command, data1, data2;
        final long time;

        RecordedEvent(int command, int data1, int data2, long time) {
            this.command = command;
            this.data1 = data1;
            this.data2 = data2;
            this.time = time;
        }
    }

    private final List<RecordedEvent> events = new ArrayList<>();
    private boolean recording;
    private long startTime;
    private int startProgram;
    private int noteCount;

    public void start(int currentProgram) {
        events.clear();
        noteCount = 0;
        startProgram = currentProgram;
        startTime = System.currentTimeMillis();
        recording = true;
    }

    public void stop() {
        recording = false;
    }

    public boolean isRecording() {
        return recording;
    }

    public boolean hasRecording() {
        return !recording && noteCount > 0;
    }

    public int getNoteCount() {
        return noteCount;
    }

    public long getElapsedMillis() {
        return recording ? System.currentTimeMillis() - startTime : 0;
    }

    public void noteOn(int note, int velocity) {
        if (recording) {
            add(ShortMessage.NOTE_ON, note, velocity);
            noteCount++;
        }
    }

    public void noteOff(int note) {
        if (recording) add(ShortMessage.NOTE_OFF, note, 0);
    }

    public void programChange(int program) {
        if (recording) add(ShortMessage.PROGRAM_CHANGE, program, 0);
    }

    public void controlChange(int controller, int value) {
        if (recording) add(ShortMessage.CONTROL_CHANGE, controller, value);
    }

    private void add(int command, int data1, int data2) {
        events.add(new RecordedEvent(command, data1, data2, System.currentTimeMillis() - startTime));
    }

    /** Builds a single-track MIDI sequence from the recorded events. */
    public Sequence toSequence() throws InvalidMidiDataException {
        Sequence sequence = new Sequence(Sequence.PPQ, TICKS_PER_QUARTER);
        Track track = sequence.createTrack();

        addTempo(track);
        track.add(new MidiEvent(new ShortMessage(ShortMessage.PROGRAM_CHANGE, 0, startProgram, 0), 0));

        for (RecordedEvent e : events) {
            ShortMessage msg = new ShortMessage(e.command, 0, e.data1, e.data2);
            track.add(new MidiEvent(msg, e.time));
        }
        return sequence;
    }

    /** Saves the recording as a standard MIDI (.mid) file. */
    public void save(File file) throws InvalidMidiDataException, IOException {
        MidiSystem.write(toSequence(), 0, file);
    }

    /** Adds a tempo of 120 BPM (500,000 microseconds per quarter note). */
    static void addTempo(Track track) throws InvalidMidiDataException {
        byte[] tempo = {0x07, (byte) 0xA1, 0x20};
        track.add(new MidiEvent(new MetaMessage(0x51, tempo, tempo.length), 0));
    }
}
