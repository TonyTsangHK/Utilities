package utils.data;

import java.util.ArrayList;

public class TwoDimensionArrayList<T> {
    private ArrayList<ArrayList<T>> elementRows;
    private int rows, cols; 
    
    public TwoDimensionArrayList(int r, int c) {
        elementRows = new ArrayList<ArrayList<T>>(rows);
        rows = (r > 0)? r : 0;
        cols = (c > 0)? c : 0;
        for (int i = 0; i < rows; i++) {
            ArrayList<T> l = new ArrayList<T>(cols);
            for (int j = 0; j < cols; j++) {
                l.add(null);
            }
            elementRows.add(l);
        }
    }
    
    public void grow(int r, int c) {
        growColumns(c);
        growRows(r);
    }
    
    public void growRows(int r) {
        if (r > 0) {
            for (int i = 0; i < r; i++) {
                ArrayList<T> l = new ArrayList<T>();
                for (int j = 0; j < cols; j++) {
                    l.add(null);
                }
                elementRows.add(l);
            }
            rows += r;
        }
    }
    
    public void growColumns(int c) {
        if (c > 0) {
            for (int i = 0; i < rows; i++) {
                ArrayList<T> l = elementRows.get(i);
                for (int j = 0; j < cols; j++) {
                    l.add(null);
                }
            }
            cols += c;
        }
    }
    
    public T getElement(int r, int c) {
        if (r < rows && c < cols) {
            return elementRows.get(r).get(c);
        } else {
            return null;
        }
    }
    
    public T getElement(DimensionIndex i) {
        return getElement(i.getRow(), i.getColumn());
    }
    
    public void setElement(int r, int c, T element) {
        if (r < rows && c < cols) {
            elementRows.get(r).set(c, element);
        }
    }
    
    public void setElement(int r, int rSpan, int c, int cSpan, T element) {
        int lastR = (rSpan > 1)? (r + (rSpan - 1)) : r;
        int lastC = (cSpan > 1)? (c + (cSpan - 1)) : c;
        if (lastR >= rows) {
            lastR = rows - 1;
        }
        if (lastC >= cols) {
            lastC = cols - 1;
        }
        if (r < rows && c < cols) {
            for (int i = r; i <= lastR; i++) {
                ArrayList<T> row = elementRows.get(i);
                for (int j = c; j <= lastC; j++) {
                    row.set(j, element);
                }
            }
        }
    }
    
    public void setElement(DimensionIndex i, T element) {
        setElement(i.getRow(), i.getColumn(), element);
    }
    
    public void setElement(DimensionIndex i, int rSpan, int cSpan, T element) {
        setElement(i.getRow(), i.getColumn(), rSpan, cSpan, element);
    }
    
    public int getRowCount() {
        return rows;
    }
    
    public int getColumnCount() {
        return cols;
    }
    
    public void insertRow(int i) {
        ArrayList<T> l = new ArrayList<T>(cols);
        for (int j = 0; j < cols; j++) {
            l.add(null);
        }
        if (i >= 0 && i < rows) {
            elementRows.add(i, l);
        } else if (i >= rows) {
            elementRows.add(l);
        } else {
            elementRows.add(0, l);
        }
        rows++;
    }
    
    public void insertColumn(int i) {
        for (int j = 0; j < rows; j++) {
            ArrayList<T> row = elementRows.get(j);
            if (i >= 0 && i < cols) {
                row.add(i, null);
            } else if (i >= cols) {
                row.add(null);
            } else {
                row.add(0, null);
            }
        }
        cols++;
    }
    
    public void removeRow(int i) {
        if (i >= 0 && i < rows) {
            elementRows.remove(i);
            rows--;
        }
    }
    
    public void removeColumn(int i) {
        if (i >= 0 && i < cols) {
            for (int j = 0; j < rows; j++) {
                elementRows.get(j).remove(i);
            }
            cols--;
        }
    }
    
    public DimensionIndex indexOf(T element) {
        for (int i = 0; i < rows; i++) {
            ArrayList<T> row = elementRows.get(i);
            int index = row.indexOf(element);
            if (index > -1) {
                return new DimensionIndex(i, index);
            }
        }
        return new DimensionIndex(-1, -1);
    }
    
    public int rowIndexOf(T element) {
        for (int i = 0; i < rows; i++) {
            ArrayList<T> row = elementRows.get(i);
            if (row.contains(element)) {
                return i;
            }
        }
        return -1;
    }
    
    public int columnIndexOf(T element) {
        for (int i = 0; i < rows; i++) {
            ArrayList<T> row = elementRows.get(i);
            int index = row.indexOf(element);
            if (index > -1) {
                return index;
            }
        }
        return -1;
    }
    
    public static class DimensionIndex {
        private int row, column;
        
        public DimensionIndex(int r, int c) {
            row = r;
            column = c;
        }
        
        public int getRow() {
            return row;
        }
        
        public int getColumn() {
            return column;
        }
        
        public String toString() {
            return "row: " + row + ", column: " + column;
        }
    }
}