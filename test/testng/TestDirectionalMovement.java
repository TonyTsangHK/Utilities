package testng;

import static org.testng.Assert.*;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.graph.Direction;
import utils.graph.DirectionalMovement;
import utils.graph.DirectionalMovementGroup;
import utils.graph.RepetitiveDirectionalMovement;
import utils.graph.SingleDirectionalMovement;
import utils.math.MathUtil;

public class TestDirectionalMovement {
    private int testSize = 10;
    
    private SingleDirectionalMovement sdm;
    private RepetitiveDirectionalMovement rdm;
    private DirectionalMovementGroup mg;
    
    @BeforeMethod
    public void setUp() {
        sdm = new SingleDirectionalMovement(Direction.N, 5);
    
        SingleDirectionalMovement[] sdms = new SingleDirectionalMovement[testSize];
        for (int i = 0; i < testSize; i++) {
            switch (MathUtil.randomInteger(1, 4)) {
                case 1:
                    sdms[i] = new SingleDirectionalMovement(Direction.N, MathUtil.randomNumber(1, 10));
                    break;
                case 2:
                    sdms[i] = new SingleDirectionalMovement(Direction.S, MathUtil.randomNumber(1, 10));
                    break;
                case 3:
                    sdms[i] = new SingleDirectionalMovement(Direction.W, MathUtil.randomNumber(1, 10));
                    break;
                case 4:
                    sdms[i] = new SingleDirectionalMovement(Direction.E, MathUtil.randomNumber(1, 10));
                    break;
            }
        }
        
        mg = new DirectionalMovementGroup();
        mg.addDirectionalMovements(sdms);
        
        rdm = new RepetitiveDirectionalMovement();
        rdm.setDirectionalMovementGroup(mg);
        rdm.setRepeatTimes(MathUtil.randomInteger(2, 10));
    }
    
    @Test
    public void testClone() {
        SingleDirectionalMovement sdmClone = sdm.clone();
        
        DirectionalMovementGroup mgClone = mg.clone();
        
        RepetitiveDirectionalMovement rdmClone = rdm.clone();
        
        assertEquals(sdmClone.getDirection(), sdm.getDirection());
        assertEquals(sdmClone.getUnit(), sdm.getUnit());
        
        Iterator<SingleDirectionalMovement> iter = rdm.movementIterator(),
            cloneIter = rdmClone.movementIterator();
        
        while (iter.hasNext() && cloneIter.hasNext()) {
            SingleDirectionalMovement sdm = iter.next(),
                cloneSdm = cloneIter.next();
            
            assertEquals(sdm.getDirection(), cloneSdm.getDirection());
            assertEquals(sdm.getUnit(), cloneSdm.getUnit());
        }
        
        assertFalse(iter.hasNext());
        assertFalse(cloneIter.hasNext());
        
        iter = mg.movementIterator();
        cloneIter = mgClone.movementIterator();
        
        while (iter.hasNext() && cloneIter.hasNext()) {
            SingleDirectionalMovement sdm = iter.next(),
                cloneSdm = cloneIter.next();
            
            assertEquals(sdm.getDirection(), cloneSdm.getDirection());
            assertEquals(sdm.getUnit(), cloneSdm.getUnit());
        }
        
        assertFalse(iter.hasNext());
        assertFalse(cloneIter.hasNext());
    }
    
