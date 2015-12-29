package utils.graph;

import java.util.ArrayList;
import java.util.List;

public class NodePath<T> {
    private List<Node<T>> nodes;
    
    private List<Edge<T>> edges;
    
    public NodePath() {
        nodes = new ArrayList<Node<T>>();
        edges = new ArrayList<Edge<T>>();
    }
    
    public void addNode(Node<T> node) {
        if (nodes.isEmpty()) {
            nodes.add(node);
        } else {
            Node<T> lastNode = nodes.get(nodes.size() - 1);
            Edge<T> edge = lastNode.findEdgeByLinkedNode(node);
            if (edge != null) {
                nodes.add(node);
                edges.add(edge);
            }
        }
    }
    
    public void removeLastNode() {
        if (nodes.size() > 0) {            
            nodes.remove(nodes.size() - 1);
            if (edges.size() > 0) {
                edges.remove(edges.size() - 1);
            }
        }
    }
    
    public void clear() {
        nodes.clear();
        edges.clear();
    }
}
