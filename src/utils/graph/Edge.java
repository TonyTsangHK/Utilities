package utils.graph;

public class Edge<T> {
    protected Node<T> endNode1, endNode2;
    
    public Edge(Node<T> node1, Node<T> node2) {
        setEndNode1(node1);
        setEndNode2(node2);
    }
    
    public void setEndNode1(Node<T> node) {
        endNode1 = node;
    }
    
    public void setEndNode2(Node<T> node) {
        endNode2 = node;
    }
    
    public Node<T> getEndNode1() {
        return endNode1;
    }
    
    public Node<T> getEndNode2() {
        return endNode2;
    }
    
    public boolean isRelatedNode(Node<T> node) {
        return endNode1.equals(node) || endNode2.equals(node);
    }
}
