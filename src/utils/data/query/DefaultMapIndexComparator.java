package utils.data.query;

import utils.data.DataComparator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2014-09-26
 * Time: 12:28
 */
public class DefaultMapIndexComparator<K, V> implements MapIndexComparator<K, V> {
    private List<K> indexedKeys;

    public DefaultMapIndexComparator(K indexedKey) {
        this.indexedKeys = new ArrayList<K>();
        this.indexedKeys.add(indexedKey);
    }

    public DefaultMapIndexComparator(K ... indexedKeys) {
        this.indexedKeys = new ArrayList<K>();
        for (K indexedKey : indexedKeys) {
            this.indexedKeys.add(indexedKey);
        }
    }

    public DefaultMapIndexComparator(List<K> indexedKeys) {
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
    public int compare(Map<K, V> o1, Map<K, V> o2) {
        for (K key : indexedKeys) {
            V v1 = o1.get(key), v2 = o2.get(key);

            int cmp = DataComparator.compare(v1, v2, true, true);

            if (cmp != 0) {
                return cmp;
            }
        }

        return 0;
    }

    @Override
    public int compare(Map<K, V> m, K key, V value) {
        if (usableForKey(key)) {
            V v = m.get(key);

            return DataComparator.compare(v, value, true, true);
        } else {
            throw new UnsupportedOperationException("This index comparator not usable for key: ["+key.toString()+"]");
        }
    }

    @Override
    public int compareSample(Map<K, V> m, Map<K, V> sample) {
        if (usableForKeys(sample.keySet())) {
            for (K k : sample.keySet()) {
                V v1 = m.get(k), v2 = sample.get(k);

                int cmp = DataComparator.compare(v1, v2, true, true);

                if (cmp != 0) {
                    return cmp;
                }
            }

            return 0;
        } else {
            boolean first = true;

            String keys = "";

            for (K k : sample.keySet()) {
                if (first) {
                    first = false;
                } else {
                    keys += ", ";
                }

                keys += k.toString();
            }

            throw new UnsupportedOperationException("This index comparator not usable for key: ["+keys+"]");
        }
    }
}
