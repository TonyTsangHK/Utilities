package utils.document;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import utils.data.StringValidator;

/**
 * Accept integer input only
 */
@SuppressWarnings("serial")
public class PositiveIntegerDocument extends PlainDocument {
    @Override
    public void insertString(int offs, String str, AttributeSet a)
            throws BadLocationException {
        String text = getText(0, offs) + str + getText(offs, getLength() - offs);
        if (text.equals("+") || StringValidator.isInteger(text, true)) {
            super.insertString(offs, str, a);
        }
    }
}