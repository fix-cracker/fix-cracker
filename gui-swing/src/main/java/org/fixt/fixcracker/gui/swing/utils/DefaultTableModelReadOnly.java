package org.fixt.fixcracker.gui.swing.utils;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.event.MouseEvent;

public class DefaultTableModelReadOnly extends DefaultTableModel {

    private String[] columnToolTips;
    private boolean editable = false;
    private boolean firstEditable = false;

    public void setEditable(boolean editable, boolean firstEditable) {
        this.editable = editable;
        this.firstEditable = firstEditable;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == 0) {
            return firstEditable;
        } else {
            return editable;
        }
    }

    public void setToolTipText(String semicolonseparatedText){
        columnToolTips=semicolonseparatedText.split(";");
    }

    //Implement table header tool tips.
    protected JTableHeader createDefaultTableHeader() {
        return new JTableHeader() {
            public String getToolTipText(MouseEvent e) {
                if (columnToolTips == null) {
                    return null;
                }
                java.awt.Point p = e.getPoint();
                int index = columnModel.getColumnIndexAtX(p.x);
                int realIndex = columnModel.getColumn(index).getModelIndex();
                if (realIndex < columnToolTips.length) {
                    return columnToolTips[realIndex];
                } else {
                    return null;
                }
            }
        };
    }

    public void clearRows() {
        setRowCount(0);
    }
}
