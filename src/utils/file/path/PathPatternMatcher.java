package utils.file.path;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2016-01-05
 * Time: 16:31
 */
public class PathPatternMatcher {
    private String pattern;
    private Pattern matchPattern;
    private boolean negate;

    public PathPatternMatcher(String pattern) {
        this(pattern, false);
    }

    public PathPatternMatcher(String pattern, boolean negate) {
        this.negate = negate;
        this.pattern = pattern;

        matchPattern = Pattern.compile(convertGlobToRegex(pattern));
    }

    public boolean match(String path) {
        Matcher patternMatcher = matchPattern.matcher(path);
        return patternMatcher.find();
    }

    public String getPattern() {
        return pattern;
    }

    public boolean isNegate() {
        return negate;
    }

    @Override
    public String toString() {
        return pattern;
    }

    /**
     * Converts a standard POSIX Shell globbing pattern into a regular expression
     * pattern. The result can be used with the standard {@link java.util.regex} API to
     * recognize strings which match the glob pattern.
     * <p/>
     * See also, the POSIX Shell language:
     * http://pubs.opengroup.org/onlinepubs/009695399/utilities/xcu_chap02.html#tag_02_13_01
     *
     * Modified version of http://stackoverflow.com/a/17369948 by adding ** conversion.
     *
     * @param pattern A glob pattern.
     * @return A regex pattern to recognize the given glob pattern.
     */
    public static String convertGlobToRegex(String pattern) {
        StringBuilder sb = new StringBuilder(pattern.length());
        int inGroup = 0;
        int inClass = 0;
        int firstIndexInClass = -1;
        char[] arr = pattern.toCharArray();

        int i = 0;
        if (arr.length > 0 && arr[0] == '/') {
            sb.append('^');
            i++;
        } else {
            sb.append("^([^/]*/)*");
        }

        for (; i < arr.length; i++) {
            char ch = arr[i];
            switch (ch) {
                case '\\':
                    if (++i >= arr.length) {
                        sb.append('\\');
                    } else {
                        char next = arr[i];
                        switch (next) {
                            case ',':
                                // escape not needed
                                break;
                            case 'Q':
                            case 'E':
                                // extra escape needed
                                sb.append('\\');
                            default:
                                sb.append('\\');
                        }
                        sb.append(next);
                    }
                    break;
                case '*':
                    if (inClass == 0) {
                        if (i+1 < arr.length) {
                            char next = arr[i+1];
                            if (next == '*') {
                                sb.append("([^/]*/)*");
                                i++;
                                if (i+1 < arr.length) {
                                    if (arr[i+1] == '/') {
                                        i++;
                                    }
                                }
                            } else {
                                sb.append("[^/]*");
                            }
                        } else {
                            sb.append("[^/]*");
                        }
                    } else {
                        sb.append('*');
                    }
                    break;
                case '?':
                    if (inClass == 0)
                        sb.append('.');
                    else
                        sb.append('?');
                    break;
                case '[':
                    inClass++;
                    firstIndexInClass = i+1;
                    sb.append('[');
                    break;
                case ']':
                    inClass--;
                    sb.append(']');
                    break;
                case '.':
                case '(':
                case ')':
                case '+':
                case '|':
                case '^':
                case '$':
                case '@':
                case '%':
                    if (inClass == 0 || (firstIndexInClass == i && ch == '^'))
                        sb.append('\\');
                    sb.append(ch);
                    break;
                case '!':
                    if (firstIndexInClass == i)
                        sb.append('^');
                    else
                        sb.append('!');
                    break;
                case '{':
                    inGroup++;
                    sb.append('(');
                    break;
                case '}':
                    inGroup--;
                    sb.append(')');
                    break;
                case ',':
                    if (inGroup > 0)
                        sb.append('|');
                    else
                        sb.append(',');
                    break;
                default:
                    sb.append(ch);
            }
        }

        if (!pattern.endsWith("/") && !pattern.endsWith("**")) {
            sb.append("(/.*)?$");
        }
        return sb.toString();
    }
}
