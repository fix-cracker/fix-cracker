package org.fixt.fixcracker.gui.swing.dialogs;

import org.fixt.fixcracker.gui.swing.FixCrackerSwingConst;
import org.fixt.fixcracker.gui.swing.actions.AbstractDeleteAction;
import org.fixt.fixcracker.gui.swing.utils.FramedDialog;
import org.fixt.fixcracker.gui.swing.utils.IDialogFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MessageTemplateLookupFDialog extends JPanel implements IDialogFrame {
    DefaultListModel model = new DefaultListModel();
    JList list;

    public MessageTemplateLookupFDialog() {
        setBorder(FixCrackerSwingConst.BORDER_DIALOG_DEFAULT);
        setLayout(new BorderLayout());
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        add(toolBar, BorderLayout.NORTH);
        list = new JList(model);
        JScrollPane scrollPane = new JScrollPane(list);
        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void init(FramedDialog dlg) {

    }

    @Override
    public String getValidationMessage() {
        return null;
    }

    @Override
    public boolean performDialogAction(FramedDialog dlg) {
        return false;
    }

    @Override
    public Frame getOwnerFrame() {
        return null;
    }

    private  class DeleteSelectedTemplateAction extends AbstractDeleteAction {
        public DeleteSelectedTemplateAction() {
            super();
            putValue(SHORT_DESCRIPTION,"Remove selected template");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

        }
    }

}
