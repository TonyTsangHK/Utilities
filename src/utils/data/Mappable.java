package utils.data;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2014-06-21
 * Time: 11:04
 */
public interface Mappable<K, V> {
    Set<K> keyset();
    Collection<V> values();
    V getAsMapValue(K key);
    void putAsMapValue(K key, V value);
    void putAsMapValues(Map<K, V> mapValues);
    Map<K, V> toMap();
}
