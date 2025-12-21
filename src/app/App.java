package app;

import ui.DashboardFrame;  // Pastikan import ini ada

public class App {
    public static void main(String[] args) {
        // Menjalankan aplikasi dan menampilkan Dashboard terlebih dahulu
        javax.swing.SwingUtilities.invokeLater(() -> new DashboardFrame().setVisible(true));
    }
}
