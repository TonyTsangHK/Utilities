package utils.data;

import utils.exception.DuplicateReferenceException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2013-10-02
 * Time: 09:25
 */
public class DoubleMap<K, V> implements Map<K, V> {
    private Map<K, V> keyReferencedMap;
    private Map<V, K> valueReferencedMap;

    public DoubleMap(int initialCapacity) {
        keyReferencedMap = new HashMap<K, V>(initialCapacity);
        valueReferencedMap = new HashMap<V, K>(initialCapacity);
    }

    public DoubleMap() {
        keyReferencedMap = new HashMap<K, V>();
        valueReferencedMap = new HashMap<V, K>();
    }

    public DoubleMap(Map<? extends K, ? extends V> m) {
        this();
        putAll(m);
    }

    @Override
    public int size() {
        return keyReferencedMap.size();
    }

    @Override
    public boolean isEmpty() {
        return keyReferencedMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return keyReferencedMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return valueReferencedMap.containsKey(value);
    }

    @Override
    public V get(Object key) {
        return keyReferencedMap.get(key);
    }

    public K getByValue(Object value) {
        return valueReferencedMap.get(value);
    }

    @Override
    public V put(K key, V value) {
        if (keyReferencedMap.containsKey(key)) {
            valueReferencedMap.remove(keyReferencedMap.get(key));
            valueReferencedMap.put(value, key);
            return keyReferencedMap.put(key, value);
        } else {
            if (valueReferencedMap.containsKey(value)) {
                throw new DuplicateReferenceException(
                    String.valueOf(value) + " is referencing " + valueReferencedMap.get(value) + ", key: "+key+" reference duplicated!"
                );
            } else {
                keyReferencedMap.put(key, value);
                valueReferencedMap.put(value, key);
                return value;
            }
        }
    }

    @Override
    public V remove(Object key) {
        if (keyReferencedMap.containsKey(key)) {
            V v = keyReferencedMap.remove(key);
            valueReferencedMap.remove(v);
            return v;
        } else {
            return null;
        }
    }

    public K removeValue(Object value) {
        if (valueReferencedMap.containsKey(value)) {
            K k = valueReferencedMap.remove(value);
            keyReferencedMap.remove(k);
            return k;
        } else {
            return null;
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        keyReferencedMap.clear();
        valueReferencedMap.clear();
    }

    @Override
    public Set<K> keySet() {
        return keyReferencedMap.keySet();
    }

    public Set<V> valueSet() {
        return valueReferencedMap.keySet();
    }

    @Override
    public Collection<V> values() {
        return keyReferencedMap.values();
    }

    public Collection<K> keys() {
        return valueReferencedMap.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return keyReferencedMap.entrySet();
    }

    public Set<Entry<V, K>> valueEntrySet() {
        return valueReferencedMap.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return keyReferencedMap.equals(o);
    }

    @Override
    public int hashCode() {
        return keyReferencedMap.hashCode();
    }
}
