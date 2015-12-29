package utils.data;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PrinterUtil {
    public static String getDoubleArrString(double[] arr) {
        if (arr == null) {
            return "NULL";
        }
        StringBuilder builder = new StringBuilder(arr.length * 5);
        builder.append('[');
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                builder.append(',');
            }
            builder.append(i);
            builder.append(':');
            builder.append(arr[i]);
        }
        builder.append(']');
        return builder.toString();
    }
    
    public static String getFloatArrString(float[] arr) {
        if (arr == null) {
            return "NULL";
        }
        StringBuilder builder = new StringBuilder(arr.length * 5);
        builder.append('[');
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                builder.append(',');
            }
            builder.append(i);
            builder.append(':');
            builder.append(arr[i]);
        }
        builder.append(']');
        return builder.toString();
    }
    
    public static String getIntArrString(int[] arr) {
        return getIntArrString(arr, true);
    }
    
    public static String getIntArrString(int[] arr, boolean printIndex) {
        if (arr == null) {
            return "NULL";
        }
        StringBuilder builder = new StringBuilder(arr.length * 5);
        builder.append('[');
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                builder.append(',');
            }
            if (printIndex) {
                builder.append(i);
                builder.append(':');
            }
            builder.append(arr[i]);
        }
        builder.append(']');
        return builder.toString();
    }

    public static String getIntArrString(Integer[] arr) {
        return getIntArrString(arr, true);
    }

    public static String getIntArrString(Integer[] arr, boolean printIndex) {
        if (arr == null) {
            return "NULL";
        }

        StringBuilder builder = new StringBuilder(arr.length*5);
        builder.append('[');
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                builder.append(',');
            }
            if (printIndex) {
                builder.append(i);
                builder.append(':');
            }
            builder.append(arr[i]);
        }
        builder.append(']');
        return builder.toString();
    }

    public static String getStringArrString(String[] arr, boolean showIndex) {
        if (arr == null) {
            return "NULL";
        }
        StringBuilder builder = new StringBuilder(arr.length * 5);
        builder.append('[');
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                builder.append(',');
            }
            if (showIndex) {
                builder.append(i);
                builder.append(':');
            }
            builder.append(arr[i]);
        }
        builder.append(']');
        return builder.toString();
    }

    public static String getStringArrString(String[] arr) {
        return getStringArrString(arr, false);
    }

    public static String getCollectionString(Collection collection) {
        if (collection == null) {
            return "NULL";
        }

        StringBuilder builder = new StringBuilder(collection.size()*5);
        builder.append('[');
        for (Object obj : collection) {
            if (builder.length() > 1) {
                builder.append(',');
            }
            if (obj instanceof Collection) {
                builder.append(getCollectionString((List)obj));
            } else if (obj instanceof Map) {
                builder.append(getMapString((Map)obj));
            } else {
                builder.append(obj.toString());
            }
        }
        builder.append(']');
        return builder.toString();
    }
    
    @SuppressWarnings("rawtypes")
    public static String getMapString(Map map) {
        StringBuilder builder = new StringBuilder(map.size() * 15);
        Set keySet = map.keySet();
        for (Object k : keySet) {
            builder.append(k.toString());
            builder.append(" : ");
            Object obj = map.get(k);
            if (obj != null) {
                builder.append(obj.toString());
            } else {
                builder.append("[Empty object value]");
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    @SuppressWarnings("rawtypes")
    public static String getListString(List list) {
        if (list == null) {
            return "NULL";
        } else if (list.isEmpty()) {
            return "[]";
        }

        StringBuilder builder = new StringBuilder(list.size()*15);
        builder.append('[');
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                builder.append(',');
            }
            builder.append(i);
            builder.append(':');
            Object obj = list.get(i);
            builder.append((obj != null)? list.get(i).toString() : "{Empty object value}");
        }
        builder.append(']');
        return builder.toString();
    }
    
    public static String getLine2DString(Line2D line) {
        return "[x1: " + line.getX1() + ", y1: " + line.getY1() + 
            ", x2: " + line.getX2() + ", y2: " + line.getY2();
    }
    
    public static String getRectangle2DString(Rectangle2D rect) {
        return "[maxX: " + rect.getMaxX() + ", maxY: " + rect.getMaxY() + 
            ", minX: " + rect.getMinX() + ", minY: " + rect.getMinY() + 
            ", width: " + rect.getWidth() + ", height: " + rect.getHeight() + "]";
    }
    
    public static void printDoubleArray(double[] arr) {
        System.out.print(getDoubleArrString(arr));
    }
    
    public static void printFloatArray(float[] arr) {
        System.out.print(getFloatArrString(arr));
    }
    
    public static void printIntArray(int[] arr, boolean printIndex) {
        System.out.print(getIntArrString(arr, printIndex));
    }
    
    public static void printIntArray(int[] arr) {
        printIntArray(arr, true);
    }
    
    public static void pringStringArray(String[] arr) {
        System.out.print(getStringArrString(arr));
    }
    
    public static void printLine2D(Line2D line) {
        System.out.print(getLine2DString(line));
    }
    
    public static void printRectangle2D(Rectangle2D rect) {
        System.out.print(getRectangle2DString(rect));
    }
    
    @SuppressWarnings("rawtypes")
    public static void printMap(Map map) {
        System.out.print(getMapString(map));
    }

    @SuppressWarnings("rawtypes")
    public static void printList(List list) {
        System.out.print(getListString(list));
    }

    public static void printCollectionString(List list) {
        System.out.print(getCollectionString(list));
    }
}
