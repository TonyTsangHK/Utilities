package utils.js;

import java.util.Date;
import netscape.javascript.JSObject;
import utils.date.DateTimeParser;

public class JavascriptHandler {
    public JSObject mainWin;
    
    public JavascriptHandler(JSObject w) {
        mainWin = w;
    }
    
    public Object callFunction(String functionName, Object ... params) {
        return mainWin.call(functionName, params);
    }

    public static String escape(String src) {
        int i;
        char j;
        StringBuilder sbld = new StringBuilder();
        sbld.ensureCapacity(src.length()*6);
        for (i=0;i<src.length() ;i++ ) {
            j = src.charAt(i);
            if (Character.isDigit(j) || Character.isLowerCase(j) ||
                    Character.isUpperCase(j)) {
                sbld.append(j);
            } else if (j < 256) {
                sbld.append("%");
                if (j < 16) {
                    sbld.append('0');
                }
                sbld.append(Integer.toString(j,16));
            } else {
                sbld.append("%u");
                sbld.append(Integer.toString(j,16));
            }
        }
        return sbld.toString();
    }

    public static String unescape(String src) {
        StringBuilder sbld = new StringBuilder();
        sbld.ensureCapacity(src.length());
        int  lastPos = 0,pos = 0;
        char ch;
        while (lastPos < src.length()) {
            pos = src.indexOf("%",lastPos);
            if (pos == lastPos) {
                if (src.charAt(pos + 1) == 'u') {
                    ch = (char)Integer.parseInt(src.substring(pos + 2,pos + 6), 16);
                    sbld.append(ch);
                    lastPos = pos+6;
                } else {
                    ch = (char)Integer.parseInt(src.substring(pos + 1,pos + 3), 16);
                    sbld.append(ch);
                    lastPos = pos + 3;
                }
            } else {
                if (pos == -1) {
                    sbld.append(src.substring(lastPos));
                    lastPos=src.length();
                } else {
                    sbld.append(src.substring(lastPos,pos));
                    lastPos=pos;
                }
            }
        }
        return sbld.toString();
    }

    public void setCookie(String name, String value) {
        setCookie(name, value, null, null, null, null);
    }

    public void setCookie(String name, String value, long validMillis) {
        setCookie(name, value, computeExpires(validMillis));
    }

    public void setCookie(String name, String value, String expires) {
        setCookie(name, value, expires, null, null, null);
    }

    public static long getMillisForSecond() {
        return 1000;
    }

    public static long getMillisForMinute() {
        return 60000;
    }

    public static long getMillisForHour() {
        return 3600000;
    }

    public static long getMillisForDay() {
        return 86400000;
    }

    public static long getMillisForWeek() {
        return 604800000;
    }

    public static long getMillisForStandardMonth() {
        return 2592000000L;
    }

    public static long getMillisForStandardYear() {
        return 31536000000L;
    }

    public static String getDefaultExpires() {
        return computeExpires(getMillisForStandardYear() * 3);
    }

    public static String computeExpires(long millisAfterCurrentTime) {
        long millis = System.currentTimeMillis() + millisAfterCurrentTime;
        return DateTimeParser.format(new Date(millis), "yyyyMMddHHmmss");
    }

    public void setCookie(String name, String value, String expires,
            String path, String domain, String secure) {
        if (expires == null) {
            expires = getDefaultExpires();
        }
        DateTimeParser.parse(expires);
        String cookieStr = name + "=" + escape(value) +
            ((expires != null) ? "; expires=" +
                    DateTimeParser.format(expires, "E, dd MMM yyyy HH:mm:ss z") : "") +
            ((path != null) ? "; path=" + path : "") +
            ((domain != null) ? "; domain=" + domain : "") +
            ((secure != null) ? "; secure" : "");
        JSObject jsDoc = (JSObject)mainWin.getMember("document");
        jsDoc.setMember("cookie", cookieStr);
    }

    public void deleteCookie(String name) {
        String cookie = getCookie(name);
        if (cookie != null && !cookie.equals("")) {
            setCookie(name, "", "19700101000000", null, null, null);
        }
    }

    public void deleteCookie(String name, String path, String domain) {
        String cookie = getCookie(name);
        if (cookie != null && !cookie.equals("")) {
            setCookie(name, "", "19700101000000", path, domain, null);
        }
    }

    public String getCookie(String name) {
        if (name == null || name.equals("")) {
            return "";
        } else {
            JSObject jsDoc = (JSObject) mainWin.getMember("document");
            String cookie = (String) jsDoc.getMember("cookie");
            String prefix = name + "=";
            StringBuilder sbld = new StringBuilder(name);
            sbld.append('=');
            if (cookie != null) {
                int startIndex = cookie.indexOf("; " + prefix);
                if (startIndex == -1) {
                    startIndex = cookie.indexOf(prefix);
                    if (startIndex != 0) {
                        return "";
                    }
                } else {
                    startIndex += 2;
                }
                int endIndex = cookie.indexOf(";", startIndex);
                if (endIndex == -1) {
                    endIndex = cookie.length();
                }
                return unescape(cookie.substring(startIndex + prefix.length(), endIndex));
            } else {
                return "";
            }
        }
    }

    public JSObject getElementById(String id) {
        JSObject obj = (JSObject)mainWin.getMember("document");
        return (JSObject)obj.call("getElementById", new Object[]{id});
    }

    public static int getArrayLength(JSObject arr) {
        if (arr != null) {
            Object len = arr.getMember("length");
            if (len instanceof Integer) {
                return ((Integer)len).intValue();
            } else if (len instanceof Double) {
                return (int)((Double)len).doubleValue();
            } else {
                try {
                    return Integer.parseInt(len.toString());
                } catch (NumberFormatException nfe) {
                    return -1;
                }
            }
        } else {
            return -1;
        }
    }

    public Object evaluate(String script) {
        return mainWin.eval(script);
    }
}
