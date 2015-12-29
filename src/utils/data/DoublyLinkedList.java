package utils.data;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class DoublyLinkedList<V> extends AbstractList<V> implements Iterable<V>, List<V>, java.io.Serializable {
    private static final long serialVersionUID = 1L;
    
    private transient DoublyNode<V> head, tail;
    private transient int size;
    
    public DoublyLinkedList() {
        head = tail = null;
        size = 0;
    }
    
    private DoublyNode<V> createNode(V data) {
        return new DoublyNode<V>(data);
    }
    
    @Override
    public void clear() {
        head = tail = null;
        size = 0;
    }
    
    public void replace(int i, V data) {
        DoublyNode<V> node = getNode(i);
        if (node != null) {
            node.setData(data);
        }
    }
    
    private void replace(DoublyNode<V> node, int size, HeadTailContainer<V> container) {
        if (head == tail && node == head) {
            head = container.getHead();
            tail = container.getTail();
            this.size = size;
        } else if (node == head) {
            container.getTail().setNext(head.getNext());
            head = container.getHead();
            this.size += size - 1;
        } else if (node == tail) {
            container.getHead().setPrevious(tail.getPrevious());
            tail = container.getTail();
            this.size += size - 1;
        } else {
            if (node.hasPrevious()) {
                node.getPrevious().setNext(container.getHead());
            }
            if (node.hasNext()) {
                node.getNext().setPrevious(container.getTail());
            }
            this.size += size - 1;
        }
    }
    
    public void replace(DoublyNode<V> node, List<V> list) {
        if (list == null || list.size() == 0) {
            removeNode(node);
        } else {
            if (node != null && containsNode(node)) {
                HeadTailContainer<V> container = createNodeList(list);
                replace(node, list.size(), container);
            }
        }
    }
    
    public void replace(DoublyNode<V> node, V ... datas) {
        if (datas == null || datas.length == 0) {
            removeNode(node);
        } else {
            if (node != null && containsNode(node)) {
                HeadTailContainer<V> container = createNodeList(datas);
                replace(node, datas.length, container);
            }
        }
    }
    
    public void replace(DoublyNode<V> node, Collection<V> datas) {
        if (datas == null || datas.size() == 0) {
            removeNode(node);
        } else {
            if (node != null && containsNode(node)) {
                HeadTailContainer<V> container = createNodeList(datas);
                replace(node, datas.size(), container);
            }
        }
    }
    
    public void replace(int i, V ... datas) {
        replace(getNode(i), datas);
    }
    
    public void replace(int i, Collection<V> datas) {
        replace(getNode(i), datas);
    }
    
    public void replace(int i, List<V> list) {
        replace(getNode(i), list);
    }
    
    public void add(V ... datas) {
        for (V data : datas) {
            add(data);
        }
    }
    
    public DoublyNode<V> add(DoublyNode<V> n) {
        return addToTail(n);
    }
    
    public DoublyNode<V> addToHead(V data) {
        return addToHead(createNode(data));
    }
    
    public DoublyNode<V> addToHead(DoublyNode<V> n) {
        if (isEmpty()) {
            return resetToOneNode(n);
        } else if (n != null) {
            head.setPrevious(n);
            head = n;
            size++;
            head.setPrevious(null);
            return head;
        } else {
            return null;
        }
    }
    
    public DoublyNode<V> addToTail(V data) {
        return addToTail(createNode(data));
    }
    
    public DoublyNode<V> addToTail(DoublyNode<V> n) {
        if (isEmpty()) {
            return resetToOneNode(n);
        } else if (n != null) {
            tail.setNext(n);
            tail = n;
            size++;
            tail.setNext(null);
            return tail;
        } else {
            return null;
        }
    }
    
    public DoublyNode<V> insertNode(int i, V data) {
        return insertNode(i, createNode(data));
    }
    
    public DoublyNode<V> insertNode(int i, DoublyNode<V> n) {
        if (i <= 0) {
            return addToHead(n);
        } else if (i >= size) {
            return addToTail(n);
        } else {
            DoublyNode<V> node = getNode(i);
            node.getPrevious().setNext(n);
            n.setNext(node);
            size++;
            return n;
        }
    }
    
    private DoublyNode<V> resetToOneNode(DoublyNode<V> n) {
        head = tail = n;
        head.setPrevious(null);
        head.setNext(null);
        size = 1;
        return head;
    }
    
    public DoublyNode<V> removeHead() {
        if (head == tail) {
            DoublyNode<V> n = head;
            head = tail = null;
            size = 0;
            return n;
        } else {
            DoublyNode<V> n = head;
            head = head.getNext();
            head.setPrevious(null);
            n.setNext(null);
            size--;
            return n;
        }
    }
    
    public DoublyNode<V> removeTail() {
        if (isEmpty()) {
            return null;
        } else if (head == tail) {
            DoublyNode<V> n = tail;
            head = tail = null;
            size = 0;
            return n;
        } else {
            DoublyNode<V> n = tail;
            tail = tail.getPrevious();
            tail.setNext(null);
            n.setPrevious(null);
            size--;
            return n;
        }
    }
    
    public DoublyNode<V> removeNode(DoublyNode<V> n) {
        if (n != null) {
            if (isEmpty()) {
                return null;
            } else if (head == n) {
                return removeHead();
            } else if (tail == n) {
                return removeTail();
            } else {
                DoublyNode<V> pre = n.getPrevious();
                DoublyNode<V> nxt = n.getNext();
                pre.setNext(nxt);
                size--;
                n.setPrevious(null);
                n.setNext(null);
                return n;
            }
        } else {
            return null;
        }
    }
    
    public DoublyNode<V> removeNode(int i) {
        return removeNode(getNode(i));
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    public int getSize() {
        return size;
    }
    
    public DoublyNode<V> getHead() {
        return head;
    }
    
    public DoublyNode<V> getTail() {
        return tail;
    }
    
    public V getHeadData() {
        if (isEmpty()) {
            return null;
        } else {
            return head.getData();
        }
    }
    
    public V getTailData() {
        if (isEmpty()) {
            return null;
        } else {
            return tail.getData();
        }
    }
    
    public V getNodeData(int i) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException("Index: " + i +
                    ", Size: " + size);
        } else {
            DoublyNode<V> n = getNode(i);
            return (n != null)? n.getData() : null;
        }
    }
    
    public DoublyNode<V> getNode(int i) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException("Index: " + i +
                    ", Size: " + size);
        } else if (i == 0) {
            return head;
        } else if (i == size - 1) {
            return tail;
        } else {
            if (i < (size >> 2)) {
                int c = 1;
                DoublyNode<V> n = head;
                while (n != null && c <= i) {
                    n = n.getNext();
                    c++;
                }
                return n;
            } else {
                int c = size - 2;
                DoublyNode<V> n = tail;
                while (n != null && c >= i) {
                    n = n.getPrevious();
                    c--;
                }
                return n;
            }
        }
    }
    
    public void addDatas(Collection<V> datas) {
        for (V d : datas) {
            add(d);
        }
    }
    
    public void addDatas(V ... datas) {
        for (V d : datas) {
            add(d);
        }
    }
    
    public int indexOfData(V data) {
        if (isEmpty()) {
            return -1;
        } else {
            DoublyNode<V> node = head;
            int i = 0;
            while (node != null) {
                if (node.getData().equals(data)) {
                    return i;
                }
                node = node.getNext();
                i++;
            }
            return -1;
        }
    }
    
    public int indexOfNode(DoublyNode<V> n) {
        if (isEmpty()) {
            return -1;
        } else {
            DoublyNode<V> node = head;
            int i = 0;
            while (node != null) {
                if (node == n) {
                    return i;
                }
                node = node.getNext();
                i++;
            }
            return -1;
        }
    }
    
    public int indexOfNode(DoublyNode<V> n, int i) {
        if (isEmpty() || i < 0 || i >= size) {
            return -1;
        } else {
            DoublyNode<V> node = getNode(i);
            int c = i;
            while (node != null) {
                if (node == n) {
                    return c;
                }
                node = node.getNext();
                c++;
            }
            return -1;
        }
    }
    
    public void setList(DoublyLinkedList<V> list) {
        if (list != null) {
            DoublyNode<V> listHead = list.getHead(),
                listTail = list.getTail();
            this.head = listHead;
            this.tail = listTail;
            size = list.getSize();
            DoublyNode<V> node = listHead;
            while (node != null) {
                node = node.getNext();
            }
        }
    }
    
    public void insertSubList(int i, DoublyLinkedList<V> list) {
        if (i >= 0 && i < size && list != null && list != this) {
            size += list.getSize();
            DoublyNode<V> h = list.getHead();
            DoublyNode<V> t = list.getTail();
            DoublyNode<V> n = getNode(i);
            DoublyNode<V> p = n.getPrevious();
            if (p != null) {
                h.setPrevious(p);
            }
            t.setNext(n);
            DoublyNode<V> node = h;
            while (node != t) {
                node = node.getNext();
            }
        } else if (size == 0) {
            setList(list);
        }
    }
    
    public void removeInterval(DoublyNode<V> startNode, DoublyNode<V> endNode) {
        if (startNode == endNode) {
            removeNode(startNode);
        } else {
            int startIndex = indexOfNode(startNode);
            int endIndex = indexOfNode(endNode, startIndex);
            if (startIndex > -1 && endIndex > -1) {
                int length = endIndex - startIndex + 1;
                size -= length;
                if (size == 0) {
                    head = tail = null;
                } else if (head == startNode) {
                    head = endNode.getNext();
                    head.setPrevious(null);
                    endNode.setNext(null);    
                } else if (tail == endNode) {
                    tail = startNode.getPrevious();
                    tail.setNext(null);
                    startNode.setPrevious(null);
                } else {
                    startNode.getPrevious().setNext(endNode.getNext());
                    startNode.setPrevious(null);
                    endNode.setNext(null);
                }
                DoublyNode<V> node = startNode;
                while (node != null) {
                    node = node.getNext();
                }
            }
        }
    }
    
    public void removeInterval(int startIndex, int endIndex) {
        if (startIndex >= 0 && endIndex >= 0 && startIndex < size && endIndex < size) {
            int length = endIndex - startIndex + 1;
            DoublyNode<V> startNode = getNode(startIndex);
            DoublyNode<V> endNode = getNode(endIndex);
            size -= length;
            if (size == 0) {
                head = tail = null;
            } else if (startNode == head) {
                head = endNode.getNext();
                head.setPrevious(null);
                endNode.setNext(null);
            } else if (endNode == tail) {
                tail = startNode.getPrevious();
                tail.setNext(null);
                startNode.setPrevious(null);
            } else {
                startNode.getPrevious().setNext(endNode.getNext());
                startNode.setPrevious(null);
                endNode.setNext(null);
            }
            DoublyNode<V> node = startNode;
            while (node != null) {
                node = node.getNext();
            }
        }
    }
    
    public boolean containsData(V data) {
        DoublyNode<V> node = head;
        while (node != null) {
            if (data.equals(node.getData())) {
                return true;
            }
            node = node.getNext();
        }
        return false;
    }
    
    public boolean containsNode(DoublyNode<V> n) {
        DoublyNode<V> node = head;
        while (node != null) {
            if (node == n) {
                return true;
            }
            node = node.getNext();
        }
        return false;
    }
    
    public boolean containsNodes(DoublyNode<V> ... nodes) {
        DoublyNode<V> node = head;
        ArrayList<DoublyNode<V>> l = new ArrayList<DoublyNode<V>>(nodes.length);
        for (DoublyNode<V> n : nodes) {
            l.add(n);
        }
        while (node != null) {
            ListIterator<DoublyNode<V>> iter = l.listIterator();
            while (iter.hasNext()) {
                DoublyNode<V> n = iter.next();
                if (n == node) {
                    iter.remove();
                    if (l.isEmpty()) {
                        return true;
                    } else {
                        continue;
                    }
                }
            }
            node = node.getNext();
        }
        return false;
    }
    
    public void concatSequence(DoublyLinkedList<V> seq) {
        DoublyNode<V> node = seq.getHead();
        while (node != null) {
            add(node.getData());
            node = node.getNext();
        }
    }
    
    public DoublyLinkedList<V> getSubList(int startIndex, int length) {
        if (startIndex >= 0 && startIndex + length <= size) {
            DoublyLinkedList<V> seq = new DoublyLinkedList<V>();
            DoublyNode<V> n = getNode(startIndex);
            seq.add(n.getData());
            int c = 1;
            while (c < length) {
                n = n.getNext();
                seq.add(n.getData());
                c++;
            }
            return seq;
        } else {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    public DoublyLinkedList<V> subSequence(DoublyNode<V> startNode, DoublyNode<V> endNode) {
        if (containsNodes(startNode, endNode)) {
            DoublyLinkedList<V> seq = new DoublyLinkedList<V>();
            if (startNode == endNode) {
                seq.add(startNode.getData());
            } else {
                DoublyNode<V> n = startNode;
                while (n != endNode) {
                    if (n != null) {
                        seq.add(n.getData());
                        n = n.getNext();
                    } else {
                        return null;
                    }
                }
                seq.add(endNode.getData());
            }
            return seq;
        } else {
            return null;
        }
    }
    
    @Override
    public DoublyLinkedList<V> clone() {
        DoublyLinkedList<V> seq = new DoublyLinkedList<V>();
        seq.addAll(this);
        return seq;
    }
    
    public void removeData(V data) {
        if (!isEmpty()) {
            DoublyNode<V> node = head;
            while (node != null) {
                if (node.getData().equals(data)) {
                    removeNode(node);
                    break;
                }
                node = node.getNext();
            }
        }
    }

    @Override
    public Iterator<V> iterator() {
        return new DoublyIterator();
    }

    @Override
    public void add(int i, V data) {
        insertNode(i, data);
    }
    
    private HeadTailContainer<V> createNodeList(List<V> list) {
        DoublyNode<V> h = null, n = null;
        for (V data : list) {
            DoublyNode<V> c = createNode(data);
            if (h == null) {
                h = n = c;
            } else {
                c.setPrevious(n);
                n = c;
            }
        }
        return new HeadTailContainer<V>(h, n);
    }
    
    private HeadTailContainer<V> createNodeList(V ... datas) {
        DoublyNode<V> h = null, n = null;
        for (V data : datas) {
            DoublyNode<V> c = createNode(data);
            if (h == null) {
                h = n = c;
            } else {
                c.setPrevious(n);
                n = c;
            }
        }
        return new HeadTailContainer<V>(h, n);
    }
    
    private HeadTailContainer<V> createNodeList(Collection<? extends V> datas) {
        DoublyNode<V> h = null, n = null;
        Iterator<? extends V> iter = datas.iterator();
        while (iter.hasNext()) {
            V data = iter.next();
            DoublyNode<V> c = createNode(data);
            if (h == null) {
                h = n = c;
            } else {
                c.setPrevious(n);
                n = c;
            }
        }
        return new HeadTailContainer<V>(h, n);
    }
    
    @SuppressWarnings("rawtypes")
    public static DoublyNode traceTail(DoublyNode n) {
        if (n == null) {
            return null;
        } else {
            while (n.hasNext()) {
                n = n.getNext();
            }
            return n;
        }
    }
    
    @SuppressWarnings("rawtypes")
    public static DoublyNode traceHead(DoublyNode n) {
        if (n == null) {
            return null;
        } else {
            while (n.hasPrevious()) {
                n = n.getPrevious();
            }
            return n;
        }
    }

    @Override
    public boolean addAll(Collection<? extends V> datas) {
        if (datas.size() == 0) {
            return true;
        }
        HeadTailContainer<V> container = createNodeList(datas);
        if (isEmpty()) {
            head = container.getHead();
            tail = container.getTail();
            size = datas.size();
        } else {
            tail.setNext(container.getHead());
            tail = container.getTail();
            size += datas.size();
        }
        return true;
    }
    
    public boolean addAllToHead(Collection<? extends V> datas) {
        if (datas.size() == 0) {
            return true;
        }
        HeadTailContainer<V> container = createNodeList(datas);
        if (isEmpty()) {
            head = container.getHead();
            tail = container.getTail();
            size = datas.size();
        } else {
            head.setPrevious(container.getTail());
            head = container.getHead();
            size += datas.size();
        }
        return true;
    }

    @Override
    public boolean addAll(int i, Collection<? extends V> datas) {
        if (i < 0 || i > size) {
            return false;
        } else {
            if (datas.size() == 0) {
                return true;
            }
            if (i == size) {
                addAll(datas);
            } else if (i == 0) {
                addAllToHead(datas);
            } else {
                HeadTailContainer<V> container = createNodeList(datas);
                DoublyNode<V> node = getNode(i);
                node.getPrevious().setNext(container.getHead());
                node.setPrevious(container.getTail());
                size += datas.size();
            }
            return true;
        }
    }

    @Override
    public boolean contains(Object data) {
        DoublyNode<V> node = head;
        while (node != null) {
            if (data.equals(node.getData())) {
                return true;
            }
            node = node.getNext();
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> datas) {
        for (Object obj : datas) {
            if (!contains(obj)) {
                return false;
            }
        }
        return true;
    }

    public V get(int i) {
        return this.getNodeData(i);
    }

    @Override
    public int indexOf(Object n) {
        if (isEmpty()) {
            return -1;
        } else {
            DoublyNode<V> node = head;
            int index = 0;
            while (node != null) {
                if (n.equals(node.getData())) {
                    return index;
                }
                node = node.getNext();
                index++;
            }
            return -1;
        }
    }

    @Override
    public int lastIndexOf(Object n) {
        if (isEmpty()) {
            return -1;
        } else {
            DoublyNode<V> node = tail;
            int index = size - 1;
            while (node != null) {
                if (n.equals(node.getData())) {
                    return index;
                }
                node = node.getPrevious();
                index--;
            }
            return -1;
        }
    }

    @Override
    public ListIterator<V> listIterator() {
        return new DoublyIterator();
    }

    @Override
    public ListIterator<V> listIterator(int i) {
        return new DoublyIterator(i);
    }

    @Override
    public boolean remove(Object obj) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        } else {
            DoublyNode<V> node = head;
            while (node != null) {
                if (obj.equals(node.getData())) {
                    return removeNode(node) != null;
                }
                node = node.getNext();
            }
            throw new NoSuchElementException();
        }
    }

    @Override
    public V remove(int i) {
        DoublyNode<V> n = removeNode(i);
        return (n != null)? n.getData() : null;
    }

    @Override
    public boolean removeAll(Collection<?> datas) {
        for (Object o : datas) {
            remove(o);
        }
        return true;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean retainAll(Collection<?> datas) {
        if (isEmpty()) {
            return true;
        } else {
            DoublyNode<V> node = head;
            while (node != null) {
                Iterator iter = datas.iterator();
                boolean canRemove = true;
                while (iter.hasNext()) {
                    Object o = iter.next();
                    if (o.equals(node.getData())) {
                        canRemove = false;
                        break;
                    }
                }
                if (canRemove) {
                    if (node.hasPrevious()) {
                        node.getPrevious().setNext(node.getNext());
                    } else {
                        head = node.getNext();
                    }
                    if (node.hasNext()) {
                        node.getNext().setPrevious(node.getPrevious());
                    } else {
                        tail = node.getPrevious();
                    }
                    DoublyNode<V> n = node;
                    node = node.getNext();
                    n.setPrevious(null);
                    n.setNext(null);
                    size--;
                } else {
                    node = node.getNext();
                }
            }
            return true;
        }
    }

    @Override
    public V set(int i, V data) {
        DoublyNode<V> node = getNode(i);
        V pData = null;
        if (node != null) {
            pData = node.getData();
            node.setData(data);
        }
        return pData;
    }

    public int size() {
        return getSize();
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int i = 0;
        DoublyNode<V> node = head;
        while (node != null) {
            arr[i] = node.getData();
            node = node.getNext();
            i++;
        }
        return arr;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] arr) {
        int i = 0;
        DoublyNode<V> node = head;
        while (i < arr.length && node != null) {
            try {
                arr[i] = (T)node.getData();
                i++;
            } catch (ClassCastException cce) {
                throw new ArrayStoreException();
            }
        }
        return arr;
    }

    @Override
    public boolean add(V data) {
        DoublyNode<V> n = add(createNode(data));
        return n != null;
    }
    
    private class DoublyIterator implements ListIterator<V> {
        private DoublyNode<V> currentNode, nextNode;
        private int nextIndex;
        
        public DoublyIterator() {
            currentNode = null;
            nextIndex = 0;
            nextNode = head;
        }
        
        public DoublyIterator(int i) {
            currentNode = null;
            nextIndex = i;
            nextNode = getNode(i);
        }

        public void add(V data) {
            if (nextIndex < 1) {
                currentNode = addToHead(data);
                nextIndex++;
            } else {
                currentNode = insertNode(nextIndex, data);
                nextIndex++;
            }
        }

        public boolean hasNext() {
            return nextIndex != size;
        }

        public boolean hasPrevious() {
            return nextIndex != 0;
        }

        public V next() {
            if (hasNext()) {
                currentNode = nextNode;
                nextNode = nextNode.getNext();
                nextIndex++;
                return currentNode.getData();
            } else {
                throw new NoSuchElementException("No next element!");
            }
        }

        public int nextIndex() {
            return nextIndex;
        }

        public V previous() {
            if (hasPrevious()) {
                if (nextNode == null) {
                    nextNode = currentNode;
                } else {
                    currentNode = nextNode = nextNode.getPrevious();
                }
                nextIndex--;
                return currentNode.getData();
            } else {
                throw new NoSuchElementException("No previous element!");
            }
        }

        public int previousIndex() {
            return nextIndex - 1;
        }

        public void remove() {
            DoublyNode<V> lastNext = currentNode.getNext();
            try {
                DoublyLinkedList.this.remove(currentNode.getData());
            } catch (NoSuchElementException e) {
                throw new IllegalStateException();
            }
            if (nextNode == currentNode) {
                nextNode = lastNext;
            } else {
                nextIndex--;
                currentNode = head;
            }
        }

        public void set(V data) {
            if (currentNode != null) {
                currentNode.setData(data);
            } else {
                throw new IllegalStateException("Nothing to set!");
            }
        }
    }
    
    public static class HeadTailContainer<V> {
        private DoublyNode<V> head, tail;
        
        public HeadTailContainer() {
            head = tail = null;
        }
        
        public HeadTailContainer(DoublyNode<V> n) {
            head = tail = n;
        }
        
        public HeadTailContainer(DoublyNode<V> h, DoublyNode<V> t) {
            head = h;
            tail = t;
        }
        
        public void setHead(DoublyNode<V> node) {
            this.head = node;
        }
        
        public void setTail(DoublyNode<V> node) {
            this.tail = node;
        }
        
        public DoublyNode<V> getHead() {
            return head;
        }
        
        public DoublyNode<V> getTail() {
            return tail;
        }
    }
}
