package org.fixt.fixcracker.gui.swing.actions;


import org.fixt.fixcracker.gui.swing.Images;


import javax.swing.*;


public abstract class AbstractClearAction extends AbstractAction {

    public AbstractClearAction() {
        putValue(NAME, "Clear");
        putValue(SMALL_ICON, Images.get(Images.CLEAR));
    }
}
