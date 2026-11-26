/*
 * Saya Najib Nurohman dengan NIM 2509653 sebagai manusia yang berTuhan, 
 * saya mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah Desain dan Pemrograman Berorientasi 
 * Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
 */

package ecowarrior.view.components;

import ecowarrior.config.GameConfig;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * Komponen JTable kustom dengan visual modern, striping warna baris,
 * dan perataan data sesuai tabel tachievement pada spesifikasi soal.
 * 
 * @author Najib Nurohman (2509653) - Kelas C1
 */
public class ModernTable extends JTable {

    public ModernTable(DefaultTableModel model) {
        super(model);
        setupStyle();
    }

    private void setupStyle() {
        setFont(new Font("SansSerif", Font.PLAIN, 14));
        setRowHeight(32);
        setShowGrid(true);
        setGridColor(new Color(45, 60, 50));
        setSelectionBackground(new Color(46, 204, 113, 100));
        setSelectionForeground(Color.WHITE);
        setBackground(new Color(24, 34, 28));
        setForeground(GameConfig.COLOR_TEXT_LIGHT);

        // Styling Header Tabel
        JTableHeader header = getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBackground(new Color(36, 52, 42));
        header.setForeground(new Color(46, 204, 113));
        header.setPreferredSize(new Dimension(header.getWidth(), 38));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(46, 204, 113)));

        // Renderer khusus untuk baris berselang-seling (Zebra Stripe)
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (isSelected) {
                    c.setBackground(new Color(39, 174, 96, 180));
                    c.setForeground(Color.WHITE);
                } else {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(24, 34, 28));
                    } else {
                        c.setBackground(new Color(30, 42, 34));
                    }
                    c.setForeground(GameConfig.COLOR_TEXT_LIGHT);
                }

                if (column == 0) {
                    setHorizontalAlignment(JLabel.LEFT);
                    setFont(new Font("SansSerif", Font.BOLD, 13));
                } else {
                    setHorizontalAlignment(JLabel.CENTER);
                    setFont(new Font("SansSerif", Font.PLAIN, 13));
                }

                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        });
    }
}

