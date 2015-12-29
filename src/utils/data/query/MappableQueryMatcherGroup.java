package utils.data.query;

import utils.data.Mappable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2014-06-24
 * Time: 09:51
 */
public class MappableQueryMatcherGroup<K, M extends MappableQueryMatcher> implements MappableSampleQueryMatcher {
    private Map<K, M> matcherMap;

    private QueryJoinCondition joinCondition;

    public MappableQueryMatcherGroup() {
        this(QueryJoinCondition.AND);
    }

    public MappableQueryMatcherGroup(QueryJoinCondition joinCondition) {
        this.joinCondition = joinCondition;
        this.matcherMap = new HashMap<K, M>();
    }

    public void setKeyMatcher(K key, M matcher) {
        matcherMap.put(key, matcher);
    }

    public void setJoinCondition(QueryJoinCondition joinCondition) {
        this.joinCondition = joinCondition;
    }

    public QueryJoinCondition getJoinCondition() {
        return joinCondition;
    }

    @Override
    public boolean match(Mappable mappable, Map sample) {
        if (joinCondition == QueryJoinCondition.AND) {
            for (Object key : sample.keySet()) {
                if (!matcherMap.containsKey(key) || !matcherMap.get(key).match(mappable, key, sample.get(key))) {
                    return false;
                }
            }
            return true;
        } else if (joinCondition == QueryJoinCondition.OR) {
            for (Object key : sample.keySet()) {
                if (matcherMap.containsKey(key) && matcherMap.get(key).match(mappable, key, sample.get(key))) {
                    return true;
                }
            }
            return false;
        } else {
            // Null join condition will always be false
            return false;
        }
    }
}
