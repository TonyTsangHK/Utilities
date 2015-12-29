package testng;

import static org.testng.Assert.*;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.date.*;

public class TestDateCalendar {
    private String tt1, tt2, tt3, tt4, tt5, tt6, tt7, tt8; /*, tt9;*/
    private String tf1, tf2, tf3, tf4, tf5; /*, tf6, tf7, tf8, tf9;*/
    
    private String ds1, ds2, df1, df2;
    
    @BeforeMethod
    public void setUp() {
        ds1 = "2008-2-1 10:59:52";
        ds2 = "2008/2/29 2.12.33";
        
        df1 = "yyyy-MM-dd HH:mm:ss";
        df2 = "yyyy/MM/dd HH.mm.ss";
        
        tt1 = "2007-01-01";
        tt2 = "2007-1-1";
        tt3 = "2007-1-01";
        tt4 = "2007-01-1";
        tt5 = "2007-2-28";
        tt6 = "2008-2-29";
        tt7 = "2007-12-31";
        tt8 = "2007-01-31";
        
        tf1 = "2007-2-29";
        tf2 = "2007-12-32";
        tf3 = "2007-01-00";
        tf4 = "2007-1-0";
        tf5 = "2007/06/06";
    }

    @Test
    public void testContainedLeadYear() {
        int testUpToYear = 4000;

        for (int i = 1; i <= testUpToYear; i++) {
            int leapYears = 0, actualLeapYears = DateCalendar.containedLeapYears(i);
            for (int j = 1; j <= i; j++) {
                if (DateCalendar.isLeapYear(j)) {
                    leapYears++;
                }
            }

            assertEquals(
                actualLeapYears, leapYears,
                "TestYear: " + i + ", expected: " + leapYears + ", actual: " + actualLeapYears
            );
        }
    }
    
    @Test
    public void testDf1() {
        DateCalendar c = new DateCalendar(ds1, df1);
        assertEquals(c.getYear(), 2008);
        assertEquals(c.getMonth(), 2);
        assertEquals(c.getDay(), 1);
        assertEquals(c.getHour(), 10);
        assertEquals(c.getMinute(), 59);
        assertEquals(c.getSecond(), 52);
    }
    
    @Test
    public void testDf2() {
        DateCalendar c = new DateCalendar(ds2, df2);
        assertEquals(c.getYear(), 2008);
        assertEquals(c.getMonth(), 2);
        assertEquals(c.getDay(), 29);
        assertEquals(c.getHour(), 2);
        assertEquals(c.getMinute(), 12);
        assertEquals(c.getSecond(), 33);
    }
    
    @Test
    public void tt1() {
        assertEquals(DateCalendar.isValidDateString(tt1), true, tt1);
    }
    
    @Test
    public void tt2() {
        assertEquals(DateCalendar.isValidDateString(tt2), true, tt2);
    }
    
    @Test
    public void tt3() {
        assertEquals(DateCalendar.isValidDateString(tt3), true, tt3);
    }
    
    @Test
    public void tt4() {
        assertEquals(DateCalendar.isValidDateString(tt4), true, tt4);
    }
    
    @Test
    public void tt5() {
        assertEquals(DateCalendar.isValidDateString(tt5), true, tt5);
    }
    
    @Test
    public void tt6() {
        assertEquals(DateCalendar.isValidDateString(tt6), true, tt6);
    }
    
    @Test
    public void tt7() {
        assertEquals(DateCalendar.isValidDateString(tt7), true, tt7);
    }
    
    @Test
    public void tt8() {
        assertTrue(DateCalendar.isValidDateString(tt8), tt8);
    }
    
    @Test
    public void tf1() {
        assertFalse(DateCalendar.isValidDateString(tf1), tf1);
    }
    
    @Test
    public void tf2() {
        assertFalse(DateCalendar.isValidDateString(tf2), tf2);
    }
    
    @Test
    public void tf3() {
        assertFalse(DateCalendar.isValidDateString(tf1), tf3);
    }
    
    @Test
    public void tf4() {
        assertFalse(DateCalendar.isValidDateString(tf4), tf4);
    }
    
    @Test
    public void tf5() {
        assertFalse(DateCalendar.isValidDateString(tf5), tf5);
    }
    
    @Test
    public void tformat1() {
        assertTrue(DateCalendar.isValidDateString(tf5, "yyyy/MM/dd"), tf5);
    }
}
