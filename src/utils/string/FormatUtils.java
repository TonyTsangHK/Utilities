package utils.string;

import utils.date.DateTimeParser;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2013-06-10
 * Time: 11:10
 */
public class FormatUtils {
    private FormatUtils() {}

    private static final NumberFormat DEFAULT_INTEGER_FORMAT,
                                      CustomFormat = NumberFormat.getInstance();
    
    private static final Map<String, String> DURATION_LANGUAGE_MAP_EN;
    private static final Map<String, String> DURATION_LANGUAGE_MAP_ZH;
    
    private static final Map<String, Map<String, String>> DURATION_LANGUAGE_MAP;
    
    static {
        DEFAULT_INTEGER_FORMAT = NumberFormat.getInstance();
        DEFAULT_INTEGER_FORMAT.setGroupingUsed(true);
        DEFAULT_INTEGER_FORMAT.setRoundingMode(RoundingMode.HALF_UP);
        DEFAULT_INTEGER_FORMAT.setMaximumFractionDigits(0);

        DURATION_LANGUAGE_MAP_EN = new HashMap<>();

        DURATION_LANGUAGE_MAP_EN.put("concatenationWord", "and");
        DURATION_LANGUAGE_MAP_EN.put("dayPlural", "days");
        DURATION_LANGUAGE_MAP_EN.put("daySingular", "day");
        DURATION_LANGUAGE_MAP_EN.put("hourPlural", "hours");
        DURATION_LANGUAGE_MAP_EN.put("hourSingular", "hour");
        DURATION_LANGUAGE_MAP_EN.put("minutePlural", "minutes");
        DURATION_LANGUAGE_MAP_EN.put("minuteSingular", "minute");
        DURATION_LANGUAGE_MAP_EN.put("secondPlural", "seconds");
        DURATION_LANGUAGE_MAP_EN.put("secondSingular", "second");
        DURATION_LANGUAGE_MAP_EN.put("millisecondPlural", "milliseconds");
        DURATION_LANGUAGE_MAP_EN.put("millisecondSingular", "millisecond");

        DURATION_LANGUAGE_MAP_ZH = new HashMap<>();

        DURATION_LANGUAGE_MAP_ZH.put("concatenationWord", "");
        DURATION_LANGUAGE_MAP_ZH.put("dayPlural", "日");
        DURATION_LANGUAGE_MAP_ZH.put("daySingular", "日");
        DURATION_LANGUAGE_MAP_ZH.put("hourPlural", "小時");
        DURATION_LANGUAGE_MAP_ZH.put("hourSingular", "小時");
        DURATION_LANGUAGE_MAP_ZH.put("minutePlural", "分");
        DURATION_LANGUAGE_MAP_ZH.put("minuteSingular", "分");
        DURATION_LANGUAGE_MAP_ZH.put("secondPlural", "秒");
        DURATION_LANGUAGE_MAP_ZH.put("secondSingular", "秒");
        DURATION_LANGUAGE_MAP_ZH.put("millisecondPlural", "毫秒");
        DURATION_LANGUAGE_MAP_ZH.put("millisecondSingular", "毫秒");
        
        DURATION_LANGUAGE_MAP = new HashMap<>();
        DURATION_LANGUAGE_MAP.put("en", DURATION_LANGUAGE_MAP_EN);
        DURATION_LANGUAGE_MAP.put("zh", DURATION_LANGUAGE_MAP_ZH);

        CustomFormat.setRoundingMode(RoundingMode.HALF_UP);
    }

    public static String formatNumber(float number) {
        return formatNumber(number, 2, 2, true);
    }

    public static String formatNumber(float number, int maxFractionDigits) {
        return formatNumber(number, 2, maxFractionDigits, true);
    }

    public static String formatNumber(float number, int minFractionDigits, int maxFractionDigits) {
        return formatNumber(number, minFractionDigits, maxFractionDigits, true);
    }

    public static String formatNumber(float number, int maxFractionDigits, boolean grouping) {
        return formatNumber(number, 2, maxFractionDigits, grouping);
    }

    public static String formatNumber(float number, int minFractionDigits, int maxFractionDigits, boolean grouping) {
        CustomFormat.setGroupingUsed(grouping);
        if (maxFractionDigits >= minFractionDigits) {
            CustomFormat.setMinimumFractionDigits(minFractionDigits);
        } else {
            CustomFormat.setMinimumFractionDigits(maxFractionDigits);
        }
        CustomFormat.setMaximumFractionDigits(maxFractionDigits);

        return CustomFormat.format(number);
    }

    public static String formatNumber(double number) {
        return formatNumber(number, 2, 2, true);
    }

