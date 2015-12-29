package utils.string;

import java.util.ArrayList;

public class DimensionalKey {
    public static final int DEFAULT_INITVALUE = 1, DEFAULT_DIMENSION = 1;
    public static final String DEFAULT_SPERATOR = "_";
    
    private String key, sperator;
    private int initialIndexValue, dimension;
    private ArrayList<Integer> indexes;
    
    private StringBuilder builder;
    
    public DimensionalKey(String key) {
        this(key, DEFAULT_SPERATOR, DEFAULT_INITVALUE, DEFAULT_DIMENSION);
    }
    /*
    public DimensionalKey(String key, int ... indexes) {
        this(key, DEFAULT_SPERATOR, DEFAULT_INITVALUE, DEFAULT_DIMENSION, indexes);
    }
    
    public DimensionalKey(String key, String sperator) {
        this(key, sperator, DEFAULT_INITVALUE, DEFAULT_DIMENSION);
    }
    
    public DimensionalKey(String key, String sperator, int ... indexes) {
        this(key, sperator, DEFAULT_INITVALUE, DEFAULT_DIMENSION, indexes);
    }
    
    public DimensionalKey(String key, int dimension) {
        this(key, DEFAULT_SPERATOR, dimension, DEFAULT_INITVALUE);
    }
    
    public DimensionalKey(String key, int dimension, int ... indexes) {
        this(key, DEFAULT_SPERATOR, dimension, DEFAULT_INITVALUE, indexes);
    }
    
    public DimensionalKey(String key, String sperator, int dimension) {
        this(key, sperator, dimension, DEFAULT_INITVALUE);
    }
    
    public DimensionalKey(String key, String sperator, int dimension, int ... indexes) {
        this(key, sperator, dimension, DEFAULT_INITVALUE, indexes);
    }
    
    public DimensionalKey(String key, int dimension, int initialIndexValue) {
        this(key, DEFAULT_SPERATOR, dimension, initialIndexValue);
    }
    
    public DimensionalKey(String key, int dimension, int initialIndexValue, int ... indexes) {
        this(key, DEFAULT_SPERATOR, dimension, initialIndexValue, indexes);
    }
    
    */
    
    public DimensionalKey(String key, String sperator, int dimension, int initialIndexValue) {
        indexes = new ArrayList<Integer>();
        setSperator(sperator);
        setKey(key);
        setInitialIndexValue(initialIndexValue);
        setDimension(dimension);
    }
    
    public DimensionalKey(String key, String sperator, int dimension, int initialIndexVallue, int ... indexes) {
        this(key, sperator, dimension, initialIndexVallue);
        setIndexes(indexes);
    }
    
    public void setKey(String k) {
        key = k;
    }
    
    public String getKey() {
        return key;
    }
    
    public void setSperator(String s) {
        sperator = s;
    }
    
    public String getSperator() {
        return sperator;
    }
    
    public void setInitialIndexValue(int v) {
        if (v >= 0) {
            initialIndexValue = v;
        }
    }
    
    public int getInitialIndexValue() {
        return initialIndexValue;
    }
    
    public void setDimension(int dimension) {
        if (dimension > 0) {
            this.dimension = dimension;
            ensureIndexDimension(dimension);
        }
    }
    
    private void ensureIndexDimension(int d) {
        while (indexes.size() < d) {
            indexes.add(new Integer(initialIndexValue));
        }
        while (indexes.size() > d && d > 0) {
            indexes.remove(indexes.size()-1);
        }
    }
    
    public int getDimension() {
        return dimension;
    }
    
    public void incrementIndex(int l) {
        incrementIndex(l, true);
    }
    
    public String incrementIndexAfter(int l) {
        return incrementIndexAfter(l, true);
    }
    
    public String incrementIndexAfter(int l, boolean resetSusequent) {
        String s = toString();
        incrementIndex(l, resetSusequent);
        return s;
    }
    
    public String incrementIndexBefore(int l) {
        return incrementIndexBefore(l, true);
    }
    
    public String incrementIndexBefore(int l, boolean resetSusequent) {
        incrementIndex(l, resetSusequent);
        return toString();
    }
    
    public void incrementIndex(int l, boolean resetSusequent) {
        if (l >= 0 && l < indexes.size()) {
            indexes.set(l, indexes.get(l).intValue() + 1);
            if (resetSusequent) {
                resetIndexes(l+1);
            }
        }
    }
    
    public String decrementIndexAfter(int l) {
        return decrementIndexAfter(l, true);
    }
    
    public String decrementIndexAfter(int l, boolean resetSusequent) {
        String s = toString();
        decrementIndex(l, resetSusequent);
        return s;
    }
    
    public String decrementIndexBefore(int l) {
        return decrementIndexBefore(l, true);
    }
    
    public String decrementIndexBefore(int l, boolean resetSusequent) {
        decrementIndex(l, resetSusequent);
        return toString();
    }
    
    public void decrementIndex(int l) {
        decrementIndex(l, true);
    }
    
    public void decrementIndex(int l, boolean resetSusequent) {
        if (l >= 0 && l < indexes.size()) {
            int v = indexes.get(l);
            if (v - 1 >= 0) {
                indexes.set(l, v-1);
                if (resetSusequent) {
                    resetIndexes(l+1);
                }
            }
        }
    }
    
    public void reset() {
        for (int i = 0; i < indexes.size(); i++) {
            setIndex(i, initialIndexValue);
        }
    }
    
    public void resetIndexes(int l) {
        if (l >= 0 && l < indexes.size()) {
            for (int i = l; i < indexes.size(); i++) {
                setIndex(i, initialIndexValue);
            }
        }
    }
    
    public void setIndexes(int ... indexes) {
        for (int i = 0; i < indexes.length && i < this.indexes.size(); i++) {
            setIndex(i, indexes[i]);
        }
    }
    
    public void setIndex(int l, int i) {
        if (l >= 0 && l < indexes.size() && i >= 0) {
            indexes.set(l, i);
        }
    }
    
    public int getIndex(int l) {
        if (l >= 0 && l < indexes.size()) {
            return indexes.get(l).intValue();
        } else {
            return -1;
        }
    }
    
    @Override
    public String toString() {
        if (builder == null) {
            builder = new StringBuilder();
        } else {
            builder.setLength(0);
        }
        builder.append(key);
        for (int i = 0; i < indexes.size(); i++) {
            builder.append(sperator);
            builder.append(indexes.get(i).intValue());
        }
        return builder.toString();
    }
}