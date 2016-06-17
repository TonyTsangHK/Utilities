package utils.data;

/**
 * Key value pair holder
 *
 * @author TonyTsang
 */

public class KeyValuePair<K, E> {
    private K key;
    private E value;

    private int hash = 0;

    public KeyValuePair(K k, E v) {
        hash = 0;
        setKey(k);
        setValue(v);
    }

    public void setKey(K k) {
        if (k == null) {
            throw new NullPointerException("Null is not allowed for key");
        }
        if (key == null || !k.equals(key)) {
            recalHash();
            this.key = k;
        }
    }

    public void setValue(E v) {
        if ((v == null && value != null) || (value == null || !v.equals(value))) {
            recalHash();
            this.value = v;
        }
    }

    public K getKey() {
        return key;
    }

    public E getValue() {
        return value;
    }

    private void recalHash() {
        if (value != null) {
            hash = key.hashCode() ^ value.hashCode();
        } else if (key != null) {
            hash = key.hashCode();
        } else {
            hash = 0;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object o) {
        if (o instanceof KeyValuePair) {
            KeyValuePair keyValuePair = (KeyValuePair) o;
            return DataComparator.compare(keyValuePair.getKey(), key, true, true) == 0 &&
                   DataComparator.compare(keyValuePair.getValue(), value, true, true) == 0;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        if (hash == 0) {
            recalHash();
        }
        return hash;
    }
}
