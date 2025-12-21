package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;


public class VehicleFrame extends JFrame {

    private DefaultTableModel model;
    private JTable table;
    private final String FILE_PATH = "data/kendaraan.csv";

    public VehicleFrame() {
        setTitle("Data Kendaraan");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 450);
        setLocationRelativeTo(null);

        model = new DefaultTableModel(
                new Object[]{"Plat", "Customer", "Merk", "Tipe", "Tahun"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;  // Kolom tidak dapat diedit secara langsung
            }
        };

        table = new JTable(model);
        loadFromCSV();

        // Panel bawah untuk tombol-tombol
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        // Tombol Tambah
        JButton btnTambah = new JButton("Tambah");
        btnTambah.addActionListener(e -> tambahData(model));
        bottom.add(btnTambah);

        // Tombol Edit
        JButton btnEdit = new JButton("Edit");
        btnEdit.addActionListener(e -> editData(table, model));
        bottom.add(btnEdit);

        // Tombol Hapus
        JButton btnHapus = new JButton("Hapus");
        btnHapus.addActionListener(e -> hapusData(table, model));
        bottom.add(btnHapus);

        // Tombol Tutup
        JButton btnTutup = new JButton("Tutup");
        btnTutup.addActionListener(e -> dispose());
        bottom.add(btnTutup);

        // Menambahkan komponen ke layout
        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    // Fungsi untuk menambah data
    private void tambahData(DefaultTableModel model) {
        JTextField tfPlat = new JTextField();
        JTextField tfCustomer = new JTextField();
        JTextField tfMerk = new JTextField();
        JTextField tfTipe = new JTextField();
        JTextField tfTahun = new JTextField();

        // Form input untuk tambah data
        Object[] form = {
                "Plat:", tfPlat,
                "Customer:", tfCustomer,
                "Merk:", tfMerk,
                "Tipe:", tfTipe,
                "Tahun:", tfTahun
        };

        if (JOptionPane.showConfirmDialog(this, form, "Tambah Kendaraan",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            // Validasi input
            if (tfPlat.getText().isEmpty() || tfCustomer.getText().isEmpty() || tfMerk.getText().isEmpty() ||
                tfTipe.getText().isEmpty() || tfTahun.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua kolom harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Menambahkan data ke model tabel
            model.addRow(new Object[]{
                    tfPlat.getText(),
                    tfCustomer.getText(),
                    tfMerk.getText(),
                    tfTipe.getText(),
                    tfTahun.getText()
            });

            saveToCSV();
        }
    }

    // Fungsi untuk mengedit data
    private void editData(JTable table, DefaultTableModel model) {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih data yang akan diedit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Mendapatkan data yang dipilih
        String plat = model.getValueAt(selectedRow, 0).toString();
        String customer = model.getValueAt(selectedRow, 1).toString();
        String merk = model.getValueAt(selectedRow, 2).toString();
        String tipe = model.getValueAt(selectedRow, 3).toString();
        String tahun = model.getValueAt(selectedRow, 4).toString();

        // Form input untuk edit data
        JTextField tfPlat = new JTextField(plat);
        JTextField tfCustomer = new JTextField(customer);
        JTextField tfMerk = new JTextField(merk);
        JTextField tfTipe = new JTextField(tipe);
        JTextField tfTahun = new JTextField(tahun);

        Object[] form = {
                "Plat:", tfPlat,
                "Customer:", tfCustomer,
                "Merk:", tfMerk,
                "Tipe:", tfTipe,
                "Tahun:", tfTahun
        };

        int option = JOptionPane.showConfirmDialog(this, form, "Edit Kendaraan", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            // Validasi input
            if (tfPlat.getText().isEmpty() || tfCustomer.getText().isEmpty() || tfMerk.getText().isEmpty() ||
                tfTipe.getText().isEmpty() || tfTahun.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua kolom harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Mengupdate data pada tabel
            model.setValueAt(tfPlat.getText(), selectedRow, 0);
            model.setValueAt(tfCustomer.getText(), selectedRow, 1);
            model.setValueAt(tfMerk.getText(), selectedRow, 2);
            model.setValueAt(tfTipe.getText(), selectedRow, 3);
            model.setValueAt(tfTahun.getText(), selectedRow, 4);
        }
    }

    // Fungsi untuk menghapus data
    private void hapusData(JTable table, DefaultTableModel model) {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih data yang akan dihapus.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            model.removeRow(selectedRow);
            saveToCSV();
        }
    }

    public static void main(String[] args) {
        // Menjalankan aplikasi
        SwingUtilities.invokeLater(() -> new VehicleFrame().setVisible(true));
    }


    private void loadFromCSV() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                model.addRow(line.split(","));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal membaca file CSV");
        }
    }

    private void saveToCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (int i = 0; i < model.getRowCount(); i++) {
                bw.write(
                        model.getValueAt(i, 0) + "," +
                        model.getValueAt(i, 1) + "," +
                        model.getValueAt(i, 2) + "," +
                        model.getValueAt(i, 3) + "," +
                        model.getValueAt(i, 4)
                );
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan ke CSV");
        }
    }
}