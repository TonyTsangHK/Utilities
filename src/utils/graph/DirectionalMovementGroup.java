package utils.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DirectionalMovementGroup implements DirectionalMovement, Cloneable {
    private List<DirectionalMovement> directionalMovements;
    
    public DirectionalMovementGroup() {
        directionalMovements = new ArrayList<DirectionalMovement>();
    }
    
    public void addDirectionalMovement(DirectionalMovement movement) {
        directionalMovements.add(movement);
    }
    
    public void addDirectionalMovements(DirectionalMovement ... movements) {
        for (DirectionalMovement movement : movements) {
            addDirectionalMovement(movement);
        }
    }
    
    public void addDirectionalMovements(DirectionalMovementGroup movementGroup) {
        for (DirectionalMovement movement : movementGroup.directionalMovements) {
            addDirectionalMovement(movement);
        }
    }
    
    public void removeDirectionalMovement(DirectionalMovement movement) {
        directionalMovements.remove(movement);
    }
    
    public void removeDirectionalMovements(DirectionalMovement ... movements) {
        for (DirectionalMovement movement : movements) {
            removeDirectionalMovement(movement);
        }
    }
    
    public void removeDirectionalMovements(DirectionalMovementGroup movementGroup) {
        for (DirectionalMovement movement : movementGroup.directionalMovements) {
            removeDirectionalMovement(movement);
        }
    }
    
    public int getDirecitonalMovementCount() {
        return directionalMovements.size();
    }
    
    public DirectionalMovement getDirectionalMovement(int i) {
        if (i >= 0 && i < directionalMovements.size()) {
            return directionalMovements.get(i);
        } else {
            return null;
        }
    }
    
    public void clear() {
        directionalMovements.clear();
    }
    
    @Override
    public DirectionalMovementGroup flip(Direction ... flipDirections) {
        DirectionalMovementGroup fliped = new DirectionalMovementGroup();
        
        Iterator<DirectionalMovement> iter = directionalMovements.iterator();
        while (iter.hasNext()) {
            fliped.addDirectionalMovement(iter.next().flip(flipDirections));
        }
        return fliped;
    }
    
    @Override
    public DirectionalMovementGroup reverse() {
        DirectionalMovementGroup reversed = new DirectionalMovementGroup();
        
        for (int i = directionalMovements.size()-1; i >= 0; i--) {
            reversed.addDirectionalMovement(directionalMovements.get(i).reverse());
        }
        
        return reversed;
    }
    
    @Override
    public DirectionalMovementGroup scale(double factor) {
        DirectionalMovementGroup clone = new DirectionalMovementGroup();
        for (DirectionalMovement movement : directionalMovements) {
            clone.addDirectionalMovement(movement.scale(factor));
        }
        return clone;
    }
    
    @Override
    public DirectionalMovementGroup clone() {
        DirectionalMovementGroup clone = new DirectionalMovementGroup();
        
        clone.directionalMovements.addAll(directionalMovements);
        
        return clone;
    }
    
    @Override
    public DirectionalMovement shortestMovement() {
        Map<Direction, Double> movementMap = new HashMap<Direction, Double>();
        Iterator<SingleDirectionalMovement> movementIter = movementIterator();
        
        while (movementIter.hasNext()) {
            SingleDirectionalMovement movement = movementIter.next();
            if (movementMap.containsKey(movement.getDirection())) {
                movementMap.put(
                        movement.getDirection(),
                        new Double(
                                movementMap.get(movement.getDirection()).doubleValue() + 
                                movement.getUnit()
                        )
                );
            } else if (movementMap.containsKey(movement.getDirection().getBackwardDirection())) {
                double unit = movementMap.get(movement.getDirection().getBackwardDirection());
                if (unit > movement.getUnit()) {
                    movementMap.put(
                            movement.getDirection().getBackwardDirection(), 
                            new Double(unit-movement.getUnit())
                    );
                } else if (unit < movement.getUnit()) {
                    movementMap.remove(movement.getDirection().getBackwardDirection());
                    movementMap.put(
                            movement.getDirection(), new Double(movement.getUnit()-unit)
                    );
                } else {
                    movementMap.remove(movement.getDirection().getBackwardDirection());
                }
            } else {
                movementMap.put(movement.getDirection(), new Double(movement.getUnit()));
            }
        }
        
        if (movementMap.size() == 0) {
            return new SingleDirectionalMovement(Direction.N, 0);
        } else if (movementMap.size() == 1) {
            Map.Entry<Direction, Double> entry = movementMap.entrySet().iterator().next();
            
            return new SingleDirectionalMovement(entry.getKey(), entry.getValue());
        } else {
            Iterator<Map.Entry<Direction, Double>> iter = movementMap.entrySet().iterator();
            
            DirectionalMovementGroup movementGroup = new DirectionalMovementGroup();
            while (iter.hasNext()) {
                Map.Entry<Direction, Double> entry = iter.next();
                
                movementGroup.addDirectionalMovement(
                        new SingleDirectionalMovement(entry.getKey(), entry.getValue())
                );
            }
            
            return movementGroup;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof DirectionalMovementGroup) {
            DirectionalMovementGroup dmg = (DirectionalMovementGroup) o;
            
            if (directionalMovements.size() == dmg.directionalMovements.size()) {
                for (int i = 0; i < directionalMovements.size(); i++) {
                    if (!directionalMovements.get(i).equals(dmg.directionalMovements.get(i))) {
                        return false;
                    }
                }
                
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int hash = 1;
        for (DirectionalMovement movement : directionalMovements) {
            hash = hash * 31 + movement.hashCode();
        }
        return hash;
    }

    @Override
    public Iterator<SingleDirectionalMovement> movementIterator() {
        return new MovementIterator();
    }
    
    private class MovementIterator implements Iterator<SingleDirectionalMovement> {
        private Iterator<SingleDirectionalMovement> currentIterator = null;
        private LinkedList<Iterator<SingleDirectionalMovement>> iterators;
        
        public MovementIterator() {
            iterators = new LinkedList<Iterator<SingleDirectionalMovement>>();
            if (directionalMovements.size() > 0) {
                currentIterator = directionalMovements.get(0).movementIterator();
            }
            for (int i = 1; i < directionalMovements.size(); i++) {
                iterators.add(directionalMovements.get(i).movementIterator());
            }
        }
        
        @Override
        public boolean hasNext() {
            ensureCurrentIterator();
            return currentIterator != null && currentIterator.hasNext();
        }

        private void ensureCurrentIterator() {
            while (currentIterator != null && !currentIterator.hasNext()) {
                if (iterators.size() > 0) {
                    currentIterator = iterators.removeFirst();
                } else {
                    currentIterator = null;
                }
            }
        }
        
        @Override
        public SingleDirectionalMovement next() {
            ensureCurrentIterator();
            if (currentIterator == null) {
                return null;
            } else {
                if (currentIterator.hasNext()) {
                    return currentIterator.next();
                } else {
                    currentIterator = null;
                    return null;
                }
            }
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported!");
        }
    }
}
