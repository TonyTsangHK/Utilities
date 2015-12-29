package utils.data;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2014-06-13
 * Time: 11:00
 */
public class MultipleKeyQueryableMap<K extends MultiDimensionalKey, V> {
    private Map<K, V> dataMap;

    public MultipleKeyQueryableMap() {
        this.dataMap = new HashMap<K, V>();
    }

    public Set<K> keySet() {
        return dataMap.keySet();
    }

    public V get(K k) {
        return dataMap.get(k);
    }

    public void put(K k, V v) {
        this.dataMap.put(k, v);
    }

    public void remove(K k) {
        this.dataMap.remove(k);
    }

    public void clear() {
        this.dataMap.clear();
    }

    public List<V> query(String key, Object keyValue) {
        List<V> results = new ArrayList<V>();

        for (K k : dataMap.keySet()) {
            if (k.isPartOfKey(key, keyValue)) {
                results.add(dataMap.get(k));
            }
        }

        return results;
    }

    public List<V> query(String key, Object keyValue, QueryMatcher<K> queryMatcher) {
        List<V> results = new ArrayList<V>();

        for (K k : dataMap.keySet()) {
            if (queryMatcher.match(k, key, keyValue)) {
                results.add(dataMap.get(k));
            }
        }

        return results;
    }

    public List<V> query(Map<String, Object> sampleKeyMap) {
        List<V> results = new ArrayList<V>();

        for (K k : dataMap.keySet()) {
            if (k.isPartOfKey(sampleKeyMap)) {
                results.add(dataMap.get(k));
            }
        }

        return results;
    }

    public List<V> query(Map<String, Object> sampleKeyMap, QuerySampleMatcher<K> querySampleMatcher) {
        List<V> results = new ArrayList<V>();

        for (K k : dataMap.keySet()) {
            if (querySampleMatcher.match(k, sampleKeyMap)) {
                results.add(dataMap.get(k));
            }
        }

        return results;
    }

    public boolean containsKey(K key) {
        return dataMap.containsKey(key);
    }

    public int size() {
        return dataMap.size();
    }

    public static interface QuerySampleMatcher<K extends MultiDimensionalKey> {
        public boolean match(K k, Map<String, Object> sampleMap);
    }

    public static interface QueryMatcher<K extends MultiDimensionalKey> {
        public boolean match(K k, String key, Object keyValue);
    }
}
