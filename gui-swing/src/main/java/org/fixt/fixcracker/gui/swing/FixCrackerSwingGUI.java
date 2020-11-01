package org.fixt.fixcracker.gui.swing;

import javax.swing.*;

public class FixCrackerSwingGUI {
    public static volatile MainWindowSwing mainWindow;

    public static void main(String... args) {

        SwingUtilities.invokeLater(() -> {
            enableNimbusLookAndFeel();
            mainWindow = new MainWindowSwing();
            mainWindow.setVisible(true);
        });


    }

    private static void enableNimbusLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
    }
}
