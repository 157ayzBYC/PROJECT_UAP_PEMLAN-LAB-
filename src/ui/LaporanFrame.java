package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class LaporanFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JTextField tfCari;
    private final String FILE_PATH = "data/transaksi.csv";

    public LaporanFrame() {
        setTitle("Laporan / Riwayat Service");
        setSize(800, 500);
        setLocationRelativeTo(null);

        model = new DefaultTableModel(
                new String[]{"ID", "Pelanggan", "Kendaraan", "Service", "Biaya", "Tanggal"}, 0
        );
        table = new JTable(model);

        tfCari = new JTextField(20);
        JButton btnCari = new JButton("Cari");
        btnCari.addActionListener(e -> cariData());

        JButton btnSortByDate = new JButton("Sort by Tanggal");
        btnSortByDate.addActionListener(e -> sortBy("Tanggal"));

        JButton btnSortByBiaya = new JButton("Sort by Biaya");
        btnSortByBiaya.addActionListener(e -> sortBy("Biaya"));

        JButton btnHome = new JButton("Home");
        btnHome.addActionListener(e -> {
            dispose();  // Menutup frame Laporan
            new DashboardFrame().setVisible(true);  // Membuka DashboardFrame
        });

        // Panel untuk pencarian dan tombol sort
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Cari Pelanggan:"));
        topPanel.add(tfCari);
        topPanel.add(btnCari);
        topPanel.add(btnSortByDate);
        topPanel.add(btnSortByBiaya);

        loadData();  // Memuat data transaksi dari file

        // Panel untuk menampilkan total pendapatan
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(new JLabel("Total Pendapatan: "));
        JLabel lblTotalPendapatan = new JLabel("0");
        bottomPanel.add(lblTotalPendapatan);

        // Menambahkan komponen ke dalam frame
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        add(btnHome, BorderLayout.EAST);

        // Menampilkan total pendapatan
        double totalPendapatan = getTotalPendapatan();
        lblTotalPendapatan.setText("Rp. " + totalPendapatan);
    }

    // Memuat data dari CSV ke dalam tabel
    private void loadData() {
        model.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                model.addRow(line.split(","));
            }
        } catch (IOException ignored) {}
    }

    // Mencari data berdasarkan nama pelanggan
    private void cariData() {
        String keyword = tfCari.getText().toLowerCase();
        model.setRowCount(0);

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.toLowerCase().contains(keyword)) {
                    model.addRow(line.split(","));
                }
            }
        } catch (IOException ignored) {}
    }

    // Sorting berdasarkan Tanggal atau Biaya
    private void sortBy(String column) {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                rows.add(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Sorting berdasarkan kolom yang dipilih
        if (column.equals("Tanggal")) {
            rows = rows.stream()
                    .sorted(Comparator.comparing(o -> o[5]))  // Kolom Tanggal ada di indeks 5
                    .collect(Collectors.toList());
        } else if (column.equals("Biaya")) {
            rows = rows.stream()
                    .sorted(Comparator.comparingDouble(o -> Double.parseDouble(o[4])))  // Kolom Biaya ada di indeks 4
                    .collect(Collectors.toList());
        }

        // Memasukkan data yang sudah diurutkan ke dalam model tabel
        model.setRowCount(0);
        for (String[] row : rows) {
            model.addRow(row);
        }
    }

    // Menghitung total pendapatan
    private double getTotalPendapatan() {
        double total = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    try {
                        double biaya = Double.parseDouble(data[4]);  // Kolom Biaya ada di indeks 4
                        total += biaya;
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return total;
    }

    public static void main(String[] args) {
        // Menjalankan aplikasi
        SwingUtilities.invokeLater(() -> new LaporanFrame().setVisible(true));
    }
}
