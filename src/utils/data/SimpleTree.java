package utils.data;

import java.util.ArrayList;
import java.util.List;

public class SimpleTree<V> implements AbstractTree<V> {
    private SimpleTreeNode<V> rootNode;
    
    /**
     *  Create a simple tree with null valued root node
     */
    public SimpleTree() {
        rootNode = new SimpleTreeNode<V>(null);
    }
    
    /**
     * Create a simple tree with specified root node
     * 
     * @param node root node
     */
    public SimpleTree(SimpleTreeNode<V> node) {
        rootNode = node;
    }
    
    /**
     * Create a search tree with specified root data
     * 
     * @param rootData data of the root node
     */
    public SimpleTree(V rootData) {
        rootNode = new SimpleTreeNode<V>(rootData);
    }
    
    @Override
    public void clear() {
        rootNode = new SimpleTreeNode<V>(null);
    }

    @Override
    public SimpleTreeNode<V> findTreeNode(V data) {
        if (rootNode == null) {
            return null;
        } else {
            return findTreeNode(rootNode, data);
        }
    }
    
    /**
     * Node locator helper method
     * 
     * @param node current node
     * @param data data to be found
     * @return node with specified data (null if not found)
     */
    private SimpleTreeNode<V> findTreeNode(SimpleTreeNode<V> node, V data) {
        if (node.getData() == data) {
            return node;
        } else {
            if (node.hasChild()) {
                SimpleTreeNode<V> resultNode = null;
                for (SimpleTreeNode<V> n : node.getChildNodes()) {
                    resultNode = findTreeNode(n, data);
                    if (resultNode != null) {
                        break;
                    }
                }
                return resultNode;
            } else {
                return null;
            }
        }
    }

    /**
     * Get all leaf nodes
     * 
     * @return list of leaf nodes
     */
    @Override
    public List<SimpleTreeNode<V>> getAllLeafNode() {
        return getLeaf(rootNode);
    }
    
    /**
     * Get all leaf nodes of the specified node
     * 
     * @param node starting node
     * @return list of leaf nodes of specified node
     */
    private List<SimpleTreeNode<V>> getLeaf(SimpleTreeNode<V> node) {
        ArrayList<SimpleTreeNode<V>> list = new ArrayList<SimpleTreeNode<V>>();
        if (node.isLeaf()) {
            list.add(node);
        } else {
            for (SimpleTreeNode<V> n : node.getChildNodes()) {
                list.addAll(getLeaf(n));
            }
        }
        return list;
    }

    @Override
    public V getRootData() {
        if (rootNode != null) {
            return rootNode.getData();
        } else {
            return null;
        }
    }

    @Override
    public SimpleTreeNode<V> getRootNode() {
        return rootNode;
    }

    @Override
    public int getTreeDepth() {
        return findDepth(rootNode, 1);
    }
    
    /**
     * Level determine helper method
     * 
     * @param node current node
     * @param currentLevel current determined level
     * @return total level of this tree 
     */
    private int findDepth(SimpleTreeNode<V> node, int currentLevel) {
        if (node.isLeaf()) {
            return currentLevel;
        } else {
            int maxLevel = currentLevel;
            for (SimpleTreeNode<V> n : node.getChildNodes()) {
                int level = findDepth(n, currentLevel+1);
                if (level > maxLevel) {
                    maxLevel = level;
                }
            }
            return maxLevel;
        }
    }
}
