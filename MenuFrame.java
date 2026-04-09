package Pong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import javax.swing.border.EmptyBorder;

public class MenuFrame extends JFrame {

    // ── Palette ──────────────────────────────────────────────────────────────
    private static final Color BG_TOP        = new Color(5,  5, 20);
    private static final Color BG_BOT        = new Color(10, 0, 40);
    private static final Color NEON_CYAN     = new Color(0, 230, 255);
    private static final Color NEON_PINK     = new Color(255, 50, 180);
    private static final Color NEON_WHITE    = new Color(220, 240, 255);
    private static final Color BTN_IDLE_BG   = new Color(255,255,255, 10);
    private static final Color BTN_HOVER_BG  = new Color(0, 230, 255, 35);
    private static final Color SCANLINE      = new Color(0,0,0,55);

    // ── Title pulse animation ─────────────────────────────────────────────────
    private float pulse = 0f;
    private Timer pulseTimer;
    private JPanel bgPanel;

    MenuFrame() {
        this.setTitle("Pong — Main Menu");
        this.setSize(520, 540);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setContentPane(buildContent());

        // Pulse the title every 30 ms
        pulseTimer = new Timer(30, e -> {
            pulse += 0.07f;
            bgPanel.repaint();
        });
        pulseTimer.start();

        this.setVisible(true);
    }

