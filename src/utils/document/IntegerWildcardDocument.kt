package utils.document

import javax.swing.text.AttributeSet
import javax.swing.text.BadLocationException
import javax.swing.text.PlainDocument

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2017-01-13
 * Time: 15:09
 */
class IntegerWildcardDocument: PlainDocument() {
    @Throws(BadLocationException::class)
    override fun insertString(offs: Int, str: String?, a: AttributeSet?) {
        if (str != null) {
            var localStr = str
            
            localStr = localStr.replace(",", "")
            
            val text = getText(0, offs) + localStr + getText(offs, getLength() - offs)

            val regex = Regex("^[-+]?[0-9?*]+$")

            if (text == "" || text == "+" || text == "-" || regex.matches(text)) {
                super.insertString(offs, localStr, a)
            }
        }
    }
}