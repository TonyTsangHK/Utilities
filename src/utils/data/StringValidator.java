package utils.data;

import java.util.regex.*;

public class StringValidator {
    public static boolean isInteger(String str, boolean allowEmpty) {
        if (str == null || str.equals("")) {
            return allowEmpty;
        } else {
            return Pattern.matches("^[-|+]?\\d+$", str);
        }
    }
    
    public static boolean isPositiveInteger(String str, boolean allowEmpty) {
        if (str == null || str.equals("")) {
            return allowEmpty;
        } else {
            return Pattern.matches("^\\+?\\d+$", str);
        }
    }
    
    public static boolean isNegativeInteger(String str, boolean allowEmpty) {
        if (str == null || str.equals("")) {
            return allowEmpty;
        } else {
            return Pattern.matches("^-\\d+$", str);
        }
    }
    
    public static boolean isNumber(String str, boolean allowEmpty) {
        if (str == null || str.equals("")) {
            return allowEmpty;
        } else {
            return Pattern.matches("^[-|+]?\\d+\\.?\\d*$", str);
        }
    }
    
    public static boolean isPositiveNumber(String str, boolean allowEmpty) {
        if (str == null || str.equals("")) {
            return allowEmpty;
        } else {
            return Pattern.matches("^+?\\d+\\.?\\d*$", str);
        }
    }
    
    public static boolean isNegativeNumber(String str, boolean allowEmpty) {
        if (str == null || str.equals("")) {
            return allowEmpty;
        } else {
            return Pattern.matches("^-\\d+\\.?\\d*$", str);
        }
    }
    
    public static boolean isNumber(String str, int precision) {
        return isNumber(str, precision, false);
    }
    
    public static boolean isNumber(String str, int precision, boolean allowEmpty) {
        if (str == null || str.equals("")) {
            return allowEmpty;
        } else if (precision == 0) {
            return isNumber(str, allowEmpty);
        } else {
            return Pattern.matches("^\\d+\\.?\\d{0," + precision + "}$", str);
        }
    }
    
    public static boolean isPositiveNumber(String str, int precision) {
        return isPositiveNumber(str, precision, false);
    }
    
    public static boolean isPositiveNumber(String str, int precision, boolean allowEmpty) {
        if (str == null || str.equals("")) {
            return allowEmpty;
        } else {
            return Pattern.matches("^+?\\d+\\.?\\d{0," + precision + "}$", str);
        }
    }
    
    public static String trimNumberToPrecision(String str, int precision) {
        if (precision < 0) {
            return str;
        } else if (precision == 0) {
            return Pattern.compile("([-|+]?\\d+)\\.?\\d*").matcher(str).replaceAll("$1");
        } else {
            return Pattern.compile("([-|+]?\\d+\\.?\\d{0," + precision + "})\\d*").matcher(str).replaceAll("$1");
        }
    }
}
