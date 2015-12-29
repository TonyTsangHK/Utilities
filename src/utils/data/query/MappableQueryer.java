package utils.data.query;

import utils.data.DataComparator;
import utils.data.Mappable;
import utils.math.IntegerRange;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2014-06-21
 * Time: 12:49
 */
public class MappableQueryer {
    private MappableQueryer() {}

    public static <T extends Mappable<K, V>, K, V> List<T> query(
        Collection<? extends Mappable<K, V>> mappables, K key, V value
    ) {
        List<T> results = new ArrayList<T>();
        for (Mappable<K, V> mappable : mappables) {
            V v = mappable.getAsMapValue(key);

            if (v.equals(value)) {
                results.add((T)mappable);
            }
        }

        return results;
    }

    public static <T extends Mappable<K, V>, K, V> List<T> query(
        Collection<? extends Mappable<K, V>> mappables, final K key, V value, boolean sort
    ) {
        List<T> results = query(mappables, key, value);

        if (sort) {
            Collections.sort(
                results,
                new Comparator<T>() {
                    @Override
                    public int compare(T o1, T o2) {
                        return DataComparator.compare(o1.getAsMapValue(key), o2.getAsMapValue(key), true, true);
                    }
                }
            );
        }

        return results;
    }

    public static <T extends Mappable<K, V>, K, V> List<T> query(
        Collection<? extends Mappable<K, V>> mappables, K key, V value, Comparator<T> sortingComparactor
    ) {
        List<T> results = query(mappables, key, value);

        Collections.sort(results, sortingComparactor);

        return results;
    }

    public static <T extends Mappable<K, V>, K, V> List<T> query(
        Collection<T> mappables, K key, V value, MappableQueryMatcher<T, K, V> queryMatcher
    ) {
        List<T> results = new ArrayList<T>();
        for (T mappable : mappables) {
            if (queryMatcher.match(mappable, key, value)) {
                results.add(mappable);
            }
        }
        return results;
    }

    public static <T extends Mappable<K, V>, K, V> List<T> query(
        Collection<T> mappables, final K key, V value,
        MappableQueryMatcher<T, K, V> queryMatcher, boolean sort
    ) {
        List<T> results = query(mappables, key, value, queryMatcher);

        if (sort) {
            Collections.sort(
                results,
                new Comparator<T>() {
                    @Override
                    public int compare(T o1, T o2) {
                        return DataComparator.compare(o1.getAsMapValue(key), o2.getAsMapValue(key), true, true);
                    }
                }
            );
        }

        return results;
    }

    public static <T extends Mappable<K, V>, K, V> List<T> query(
        Collection<T> mappables, final K key, V value,
        MappableQueryMatcher<T, K, V> queryMatcher, Comparator<T> comparator
    ) {
        List<T> results = query(mappables, key, value, queryMatcher);

        Collections.sort(results, comparator);

        return results;
    }

    public static <T extends Mappable<K,V>, K, V> List<T> query(
        Collection<? extends Mappable<K, V>> mappables, Map<K, V> sample
    ) {
        List<T> results = new ArrayList<T>();
        for (Mappable<K, V> mappable : mappables) {
            boolean allMatch = true;
            for (K sampleKey : sample.keySet()) {
                V sampleValue = sample.get(sampleKey), value = mappable.getAsMapValue(sampleKey);
                if (!sampleValue.equals(value)) {
                    allMatch = false;
                }
            }
            if (allMatch) {
                results.add((T)mappable);
            }
        }
        return results;
    }

    // Must be sorted
    public static <T extends Mappable<K, V>, K, V> List<T> query(
        List<? extends Mappable<K, V>> mappables, Map<K, V> sample, MappableIndexComparator<K, V> indexComparator
    ) {
        if (indexComparator.usableForKeys(sample.keySet())) {
            return queryBinarySearch(mappables, sample, indexComparator);
        } else {
            return querySequentialSearch(mappables, sample);
        }
    }

    // Must be sorted
    public static <T extends Mappable<K, V>, K, V> List<T> query(
        List<? extends Mappable<K, V>> mappables, K key, V value, MappableIndexComparator<K, V> indexComparator
    ) {
        if (indexComparator.usableForKey(key)) {
            return queryBinarySearch(mappables, key, value, indexComparator);
        } else {
            return querySequentialSearch(mappables, key, value);
        }
    }

    private static <T extends Mappable<K, V>, K, V> List<T> queryBinarySearch(
        List<? extends Mappable<K, V>> mappables, Map<K, V> sample, MappableIndexComparator<K, V> indexComparator
    ) {
        List<T> results = new ArrayList<T>();

        IntegerRange range = binarySearchRange(mappables, sample, indexComparator);

        if (range != null) {
            for (int i = range.getMin(); i <= range.getMax(); i++) {
                results.add((T) mappables.get(i));
            }
        }

        return results;
    }

