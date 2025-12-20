package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VehicleFrame extends JFrame {

    public VehicleFrame() {
        setTitle("Data Kendaraan");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 450);
        setLocationRelativeTo(null);

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Plat", "Customer", "Merk", "Tipe", "Tahun"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable table = new JTable(model);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottom.add(new JButton("Tambah"));
        bottom.add(new JButton("Edit"));
        bottom.add(new JButton("Hapus"));

        JButton btnTutup = new JButton("Tutup");
        btnTutup.addActionListener(e -> dispose());
        bottom.add(btnTutup);

        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }
}
