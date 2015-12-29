package utils.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

import utils.math.MathUtil;

public class RandomObjectGenerator<T> {
    private ArrayList<T> objectList;
    
    public RandomObjectGenerator(Collection<? extends T> distinctObjs) {
        objectList = new ArrayList<T>(distinctObjs);
    }
    
    public void setDistinctObjects(Collection<? extends T> distinctObjectList) {
        this.objectList = new ArrayList<T>(distinctObjectList);
    }
    
    public static <T> List<T> getRandomObjects(int size, boolean unique,
            Collection<? extends T> distinctObjs) {
        ArrayList<T> objectList = new ArrayList<T>(distinctObjs);
        if (unique) {
            if (size > objectList.size()) {
                throw new RuntimeException("distinct object size is smaller than target size, uniqueness is impossible!");
            } else {
                if (size == objectList.size()) {
                    shuffleObjectList(objectList);
                    return new ArrayList<T>(objectList);                    
                } else {
                    shuffleObjectList(objectList, size);
                    ArrayList<T> resultList = new ArrayList<T>();
                    for (int i = 0; i < size; i++) {
                        resultList.add(objectList.get(i));
                    }
                    return resultList;
                }
            }
        } else {
            ArrayList<T> resultList = new ArrayList<T>();
            
            for (int i = 0; i < size; i++) {
                resultList.add(objectList.get(MathUtil.randomInteger(0, objectList.size()-1)));
            }
            
            return resultList;
        }
    }
    
    public List<T> getRandomObjects(int size, boolean unique) {
        if (unique) {
            if (size > objectList.size()) {
                throw new RuntimeException("distinct object size is smaller than target size, uniqueness is impossible!");
            } else {
                if (size == objectList.size()) {
                    shuffleObjectList();
                    return new ArrayList<T>(objectList);                    
                } else {
                    shuffleObjectList(size);
                    ArrayList<T> resultList = new ArrayList<T>();
                    for (int i = 0; i < size; i++) {
                        resultList.add(objectList.get(i));
                    }
                    return resultList;
                }
            }
        } else {
            ArrayList<T> resultList = new ArrayList<T>();
            
            for (int i = 0; i < size; i++) {
                resultList.add(objectList.get(MathUtil.randomInteger(0, objectList.size()-1)));
            }
            
            return resultList;
        }
    }
    
    private void shuffleObjectList() {
        shuffleObjectList(objectList.size());
    }
    
    private void shuffleObjectList(int size) {
        if (size > objectList.size()) {
            size = objectList.size();
        }
        for (int i = 0; i < size; i++) {
            int k = MathUtil.randomInteger(i, objectList.size()-1);
            DataManipulator.swapData(objectList, i, k);
        }
    }
    
    public static <T> void shuffleObjectList(List<T> list) {
        shuffleObjectList(list, list.size());
    }
    
    @SuppressWarnings("unchecked")
    public static <T> void shuffleObjectList(List<T> list, int size) {
        if (size > list.size()) {
            size = list.size();
        }
        if (list instanceof RandomAccess) {
            for (int i = 0; i < size; i++) {
                int k = MathUtil.randomInteger(i, list.size()-1);
                DataManipulator.swapData(list, i, k);
            }
        } else {
            Object[] objs = list.toArray();
            for (int i = 0; i < size; i++) {
                int k = MathUtil.randomInteger(i, objs.length-1);
                DataManipulator.swapData(objs, i, k);
            }
            
            ListIterator<T> iter = list.listIterator();
            for (int i = 0; i < objs.length; i++) {
                iter.next();
                iter.set((T)objs[i]);
            }
        }
    }
}
