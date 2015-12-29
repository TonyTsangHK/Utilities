package utils.graph;

import java.util.ArrayList;
import java.util.List;

public class Direction {
    public static final Direction
        N = new Direction("NORTH"), S = new Direction("SOUTH"),
        W = new Direction("WEST"),  E = new Direction("EAST"),
        NE = new Direction("NORTH_EAST"), SW = new Direction("SOUTH_WEST"),
        NW = new Direction("NORTH_WEST"), SE = new Direction("SOUTH_EAST");
    
    static {
        N.backwardDirection = S;
        S.backwardDirection = N;
        W.backwardDirection = E;
        E.backwardDirection = W;
        
        NE.backwardDirection = SW;
        SW.backwardDirection = NE;
        NW.backwardDirection = SE;
        SE.backwardDirection = NW;
    }
    
    public static List<Direction> createUniDirections(String ... directionDescs) {
        List<Direction> directions = new ArrayList<Direction>(directionDescs.length);
        
        for (String desc : directionDescs) {
            directions.add(new Direction(desc));
        }
        
        return directions;
    }
    
    public static List<Direction> createPairedDirections(String ... directionDescs) {
        int pairCount = directionDescs.length / 2;
        List<Direction> directions = new ArrayList<Direction>(pairCount);
        for (int i = 0; i < pairCount; i++) {
            int si = i*2, ei = si+1;
            String f = directionDescs[si], e = (ei < directionDescs.length)? directionDescs[ei] : null;
            Direction fd = new Direction(f);
            directions.add(fd);
            if (e != null) {
                Direction ed = new Direction(e, fd);
                fd.backwardDirection = ed;
                directions.add(ed);
            }
        }
        return directions;
    }
    
    private String desc;
    
    private Direction backwardDirection;
    
    private Direction() {}
    
    private Direction(String desc) {
        this.desc = desc;
    }
    
    private Direction(String desc, Direction backwardDirection) {
        this.desc = desc;
        this.backwardDirection = backwardDirection;
    }
    
    public Direction getBackwardDirection() {
        return backwardDirection;
    }
    
    @Override
    public String toString() {
        return desc;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof Direction) {
            return this.desc.equals(((Direction)o).desc);
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return desc.hashCode();
    }
}