/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fixt.fixcracker.gui.swing.utils.table;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * Listener for JTable header
 * Usage:
 * headerListener = new ColumnHeaderListener(grd, new ColumnHeaderListener.ColumnHeaderClickAction() {
 *             @Override
 *             public void onHeaderColumnClick(int vColIndex, MouseEvent evt) {
 *                 if (evt.getClickCount() == 2) {
 *                     tca.adjustColumn(vColIndex);
 *                 }
 *             }
 *         }
 *         );
 *         grd.getTableHeader().addMouseListener(headerListener);
 *
 */
public class ColumnHeaderListener extends MouseAdapter {

    private JTable table;
    private ColumnHeaderClickAction handler;

    public ColumnHeaderListener(JTable table, ColumnHeaderClickAction handler) {
        this.table = table;
        this.handler = handler;
    }

    @Override
    public void mouseClicked(MouseEvent evt) {

        TableColumnModel colModel = table.getColumnModel();

        // The index of the column whose header was clicked
        int vColIndex = colModel.getColumnIndexAtX(evt.getX());
        int mColIndex = table.convertColumnIndexToModel(vColIndex);

        // Return if not clicked on any column header
        if (vColIndex == -1) {
            return;
        }


        handler.onHeaderColumnClick(mColIndex, evt);
    }

    public interface ColumnHeaderClickAction {

        void onHeaderColumnClick(int vColIndex, MouseEvent evt);
    }
}