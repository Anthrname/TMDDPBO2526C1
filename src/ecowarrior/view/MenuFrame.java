/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.view;

import ecowarrior.audio.SoundManager;
import ecowarrior.config.GameConfig;
import ecowarrior.model.Achievement;
import ecowarrior.model.RankTier;
import ecowarrior.view.components.ModernButton;
import ecowarrior.view.components.ModernTable;
import ecowarrior.view.components.SoundControlPanel;
import ecowarrior.view.dialogs.AboutDialog;
import ecowarrior.view.dialogs.HowToPlayDialog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 * MenuFrame - Tampilan Layar Menu Utama ECO WARRIOR (View Layer - Pola MVP).
 * 
 * Menampilkan:
 * 1. Judul Game & Identitas Mahasiswa yang mencolok dan estetik.
 * 2. JTable tachievement (username, achievement, destroyer, fire) secara real-time.
 * 3. Input username sebelum bermain.
 * 4. Status koneksi database aktif (MySQL / SQLite Fallback).
 * 5. Kontrol volume suara (Fitur Bonus Nilai 20).
 * 
 * @author Najib Nurohman (2509653) - Kelas C1
 */
public class MenuFrame extends JFrame implements IMenuView {

    // Komponen UI
    private JTextField tfUsername;
    private ModernTable achievementTable;
    private DefaultTableModel tableModel;
    private JLabel dbStatusLabel;
    private JLabel rankPreviewLabel;
    private ModernButton btnPlay;
    private ModernButton btnRefresh;
    private ModernButton btnHowToPlay;
    private ModernButton btnAbout;

    // Listener yang akan diisi oleh Presenter (MVP: View tidak tahu logika)
    private ActionListener playListener;
    private ActionListener refreshListener;

