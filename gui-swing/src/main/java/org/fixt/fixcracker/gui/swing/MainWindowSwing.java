package org.fixt.fixcracker.gui.swing;


import org.fixt.fixcracker.core.api.FIXConnector;
import org.fixt.fixcracker.core.api.GeneralSettingStorage;
import org.fixt.fixcracker.core.api.MainWindow;
import org.fixt.fixcracker.core.api.MessageSenderView;
import org.fixt.fixcracker.core.connector.QuickFIXConnector;
import org.fixt.fixcracker.core.domain.FIXSession;
import org.fixt.fixcracker.gui.swing.dialogs.AboutFDialog;
import org.fixt.fixcracker.gui.swing.dialogs.SessionSelectionFDialog;
import org.fixt.fixcracker.gui.swing.frames.MessagesTableView;
import org.fixt.fixcracker.gui.swing.frames.MessageSender;
import org.fixt.fixcracker.core.settings.PreferenceSettingStorage;
import org.fixt.fixcracker.core.settings.PreferencesTemplateStorage;
import org.fixt.fixcracker.gui.swing.utils.FramedDialog;
import org.fixt.fixcracker.core.MainController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static org.fixt.fixcracker.gui.swing.Images.GO_BOTTOM;

public class MainWindowSwing extends JFrame implements MainWindow {
    public static final String TITLE = "[FIX Cracker]";
    public static final String MESSAGESVIEW_SUPPRESS_HB = "messagesview.suppressHB";
    public static final String MESSAGESVIEW_KEEP_LAST_SELECTED = "messagesview.keepLastSelected";
    private final Logger LOG = LoggerFactory.getLogger(MainWindowSwing.class);
    private JPanel pnlMain;
    private JMenuBar mainMenuBar;
    private JToolBar toolBar;
    private final FIXConnector connector = new QuickFIXConnector();
    private MessagesTableView messagesView;
    private MessageSenderView senderView;
    private JCheckBox suppressHB = new JCheckBox("Suppress HB", true);
    private JToggleButton keepLastSelectedButton = new JToggleButton();
    private GeneralSettingStorage settingStorage = new PreferenceSettingStorage();

    private final MainController controller = new MainController();


    public MainWindowSwing() throws HeadlessException {
        pnlMain = new JPanel();
        pnlMain.setLayout(new BorderLayout(0, 0));
        pnlMain.setPreferredSize(new Dimension(700, 500));
        setContentPane(pnlMain);
        setIconImage(Images.getImage(Images.APP_ICON));
        setTitle(TITLE);

        mainMenuBar = new JMenuBar();
        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        createMenu();
        setJMenuBar(mainMenuBar);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(toolBar, BorderLayout.NORTH);
        messagesView = new MessagesTableView();
        leftPanel.add(messagesView, BorderLayout.CENTER);

        senderView = new MessageSender(new PreferencesTemplateStorage());
        senderView.setMainController(controller);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, (Component) senderView);
        splitPane.setResizeWeight(1);//right is fixed
        add(splitPane, BorderLayout.CENTER);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();

        setLocationRelativeTo(null);// put into center

        controller.init(this, senderView, connector);

        restoreStateFromSetting();

