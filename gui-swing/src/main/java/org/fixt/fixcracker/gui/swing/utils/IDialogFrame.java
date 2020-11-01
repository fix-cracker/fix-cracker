package org.fixt.fixcracker.gui.swing.utils;

import java.awt.*;

/**
 * Interface for dialog frame
 * @author fixt
 */
public interface IDialogFrame {
    /**
     * initialization of dialog frame
     * @param dlg reference to FramedDialog instance in order to get access to some its methods and properties
     */
    void init(FramedDialog dlg);

    /**
     * Check that every control has correct selected value before accept to pressing on the OK Button
     * @return  "" - if all controls are populated correctly, otherwise - corresponding message
    */
    String getValidationMessage();
    /**
     * Perform some action taking in account selected values in controls(save into db or some structure)
     * @param dlg reference to FramedDialog instance in order to get access to some its methods and properties
     * @return indicate that all actions finished successfully or return false in case of emerged error(sqlexception from db for example)
     */
    boolean performDialogAction(FramedDialog dlg);
    /**
     * Get frame to hande into JDialog constructor. It is MainFrame of your app in common case.
     * @return frame to hande into JDialog constructor
     */
    Frame getOwnerFrame();
}
