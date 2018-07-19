package utils.document;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Change all input to upper case input only
 */
@SuppressWarnings("serial")
public class UppercaseDocument extends PlainDocument {
    @Override
    public void insertString(int offs, String str, AttributeSet a)
            throws BadLocationException {
        super.insertString(offs, str.toUpperCase(), a);
    }
}