    @Test
    public void testReverse() {
        SingleDirectionalMovement sdmReverse = sdm.reverse();
        
        RepetitiveDirectionalMovement rdmReverse = rdm.reverse();
        
        DirectionalMovementGroup mgReverse = mg.reverse();
        
        assertEquals(sdm.getDirection().getBackwardDirection(), sdmReverse.getDirection());
        assertEquals(sdm.getUnit(), sdmReverse.getUnit());
        
        Iterator<SingleDirectionalMovement> iter = rdm.movementIterator(),
        reverseIter = rdmReverse.movementIterator();
        
        List<SingleDirectionalMovement> sdmList = new ArrayList<SingleDirectionalMovement>(),
            reverseSdmList = new ArrayList<SingleDirectionalMovement>();
        
        while (iter.hasNext()) {
            sdmList.add(iter.next());    
        }
        
        while (reverseIter.hasNext()) {
            reverseSdmList.add(reverseIter.next());
        }
        
        assertEquals(sdmList.size(), reverseSdmList.size());
        
        for (int i = 0, j = reverseSdmList.size() - 1; i < sdmList.size() && j >= 0; i++, j--) {
            assertEquals(
                    sdmList.get(i).getDirection(),
                    reverseSdmList.get(j).getDirection().getBackwardDirection()
            );
            assertEquals(
                    sdmList.get(i).getUnit(),
                    reverseSdmList.get(j).getUnit()
            );
        }
        
        sdmList.clear();
        reverseSdmList.clear();
        
        iter = mg.movementIterator();
        reverseIter = mgReverse.movementIterator();
        
        while (iter.hasNext()) {
            sdmList.add(iter.next());
        }
        
        while (reverseIter.hasNext()) {
            reverseSdmList.add(reverseIter.next());
        }
        
        assertEquals(sdmList.size(), reverseSdmList.size());
        
        for (int i = 0, j = reverseSdmList.size() - 1; i < sdmList.size() && j >= 0; i++, j--) {
            assertEquals(
                    sdmList.get(i).getDirection(),
                    reverseSdmList.get(j).getDirection().getBackwardDirection()
            );
            assertEquals(
                    sdmList.get(i).getUnit(),
                    reverseSdmList.get(j).getUnit()
            );
        }
    }
    
    @Test
    public void testFlip() {
        SingleDirectionalMovement sdmFlip = sdm.flip(Direction.N, Direction.S);
        
        RepetitiveDirectionalMovement rdmFlip = rdm.flip(Direction.N, Direction.S);
        
        DirectionalMovementGroup mgFlip = mg.flip(Direction.N, Direction.S);
        
        if (sdm.getDirection() == Direction.N || sdm.getDirection() == Direction.S) {            
            assertEquals(sdm.getDirection().getBackwardDirection(), sdmFlip.getDirection());
        } else {
            assertEquals(sdm.getDirection(), sdmFlip.getDirection());
        }
        assertEquals(sdm.getUnit(), sdmFlip.getUnit());
        
        Iterator<SingleDirectionalMovement> iter = rdm.movementIterator(),
        flipIter = rdmFlip.movementIterator();
        
        while (iter.hasNext() && flipIter.hasNext()) {
            SingleDirectionalMovement sdm = iter.next(),
                flipSdm = flipIter.next();
            
            if (sdm.getDirection() == Direction.N || sdm.getDirection() == Direction.S) {
                assertEquals(sdm.getDirection(), flipSdm.getDirection().getBackwardDirection());
            } else {
                assertEquals(sdm.getDirection(), flipSdm.getDirection());
            }
            assertEquals(sdm.getUnit(), flipSdm.getUnit());
        }
        
        assertFalse(iter.hasNext());
        assertFalse(flipIter.hasNext());
        
        iter = mg.movementIterator();
        flipIter = mgFlip.movementIterator();
        
        while (iter.hasNext() && flipIter.hasNext()) {
            SingleDirectionalMovement sdm = iter.next(),
                flipSdm = flipIter.next();
            
            if (sdm.getDirection() == Direction.N || sdm.getDirection() == Direction.S) {
                assertEquals(sdm.getDirection(), flipSdm.getDirection().getBackwardDirection());
            } else {
                assertEquals(sdm.getDirection(), flipSdm.getDirection());
            }
            assertEquals(sdm.getUnit(), flipSdm.getUnit());
        }
        
        assertFalse(iter.hasNext());
        assertFalse(flipIter.hasNext());
    }
    
