package piano;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import piano.controller.PianoController;
import piano.model.PianoModel;
import piano.view.PianoView;

/**
 * Entry point: creates the Model, View and Controller and shows the window.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // fall back to the default look and feel
            }

            try {
                PianoModel model = new PianoModel();
                PianoView view = new PianoView(PianoModel.INSTRUMENT_NAMES);
                new PianoController(model, view);
                view.setVisible(true);
            } catch (MidiUnavailableException e) {
                JOptionPane.showMessageDialog(null,
                        "The Java MIDI synthesizer is not available on this computer:\n" + e.getMessage(),
                        "Virtual Piano", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
