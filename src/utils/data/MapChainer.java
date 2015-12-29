package utils.data;

import java.util.HashMap;
import java.util.Map;

public class MapChainer<K, V> {
    private Map<K, V> map;
    
    public MapChainer() {
        map = new HashMap<K, V>();
    }
    
    public MapChainer(Map<K, V> map) {
        this.map = map;
    }
    
    public MapChainer<K, V> put(K key, V value) {
        map.put(key, value);
        return this;
    }

    public MapChainer<K, V> put(K key, V value, boolean filterNull) {
        if (!filterNull || value != null) {
            map.put(key, value);
        }

        return this;
    }
    
    public MapChainer<K, V> putAll(Map<? extends K, ? extends V> map) {
        this.map.putAll(map);
        return this;
    }

    public MapChainer<K, V> putAll(Map<? extends K, ? extends V> map, boolean filterNull) {
        if (filterNull) {
            for (K k : map.keySet()) {
                put(k, map.get(k), true);
            }

            return this;
        } else {
            return putAll(map);
        }
    }
    
    public MapChainer<K, V> remove(K key) {
        this.map.remove(key);
        return this;
    }
    
    public Map<K, V> getMap() {
        return map;
    }
}
