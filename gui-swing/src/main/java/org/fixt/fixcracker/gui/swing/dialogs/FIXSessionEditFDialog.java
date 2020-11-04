package org.fixt.fixcracker.gui.swing.dialogs;

import org.fixt.fixcracker.core.domain.FIXSession;
import org.fixt.fixcracker.gui.swing.Configuration;
import org.fixt.fixcracker.gui.swing.FixCrackerSwingConst;
import org.fixt.fixcracker.gui.swing.FixCrackerSwingGUI;
import org.fixt.fixcracker.gui.swing.Images;
import org.fixt.fixcracker.gui.swing.utils.DefaultMapComboBoxModel;
import org.fixt.fixcracker.gui.swing.utils.FramedDialog;
import org.fixt.fixcracker.gui.swing.utils.IDialogFrame;
import org.fixt.fixcracker.gui.swing.utils.MapEntryViewItem;
import org.fixt.fixcracker.core.FIXCrackerConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class FIXSessionEditFDialog extends JPanel implements IDialogFrame {
    private static final Logger LOG = LoggerFactory.getLogger(FIXSessionEditFDialog.class);
    private static final FIXSessionEditFDialog dlg = new FIXSessionEditFDialog();
    private DialogMode mode;
    private FIXSession session;

    public static FIXSession add() {
        dlg.initControls(DialogMode.ADD, null);
        if (FramedDialog.show(dlg, "Add new FIX session")) {
            return dlg.getValue();
        }
        return null;
    }


    public static FIXSession edit(FIXSession session) {
        dlg.initControls(DialogMode.EDIT, session);
        if (FramedDialog.show(dlg, "Edit FIX session")) {
            return dlg.getValue();
        }
        return null;
    }


    private FIXSession getValue() {
        MapEntryViewItem<Integer, String> selectedApplVerID = (MapEntryViewItem<Integer, String>) applVerIDCBox.getSelectedItem();
        if (session == null) {
            session = new FIXSession("", "", 0, "", 0, 0, false, "");
        }
        session.setName(nameEdit.getText().trim());
        session.setHost(hostEdit.getText().trim());
        session.setPort((Integer) portEdit.getValue());
        session.setBeginString((String) beginStringCBox.getSelectedItem());
        session.setDefaultApplVerID(Objects.requireNonNull(selectedApplVerID).getEntry().getKey());
        session.setHeartBtInt((Integer) heartbeatIntEdit.getValue());
        session.setResetOnLogon(resetOnLogonCheckbox.isSelected());
        session.setSenderCompID(senderCompIDEdit.getText());
        session.setTargetCompID(targetCompIDEdit.getText());
        return session;
    }

    private JTextField nameEdit = new JTextField();
    private JTextField hostEdit = new JTextField();
    private JSpinner portEdit = new JSpinner(new SpinnerNumberModel(1234, 1, 65535, 1));
    private JTextField senderCompIDEdit = new JTextField();
    private JTextField targetCompIDEdit = new JTextField();
    private JComboBox<String> beginStringCBox = new JComboBox<>(FIXSession.BEGIN_STRING_ARRAY);
    private JComboBox<MapEntryViewItem<Integer, String>> applVerIDCBox = new JComboBox<>(new DefaultMapComboBoxModel<>(FIXSession.APPL_VER_IDS));
    private JSpinner heartbeatIntEdit = new JSpinner(new SpinnerNumberModel(30, 0, 1000, 1));
    private JCheckBox resetOnLogonCheckbox = new JCheckBox("ResetOnLogon");

    public FIXSessionEditFDialog() {
        super(new GridLayout(0, 2));
        setBorder(FixCrackerSwingConst.BORDER_DIALOG_DEFAULT);
        add(new JLabel("Name"));
        add(nameEdit);
        add(new JLabel("Host"));
        add(hostEdit);
        add(new JLabel("Port"));
        add(portEdit);
        add(new JLabel("SenderCompID"));
        add(senderCompIDEdit);
        add(new JLabel("TargetCompID"));
        add(targetCompIDEdit);
        add(new JLabel("BeginString"));
        add(beginStringCBox);
        add(new JLabel("ApplVerID"));
        add(applVerIDCBox);
        add(new JLabel("Heartbeat interval"));
        add(heartbeatIntEdit);

        add(resetOnLogonCheckbox);

    }

    private void initControls(DialogMode mode, FIXSession session) {
        this.mode = mode;
        this.session = session;
    }

    @Override
    public void init(FramedDialog dlg) {
        if (session != null) {
            nameEdit.setText(session.getName());
            hostEdit.setText(session.getHost());
            portEdit.setValue(session.getPort());
            senderCompIDEdit.setText(session.getSenderCompID());
            targetCompIDEdit.setText(session.getTargetCompID());
            beginStringCBox.setSelectedItem(session.getBeginString());
            applVerIDCBox.setSelectedItem(session.getDefaultApplVerID());
            heartbeatIntEdit.setValue(session.getHeartBtInt());
            resetOnLogonCheckbox.setSelected(session.isResetOnLogon());
        } else {
            nameEdit.setText("");
            hostEdit.setText("");
            portEdit.setValue(1234);
            senderCompIDEdit.setText("FIX_CRACKER");
            targetCompIDEdit.setText("");
            beginStringCBox.setSelectedItem(FIXSession.BEGIN_STRING_DEFAULT);
            applVerIDCBox.setSelectedItem(FIXSession.APPL_VER_ID_DEFAULT);
            heartbeatIntEdit.setValue(FIXSession.HEARTBEAT_INTERVAL_DEFAULT);
            resetOnLogonCheckbox.setSelected(false);
        }
        nameEdit.requestFocus();
    }

    @Override
    public String getValidationMessage() {
        if (validateTextBoxIsEmpty(nameEdit)) return "Session name cannot be empty!";
        if (validateTextBoxIsEmpty(hostEdit)) return "Host cannot be empty!";
        if (portEdit.getValue() == null) {
            portEdit.requestFocus();
            return "Port cannot be empty!";
        }
        if (validateTextBoxIsEmpty(senderCompIDEdit)) return "SenderCompID cannot be empty!";
        if (validateTextBoxIsEmpty(targetCompIDEdit)) return "TargetCompID cannot be empty!";
        return null;
    }

    private boolean validateTextBoxIsEmpty(JTextField tb) {
        if ("".equals(tb.getText().trim())) {
            tb.requestFocus();
            return true;
        }
        return false;
    }

    @Override
    public boolean performDialogAction(FramedDialog dlg) {
        FIXSession value = getValue();
        try {
            switch (mode) {
                case ADD:
                    Configuration.FIX_SESSION_STORAGE.add(value);
                    break;
                case EDIT:
                    Configuration.FIX_SESSION_STORAGE.update(session, value);
                    break;
                default:
                    throw new IllegalArgumentException("unknown mode:" + mode);
            }
            return true;
        } catch (Exception e) {
            LOG.error("error while storing session", e);
            JOptionPane.showMessageDialog(this, "Error while storing session", "Error", JOptionPane.ERROR_MESSAGE, Images.get(Images.DIALOG_ERROR));
            return false;
        }
    }

    @Override
    public Frame getOwnerFrame() {
        return FixCrackerSwingGUI.mainWindow;
    }
}
