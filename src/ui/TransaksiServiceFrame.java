package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;

public class TransaksiServiceFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private final String FILE_PATH = "data/transaksi.csv";

    public TransaksiServiceFrame() {
        setTitle("Transaksi Service");
        setSize(800, 400);
        setLocationRelativeTo(null);

        // Menentukan model tabel dengan 5 kolom (ID, Pelanggan, Kendaraan, Service, Biaya)
        model = new DefaultTableModel(
                new String[]{"ID", "Pelanggan", "Kendaraan", "Service", "Biaya"}, 0
        );
        table = new JTable(model);

        // Memuat data dari CSV saat frame dibuka
        loadData();

        // Tombol untuk menambah transaksi
        JButton btnTambah = new JButton("Tambah Transaksi");
        btnTambah.addActionListener(e -> tambahTransaksi());

        // Tombol Home untuk kembali ke Dashboard
        JButton btnHome = new JButton("Home");
        btnHome.addActionListener(e -> {
            // Menutup frame TransaksiServiceFrame dan membuka Dashboard
            dispose();  // Menutup frame ini
            new DashboardFrame().setVisible(true);  // Membuka frame Dashboard
        });

        // Menambahkan komponen ke dalam frame
        JPanel panel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnTambah);
        buttonPanel.add(btnHome);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
    }

    // Metode untuk memuat data dari file CSV
    private void loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Memisahkan data berdasarkan koma
                String[] data = line.split(",");
                
                // Pastikan data yang dimuat memiliki 5 kolom sesuai dengan header CSV
                if (data.length == 5) {
                    model.addRow(new Object[]{
                        data[0],  // id
                        data[1],  // pelanggan
                        data[2],  // kendaraan
                        data[3],  // service
                        data[4]   // biaya
                    });
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data transaksi", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metode untuk menambah transaksi baru
    private void tambahTransaksi() {
        JTextField tfPelanggan = new JTextField();
        JTextField tfKendaraan = new JTextField();
        JTextField tfService = new JTextField();
        JTextField tfBiaya = new JTextField();

        Object[] form = {
                "Nama Pelanggan:", tfPelanggan,
                "Kendaraan:", tfKendaraan,
                "Jenis Service:", tfService,
                "Biaya:", tfBiaya
        };

        int result = JOptionPane.showConfirmDialog(this, form,
                "Tambah Transaksi", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String pelanggan = tfPelanggan.getText().trim();
            String kendaraan = tfKendaraan.getText().trim();
            String service = tfService.getText().trim();
            String biayaText = tfBiaya.getText().trim();

            // Validasi input
            if (pelanggan.isEmpty() || kendaraan.isEmpty() || service.isEmpty() || biayaText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua kolom harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validasi biaya harus berupa angka
            double biaya;
            try {
                biaya = Double.parseDouble(biayaText);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Biaya harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Mendapatkan ID terakhir dan meningkatkan 1 untuk ID berikutnya
            int idBaru = getNextId();

            // Menambahkan baris baru ke dalam tabel
            model.addRow(new Object[]{
                    idBaru,
                    pelanggan,
                    kendaraan,
                    service,
                    biayaText
            });

            // Menyimpan data transaksi ke dalam file CSV
            saveData();
        }
    }

    // Mendapatkan ID terakhir yang ada di file CSV dan menghitung ID berikutnya
    private int getNextId() {
        int lastId = 0;

        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return 1;  // Jika file belum ada, mulai dari ID 1
        }

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                // Pastikan data memiliki 5 kolom dan ID dapat diubah menjadi angka
                if (data.length == 5) {
                    try {
                        int id = Integer.parseInt(data[0]);
                        if (id > lastId) {
                            lastId = id;
                        }
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data transaksi", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return lastId + 1;  // Mengembalikan ID berikutnya
    }

    // Metode untuk menyimpan data ke dalam file CSV
    private void saveData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            // Menulis header CSV
            bw.write("id,pelanggan,kendaraan,service,biaya");
            bw.newLine();
            
            // Menyimpan setiap baris data di tabel ke file CSV
            for (int i = 0; i < model.getRowCount(); i++) {
                bw.write(
                        model.getValueAt(i, 0) + "," +  // id
                        model.getValueAt(i, 1) + "," +  // pelanggan
                        model.getValueAt(i, 2) + "," +  // kendaraan
                        model.getValueAt(i, 3) + "," +  // service
                        model.getValueAt(i, 4)          // biaya
                );
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan transaksi", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Menjalankan aplikasi
        SwingUtilities.invokeLater(() -> new TransaksiServiceFrame().setVisible(true));
    }
}
