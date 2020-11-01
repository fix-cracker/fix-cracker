package org.fixt.fixcracker.gui.swing.utils.table;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Arrays;

/**
 * Move range of rows in JTable
 */
public class UpDownTableSorter {
    private final JTable grd;

    public UpDownTableSorter(JTable grd) {
        this.grd = grd;
    }

    public void moveSelectedUp() {
        int[] rows = getSelectedModelRowsWithValidation();
        if (rows == null) return;
        if (rows[0] == 0) return;
        DefaultTableModel model = (DefaultTableModel) grd.getModel();
        int columnCount = model.getColumnCount();
        Object tmp;
        int lastRowIdx = rows[rows.length - 1];
        for (int i = 0; i < columnCount; i++) {
            tmp = model.getValueAt(rows[0] - 1, i);
            for (int r = rows[0]; r <= lastRowIdx; r++) {
                model.setValueAt(model.getValueAt(r, i), r - 1, i);
            }
            model.setValueAt(tmp, lastRowIdx, i);
        }
        model.fireTableRowsUpdated(rows[0] - 1, lastRowIdx);
        grd.setRowSelectionInterval(rows[0] - 1, lastRowIdx - 1);
    }


    public void moveSelectedDown() {
        int[] rows = getSelectedModelRowsWithValidation();
        if (rows == null) return;
        DefaultTableModel model = (DefaultTableModel) grd.getModel();
        int lastRowIdx = rows[rows.length - 1];
        if (lastRowIdx == model.getRowCount() - 1) return;
        int columnCount = model.getColumnCount();
        Object tmp;
        for (int i = 0; i < columnCount; i++) {
            tmp = model.getValueAt(lastRowIdx+1, i);
            for (int r = lastRowIdx; r >= rows[0]; r--) {
                model.setValueAt(model.getValueAt(r, i), r + 1, i);
            }
            model.setValueAt(tmp, rows[0], i);
        }
        model.fireTableRowsUpdated(rows[0], lastRowIdx + 1);
        grd.setRowSelectionInterval(rows[0] + 1, lastRowIdx + 1);
    }

    private int[] continuousRangeSelected(int[] selectedRows) {
        if (selectedRows.length == 1) return selectedRows;
        for (int i = 0; i < selectedRows.length - 1; i++) {
            if (selectedRows[i] + 1 < selectedRows[i + 1]) {
                return null;
            }
        }
        return selectedRows;
    }


    private int[] getSelectedModelRowsWithValidation() {
        if (grd.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "There is no selected rows");
            return null;
        }
        int[] selectedRows = grd.getSelectedRows();
        for (int i = 0; i < selectedRows.length; i++) {
            selectedRows[i] = grd.convertRowIndexToModel(selectedRows[i]);
        }
        Arrays.sort(selectedRows);
        int[] rows = continuousRangeSelected(selectedRows);
        if (rows == null) {
            JOptionPane.showMessageDialog(null, "Need to select continuous range of rows");
        }
        return rows;
    }

}