    public static String formatNumber(double number, int maxFractionDigits) {
        return formatNumber(number, 2, maxFractionDigits, true);
    }

    public static String formatNumber(double number, int minFractionDigits, int maxFractionDigits) {
        return formatNumber(number, minFractionDigits, maxFractionDigits, true);
    }

    public static String formatNumber(double number, int maxFractionDigits, boolean grouping) {
        return formatNumber(number, 2, maxFractionDigits, grouping);
    }

    public static String formatNumber(double number, int minFractionDigits, int maxFractionDigits, boolean grouping) {
        CustomFormat.setGroupingUsed(grouping);
        if (maxFractionDigits >= minFractionDigits) {
            CustomFormat.setMinimumFractionDigits(minFractionDigits);
        } else {
            CustomFormat.setMaximumFractionDigits(maxFractionDigits);
        }
        CustomFormat.setMaximumFractionDigits(maxFractionDigits);

        return CustomFormat.format(number);
    }

    public static String formatNumber(BigDecimal number) {
        return formatNumber(number, 2, 2, true);
    }

    public static String formatNumber(BigDecimal number, int maxFractionDigits) {
        return formatNumber(number, 2, maxFractionDigits, true);
    }

    public static String formatNumber(BigDecimal number, int minFractionDigits, int maxFractionDigits) {
        return formatNumber(number, minFractionDigits, maxFractionDigits, true);
    }

    public static String formatNumber(BigDecimal number, int maxFractionDigits, boolean grouping) {
        return formatNumber(number, 2, maxFractionDigits, grouping);
    }

    public static String formatNumber(BigDecimal number, int minFractionDigits, int maxFractionDigits, boolean grouping) {
        if (number == null) {
            // Return empty string for null number
            return "";
        } else {
            CustomFormat.setMaximumFractionDigits(maxFractionDigits);
            if (maxFractionDigits >= minFractionDigits) {
                CustomFormat.setMinimumFractionDigits(minFractionDigits);
            } else {
                CustomFormat.setMinimumFractionDigits(maxFractionDigits);
            }
            CustomFormat.setGroupingUsed(grouping);

            return CustomFormat.format(number);
        }
    }

    public static String formatInteger(int integer) {
        return DEFAULT_INTEGER_FORMAT.format(integer);
    }

    public static String formatInteger(long longInteger) {
        return DEFAULT_INTEGER_FORMAT.format(longInteger);
    }

    public static String fillZeros(int id, int length) {
        String idStr = String.valueOf(Math.abs(id));
        char[] idChars = idStr.toCharArray();

        if (idChars.length > length) {
            return idStr;
        } else {
            char[] chars = new char[length];

            int zeroLength = chars.length - idChars.length;

            for (int i = 0; i < zeroLength; i++) {
                chars[i] = '0';
            }

            for (int i = 0; i < idChars.length; i++) {
                chars[zeroLength+i] = idChars[i];
            }

            if (id > 0) {
                return new String(chars);
            } else {
                return "-" + new String(chars);
            }
        }
    }

    public static String fillZeros(long id, int length) {
        String idStr = String.valueOf(Math.abs(id));
        char[] idChars = idStr.toCharArray();

        if (idChars.length > length) {
            return idStr;
        } else {
            char[] chars = new char[length];

            int zeroLength = chars.length - idChars.length;

            for (int i = 0; i < zeroLength; i++) {
                chars[i] = '0';
            }

            for (int i = 0; i < idChars.length; i++) {
                chars[zeroLength+i] = idChars[i];
            }

            if (id > 0) {
                return new String(chars);
            } else {
                return "-" + new String(chars);
            }
        }
    }

    public static String formatDate(Date date) {
        return DateTimeParser.format(date, DateTimeParser.NORMAL_DATE_FORMAT);
    }

    public static String formatDate(Date date, String datePattern) {
        return DateTimeParser.format(date, datePattern);
    }

    public static String formatDateTime(long timemillis) {
        return formatDateTime(timemillis, DateTimeParser.NORMAL_DATETIME_FORMAT);
    }

    public static String formatDateTime(long timemillis, String dateTimeFormat) {
        return DateTimeParser.format(new Date(timemillis), dateTimeFormat);
    }

    public static String formatDateTime(Date dateTime) {
        return DateTimeParser.format(dateTime, DateTimeParser.NORMAL_DATETIME_FORMAT);
    }

    public static String formatDateTime(Date dateTime, String dateTimePattern) {
        return DateTimeParser.format(dateTime, dateTimePattern);
    }

    public static <E> String formatList(List<E> list) {
        return formatList(list, true);
    }

