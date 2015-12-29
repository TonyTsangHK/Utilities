package utils.js;

public class JsonHandler {
    public static final char
        OBJECT_START     = '{',  OBJECT_END     = '}',
        ARRAY_START      = '[',  ARRAY_END      = ']',
        STRING_START     = '\'', STRING_END     = '\'',
        STRING_ALT_START = '"',  STRING_ALT_END = '"',
        ESCAPE_CHAR      = '\\', NEWLINE_CHAR   = '\n',
        TAB_CHAR         = '\t';
    
    public static boolean isStartChar(char c) {
        return c == OBJECT_START || c == ARRAY_START || c == STRING_START || c == STRING_ALT_START;
    }
    
    public static char getExpectedEnd(char c) {
        switch (c) {
            case OBJECT_START:
                return OBJECT_END;
            case ARRAY_START:
                return ARRAY_END;
            case STRING_START:
                return STRING_END;
            case STRING_ALT_START:
                return STRING_ALT_END;
            default:
                return (char)0;
        }
    }
    
    public static void appendTabs(StringBuilder builder, int i) {
        for (int c = 0; c < i; c++) {
            builder.append(TAB_CHAR);
        }
    }
    
    public static void expandStart(StringBuilder builder, char c, int level) {
        if (builder.length() > 0) {
            builder.append(NEWLINE_CHAR);
        }
        appendTabs(builder, level - 1);
        builder.append(c);
        builder.append(NEWLINE_CHAR);
        appendTabs(builder, level);
    }
    
    public static void expandEnd(StringBuilder builder, char c, int level) {
        builder.append(NEWLINE_CHAR);
        appendTabs(builder, level - 1);
        builder.append(c);
    }
    
    public static int normalSubRoutine(
            StringBuilder builder, char[] chars, int start, int level, boolean expand, char endChar
    ) {
        boolean isEscape = false;
        int i = start;
        while (i < chars.length) {
            char c = chars[i];
            if (isEscape) {
                builder.append(c);
                isEscape = false;
                i++;
                continue;
            }
            if (c == ESCAPE_CHAR) {
                isEscape = true;
                builder.append(c);
                i++;
            } else if (c == OBJECT_START) {
                i = objectSubRoutine(builder, chars, i, level + 1, expand);
            } else if (c == ARRAY_START) {
                i = arraySubRoutine(builder, chars, i, level + 1, expand);
            } else if (c == STRING_START || c == STRING_ALT_START) {
                i = stringSubRoutine(builder, chars, i);
            } else if (c == endChar) {
                return i;
            } else {
                if (expand || !Character.isWhitespace(c)) {
                    builder.append(c);
                }
                i++;
            }
        }
        return chars.length;
    }
    
    public static int objectSubRoutine(StringBuilder builder, char[] chars, int start, int level, boolean isExpand) {
        if (isExpand) {
            expandStart(builder, OBJECT_START, level);
        } else {
            builder.append(OBJECT_START);
        }
        int i = normalSubRoutine(builder, chars, start + 1, level, isExpand, OBJECT_END);
        if (isExpand) {
            expandEnd(builder, OBJECT_END, level);
        } else {
            builder.append(OBJECT_END);
        }
        return i + 1;
    }
    
    public static int arraySubRoutine(StringBuilder builder, char[] chars, int start, int level, boolean expand) {
        if (expand) {
            expandStart(builder, ARRAY_START, level);
        } else {
            builder.append(ARRAY_START);
        }
        int i = normalSubRoutine(builder, chars, start + 1, level, expand, ARRAY_END);
        if (expand) {
            expandEnd(builder, ARRAY_END, level);
        } else {
            builder.append(ARRAY_END);
        }
        return i + 1;
    }
    
    public static int stringSubRoutine(StringBuilder builder, char[] chars, int start) {
        boolean isEscape = false;
        char startChar = chars[start];
        char endChar = (char)0;
        if (startChar == STRING_START) {
            endChar = STRING_END;
        }
        if (startChar == STRING_ALT_START) {
            endChar = STRING_ALT_END;
        }
        builder.append(startChar);
        for (int i = start + 1; i < chars.length; i++) {
            char c = chars[i];
            if (isEscape) {
                builder.append(c);
                isEscape = false;
                continue;
            }
            if (c == ESCAPE_CHAR) {
                isEscape = true;
                builder.append(c);
                continue;
            }
            if (c == endChar) {
                builder.append(c);
                return i + 1;
            } else {
                builder.append(c);    
            }
        }
        return chars.length;
    }
    
    public static String indentJson(String json) {
        char[] jsonChars = json.toCharArray();
        StringBuilder builder = new StringBuilder(jsonChars.length * 3);
        normalSubRoutine(builder, jsonChars, 0, 0, true, (char)-1);
        return builder.toString();
    }
    
    public static String collapseJson(String json) {
        char[] jsonChars = json.toCharArray();
        StringBuilder builder = new StringBuilder(jsonChars.length);
        normalSubRoutine(builder, jsonChars, 0, 0, false, (char)-1);
        return builder.toString();
    }
}