    private static <T extends Mappable<K, V>, K, V> List<T> querySequentialSearch(
        List<? extends Mappable<K, V>> mappables, Map<K, V> sample
    ) {
        List<T> results = new ArrayList<T>();

        for (Mappable<K, V> mappable : mappables) {
            boolean match = true;

            for (K key : sample.keySet()) {
                if (!mappable.getAsMapValue(key).equals(sample.get(key))) {
                    match = false;
                    break;
                }
            }

            if (match) {
                results.add((T)mappable);
            }
        }

        return results;
    }

    private static <T extends Mappable<K, V>, K, V> List<T> queryBinarySearch(
        List<? extends Mappable<K, V>> mappables, K key, V value, MappableIndexComparator<K, V> indexComparator
    ) {
        List<T> results = new ArrayList<T>();

        IntegerRange range = binarySearchRange(mappables, key, value, indexComparator);

        if (range != null) {
            for (int i = range.getMin(); i <= range.getMax(); i++) {
                results.add((T) mappables.get(i));
            }
        }

        return results;
    }

    private static <T extends Mappable<K, V>, K, V> List<T> querySequentialSearch(
        List<? extends Mappable<K, V>> mappables, K key, V value
    ) {
        List<T> results = new ArrayList<T>();

        for (Mappable<K, V> mappable : mappables) {
            if (value.equals(mappable.getAsMapValue(key))) {
                results.add((T) mappable);
            }
        }

        return results;
    }

    private static <T extends Mappable<K, V>, K, V> IntegerRange binarySearchRange(
        List<? extends Mappable<K, V>> mappables, K key, V value, MappableIndexComparator<K, V> indexComparator
    ) {
        if (mappables.size() == 0) {
            return null;
        } else {
            int s = 0, e = mappables.size()-1, ps = s, pe = e;

            while (s <= e) {
                int m = (s+e) >>> 1;

                Mappable<K, V> mv = mappables.get(m);

                int cmp = indexComparator.compare(mv, key, value);

                if (cmp < 0) {
                    s = ps = m + 1;
                } else if (cmp > 0) {
                    e = pe = m - 1;
                } else {
                    return binarySearchRange(mappables, key, value, indexComparator, m, ps, pe);
                }
            }

            return null;
        }
    }

    private static <T extends Mappable<K, V>, K, V> IntegerRange binarySearchRange(
        List<? extends Mappable<K, V>> mappables, K key, V value, MappableIndexComparator<K, V> indexComparator,
        int found, int possibleStart, int possibleEnd
    ) {
        int s = possibleStart, e = found, fs, fe;

        while (s < e) {
            if (s == e-1) {
                Mappable<K, V> mv = mappables.get(s);

                int cmp = indexComparator.compare(mv, key, value);

                if (cmp < 0) {
                    s = e;
                    break;
                } else {
                    break;
                }
            } else {
                int m = (s + e) >>> 1;

                Mappable<K, V> mv = mappables.get(m);

                int cmp = indexComparator.compare(mv, key, value);

                if (cmp < 0) {
                    s = m + 1;
                } else {
                    e = m;
                }
            }
        }

        fs = s;

        s = found;
        e = possibleEnd;

        while (s < e) {
            if (e == s + 1) {
                Mappable<K, V> mv = mappables.get(e);

                int cmp = indexComparator.compare(mv, key, value);

                if (cmp > 0) {
                    e = s;
                    break;
                } else {
                    break;
                }
            } else {
                int m = (s + e) >>> 1;

                Mappable<K, V> mv = mappables.get(m);

                int cmp = indexComparator.compare(mv, key, value);

                if (cmp > 0) {
                    e = m - 1;
                } else {
                    s = m;
                }
            }
        }

        fe = e;

        return new IntegerRange(fs, fe);
    }

    private static <T extends Mappable<K, V>, K, V> IntegerRange binarySearchRange(
        List<? extends Mappable<K, V>> mappables, Map<K, V> sample, MappableIndexComparator<K, V> indexComparator
    ) {
        if (mappables.size() == 0) {
            return null;
        } else {
            int s = 0, e = mappables.size()-1, ps = s, pe = e;

            while (s <= e) {
                int m = (s+e) >>> 1;

                Mappable<K, V> mv = mappables.get(m);

                int cmp = indexComparator.compare(mv, sample);

                if (cmp < 0) {
                    s = ps = m + 1;
                } else if (cmp > 0) {
                    e = pe = m - 1;
                } else {
                    return binarySearchRange(mappables, sample, indexComparator, m, ps, pe);
                }
            }

            return null;
        }
    }

