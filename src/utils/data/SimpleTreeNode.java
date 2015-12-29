package utils.data;

import java.util.ArrayList;
import java.util.ListIterator;

public class SimpleTreeNode<V> implements AbstractTreeNode<V> {
    private SimpleTreeNode<V> parentNode;
    private ArrayList<SimpleTreeNode<V>> childNodes;
    private V data;
    
    public SimpleTreeNode(V data) {
        childNodes = new ArrayList<SimpleTreeNode<V>>();
        setData(data);
    }

    @Override
    public void addChildNode(int i, AbstractTreeNode<V> node) {
        node.setParent(this);
        childNodes.add(i, (SimpleTreeNode<V>)node);
    }

    @Override
    public void addChildNode(AbstractTreeNode<V> node) {
        node.setParent(this);
        childNodes.add((SimpleTreeNode<V>) node);
    }
    
    @Override
    public SimpleTreeNode<V> getChildNode(int i) {
        return childNodes.get(i);
    }
    
    @Override
    public V getChildNodeData(int i) {
        return childNodes.get(i).getData();
    }

    @Override
    public ArrayList<SimpleTreeNode<V>> getChildNodes() {
        return childNodes;
    }

    @Override
    public int getChildNodesCount() {
        return childNodes.size();
    }

    @Override
    public V getData() {
        return data;
    }

    @Override
    public V getParentData() {
        if (parentNode != null) {
            return parentNode.getData();
        } else {
            return null;
        }
    }

    @Override
    public SimpleTreeNode<V> getParentNode() {
        return parentNode;
    }

    @Override
    public boolean hasChild() {
        return !childNodes.isEmpty();
    }

    @Override
    public boolean isLeaf() {
        return childNodes.isEmpty();
    }

    @Override
    public SimpleTreeNode<V> removeChild(int i) {
        return childNodes.remove(i);
    }
    
    public void removeChildNode(SimpleTreeNode<V> node) {
        childNodes.remove(node);
    }

    @Override
    public SimpleTreeNode<V> removeChild(V data) {
        ListIterator<SimpleTreeNode<V>> iter = childNodes.listIterator();
        SimpleTreeNode<V> nodeToReturn = null;
        while (iter.hasNext()) {
            SimpleTreeNode<V> node = iter.next();
            if (node.data == data) {
                iter.remove();
                nodeToReturn = node;
                break;
            }
        }
        return nodeToReturn;
    }

    @Override
    public void setData(V data) {
        this.data = data;
    }

    @Override
    public boolean setChildNodeData(int i, V data) {
        if (i >= 0 && i < childNodes.size()) {
            childNodes.get(i).setData(data);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setParent(AbstractTreeNode<V> parentNode) {
        this.parentNode = (SimpleTreeNode<V>) parentNode;
    }

}
