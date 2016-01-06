package utils.file.path;

import utils.string.StringUtil;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2016-01-05
 * Time: 11:53
 */

/**
 * Path matching pattern parser, following gitignore specification.
 *
 * gitignore documentation reference: https://git-scm.com/docs/gitignore
 */
public class PathPatternMatcherGroupParser {
    private static PathPatternMatcherGroupParser instance = new PathPatternMatcherGroupParser();

    private PathPatternMatcherGroupParser() {}

    public static PathPatternMatcherGroupParser getInstance() {
        return instance;
    }

    public PathPatternMatcherGroup parse(String content) {
        PathPatternMatcherGroup result = new PathPatternMatcherGroup();
        if (!StringUtil.isEmptyString(content)) {
            String[] lines = content.split("\\r|\\n");

            for (String line : lines) {
                if (!line.startsWith("#")) {
                    line = trimTrailingSpaces(line);
                    if (line.length() > 0) {
                        result.addPathPatternMatcher(parseLine(line));
                    }
                }
            }
        }

        return result;
    }

    private PathPatternMatcher parseLine(String line) {
        boolean negate = false;
        if (line.startsWith("!")) {
            negate = true;
            line = line.substring(1);
        }
        return new PathPatternMatcher(line, negate);
    }

    private String trimTrailingSpaces(String line) {
        char[] chars = line.toCharArray();

        int lastPos = chars.length-1;

        while (lastPos > 0) {
            char lastCh = chars[lastPos];

            if (lastCh == ' ') {
                if (lastPos-1 >= 0) {
                    if (chars[lastPos-1] == '\\') {
                        break;
                    } else {
                        lastPos--;
                    }
                }
            } else {
                break;
            }
        }

        if (lastPos <= 0) {
            return "";
        } else {
            return new String(chars, 0, lastPos+1);
        }
    }
}