    private static <T extends Mappable<K, V>, K, V> IntegerRange binarySearchRange(
        List<? extends Mappable<K, V>> mappables, Map<K, V> sample, MappableIndexComparator<K, V> indexComparator,
        int found, int possibleStart, int possibleEnd
    ) {
        int s = possibleStart, e = found, fs, fe;

        while (s < e) {
            if (s == e-1) {
                Mappable<K, V> mv = mappables.get(s);

                int cmp = indexComparator.compare(mv, sample);

                if (cmp < 0) {
                    s = e;
                    break;
                } else {
                    break;
                }
            } else {
                int m = (s + e) >>> 1;

                Mappable<K, V> mv = mappables.get(m);

                int cmp = indexComparator.compare(mv, sample);

                if (cmp < 0) {
                    s = m + 1;
                } else {
                    e = m;
                }
            }
        }

        fs = s;

        s = found;
        e = possibleEnd;

        while (s < e) {
            if (e == s + 1) {
                Mappable<K, V> mv = mappables.get(e);

                int cmp = indexComparator.compare(mv, sample);

                if (cmp > 0) {
                    e = s;
                    break;
                } else {
                    break;
                }
            } else {
                int m = (s + e) >>> 1;

                Mappable<K, V> mv = mappables.get(m);

                int cmp = indexComparator.compare(mv, sample);

                if (cmp > 0) {
                    e = m - 1;
                } else {
                    s = m;
                }
            }
        }

        fe = e;

        return new IntegerRange(fs, fe);
    }

    public static <T extends Mappable<K,V>, K, V> List<T> query(
        Collection<? extends Mappable<K, V>> mappables, Map<K, V> sample, Comparator<T> comparator
    ) {
        List<T> results = query(mappables, sample);

        Collections.sort(results, comparator);

        return results;
    }

    public static <T extends Mappable<K, V>, K, V> List<T> query(
        Collection<? extends Mappable<K, V>> mappables, Map<K, V> sample,
        MappableSampleQueryMatcher<K, V> sampleQueryMatcher
    ) {
        List<T> results = new ArrayList<T>();
        for (Mappable<K, V> mappable : mappables) {
            if (sampleQueryMatcher.match(mappable, sample)) {
                results.add((T)mappable);
            }
        }
        return results;
    }

    public static <T extends Mappable<K, V>, K, V> List<T> query(
        Collection<? extends Mappable<K, V>> mappables, Map<K, V> sample,
        MappableSampleQueryMatcher<K, V> sampleQueryMatcher, Comparator<T> comparator
    ) {
        List<T> results = query(mappables, sample, sampleQueryMatcher);

        Collections.sort(results, comparator);

        return results;
    }

    public static <T extends Mappable<K, V>, K, V> List<T> queryMax(
        Collection<? extends Mappable<K, V>> mappables, K key
    ) {
        List<T> results = new ArrayList<T>();
        V currentMax = null;

        for (Mappable<K, V> mappable : mappables) {
            V v = mappable.getAsMapValue(key);

            if (currentMax == null) {
                currentMax = v;
                results.add((T)mappable);
            } else {
                int cmp = DataComparator.compare(v, currentMax, true, true);

                if (cmp > 0) {
                    currentMax = v;
                    results.clear();
                    results.add((T)mappable);
                } else if (cmp == 0) {
                    results.add((T)mappable);
                }
            }
        }

        return results;
    }

    public static <T extends Mappable<K, V>, K, V> List<T> queryMin(
        Collection<? extends Mappable<K, V>> mappables, K key
    ) {
        List<T> results = new ArrayList<T>();
        V currentMin = null;

        for (Mappable<K, V> mappable : mappables) {
            V v = mappable.getAsMapValue(key);

            if (currentMin == null) {
                currentMin = v;
                results.add((T)mappable);
            } else {
                int cmp = DataComparator.compare(v, currentMin, true, true);

                if (cmp < 0) {
                    currentMin = v;
                    results.clear();
                    results.add((T)mappable);
                } else if (cmp == 0) {
                    results.add((T)mappable);
                }
            }
        }

        return results;
    }

    public static <T extends Mappable<K, V>, K, V> List<T> queryMax(
        Collection<? extends Mappable<K, V>> mappables, K key, Comparator<V> comparator
    ) {
        List<T> results = new ArrayList<T>();
        V currentMax = null;

        for (Mappable<K, V> mappable : mappables) {
            V v = mappable.getAsMapValue(key);

            if (currentMax == null) {
                currentMax = v;
                results.add((T)mappable);
            } else {
                int cmp = comparator.compare(v, currentMax);

                if (cmp > 0) {
                    currentMax = v;
                    results.clear();
                    results.add((T)mappable);
                } else if (cmp == 0) {
                    results.add((T)mappable);
                }
            }
        }

        return results;
    }

    public static <T extends Mappable<K, V>, K, V> List<T> queryMin(
        Collection<? extends Mappable<K, V>> mappables, K key, Comparator<V> comparator
    ) {
        List<T> results = new ArrayList<T>();
        V currentMin = null;

        for (Mappable<K, V> mappable : mappables) {
            V v = mappable.getAsMapValue(key);

            if (currentMin == null) {
                currentMin = v;
                results.add((T)mappable);
            } else {
                int cmp = comparator.compare(v, currentMin);

                if (cmp < 0) {
                    currentMin = v;
                    results.clear();
                    results.add((T)mappable);
                } else if (cmp == 0) {
                    results.add((T)mappable);
                }
            }
        }

        return results;
    }
}
