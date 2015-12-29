package utils.data.query;

import utils.data.DataComparator;
import utils.math.IntegerRange;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2014-09-26
 * Time: 11:47
 */
public class MapQueryer {
    private MapQueryer() {}

    public static <K, V> List<Map<K, V>> query(
        Collection<? extends Map<K, V>> maps, K key, V value
    ) {
        List<Map<K, V>> results = new ArrayList<Map<K, V>>();
        for (Map<K, V> map : maps) {
            V v = map.get(key);

            if (v.equals(value)) {
                results.add(map);
            }
        }

        return results;
    }

    public static <K, V> List<Map<K, V>> query(
        Collection<? extends Map<K, V>> maps, final K key, V value, boolean sort
    ) {
        List<Map<K, V>> results = query(maps, key, value);

        if (sort) {
            Collections.sort(
                results,
                new Comparator<Map<K, V>>() {
                    @Override
                    public int compare(Map<K, V> o1, Map<K, V> o2) {
                        return DataComparator.compare(o1.get(key), o2.get(key), true, true);
                    }
                }
            );
        }

        return results;
    }

    public static <K, V> List<Map<K, V>> query(
        Collection<? extends Map<K, V>> maps, K key, V value, Comparator<Map<K, V>> sortingComparactor
    ) {
        List<Map<K, V>> results = query(maps, key, value);

        Collections.sort(results, sortingComparactor);

        return results;
    }

    public static <K, V> List<Map<K, V>> query(
        Collection<? extends Map<K, V>> maps, K key, V value, MapQueryMatcher<K, V> queryMatcher
    ) {
        List<Map<K, V>> results = new ArrayList<Map<K, V>>();
        for (Map<K, V> map : maps) {
            if (queryMatcher.match(map, key, value)) {
                results.add(map);
            }
        }
        return results;
    }

    public static <K, V> List<Map<K, V>> query(
        Collection<? extends Map<K, V>> maps, final K key, V value,
        MapQueryMatcher<K, V> queryMatcher, boolean sort
    ) {
        List<Map<K, V>> results = query(maps, key, value, queryMatcher);

        if (sort) {
            Collections.sort(
                results,
                new Comparator<Map<K, V>>() {
                    @Override
                    public int compare(Map<K, V> o1, Map<K, V> o2) {
                        return DataComparator.compare(o1.get(key), o2.get(key), true, true);
                    }
                }
            );
        }

        return results;
    }

    public static <K, V> List<Map<K, V>> query(
        Collection<? extends Map<K, V>> maps, final K key, V value,
        MapQueryMatcher<K, V> queryMatcher, Comparator<Map<K, V>> comparator
    ) {
        List<Map<K, V>> results = query(maps, key, value, queryMatcher);

        Collections.sort(results, comparator);

        return results;
    }

    public static <K, V> List<Map<K, V>> query(
        Collection<? extends Map<K, V>> maps, Map<K, V> sample
    ) {
        List<Map<K, V>> results = new ArrayList<Map<K, V>>();
        for (Map<K, V> map : maps) {
            boolean allMatch = true;
            for (K sampleKey : sample.keySet()) {
                V sampleValue = sample.get(sampleKey), value = map.get(sampleKey);
                if (!sampleValue.equals(value)) {
                    allMatch = false;
                }
            }
            if (allMatch) {
                results.add(map);
            }
        }
        return results;
    }

    // Must be sorted
    public static <K, V> List<Map<K, V>> query(
        List<? extends Map<K, V>> maps, Map<K, V> sample, MapIndexComparator<K, V> indexComparator
    ) {
        if (indexComparator.usableForKeys(sample.keySet())) {
            return queryBinarySearch(maps, sample, indexComparator);
        } else {
            return querySequentialSearch(maps, sample);
        }
    }

    // Must be sorted
    public static <K, V> List<Map<K, V>> query(
        List<? extends Map<K, V>> maps, K key, V value, MapIndexComparator<K, V> indexComparator
    ) {
        if (indexComparator.usableForKey(key)) {
            return queryBinarySearch(maps, key, value, indexComparator);
        } else {
            return querySequentialSearch(maps, key, value);
        }
    }

