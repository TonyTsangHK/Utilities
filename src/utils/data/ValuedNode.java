package utils.data;

public class ValuedNode<T extends Comparable<T>> implements Comparable<ValuedNode<T>> {
    private ValuedNode<T> parent;
    private T data;
    private MinMaxQueue<ValuedNode<T>> childs;
    private Cost culminlativeCost;
    
    public ValuedNode(T data) {
        this.data = data;
        childs = new MinMaxQueue<ValuedNode<T>>();
        culminlativeCost = new Cost();
    }
    
    public int compareTo(ValuedNode<T> node) {
        return data.compareTo(node.getData());
    }
    
    public T getData() {
        return data;
    }
    
    public ValuedNode<T> getParent() {
        return parent;
    }
    
    public T getParantData() {
        return (parent != null)? parent.getData() : null;
    }
    
    public void setParent(ValuedNode<T> p) {
        parent = p;
    }
    
    public boolean hasChild() {
        return !childs.isEmpty();
    }
    
    public int getChildSize() {
        return childs.size();
    }
    
    public void addChild(T data) {
        ValuedNode<T> n = new ValuedNode<T>(data);
        n.setParent(this);
        childs.add(n);
    }
    
    public void addChild(ValuedNode<T> n) {
        if (n != null) {
            n.setParent(this);
            childs.add(n);
        }
    }
    
    public ValuedNode<T> getChild(int i) {
        if (i >= 0 && i < childs.size()) {
            return childs.get(i);
        } else {
            return null;
        }
    }
    
    public ValuedNode<T> getMinChild() {
        return childs.getMin();
    }
    
    public ValuedNode<T> getMaxChild() {
        return childs.getMax();
    }
    
    public T getChildData(int i) {
        if (i >= 0 && i < childs.size()) {
            return childs.get(i).getData();
        } else {
            return null;
        }
    }
    
    public Cost getCulminlativeCost() {
        return culminlativeCost;
    }
    
    public class Cost implements Comparable<Cost> {
        private double cost;
        
        public Cost() {
            cost = 0;
        }
        
        public double getCost() {
            return cost;
        }
        
        public void setCost(double c) {
            cost = c;
        }
        
        public void increment(double c) {
            cost += c;
        }
        
        public int compareTo(Cost c) {
            return (int) (cost - c.cost);
        }
    }
}
