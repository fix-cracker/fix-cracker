package org.fixt.fixcracker.gui.swing.frames.dialogs;

import org.fixt.fixcracker.gui.swing.Configuration;
import org.fixt.fixcracker.gui.swing.frames.SessionsListFrame;

import javax.swing.*;
import java.awt.*;

public class SessionsListFrameTest extends FrameTest {
    public static void main(String... args) {
        SwingUtilities.invokeLater(() -> {
            SessionsListFrame frame = new SessionsListFrame(Configuration.FIX_SESSION_STORAGE);
            frame.init();
            SessionsListFrameTest frm = new SessionsListFrameTest(frame);
            frm.setVisible(true);
        });
    }

    public SessionsListFrameTest(SessionsListFrame frame) throws HeadlessException {
        super(frame);
    }
}