        SwingUtilities.invokeLater(() -> splitPane.setDividerLocation(0.7));
    }


    private void createMenu() {
        ConnectSessionAction connectSessionAction = new ConnectSessionAction();
        DisconnectSessionAction acDisconnect = new DisconnectSessionAction();

        JMenu sessionsMenu = new JMenu("Sessions");
        sessionsMenu.add(connectSessionAction);
        sessionsMenu.add(acDisconnect);
        sessionsMenu.addSeparator();
        sessionsMenu.add(suppressHB);
        suppressHB.setToolTipText("Suppress heartbeat messages in messages view");
        suppressHB.addItemListener(itemEvent -> {
            messagesView.suppressHB(suppressHB.isSelected());
            try {
                settingStorage.putBoolean(MESSAGESVIEW_SUPPRESS_HB, suppressHB.isSelected());
            } catch (Exception e) {
                LOG.error("error", e);
            }
        });

        JMenu help = new JMenu("Help");
        help.add(new JMenuItem(new AboutAppAction()));

        mainMenuBar.add(sessionsMenu);
        mainMenuBar.add(help);

        toolBar.add(connectSessionAction);
        toolBar.add(acDisconnect);
        keepLastSelectedButton.setAction(new KeepLastSelectedAction());
        toolBar.add(keepLastSelectedButton);
    }

    private void restoreStateFromSetting() {
        boolean value = settingStorage.getBoolean(MESSAGESVIEW_SUPPRESS_HB, true);
        this.suppressHB.setSelected(value);
        messagesView.suppressHB(value);

        value = settingStorage.getBoolean(MESSAGESVIEW_KEEP_LAST_SELECTED, true);
        keepLastSelectedButton.setSelected(value);
        messagesView.keepLastSelected(value);
    }


    @Override
    public void showErrorMessageBox(String title, Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), title, JOptionPane.ERROR_MESSAGE);
    }

    private class AboutAppAction extends AbstractAction {
        public AboutAppAction() {
            super("About app", Images.get(Images.ABOUT));
        }


        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            FramedDialog.show(new AboutFDialog(),"About application");
//            JOptionPane.showMessageDialog(null, "About");
        }
    }

    private class ConnectSessionAction extends AbstractAction {
        public ConnectSessionAction() {
            putValue(Action.NAME, "Connect");
            putValue(Action.SMALL_ICON, Images.get(Images.START));
            putValue(Action.SHORT_DESCRIPTION, "Connect new session");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            SessionSelectionFDialog dlg = new SessionSelectionFDialog();
            boolean dlgResult = FramedDialog.show(dlg, "Select session to connect");
            if (dlgResult) {
                try {
                    FIXSession session = dlg.getSelectedSesssion();
                    if (connector.isConnected()) {
                        int choice = JOptionPane.showConfirmDialog(null, "We are connected to another session alredy.Do you want to disconnect before connection?",
                                "Please confirm disconnection before connecting to a new session", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (choice != JOptionPane.YES_OPTION) {
                            return;
                        }
                        connector.disconnect();
                        connector.setMessageMonitor(null);
                        messagesView.clearView();
                        messagesView.suppressHB(suppressHB.isSelected());
                    }
                    setTitle(String.format("%s - %s:%d %s->%s", session.getName(), session.getHost(), session.getPort(), session.getSenderCompID(), session.getTargetCompID()));

                    connector.setMessageMonitor(messagesView);

                    connector.connect(session);
                } catch (Exception ex) {
                    LOG.error("Error", ex);
                    JOptionPane.showMessageDialog(null, ex.toString());
                }
            }
        }
    }

    private class DisconnectSessionAction extends AbstractAction {
        public DisconnectSessionAction() {
            putValue(Action.NAME, "Disconnect");
            putValue(Action.SMALL_ICON, Images.get(Images.STOP));
            putValue(Action.SHORT_DESCRIPTION, "Disconnect current session");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (!connector.isConnected()) {
                JOptionPane.showMessageDialog(null, "There is no connected session");
                return;
            }
            if (JOptionPane.showOptionDialog(null,
                    "Do you want to disconnect current session?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, null, JOptionPane.YES_OPTION) == JOptionPane.YES_OPTION) {
                connector.disconnect();
            }
        }
    }

    private class KeepLastSelectedAction extends AbstractAction {
        public KeepLastSelectedAction() {
            putValue(SMALL_ICON, Images.get(GO_BOTTOM));
            putValue(SHORT_DESCRIPTION, "Select new message once it added to messages view");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            messagesView.keepLastSelected(keepLastSelectedButton.isSelected());
            try {
                settingStorage.putBoolean(MESSAGESVIEW_KEEP_LAST_SELECTED, keepLastSelectedButton.isSelected());
            } catch (Exception e) {
                LOG.error("error", e);
            }

        }
    }


}