    private static <K, V> List<Map<K, V>> queryBinarySearch(
        List<? extends Map<K, V>> maps, Map<K, V> sample, MapIndexComparator<K, V> indexComparator
    ) {
        List<Map<K, V>> results = new ArrayList<Map<K, V>>();

        IntegerRange range = binarySearchRange(maps, sample, indexComparator);

        if (range != null) {
            for (int i = range.getMin(); i <= range.getMax(); i++) {
                results.add(maps.get(i));
            }
        }

        return results;
    }

    private static <K, V> List<Map<K, V>> querySequentialSearch(
        List<? extends Map<K, V>> maps, Map<K, V> sample
    ) {
        List<Map<K, V>> results = new ArrayList<Map<K, V>>();

        for (Map<K, V> map : maps) {
            boolean match = true;

            for (K key : sample.keySet()) {
                if (!map.get(key).equals(sample.get(key))) {
                    match = false;
                    break;
                }
            }

            if (match) {
                results.add(map);
            }
        }

        return results;
    }

    private static <K, V> List<Map<K, V>> queryBinarySearch(
        List<? extends Map<K, V>> maps, K key, V value, MapIndexComparator<K, V> indexComparator
    ) {
        List<Map<K, V>> results = new ArrayList<Map<K, V>>();

        IntegerRange range = binarySearchRange(maps, key, value, indexComparator);

        if (range != null) {
            for (int i = range.getMin(); i <= range.getMax(); i++) {
                results.add(maps.get(i));
            }
        }

        return results;
    }

    private static <K, V> List<Map<K, V>> querySequentialSearch(
        List<? extends Map<K, V>> maps, K key, V value
    ) {
        List<Map<K, V>> results = new ArrayList<Map<K, V>>();

        for (Map<K, V> map : maps) {
            if (value.equals(map.get(key))) {
                results.add(map);
            }
        }

        return results;
    }

    private static <K, V> IntegerRange binarySearchRange(
        List<? extends Map<K, V>> maps, K key, V value, MapIndexComparator<K, V> indexComparator
    ) {
        if (maps.size() == 0) {
            return null;
        } else {
            int s = 0, e = maps.size()-1, ps = s, pe = e;

            while (s <= e) {
                int m = (s+e) >>> 1;

                Map<K, V> mv = maps.get(m);

                int cmp = indexComparator.compare(mv, key, value);

                if (cmp < 0) {
                    s = ps = m + 1;
                } else if (cmp > 0) {
                    e = pe = m - 1;
                } else {
                    return binarySearchRange(maps, key, value, indexComparator, m, ps, pe);
                }
            }

            return null;
        }
    }

