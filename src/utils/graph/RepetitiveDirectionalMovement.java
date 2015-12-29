package utils.graph;

import java.util.Iterator;

public class RepetitiveDirectionalMovement implements DirectionalMovement {
    private DirectionalMovementGroup directionalMovementGroup;
    private int repeatTimes;
    
    public RepetitiveDirectionalMovement() {
        directionalMovementGroup = new DirectionalMovementGroup();
    }
    
    public void setDirectionalMovementGroup(DirectionalMovementGroup directionalMovementGroup) {
        this.directionalMovementGroup = directionalMovementGroup;
    }
    
    public DirectionalMovementGroup getDirectionalMovementGroup() {
        return directionalMovementGroup;
    }
    
    public void setRepeatTimes(int repeatTimes) {
        this.repeatTimes = repeatTimes;
    }
    
    public int getRepeatTimes() {
        return repeatTimes;
    }
    
    @Override
    public RepetitiveDirectionalMovement flip(Direction ... flipDirections) {
        RepetitiveDirectionalMovement flip = new RepetitiveDirectionalMovement();
        flip.directionalMovementGroup = directionalMovementGroup.flip(flipDirections);
        flip.repeatTimes = repeatTimes;
        return flip;
    }
    
    @Override
    public RepetitiveDirectionalMovement reverse() {
        RepetitiveDirectionalMovement reverse = new RepetitiveDirectionalMovement();
        reverse.directionalMovementGroup = directionalMovementGroup.reverse();
        reverse.repeatTimes = repeatTimes;
        return reverse;
    }
    
    @Override
    public RepetitiveDirectionalMovement clone() {
        RepetitiveDirectionalMovement clone = new RepetitiveDirectionalMovement();
        clone.directionalMovementGroup = directionalMovementGroup.clone();
        clone.repeatTimes = repeatTimes;
        return clone;
    }
    
    @Override
    public RepetitiveDirectionalMovement scale(double factor) {
        RepetitiveDirectionalMovement clone = new RepetitiveDirectionalMovement();
        clone.directionalMovementGroup = directionalMovementGroup.scale(factor);
        clone.repeatTimes = repeatTimes;
        return clone;
    }
    
    @Override
    public DirectionalMovement shortestMovement() {
        return directionalMovementGroup.shortestMovement().scale(repeatTimes);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof RepetitiveDirectionalMovement) {
            RepetitiveDirectionalMovement rdm = (RepetitiveDirectionalMovement) o;
            
            return directionalMovementGroup.equals(rdm.directionalMovementGroup) &&
                repeatTimes == rdm.repeatTimes;
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return directionalMovementGroup.hashCode() * 31 + repeatTimes;
    }
    
    @Override
    public Iterator<SingleDirectionalMovement> movementIterator() {
        return new MovementIterator();
    }
    
    private class MovementIterator implements Iterator<SingleDirectionalMovement> {
        private Iterator<SingleDirectionalMovement> currentIterator;
        private int currentTime = 1;
        
        private MovementIterator() {
            currentIterator = directionalMovementGroup.movementIterator();
        }
        
        private void ensureCurrentIterator() {
            if (currentIterator != null && !currentIterator.hasNext()) {
                if (currentTime < repeatTimes) {
                    currentTime++;
                    currentIterator = directionalMovementGroup.movementIterator();
                } else {
                    currentIterator = null;
                }
            }
        }
        
        @Override
        public boolean hasNext() {
            ensureCurrentIterator();
            return currentIterator != null && currentIterator.hasNext();
        }

        @Override
        public SingleDirectionalMovement next() {
            ensureCurrentIterator();
            if (currentIterator != null && currentIterator.hasNext()) {
                return currentIterator.next();
            } else {
                return null;
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported!");
        }    
    }
}
