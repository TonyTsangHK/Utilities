package utils.data;

import java.util.Collection;
import java.util.LinkedList;

public class CharacterQueue {
    private LinkedList<Character> queue;
    
    public CharacterQueue() {
        queue = new LinkedList<Character>();
    }
    
    public CharacterQueue(String str) {
        this(str.toCharArray());
    }
    
    public CharacterQueue(char[] chars) {
        this();
        for (char c : chars) {
            queue.add(new Character(c));
        }
    }
    
    public CharacterQueue(Collection<Character> chars) {
        this();
        queue.addAll(chars);
    }
    
    public CharacterQueue(Character[] chars) {
        this();
        for (Character c : chars) {
            queue.add(c);
        }
    }
    
    public Character dequeue() {
        if (!queue.isEmpty()) {
            return queue.removeFirst();
        } else {
            return null;
        }
    }
    
    public Character peekFirst() {
        if (queue.isEmpty()) {
            return null;
        } else {
            return queue.getFirst();
        }
    }
    
    public Character peekLast() {
        if (queue.isEmpty()) {
            return null;
        } else {
            return queue.getLast();
        }
    }
    
    public void enqueue(Character c) {
        queue.add(c);
    }
    
    public void enqueue(char c) {
        enqueue(new Character(c));
    }
    
    public Character dequeueLast() {
        if (!queue.isEmpty()) {
            return queue.removeLast();
        } else {
            return null;
        }
    }
    
    public void enqueueFirst(Character c) {
        queue.addFirst(c);
    }
    
    public void enqueueFirst(char c) {
        enqueueFirst(new Character(c));
    }
    
    public Character get(int i) {
        if (i >= 0 && i < queue.size()) {
            return queue.get(i);
        } else {
            return null;
        }
    }
    
    public boolean isEmpty() {
        return queue.isEmpty();
    }
    
    public boolean contains(char c) {
        return queue.contains(new Character(c));
    }
}
