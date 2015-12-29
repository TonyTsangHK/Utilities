package utils.data;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;

import utils.constants.MathValueType;
import utils.date.DateCalendar;
import utils.date.DateTimeParser;

import utils.math.MathUtil;
import utils.string.*;

public class DataManipulator {
	private DataManipulator() {}
	
    public static <E> boolean swapData(E[] array, int f, int t) {
        if (f >= 0 && f < array.length && t >= 0 && t < array.length) {
            E o = array[f];
            array[f] = array[t];
            array[t] = o;
            return true;
        } else {
            return false;
        }
    }
    
    // All swap data method will not check index!
    public static void swapData(boolean[] a, int i, int j) {
    	boolean v = a[i];
    	a[i] = a[j];
    	a[j] = v;
    }
    
    public static void swapData(byte[] a, int i, int j) {
    	byte v = a[i];
    	a[i] = a[j];
    	a[j] = v;
    }
    
    public static void swapData(char[] a, int i, int j) {
    	char v = a[i];
    	a[i] = a[j];
    	a[j] = v;
    }
    
    public static void swapData(short[] a, int i, int j) {
    	short v = a[i];
    	a[i] = a[j];
    	a[j] = v;
    }
    
    public static void swapData(float[] a, int i, int j) {
    	float v = a[i];
    	a[i] = a[j];
    	a[j] = v;
    }
    
    public static void swapData(double[] a, int i, int j) {
    	double v = a[i];
    	a[i] = a[j];
    	a[j] = v;
    }
    
    public static void swapData(int[] a, int i, int j) {
    	int v = a[i];
    	a[i] = a[j];
    	a[j] = v;
    }
    
    public static void swapData(long[] a, int i, int j) {
    	long v = a[i];
    	a[i] = a[j];
    	a[j] = v;
    }
    
    public static <E> boolean swapData(List<E> l, int f, int t) {
        if (f >= 0 && f < l.size() && t >= 0 && t < l.size() && f != t) {
            E o = l.get(f);
            l.set(f, l.get(t));
            l.set(t, o);
            return true;
        } else {
            return false;
        }
    }
    
    public static <E> boolean swapData(List<List<E>> l, int f, int t, int[] coi) {
        if (f >= 0 && f < l.size() && t >= 0 && t < l.size() && f != t) {
            List<E> fl = l.get(f);
            List<E> tl = l.get(t);
            for (int k : coi) {
                E fVal = fl.get(k);
                fl.set(k, tl.get(k));
                tl.set(k, fVal);
            }
            return true;
        } else {
            return false;
        }
    }
    
    public static <E> boolean moveData(List<E> l, int f, int t) {
        if (f >= 0 && f < l.size() && t >= 0 && t < l.size() && f != t) {
            E o = l.get(f);
            if (f < t) {
                for (int i = f; i < t; i++) {
                    l.set(i, l.get(i+1));
                }
            } else {
                for (int i = f; i > t; i--) {
                    l.set(i, l.get(i-1));
                }
            }
            l.set(t, o);
            return true;
        } else {
            return false;
        }
    }
    
    public static <E> boolean swapDatas(List<E> l, int fs, int fe, int es, int ee) {
        if (fs > es) {
            int t = fs;
            fs = es;
            es = t;
            t = fe;
            fe = ee;
            ee = t;
        }
        if (fs >= 0 && fs < l.size() && fe >= 0 && fe < l.size() 
                && es >= 0 && es < l.size() && ee >= 0 && ee < l.size()
                && fe < es && fs <= fe && es <= ee) {
            LinkedList<E> ll = new LinkedList<E>();
            for (int i = es; i <= ee; i++) {
                ll.add(l.get(i));
            }
            for (int i = fe + 1; i < es; i++) {
                ll.add(l.get(i));
            }
            for (int i = fs; i <= fe; i++) {
                ll.add(l.get(i));
            }
            for (int i = fs, k = 0; i <= ee; i++, k++) {
                l.set(i, ll.removeFirst());
            }
            return true;
        } else {
            return false;
        }
    }
    
