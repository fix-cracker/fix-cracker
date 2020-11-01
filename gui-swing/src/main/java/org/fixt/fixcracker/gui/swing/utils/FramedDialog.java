package org.fixt.fixcracker.gui.swing.utils;


import org.fixt.fixcracker.gui.swing.actions.AbstractOkAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * @author fixt
 */
public class FramedDialog extends JDialog {

    public static boolean show(IDialogFrame fm, String title) {
        return show(fm, title, fm.getOwnerFrame(), true, true);
    }

    public static boolean show(IDialogFrame fm, String title, Frame owner, boolean allowCloseWindowByCorner, boolean closeAfterPerform) {
        FramedDialog dlg = new FramedDialog(owner, title, allowCloseWindowByCorner);
        fm.init(dlg);
        dlg.fm = fm;
        dlg.add((Component) fm, BorderLayout.CENTER);
        dlg.setCloseAfterPerform(closeAfterPerform);
        dlg.pack();
        dlg.setLocationRelativeTo(owner);
        dlg.setVisible(true);
        return dlg.dlgResult;
    }

    private IDialogFrame fm;
    private boolean dlgResult;
    private boolean closeAfterPerform;
    private WindowCloseQueryInterface closeConstraint;
    private JPanel buttonPanel;
    private JButton btnOK;
    private JButton btnCancel;
    private OkAction okAction;
    private CancelDialogAction acCancel;


    private FramedDialog(Frame owner, String caption, boolean allowCloseWindowByCorner) {
        super(owner, caption);
        this.dlgResult = false;

        JRootPane rootPane = this.getRootPane();
        InputMap iMap = rootPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
        if (!allowCloseWindowByCorner) {
            this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            addWindowListener(new DialogWindowListener());
        } else {
            this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        }
        this.setModal(true);

        ActionMap aMap = rootPane.getActionMap();

        aMap.put("escape", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
//	   listener.windowClosed(null);
                dispose();
            }
        });


        setLayout(new BorderLayout());
        buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

//        add(getTestPanel(),BorderLayout.NORTH);

    }

    private JPanel createButtonPanel() {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        okAction = new OkAction();
        btnOK = new JButton(okAction);
        acCancel = new CancelDialogAction(this);
        btnCancel = new JButton(acCancel);
        Dimension d = new Dimension(120, 25);
        pnl.add(btnOK);
        pnl.add(btnCancel);
        btnOK.setSize(d);
        btnCancel.setSize(d);
        btnOK.setPreferredSize(d);
        btnCancel.setPreferredSize(d);
        return pnl;
    }

    public AbstractAction getOKAction() {
        return okAction;
    }


    public void setCloseConstraint(WindowCloseQueryInterface closeConstraint) {
        this.closeConstraint = closeConstraint;
        AcCloseDialogEx ac;
        if (!(acCancel instanceof AcCloseDialogEx)) {
            acCancel = new AcCloseDialogEx(this);
            btnCancel.setAction(acCancel);
        }
        ac = (AcCloseDialogEx) acCancel;
        ac.setIntf(closeConstraint);
    }

    public JButton getBtnOK() {
        return btnOK;
    }

    public JButton getBtnCancel() {
        return btnCancel;
    }

    public JPanel getButtonPanel() {
        return buttonPanel;
    }


    private class AcCloseDialogEx extends CloseDialogAction {

        private WindowCloseQueryInterface intf;

        public AcCloseDialogEx(Dialog dlg) {
            super(dlg);
        }

        public void setIntf(WindowCloseQueryInterface intf) {
            this.intf = intf;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (intf != null) {
                if (!intf.closeWindowQuery()) {
                    return;
                }
            }
            super.actionPerformed(e);
        }
    }

    private class OkAction extends AbstractOkAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            String msg = fm.getValidationMessage();
            if (msg != null && !msg.equals("")) {
                JOptionPane.showMessageDialog(null, msg);
                return;
            }
            dlgResult = fm.performDialogAction(FramedDialog.this);
            if (dlgResult) {
                if (closeAfterPerform) {
                    FramedDialog.this.setVisible(false);
                    FramedDialog.this.dispose();
                } else {
                    btnOK.setEnabled(false);
                }
            }
        }
    }

    public void setCloseAfterPerform(boolean closeAfterPerform) {
        this.closeAfterPerform = closeAfterPerform;
    }

    private class DialogWindowListener implements WindowListener {

        @Override
        public void windowOpened(WindowEvent e) {
//            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void windowClosing(WindowEvent e) {
            if (closeConstraint != null) {
                if (!closeConstraint.closeWindowQuery()) {
                    return;
                }
            }
            setVisible(false);
            dispose();
        }

        @Override
        public void windowClosed(WindowEvent e) {
//            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void windowIconified(WindowEvent e) {
//            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
//            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void windowActivated(WindowEvent e) {
//            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
//            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
