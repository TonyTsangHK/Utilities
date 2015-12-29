package utils.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ImmutableMap<K, V> implements Map<K, V> {
    private Map<K, V> map;
    private Set<Map.Entry<K, V>> entrySet;
    private Set<K> keySet;
    
    public ImmutableMap() {
        this.map = new HashMap<K, V>();
        this.entrySet = new HashSet<Map.Entry<K,V>>();
        this.keySet = new HashSet<K>();
    }
    
    public ImmutableMap(Map<K, V> map) {
        this.map = new HashMap<K, V>();
        this.entrySet = new HashSet<Map.Entry<K, V>>();
        this.keySet = new HashSet<K>();
        
        for (Map.Entry<K, V> entry : map.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();
            this.map.put(key, value);
            this.entrySet.add(new ImmutableMap.Entry<K, V>(key, value));
            this.keySet.add(key);
        }
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException("Immutable");
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return entrySet;
    }
    
    @Override
    public V get(Object key) {
        return map.get(key);
    }
    
    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public Set<K> keySet() {
        return keySet;
    }

    @Override
    public V put(K key, V value) {
        throw new UnsupportedOperationException("Immutable");
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("Immutable");
    }

    @Override
    public V remove(Object key) {
        throw new UnsupportedOperationException("Immutable");
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }
    
    public static class Entry<K, V> implements Map.Entry<K, V> {
        private K key;
        private V value;
        
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            throw new UnsupportedOperationException("Immutable");
        }
    }
}
