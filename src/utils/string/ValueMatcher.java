package utils.string;

import java.util.ArrayList;
import java.util.HashMap;

public class ValueMatcher {
    private char[] startTag, endTag, seperator, scanChars;
    private int currentIndex;
    private ValuePair current, next;
    
    public ValueMatcher(String sTag, String eTag, String s) {
        startTag = sTag.toCharArray();
        endTag = eTag.toCharArray();
        seperator = s.toCharArray();
    }
    
    public void setScanString(String input) {
        if (input == null) {
            scanChars = new char[0];
        } else {
            scanChars = input.toCharArray();
            reset();
        }
    }
    
    public boolean hasNext() {
        return next != null;
    }
    
    public ValuePair nextValuePair() {
        current = next;
        next = findNext();
        return current;
    }
    
    public void reset() {
        currentIndex = 0;
        current = null;
        next = findNext();
    }
    
    private ValuePair findNext() {
        boolean started = false, seperate = false;
        ValuePair vp = new ValuePair();
        while (currentIndex < scanChars.length) {
            if (!started) {
                int index = findMatch(scanChars, startTag, currentIndex);
                if (index == -1) {
                    break;
                } else {
                    started = true;
                    currentIndex = index + startTag.length;
                }
            } else if (!seperate) {
                int index = findMatch(scanChars, seperator, currentIndex);
                if (index == -1) {
                    break;
                } else {
                    vp.setName(StringUtil.buildString(scanChars, currentIndex, index));
                    seperate = true;
                    currentIndex = index + seperator.length;
                }
            } else {
                int index = findMatch(scanChars, endTag, currentIndex);
                if (index == -1) {
                    break;
                } else {
                    vp.setValue(StringUtil.buildString(scanChars, currentIndex, index));
                    started = false;
                    seperate = false;
                    currentIndex = index + endTag.length;
                    return vp;
                }
            }
        }
        return null;
    }
    
    public ArrayList<ValuePair> scan(String str) {
        ArrayList<ValuePair> vps = new ArrayList<ValuePair>();
        setScanString(str);
        while (hasNext()) {
            ValuePair vp = nextValuePair();
            vps.add(vp);
        }
        return vps;
    }
    
    public HashMap<String, String> scanAsTable(String str) {
        HashMap<String, String> h = new HashMap<String, String>();
        setScanString(str);
        while (hasNext()) {
            ValuePair vp = nextValuePair();
            h.put(vp.getName(), vp.getValue());
        }
        reset();
        return h;
    }
    
    public static boolean compareChars(char[] c1, char[] c2, int s1, int s2, int len) {
        for (int i = 0; i < len; i++) {
            if (c1[s1+i] != c2[s2+i]) {
                return false;
            }
        }
        return true;
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
}
