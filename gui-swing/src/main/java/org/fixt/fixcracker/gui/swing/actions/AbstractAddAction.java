package org.fixt.fixcracker.gui.swing.actions;

import org.fixt.fixcracker.gui.swing.Images;


import javax.swing.*;


public abstract class AbstractAddAction extends AbstractAction {

    public AbstractAddAction() {
        super();
        putValue(Action.SMALL_ICON, Images.get(Images.ADD));
    }
}
