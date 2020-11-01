package org.fixt.fixcracker.gui.swing.actions;

import org.fixt.fixcracker.gui.swing.Images;


import javax.swing.*;


public abstract class AbstractMoveUpAction extends AbstractAction {

    public AbstractMoveUpAction() {
        putValue(Action.SMALL_ICON, Images.get(Images.GO_UP));
    }
}
