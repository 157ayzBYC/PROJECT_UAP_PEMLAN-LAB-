package app;

import javax.swing.SwingUtilities;
import ui.DashboardFrame;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardFrame().setVisible(true));
    }
}
