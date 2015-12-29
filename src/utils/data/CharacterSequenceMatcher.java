package utils.data;

import java.util.ArrayList;
import java.util.List;

public class CharacterSequenceMatcher {
    private boolean ignoreCase = false;
    
    private int size;
    
    private SimpleTree<Character> characterTree;
    
    public CharacterSequenceMatcher() {
        this(false);
    }
    
    public CharacterSequenceMatcher(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
        characterTree = new SimpleTree<Character>();
        size = 0;
    }
    
    public void addWord(String word) {
        if (word != null && !word.equals("")) {
            SimpleTreeNode<Character> node = characterTree.getRootNode();
            for (int i = 0; i < word.length(); i++) {
                Character c = new Character(word.charAt(i));
                boolean found = false;
                for (int j = 0; j < node.getChildNodesCount(); j++) {
                    if (c.equals(node.getChildNodeData(j))) {
                        node = node.getChildNode(j);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    SimpleTreeNode<Character> newChild = new SimpleTreeNode<Character>(c);
                    node.addChildNode(newChild);
                    node = newChild;
                }
            }
            node.addChildNode(new SimpleTreeNode<Character>(null));
            size ++;
        }
    }
    
    public boolean removeWord(String word) {
        if (word == null) {
            return false;
        } else {
            SimpleTreeNode<Character> node = characterTree.getRootNode();
            
            for (int i = 0; i < word.length(); i++) {
                Character c = new Character(word.charAt(i));
                
                boolean found = false;
                for (int j = 0; j < node.getChildNodesCount(); j++) {
                    SimpleTreeNode<Character> childNode = node.getChildNode(j);
                    
                    if (c.equals(childNode.getData())) {
                        node = childNode;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    return false;
                }
            }
            
            boolean removed = false;
            for (int i = 0; i < node.getChildNodesCount(); i++) {
                SimpleTreeNode<Character> childNode = node.getChildNode(i);
                
                if (childNode.getData() == null) {
                    node.removeChildNode(childNode);
                    removed = true;
                    break;
                }
            }
            
            if (removed) {
                size--;
                while (node != null && node != characterTree.getRootNode()) {
                    if (node.getChildNodesCount() == 0) {
                        node.getParentNode().removeChildNode(node);
                        node = node.getParentNode();
                    } else {
                        break;
                    }
                }
            }
            
            return removed;
        }
    }
    
    public void clear() {
        characterTree.clear();
        size = 0;
    }
    
    public List<String> getAllStrings() {
        List<String> results = new ArrayList<String>();
        
        List<SimpleTreeNode<Character>> leaves = characterTree.getAllLeafNode();
        
        SimpleTreeNode<Character> root = characterTree.getRootNode();
        
        for (SimpleTreeNode<Character> leaf : leaves) {
            SimpleTreeNode<Character> node = leaf.getParentNode();
            StringBuilder builder = new StringBuilder();
            while (node != root && node != null) {
                builder.append(node.getData());
                
                node = node.getParentNode();
            }
            
            results.add(builder.reverse().toString());
        }
        
        return results;
    }
    
    public List<String> findPossibleMatch(String prefix) {
        List<String> possibleMatches = new ArrayList<String>();
        
        List<SimpleTreeNode<Character>> nodeList = new ArrayList<SimpleTreeNode<Character>>();
        nodeList.add(characterTree.getRootNode());
        
        for (int i = 0; i < prefix.length(); i++) {
            if (nodeList.isEmpty()) {
                break;
            }
            
            Character c = new Character(prefix.charAt(i));
            List<SimpleTreeNode<Character>> nextNodeList = new ArrayList<SimpleTreeNode<Character>>();
            for (SimpleTreeNode<Character> node : nodeList) {
                for (int j = 0; j < node.getChildNodesCount(); j++) {
                    SimpleTreeNode<Character> childNode = node.getChildNode(j);
                    if (childNode.getData() != null) {
                        Character mc = childNode.getData();
                        if (ignoreCase) {
                            if (Character.toLowerCase(c.charValue()) == Character.toLowerCase(mc.charValue())) {
                                nextNodeList.add(node.getChildNode(j));
                            }
                        } else {
                            if (c.charValue() == mc.charValue()) {
                                nextNodeList.add(node.getChildNode(j));
                            }
                        }
                    }
                }
            }
            nodeList = nextNodeList;
        }
        
        List<SimpleTreeNode<Character>> endNodes = new ArrayList<SimpleTreeNode<Character>>();
        while (true) {
            List<SimpleTreeNode<Character>> nextNodeList = new ArrayList<SimpleTreeNode<Character>>();
            for (SimpleTreeNode<Character> node : nodeList) {
                for (int i = 0; i < node.getChildNodesCount(); i++) {
                    SimpleTreeNode<Character> n = node.getChildNode(i);
                    if (n.getData() == null) {
                        endNodes.add(n);
                    } else {
                        nextNodeList.add(node.getChildNode(i));
                    }
                }
            }
            if (nextNodeList.size() > 0) {
                nodeList = nextNodeList;
            } else {
                break;
            }
        }
        
        for (SimpleTreeNode<Character> node : endNodes) {
            StringBuilder builder = new StringBuilder();
            
            SimpleTreeNode<Character> parentNode = node.getParentNode();
            
            while (parentNode != null && parentNode != characterTree.getRootNode()) {
                builder.append(parentNode.getData());
                
                parentNode = parentNode.getParentNode();
            }
            
            possibleMatches.add(builder.reverse().toString());
        }
        
        return possibleMatches;
    }
    
    public boolean match(String matchWord) {
        if (matchWord == null) {
            return false;
        } else {
            SimpleTreeNode<Character> node = characterTree.getRootNode();
            
            for (int i = 0; i < matchWord.length(); i++) {
                Character c = new Character(matchWord.charAt(i));
                
                boolean found = false;
                for (int j = 0; j < node.getChildNodesCount(); j++) {
                    SimpleTreeNode<Character> childNode = node.getChildNode(j);
                    
                    if (c.equals(childNode.getData())) {
                        node = childNode;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    return false;
                }
            }
            
            for (int i = 0; i < node.getChildNodesCount(); i++) {
                if (node.getChildNodeData(i) == null) {
                    return true;
                }
            }
            return false;
        }
    }
    
    public void setIgnoreCase(boolean ignore) {
        this.ignoreCase = ignore;
    }
    
    public boolean isIgnoreCase() {
        return ignoreCase;
    }
    
    public int size() {
        return size;
    }
}
