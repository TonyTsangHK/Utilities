package testng;

import org.testng.annotations.Test;
import utils.string.StringUtil;

import static org.testng.Assert.assertEquals;

public class TestStringUtil {
    @Test
    public void TestAddQuote() {
        assertEquals(StringUtil.sqlAddQuote("''"), "''''");
        assertEquals(StringUtil.sqlAddQuote("James' watch"), "James'' watch");
    }
}
