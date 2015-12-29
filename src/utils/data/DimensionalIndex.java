/*
 * Multidimensional index for DimensionalList
 */

package utils.data;

import java.util.Arrays;

/**
 *
 * @author TonyTsang
 */
public class DimensionalIndex {
    private int[] indexes;

    public DimensionalIndex(int ... indexes) {
        this.indexes = indexes;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DimensionalIndex) {
            if (o != null) {
                int[] oIndexes = ((DimensionalIndex)o).indexes;
                if (oIndexes.length == indexes.length) {
                    for (int i = 0; i < oIndexes.length; i++) {
                        if (oIndexes[i] != indexes[i]) {
                            return false;
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(indexes);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < indexes.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(indexes[i]);
        }
        return builder.toString();
    }

    public int getSubIndex(int i) {
        return indexes[i];
    }

    public int getDimension() {
        return indexes.length;
    }
}
