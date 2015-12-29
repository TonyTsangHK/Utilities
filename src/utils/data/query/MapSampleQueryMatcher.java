package utils.data.query;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2014-09-26
 * Time: 12:19
 */
public interface MapSampleQueryMatcher<K, V> {
    public boolean match(Map<K, V> map, Map<K, V> sample);
}
