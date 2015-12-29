package utils.data;

import java.util.HashMap;
import java.util.Map;

public class BinaryTreeNodeBuilder<E> {
    private BinaryTreeNode<E> currentNode;
    private BinaryTreeNode<E> root;
    
    private Map<String, BinaryTreeNode<E>> branchMap;
    
    public BinaryTreeNodeBuilder() {
        branchMap = new HashMap<String, BinaryTreeNode<E>>();
    }
    
    public BinaryTreeNodeBuilder(E e) {
        this();
        begin(e);
    }
    
    public BinaryTreeNodeBuilder<E> begin(E e) {
        root = currentNode = new BinaryTreeNode<E>(e);
        
        return this;
    }
    
    public BinaryTreeNodeBuilder<E> parent() {
        currentNode = currentNode.getParent();
        return this;
    }
    
    public BinaryTreeNodeBuilder<E> left() {
        currentNode = currentNode.getLeft();
        
        return this;
    }
    
    public BinaryTreeNodeBuilder<E> right() {
        currentNode = currentNode.getRight();
        
        return this;
    }
    
    public BinaryTreeNodeBuilder<E> parent(E e, BinaryTreeNode.Type type) {
        BinaryTreeNode<E> parent = new BinaryTreeNode<E>(e);
        currentNode.appendToParent(parent, type);
        currentNode = parent;
        return this;
    }
    
    public BinaryTreeNodeBuilder<E> left(E e) {
        BinaryTreeNode<E> left = new BinaryTreeNode<E>(e);
        
        currentNode.setLeft(left, true);
        
        currentNode = left;
        
        return this;
    }
    
    public BinaryTreeNodeBuilder<E> right(E e) {
        BinaryTreeNode<E> right = new BinaryTreeNode<E>(e);
        
        currentNode.setRight(right, true);
        currentNode = right;
        
        return this;
    }
    
    public BinaryTreeNodeBuilder<E> root() {
        currentNode = root;
        
        return this;
    }
    
    public BinaryTreeNodeBuilder<E> branch(String branchName) {
        branchMap.put(branchName, currentNode);
        
        return this;
    }
    
    public BinaryTreeNodeBuilder<E> branchRoot(String branchName) {
        if (branchMap.containsKey(branchName)) {
            currentNode = branchMap.get(branchName);
            
            return this;
        } else {
            return this;
        }
    }
    
    public BinaryTreeNodeBuilder<E> removeBranchRecord(String branchName) {
        branchMap.remove(branchName);
        
        return this;
    }
    
    public BinaryTreeNode<E> getRootNode() {
        return root;
    }
    
    public BinaryTreeNodeBuilder<E> clear() {
        currentNode = root = null;
        branchMap.clear();
        return this;
    }
}
