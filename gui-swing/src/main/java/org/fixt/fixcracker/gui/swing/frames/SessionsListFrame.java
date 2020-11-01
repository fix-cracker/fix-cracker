package org.fixt.fixcracker.gui.swing.frames;

import org.fixt.fixcracker.core.domain.FIXSession;
import org.fixt.fixcracker.core.api.FIXSessionStorage;
import org.fixt.fixcracker.gui.swing.Configuration;
import org.fixt.fixcracker.gui.swing.actions.AbstractAddAction;
import org.fixt.fixcracker.gui.swing.actions.AbstractDeleteAction;
import org.fixt.fixcracker.gui.swing.actions.AbstractEditAction;
import org.fixt.fixcracker.gui.swing.dialogs.FIXSessionEditFDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

import static org.fixt.fixcracker.gui.swing.utils.Utils.bindActionOnInsertKey;

public class SessionsListFrame extends JPanel {
    private static final Logger LOG = LoggerFactory.getLogger(SessionsListFrame.class);
    private final FIXSessionStorage manager;
    private JList<FIXSessionListEntry> list;
    private JPopupMenu popupMenu;

    private DefaultListModel<FIXSessionListEntry> model = new DefaultListModel<>();

    private AbstractAction dblClickAction;

    public SessionsListFrame(FIXSessionStorage manager) {
        super(new BorderLayout());
        this.manager = manager;
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        add(toolbar, BorderLayout.NORTH);

        list = new JList<>();
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(list);
        add(scrollPane, BorderLayout.CENTER);
        list.setModel(model);
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList) evt.getSource();
                if (evt.getClickCount() == 2 && dblClickAction != null) {
                    dblClickAction.actionPerformed(new ActionEvent(list, -1, "dblClick"));
                }
            }
        });

        AddAction addAction = new AddAction();
        RemoveAction removeAction = new RemoveAction();
        EditAction editAction = new EditAction();

        popupMenu = new JPopupMenu();
        popupMenu.add(addAction);
        popupMenu.add(editAction);
        popupMenu.add(removeAction);

        list.setComponentPopupMenu(popupMenu);
        toolbar.add(addAction);
        toolbar.add(editAction);
        toolbar.add(removeAction);

        bindActionOnInsertKey(list, addAction);

    }

    public void setDblClickAction(AbstractAction dblClickAction) {
        this.dblClickAction = dblClickAction;
    }

    public void init() {
        List<FIXSessionListEntry> sessions;
        try {
            sessions = manager.getAll().stream().map(s -> new FIXSessionListEntry(s)).collect(Collectors.toList());
            model.addAll(sessions);
        } catch (Exception e) {
            LOG.error("error", e);
        }
        list.requestFocus();
    }

    public FIXSession getSelected() {
        return list.getSelectedValue() == null ? null : list.getSelectedValue().getSession();
    }

    private static class FIXSessionListEntry {
        private final FIXSession session;

        public FIXSessionListEntry(FIXSession session) {
            this.session = session;
        }

        public FIXSession getSession() {
            return session;
        }

        @Override
        public String toString() {
            return session.getName();
        }
    }

    private class AddAction extends AbstractAddAction {
        public AddAction() {
            putValue(SHORT_DESCRIPTION, "Add new session");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            FIXSession session = FIXSessionEditFDialog.add();
            if (session == null) return;
            model.addElement(new FIXSessionListEntry(session));
        }
    }

    private class EditAction extends AbstractEditAction {
        public EditAction() {
            putValue(SHORT_DESCRIPTION, "Edit selected session");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            FIXSessionListEntry selected = list.getSelectedValue();
            if (selected == null) return;
            FIXSession session = FIXSessionEditFDialog.edit(selected.session);
            if (session == null) return;
            model.setElementAt(new FIXSessionListEntry(session), list.getSelectedIndex());
        }
    }

    private class RemoveAction extends AbstractDeleteAction {
        public RemoveAction() {
            putValue(SHORT_DESCRIPTION,"Remove selected session");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int selectedIndex = list.getSelectedIndex();
            if (selectedIndex == -1) return;
            if (JOptionPane.showConfirmDialog(null, "Please confirm removal of selected session") != JOptionPane.YES_OPTION) {
                return;
            }
            try {
                Configuration.FIX_SESSION_STORAGE.remove(list.getSelectedValue().getSession());
            } catch (Exception e) {
                LOG.error("Error while removal of session", e);
                JOptionPane.showMessageDialog(null, e.toString(), "Error while removal of session", JOptionPane.ERROR_MESSAGE);
                return;
            }
            model.removeElementAt(selectedIndex);
        }
    }

}