    private static <K, V> IntegerRange binarySearchRange(
        List<? extends Map<K, V>> maps, K key, V value, MapIndexComparator<K, V> indexComparator,
        int found, int possibleStart, int possibleEnd
    ) {
        int s = possibleStart, e = found, fs, fe;

        while (s < e) {
            if (s == e-1) {
                Map<K, V> mv = maps.get(s);

                int cmp = indexComparator.compare(mv, key, value);

                if (cmp < 0) {
                    s = e;
                    break;
                } else {
                    break;
                }
            } else {
                int m = (s + e) >>> 1;

                Map<K, V> mv = maps.get(m);

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
                Map<K, V> mv = maps.get(e);

                int cmp = indexComparator.compare(mv, key, value);

                if (cmp > 0) {
                    e = s;
                    break;
                } else {
                    break;
                }
            } else {
                int m = (s + e) >>> 1;

                Map<K, V> mv = maps.get(m);

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

    private static <K, V> IntegerRange binarySearchRange(
        List<? extends Map<K, V>> maps, Map<K, V> sample, MapIndexComparator<K, V> indexComparator
    ) {
        if (maps.size() == 0) {
            return null;
        } else {
            int s = 0, e = maps.size()-1, ps = s, pe = e;

            while (s <= e) {
                int m = (s+e) >>> 1;

                Map<K, V> mv = maps.get(m);

                int cmp = indexComparator.compare(mv, sample);

                if (cmp < 0) {
                    s = ps = m + 1;
                } else if (cmp > 0) {
                    e = pe = m - 1;
                } else {
                    return binarySearchRange(maps, sample, indexComparator, m, ps, pe);
                }
            }

            return null;
        }
    }

    private static <K, V> IntegerRange binarySearchRange(
        List<? extends Map<K, V>> maps, Map<K, V> sample, MapIndexComparator<K, V> indexComparator,
        int found, int possibleStart, int possibleEnd
    ) {
        int s = possibleStart, e = found, fs, fe;

        while (s < e) {
            if (s == e-1) {
                Map<K, V> mv = maps.get(s);

                int cmp = indexComparator.compare(mv, sample);

                if (cmp < 0) {
                    s = e;
                    break;
                } else {
                    break;
                }
            } else {
                int m = (s + e) >>> 1;

                Map<K, V> mv = maps.get(m);

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
                Map<K, V> mv = maps.get(e);

                int cmp = indexComparator.compare(mv, sample);

                if (cmp > 0) {
                    e = s;
                    break;
                } else {
                    break;
                }
            } else {
                int m = (s + e) >>> 1;

                Map<K, V> mv = maps.get(m);

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

    public static <K, V> List<Map<K, V>> query(
        Collection<? extends Map<K, V>> maps, Map<K, V> sample, Comparator<Map<K, V>> comparator
    ) {
        List<Map<K, V>> results = query(maps, sample);

        Collections.sort(results, comparator);

        return results;
    }

    public static <K, V> List<Map<K, V>> query(
        Collection<? extends Map<K, V>> maps, Map<K, V> sample,
        MapSampleQueryMatcher<K, V> sampleQueryMatcher
    ) {
        List<Map<K, V>> results = new ArrayList<Map<K, V>>();
        for (Map<K, V> map : maps) {
            if (sampleQueryMatcher.match(map, sample)) {
                results.add(map);
            }
        }
        return results;
    }

    public static <K, V> List<Map<K, V>> query(
        Collection<? extends Map<K, V>> maps, Map<K, V> sample,
        MapSampleQueryMatcher<K, V> sampleQueryMatcher, Comparator<Map<K, V>> comparator
    ) {
        List<Map<K, V>> results = query(maps, sample, sampleQueryMatcher);

        Collections.sort(results, comparator);

        return results;
    }

    public static <K, V> List<Map<K, V>> queryMax(
        Collection<? extends Map<K, V>> maps, K key
    ) {
        List<Map<K, V>> results = new ArrayList<Map<K, V>>();
        V currentMax = null;

        for (Map<K, V> map : maps) {
            V v = map.get(key);

            if (currentMax == null) {
                currentMax = v;
                results.add(map);
            } else {
                int cmp = DataComparator.compare(v, currentMax, true, true);

                if (cmp > 0) {
                    currentMax = v;
                    results.clear();
                    results.add(map);
                } else if (cmp == 0) {
                    results.add(map);
                }
            }
        }

        return results;
    }

    public static <K, V> List<Map<K, V>> queryMin(
        Collection<? extends Map<K, V>> maps, K key
    ) {
        List<Map<K, V>> results = new ArrayList<Map<K, V>>();
        V currentMin = null;

        for (Map<K, V> map : maps) {
            V v = map.get(key);

            if (currentMin == null) {
                currentMin = v;
                results.add(map);
            } else {
                int cmp = DataComparator.compare(v, currentMin, true, true);

                if (cmp < 0) {
                    currentMin = v;
                    results.clear();
                    results.add(map);
                } else if (cmp == 0) {
                    results.add(map);
                }
            }
        }

        return results;
    }

    public static <K, V> List<Map<K, V>> queryMax(
        Collection<? extends Map<K, V>> maps, K key, Comparator<V> comparator
    ) {
        List<Map<K, V>> results = new ArrayList<Map<K, V>>();
        V currentMax = null;

        for (Map<K, V> map : maps) {
            V v = map.get(key);

            if (currentMax == null) {
                currentMax = v;
                results.add(map);
            } else {
                int cmp = comparator.compare(v, currentMax);

                if (cmp > 0) {
                    currentMax = v;
                    results.clear();
                    results.add(map);
                } else if (cmp == 0) {
                    results.add(map);
                }
            }
        }

        return results;
    }

    public static <K, V> List<Map<K, V>> queryMin(
        Collection<? extends Map<K, V>> maps, K key, Comparator<V> comparator
    ) {
        List<Map<K, V>> results = new ArrayList<Map<K, V>>();
        V currentMin = null;

        for (Map<K, V> map : maps) {
            V v = map.get(key);

            if (currentMin == null) {
                currentMin = v;
                results.add(map);
            } else {
                int cmp = comparator.compare(v, currentMin);

                if (cmp < 0) {
                    currentMin = v;
                    results.clear();
                    results.add(map);
                } else if (cmp == 0) {
                    results.add(map);
                }
            }
        }

        return results;
    }
}
