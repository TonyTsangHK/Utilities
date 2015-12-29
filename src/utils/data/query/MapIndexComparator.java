package utils.data.query;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2014-09-26
 * Time: 11:57
 */
public interface MapIndexComparator<K, V> extends Comparator<Map<K, V>>  {
    public List<K> getIndexedKeys();
    public boolean usableForKey(K key);
    public boolean usableForKeys(Collection<K> keys);

    public int compare(Map<K, V> m, K key, V value);

    public int compareSample(Map<K, V> m, Map<K, V> sample);
}
