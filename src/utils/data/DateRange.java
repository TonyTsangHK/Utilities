package utils.data;

import utils.date.SimpleDateUtils;
import utils.string.FormatUtils;

import java.util.Date;

public class DateRange {
    private Date fromDate, toDate;
        
    public DateRange(Date fromDate, Date toDate) {
        if (fromDate != null && toDate != null && fromDate.compareTo(toDate) <= 0) {
            this.fromDate = fromDate;
            this.toDate = toDate;
        } else {
            throw new IllegalArgumentException(
                "Illegal argument for date range, fromDate: " +
                    FormatUtils.formatDate(fromDate) + ", toDate: " +
                    FormatUtils.formatDate(toDate)
            );
        }
    }
    
    public Date getFromDate() {
        return fromDate;
    }
    
    public Date getToDate() {
        return toDate;
    }

    public boolean contains(Date date) {
        return fromDate.compareTo(date) <= 0 && toDate.compareTo(date) >= 0;
    }

    /**
     * Compare this date range with input date
     *
     * @param date input date
     * @return -1 for date greater than toDate, 1 for date smaller than fromDate, 0 for within fromDate & toDate
     *
     */
    public int compareTo(Date date) {
        if (fromDate.compareTo(date) > 0) {
            return 1;
        } else if (toDate.compareTo(date) < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return toString(true);
    }

    public String toString(boolean trimCommonPart) {
        if (fromDate.compareTo(toDate) == 0) {
            return FormatUtils.formatDate(fromDate);
        } else {
            String s = FormatUtils.formatDate(fromDate), e = FormatUtils.formatDate(toDate);

            if (trimCommonPart) {
                int sy = SimpleDateUtils.getYear(fromDate), sm = SimpleDateUtils.getMonth(fromDate),
                    ey = SimpleDateUtils.getYear(toDate), em = SimpleDateUtils.getMonth(toDate);

                if (sy == ey && sm == em) {
                    return s + "~" + e.substring(8);
                } else if (sy == ey) {
                    return s + "~" + e.substring(5);
                } else {
                    return s + "~" + e;
                }
            } else {
                return s + "~" + e;
            }
        }
    }
}
