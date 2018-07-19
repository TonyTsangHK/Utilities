package utils.date;

import utils.string.StringUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeParser {
    public static final String
        CLEAR_DATETIME_FORMAT           = "yyyyMMddHHmmss",
        CLEAR_DATETIME_MILLIS_FORMAT    = "yyyyMMddHHmmssSSS",
        SIMPLE_DATE_FORMAT_ENG          = "dd/MM",
        SIMPLE_DATE_FORMAT_US           = "MM/dd",
        CLEAR_DATE_FORMAT               = "yyyyMMdd",
        NORMAL_DATE_FORMAT              = "yyyy-MM-dd",
        NORMAL_DATE_FORAMT_ENG          = "dd-MM-yyyy", 
        NORMAL_DATE_FORMAT_US           = "MM-dd-yyyy",
        NORMAL_TIME_FORMAT              = "HH:mm:ss",
        NORMAL_DATETIME_LESS_SEC_FORMAT = "yyyy-MM-dd HH:mm",
        NORMAL_DATETIME_FORMAT          = "yyyy-MM-dd HH:mm:ss",
        JAVA_DATETIME_FORMAT            = "yyyy-MM-dd'T'HH:mm:ss",
        NORMAL_DATETIME_MILLIS_FORMAT   = "yyyy-MM-dd HH:mm:ss.SSS",
        SQL_TIMESTAMP_FORMAT            = "yyyy-MM-dd HH:mm:ss.SSSSSS";
    
    private DateTimeParser() {}
    
    public static Date parse(String input) {
        return parse(input, CLEAR_DATETIME_FORMAT);
    }
    
    public static SimpleDateFormat getSimpleDateFormat() {
        return getSimpleDateFormat(null, "GMT");
    }
    
    public static SimpleDateFormat getSimpleDateFormat(String pattern) {
        return getSimpleDateFormat(pattern, "GMT");
    }
    
    public static SimpleDateFormat getSimpleDateFormat(String pattern, String timezone) {
        SimpleDateFormat sdf = 
            (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, new Locale("en"));
        if (pattern != null) {
            sdf.applyPattern(pattern);
        }
        sdf.setLenient(false);
        //sdf.setTimeZone(TimeZone.getTimeZone(timezone));
        return sdf;
    }
    
    public static Date parse(String input, String pattern) {
        String trimed = input.trim();
        
        String datePattern = pattern;
        
        if (trimed.length() < datePattern.length()) {
            datePattern = datePattern.substring(0, trimed.length());
        } else if (trimed.length() > datePattern.length()) {
            trimed = trimed.substring(0, datePattern.length());
        }
        
        SimpleDateFormat sdf = getSimpleDateFormat(datePattern);
        try {
            return sdf.parse(trimed);
        } catch (ParseException pe) {
            return null;
        }
    }
    
    public static Date parse(String input, String ... patterns) {
        for (String pattern : patterns) {
            Date d = parse(input, pattern);
            if (d != null) {
                return d;
            }
        }
        return null;
    }
    
    public static java.sql.Time parseTime(String input) {
        return parseTime(input, NORMAL_TIME_FORMAT);
    }
    
    public static java.sql.Time parseTime(String input, String pattern) {
        SimpleDateFormat sdf = getSimpleDateFormat(pattern);
        try {
            return new java.sql.Time(sdf.parse(input.trim()).getTime());
        } catch (ParseException pe) {
            return null;
        }
    }
    
    public static String format(String input, String pattern, String outputPattern) {
        if (StringUtil.isEmptyString(input)) {
            return "";
        } else {
            SimpleDateFormat sdf = getSimpleDateFormat(outputPattern);
            return sdf.format(parse(input, pattern));
        }
    }
    
    public static String format(String input, String outputPattern) {
        if (StringUtil.isEmptyString(input)) {
            return "";
        } else {
            SimpleDateFormat sdf = getSimpleDateFormat(outputPattern);
            return sdf.format(parse(input));
        }
    }
    
    public static String format(Date inputDate, String outputPattern) {
        if (inputDate == null) {
            return "";
        } else {
            SimpleDateFormat sdf = getSimpleDateFormat(outputPattern);
            return sdf.format(inputDate);
        }
    }
    
    public static String getCurrentTimeString() {
        return format(new Date(System.currentTimeMillis()), NORMAL_DATETIME_FORMAT);
    }
}
