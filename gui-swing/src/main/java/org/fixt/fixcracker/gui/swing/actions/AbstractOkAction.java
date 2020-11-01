package org.fixt.fixcracker.gui.swing.actions;


import org.fixt.fixcracker.gui.swing.Images;


import javax.swing.*;



public abstract class AbstractOkAction extends AbstractAction{
    
    public AbstractOkAction(){
        putValue(Action.NAME,"ОК");
        putValue(Action.SMALL_ICON, Images.get(Images.OK));
    }
    
}
