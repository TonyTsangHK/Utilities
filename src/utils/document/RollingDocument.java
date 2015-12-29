package utils.document;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2013-10-04
 * Time: 17:37
 */
public class RollingDocument extends PlainDocument {
    public static final int DEFAULT_LIMIT = 1000000;
    private int limit;

    public RollingDocument() {
        this(DEFAULT_LIMIT);
    }

    public RollingDocument(int limit) {
        this.limit = limit;
    }

    private void ensureLimit() throws BadLocationException {
        if (getLength() > limit) {
            remove(0, getLength()-limit);
        }
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        super.insertString(offs, str, a);
        ensureLimit();
    }
}
