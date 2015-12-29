package utils.data.query;

import utils.data.DataComparator;
import utils.data.Mappable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2014-06-30
 * Time: 09:59
 */
public class DefaultMappableIndexComparator<K, V> implements MappableIndexComparator<K, V> {
    private List<K> indexedKeys;

    public DefaultMappableIndexComparator(K indexedKey) {
        this.indexedKeys = new ArrayList<K>();
        this.indexedKeys.add(indexedKey);
    }

    public DefaultMappableIndexComparator(K ... indexedKeys) {
        this.indexedKeys = new ArrayList<K>();
        for (K indexedKey : indexedKeys) {
            this.indexedKeys.add(indexedKey);
        }
    }

    public DefaultMappableIndexComparator(List<K> indexedKeys) {
        this.indexedKeys = indexedKeys;
    }

    @Override
    public List<K> getIndexedKeys() {
        return indexedKeys;
    }

    @Override
    public boolean usableForKey(K key) {
        return this.indexedKeys.size() > 0 && this.indexedKeys.get(0).equals(key);
    }

    @Override
    public boolean usableForKeys(Collection<K> keys) {
        if (this.indexedKeys.size() >= keys.size()) {
            for (int i = 0; i < keys.size(); i++) {
                if (!keys.contains(this.indexedKeys.get(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public int compare(Mappable<K, V> o1, Mappable<K, V> o2) {
        for (K key : indexedKeys) {
            V v1 = o1.getAsMapValue(key), v2 = o2.getAsMapValue(key);

            int cmp = DataComparator.compare(v1, v2, true, true);

            if (cmp != 0) {
                return cmp;
            }
        }

        return 0;
    }

    @Override
    public int compare(Mappable<K, V> m, K key, V value) {
        if (usableForKey(key)) {
            V v = m.getAsMapValue(key);

            return DataComparator.compare(v, value, true, true);
        } else {
            throw new UnsupportedOperationException("This index comparator not usable for key: ["+key.toString()+"]");
        }
    }

    @Override
    public int compare(Mappable<K, V> m, Map<K, V> sample) {
        if (usableForKeys(sample.keySet())) {
            for (K k : indexedKeys) {
                if (sample.containsKey(k)) {
                    V v1 = m.getAsMapValue(k), v2 = sample.get(k);

                    int cmp = DataComparator.compare(v1, v2, true, true);

                    if (cmp != 0) {
                        return cmp;
                    }
                }
            }

            return 0;
        } else {
            boolean first = true;

            StringBuilder keysBuilder = new StringBuilder();

            for (K k : indexedKeys) {
                if (sample.containsKey(k)) {
                    if (first) {
                        first = false;
                    } else {
                        keysBuilder.append(", ");
                    }

                    keysBuilder.append(k.toString());
                }
            }

            throw new UnsupportedOperationException("This index comparator not usable for key: ["+keysBuilder.toString()+"]");
        }
    }
}
