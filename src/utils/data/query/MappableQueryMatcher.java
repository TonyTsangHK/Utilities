package utils.data.query;

import utils.data.Mappable;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2014-06-23
 * Time: 10:11
 */
public interface MappableQueryMatcher<T extends Mappable<K, V>, K, V> {
    boolean match(T mappable, K key, V value);
}
