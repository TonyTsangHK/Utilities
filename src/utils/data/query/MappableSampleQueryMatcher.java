package utils.data.query;

import utils.data.Mappable;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2014-06-23
 * Time: 10:12
 */
public interface MappableSampleQueryMatcher<K, V> {
    public boolean match(Mappable<K, V> mappable, Map<K, V> sample);
}
