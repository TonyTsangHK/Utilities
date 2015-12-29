/*
 * Multi dimensional list
 */

package utils.data;

import java.util.Collection;
import utils.exception.DimensionException;

/**
 *
 * @author TonyTsang
 */
public class DimensionalList<E> {
    private SimpleTreeNode<E> rootNode;
    private int dimension;

    public DimensionalList(int dimension) {
        checkDimension(dimension);
        this.dimension = dimension;
        rootNode = new SimpleTreeNode<E>(null);
    }

    private void checkDimension(int dimension) {
        if (dimension <= 0) {
            throw new DimensionException("Dimension (" + dimension + ") is not a valid dimension!");
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void setData(E data, DimensionalIndex index) {
        checkDimension(index.getDimension());
        SimpleTreeNode<E> node = rootNode;
        for (int i = 0; i < dimension-1; i++) {
            while (node.getChildNodesCount() <= index.getSubIndex(i)) {
                node.addChildNode(new SimpleTreeNode(null));
            }
            node = node.getChildNode(index.getSubIndex(i));
        }
        int lastIndex = index.getSubIndex(dimension-1);
        while (node.getChildNodesCount() <= lastIndex) {
            node.addChildNode(new SimpleTreeNode<E>(null));
        }
        node.setChildNodeData(lastIndex, data);
    }

    public void setDatas(Collection<E> datas, int ... dimensionalIndexes) {
        int lastIndex = dimension - 1;
        int counter = 0;
        int[] fullDimensionalIndexes = new int[dimension];
        if (dimensionalIndexes.length == dimension) {
            counter = dimensionalIndexes[lastIndex];
            System.arraycopy(dimensionalIndexes, 0, fullDimensionalIndexes, 0, dimensionalIndexes.length);
        } else if (dimensionalIndexes.length == dimension -1) {
            counter = 0;
            System.arraycopy(dimensionalIndexes, 0, fullDimensionalIndexes, 0, dimensionalIndexes.length);
        } else {
            throw new DimensionException("Error setting data collection.");
        }
        for (E data : datas) {
            fullDimensionalIndexes[lastIndex] = counter;
            setData(data, fullDimensionalIndexes);
            counter++;
        }
    }

    public void setData(E data, int ... dimensionIndexes) {
        setData(data, new DimensionalIndex(dimensionIndexes));
    }

    public E getData(DimensionalIndex index) {
        checkDimension(dimension);
        SimpleTreeNode<E> node = rootNode;
        for (int i = 0; i < dimension; i++) {
            node = node.getChildNode(index.getSubIndex(i));
        }
        return node.getData();
    }
    
    public E getData(int ... dimensionIndexes) {
        return getData(new DimensionalIndex(dimensionIndexes));
    }

    public E removeData(DimensionalIndex index) {
        checkDimension(index.getDimension());
        SimpleTreeNode<E> node = rootNode;
        for (int i = 0; i < dimension-1; i++) {
            node = node.getChildNode(index.getSubIndex(i));
        }
        int lastIndex = index.getSubIndex(dimension - 1);
        E data = node.getChildNodeData(lastIndex);
        node.removeChild(lastIndex);
        return data;
    }

    public E removeData(int ... dimensionIndexes) {
        return removeData(new DimensionalIndex(dimensionIndexes));
    }

    public boolean removeData(E data) {
        DimensionalIndex index = indexOf(data);
        if (index == null) {
            return false;
        } else {
            removeData(index);
            return true;
        }
    }

    public DimensionalIndex indexOf(E data) {
        int[] index = ArrayUtil.createIntegerArray(dimension, -1);
        findIndexOf(data, rootNode, index, 0);
        for (int i = 0; i < index.length; i++) {
            if (index[i] == -1) {
                return null;
            }
        }
        return new DimensionalIndex(index);
    }

    private boolean findIndexOf(E data, SimpleTreeNode<E> node, int[] index, int level, int ... parentIndexes) {
        if (level == dimension) {
            if (DataComparator.compare(node.getData(), data, true, true) == 0) {
                System.arraycopy(parentIndexes, 0, index, 0, Math.min(parentIndexes.length, index.length));
                return true;
            }
        }
        for (int i = 0; i < node.getChildNodesCount(); i++) {
            int[] newParentIndexes = new int[parentIndexes.length+1];
            if (parentIndexes.length > 0) {
                System.arraycopy(parentIndexes, 0, newParentIndexes, 0, parentIndexes.length);
            }
            newParentIndexes[parentIndexes.length] = i;
            if (findIndexOf(data, node.getChildNode(i), index, level+1, newParentIndexes)) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(E data) {
        return indexOf(data) != null;
    }

    public int getDimension() {
        return dimension;
    }
    
    public int getLength(DimensionalIndex index) {
        if (index.getDimension() > dimension) {
            throw new DimensionException(index.getDimension() + " is outside dimension: " + dimension);
        } else {
            SimpleTreeNode<E> node = rootNode;
            for (int i = 0; i < index.getDimension(); i++) {
                node = node.getChildNode(index.getSubIndex(i));
            }
            return node.getChildNodesCount();
        }
    }

    public int getLength(int ... indexes) {
        if (indexes == null) {
            return rootNode.getChildNodesCount();
        }
        if (indexes.length > dimension) {
            throw new DimensionException(indexes.length + " is outside dimension: " + dimension);
        } else {
            SimpleTreeNode<E> node = rootNode;
            for (int i = 0; i < indexes.length; i++) {
                node = node.getChildNode(indexes[i]);
            }
            return node.getChildNodesCount();
        }
    }

    public void clear() {
        rootNode = new SimpleTreeNode<E>(null);
    }
}
