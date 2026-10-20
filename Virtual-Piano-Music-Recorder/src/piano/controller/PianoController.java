package piano.controller;

import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;

import piano.model.PianoModel;
import piano.model.PlaybackListener;
import piano.view.KeyboardPanel;
import piano.view.PianoView;

/**
 * Controller: connects the View to the Model.
 * Handles computer-keyboard keys, mouse clicks on the piano and all buttons.
 */
public class PianoController {

    private static final int MIN_OCTAVE = 1, MAX_OCTAVE = 6;

    private final PianoModel model;
    private final PianoView view;

    private final Map<Integer, Integer> keyToOffset = new HashMap<>();   // key code -> semitones above base C
    private final Map<Integer, Integer> heldKeys = new HashMap<>();      // key code -> note being played
    private int octave = 4;
    private final Timer recordTimer;

    public PianoController(PianoModel model, PianoView view) {
        this.model = model;
        this.view = view;

        buildKeyMap();

        view.keyboard.setNoteListener(new KeyboardPanel.NoteListener() {
            @Override
            public void notePressed(int note) {
                play(note);
            }

            @Override
            public void noteReleased(int note) {
                release(note);
            }
        });

        view.instrumentBox.addActionListener(e -> {
            model.setInstrument(view.instrumentBox.getSelectedIndex());
            setStatus("Instrument: " + view.instrumentBox.getSelectedItem());
        });
        view.octaveDownButton.addActionListener(e -> changeOctave(-1));
        view.octaveUpButton.addActionListener(e -> changeOctave(+1));
        view.volumeSlider.addChangeListener(e -> model.setVolume(view.volumeSlider.getValue()));
        view.sustainBox.addActionListener(e -> model.setSustain(view.sustainBox.isSelected()));

        view.recordButton.addActionListener(e -> startRecording());
        view.stopButton.addActionListener(e -> stop());
        view.playButton.addActionListener(e -> playRecording());
        view.saveButton.addActionListener(e -> saveRecording());
        view.openButton.addActionListener(e -> openMidiFile());
        view.demoButton.addActionListener(e -> playDemo());

        // Computer keyboard: catch keys anywhere in the window
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this::handleKey);

