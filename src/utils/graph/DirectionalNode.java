package utils.graph;

import utils.data.SortedListAvl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DirectionalNode<T> implements Cloneable {
    private T data;
    private Map<Direction, DirectionalNode<T>> nodeMap;
    
    public DirectionalNode() {}
    
    public DirectionalNode(T data) {
        nodeMap = new HashMap<Direction, DirectionalNode<T>>();
        this.data = data;
    }
    
    public void setNode(Direction direction, DirectionalNode<T> node) {
        setNode(direction, node, false);
    }
    
    public void setNode(Direction direction, DirectionalNode<T> node, boolean keepDirectionConsistency) {
        if (node == null) {
            clearDirection(direction, keepDirectionConsistency);
        } else {
            nodeMap.put(direction, node);
            if (keepDirectionConsistency && direction.getBackwardDirection() != null) {
                if (node.getNode(direction.getBackwardDirection()) != this) {
                    node.setNode(direction.getBackwardDirection(), this, false);
                }
            }
        }
    }
    
    public void clearDirection(Direction direction) {
        clearDirection(direction, false);
    }
    
    public void clearDirection(Direction direction, boolean keepDirectionConsistency) {
        DirectionalNode<T> n = nodeMap.remove(direction);
        if (n != null && keepDirectionConsistency && direction.getBackwardDirection() != null) {
            n.clearDirection(direction.getBackwardDirection());
        }
    }
    
    public DirectionalNode<T> getNode(Direction direction) {
        return nodeMap.get(direction);
    }
    
    public Direction getDirection(DirectionalNode<T> node) {
        Set<Direction> directions = nodeMap.keySet();
        for (Direction direction : directions) {
            if (nodeMap.get(direction) == node) {
                return direction;
            }
        }
        return null;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public T getData() {
        return data;
    }
    
    public static <T> void clearSameNodePath(
            SortedListAvl<DirectionalNodePath<T>> storedPaths, DirectionalNode<T> targetNode
    ) {
        if (storedPaths.isEmpty()) {
            return;
        }
        int stopSize = storedPaths.getFirst().size();
        for (int i = storedPaths.size() - 1; i >= 0; i--) {
            DirectionalNodePath<T> path = storedPaths.get(i);
            
            if (path.size() > stopSize) {
                if (path.getNextNode() == targetNode) {
                    storedPaths.remove(i);
                }
            } else {
                break;
            }
        }
    }
    
    public boolean hasNode(Direction direction) {
        return nodeMap.containsKey(direction);
    }
    
    public List<Direction> getLinkedDirections() {
        return new ArrayList<Direction>(nodeMap.keySet());
    }
    
    public List<DirectionalNode<T>> findPathToNode(DirectionalNode<T> endNode) {
        return findPathToNode(this, endNode);
    }
    
    public static <T> List<DirectionalNode<T>> findPathToNode(
            DirectionalNode<T> startNode, DirectionalNode<T> endNode
    ) {
        if (startNode == null) {
            return null;
        } else if (startNode == endNode) {
            List<DirectionalNode<T>> path = new ArrayList<DirectionalNode<T>>();
            path.add(startNode);
            return path;
        }
        
        SortedListAvl<DirectionalNodePath<T>> storedPaths = new SortedListAvl<DirectionalNodePath<T>>();
        
        DirectionalNodePath<T> newPath = new DirectionalNodePath<T>(
                new ArrayList<DirectionalNode<T>>(), startNode
        );
        
        storedPaths.add(newPath);
        
        while (storedPaths.size() > 0) {
            DirectionalNodePath<T> firstPath = storedPaths.pollFirst();
            
            if (firstPath.getNextNode() == endNode) {
                return firstPath.toList();
            } else {
                DirectionalNode<T> node = firstPath.getNextNode();
                
                List<DirectionalNode<T>> nodeList = firstPath.toList();
                
                List<Direction> directions = node.getLinkedDirections();
                
                for (Direction direction : directions) {
                    DirectionalNode<T> n = node.getNode(direction);
                    if (n == endNode) {
                        nodeList.add(n);
                        return nodeList;
                    } else if (!nodeList.contains(n)) {
                        clearSameNodePath(storedPaths, n);
                        storedPaths.add(new DirectionalNodePath<T>(nodeList, n));
                    }
                }
            }
        }
        
        return null;
    }
    
    public static <T> DirectionalNode<T> findSpecificDataNode(DirectionalNode<T> startNode, T data) {
        if (startNode == null) {
            return null;
        }
        SortedListAvl<DirectionalNodePath<T>> storedPaths = new SortedListAvl<DirectionalNodePath<T>>();
        
        DirectionalNodePath<T> newPath =
            new DirectionalNodePath<T>(
                new ArrayList<DirectionalNode<T>>(), startNode
            );
        
        storedPaths.add(newPath);
        
        while (storedPaths.size() > 0) {
            DirectionalNodePath<T> firstPath = storedPaths.pollFirst();
            
            if (firstPath.getNextNode().getData() == data) {
                return firstPath.getNextNode();
            } else {
                DirectionalNode<T> node = firstPath.getNextNode();
                
                List<DirectionalNode<T>> nodeList = firstPath.toList();
                
                List<Direction> directions = node.getLinkedDirections();
                
                for (Direction direction : directions) {
                    DirectionalNode<T> n = node.getNode(direction);
                    if (n.getData() == data) {
                        return n;
                    } else if (!nodeList.contains(n)) {
                        clearSameNodePath(storedPaths, n);
                        storedPaths.add(new DirectionalNodePath<T>(nodeList, n));
                    }
                }
            }
        }
        return null;
    }
    
    public static <T> List<DirectionalNode<T>> findPathToSpecificDataNode(
            DirectionalNode<T> startNode, T data
    ) {
        if (startNode == null) {
            return null;
        }
        
        SortedListAvl<DirectionalNodePath<T>> storedPaths = new SortedListAvl<DirectionalNodePath<T>>();
        
        DirectionalNodePath<T> newPath =
            new DirectionalNodePath<T>(
                new ArrayList<DirectionalNode<T>>(), startNode
            );
        
        storedPaths.add(newPath);
        
        while (storedPaths.size() > 0) {
            DirectionalNodePath<T> firstPath = storedPaths.pollFirst();
            
            if (firstPath.getNextNode().getData() == data) {
                return firstPath.toList();
            } else {
                DirectionalNode<T> node = firstPath.getNextNode();
                
                List<DirectionalNode<T>> nodeList = firstPath.toList();
                
                List<Direction> directions = node.getLinkedDirections();
                
                for (Direction direction : directions) {
                    DirectionalNode<T> n = node.getNode(direction);
                    
                    if (n.getData() == data) {
                        nodeList.add(n);
                        return nodeList;
                    } else if (!nodeList.contains(n)) {
                        clearSameNodePath(storedPaths, n);
                        storedPaths.add(new DirectionalNodePath<T>(nodeList, n));
                    }
                }
            }
        }
        return null;
    }
    
    public DirectionalNode<T> findSpecificDataNode(T data) {
        return findSpecificDataNode(this, data);
    }
    
    private void cloneStructure(
            DirectionalNode<T> cloneNode,
            Map<DirectionalNode<T>, DirectionalNode<T>> clonedNodeMap
    ) {
        if (clonedNodeMap == null) {
            clonedNodeMap = new HashMap<DirectionalNode<T>, DirectionalNode<T>>();
            clonedNodeMap.put(this, cloneNode);
        }
        List<Direction> directions = getLinkedDirections();
        
        for (Direction direction : directions) {
            if (cloneNode.getNode(direction) == null) {
                DirectionalNode<T> n = getNode(direction);
                if (clonedNodeMap.containsKey(n)) {
                    DirectionalNode<T> cn = clonedNodeMap.get(n);
                    if (direction.getBackwardDirection() != null) {
                        cn.setNode(direction.getBackwardDirection(), cloneNode);
                    }
                    cloneNode.setNode(direction, cn);
                } else {
                    DirectionalNode<T> cn = new DirectionalNode<T>(n.getData());
                    
                    if (direction.getBackwardDirection() != null) {
                        cn.setNode(direction.getBackwardDirection(), cloneNode);
                    }
                    
                    cloneNode.setNode(direction, cn);
                    clonedNodeMap.put(n, cn);
                    n.cloneStructure(cn, clonedNodeMap);
                }
            }
        }
    }
    
    public DirectionalNode<T> clone(boolean cloneStructure) {
        DirectionalNode<T> clone = new DirectionalNode<T>(data);
        if (cloneStructure) {
            Map<DirectionalNode<T>, DirectionalNode<T>> clonedNodeMap =
                new HashMap<DirectionalNode<T>, DirectionalNode<T>>();
            clonedNodeMap.put(this, clone);
            cloneStructure(clone, clonedNodeMap);
        }
        return clone;
    }
    
    @Override
    public DirectionalNode<T> clone() {
        return clone(true);
    }
}
