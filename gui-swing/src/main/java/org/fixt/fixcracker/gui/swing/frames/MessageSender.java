package org.fixt.fixcracker.gui.swing.frames;


import org.fixt.fixcracker.core.FIXCrackerConst;
import org.fixt.fixcracker.core.MainController;
import org.fixt.fixcracker.core.api.MessageSenderView;
import org.fixt.fixcracker.core.api.MessageTemplateStorage;
import org.fixt.fixcracker.core.description.FieldNameResolver;
import org.fixt.fixcracker.core.domain.TagValue;
import org.fixt.fixcracker.gui.swing.Images;
import org.fixt.fixcracker.gui.swing.actions.*;
import org.fixt.fixcracker.gui.swing.dialogs.TagAddFDialog;
import org.fixt.fixcracker.gui.swing.utils.FramedDialog;
import org.fixt.fixcracker.gui.swing.utils.table.TableColumnAdjuster;
import org.fixt.fixcracker.gui.swing.utils.table.UpDownTableSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.field.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.fixt.fixcracker.core.FIXCrackerConst.SOH_STR;

public class MessageSender extends JPanel implements MessageSenderView, TagAddFDialog.TagValidationFunc {
    private static final Logger LOG = LoggerFactory.getLogger(MessageSender.class);
    private final MessageTemplateStorage messageTemplateStorage;
    private MainController controller;
    private JTable grd;
    private MessageTableModel model;
    private UpDownTableSorter upDown;
    private TableColumnAdjuster tca;

    private Set<Integer> automaticallyPopulatedTags = new HashSet<>() {
        {
            add(BeginString.FIELD);
            add(SenderCompID.FIELD);
            add(SenderSubID.FIELD);
            add(SenderLocationID.FIELD);
            add(TargetCompID.FIELD);
            add(TargetSubID.FIELD);
            add(TargetLocationID.FIELD);
            add(SendingTime.FIELD);
            add(CheckSum.FIELD);
            add(BodyLength.FIELD);
            add(MsgSeqNum.FIELD);
        }
    };

    public MessageSender(MessageTemplateStorage messageTemplateStorage) {
        super(new BorderLayout());
        this.messageTemplateStorage = messageTemplateStorage;
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        add(toolBar, BorderLayout.NORTH);

        model = new MessageTableModel();
        grd = new JTable(model);
        tca = new TableColumnAdjuster(grd);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(grd);
        add(scrollPane, BorderLayout.CENTER);

        toolBar.add(new SendMessageAction());
        toolBar.addSeparator();
        toolBar.add(new ImportFromClipboardAction());
        toolBar.add(new LoadTemplateAction());
        toolBar.add(new SaveTemplateAction());
        toolBar.addSeparator();
        toolBar.add(new AddTagAction());
        toolBar.add(new MoveUpAction());
        toolBar.add(new MoveDownAction());
        toolBar.add(new RemoveTagAction());
        upDown = new UpDownTableSorter(grd);
    }

    @Override
    public void setMainController(MainController controller) {
        this.controller = controller;
    }