        view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                model.close();
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                releaseAllKeys();
            }
        });

        recordTimer = new Timer(200, e -> showRecordingStatus());

        changeOctave(0);
        updateButtons();
    }

    // ---------------------------------------------------------------- keyboard mapping

    private void buildKeyMap() {
        int[] whiteKeys = {KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_F, KeyEvent.VK_G,
                KeyEvent.VK_H, KeyEvent.VK_J, KeyEvent.VK_K, KeyEvent.VK_L, KeyEvent.VK_SEMICOLON, KeyEvent.VK_QUOTE};
        int[] whiteOffsets = {0, 2, 4, 5, 7, 9, 11, 12, 14, 16, 17};
        String whiteNames = "ASDFGHJKL;'";

        int[] blackKeys = {KeyEvent.VK_W, KeyEvent.VK_E, KeyEvent.VK_T, KeyEvent.VK_Y,
                KeyEvent.VK_U, KeyEvent.VK_O, KeyEvent.VK_P};
        int[] blackOffsets = {1, 3, 6, 8, 10, 13, 15};
        String blackNames = "WETYUOP";

        Map<Integer, String> hints = new HashMap<>();
        for (int i = 0; i < whiteKeys.length; i++) {
            keyToOffset.put(whiteKeys[i], whiteOffsets[i]);
            hints.put(whiteOffsets[i], String.valueOf(whiteNames.charAt(i)));
        }
        for (int i = 0; i < blackKeys.length; i++) {
            keyToOffset.put(blackKeys[i], blackOffsets[i]);
            hints.put(blackOffsets[i], String.valueOf(blackNames.charAt(i)));
        }
        view.keyboard.setKeyHints(hints);
    }

    private boolean handleKey(KeyEvent e) {
        if (!view.isActive() || e.isControlDown() || e.isAltDown()) return false;
        int code = e.getKeyCode();

        if (e.getID() == KeyEvent.KEY_PRESSED) {
            if (code == KeyEvent.VK_Z) {
                changeOctave(-1);
                return true;
            }
            if (code == KeyEvent.VK_X) {
                changeOctave(+1);
                return true;
            }
            Integer offset = keyToOffset.get(code);
            if (offset == null) return false;
            if (!heldKeys.containsKey(code)) {          // ignore auto-repeat while held
                int note = baseNote() + offset;
                heldKeys.put(code, note);
                play(note);
            }
            return true;
        }

        if (e.getID() == KeyEvent.KEY_RELEASED) {
            Integer note = heldKeys.remove(code);
            if (note != null) {
                release(note);
                return true;
            }
            return keyToOffset.containsKey(code) || code == KeyEvent.VK_Z || code == KeyEvent.VK_X;
        }
        return keyToOffset.containsKey(code);
    }

    private int baseNote() {
        return 12 * (octave + 1);      // octave 4 -> 60 (middle C)
    }

    private void changeOctave(int delta) {
        octave = Math.max(MIN_OCTAVE, Math.min(MAX_OCTAVE, octave + delta));
        view.octaveLabel.setText(String.valueOf(octave));
        view.keyboard.setBaseNote(baseNote());
        view.octaveDownButton.setEnabled(octave > MIN_OCTAVE);
        view.octaveUpButton.setEnabled(octave < MAX_OCTAVE);
    }

    // ---------------------------------------------------------------- playing notes

    private void play(int note) {
        if (model.isPlaying()) return;           // don't mix live notes with playback
        model.noteOn(note);
        view.keyboard.setPressed(note, true);
        if (!model.isRecording()) setStatus("Playing " + PianoModel.noteName(note));
    }

    private void release(int note) {
        model.noteOff(note);
        view.keyboard.setPressed(note, false);
    }

    private void releaseAllKeys() {
        heldKeys.clear();
        model.allNotesOff();
        view.keyboard.clearPressed();
    }

    // ---------------------------------------------------------------- recorder buttons

    private void startRecording() {
        model.startRecording();
        recordTimer.start();
        showRecordingStatus();
        updateButtons();
    }

    private void stop() {
        if (model.isRecording()) {
            model.stopRecording();
            recordTimer.stop();
            setStatus(model.hasRecording()
                    ? "Recording saved in memory: " + model.getRecordedNoteCount() + " notes. Press Play or Save."
                    : "Nothing was recorded.");
        } else if (model.isPlaying()) {
            model.stopPlayback();
            view.keyboard.clearPressed();
            setStatus("Playback stopped.");
        }
        updateButtons();
    }

    private void playRecording() {
        try {
            model.playRecording(playbackListener);
            setStatus("▶ Playing your recording...");
        } catch (Exception ex) {
            showError("Could not play the recording", ex);
        }
        updateButtons();
    }

    private void playDemo() {
        try {
            model.playDemo(playbackListener);
            setStatus("♫ Playing demo: Ode to Joy (Beethoven)");
        } catch (Exception ex) {
            showError("Could not play the demo song", ex);
        }
        updateButtons();
    }

    private void saveRecording() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save recording as MIDI");
        chooser.setFileFilter(new FileNameExtensionFilter("MIDI files (*.mid)", "mid", "midi"));
        chooser.setSelectedFile(new File("my-song.mid"));
        if (chooser.showSaveDialog(view) != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();
        if (!file.getName().toLowerCase().matches(".*\\.midi?$")) {
            file = new File(file.getParentFile(), file.getName() + ".mid");
        }
        try {
            model.saveRecording(file);
            setStatus("Saved: " + file.getName());
            JOptionPane.showMessageDialog(view, "Recording saved to:\n" + file.getAbsolutePath(),
                    "Saved", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError("Could not save the file", ex);
        }
    }

    private void openMidiFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Open a MIDI file");
        chooser.setFileFilter(new FileNameExtensionFilter("MIDI files (*.mid)", "mid", "midi"));
        if (chooser.showOpenDialog(view) != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();
        try {
            model.playFile(file, playbackListener);
            setStatus("▶ Playing " + file.getName());
        } catch (Exception ex) {
            showError("Could not open " + file.getName(), ex);
        }
        updateButtons();
    }

    /** Lights up keys during playback. Called from the MIDI thread, so it hops to the Swing thread. */
    private final PlaybackListener playbackListener = new PlaybackListener() {
        @Override
        public void noteOn(int note) {
            SwingUtilities.invokeLater(() -> view.keyboard.setPressed(note, true));
        }

        @Override
        public void noteOff(int note) {
            SwingUtilities.invokeLater(() -> view.keyboard.setPressed(note, false));
        }

        @Override
        public void playbackFinished() {
            SwingUtilities.invokeLater(() -> {
                view.keyboard.clearPressed();
                setStatus("Playback finished.");
                updateButtons();
            });
        }
    };

    // ---------------------------------------------------------------- helpers

    private void showRecordingStatus() {
        long seconds = model.getRecordingMillis() / 1000;
        setStatus(String.format("● Recording...  %02d:%02d  |  %d notes",
                seconds / 60, seconds % 60, model.getRecordedNoteCount()));
    }

    private void updateButtons() {
        view.setButtonStates(model.isRecording(), model.isPlaying(), model.hasRecording());
    }

    private void setStatus(String text) {
        view.statusLabel.setText(text);
    }

    private void showError(String message, Exception ex) {
        JOptionPane.showMessageDialog(view, message + ":\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
