package utils.file.path;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2016-01-05
 * Time: 14:18
 */
public class PathPatternMatcherGroup {
    private List<PathPatternMatcher> pathPatternMatchers;
    private boolean hasNegateMatcher;

    public PathPatternMatcherGroup() {
        hasNegateMatcher = false;
        this.pathPatternMatchers = new ArrayList<>();
    }

    public PathPatternMatcherGroup(Collection<PathPatternMatcher> pathPatternMatchers) {
        hasNegateMatcher = false;
        if (pathPatternMatchers != null && pathPatternMatchers.size() > 0) {
            this.pathPatternMatchers = new ArrayList<>(pathPatternMatchers);

            for (PathPatternMatcher matcher : this.pathPatternMatchers) {
                if (matcher.isNegate()) {
                    hasNegateMatcher = true;
                    break;
                }
            }
        } else {
            this.pathPatternMatchers = new ArrayList<>();
        }
    }

    public void addPathPatternMatcher(PathPatternMatcher pathPatternMatcher) {
        if (pathPatternMatcher != null) {
            this.pathPatternMatchers.add(pathPatternMatcher);
            if (pathPatternMatcher.isNegate()) {
                hasNegateMatcher = true;
            }
        }
    }

    public boolean match(String path) {
        if (hasNegateMatcher) {
            boolean result = false;
            // All matcher must be processed for negate matcher to take effect.
            for (PathPatternMatcher matcher : pathPatternMatchers) {
                if (matcher.isNegate()) {
                    if (result) {
                        if (matcher.match(path)) {
                            result = false;
                        }
                    }
                } else if (!result) {
                    result = matcher.match(path);
                }
            }
            return result;
        } else {
            for (PathPatternMatcher matcher : pathPatternMatchers) {
                if (matcher.match(path)) {
                    // Since there is no negate matcher, matched result can be returned early
                    return true;
                }
            }

            return false;
        }
    }
}
