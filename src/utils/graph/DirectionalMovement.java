package utils.graph;

import java.util.Iterator;

public interface DirectionalMovement extends Cloneable {
    public DirectionalMovement flip(Direction ... flipDirections);
    public DirectionalMovement reverse();
    public DirectionalMovement scale(double factor);
    public DirectionalMovement shortestMovement();
    
    public Iterator<SingleDirectionalMovement> movementIterator();
}
