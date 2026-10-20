package piano.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import piano.model.PianoModel;

/**
 * Draws a two-octave piano keyboard and reports mouse presses on its keys.
 */
public class KeyboardPanel extends JPanel {

    /** Notified when the user presses or releases a key with the mouse. */
    public interface NoteListener {
        void notePressed(int note);

        void noteReleased(int note);
    }

    private static final int WHITE_KEYS = 15;                       // two octaves + top C
    private static final int[] WHITE_OFFSETS = {0, 2, 4, 5, 7, 9, 11};
    private static final boolean[] BLACK_AFTER = {true, true, false, true, true, true, false};

    private static final Color PRESSED_WHITE = new Color(120, 190, 255);
    private static final Color PRESSED_BLACK = new Color(40, 120, 220);

    private int baseNote = 60;                                     // C4
    private final boolean[] pressed = new boolean[128];
    private Map<Integer, String> keyHints = new HashMap<>();       // note offset -> keyboard key
    private NoteListener listener;
    private int mouseNote = -1;

    public KeyboardPanel() {
        setPreferredSize(new Dimension(960, 260));
        setBackground(new Color(30, 30, 36));

        MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseNote = noteAt(e.getX(), e.getY());
                if (listener != null) listener.notePressed(mouseNote);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                int note = noteAt(e.getX(), e.getY());
                if (note != mouseNote && listener != null) {
                    listener.noteReleased(mouseNote);
                    listener.notePressed(note);
                    mouseNote = note;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (mouseNote >= 0 && listener != null) listener.noteReleased(mouseNote);
                mouseNote = -1;
            }
        };
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    public void setNoteListener(NoteListener listener) {
        this.listener = listener;
    }

    public void setBaseNote(int baseNote) {
        this.baseNote = baseNote;
        repaint();
    }

    public void setKeyHints(Map<Integer, String> keyHints) {
        this.keyHints = keyHints;
        repaint();
    }

    public void setPressed(int note, boolean down) {
        if (note >= 0 && note < 128) {
            pressed[note] = down;
            repaint();
        }
    }

    public void clearPressed() {
        java.util.Arrays.fill(pressed, false);
        repaint();
    }

    private int whiteNote(int index) {
        return baseNote + 12 * (index / 7) + WHITE_OFFSETS[index % 7];
    }

    private double whiteWidth() {
        return (getWidth() - 20) / (double) WHITE_KEYS;
    }

    /** Returns the MIDI note under the given point (black keys are checked first). */
    private int noteAt(int x, int y) {
        double ww = whiteWidth(), bw = ww * 0.6, bh = (getHeight() - 20) * 0.62;
        double px = x - 10, py = y - 10;

        for (int i = 0; i < WHITE_KEYS - 1; i++) {
            if (!BLACK_AFTER[i % 7]) continue;
            double bx = (i + 1) * ww - bw / 2;
            if (px >= bx && px <= bx + bw && py <= bh) return whiteNote(i) + 1;
        }
        int index = (int) (px / ww);
        index = Math.max(0, Math.min(WHITE_KEYS - 1, index));
        return whiteNote(index);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.translate(10, 10);

        double ww = whiteWidth(), bw = ww * 0.6;
        int h = getHeight() - 20, bh = (int) (h * 0.62);
        Font hintFont = new Font("SansSerif", Font.BOLD, 14);
        Font nameFont = new Font("SansSerif", Font.PLAIN, 11);

        // white keys
        for (int i = 0; i < WHITE_KEYS; i++) {
            int note = whiteNote(i);
            double x = i * ww;
            RoundRectangle2D key = new RoundRectangle2D.Double(x + 1, 0, ww - 2, h, 8, 8);
            g2.setPaint(pressed[note] ? PRESSED_WHITE
                    : new GradientPaint(0, 0, new Color(235, 235, 235), 0, h, Color.WHITE));
            g2.fill(key);
            g2.setColor(new Color(90, 90, 90));
            g2.setStroke(new BasicStroke(1f));
            g2.draw(key);

            drawCentered(g2, PianoModel.noteName(note), nameFont, new Color(120, 120, 120), x, ww, h - 10);
            String hint = keyHints.get(note - baseNote);
            if (hint != null) drawCentered(g2, hint, hintFont, new Color(40, 40, 40), x, ww, h - 30);
        }

        // black keys
        for (int i = 0; i < WHITE_KEYS - 1; i++) {
            if (!BLACK_AFTER[i % 7]) continue;
            int note = whiteNote(i) + 1;
            double x = (i + 1) * ww - bw / 2;
            RoundRectangle2D key = new RoundRectangle2D.Double(x, 0, bw, bh, 6, 6);
            g2.setPaint(pressed[note] ? PRESSED_BLACK
                    : new GradientPaint(0, 0, new Color(60, 60, 60), 0, bh, new Color(15, 15, 15)));
            g2.fill(key);
            g2.setColor(Color.BLACK);
            g2.draw(key);

            String hint = keyHints.get(note - baseNote);
            if (hint != null) drawCentered(g2, hint, hintFont, new Color(230, 230, 230), x, bw, bh - 10);
        }
        g2.dispose();
    }

    private void drawCentered(Graphics2D g2, String text, Font font, Color color,
                              double x, double width, int baselineY) {
        g2.setFont(font);
        g2.setColor(color);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, (float) (x + (width - fm.stringWidth(text)) / 2), baselineY);
    }
}