    public MenuFrame() {
        setTitle(GameConfig.APP_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(1050, 720);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, GameConfig.COLOR_PRIMARY_DARK, 0, getHeight(), new Color(20, 38, 26));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setOpaque(false);

        // ===== PANEL KIRI: Judul, Input Username, Tombol Aksi =====
        JPanel leftPanel = buildLeftPanel();
        leftPanel.setPreferredSize(new Dimension(380, 720));

        // ===== PANEL KANAN: Tabel Achievement =====
        JPanel rightPanel = buildRightPanel();

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private JPanel buildLeftPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(18, 28, 22, 240));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                // Border garis kanan
                g2d.setColor(new Color(46, 204, 113, 100));
                g2d.fillRect(getWidth() - 2, 0, 2, getHeight());
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 24, 20, 24));

        // Judul Game Utama
        JLabel lblTitle = new JLabel("ECO WARRIOR");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 36));
        lblTitle.setForeground(GameConfig.COLOR_ACCENT_GREEN);
        lblTitle.setAlignmentX(CENTER_ALIGNMENT);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblSubtitle = new JLabel("Forest Protection Simulator");
        lblSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblSubtitle.setForeground(GameConfig.COLOR_TEXT_MUTED);
        lblSubtitle.setAlignmentX(CENTER_ALIGNMENT);

        // Dekorasi Karakter Pion sesuai diagram soal
        JLabel lblCharacterArt = new JLabel("<html><div style='text-align:center; font-size:40px; line-height:1.2'>🌲🌱🌿<br><span style='font-size:28px'>⚔️ vs 💥</span></div></html>");
        lblCharacterArt.setAlignmentX(CENTER_ALIGNMENT);

        // Info Mahasiswa
        JPanel idPanel = new JPanel();
        idPanel.setOpaque(false);
        idPanel.setLayout(new BoxLayout(idPanel, BoxLayout.Y_AXIS));
        idPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(46, 204, 113, 60), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        idPanel.setMaximumSize(new Dimension(330, 90));

        addInfoLabel(idPanel, "Mahasiswa: " + GameConfig.STUDENT_NAME, GameConfig.COLOR_TEXT_LIGHT, Font.BOLD);
        addInfoLabel(idPanel, "NIM: " + GameConfig.STUDENT_NIM + "  |  Kelas: " + GameConfig.STUDENT_CLASS, GameConfig.COLOR_TEXT_MUTED, Font.PLAIN);
        addInfoLabel(idPanel, "Dosen: Rosa Ariani Sukamto", GameConfig.COLOR_TEXT_MUTED, Font.PLAIN);

        // Panel Input Username
        JLabel lblUsername = new JLabel("Masukkan Username Pemain:");
        lblUsername.setFont(GameConfig.FONT_BOLD);
        lblUsername.setForeground(GameConfig.COLOR_TEXT_LIGHT);
        lblUsername.setAlignmentX(LEFT_ALIGNMENT);

        tfUsername = new JTextField();
        tfUsername.setFont(new Font("SansSerif", Font.PLAIN, 15));
        tfUsername.setBackground(new Color(30, 42, 36));
        tfUsername.setForeground(Color.WHITE);
        tfUsername.setCaretColor(GameConfig.COLOR_ACCENT_GREEN);
        tfUsername.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(46, 204, 113, 100), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        tfUsername.setMaximumSize(new Dimension(330, 42));

        // Label preview rank dari achievement yang ada
        rankPreviewLabel = new JLabel("🏆 Rank: Ranger Recruit (Achievement: 0 pts)");
        rankPreviewLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        rankPreviewLabel.setForeground(GameConfig.COLOR_TEXT_MUTED);
        rankPreviewLabel.setAlignmentX(LEFT_ALIGNMENT);

        tfUsername.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateRankPreview(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateRankPreview(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateRankPreview(); }
        });

        // Tombol Aksi Utama
        btnPlay = new ModernButton("▶  MULAI BERMAIN", GameConfig.COLOR_ACCENT_GREEN, GameConfig.COLOR_ACCENT_HOVER);
        btnPlay.setPreferredSize(new Dimension(330, 46));
        btnPlay.setMaximumSize(new Dimension(330, 46));
        btnPlay.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnPlay.addActionListener(e -> { if (playListener != null) playListener.actionPerformed(e); });

        btnRefresh = new ModernButton("⟳  Refresh Leaderboard", new Color(44, 62, 80), new Color(52, 73, 94));
        btnRefresh.setMaximumSize(new Dimension(330, 36));
        btnRefresh.addActionListener(e -> { if (refreshListener != null) refreshListener.actionPerformed(e); });

        btnHowToPlay = new ModernButton("❓  Cara Bermain & Buff", new Color(39, 55, 70), new Color(52, 73, 94));
        btnHowToPlay.setMaximumSize(new Dimension(330, 36));
        btnHowToPlay.addActionListener(e -> new HowToPlayDialog(this).setVisible(true));

        btnAbout = new ModernButton("ℹ  Tentang Program", new Color(39, 55, 70), new Color(52, 73, 94));
        btnAbout.setMaximumSize(new Dimension(330, 36));
        btnAbout.addActionListener(e -> new AboutDialog(this).setVisible(true));

        // Label Status Koneksi Database
        dbStatusLabel = new JLabel("● Memeriksa koneksi database...");
        dbStatusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        dbStatusLabel.setForeground(Color.ORANGE);
        dbStatusLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Kontrol Suara
        SoundControlPanel soundPanel = new SoundControlPanel();
        soundPanel.setMaximumSize(new Dimension(330, 50));

        // Susun semua ke panel
        panel.add(lblTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(lblSubtitle);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(lblCharacterArt);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(idPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 18)));
        panel.add(lblUsername);
        panel.add(Box.createRigidArea(new Dimension(0, 6)));
        panel.add(tfUsername);
        panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(rankPreviewLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 18)));
        panel.add(btnPlay);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(btnRefresh);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(btnHowToPlay);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(btnAbout);
        panel.add(Box.createVerticalGlue());
        panel.add(soundPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(dbStatusLabel);

        return panel;
    }

    private JPanel buildRightPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 20, 20, 20));

        // Header Tabel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel tblTitle = new JLabel("🏆  PAPAN PERINGKAT  ACHIEVEMENT");
        tblTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        tblTitle.setForeground(new Color(241, 196, 15));

        JLabel tblSubtitle = new JLabel("Riwayat seluruh pemain tersimpan di database tachievement — skor bertambah tiap sesi!");
        tblSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tblSubtitle.setForeground(GameConfig.COLOR_TEXT_MUTED);

        headerPanel.add(tblTitle, BorderLayout.NORTH);
        headerPanel.add(tblSubtitle, BorderLayout.SOUTH);

        // Tabel Achievement (Sesuai Spesifikasi Soal: username, achievement, destroyer, fire)
        String[] columns = {"#", "Username", "Achievement (Skor)", "Destroyer 💥", "Fire 🔥", "Rank 🌿"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        achievementTable = new ModernTable(tableModel);
        achievementTable.getColumnModel().getColumn(0).setPreferredWidth(36);
        achievementTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        achievementTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        achievementTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        achievementTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        achievementTable.getColumnModel().getColumn(5).setPreferredWidth(160);

        JScrollPane scrollPane = new JScrollPane(achievementTable);
        scrollPane.setBackground(new Color(24, 34, 28));
        scrollPane.getViewport().setBackground(new Color(24, 34, 28));
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(46, 204, 113, 80), 1),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

        // Legend & keterangan sistem rank
        JPanel legendPanel = buildRankLegend();

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(legendPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buildRankLegend() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 8, 2, 8);
        gbc.gridy = 0;

        JLabel legendTitle = new JLabel("Buff Rank Sistem (berdasarkan Total Achievement):");
        legendTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
        legendTitle.setForeground(GameConfig.COLOR_TEXT_LIGHT);
        gbc.gridx = 0; gbc.gridwidth = 5;
        panel.add(legendTitle, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        for (RankTier tier : RankTier.values()) {
            JLabel lbl = new JLabel("● " + tier.getDisplayName() + " (" + tier.getMinScore() + "+)");
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
            lbl.setForeground(tier.getBadgeColor());
            panel.add(lbl, gbc);
            gbc.gridx++;
        }

        return panel;
    }

    private void addInfoLabel(JPanel panel, String text, Color color, int style) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", style, 12));
        lbl.setForeground(color);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(lbl);
    }

    private void updateRankPreview() {
        // Akan diisi oleh presenter melalui setter listener jika dibutuhkan
        rankPreviewLabel.setText("🏆 Rank: (tekan Play untuk memuat data achievement)");
    }

    public void updateRankPreviewForUser(Achievement ach) {
        if (ach == null) {
            rankPreviewLabel.setText("🌱 Pemain baru — Rank: Ranger Recruit");
            rankPreviewLabel.setForeground(GameConfig.COLOR_TEXT_MUTED);
        } else {
            RankTier tier = ach.getRankTier();
            rankPreviewLabel.setText("🏆 " + tier.getDisplayName() + " — Achievement: " + ach.getAchievement() + " pts (Speed ×" + tier.getSpeedMultiplier() + ")");
            rankPreviewLabel.setForeground(tier.getBadgeColor());
        }
    }

    // ===== Implementasi IMenuView =====

    @Override
    public void displayAchievements(List<Achievement> list) {
        tableModel.setRowCount(0);
        int rank = 1;
        for (Achievement ach : list) {
            tableModel.addRow(new Object[]{
                rank++,
                ach.getUsername(),
                ach.getAchievement(),
                ach.getDestroyer(),
                ach.getFire(),
                ach.getRankTier().getDisplayName()
            });
        }
    }

    @Override
    public String getUsernameInput() {
        return tfUsername.getText().trim();
    }

    @Override
    public void setUsernameInput(String username) {
        tfUsername.setText(username);
    }

    @Override
    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    @Override
    public void setDatabaseStatus(String text, boolean isConnected) {
        dbStatusLabel.setText("● " + text);
        dbStatusLabel.setForeground(isConnected ? new Color(46, 204, 113) : Color.ORANGE);
    }

    // ===== Setter Listener untuk Presenter =====

    public void setPlayListener(ActionListener listener) {
        this.playListener = listener;
    }

    public void setRefreshListener(ActionListener listener) {
        this.refreshListener = listener;
    }

    public void triggerRankPreviewUpdate(Achievement ach) {
        updateRankPreviewForUser(ach);
    }
}

