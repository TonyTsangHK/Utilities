package utils.data.query;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2014-06-24
 * Time: 10:03
 */

import utils.data.Mappable;
import utils.regex.WildcardToRegularExpression;

/**
 * String only, * as wildcard
 */
public class MappableLikeMatcher implements MappableQueryMatcher {
    private static MappableLikeMatcher instance = new MappableLikeMatcher();

    public static MappableLikeMatcher getInstance() {
        return instance;
    }

    @Override
    public boolean match(Mappable mappable, Object key, Object value) {
        String s = mappable.getAsMapValue(key).toString();

        String regex = WildcardToRegularExpression.wildcardToRegex(value.toString());

        return s.matches(regex);
    }
}
