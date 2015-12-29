package utils.data.query;

import utils.data.Mappable;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2014-06-30
 * Time: 09:43
 */
public interface MappableIndexComparator<K, V> extends Comparator<Mappable<K, V>> {
    public List<K> getIndexedKeys();
    public boolean usableForKey(K key);
    public boolean usableForKeys(Collection<K> keys);

    @Override
    public int compare(Mappable<K, V> o1, Mappable<K, V> o2);

    @Override
    public boolean equals(Object obj);

    public int compare(Mappable<K, V> m, K key, V value);

    public int compare(Mappable<K, V> m, Map<K, V> sample);
}