    // ── Main content panel ────────────────────────────────────────────────────
    private JPanel buildContent() {
        bgPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient background
                GradientPaint grad = new GradientPaint(0,0, BG_TOP, 0, getHeight(), BG_BOT);
                g2.setPaint(grad);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Subtle grid lines
                g2.setColor(new Color(0, 180, 255, 18));
                g2.setStroke(new BasicStroke(0.7f));
                for (int y = 0; y < getHeight(); y += 32) g2.drawLine(0, y, getWidth(), y);
                for (int x = 0; x < getWidth();  x += 32) g2.drawLine(x, 0, x, getHeight());

                // Scanlines
                g2.setColor(SCANLINE);
                for (int y = 0; y < getHeight(); y += 3) g2.drawLine(0, y, getWidth(), y);

                // Corner glow blobs
                drawGlow(g2, 0,           0,            220, NEON_PINK,  0.12f);
                drawGlow(g2, getWidth(),  getHeight(),  220, NEON_CYAN,  0.12f);

                // Centre divider line
                g2.setColor(new Color(0, 230, 255, 60));
                g2.setStroke(new BasicStroke(1f));
                g2.drawLine(60, 170, getWidth()-60, 170);
            }
        };
        bgPanel.setOpaque(true);
        bgPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        bgPanel.add(buildTitle(),   BorderLayout.NORTH);
        bgPanel.add(buildButtons(), BorderLayout.CENTER);
        bgPanel.add(buildFooter(),  BorderLayout.SOUTH);

        return bgPanel;
    }

    // ── Title block ───────────────────────────────────────────────────────────
    private JPanel buildTitle() {
        JPanel p = new JPanel(new BorderLayout()) { { setOpaque(false); } };

        // "PONG" rendered with custom glow
        JLabel title = new JLabel("PONG", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                Font f = new Font("Courier New", Font.BOLD, 80);
                g2.setFont(f);
                FontMetrics fm = g2.getFontMetrics(f);
                String text = "PONG";
                int tx = (getWidth() - fm.stringWidth(text)) / 2;
                int ty = fm.getAscent() + 6;

                // Glow layers (animated via pulse)
                float alpha = 0.25f + 0.1f * (float)Math.sin(pulse);
                for (int r = 20; r >= 2; r -= 3) {
                    g2.setColor(new Color(0, 230, 255, (int)(alpha * 255 * r / 20)));
                    g2.drawString(text, tx - r/2, ty + r/2);
                }
                // Solid text
                g2.setColor(NEON_CYAN);
                g2.drawString(text, tx, ty);
            }
            @Override public Dimension getPreferredSize() { return new Dimension(440, 100); }
        };

        JLabel sub = new JLabel("CLASSIC ARCADE · FIRST TO 11 WINS", SwingConstants.CENTER);
        sub.setFont(new Font("Courier New", Font.PLAIN, 12));
        sub.setForeground(new Color(0, 200, 230, 160));
        sub.setBorder(new EmptyBorder(0, 0, 18, 0));

        p.add(title, BorderLayout.CENTER);
        p.add(sub,   BorderLayout.SOUTH);
        return p;
    }

    // ── Buttons ───────────────────────────────────────────────────────────────
    private JPanel buildButtons() {
        JPanel p = new JPanel() { { setOpaque(false); setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); } };
        p.setBorder(new EmptyBorder(18, 0, 18, 0));

        p.add(arcadeButton("▶  PLAY MATCH",    NEON_CYAN,  e -> startGame()));
        p.add(Box.createVerticalStrut(14));
        p.add(arcadeButton("⌨  CONTROLS",      NEON_WHITE, e -> showControls()));
        p.add(Box.createVerticalStrut(14));
        p.add(arcadeButton("ℹ  ABOUT",         NEON_WHITE, e -> showAbout()));
        p.add(Box.createVerticalStrut(14));
        p.add(arcadeButton("✕  EXIT",           NEON_PINK,  e -> System.exit(0)));

        return p;
    }

    // ── Single styled button ──────────────────────────────────────────────────
    private JButton arcadeButton(String label, Color accent, ActionListener action) {
        JButton btn = new JButton(label) {
            boolean hovered = false;

            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                    public void mouseExited (MouseEvent e) { hovered = false; repaint(); }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                // Background
                g2.setColor(hovered ? BTN_HOVER_BG : BTN_IDLE_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                // Left accent bar
                g2.setColor(accent);
                g2.fillRect(0, 0, 4, getHeight());

                // Hover right glow
                if (hovered) {
                    GradientPaint glow = new GradientPaint(0,0, new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 60),
                            getWidth(),0, new Color(0,0,0,0));
                    g2.setPaint(glow);
                    g2.fillRoundRect(4, 0, getWidth()-4, getHeight(), 8, 8);
                }

                // Border
                g2.setColor(hovered ? new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 200)
                        : new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 70));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);

                // Label
                g2.setFont(new Font("Courier New", Font.BOLD, 16));
                g2.setColor(hovered ? accent : new Color(200, 220, 240));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), 22, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }

            @Override public Dimension getPreferredSize()  { return new Dimension(380, 52); }
            @Override public Dimension getMaximumSize()    { return getPreferredSize(); }
            @Override public Dimension getMinimumSize()    { return getPreferredSize(); }
        };

        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);
        return btn;
    }

    // ── Footer ────────────────────────────────────────────────────────────────
    private JLabel buildFooter() {
        JLabel f = new JLabel("© 2025  PONG GAME  ·  2-Player Local", SwingConstants.CENTER);
        f.setFont(new Font("Courier New", Font.PLAIN, 11));
        f.setForeground(new Color(100, 120, 160));
        return f;
    }

    // ── Utility: draw a soft radial glow ─────────────────────────────────────
    private void drawGlow(Graphics2D g2, int cx, int cy, int r, Color c, float alpha) {
        RadialGradientPaint rg = new RadialGradientPaint(
                new Point2D.Float(cx, cy), r,
                new float[]{0f, 1f},
                new Color[]{new Color(c.getRed(), c.getGreen(), c.getBlue(), (int)(alpha*255)),
                        new Color(c.getRed(), c.getGreen(), c.getBlue(), 0)});
        g2.setPaint(rg);
        g2.fillOval(cx - r, cy - r, r*2, r*2);
    }

    // ── Actions ───────────────────────────────────────────────────────────────
    private void startGame() {
        pulseTimer.stop();
        new GameFrame();
        this.dispose();
    }

    private void showControls() {
        String msg =
                "┌─────────────────────────────────┐\n" +
                        "│          PLAYER CONTROLS        │\n" +
                        "├─────────────────────────────────┤\n" +
                        "│  Player 1 (LEFT paddle)         │\n" +
                        "│    Move Up   →  W               │\n" +
                        "│    Move Down →  S               │\n" +
                        "├─────────────────────────────────┤\n" +
                        "│  Player 2 (RIGHT paddle)        │\n" +
                        "│    Move Up   →  ↑  (Arrow Up)   │\n" +
                        "│    Move Down →  ↓  (Arrow Down) │\n" +
                        "└─────────────────────────────────┘";

        JTextArea ta = styledTextArea(msg);
        JOptionPane.showMessageDialog(this, new JScrollPane(ta), "Controls", JOptionPane.PLAIN_MESSAGE);
    }

    private void showAbout() {
        String msg =
                "══════════════════════════════════════════\n" +
                        "                  PONG\n" +
                        "   Classic Arcade Table Tennis — 2 Player\n" +
                        "══════════════════════════════════════════\n\n" +
                        "OBJECTIVE\n" +
                        "  Be the first player to reach 11 points.\n" +
                        "  Score by getting the ball past your\n" +
                        "  opponent's paddle.\n\n" +
                        "HOW TO PLAY\n" +
                        "  • Each player controls a vertical paddle\n" +
                        "    on their side of the screen.\n" +
                        "  • Use your paddle to deflect the ball\n" +
                        "    back toward your opponent.\n" +
                        "  • If the ball passes your paddle, the\n" +
                        "    other player scores a point.\n" +
                        "  • After each point the ball and paddles\n" +
                        "    reset to the centre.\n\n" +
                        "BALL PHYSICS\n" +
                        "  • The ball starts at a random angle.\n" +
                        "  • Every time a paddle hits the ball, the\n" +
                        "    ball speeds up — keep your reflexes\n" +
                        "    sharp as rallies go on!\n" +
                        "  • The ball bounces off the top and bottom\n" +
                        "    walls at equal angles.\n\n" +
                        "WINNING\n" +
                        "  • First to 11 points wins the match.\n" +
                        "  • The winner is displayed on screen.\n" +
                        "  • Close the game window to return here\n" +
                        "    and start a new match.\n\n" +
                        "TIPS\n" +
                        "  • Anticipate where the ball will be, not\n" +
                        "    where it is — the ball moves fast!\n" +
                        "  • Keep your paddle centred when the ball\n" +
                        "    is on the opponent's side.\n" +
                        "  • Stay calm in long rallies; the ball\n" +
                        "    will become very fast!\n\n" +
                        "══════════════════════════════════════════\n" +
                        "         Inspired by Atari Pong (1972)\n" +
                        "══════════════════════════════════════════";

        JTextArea ta = styledTextArea(msg);
        ta.setRows(22);
        JScrollPane sp = new JScrollPane(ta);
        sp.setPreferredSize(new Dimension(430, 440));
        sp.setBorder(null);
        JOptionPane.showMessageDialog(this, sp, "About Pong", JOptionPane.PLAIN_MESSAGE);
    }

    // ── Shared styled text area for dialogs ───────────────────────────────────
    private JTextArea styledTextArea(String text) {
        JTextArea ta = new JTextArea(text);
        ta.setFont(new Font("Courier New", Font.PLAIN, 13));
        ta.setEditable(false);
        ta.setOpaque(true);
        ta.setBackground(new Color(8, 8, 25));
        ta.setForeground(new Color(180, 220, 255));
        ta.setCaretColor(NEON_CYAN);
        ta.setBorder(new EmptyBorder(14, 18, 14, 18));
        return ta;
    }
}