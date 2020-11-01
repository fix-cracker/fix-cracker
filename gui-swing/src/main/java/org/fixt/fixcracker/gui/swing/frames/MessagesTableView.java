package org.fixt.fixcracker.gui.swing.frames;

import org.fixt.fixcracker.core.api.MessagesListView;
import org.fixt.fixcracker.core.description.MsgTypeNameResolver;
import org.fixt.fixcracker.gui.swing.actions.AbstractClearAction;
import org.fixt.fixcracker.gui.swing.utils.DefaultTableModelReadOnly;
import org.fixt.fixcracker.gui.swing.utils.TableRowObjectDecorator;
import org.fixt.fixcracker.gui.swing.utils.Utils;
import org.fixt.fixcracker.gui.swing.utils.table.ColumnHeaderListener;
import org.fixt.fixcracker.gui.swing.utils.table.TableColumnAdjuster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.MsgType;
import quickfix.field.SendingTime;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

public class MessagesTableView extends JPanel implements MessagesListView {
    private static final Logger LOG = LoggerFactory.getLogger(MessagesTableView.class);
    private final JScrollPane scrollPane;
    private final JTable grd;
    private final MessagesTableModel model;
    private final JPopupMenu popupMenu;
    private final TableColumnAdjuster tca;
    private final ColumnHeaderListener headerListener;
    private boolean suppressHB;
    private boolean keepLastSelected;

    public MessagesTableView() {
        super(new BorderLayout());
        popupMenu = new JPopupMenu();

        model = new MessagesTableModel();
        grd = new JTable();
        grd.setAutoCreateColumnsFromModel(true);

        grd.setModel(model);
        grd.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tca = new TableColumnAdjuster(grd);
        headerListener = new ColumnHeaderListener(grd, (vColIndex, evt) -> {
            if (evt.getClickCount() == 2) {
                tca.adjustColumn(vColIndex);
            }
        }
        );
        grd.getTableHeader().addMouseListener(headerListener);

        grd.setComponentPopupMenu(popupMenu);

        scrollPane = new JScrollPane();
        scrollPane.setViewportView(grd);
        scrollPane.setComponentPopupMenu(popupMenu);

        add(scrollPane, BorderLayout.CENTER);

        popupMenu.add(new AcClear());


    }


    @Override
    public void onMessage(boolean incoming, Message msg) {
        try {
            if (suppressHB && msg.getHeader().isSetField(MsgType.FIELD)
                    && Objects.equals(msg.getHeader().getString(MsgType.FIELD), MsgType.HEARTBEAT)) {
                return;
            }
        } catch (FieldNotFound ex) {
            LOG.error("error", ex);
        }
        int modelIdx = model.add(incoming, msg);
        model.fireTableRowsInserted(modelIdx, modelIdx);
        boolean needColumnsResize = model.getRowCount() == 1;
        if (needColumnsResize) {
            SwingUtilities.invokeLater(()->tca.adjustColumns());
        }
        if (keepLastSelected) {
            selectAddedRow(modelIdx);
        }
    }

    private void selectAddedRow(int modelIdx) {
        int viewIdx = grd.convertRowIndexToView(modelIdx);
        SwingUtilities.invokeLater(() -> {
            Utils.jtableSelectViewRow(grd, viewIdx);
        });
    }

    @Override
    public void clearView() {
        model.clearRows();
    }

    @Override
    public void onSessionError(String error) {
        int rowIdx = model.addErrorMessage(error);
        model.fireTableRowsInserted(rowIdx, rowIdx);
        selectAddedRow(rowIdx);
    }

    @Override
    public void suppressHB(boolean suppressHB) {
        this.suppressHB = suppressHB;
    }

    @Override
    public void keepLastSelected(boolean enabled) {
        this.keepLastSelected = enabled;
    }

    private class MessagesTableModel extends DefaultTableModelReadOnly {
        public MessagesTableModel() {
            super();
            addColumn("<>");
            addColumn("SendingTime");
            addColumn("MsgType");
            addColumn("RawMessage");
        }

        public int add(boolean incoming, Message msg) {
            addRow(msgtoArray(incoming, msg));
            return getRowCount() - 1;
        }

        private Object[] msgtoArray(boolean incoming, Message msg) {
            return new Object[]{
                    new TableRowObjectDecorator<>(msg, message -> incoming ? "in" : "out"),
                    getFromHeader(msg, SendingTime.FIELD),
                    getFromHeader(msg, MsgType.FIELD),
                    msg.toString().replace((char) 1, '|')
            };
        }

        private String getFromHeader(Message msg, int field) {
            try {
                if (msg.getHeader().isSetField(field)) {
                    String msgType = msg.getHeader().getString(field);
                    String desc = MsgTypeNameResolver.getDescription(msgType);
                    return msgType + (desc != null ? "(" + desc + ")" : "");
                }
                return null;

            } catch (FieldNotFound ex) {
                LOG.error("error", ex);
                return null;
            }
        }

        private String get(Message msg, int field) {
            try {
                return msg.isSetField(field) ? msg.getString(field) : null;
            } catch (FieldNotFound ex) {
                LOG.error("error", ex);
                return null;
            }
        }

        public int addErrorMessage(String error) {
            addRow(new Object[]{"in", null, null, error});
            return getRowCount() - 1;
        }
    }

    private class AcClear extends AbstractClearAction {
        public AcClear() {
            super();
            putValue(SHORT_DESCRIPTION, "Clear all messages from view");
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            MessagesTableView.this.clearView();
        }
    }


}
