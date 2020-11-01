package org.fixt.fixcracker.gui.swing.actions;


import org.fixt.fixcracker.gui.swing.Images;


import javax.swing.*;


public abstract class AbstractEditAction extends AbstractAction {


    public AbstractEditAction() {
        super();
        putValue(SMALL_ICON, Images.get(Images.EDIT));
    }


}
