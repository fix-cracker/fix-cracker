package org.fixt.fixcracker.gui.swing.actions;

import org.fixt.fixcracker.gui.swing.Images;

import javax.swing.*;

public abstract class AbstractMoveDownAction extends AbstractAction {

    public AbstractMoveDownAction() {
        putValue(Action.SMALL_ICON, Images.get(Images.GO_DOWN));
    }
}
