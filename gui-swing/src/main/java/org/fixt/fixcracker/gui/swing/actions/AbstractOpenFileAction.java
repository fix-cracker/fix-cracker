package org.fixt.fixcracker.gui.swing.actions;

import org.fixt.fixcracker.gui.swing.Images;


import javax.swing.*;

public abstract class AbstractOpenFileAction extends AbstractAction {

    public AbstractOpenFileAction() {
        super();
        putValue(SMALL_ICON, Images.get(Images.OPEN_FILE));
    }


}
