package piano.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

/**
 * View: builds the window and its components. Contains no sound logic;
 * the controller attaches the listeners.
 */
public class PianoView extends JFrame {

    private static final Color HEADER = new Color(28, 32, 48);
    private static final Color PANEL = new Color(244, 245, 248);

    public final KeyboardPanel keyboard = new KeyboardPanel();

    public final JComboBox<String> instrumentBox;
    public final JButton octaveDownButton = new JButton("-");
    public final JButton octaveUpButton = new JButton("+");
    public final JLabel octaveLabel = new JLabel("4");
    public final JSlider volumeSlider = new JSlider(0, 127, 100);
    public final JCheckBox sustainBox = new JCheckBox("Sustain");

    public final JButton recordButton = new JButton("● Record");
    public final JButton stopButton = new JButton("■ Stop");
    public final JButton playButton = new JButton("▶ Play Recording");
    public final JButton saveButton = new JButton("Save as MIDI");
    public final JButton openButton = new JButton("Open MIDI File");
    public final JButton demoButton = new JButton("♫ Play Demo Song");

    public final JLabel statusLabel = new JLabel("Ready - play with the mouse or your computer keyboard");

    public PianoView(String[] instruments) {
        setTitle("Virtual Piano & Music Recorder");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        instrumentBox = new JComboBox<>(instruments);

        add(buildHeader(), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(new Color(30, 30, 36));
        center.add(keyboard, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        add(buildBottom(), BorderLayout.SOUTH);

        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER);
        header.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JLabel title = new JLabel("Virtual Piano & Music Recorder");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        controls.setOpaque(false);

        controls.add(whiteLabel("Instrument"));
        controls.add(instrumentBox);

        controls.add(whiteLabel("   Octave"));
        controls.add(octaveDownButton);
        octaveLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        octaveLabel.setForeground(Color.WHITE);
        controls.add(octaveLabel);
        controls.add(octaveUpButton);

        controls.add(whiteLabel("   Volume"));
        volumeSlider.setOpaque(false);
        volumeSlider.setPreferredSize(new java.awt.Dimension(110, 24));
        controls.add(volumeSlider);

        sustainBox.setOpaque(false);
        sustainBox.setForeground(Color.WHITE);
        controls.add(sustainBox);

        header.add(controls, BorderLayout.EAST);
        return header;
    }

    private JPanel buildBottom() {
        JPanel bottom = new JPanel(new GridLayout(2, 1));
        bottom.setBackground(PANEL);
        bottom.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        buttons.setOpaque(false);
        recordButton.setForeground(new Color(200, 30, 30));
        for (JButton b : new JButton[]{recordButton, stopButton, playButton, saveButton, openButton, demoButton}) {
            b.setFocusable(false);
            buttons.add(b);
        }
        bottom.add(buttons);

        JPanel status = new JPanel(new BorderLayout());
        status.setOpaque(false);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        status.add(statusLabel, BorderLayout.WEST);
        JLabel help = new JLabel("Keys: A S D F G H J K L ; '  (white)   W E T Y U O P  (black)   Z / X  octave down / up");
        help.setForeground(new Color(90, 90, 90));
        status.add(help, BorderLayout.EAST);
        bottom.add(status);

        octaveDownButton.setFocusable(false);
        octaveUpButton.setFocusable(false);
        sustainBox.setFocusable(false);
        volumeSlider.setFocusable(false);
        instrumentBox.setFocusable(false);
        return bottom;
    }

    private JLabel whiteLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(200, 205, 220));
        return label;
    }

    /** Enables/disables the recorder buttons for the current state. */
    public void setButtonStates(boolean recording, boolean playing, boolean hasRecording) {
        boolean idle = !recording && !playing;
        recordButton.setEnabled(idle);
        stopButton.setEnabled(recording || playing);
        playButton.setEnabled(idle && hasRecording);
        saveButton.setEnabled(idle && hasRecording);
        openButton.setEnabled(idle);
        demoButton.setEnabled(idle);
    }
}
