package utils.data.query;

import utils.data.Mappable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2014-06-24
 * Time: 12:43
 */
public class MappableSampleQueryMatcherCollections implements MappableSampleQueryMatcher {
    private List<MappableSampleQueryMatcher> matchers;

    private QueryJoinCondition joinCondition;

    public MappableSampleQueryMatcherCollections() {
        this(QueryJoinCondition.AND);
    }

    public MappableSampleQueryMatcherCollections(QueryJoinCondition joinCondition) {
        matchers = new ArrayList<MappableSampleQueryMatcher>();
        this.joinCondition = joinCondition;
    }

    public void addMatcher(MappableSampleQueryMatcher matcher) {
        matchers.add(matcher);
    }

    public void addMatcher(final Object key, final MappableQueryMatcher matcher) {
        addMatcher(
            new MappableSampleQueryMatcher() {
                @Override
                public boolean match(Mappable mappable, Map sample) {
                    return matcher.match(mappable, key, sample.get(key));
                }
            }
        );
    }

    public void addNotMatcher(final MappableSampleQueryMatcher matcher) {
        addMatcher(
            new MappableSampleQueryMatcher() {
                @Override
                public boolean match(Mappable mappable, Map sample) {
                    return !matcher.match(mappable, sample);
                }
            }
        );
    }

    public void addNotMatcher(final Object key, final MappableQueryMatcher matcher) {
        addMatcher(
            new MappableSampleQueryMatcher() {
                @Override
                public boolean match(Mappable mappable, Map sample) {
                    return !matcher.match(mappable, key, sample.get(key));
                }
            }
        );
    }

    public void clearMatchers() {
        matchers.clear();
    }

    public QueryJoinCondition getJoinCondition() {
        return joinCondition;
    }

    public void setJoinCondition(QueryJoinCondition joinCondition) {
        this.joinCondition = joinCondition;
    }

    @Override
    public boolean match(Mappable mappable, Map sample) {
        if (joinCondition == QueryJoinCondition.AND) {
            for (MappableSampleQueryMatcher matcher : matchers) {
                if (!matcher.match(mappable, sample)) {
                    return false;
                }
            }
            return true;
        } else if (joinCondition == QueryJoinCondition.OR) {
            for (MappableSampleQueryMatcher matcher : matchers) {
                if (matcher.match(mappable, sample)) {
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
