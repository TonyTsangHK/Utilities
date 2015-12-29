package utils.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2014-06-13
 * Time: 11:04
 */
public class MultiDimensionalKey {
    protected Map<String, Object> keyMap;

    public MultiDimensionalKey() {
        this.keyMap = new HashMap<String, Object>();
    }

    public MultiDimensionalKey(Map<String, Object> keyMap) {
        this.keyMap = new HashMap<String, Object>(keyMap);
    }

    public void clear() {
        keyMap.clear();
    }

    public void putKeyPart(String k, Object v) {
        keyMap.put(k, v);
    }

    public Object getKeyPart(String k) {
        return keyMap.get(k);
    }

    public boolean isPartOfKey(String k, Object v) {
        if (keyMap.containsKey(k)) {
            return keyMap.get(k).equals(v);
        } else {
            return false;
        }
    }

    public boolean isPartOfKey(Map<String, Object> sample) {
        for (String k : sample.keySet()) {
            if (!keyMap.containsKey(k) || !keyMap.get(k).equals(sample.get(k))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null | !(obj instanceof MultiDimensionalKey)) {
            return false;
        } else {
            MultiDimensionalKey k = (MultiDimensionalKey) obj;

            if (k.keyMap.size() != keyMap.size()) {
                return false;
            } else {
                for (String skey : keyMap.keySet()) {
                    if (!k.keyMap.containsKey(skey)) {
                        return false;
                    } else {
                        if (!keyMap.get(skey).equals(k.keyMap.get(skey))) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;

        for (String k : keyMap.keySet()) {
            hash = 31 * hash + k.hashCode();
            hash = 31 * hash + keyMap.get(k).hashCode();
        }

        return hash;
    }


}
