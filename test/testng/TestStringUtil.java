package testng;

import static org.testng.Assert.*;

import org.testng.annotations.Test;
import utils.string.StringUtil;

public class TestStringUtil {
    @Test
    public void TestAddQuote() {
        assertEquals(StringUtil.sqlAddQuote("''"), "''''");
        assertEquals(StringUtil.sqlAddQuote("James' watch"), "James'' watch");
    }
}
