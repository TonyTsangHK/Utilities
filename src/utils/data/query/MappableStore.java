package utils.data.query;

import utils.data.CrossComparator;
import utils.data.DataManipulator;
import utils.data.Mappable;
import utils.data.SortedListAvl;
import utils.math.IntegerRange;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2014-06-30
 * Time: 09:37
 */
public class MappableStore<T extends Mappable<K, V>, K, V> {
    private SortedListAvl<T> mappables;

    private MappableIndexComparator<K, V> indexComparator;

    public MappableStore() {
        this.mappables = new SortedListAvl<T>();
    }

    public MappableStore(List<T> mappables) {
        this.mappables = new SortedListAvl<T>(mappables);
    }

    public MappableStore(MappableIndexComparator<K, V> indexComparator) {
        this.indexComparator = indexComparator;
        this.mappables = new SortedListAvl<T>((Comparator<T>)indexComparator);
    }

    public MappableStore(List<T> mappables, MappableIndexComparator<K, V> indexComparator) {
        this.indexComparator = indexComparator;
        this.mappables = new SortedListAvl<T>(mappables, (Comparator<T>)indexComparator);
    }

    public void add(T mappable) {
        this.mappables.add(mappable);
    }

    public boolean remove(T mappable) {
        return this.mappables.remove(mappable);
    }

    public T remove(int index) {
        return this.mappables.remove(index);
    }

    public List<T> getMappables() {
        return this.mappables;
    }

    public MappableIndexComparator getIndexComparator() {
        return this.indexComparator;
    }

    public T get(int index) {
        if (index >= 0 && index < this.mappables.size()) {
            return this.mappables.get(index);
        } else {
            return null;
        }
    }

    public List<T> query(K key, V value) {
        if (this.indexComparator != null && this.indexComparator.usableForKey(key)) {
            IntegerRange range = binarySearchRange(key, value);

            return get(range);
        } else {
            return MappableQueryer.query(this.mappables, key, value);
        }
    }

    public List<T> query(Map<K, V> sample) {
        if (this.indexComparator != null && this.indexComparator.usableForKeys(sample.keySet())) {
            IntegerRange range = binarySearchRange(sample);

            return get(range);
        } else {
            return MappableQueryer.query(this.mappables, sample);
        }
    }

    public void setIndexComparator(MappableIndexComparator<K, V> indexComparator) {
        if (this.indexComparator != indexComparator) {
            this.indexComparator = indexComparator;

            SortedListAvl<T> tmp = this.mappables;
            if (this.indexComparator == null) {
                this.mappables = new SortedListAvl<T>(tmp);
            } else {
                this.mappables = new SortedListAvl<T>(tmp, (Comparator<T>)this.indexComparator);
            }
        }
    }

    public List<K> getIndexKeys() {
        return (this.indexComparator != null)? this.indexComparator.getIndexedKeys() : null;
    }

    public List<T> get(IntegerRange range) {
        List<T> results = new ArrayList<T>();
        if (range != null) {
            int s = range.getMin(), e = range.getMax();

            if (s >= 0 && e >= 0 && s < this.mappables.size() && e < this.mappables.size()) {
                ListIterator<T> iter = this.mappables.listIterator(s);

                int c = s;
                while (c <= e) {
                    results.add(iter.next());
                    c++;
                }
            }
        }

        return results;
    }

    public int binarySearch(K key, V value) {
        return binarySearch(DataManipulator.createSimpleMap(key, value));
    }

    private IntegerRange binarySearchRange(K key, V value) {
        return binarySearchRange(DataManipulator.createSimpleMap(key, value));
    }

    public IntegerRange binarySearchRange(final Map<K, V> sample) {
        if (this.indexComparator != null) {
            CrossComparator<Map<K, V>, T> cc = new CrossComparator<Map<K, V>, T>() {
                @Override
                public int compare(Map<K, V> sample, T t) {
                    return -indexComparator.compare(t, sample);
                }
            };

            int startIndex = this.mappables.indexOf(sample, cc);

            if (startIndex != -1) {
                return new IntegerRange(startIndex, this.mappables.lastIndexOf(sample, cc));
            } else {
                return null;
            }
        } else {
            throw new UnsupportedOperationException("Binary search is only available with indexed store!");
        }
    }

    public int binarySearch(Map<K, V> sample) {
        CrossComparator<Map<K, V>, T> cc;
        if (this.indexComparator != null) {
            cc = new CrossComparator<Map<K, V>, T>() {
                @Override
                public int compare(Map<K, V> sample, T t) {
                    return -indexComparator.compare(t, sample);
                }
            };
            return this.mappables.binarySearch(sample, cc);
        } else {
            throw new UnsupportedOperationException("Binary search is only available with indexed store!");
        }
    }

    private T get(ListIterator<T> i, int index) {
        T obj;
        int pos = i.nextIndex();
        if (pos <= index) {
            do {
                obj = i.next();
            } while (pos++ < index);
        } else {
            do {
                obj = i.previous();
            } while (--pos > index);
        }
        return obj;
    }

    public int size() {
        return this.mappables.size();
    }
}
