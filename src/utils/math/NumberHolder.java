package utils.math;

import java.util.*;

public class NumberHolder {
    ArrayList<Double> numbers;
    double sum, avg, max, min;
    
    public NumberHolder() {
        numbers = new ArrayList<Double>();
        reset();
    }
    
    public void addNumber(double n) {
        numbers.add(new Double(n));
        if (max < n) {
            max = n;
        }
        if (min > n) {
            min = n;
        }
        sum += n;
        avg = sum / numbers.size();
    }
    
    public void addNumber(int n) {
        addNumber((double)n);
    }
    
    public void addNumber(float n) {
        addNumber((double)n);
    }
    
    public void addNumber(long n) {
        addNumber((double)n);
    }
    
    public void removeNumber(int i) {
        if (i >= 0 && i < numbers.size()) {
            Double n = numbers.remove(i);
            sum -= n.doubleValue();
            if (numbers.size() > 0) { 
                avg = sum / numbers.size();
            } else {
                avg = 0;
            }
        }
    }
    
    public double getNumber(int i) {
        if (i >= 0 && i < numbers.size()) {
            return numbers.get(i).doubleValue();
        } else {
            return -1;
        }
    }
    
    public double getPercetage(int i) {
        if (i >= 0 && i < numbers.size()) {
            return calPercetage(numbers.get(i).doubleValue());
        } else {
            return -1;
        }
    }
    
    public double calPercetage(double n) {
        return n / sum * 100.0;
    }
    
    public double getValuePercetage(double pert) {
        return sum * pert / 100.0;
    }
    
    public double getMax() {
        return max;
    }
    
    public double getMin() {
        return min;
    }
    
    public double getSum() {
        return sum;
    }
    
    public double getAvg() {
        return avg;
    }
    
    public int size() {
        return numbers.size();
    }
    
    public int getCount() {
        return numbers.size();
    }
    
    public void recal() {
        if (numbers.size() == 0) {
            clearAll();
        } else {
            reset();
            for (Double n : numbers) {
                sum += n.doubleValue();
                if (max < n.doubleValue()) {
                    max = n.doubleValue();
                }
                if (min > n.doubleValue()) {
                    min = n.doubleValue();
                }
            }
            avg = sum / numbers.size();
        }
    }
    
    public void clearAll() {
        numbers.clear();
        reset();
    }
    
    private void reset() {
        max = Double.MIN_VALUE;
        min = Double.MAX_VALUE;
        sum = 0;
        avg = 0;
    }
}