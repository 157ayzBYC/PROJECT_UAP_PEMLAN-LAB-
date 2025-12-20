package ui;

import data.CustomerCsvRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerFrame extends JFrame {

    private DefaultTableModel model;
    private JTable table;

    private final CustomerCsvRepository repo = new CustomerCsvRepository("data/customers.csv");

    public CustomerFrame() {
        initUI();
        loadFromCsv();
    }

    private void initUI() {
        setTitle("Data Pelanggan");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 450);
        setLocationRelativeTo(null);

        model = new DefaultTableModel(new Object[]{"ID", "Nama", "No HP", "Alamat"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");
        JButton btnTutup = new JButton("Tutup");

        btnTambah.addActionListener(e -> onTambah());
        btnEdit.addActionListener(e -> onEdit());
        btnHapus.addActionListener(e -> onHapus());
        btnTutup.addActionListener(e -> dispose());

        bottom.add(btnTambah);
        bottom.add(btnEdit);
        bottom.add(btnHapus);
        bottom.add(btnTutup);

        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    private void loadFromCsv() {
        model.setRowCount(0);
        List<String[]> rows = repo.loadAll();
        for (String[] r : rows) {
            model.addRow(new Object[]{r[0], r[1], r[2], r[3]});
        }
    }

    private void saveToCsv() {
        List<String[]> rows = new ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            rows.add(new String[]{
                    String.valueOf(model.getValueAt(i, 0)),
                    String.valueOf(model.getValueAt(i, 1)),
                    String.valueOf(model.getValueAt(i, 2)),
                    String.valueOf(model.getValueAt(i, 3))
            });
        }
        repo.saveAll(rows);
    }

    private void onTambah() {
        CustomerFormDialog dialog = new CustomerFormDialog(this, "Tambah Pelanggan");
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            if (existsId(dialog.getId())) {
                JOptionPane.showMessageDialog(this, "ID sudah dipakai. Gunakan ID lain.");
                return;
            }
            model.addRow(new Object[]{
                    dialog.getId(),
                    dialog.getNama(),
                    dialog.getNoHp(),
                    dialog.getAlamat()
            });
            saveToCsv();
        }
    }

    private void onEdit() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang mau diedit.");
            return;
        }

        String id = String.valueOf(model.getValueAt(row, 0));
        String nama = String.valueOf(model.getValueAt(row, 1));
        String noHp = String.valueOf(model.getValueAt(row, 2));
        String alamat = String.valueOf(model.getValueAt(row, 3));

        CustomerFormDialog dialog = new CustomerFormDialog(this, "Edit Pelanggan");
        dialog.setData(id, nama, noHp, alamat);
        dialog.setIdEditable(false);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            model.setValueAt(dialog.getNama(), row, 1);
            model.setValueAt(dialog.getNoHp(), row, 2);
            model.setValueAt(dialog.getAlamat(), row, 3);
            saveToCsv();
        }
    }

    private void onHapus() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang mau dihapus.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Yakin hapus data ini?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            model.removeRow(row);
            saveToCsv();
        }
    }

    private boolean existsId(String id) {
        for (int i = 0; i < model.getRowCount(); i++) {
            String existing = String.valueOf(model.getValueAt(i, 0));
            if (existing.equalsIgnoreCase(id)) return true;
        }
        return false;
    }
}
