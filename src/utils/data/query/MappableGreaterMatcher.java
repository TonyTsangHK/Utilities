package utils.data.query;

import utils.data.Mappable;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2014-06-24
 * Time: 09:42
 */
public class MappableGreaterMatcher implements MappableQueryMatcher {
    private static MappableGreaterMatcher instance = new MappableGreaterMatcher();

    public static MappableGreaterMatcher getInstance() {
        return instance;
    }

    @Override
    public boolean match(Mappable mappable, Object key, Object value) {
        return ((Comparable)mappable.getAsMapValue(key)).compareTo(value) > 0;
    }
}
