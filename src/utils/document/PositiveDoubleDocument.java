package utils.document;

import utils.data.StringValidator;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Accept integer & . inputs only
 */
public class PositiveDoubleDocument extends PlainDocument {
    private static final long serialVersionUID = 1L;
    
    private int precision = 0;
    
    public PositiveDoubleDocument() {
        this(0);
    }
    
    public PositiveDoubleDocument(int precision) {
        super();
        this.precision = precision;
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a)
            throws BadLocationException {
        String text = getText(0, offs) + str + getText(offs, getLength() - offs);
        if (precision == 0) {
            if (text.equals("+") || StringValidator.isPositiveNumber(text, true)) {                
                super.insertString(offs, str, a);
            }
        } else {
            if (text.equals("+") || StringValidator.isPositiveNumber(text, precision, true)) {
                super.insertString(offs, str, a);
            }
        }
    }
}
