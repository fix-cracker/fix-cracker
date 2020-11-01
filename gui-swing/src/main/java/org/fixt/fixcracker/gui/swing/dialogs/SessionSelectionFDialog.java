package org.fixt.fixcracker.gui.swing.dialogs;

import org.fixt.fixcracker.core.domain.FIXSession;
import org.fixt.fixcracker.gui.swing.Configuration;
import org.fixt.fixcracker.gui.swing.frames.SessionsListFrame;
import org.fixt.fixcracker.gui.swing.utils.FramedDialog;
import org.fixt.fixcracker.gui.swing.utils.IDialogFrame;

import javax.swing.*;
import java.awt.*;

public class SessionSelectionFDialog extends JPanel implements IDialogFrame {
    private SessionsListFrame frame;

    public SessionSelectionFDialog() {
        super(new BorderLayout());
        frame = new SessionsListFrame(Configuration.FIX_SESSION_STORAGE);
        add(frame, BorderLayout.CENTER);
    }

    @Override
    public void init(FramedDialog dlg) {
        frame.init();
        frame.setDblClickAction(dlg.getOKAction());
    }

    @Override
    public String getValidationMessage() {
        return frame.getSelected() != null ? "" : "Please select FIX session";
    }

    @Override
    public boolean performDialogAction(FramedDialog dlg) {
        return true;
    }

    @Override
    public Frame getOwnerFrame() {
        return null;
    }

    public FIXSession getSelectedSesssion() {
        return frame.getSelected();
    }
}
