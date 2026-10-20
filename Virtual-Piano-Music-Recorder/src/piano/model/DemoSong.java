package piano.model;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

/**
 * A built-in demo tune: "Ode to Joy" by Beethoven (public domain).
 */
public class DemoSong {

    private static final int C = 60, D = 62, E = 64, F = 65, G = 67;

    // { note, length in half-beats }
    private static final int[][] MELODY = {
            {E, 2}, {E, 2}, {F, 2}, {G, 2}, {G, 2}, {F, 2}, {E, 2}, {D, 2},
            {C, 2}, {C, 2}, {D, 2}, {E, 2}, {E, 3}, {D, 1}, {D, 4},
            {E, 2}, {E, 2}, {F, 2}, {G, 2}, {G, 2}, {F, 2}, {E, 2}, {D, 2},
            {C, 2}, {C, 2}, {D, 2}, {E, 2}, {D, 3}, {C, 1}, {C, 4}
    };

    private static final int HALF_BEAT = 250;   // ticks (= ms at 120 BPM)

    private DemoSong() {
    }

    public static Sequence odeToJoy(int program) throws InvalidMidiDataException {
        Sequence sequence = new Sequence(Sequence.PPQ, 500);
        Track track = sequence.createTrack();

        Recorder.addTempo(track);
        track.add(new MidiEvent(new ShortMessage(ShortMessage.PROGRAM_CHANGE, 0, program, 0), 0));

        long tick = 0;
        for (int[] n : MELODY) {
            long length = (long) n[1] * HALF_BEAT;
            track.add(new MidiEvent(new ShortMessage(ShortMessage.NOTE_ON, 0, n[0], 100), tick));
            track.add(new MidiEvent(new ShortMessage(ShortMessage.NOTE_OFF, 0, n[0], 0), tick + length * 9 / 10));
            tick += length;
        }
        return sequence;
    }
}
