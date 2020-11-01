package org.fixt.fixcracker.gui.swing.dialogs;

import org.fixt.fixcracker.core.domain.TagValue;
import org.fixt.fixcracker.gui.swing.utils.FramedDialog;
import org.fixt.fixcracker.gui.swing.utils.IDialogFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TagAddFDialog extends JPanel implements IDialogFrame {
    private final TagValidationFunc tagValidator;
    JSpinner tagNumber = new JSpinner(new SpinnerNumberModel());
    JTextField tagValue = new JTextField();

    public TagAddFDialog(TagValidationFunc tagValidator) {
        super(new GridLayout(0, 2));
        setBorder(new EmptyBorder(10,10,10,10));
        this.tagValidator = tagValidator;
        add(new JLabel("Tag number"));
        add(tagNumber);
        add(new JLabel("Tag Value"));
        add(tagValue);

    }

    @Override
    public void init(FramedDialog dlg) {
        tagNumber.requestFocus();
    }

    @Override
    public String getValidationMessage() {
        Integer tag = (Integer) tagNumber.getValue();
        return tagValidator.validate(tag);
    }

    @Override
    public boolean performDialogAction(FramedDialog dlg) {
        return true;
    }

    @Override
    public Frame getOwnerFrame() {
        return null;
    }

    public TagValue getTagValue() {
        return new TagValue((Integer) tagNumber.getValue(),tagValue.getText());
    }

    public interface TagValidationFunc {
        String validate(int tagNumber);
    }
}
