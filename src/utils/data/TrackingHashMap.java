package utils.data;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2012-08-21
 * Time: 16:24
 */
public class TrackingHashMap<K, V> extends HashMap<K, V> {
    private Set<K> modifiedKeys;
    private Set<K> removedKeys;

    private HashMap<K, V> map;

    public TrackingHashMap() {
        map = new HashMap<K, V>();
        initializeTrackingSets();
    }

    public TrackingHashMap(int initialCapacity) {
        map = new HashMap<K, V>(initialCapacity);
        initializeTrackingSets();
    }

    public TrackingHashMap(int initialCapacity, float loadFactor) {
        map = new HashMap<K, V>(initialCapacity, loadFactor);
        initializeTrackingSets();
    }

    public TrackingHashMap(Map<? extends K, ? extends V> m) {
        map = new HashMap<K, V>(m);
        initializeTrackingSets();
    }

    private void initializeTrackingSets() {
        modifiedKeys = new HashSet<K>();
        removedKeys = new HashSet<K>();
    }

    public boolean isModified() {
        return !modifiedKeys.isEmpty() || !removedKeys.isEmpty();
    }

    public synchronized Set<K> modifiedKeys() {
        return new HashSet<K>(modifiedKeys);
    }

    public synchronized Set<K> removedKeys() {
        return new HashSet<K>(removedKeys);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public synchronized V put(K key, V value) {
        modifiedKeys.add(key);
        return map.put(key, value);
    }

    public synchronized void putAll(Map<? extends K, ? extends V> m) {
        for (K key : m.keySet()) {
            modifiedKeys.add(key);
        }
        map.putAll(m);
    }

    public synchronized V silentPut(K key, V value) {
        return map.put(key, value);
    }

    public synchronized void silentPutAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    public synchronized void clearTrackingDatas() {
        removedKeys.clear();
        modifiedKeys.clear();
    }

    @Override
    public synchronized V remove(Object key) {
        if (containsKey(key)) {
            removedKeys.add((K)key);
        }
        return map.remove(key);
    }

    @Override
    public synchronized void clear() {
        for (K key : keySet()) {
            removedKeys.add(key);
        }
        map.clear();
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Object clone() {
        return map.clone();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return map.equals(o);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
