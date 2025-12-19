package ui;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("Aplikasi Manajemen Bengkel Service");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 420);
        setLocationRelativeTo(null);

        JLabel lblTitle = new JLabel("Aplikasi Manajemen Bengkel Service", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel panelMenu = new JPanel(new GridLayout(5, 1, 10, 10));
        panelMenu.setBorder(BorderFactory.createEmptyBorder(25, 70, 25, 70));

        JButton btnPelanggan = createMenuButton("Kelola Data Pelanggan");
        JButton btnKendaraan = createMenuButton("Kelola Data Kendaraan");
        JButton btnTransaksi = createMenuButton("Transaksi Service");
        JButton btnLaporan = createMenuButton("Laporan / Riwayat");
        JButton btnKeluar = createMenuButton("Keluar");

        //tombol ini diarahkan ke frame masing-masing
        btnPelanggan.addActionListener(e -> JOptionPane.showMessageDialog(this, "Menu Pelanggan (belum dibuat)"));
        btnKendaraan.addActionListener(e -> JOptionPane.showMessageDialog(this, "Menu Kendaraan (belum dibuat)"));
        btnTransaksi.addActionListener(e -> JOptionPane.showMessageDialog(this, "Menu Transaksi (belum dibuat)"));
        btnLaporan.addActionListener(e -> JOptionPane.showMessageDialog(this, "Menu Laporan (belum dibuat)"));

        btnKeluar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Keluar dari aplikasi?",
                    "Konfirmasi",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
            }
        });

        panelMenu.add(btnPelanggan);
        panelMenu.add(btnKendaraan);
        panelMenu.add(btnTransaksi);
        panelMenu.add(btnLaporan);
        panelMenu.add(btnKeluar);

        setLayout(new BorderLayout());
        add(lblTitle, BorderLayout.NORTH);
        add(panelMenu, BorderLayout.CENTER);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        return button;
    }
}
