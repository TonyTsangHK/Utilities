package utils.document;

import utils.data.StringValidator;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Accept integer & . inputs only
 */
@SuppressWarnings("serial")
public class DoubleDocument extends PlainDocument {
    private int precision = 0;
    
    public DoubleDocument() {
        this(0);
    }
    
    public DoubleDocument(int precision) {
        super();
        this.precision = precision;
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a)
            throws BadLocationException {
        str = str.replaceAll(",", "");
        String text = getText(0, offs) + str + getText(offs, getLength() - offs);
        if (precision == 0) {
            if (text.equals("-") || text.equals("+") || StringValidator.isNumber(text, true)) {                
                super.insertString(offs, str, a);
            }
        } else {
            if (text.equals("-") || text.equals("+") || StringValidator.isNumber(text, precision, true)) {
                super.insertString(offs, str, a);
            }
        }
    }
}
