package utils.data;

public class DoublyNode<V> implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    
    private V data;
    private DoublyNode<V> previous, next;
    
    public DoublyNode(V data) {
        this(null, null, data);
    }
    
    public DoublyNode(DoublyNode<V> p, DoublyNode<V> n, V data) {
        setData(data);
        setPrevious(p);
        setNext(n);
    }
    
    public V getData() {
        return data;
    }
    
    public V setData(V d) {
        data = d;
        return data;
    }
    
    public DoublyNode<V> getPrevious() {
        return previous;
    }
    
    public DoublyNode<V> getNext() {
        return next;
    }
    
    public DoublyNode<V> getNode(int shift) {
        if (shift == 0) {
            return this;
        } else if (shift < 0) {
            if (previous != null) {
                return previous.getNode(shift+1);
            } else {
                return null;
            }
        } else {
            if (next != null) {
                return next.getNode(shift-1);
            } else {
                return null;
            }
        }
    }
    
    protected DoublyNode<V> setPrevious(DoublyNode<V> n) {
        previous = n;
        if (n != null && n.getNext() != this) {
            n.setNext(this);
        }
        return previous;
    }
    
    protected DoublyNode<V> setNext(DoublyNode<V> n) {
        next = n;
        if (n != null && n.getPrevious() != this) {
            n.setPrevious(this);
        }
        return next;
    }
    
    public DoublyNode<V> locateNode(int i) {
        DoublyNode<V> n = this;
        if (i > 0) {
            while (i > 0) {
                n = n.getNext();
                if (n == null) {
                    return null;
                }
                i--;
            }
            return n;
        } else if (i < 0) {
            while (i < 0) {
                n = n.getPrevious();
                if (n == null) {
                    return null;
                }
                i++;
            }
            return n;
        } else {
            return this;
        }
    }
    
    public boolean hasPrevious() {
        return previous != null;
    }
    
    public boolean hasNext() {
        return next != null;
    }
}
