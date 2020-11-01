package org.fixt.fixcracker.gui.swing.utils;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class Utils {
    public static void jtableSelectViewRow(JTable grd, int viewIdx) {
        grd.getSelectionModel().setSelectionInterval(viewIdx, viewIdx);
        grd.scrollRectToVisible(grd.getCellRect(viewIdx, 0, true));
    }

    public static void bindActionOnInsertKey(JComponent component, AbstractAction action) {
        bindActionOnKey(component, action, "onInsertKey", KeyEvent.VK_INSERT);
    }

    public static void bindActionOnEnterKey(JComponent component, AbstractAction action) {
        bindActionOnKey(component, action, "onEnterKey", KeyEvent.VK_ENTER);
    }

    public static void bindActionOnDeleteKey(JComponent component, AbstractAction action) {
        bindActionOnKey(component, action, "onDeleteKey", KeyEvent.VK_DELETE);
    }

    private static void bindActionOnKey(JComponent component, AbstractAction action, String eventName, int keyStroke) {
        KeyStroke insert = KeyStroke.getKeyStroke(keyStroke, 0);
        component.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(insert, eventName);
        component.getActionMap().put(eventName, action);
    }

}
