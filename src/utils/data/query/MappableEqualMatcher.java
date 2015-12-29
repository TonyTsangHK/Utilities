package utils.data.query;

import utils.data.Mappable;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2014-06-24
 * Time: 09:56
 */
public class MappableEqualMatcher implements MappableQueryMatcher {
    private static MappableEqualMatcher instance = new MappableEqualMatcher();

    public static MappableEqualMatcher getInstance() {
        return instance;
    }

    @Override
    public boolean match(Mappable mappable, Object key, Object value) {
        return mappable.getAsMapValue(key).equals(value);
    }
}
