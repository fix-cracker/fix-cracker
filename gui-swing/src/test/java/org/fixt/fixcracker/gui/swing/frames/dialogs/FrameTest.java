package org.fixt.fixcracker.gui.swing.frames.dialogs;

import javax.swing.*;
import java.awt.*;

public class FrameTest extends JFrame {

    public FrameTest(JComponent component) throws HeadlessException {
        JPanel pnlMain = new JPanel();
        pnlMain.setLayout(new BorderLayout(0, 0));
        pnlMain.setPreferredSize(new Dimension(700, 500));
        setContentPane(pnlMain);
        pnlMain.add(component, BorderLayout.CENTER);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();

        setLocationRelativeTo(null);// put into center
    }
}
