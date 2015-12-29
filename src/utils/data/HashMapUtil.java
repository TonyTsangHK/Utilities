package utils.data;

import java.util.HashMap;

public class HashMapUtil {
    @SuppressWarnings("rawtypes")
    public static HashMap createHashMap(Object ... objects) {
        HashMap map = new HashMap();
        return putMapDatas(map, objects);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static HashMap putMapDatas(HashMap map, Object ... objects) {
        for (int i = 0; i < objects.length; i+=2) {
            Object key = objects[i];
            Object value = null;
            if (i + 1 < objects.length) {
                value = objects[i+1];
            }
            map.put(key, value);
        }
        return map;
    }
}
