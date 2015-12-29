package utils.graph;

import java.util.Iterator;

public class SingleDirectionalMovement implements DirectionalMovement {
    private Direction direction;
    private double unit;
    
    public SingleDirectionalMovement(Direction direction, double unit) {
        setDirection(direction);
        setUnit(unit);
    }
    
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    
    public void setUnit(double unit) {
        this.unit = unit;
    }
    
    public Direction getDirection() {
        return direction;
    }
    
    public double getUnit() {
        return unit;
    }
    
    @Override
    public SingleDirectionalMovement flip(Direction ... flipDirections) {
        for (Direction direction : flipDirections) {
            if (direction == this.direction) {
                return new SingleDirectionalMovement(direction.getBackwardDirection(), unit);
            }
        }
        return clone();
    }
    
    @Override
    public SingleDirectionalMovement reverse() {        
        return new SingleDirectionalMovement(direction.getBackwardDirection(), unit);
    }
    
    @Override
    public SingleDirectionalMovement scale(double factor) {
        return new SingleDirectionalMovement(direction, unit*factor);
    }
    
    @Override
    public SingleDirectionalMovement shortestMovement() {
        return clone();
    }
    
    @Override
    public SingleDirectionalMovement clone() {
        return new SingleDirectionalMovement(direction, unit);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof SingleDirectionalMovement) {
            SingleDirectionalMovement sdm = (SingleDirectionalMovement) o;
            return direction.equals(sdm.direction) && unit == sdm.unit;
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return direction.hashCode() * 31 + (int)unit;
    }
    
    @Override
    public Iterator<SingleDirectionalMovement> movementIterator() {
        return new MovementIterator();
    }
    
    private class MovementIterator implements Iterator<SingleDirectionalMovement> {
        private boolean ended = false;
        
        @Override
        public boolean hasNext() {
            return !ended;
        }
        
        @Override
        public SingleDirectionalMovement next() {
            ended = true;
            return SingleDirectionalMovement.this;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported!");
        }
    }
}
