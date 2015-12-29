package utils.graph;

import java.util.ArrayList;
import java.util.List;

public class DirectionalNodePath<T> implements Comparable<DirectionalNodePath<T>> {
    private List<DirectionalNode<T>> path;
    private DirectionalNode<T> nextNode;
    
    public DirectionalNodePath(List<DirectionalNode<T>> path, DirectionalNode<T> nextNode) {
        this.path = path;
        this.nextNode = nextNode;
    }
    
    public void addNode(DirectionalNode<T> node) {
        path.add(nextNode);
        nextNode = node;
    }
    
    public boolean contains(DirectionalNode<T> node) {
        if (nextNode == node) {
            return true;
        } else {
            return path.contains(node);
        }
    }
    
    public DirectionalNode<T> getNextNode() {
        return nextNode;
    }
    
    public List<DirectionalNode<T>> toList() {
        List<DirectionalNode<T>> list = new ArrayList<DirectionalNode<T>>(path);
        list.add(nextNode);
        return list;
    }
    
    public int size() {
        return path.size() + 1;
    }
    
    @Override
    public int compareTo(DirectionalNodePath<T> otherPath) {
        if (otherPath == null) {
            return 1;
        } else {
            return path.size() - otherPath.path.size();
        }
    }
}
