package utils.graph;

import java.util.Iterator;
import java.util.List;

public class Node<T> {
    protected T data;
    
    protected List<Edge<T>> edges;
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public Edge<T> unlinkNode(Node<T> node) {
        Iterator<Edge<T>> iter = edges.iterator();
        while (iter.hasNext()) {
            Edge<T> edge = iter.next();
            if (
                    (this == edge.getEndNode1() && node == edge.getEndNode2()) || 
                    (this == edge.getEndNode2() && node == edge.getEndNode1())
            ) {
                iter.remove();
                return edge;
            }
        }
        return null;
    }
    
    public void linkNode(Node<T> node) {
        Edge<T> edge = new Edge<T>(this, node);
        edges.add(edge);
    }
    
    public Edge<T> findEdgeByLinkedNode(Node<T> node) {
        for (Edge<T> edge : edges) {
            if (
                    (this == edge.getEndNode1() && node == edge.getEndNode2()) || 
                    (this == edge.getEndNode2() && node == edge.getEndNode1())
            ) {
                return edge;
            }
        }
        return null;
    }
}