    public static <E> boolean swapDatas(List<List<E>> l, int fs, int fe, int es, int ee, int[] coi) {
        if (fs > es) {
            int t = fs;
            fs = es;
            es = t;
            t = fe;
            fe = ee;
            ee = t;
        }
        if (fs >= 0 && fs < l.size() && fe >= 0 && fe < l.size() 
                && es >= 0 && es < l.size() && ee >= 0 && ee < l.size()
                && fe < es && fs <= fe && es <= ee) {
            LinkedList<List<E>> ll = new LinkedList<>();
            for (int i = es; i <= ee; i++) {
                ll.add(cloneList(l.get(i)));
            }
            for (int i = fe + 1; i < es; i++) {
                ll.add(cloneList(l.get(i)));
            }
            for (int i = fs; i <= fe; i++) {
                ll.add(cloneList(l.get(i)));
            }
            for (int i = fs; i <= ee; i++) {
                List<E> vals = ll.removeFirst();
                List<E> oVals = l.get(i);
                for (int index : coi) {
                    E tVal = vals.get(index);
                    oVals.set(index, tVal);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public static <E> void moveDatas(List<E> list, int start, int end, int to) {
        if (start != to) {
            int first, last;

            List<E> partition = new ArrayList<E>();

            if (start < to) {
                first = start;
                last = to + end - start;

                if (last >= list.size()) {
                    last = list.size()-1;
                }

                for (int i = end+1; i <= last; i++) {
                    partition.add(list.get(i));
                }

                for (int i = start; i <= end; i++) {
                    partition.add(list.get(i));
                }
            } else {
                first = to;
                last = end;

                for (int i = start; i <= end; i++) {
                    partition.add(list.get(i));
                }

                for (int i = first; i <= start-1; i++) {
                    partition.add(list.get(i));
                }
            }

            for (int i = first, c = 0; i <= last; i++, c++) {
                list.set(i, partition.get(c));
            }
        }
    }

    public static <E> void addDatas(List<E> list, E ... objs) {
        Collections.addAll(list, objs);
    }
    
    public static <E> void addDatas(List<E> list, Collection<E> objs) {
        for (E o : objs) {
            list.add(o);
        }
    }
    
    public static <E> void removeDatas(List<E> list, E ... objs) {
        for (E o : objs) {
            list.remove(o);
        }
    }
    
    public static <E> void removeDatas(List<E> list, Collection<E> dataToBeRemoved) {
        dataToBeRemoved.forEach(list::remove);
    }
    
    public static <E> void removeDatas(List<E> list, int[] indexes) {
        Arrays.sort(indexes);
        for (int i = indexes.length - 1; i >= 0; i--) {
            int index = indexes[i];
            list.remove(index);
        }
    }
    
    public static <E> void reverseList(List<E> list) {
        for (int i = 0, j = list.size() - 1; j > i; i++, j--) {
            E t = list.get(i);
            list.set(i, list.get(j));
            list.set(j, t);
        }
    }
    
    public static boolean within(int[] arr, int i) {
        for (int v : arr) {
            if (i == v) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean within(int[] arr, int i, int s, int e) {
        for (int j = s; j <= e && j < arr.length && j >= 0; j++) {
            if (i == arr[j]) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean within(String[] arr, String v) {
        for (String sv : arr) {
            if (sv.equals(v)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean within(String[] arr, String v, int s, int e) {
        for (int j = s; j <= e && j < arr.length && j >= 0; j++) {
            if (v.equals(arr[j])) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean within(boolean[] arr, boolean i) {
        for (boolean bv : arr) {
            if (i == bv) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean within(boolean[] arr, boolean i, int s, int e) {
        for (int j = s; j <= e && j < arr.length && j >= 0; j++) {
            if (i == arr[j]) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean within(char[] arr, char i) {
        for (char bv : arr) {
            if (i == bv) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean within(char[] arr, char i, int s, int e) {
        for (int j = s; j <= e && j < arr.length && j >= 0; j++) {
            if (i == arr[j]) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean within(short[] arr, short i) {
        for (short anArr : arr) {
            if (i == anArr)
                return true;
        }
        return false;
    }
    
    public static boolean within(short[] arr, short i, int s, int e) {
        for (int j = s; j <= e && j < arr.length && j >= 0; j++) {
            if (i == arr[j]) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean within(long[] arr, long i) {
        for (long anArr : arr) {
            if (i == anArr)
                return true;
        }
        return false;
    }
    
    public static boolean within(long[] arr, long i, int s, int e) {
        for (int j = s; j <= e && j < arr.length && j >= 0; j++) {
            if (i == arr[j]) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean within(byte[] arr, byte i) {
        for (byte anArr : arr) {
            if (i == anArr) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean within(byte[] arr, byte i, int s, int e) {
        for (int j = s; j <= e && j < arr.length && j >= 0; j++) {
            if (i == arr[j]) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean within(double[] arr, double i) {
        for (double anArr : arr) {
            if (i == anArr) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean within(double[] arr, double i, int s, int e) {
        for (int j = s; j <= e && j < arr.length && j >= 0; j++) {
            if (i == arr[j]) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean within(float[] arr, float i) {
        for (float anArr : arr) {
            if (i == anArr) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean within(float[] arr, float i, int s, int e) {
        for (int j = s; j <= e && j < arr.length && j >= 0; j++) {
            if (i == arr[j]) {
                return true;
            }
        }
        return false;
    }

    public static <E, V> Map<E, V> cloneMap(Map<E, V> map) {
        return cloneMap(map, false);
    }

    public static <E, V> Map<E, V> cloneMap(Map<E, V> map, boolean deepClone) {
        if (map != null) {
            Map<E, V> newMap = new HashMap<E, V>(map.size());

            for (Map.Entry<E, V> entry : map.entrySet()) {
                E k = entry.getKey();
                V v = entry.getValue();
                if (deepClone) {
                    if (v instanceof List) {
                        newMap.put(k, (V) cloneList((List)v, deepClone));
                    } else if (v instanceof Map) {
                        newMap.put(k, (V) cloneMap((Map)v, deepClone));
                    } else {
                        newMap.put(k, v);
                    }
                } else {
                    newMap.put(k, v);
                }
            }

            return newMap;
        } else {
            return null;
        }
    }

    public static <E> List<E> cloneList(List<E> list) {
        return cloneList(list, false);
    }

    public static <E> List<E> cloneList(List<E> list, boolean deepClone) {
        if (list != null) {
            Vector<E> vect = new Vector<E>(list.size());
            for (E o : list) {
                if (deepClone) {
                    if (o instanceof List) {
                        vect.add((E)cloneList((List)o, deepClone));
                    } else if (o instanceof Map) {
                        vect.add((E)cloneMap((Map)o, deepClone));
                    }
                } else {
                    vect.add(o);
                }
            }
            return vect;
        } else {
            return null;
        }
    }
    
    public static int indexOf(String[] c, String k) {
        for (int i = 0; i < c.length; i++) {
            if (c[i].equals(k)) {
                return i;
            }
        }
        return -1;
    }
    
    public static int indexOf(short[] s, short k) {
    	for (int i = 0; i < s.length; i++) {
    		if (s[i] == k) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    public static int indexOf(long[] l, long k) {
    	for (int i = 0; i < l.length; i++) {
    		if (l[i] == k) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    public static int indexOf(byte[] b, byte k) {
    	for (int i = 0; i < b.length; i++) {
    		if (b[i] == k) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    public static int indexOf(float[] f, float k) {
    	for (int i = 0; i < f.length; i++) {
    		if (f[i] == k) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    public static int indexOf(boolean[] b, boolean k) {
    	for (int i = 0; i < b.length; i++) {
    		if (b[i] == k) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    public static int indexOf(int[] c, int k) {
        for (int i = 0; i < c.length; i++) {
            if (c[i] == k) {
                return i;
            }
        }
        return -1;
    }
    
    public static int indexOf(char[] c, char k) {
        for (int i = 0; i < c.length; i++) {
            if (c[i] == k){
                return i;
            }
        }
        return -1;
    }
    
    public static int indexOf(double[] c, double k) {
        for (int i = 0; i < c.length; i++) {
            if (c[i] == k) {
                return i;
            }
        }
        return -1;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Object findValue(List<? extends Comparable> list, MathValueType findType) {
        if (list == null || list.size() == 0) {
            return null;
        } else {
            boolean isMin = findType == MathValueType.MIN;
            Object val = null;
            for (Comparable c : list) {
                if (val == null) {
                    val = c;
                } else {
                    if (isMin) {
                        if (c.compareTo(val) < 0) {
                            val = c;
                        }
                    } else {
                        if (c.compareTo(val) > 0) {
                            val = c;
                        }
                    }
                }
            }
            return val;
        }
    }
    
    @SuppressWarnings("rawtypes")
    public static Object findMin(List<? extends Comparable> list) {
        return findValue(list, MathValueType.MIN);
    }
    
    @SuppressWarnings("rawtypes")
    public static Object findMax(List<? extends Comparable> list) {
        return findValue(list, MathValueType.MAX);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Object findValue(List list, MathValueType findType, Comparator comparator) {
        if (list == null || list.size() <= 0) {
            return null;
        } else {
            Object val = null;
            boolean isMin = findType == MathValueType.MIN;
            for (Object o : list) {
                if (val == null) {
                    val = o;
                } else {
                    if (isMin) {
                        if (comparator.compare(o, val) < 0) {
                            val = o;
                        }
                    } else {
                        if (comparator.compare(o, val) > 0) {
                            val = o;
                        }
                    }
                }
            }
            return val;
        }
    }
    
    @SuppressWarnings("rawtypes")
    public static Object findMin(List list, Comparator comparator) {
        return findValue(list, MathValueType.MIN, comparator);
    }
    
    @SuppressWarnings("rawtypes")
    public static Object findMax(List list, Comparator comparator) {
        return findValue(list, MathValueType.MAX, comparator);
    }
    
    public static int findValue(int[] intArr, MathValueType findType) {
        boolean isMin = findType == MathValueType.MIN;
        int val = (isMin)? Integer.MAX_VALUE : Integer.MIN_VALUE;
        if (intArr == null) {
            return (isMin)? Integer.MIN_VALUE : Integer.MAX_VALUE;
        } else {
            for (int v : intArr) {
                if (isMin && val > v) {
                    val = v;
                } else if (!isMin && val < v) {
                    val = v;
                }
            }
            return val;
        }
    }
    
    public static int findMin(int[] intArr) {
        return findValue(intArr, MathValueType.MIN);
    }
    
    public static int findMax(int[] intArr) {
        return findValue(intArr, MathValueType.MAX);
    }
    
    public static <E> void addAllListDatas(Collection<E> datas, List<E> target, boolean distinct) {
        for (E data : datas) {
            if (distinct && !target.contains(data)) {
                target.add(data);
            } else {
                target.add(data);
            }
        }
    }

    public static byte extractByte(Object o, byte defaultValue) {
        Byte v = extractByte(o);

        return (v != null)? v : defaultValue;
    }

    public static Byte extractByte(Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof Byte) {
            return (Byte) o;
        } else if (o instanceof ValueHolder) {
            return extractByte(((ValueHolder)o).getValue());
        } else if (o instanceof String) {
            String s = ((String)o).toLowerCase();

            if (s.length() == 4 && s.startsWith("0x")) {
                return (byte) ((Character.digit(s.charAt(2), 16) << 4) + Character.digit(s.charAt(3), 16));
            } else if (s.length() == 2) {
                return (byte) ((Character.digit(s.charAt(0), 16) << 4) + Character.digit(s.charAt(1), 16));
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static short extractShort(Object o, short defaultValue) {
        Short v = extractShort(o);

        return (v != null)? v : defaultValue;
    }

    public static Short extractShort(Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof Short) {
            return (Short) o;
        } else if (o instanceof Number) {
            return ((Number) o).shortValue();
        } else if (o instanceof ValueHolder) {
            return extractShort(((ValueHolder)o).getValue());
        } else if (o instanceof String || !o.toString().equals("")) {
            String s = o.toString();
            try {
                return new Short(s);
            } catch (NumberFormatException nfe) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static int extractInteger(Object o, int defaultValue) {
        Integer v = extractInteger(o);

        return (v != null)? v : defaultValue;
    }

    public static Integer extractInteger(Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof Integer) {
            return (Integer)o;
        } else if (o instanceof Number) {
            return ((Number) o).intValue();
        } else if (o instanceof ValueHolder) {
            return extractInteger(((ValueHolder)o).getValue());
        } else if (o instanceof String || !o.toString().equals("")) {
            String s = o.toString();
            try {
                return new Integer(s);
            } catch (NumberFormatException nfe) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static long extractLong(Object o, long defaultValue) {
        Long v = extractLong(o);

        return (v != null)? v : defaultValue;
    }
    
    public static Long extractLong(Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof Long) {
            return (Long) o;
        } else if (o instanceof Number) {
            return ((Number) o).longValue();
        } else if (o instanceof ValueHolder) {
            return extractLong(((ValueHolder)o).getValue());
        } else if (o instanceof String || !o.toString().equals("")) {
            String s = o.toString();
            try {
                return new Long(s);
            } catch (NumberFormatException nfe) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static float extractFloat(Object o, float defaultValue) {
        Float v = extractFloat(o);

        return (v != null)? v : defaultValue;
    }

    public static Float extractFloat(Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof Float) {
            return (Float)o;
        } else if (o instanceof Number) {
            return ((Number) o).floatValue();
        } else if (o instanceof ValueHolder) {
            return extractFloat(((ValueHolder)o).getValue());
        } else if (o instanceof String || !o.toString().equals("")) {
            String s = o.toString();

            try {
                return new Float(s);
            } catch (NumberFormatException nfe) {
                return null;
            }
        } else {
            return null;
        }
    }
    
    public static double extractDouble(Object o, double defaultValue) {
        Double v = extractDouble(o);

        return (v != null)? v : defaultValue;
    }

    public static Double extractDouble(Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof Double) {
            return (Double)o;
        } else if (o instanceof Number) {
            return ((Number) o).doubleValue();
        } else if (o instanceof ValueHolder) {
            return extractDouble(((ValueHolder)o).getValue());
        } else if (o instanceof String || !o.toString().equals("")) {
            String s = o.toString();
            try {
                return new Double(s);
            } catch (NumberFormatException nfe) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static BigDecimal extractBigDecimal(Object o, BigDecimal defaultValue) {
        BigDecimal v = extractBigDecimal(o);

        return (v != null)? v : defaultValue;
    }

    public static BigDecimal extractBigDecimal(Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof BigDecimal) {
            return (BigDecimal)o;
        } else if (o instanceof Number) {
            return new BigDecimal(o.toString());
        } else if (o instanceof ValueHolder) {
            return extractBigDecimal(((ValueHolder)o).getValue());
        } else if (o instanceof String || !o.toString().equals("")) {
            String s = o.toString().replaceAll(",", "");
            try {
                return new BigDecimal(s);
            } catch (NumberFormatException nfe) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static boolean extractBoolean(Object o, boolean defaultValue) {
        Boolean v = extractBoolean(o);

        return (v != null)? v.booleanValue() : defaultValue;
    }

    public static Boolean extractBoolean(Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof Boolean) {
            return (Boolean)o;
        } else if (o instanceof ValueHolder) {
            return extractBoolean(((ValueHolder)o).getValue());
        } else if (o instanceof String || !o.toString().equals("")) {
            String s = o.toString();
            if (StringUtil.stringMatchOnceIgnoreCase(s, "y", "yes", "true")) {
                return Boolean.TRUE;
            } else if (StringUtil.stringMatchOnceIgnoreCase(s, "n", "no", "false")) {
                return Boolean.FALSE;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    
    public static String extractString(Object o, String defaultValue) {
        String v = extractString(o);

        return (v != null)? v : defaultValue;
    }

    public static String extractString(Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof String) {
            return (String)o;
        } else if (o instanceof ValueHolder) {
            return extractString(((ValueHolder)o).getValue());
        } else {
            return o.toString();
        }
    }
    
    public static DateCalendar DateCalendar(Object o, DateCalendar defaultValue) {
        DateCalendar v = extractDateCalendar(o);

        return (v != null)? v : defaultValue;
    }

    public static DateCalendar extractDateCalendar(Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof DateCalendar) {
            return (DateCalendar)o;
        } else if (o instanceof Date) {
            Date date = (Date) o;
            return new DateCalendar(date);
        } else if (o instanceof ValueHolder) {
            return extractDateCalendar(((ValueHolder)o).getValue());
        } else if (o instanceof String || !o.toString().equals("")) {
            String s = o.toString();
            if (s.trim().equals("")) {
                return null;
            } else {
                return new DateCalendar(s, DateCalendar.DEFAULT_DATE_FORMAT);
            }
        } else {
            return null;
        }
    }

    public static Date extractDate(Object o, Date defaultValue) {
        Date v = extractDate(o);

        return (v != null)? v : defaultValue;
    }

    public static Date extractDate(Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof Date) {
            return (Date)o;
        } else if (o instanceof ValueHolder) {
            return extractDate(((ValueHolder)o).getValue());
        } else if (o instanceof String || !o.toString().equals("")) {
            String s = o.toString();
            if ("".equals(s.trim())) {
                return null;
            } else {
                return DateTimeParser.parseTime(s, DateTimeParser.NORMAL_DATE_FORMAT);
            }
        } else {
            return null;
        }
    }

    public static Date extractDateTime(Object o, Date defaultValue) {
        Date v = extractDateTime(o);

        return (v != null)? v : defaultValue;
    }

    public static Date extractDateTime(Object o) {
        return extractDate(o);
    }

    public static <K, V> Map<K, V> joinHashMap(Map<K, V> a, Map<K, V> b) {
        Map<K, V> c = new HashMap<>();
        c.putAll(a);
        c.putAll(b);
        return c;
    }

    public static <K, E> BigDecimal getBigDecimal(Map<K, E> map, K key) {
        return getBigDecimal(map, key, BigDecimal.ZERO);
    }

    public static <K, E> BigDecimal getBigDecimal(Map<K, E> map, K key, BigDecimal defaultValue) {
        if (map == null || !map.containsKey(key) || map.get(key) == null) {
            return defaultValue;
        } else {
            E v = map.get(key);

            if (v instanceof BigDecimal) {
                return (BigDecimal) v;
            } else {
                try {
                    return new BigDecimal(v.toString());
                } catch (NumberFormatException nfe) {
                    return defaultValue;
                }
            }
        }
    }

    public static <K, E> Date getDate(Map<K, E> map, K key) {
        return getDate(map, key, null);
    }

    public static <K, E> Date getDate(Map<K, E> map, K key, Date defaultValue) {
        if (map == null || !map.containsKey(key)) {
            return defaultValue;
        } else {
            E v = map.get(key);

            if (v == null) {
                return defaultValue;
            } else if (v instanceof Date) {
                return (Date) v;
            } else if (v instanceof String){
                Date dateValue = DateTimeParser.parse((String) v, DateTimeParser.NORMAL_DATE_FORMAT);

                if (dateValue == null) {
                    return defaultValue;
                } else {
                    return dateValue;
                }
            } else {
                return defaultValue;
            }
        }
    }

    public static <K, E> int getIntValue(Map<K, E> map, K key) {
        return getIntValue(map, key, 10, -1);
    }
    
    public static <K, E> int getIntValue(Map<K, E> map, K key, int radix) {
        return getIntValue(map, key, radix, -1);
    }
    
    public static <K, E> int getIntValue(Map<K, E> map, K key, int radix, int defaultValue) {
        if (map == null || !map.containsKey(key) || map.get(key) == null) {
            return defaultValue;
        } else {
            E v = map.get(key);

            if (v instanceof Integer) {
                return (Integer) v;
            } else if (v instanceof Long) {
                return ((Long) v).intValue();
            } else if (v instanceof Float) {
                return ((Float) v).intValue();
            } else if (v instanceof Double) {
                return ((Double) v).intValue();
            } else {
                return MathUtil.parseInt(v.toString(), radix, defaultValue);
            }
        }
    }

    public static <K, E> boolean getBooleanValue(Map<K, E> map, K key) {
        return getBooleanValue(map, key, false);
    }

    public static <K, E> boolean getBooleanValue(Map<K, E> map, K key, boolean defaultValue) {
        if (map == null || !map.containsKey(key) || map.get(key) == null) {
            return defaultValue;
        } else {
            E v = map.get(key);

            if (v instanceof Boolean) {
                return (Boolean) v;
            } else {
                return Boolean.parseBoolean(v.toString());
            }
        }
    }
    
    public static <K, E> double getDoubleValue(Map<K, E> map, K key) {
        return getDoubleValue(map, key, -1);
    }
    
    public static <K, E> double getDoubleValue(Map<K, E> map, K key, double defaultValue) {
        if (map == null || !map.containsKey(key) || map.get(key) == null) {
            return defaultValue;
        } else {
            E v = map.get(key);

            if (v instanceof Integer) {
                return ((Integer) v).doubleValue();
            } else if (v instanceof Long) {
                return ((Long) v).doubleValue();
            } else if (v instanceof Float) {
                return ((Float) v).doubleValue();
            } else if (v instanceof Double) {
                return (Double) v;
            } else {
                return MathUtil.parseDouble(v.toString(), defaultValue);
            }
        }
    }

    public static <K, E> long getLongValue(Map<K, E> map, K key) {
        return getLongValue(map, key, 10, -1);
    }

    public static <K, E> long getLongValue(Map<K, E> map, K key, int radix) {
        return getLongValue(map, key, radix, -1);
    }

    public static <K, E> long getLongValue(Map<K, E> map, K key, int radix, long defaultValue) {
        if (map == null || !map.containsKey(key) || map.get(key) == null) {
            return defaultValue;
        } else {
            E v = map.get(key);
            if (v instanceof Integer) {
                return ((Integer) v).longValue();
            } else if (v instanceof Long) {
                return (Long) v;
            } else if (v instanceof Float) {
                return ((Float) v).longValue();
            } else if (v instanceof Double) {
                return ((Double) v).longValue();
            } else {
                return MathUtil.parseLong(v.toString(), radix, defaultValue);
            }
        }
    }

    public static <E, C> String getMapString(Map<E, C> map) {
        StringBuilder builder = new StringBuilder();

        builder.append("{");

        boolean first = true;

        for (Map.Entry<E, C> entry : map.entrySet()) {
            if (first) {
                first = false;
            } else {
                builder.append(", ");
            }

            builder.append(entry.getKey().toString() + ": ");

            if (entry.getValue() == null) {
                builder.append("null");
            } else {
                builder.append(entry.getValue().toString());
            }
        }

        builder.append("}");

        return builder.toString();
    }

    public static <E, C> boolean containOneKey(Map<E, C> map, E ... keys) {
        for (E key : keys) {
            if (map.containsKey(key)) {
                return true;
            }
        }
        return false;
    }

    public static <E, C> boolean containAllKeys(Map<E, C> map, E ... keys) {
        for (E key : keys) {
            if (!map.containsKey(key)) {
                return false;
            }
        }
        return true;
    }

    public static <E> String getListString(List<E> list) {
        StringBuilder builder = new StringBuilder();

        builder.append('[');

        boolean first = true;

        for (E ele : list) {
            if (first) {
                first = false;
            } else {
                builder.append(", ");
            }

            if (ele == null) {
                builder.append("null");
            } else {
                builder.append(ele.toString());
            }
        }

        builder.append(']');

        return builder.toString();
    }

    public static <K, V> V getOrDefault(Map<K, V> map, K key, V defaultValue) {
        if (map.containsKey(key)) {
            V v = map.get(key);
            return (v != null)? v : defaultValue;
        } else {
            return defaultValue;
        }
    }

    public static <K, V> String getStringValue(Map<K, V> map, K key, String defaultValue) {
        if (map.containsKey(key)) {
            V value = map.get(key);

            return (value != null)? String.valueOf(value) : defaultValue;
        } else {
            return defaultValue;
        }
    }

    public static <E> E getValue(Map<String, E> map, String key, E defaultValue) {
        if (map.containsKey(key)) {
            E v = map.get(key);
            if (v == null) {
                return defaultValue;
            } else {
                return v;
            }
        } else {
            return defaultValue;
        }
    }

    public static <E> void setListData(List<E> list, int index, E value) {
        setListData(list, index, value, null);
    }

    public static <E> void setListData(List<E> list, int index, E value, E fillValue) {
        if (index < 0) {
            return;
        }
        if (index < list.size()) {
            list.set(index, value);
        } else {
            while (list.size() <= index) {
                list.add(fillValue);
            }
            list.add(value);
        }
    }
    
    public static <E> void copyArrayToList(List<E> list, E[] array) {
        ListIterator<E> iter = list.listIterator();
        for (E anArray : array) {
            iter.next();
            iter.set(anArray);
        }
    }
    
    public static int hashCode(Object ... objs) {
        if (objs == null) {
            return -1;
        } else {
            int hashCode = 0;
            for (Object obj : objs) {
                hashCode = 31 * hashCode + obj.hashCode();
            }
            return hashCode;
        }
    }

    public static <E> boolean hasDuplicates(List<E> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i).equals(list.get(j))) {
                    return true;
                }
            }
        }

        return false;
    }

    public static <K, V> Map<K, V> createMap(Map.Entry<K, V> ... entries) {
        Map<K, V> map = new HashMap<>(entries.length);
        for (Map.Entry<K, V> entry : entries) {
            map.put(entry.getKey(), entry.getValue());
        }

        return map;
    }

    public static <K, V> KeyValuePair<K, V> pair(K k, V v) {
        return new KeyValuePair<K, V>(k, v);
    }

    public static <K, V> Map<K, V> createSimpleMap(K k, V v) {
        Map<K, V> map = new HashMap<K, V>();
        map.put(k, v);
        return map;
    }

    public static <K, V> Map<K, V> createMap(KeyValuePair<K, V> ... pairs) {
        Map<K, V> map = new HashMap<K, V>(pairs.length);

        for (KeyValuePair<K, V> pair : pairs) {
            map.put(pair.getKey(), pair.getValue());
        }

        return map;
    }

    public static <K, V> Map<K, V> createLinkedMap(KeyValuePair<K, V> ... pairs) {
        Map<K, V> map = new LinkedHashMap<K, V>(pairs.length);

        for (KeyValuePair<K, V> pair : pairs) {
            map.put(pair.getKey(), pair.getValue());
        }

        return map;
    }

    public static <K, V> Map<K, V> createMap(List<K> keys, List<V> values) {
        return createMap(HashMap.class, keys, values);
    }

    public static <K, V> List<Map<K, V>> createMapList(List<K> keys, List<V> ... valueLists) {
        List<Map<K, V>> mapList = new ArrayList<>();

        for (List<V> values : valueLists) {
            mapList.add(createMap(keys, values));
        }

        return mapList;
    }

    public static <K, V> List<Map<K, V>> createMapList(List<K> keys, List<List<V>> valueLists) {
        List<Map<K, V>> mapList = new ArrayList<>();

        for (List<V> values : valueLists) {
            mapList.add(createMap(keys, values));
        }

        return mapList;
    }

    public static <K, V> Map<K, V> createMap(Class<? extends Map> mapClass, List<K> keys, List<V> values) {
        try {
            @SuppressWarnings("unchecked")
            Map<K, V> resultMap = mapClass.newInstance();

            ListIterator<K> keyIter = keys.listIterator();
            ListIterator<V> valueIter = values.listIterator();

            while (keyIter.hasNext() && valueIter.hasNext()) {
                resultMap.put(keyIter.next(), valueIter.next());
            }

            return resultMap;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static <E> List<E> createListWrapper(E element) {
        List<E> list = new ArrayList<E>();
        list.add(element);
        return list;
    }
    
    public static List<Integer> createSequence(int start, int end) {
        if (end < start) {
            int t = end;
            end = start;
            start = t;
        }
        List<Integer> seq = new ArrayList<>();
        
        for (int i = start; i <= end; i++) {
            seq.add(i);
        }
        
        return seq;
    }
    
    public static <E> List<E> createList(List<E> list, List<Integer> indexes) {
        List<E> r = new ArrayList<E>(indexes.size());
        
        for (Integer index : indexes) {
            r.add(list.get(index));
        }
        
        return r;
    }

    public static <E> List<E> createDefaultList(int length, E defaultValue) {
        List<E> list = new ArrayList<E>(length);

        for (int i = 0; i < length; i++) {
            list.add(defaultValue);
        }

        return list;
    }

    public static <E> List<E> createList(E ... values) {
        List<E> list = new ArrayList<E>(values.length);

        Collections.addAll(list, values);

        return list;
    }

    public static <E> List<E> createList(Class<? extends List> listclass, E ... values) {
        try {
            @SuppressWarnings("unchecked")
            List<E> list = listclass.newInstance();

            for (E value : values) {
                list.add(value);
            }

            return list;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <E> E[] createArray(Class<E> clz, E ... eles) {
        return (E[]) Array.newInstance(clz, eles.length);
    }

    public static <E, C> void retainMapKeys(Map<E, C> map, E ... keys) {
        map.keySet().retainAll(createList(keys));
    }

    public static <K, V> void sortListOfMaps(List<Map<K, V>> maps, K ... orderKeys) {
        sortListOfMaps(maps, true, true, orderKeys);
    }

    public static <K, V> void sortListOfMaps(
            List<Map<K, V>> maps, final boolean asc, final boolean nullAsSmaller, final K ... orderKeys
    ) {
        Collections.sort(
                maps, (o1, o2) -> {
                    for (K orderKey : orderKeys) {
                        V v1 = o1.get(orderKey), v2 = o2.get(orderKey);

                        if (!v1.equals(v2)) {
                            return DataComparator.compare(v1, v2, asc, nullAsSmaller);
                        }
                    }

                    return 0;
                }
        );
    }

    public static <K> int adjustMapIntegerValue(Map<K, Object> map, K key, Integer adjustValue) {
        int result = getIntValue(map, key, 10, 0);

        result += adjustValue;

        map.put(key, result);

        return result;
    }

    public static <K> BigDecimal adjustMapBigDecimalValue(Map<K, Object> map, K key, BigDecimal adjustValue) {
        BigDecimal result = getBigDecimal(map, key, BigDecimal.ZERO);

        result = result.add(adjustValue);

        map.put(key, result);

        return result;
    }

    public static int integerSumOf(Collection<? extends Integer> list) {
        int result = 0;

        for (int v : list) {
            result += v;
        }

        return result;
    }

    public static int integerSumOfList(List<? extends Integer> list, int start, int end) {
        int result = 0;

        for (int i = start; i <= end && i >= 0 && i < list.size(); i++) {
            result += list.get(i);
        }

        return result;
    }

    public static BigDecimal bigDecimalSumOf(Collection<? extends BigDecimal> list) {
        BigDecimal result = BigDecimal.ZERO;

        for (BigDecimal v : list) {
            result = result.add(v);
        }

        return result;
    }

    public static BigDecimal bigDecimalSumOfList(List<? extends BigDecimal> list, int start, int end) {
        BigDecimal result = BigDecimal.ZERO;

        for (int i = start; i <= end && i >= 0 && i < list.size(); i++) {
            result = result.add(list.get(i));
        }

        return result;
    }

    public static <K> int integerSumOfMapData(
        Map<K, Object> map, K ... keys
    ) {
        int result = 0;

        for (K key : keys) {
            result += getIntValue(map, key, 10, 0);
        }

        return result;
    }

    public static <K> BigDecimal bigDecimalSumOfMapData(
        Map<K, Object> map, K ... keys
    ) {
        BigDecimal result = BigDecimal.ZERO;

        for (K key : keys) {
            result = result.add(getBigDecimal(map, key, BigDecimal.ZERO));
        }

        return result;
    }

    public static <K> long adjustMapLongValue(Map<K, Object> map, K key, Long adjustValue) {
        long result = getLongValue(map, key, 10, 0);

        result += adjustValue;

        map.put(key, result);

        return result;
    }
}
