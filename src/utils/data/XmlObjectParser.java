package utils.data;

/*import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;*/

import utils.string.*;

public class XmlObjectParser {
    public static final char StartTagIndicator = '<', EndTagIndicator = '>', ClosingTagIndicator = '/';
    
    public static XmlObject parse(String input, XmlObjectDefination def) {
        XmlObject root = new XmlObject("xmlRoot"); 
        char[] chars = input.toCharArray();
        parseDef(chars, 0, chars.length - 1, def, root);
        return root;
    }
    
    public static void parseDef(char[] chars, int s, int e, XmlObjectDefination def, XmlObject parent) {
        for (String n : def.getInvolvedNames()) {
            if (s > e) {
                break;
            }
            char[] startTagChars = new char[n.length() + 2], 
                endTagChars = new char[n.length() + 3];
            char[] nameChars = n.toCharArray();
            startTagChars[0] = endTagChars[0] = StartTagIndicator;
            startTagChars[startTagChars.length - 1] = endTagChars[endTagChars.length - 1] = EndTagIndicator;
            endTagChars[1] = ClosingTagIndicator;
            System.arraycopy(nameChars, 0, startTagChars, 1, nameChars.length);
            System.arraycopy(nameChars, 0, endTagChars, 2, nameChars.length);
            
            int pIndex = s;
            int minContentIndex = pIndex + startTagChars.length + endTagChars.length;
            while (minContentIndex <= e) {
                int startTagIndex = findMatch(chars, startTagChars, pIndex);
                int endTagIndex = findMatch(chars, endTagChars, startTagIndex + startTagChars.length);
                if (startTagIndex > -1 && endTagIndex > -1 && startTagIndex <= e && endTagIndex <= e) {
                    XmlObject xObj = new XmlObject(n);
                    XmlObjectDefination childDef = def.getChildDefination(n);
                    if (childDef != null) {
                        parseDef(chars, startTagIndex + startTagChars.length, endTagIndex - 1, childDef, xObj);
                    } else {
                        xObj.setValue(StringUtil.buildString(chars, startTagIndex + startTagChars.length, endTagIndex, true));
                    }
                    pIndex = endTagIndex + endTagChars.length;
                    parent.addXmlObject(xObj);
                } else {
                    break;
                }
            }
        }
    }
    
    public static int findMatch(char[] chars, char c, int s) {
        for (int i = s; i < chars.length; i++) {
            if (chars[i] == c) {
                return i;
            }
        }
        return -1;
    }
    
    public static int findMatch(char[] c1, char[] c2, int s) {
        for (int i = s; i < c1.length; i++) {
            if (c1[i] == c2[0]) {
                if (compareChars(c1, c2, i, 0, c2.length)) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    public static boolean compareChars(char[] c1, char[] c2, int s1, int s2, int len) {
        for (int i = 0; i < len; i++) {
            if (c1[s1+i] != c2[s2+i]) {
                return false;
            }
        }
        return true;
    }
}
