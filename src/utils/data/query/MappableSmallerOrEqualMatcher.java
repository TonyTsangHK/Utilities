package utils.data.query;

import utils.data.Mappable;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2014-06-24
 * Time: 09:50
 */
public class MappableSmallerOrEqualMatcher implements MappableQueryMatcher {
    private static MappableSmallerOrEqualMatcher instance = new MappableSmallerOrEqualMatcher();

    public static MappableSmallerOrEqualMatcher getInstance() {
        return instance;
    }

    @Override
    public boolean match(Mappable mappable, Object key, Object value) {
        return ((Comparable)mappable.getAsMapValue(key)).compareTo(value) <= 0;
    }
}
