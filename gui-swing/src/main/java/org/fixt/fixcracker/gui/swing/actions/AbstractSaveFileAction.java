package org.fixt.fixcracker.gui.swing.actions;


import org.fixt.fixcracker.gui.swing.Images;

import javax.swing.*;

public abstract class AbstractSaveFileAction extends AbstractAction {

    public AbstractSaveFileAction() {
        super();
        putValue(SMALL_ICON, Images.get(Images.SAVE_FILE));
    }
}
