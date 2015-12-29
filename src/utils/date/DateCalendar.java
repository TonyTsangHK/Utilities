package utils.date;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import utils.data.CharacterQueue;
import utils.math.MathUtil;

@SuppressWarnings("serial")
public class DateCalendar implements Comparable<DateCalendar>, Serializable {
    private static GregorianCalendar calendar = new GregorianCalendar();
    
    public enum WeekDay {
        MONDAY   ("星期一", "Monday",    GregorianCalendar.MONDAY),
        TUESDAY  ("星期二", "Tuesday",   GregorianCalendar.TUESDAY),
        WEDNESDAY("星期三", "Wednesday", GregorianCalendar.WEDNESDAY),
        THURSDAY ("星期四", "Thursday",  GregorianCalendar.THURSDAY),
        FRIDAY   ("星期五", "Friday",    GregorianCalendar.FRIDAY),
        SATURDAY ("星期六", "Saturday",  GregorianCalendar.SATURDAY),
        SUNDAY   ("星期日", "Sunday",    GregorianCalendar.SUNDAY);
        
        public final String chineseDesc, englishDesc;
        public final int identity;
        
        WeekDay(String chi, String eng, int id) {
            this.chineseDesc = chi;
            this.englishDesc = eng;
            this.identity = id;
        }
        
        @Override
        public String toString() {
            return englishDesc;
        }
    }
    
    public enum Month {
        JANUARY  ("一月",   "January",   GregorianCalendar.JANUARY),
        FEBRUARY ("二月",   "February",  GregorianCalendar.FEBRUARY),
        MARCH    ("三月",   "March",     GregorianCalendar.MARCH),
        APRIL    ("四月",   "April",     GregorianCalendar.APRIL),
        MAY      ("五月",   "May",       GregorianCalendar.MAY),
        JUNE     ("六月",   "June",      GregorianCalendar.JUNE),
        JULY     ("七月",   "July",      GregorianCalendar.JULY),
        AUGUST   ("八月",   "August",    GregorianCalendar.AUGUST),
        SEPTEMBER("九月",   "September", GregorianCalendar.SEPTEMBER),
        OCTOBER  ("十月",   "October",   GregorianCalendar.OCTOBER),
        NOVEMBER ("十一月", "November",  GregorianCalendar.NOVEMBER),
        DECEMBER ("十二月", "December",  GregorianCalendar.DECEMBER);
        
        public final String chineseDesc, englishDesc;
        public final int identity;
        
        Month(String chi, String eng, int id) {
            this.chineseDesc = chi;
            this.englishDesc = eng;
            this.identity = id;
        }
        
        @Override
        public String toString() {
            return chineseDesc;
        }
    }
    
    public static final String
        DEFAULT_DATE_FORMAT     = "yyyy-MM-dd",
        US_DATE_FORMAT          = "MM-dd-yyyy",
        UK_DATE_FORMAT          = "dd-MM-yyyy",
        CN_DATE_FORMAT          = "yyyy年MM月dd日",
        DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss",
        US_DATETIME_FORMAT      = "MM-dd-yyyy HH:mm:ss",
        UK_DATETIME_FORMAT      = "dd-MM-yyyy HH:mm:ss",
        CN_DATETIME_FORMAT      = "yyyy年MM月dd日 HH:mm:ss";
    
    public static final char
        YEAR_CHAR   = 'y',
        MONTH_CHAR  = 'M', 
        DATE_CHAR   = 'd',
        HOUR_CHAR   = 'H',
        MINUTE_CHAR = 'm',
        SECOND_CHAR = 's';
    
    private int day, year, month, hour, minute, second;
    private String dateFormat;
    private static final int NORMAL_YEAR_DAYS = 365, LEAP_YEAR_DAYS = 366;
    private static final int[] monthdays = {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334};
    
