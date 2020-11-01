package org.fixt.fixcracker.gui.swing.actions;

import org.fixt.fixcracker.gui.swing.Images;


import javax.swing.*;


public abstract class AbstractDeleteAction extends AbstractAction {

    public AbstractDeleteAction() {
        super();
        putValue(Action.SMALL_ICON, Images.get(Images.REMOVE));
    }
}
