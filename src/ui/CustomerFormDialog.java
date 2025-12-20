package ui;

import javax.swing.*;
import java.awt.*;

public class CustomerFormDialog extends JDialog {

    private JTextField tfId;
    private JTextField tfNama;
    private JTextField tfNoHp;
    private JTextField tfAlamat;

    private boolean saved = false;

    public CustomerFormDialog(JFrame parent, String title) {
        super(parent, title, true);
        initUI();
    }

    private void initUI() {
        setSize(420, 260);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tfId = new JTextField();
        tfNama = new JTextField();
        tfNoHp = new JTextField();
        tfAlamat = new JTextField();

        form.add(new JLabel("ID:"));
        form.add(tfId);
        form.add(new JLabel("Nama:"));
        form.add(tfNama);
        form.add(new JLabel("No HP:"));
        form.add(tfNoHp);
        form.add(new JLabel("Alamat:"));
        form.add(tfAlamat);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnSimpan = new JButton("Simpan");
        JButton btnBatal = new JButton("Batal");

        btnSimpan.addActionListener(e -> onSave());
        btnBatal.addActionListener(e -> dispose());

        actions.add(btnSimpan);
        actions.add(btnBatal);

        add(form, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);
    }

    private void onSave() {
        if (getId().isEmpty() || getNama().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID dan Nama wajib diisi.");
            return;
        }
        saved = true;
        dispose();
    }

    public boolean isSaved() { return saved; }

    public String getId() { return tfId.getText().trim(); }
    public String getNama() { return tfNama.getText().trim(); }
    public String getNoHp() { return tfNoHp.getText().trim(); }
    public String getAlamat() { return tfAlamat.getText().trim(); }

    public void setData(String id, String nama, String noHp, String alamat) {
        tfId.setText(id);
        tfNama.setText(nama);
        tfNoHp.setText(noHp);
        tfAlamat.setText(alamat);
    }

    public void setIdEditable(boolean editable) {
        tfId.setEditable(editable);
    }
}