    private static final int[] availableMonthdays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    
    public DateCalendar() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        year = cal.get(java.util.Calendar.YEAR); 
        month = cal.get(java.util.Calendar.MONTH) + 1;
        day = cal.get(java.util.Calendar.DATE);
        dateFormat = DEFAULT_DATE_FORMAT;
    }

    public DateCalendar(java.sql.Date date) {
        this(date.getTime());
    }
    
    public DateCalendar(java.sql.Date date, String dateFormat) {
        this(date.getTime(), dateFormat);
    }

    public DateCalendar(java.util.Date date) {
        this(date.getTime());
    }
    
    public DateCalendar(java.util.Date date, String dateFormat) {
        this(date.getTime(), dateFormat);
    }
    
    public DateCalendar(long millis) {
        this(millis, DEFAULT_DATETIME_FORMAT);
    }
    
    public DateCalendar(long millis, String dateFormat) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        setYear(c.get(Calendar.YEAR));
        setMonth(c.get(Calendar.MONTH)+1);
        setDay(c.get(Calendar.DATE));
        setHour(c.get(Calendar.HOUR_OF_DAY));
        setMinute(c.get(Calendar.MINUTE));
        setSecond(c.get(Calendar.SECOND));
        this.dateFormat = dateFormat;
    }
    
    public DateCalendar(int y, int mon, int d, int h, int min, int s, String dateFormat) {
        setYear(y);
        setMonth(mon);
        setDay(d);
        setHour(h);
        setMinute(min);
        setSecond(s);
        this.dateFormat = dateFormat;
    }
    
    public DateCalendar(int y, int m, int d) {
        this(y, m, d, 0, 0, 0, DEFAULT_DATE_FORMAT);
    }
    
    public DateCalendar(int y, int mon, int d, int h, int min, int s) {
        this(y, mon, d, h, min, s, DEFAULT_DATETIME_FORMAT);
    }
    
    public DateCalendar(int y, int dayIndex) {
        this(y, dayIndex, 0, 0, 0, DEFAULT_DATE_FORMAT);
    }
    
    public DateCalendar(int y, int dayIndex, int h, int min, int s) {
        this(y, dayIndex, h, min, s, DEFAULT_DATETIME_FORMAT);
    }
    
    public DateCalendar(int y, int dayIndex, int h, int min, int s, String dateFormat) {
        while (dayIndex < 1) {
            y--;
            if (isLeapYear(y)) {
                dayIndex += LEAP_YEAR_DAYS;
            } else {
                dayIndex += NORMAL_YEAR_DAYS;
            }
        }
        while (dayIndex > ((isLeapYear(y))? 366 : 365)) {
            y++;
            if (isLeapYear(y)) {
                dayIndex -= 366;
            } else {
                dayIndex -= 365;
            }
        }
        boolean isLeapYear = isLeapYear(y);
        setYear(y);
        int m = 0, d = 0;
        for (int i = 0; i < availableMonthdays.length; i++) {
            int days = availableMonthdays[i];
            if (i == 1 && isLeapYear) {
                days++;
            }
            if (dayIndex <= days) {
                m = i + 1;
                d = dayIndex;
                break;
            } else {
                dayIndex -= days;
            }
        }
        setMonth(m);
        setDay(d);
        setHour(h);
        setMinute(min);
        setSecond(s);
        this.dateFormat = dateFormat;
    }
    
    public DateCalendar(String dateString) throws NumberFormatException {
        this(dateString, DEFAULT_DATETIME_FORMAT);
    }
    
    public DateCalendar(String dateString, String format) throws NumberFormatException {
        setDate(dateString, format);
    }
    
    public static int getDayIndex(int y, int m, int d) {
        boolean isLeap = isLeapYear(y);
        return monthdays[m-1] + ((isLeap && m > 2)? 1 : 0) + d;
    }
    
    public int getDayIndex() {
        boolean isLeap = isLeapYear(year);
        return monthdays[month-1] + ((isLeap && month > 2)? 1 : 0) + day;
    }
    
    public void setDateFormat(String f) {
        if (f != null) {
            dateFormat = f;
        }
    }
    
    public void setYear(int y) {
        year = y;
    }
    
    public void setMonth(int m) {
        if (m > 12) {
            while (m > 12) {
                m -= 12;
                year++;
            }
        } else if (m < 1) {
            while (m < 1) {
                m += 12;
                year--;
            }
        }
        month = m;
    }
    
    public void setDay(int d) {
        int days = getAvailableMonthdays(year, month);
        if (d > days) {
            while (d > days) {
                d -= days;
                setMonth(month + 1);
                days = getAvailableMonthdays(year, month);
            }
        } else if (d < 1) {
            while (d < 1) {
                setMonth(month - 1);
                days = getAvailableMonthdays(year, month);
                d += days; 
            }
        }
        day = d;
    }
    
    public void setHour(int h) {
        while (h >= 24) {
            h -= 24;
            setDay(day + 1);
        }
        while (h < 0) {
            h += 24;
            setDay(day - 1);
        }
        hour = h;
    }
    
    public void setMinute(int m) {
        while (m >= 60) {
            m -= 60;
            setHour(hour + 1);
        }
        while (m < 0) {
            m += 60;
            setHour(hour - 1);
        }
        minute = m;
    }
    
    public void setSecond(int s) {
        while (s >= 60) {
            s -= 60;
            setMinute(minute + 1);
        }
        while (s < 0) {
            s += 60;
            setMinute(minute - 1);
        }
        second = s;
    }
    
    public void setDate(int yr, int mh, int dy) {
        setYear(yr);
        setMonth(mh);
        setDay(dy);
    }
    
    public void setDateTime(int yr, int mh, int dy, int hr, int mn, int sc) {
        setYear(yr);
        setMonth(mh);
        setDay(dy);
        setHour(hr);
        setMinute(mn);
        setSecond(sc);
    }
    
    public void setDate(String dateStr) {
        String[] dateToken = dateStr.split("-");
        if (dateToken.length != 3) {
            throw new NumberFormatException("Invalid date string: " + dateStr);
        }
        setYear(Integer.parseInt(dateToken[0]));
        setMonth(Integer.parseInt(dateToken[1]));
        setDay(Integer.parseInt(dateToken[2]));
    }
    
    public void setCurrentDate() {
        setYear(getCurrentYear());
        setMonth(getCurrentMonth());
        setDay(getCurrentDay());
    }
    
    private String scanDateString(CharacterQueue formatChars, CharacterQueue dateChars, char formatChar) {
        String str = "";
        Character f = formatChars.peekFirst();
        while (f != null && f.charValue() == formatChar) {
            formatChars.dequeue();
            Character d = dateChars.peekFirst();
            if (d != null && Character.isDigit(d.charValue())) {
                str += d.charValue();
                dateChars.dequeue();
            }
            f = formatChars.peekFirst();
        }
        return str;
    }
    
    public void setDate(String dateString, String format) throws NumberFormatException {
        dateFormat = format;
        if (dateString == null || dateString.equals("")) {
            throw new NumberFormatException();
        } else {
            CharacterQueue formatQueue = new CharacterQueue(format),
                dateQueue = new CharacterQueue(dateString);
            String yrStr = "", mhStr = "", dyStr = "", hrStr = "", minStr = "", scStr = "";
            
            while (!formatQueue.isEmpty() && !dateQueue.isEmpty()) {
                Character c = formatQueue.peekFirst();
                switch (c.charValue()) {
                    case YEAR_CHAR:
                        yrStr = scanDateString(formatQueue, dateQueue, YEAR_CHAR);
                        break;
                    case MONTH_CHAR:
                        mhStr = scanDateString(formatQueue, dateQueue, MONTH_CHAR);
                        break;
                    case DATE_CHAR:
                        dyStr = scanDateString(formatQueue, dateQueue, DATE_CHAR);
                        break;
                    case HOUR_CHAR:
                        hrStr = scanDateString(formatQueue, dateQueue, HOUR_CHAR);
                        break;
                    case MINUTE_CHAR:
                        minStr = scanDateString(formatQueue, dateQueue, MINUTE_CHAR);
                        break;
                    case SECOND_CHAR:
                        scStr = scanDateString(formatQueue, dateQueue, SECOND_CHAR);
                        break;
                    default:
                        formatQueue.dequeue();
                        dateQueue.dequeue();
                }
            }
            setYear(Integer.parseInt(yrStr, 10));
            setMonth(Integer.parseInt(mhStr, 10));
            setDay(Integer.parseInt(dyStr, 10));
            if (!hrStr.equals("")) {
                setHour(Integer.parseInt(hrStr, 10));
            } else {
                setHour(0);
            }
            if (!minStr.equals("")) {
                setMinute(Integer.parseInt(minStr, 10));
            } else {
                setMinute(0);
            }
            if (!scStr.equals("")) {
                setSecond(Integer.parseInt(scStr, 10));
            } else {
                setSecond(0);
            }
        }
    }
    
    public int getYear() {
        return year;
    }
    
    public int getMonth() {
        return month;
    }
    
    public int getDay() {
        return day;
    }
    
    public int getHour() {
        return hour;
    }
    
    public int getMinute() {
        return minute;
    }
    
    public int getSecond() {
        return second;
    }
    
    public static String toString(DateCalendar c) {
        if (c != null) {
            return c.toString();
        } else {
            return "";
        }
    }
    
    @Override
    public String toString() {
        char[] formatChars = dateFormat.toCharArray();
        char[] dateChars = new char[formatChars.length];
        String yearString = Integer.toString(year),
                monthString = Integer.toString(month),
                dateString = Integer.toString(day),
                hourString = Integer.toString(hour),
                minuteString = Integer.toString(minute),
                secondString = Integer.toString(second);
        int yearIndex = yearString.length() - 1,
                monthIndex = monthString.length() - 1,
                dateIndex = dateString.length() - 1,
                hourIndex = hourString.length() - 1,
                minuteIndex = minuteString.length() - 1,
                secondIndex = secondString.length() - 1;
        for (int i = formatChars.length - 1; i >= 0; i--) {
            switch (formatChars[i]) {
                case YEAR_CHAR:
                    if (yearIndex >= 0) {
                        dateChars[i] = yearString.charAt(yearIndex--);
                    } else {
                        dateChars[i] = '0';
                    }
                    break;
                case MONTH_CHAR:
                    if (monthIndex >= 0) {
                        dateChars[i] = monthString.charAt(monthIndex--);
                    } else {
                        dateChars[i] = '0';
                    }
                    break;
                case DATE_CHAR:
                    if (dateIndex >= 0) {
                        dateChars[i] = dateString.charAt(dateIndex--);
                    } else {
                        dateChars[i] = '0';
                    }
                    break;
                case HOUR_CHAR:
                    if (hourIndex >= 0) {
                        dateChars[i] = hourString.charAt(hourIndex--);
                    } else {
                        dateChars[i] = '0';
                    }
                    break;
                case MINUTE_CHAR:
                    if (minuteIndex >= 0) {
                        dateChars[i] = minuteString.charAt(minuteIndex--);
                    } else {
                        dateChars[i] = '0';
                    }
                    break;
                case SECOND_CHAR:
                    if (secondIndex >= 0) {
                        dateChars[i] = secondString.charAt(secondIndex--);
                    } else {
                        dateChars[i] = '0';
                    }
                    break;
                default:
                    dateChars[i] = formatChars[i];
            }
        }
        return new String(dateChars);
    }
    
    @Override
    public int compareTo(DateCalendar cal) {
        if (cal == null) {
            return 1;
        }
        int totalDays = getTotalDays();
        int calTotalDays = cal.getTotalDays();
        
        if (totalDays > calTotalDays) {
            return 1;
        } else if (totalDays < calTotalDays) {
            return -1;
        } else {
            return 0;
        }
    }
    
    public int getTotalDays() {
        int numberOfLeapYears = containedLeapYears(year);
        int totalDays = getDayIndex() + year * 365 + numberOfLeapYears;
        return totalDays;
    }
    
    public int getDiffDays(DateCalendar c) {
        return getTotalDays() - c.getTotalDays();
    }
    
    public static int getDiffDays(DateCalendar c1, DateCalendar c2) {
        return c1.getTotalDays() - c2.getTotalDays();
    }

    public static int compare(DateCalendar c1, DateCalendar c2) {
        return c1.compareTo(c2);
    }

    public static int compare(Date d1, Date d2) {
        return new DateCalendar(d1).compareTo(new DateCalendar(d2));
    }

    public static int getTotalDays(Date date) {
        return new DateCalendar(date).getTotalDays();
    }

    public static int getTotalDays(int y, int m, int d) {
        int numberOfLeapYears = containedLeapYears(y);
        int total = getDayIndex(y, m, d) + y * 365 + numberOfLeapYears;
        return total;
    }
    
    public static int getCurrentYear() {
        return java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
    }
    
    public static int getCurrentMonth() {
        return java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1;
    }
    
    public static int getCurrentDay() {
        return java.util.Calendar.getInstance().get(java.util.Calendar.DATE);
    }
    
    public static int getCurrentHour() {
        return java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
    }
    
    public static int getCurrentMinute() {
        return java.util.Calendar.getInstance().get(java.util.Calendar.MINUTE);
    }
    
    public static int getCurrentSecond() {
        return java.util.Calendar.getInstance().get(java.util.Calendar.SECOND);
    }
    
    public static String getCurrentDateString() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        return cal.get(java.util.Calendar.YEAR) + "-" + 
                (cal.get(java.util.Calendar.MONTH) + 1) + "-" + 
                cal.get(java.util.Calendar.DATE);
    }
    
    public static String getCurrentDateTimeString() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        return cal.get(java.util.Calendar.YEAR) + "-" +
            (cal.get(java.util.Calendar.MONTH) + 1) + "-" +
            cal.get(java.util.Calendar.DATE) + " " + 
            cal.get(java.util.Calendar.HOUR_OF_DAY) + ":" +
            cal.get(java.util.Calendar.MINUTE) + ":" +
            cal.get(java.util.Calendar.SECOND);
    }
    
    public static DateCalendar getCurrentDate() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        return new DateCalendar(cal.get(java.util.Calendar.YEAR), 
                (cal.get(java.util.Calendar.MONTH) + 1),
                cal.get(java.util.Calendar.DATE));
    }
    
    public static DateCalendar getCurrentDateTime() {
        return new DateCalendar(java.util.Calendar.getInstance().getTime());
    }
    
    public static int getFirstWeekDay(int year, int month) {
        set(year, month);
        return calendar.get(GregorianCalendar.DAY_OF_WEEK);
    }
    
    /*public static boolean isLeapYear(int year) {
        return calendar.isLeapYear(year);
    }*/
    
    public static boolean isLeapYear(int y) {
        return (y % 4 == 0) && (y % 100 != 0) || (y % 400 == 0);
    }
    
    public static int containedLeapYears(int y) {
        return (y / 4) + (y / 400) - (y / 100);
    }
    
    public static int getWeekDay(int year, int month, int day) {
        set(year, month, day, 0, 0, 0);
        return calendar.get(GregorianCalendar.DAY_OF_WEEK);
    }
    
    public static void set(int y, int mon, int d, int h, int min, int s) {
        calendar.set(GregorianCalendar.YEAR, y);
        calendar.set(GregorianCalendar.MONTH, (mon - 1) % 12);
        calendar.set(GregorianCalendar.DATE, d);
        calendar.set(GregorianCalendar.HOUR_OF_DAY, h);
        calendar.set(GregorianCalendar.MINUTE, min);
        calendar.set(GregorianCalendar.SECOND, s);
    }
    
    public static void set(int y, int m) {
        set(y, m, 1, 0, 0, 0);
    }
    
    public static void set(int y) {
        set(y, 1, 1, 0, 0, 0);
    }
    
    public static int getAvailableMonthdays(int year, int month) {
        return getAvailableMonthdays(month, isLeapYear(year));
    }
    
    public static int getAvailableMonthdays(int month, boolean isLeapYear) {
        if (month < 1 && month > 12) {
            return -1;
        } else {
            if (isLeapYear && month == 2) {
                return availableMonthdays[month - 1] + 1;
            } else {
                return availableMonthdays[month - 1];
            }
        }
    }
    
    public static int getRandomYear(int diff) {
        int currentYear = DateCalendar.getCurrentYear();
        return MathUtil.randomInteger(currentYear - diff, currentYear + diff);
    }
    
    public static int getRandomYear() {
        return getRandomYear(50);
    }
    
    public static int getRandomMonth() {
        return MathUtil.randomInteger(1, 12);
    }
    
    public static int getRandomDate(int year, int month) {
        return getRandomDate(getAvailableMonthdays(year, month));
    }
    
    public static int getRandomDate(int maxDate) {
        return MathUtil.randomInteger(1, maxDate);
    }
    
    public static int getRandomDate() {
        return getRandomDate(30);
    }
    
    public static int getRandomHour() {
        return MathUtil.randomInteger(0, 23);
    }
    
    public static int getRandomMinute() {
        return MathUtil.randomInteger(0, 59);
    }
    
    public static int getRandomSecond() {
        return MathUtil.randomInteger(0, 59);
    }
    
    public static DateCalendar getRandomCalendarDate() {
        int year = DateCalendar.getRandomYear();
        int month = DateCalendar.getRandomMonth();
        int date = getRandomDate(year, month);
        return new DateCalendar(year, month, date);
    }
    
    public static DateCalendar getRandomCalendarDateTime() {
        int year = getRandomYear(), month = getRandomMonth(), date = getRandomDate(year, month),
            hour = getRandomHour(), minute = getRandomMinute(), second = getRandomSecond();
        return new DateCalendar(year, month, date, hour, minute, second);
    }
    
    public static boolean isValidDateString(String str) {
        return isValidDateString(str, DEFAULT_DATE_FORMAT);
    }
    
    public static boolean isValidDateTimeString(String str) {
        return isValidDateString(str, DEFAULT_DATETIME_FORMAT);
    }
    
    public static boolean isValidDateString(String str, String dateFormat) {
        if (str == null || str.equals("")) {
            return false;
        }
        char[] formatChars = dateFormat.toCharArray();
        char[] chars = str.toCharArray();
        int i = formatChars.length - 1, ci = chars.length - 1, 
            dP = 0, mP = 0, yP = 0, hP = 0, minP = 0, sP = 0,
            d = 0, m = 0, y = 0, h = 0, min = 0, s = 0;
        while (i >= 0) {
            char fc = formatChars[i--], c = chars[ci];
            switch (fc) {
                case YEAR_CHAR:
                    if (Character.isDigit(c)) {
                        y += Character.getNumericValue(c) * Math.pow(10, yP++);
                        ci--;
                    }
                    break;
                case MONTH_CHAR:
                    if (Character.isDigit(c)) {
                        m += Character.getNumericValue(c) * Math.pow(10, mP++);
                        ci--;
                    }
                    break;
                case DATE_CHAR:
                    if (Character.isDigit(c)) {
                        d += Character.getNumericValue(c) * Math.pow(10, dP++);
                        ci--;
                    }
                    break;
                case HOUR_CHAR:
                    if (Character.isDigit(c)) {
                        h += Character.getNumericValue(c) * Math.pow(10, hP++);
                        ci--;
                    }
                    break;
                case MINUTE_CHAR:
                    if (Character.isDigit(c)) {
                        min += Character.getNumericValue(c) * Math.pow(10, minP++);
                        ci--;
                    }
                    break;
                case SECOND_CHAR:
                    if (Character.isDigit(c)) {
                        s += Character.getNumericValue(c) * Math.pow(10, sP++);
                        ci--;
                    }
                    break;
                default:
                    if (fc != c) {
                        return false;
                    } else {
                        ci--;
                    }
            }
        }
        return (
                m > 0 && m <= 12 && d > 0 && 
                d <= ((isLeapYear(y) && m == 2)? availableMonthdays[m-1]+1 : availableMonthdays[m-1]) &&
                h >= 0 && h < 60 && min >= 0 && min < 60 && s >= 0 && s < 60
        );
    }
    
    public int getWeekDay() {
        /*int totalDays = getTotalDays();
        return (totalDays + 5) % 7 + 1;
        */
        return getWeekDay(year, month, day);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DateCalendar) {
            return getTimeMillis() == ((DateCalendar)o).getTimeMillis();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return getCurrentDateTimeString().hashCode();
    }
    
    public long getTimeMillis() {
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, day, hour, minute, second);
        return (long)(c.getTimeInMillis() / 1000) * 1000;
    }
    
    public java.sql.Date getSqlDate() {
        return new java.sql.Date(getTimeMillis());
    }
}
