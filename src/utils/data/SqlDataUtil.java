package utils.data;

import java.sql.*;

import utils.date.*;

public class SqlDataUtil {
    public static void setDateParameter(PreparedStatement pStat, int index, DateCalendar date, boolean allowNull, DateCalendar defaultDate) throws SQLException {
        if (date == null) {
            if (allowNull || defaultDate == null) {
                pStat.setNull(index, Types.DATE);
            } else {
                pStat.setDate(index, defaultDate.getSqlDate());
            }
        } else {
            pStat.setDate(index, date.getSqlDate());
        }
    }
}
