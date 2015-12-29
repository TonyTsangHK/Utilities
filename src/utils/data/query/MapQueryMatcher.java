package utils.data.query;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2014-09-26
 * Time: 11:53
 */
public interface MapQueryMatcher<K, V> {
    public boolean match(Map<K, V> map, K key, V value);
}