    @Override
    public String getFIXMessage() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < model.getRowCount(); i++) {
            sb.append(model.getTag(i)).append("=").append(model.getTagValue(i));
            sb.append(SOH_STR);
        }
        return sb.toString();
    }

    @Override
    public String validate(int tag) {
        if (automaticallyPopulatedTags.contains(tag)) {
            return String.format("Tag %d cannot be added because it is session related.\n" +
                    "Session related tags populated from session settings", tag);
        }
        TagValue rowValue;
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getTag(i) == tag) {
                return String.format("Tag %d exists in message already", tag);
            }
        }
        return null;
    }

    private class MessageTableModel extends DefaultTableModel {
        public MessageTableModel() {
            super();
            addColumn("Tag");
            addColumn("Value");
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column != 0;
        }

        public int add(int tag, String tagValue) {
            addRow(new Object[]{new TagWithDescription(tag, FieldNameResolver.getDescription(tag)), tagValue});
            return getRowCount() - 1;
        }

        public int getTag(int row) {
            return ((TagWithDescription) model.getValueAt(row, 0)).tag;
        }

        public String getTagValue(int row) {
            return (String) model.getValueAt(row, 1);
        }
    }

    private class TagWithDescription {
        int tag;
        String desc;

        public TagWithDescription(int tag, String desc) {
            this.tag = tag;
            this.desc = desc;
        }

        @Override
        public String toString() {
            return tag + (desc != null ? " " + desc : "");
        }
    }

    private class ImportFromClipboardAction extends AbstractAction {
        public ImportFromClipboardAction() {
            putValue(Action.SMALL_ICON, Images.get(Images.EDIT_PASTE));
            putValue(SHORT_DESCRIPTION, "Import message from Clipboard");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String data = null;
            try {
                if (Toolkit.getDefaultToolkit()
                        .getSystemClipboard().isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                    data = (String) Toolkit.getDefaultToolkit()
                            .getSystemClipboard().getData(DataFlavor.stringFlavor);
                }
            } catch (UnsupportedFlavorException | IOException e) {
                LOG.error("error", e);
            }
            if (data == null) {
                return;
            }
            setMessageContent(data);
        }
    }

    private void setMessageContent(String data) {
        String[] partsPipe = data.split("\\|");
        String[] partsChar1 = data.split(SOH_STR);
        String[] parts = partsPipe.length > partsChar1.length ? partsPipe : partsChar1;
        if (parts.length < 1) return;

        model.setRowCount(0);
        int tag;
        for (String part : parts) {
            String[] values = part.split("=");
            if (values.length < 2) continue;
            try {
                tag = Integer.parseInt(values[0]);
            } catch (NumberFormatException e) {
                continue;
            }
            if (automaticallyPopulatedTags.contains(tag)) continue;
            model.add(tag, values[1]);
        }
        model.fireTableDataChanged();
        tca.adjustColumns();
    }

    private class SaveTemplateAction extends AbstractSaveFileAction {
        public SaveTemplateAction() {
            putValue(SHORT_DESCRIPTION, "Save as template");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String templateName = JOptionPane.showInputDialog("Specify template name");
            if (templateName != null && !templateName.trim().isEmpty()) {
                try {
                    messageTemplateStorage.putMessageTemplate(FIXCrackerConst.PROTOCOL_DEFAULT, templateName.trim(), getFIXMessage());
                } catch (Exception e) {
                    LOG.error("error", e);
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class LoadTemplateAction extends AbstractOpenFileAction {
        public LoadTemplateAction() {
            putValue(SHORT_DESCRIPTION, "Load from template");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String selected;
            try {
                String[] messageTemplateArray = messageTemplateStorage.getMessageTemplateArray(FIXCrackerConst.PROTOCOL_DEFAULT);
                LOG.info(Arrays.toString(messageTemplateArray));
                selected = (String) JOptionPane.showInputDialog(null,
                        "Choose message template", "Choose message template", JOptionPane.QUESTION_MESSAGE,
                        null, messageTemplateArray, null);

                if (selected != null) {
                    String msg = messageTemplateStorage.getMessageTemplate(FIXCrackerConst.PROTOCOL_DEFAULT, selected);
                    setMessageContent(msg);
                }
            } catch (Exception e) {
                LOG.error("error", e);
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class AddTagAction extends AbstractAddAction {
        public AddTagAction() {
            super();
            putValue(SHORT_DESCRIPTION, "Add tag");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            TagAddFDialog dlg = new TagAddFDialog(MessageSender.this);
            if (FramedDialog.show(dlg, "Add new tag")) {
                int lastRowCount = model.getRowCount();
                TagValue tagValue = dlg.getTagValue();
                model.add(tagValue.getTag(), tagValue.getValue());
                model.fireTableRowsInserted(lastRowCount, lastRowCount);
            }
        }
    }

    private class RemoveTagAction extends AbstractDeleteAction {
        public RemoveTagAction() {
            super();
            putValue(SHORT_DESCRIPTION, "Remove selected tags");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int selectedRowCount = grd.getSelectedRowCount();
            if (selectedRowCount == 0) {
                JOptionPane.showMessageDialog(null, "Select tags to remove!");
                return;
            }
            if (JOptionPane.showConfirmDialog(null, "Remove selected tags?") != JOptionPane.YES_OPTION) {
                return;
            }
            int[] selectedRows = grd.getSelectedRows();
            for (int i = 0; i < selectedRows.length; i++) {
                selectedRows[i] = grd.convertRowIndexToModel(selectedRows[i]);
            }
            Arrays.sort(selectedRows);
            for (int i = selectedRows.length - 1; i >= 0; i--) {
                model.removeRow(selectedRows[i]);
                model.fireTableRowsDeleted(selectedRows[i], selectedRows[i]);
            }
        }
    }

    private class SendMessageAction extends AbstractAction {
        public SendMessageAction() {
            super();
            putValue(SMALL_ICON, Images.get(Images.SEND));
            putValue(SHORT_DESCRIPTION, "Send configured message");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            controller.sendMessage();
        }
    }

    private class MoveUpAction extends AbstractMoveUpAction {
        public MoveUpAction() {
            putValue(SHORT_DESCRIPTION, "Move selected tag up");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            upDown.moveSelectedUp();
        }
    }

    private class MoveDownAction extends AbstractMoveDownAction {
        public MoveDownAction() {
            putValue(SHORT_DESCRIPTION, "Move selected tag down");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            upDown.moveSelectedDown();
        }
    }

}