    public static <E> String formatList(List<E> list, boolean bracket) {
        boolean first = true;
        StringBuilder builder = new StringBuilder();
        if (bracket) {
            builder.append('[');
        }
        for (E v : list) {
            if (first) {
                first = false;
            } else {
                builder.append(',');
            }
            builder.append((v != null)? String.valueOf(v) : "-");
        }
        if (bracket) {
            builder.append(']');
        }

        return builder.toString();
    }

    public static String formatPossibleNumber(String str) {
        try {
            BigDecimal num = new BigDecimal(str);
            return formatNumber(num);
        } catch (NumberFormatException nfe) {
            return str;
        }
    }

    public static String formatPossibleNumber(String str, int maxFractionDigits) {
        return formatPossibleNumber(str, 2, maxFractionDigits, true);
    }

    public static String formatPossibleNumber(String str, int minFractionDigits, int maxFractionDigits) {
        return formatPossibleNumber(str, minFractionDigits, maxFractionDigits, true);
    }

    public static String formatPossibleNumber(String str, int maxFractionDigits, boolean grouping) {
        return formatPossibleNumber(str, 2, maxFractionDigits, grouping);
    }

    public static String formatPossibleNumber(String str, int minFractionDigits, int maxFractionDigits, boolean grouping) {
        try {
            BigDecimal num = new BigDecimal(str);

            return formatNumber(num, minFractionDigits, maxFractionDigits, grouping);
        } catch (NumberFormatException nfe) {
            return str;
        }
    }

    public static String trimTrailingZeros(String str) {
        String s = str.replaceAll("\\.0+$", "");

        if (s.contains(".")) {
            return s.replaceAll("0+$", "");
        } else {
            return s;
        }
    }
    
    public static String formatDuration(long milliseconds) {
        return formatDuration(milliseconds, "en");
    }
    
    public static String formatDuration(long milliseconds, String lang) {
        if (DURATION_LANGUAGE_MAP.containsKey(lang)) {
            return formatDuration(milliseconds, DURATION_LANGUAGE_MAP.get(lang));
        } else {
            throw new IllegalArgumentException("["+lang+"] is not supported language");
        }
    }
    
    public static String formatDuration(long milliseconds, Map<String, String> languageMap) {
        long millis =  milliseconds % 1000;
        long seconds = milliseconds / 1000;
        long minutes = 0;
        long hours = 0;
        long days = 0;
        
        if (seconds > 60) {
            minutes = seconds / 60;
            seconds = seconds % 60;
        }
        
        if (minutes > 60) {
            hours = minutes / 60;
            minutes = minutes % 60;
        }
        
        if (hours > 24) {
            days = hours / 24;
            hours = hours % 24;
        }

        StringBuilder builder = new StringBuilder();
        
        String concatenationWord = languageMap.get("concatenationWord");
        boolean hasConcatenationWord = StringUtil.isNotEmptyString(concatenationWord); 

        if (days > 0 || builder.length() > 0) {
            appendDurationHelper(builder, concatenationWord, hasConcatenationWord, days, languageMap.get("dayPlural"), languageMap.get("daySingular"));
        }

        if (hours > 0 || builder.length() > 0) {
            appendDurationHelper(builder, concatenationWord, hasConcatenationWord, hours, languageMap.get("hourPlural"), languageMap.get("hourSingular"));
        }

        if (minutes > 0 || builder.length() > 0) {
            appendDurationHelper(builder, concatenationWord, hasConcatenationWord, minutes, languageMap.get("minutePlural"), languageMap.get("minuteSingular"));
        }

        if (seconds > 0 || builder.length() > 0) {
            appendDurationHelper(builder, concatenationWord, hasConcatenationWord, seconds, languageMap.get("secondPlural"), languageMap.get("secondSingular"));
        }
        
        if (millis > 0) {
            appendDurationHelper(builder, concatenationWord, hasConcatenationWord, millis, languageMap.get("millisecondPlural"), languageMap.get("millisecondSingular"));
        }

        if (builder.length() > 0) {
            return builder.toString();
        } else {
            return "-";
        }
    }
    
    private static void appendDurationHelper(StringBuilder builder, String concatenationWord, boolean hasConcatenationWord, long value, String plural, String singular) {
        if (builder.length() > 0) {
            if (hasConcatenationWord) {
                builder.append(" ").append(concatenationWord).append(" ");
            } else {
                builder.append(" ");
            }
        }

        builder.append(value).append(" ");

        if (value > 1) {
            builder.append(plural);
        } else {
            builder.append(singular);
        }
    }
}