    @Test
    public void testScale() {
        double scale = 5;
        SingleDirectionalMovement sdmScale = sdm.scale(scale);
        
        DirectionalMovementGroup mgScale = mg.scale(scale);
        
        RepetitiveDirectionalMovement rdmScale = rdm.scale(scale);
        
        assertEquals(sdmScale.getDirection(), sdm.getDirection());
        assertEquals(sdmScale.getUnit(), sdm.getUnit()*scale);
        
        Iterator<SingleDirectionalMovement> iter = rdm.movementIterator(),
            scaleIter = rdmScale.movementIterator();
        
        while (iter.hasNext() && scaleIter.hasNext()) {
            SingleDirectionalMovement sdm = iter.next(),
                scaleSdm = scaleIter.next();
            
            assertEquals(sdm.getDirection(), scaleSdm.getDirection());
            assertEquals(sdm.getUnit()*scale, scaleSdm.getUnit());
        }
        
        assertFalse(iter.hasNext());
        assertFalse(scaleIter.hasNext());
        
        iter = mg.movementIterator();
        scaleIter = mgScale.movementIterator();
        
        while (iter.hasNext() && scaleIter.hasNext()) {
            SingleDirectionalMovement sdm = iter.next(),
                scaleSdm = scaleIter.next();
            
            assertEquals(sdm.getDirection(), scaleSdm.getDirection());
            assertEquals(sdm.getUnit()*scale, scaleSdm.getUnit());
        }
        
        assertFalse(iter.hasNext());
        assertFalse(scaleIter.hasNext());
    }
    
    private Point2D runDirectionalMovement(DirectionalMovement movement, Point2D startPoint) {
        return runDirectionalMovement(movement, startPoint, false);
    }
    
    private Point2D runDirectionalMovement(DirectionalMovement movement, Point2D startPoint, boolean debug) {
        double x = startPoint.getX(), y = startPoint.getY();
        
        Iterator<SingleDirectionalMovement> iter = movement.movementIterator();
        
        while (iter.hasNext()) {
            SingleDirectionalMovement sdm = iter.next();
            
            if (debug) {
                System.out.println(sdm.getDirection() + ": " + sdm.getUnit());
            }
            
            Direction direction = sdm.getDirection();
            
            if (direction == Direction.N) {
                y += sdm.getUnit();
            } else if (direction == Direction.S) {
                y -= sdm.getUnit();
            } else if (direction == Direction.W) {
                x -= sdm.getUnit();
            } else if (direction == Direction.E) {
                x += sdm.getUnit();
            }
        }
        
        return new Point2D.Double(x, y);
    }
    
    @Test
    public void testShortestMovement() {
        DirectionalMovement sdmShort = sdm.shortestMovement(), 
            rdmShort = rdm.shortestMovement(), mgShort = mg.shortestMovement();
        
        Point2D startPoint = new Point.Double(0,  0);
        
        Point2D p1 = runDirectionalMovement(sdm, startPoint),
            p2 = runDirectionalMovement(sdmShort, startPoint);
        
        assertTrue(
                MathUtil.isFloatingPointNumberEquals(p1.getX(), p2.getX()) &&
                MathUtil.isFloatingPointNumberEquals(p1.getY(), p2.getY())
        );
        
        p1 = runDirectionalMovement(mg, startPoint);
        p2 = runDirectionalMovement(mgShort, startPoint);
        
        assertTrue(
                MathUtil.isFloatingPointNumberEquals(p1.getX(), p2.getX()) &&
                MathUtil.isFloatingPointNumberEquals(p1.getY(), p2.getY())
        );
        
        p1 = runDirectionalMovement(rdm, startPoint);
        p2 = runDirectionalMovement(rdmShort, startPoint);
        
        assertTrue(
                MathUtil.isFloatingPointNumberEquals(p1.getX(), p2.getX()) &&
                MathUtil.isFloatingPointNumberEquals(p1.getY(), p2.getY())
        );
    }
}
