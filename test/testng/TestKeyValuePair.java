package testng;

import static org.testng.Assert.*;

import org.testng.annotations.*;
import utils.data.KeyValuePair;

public class TestKeyValuePair {
    public TestKeyValuePair() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testKeyValuePair() {
        KeyValuePair<String, String> pair1 = new KeyValuePair<String, String>("", "");
        KeyValuePair<String, Double> pair2 = new KeyValuePair<String, Double>("aa", new Double(596.33215));

        assertTrue(new KeyValuePair("", "").equals(pair1));
        assertTrue(
            new KeyValuePair<String, Double>("aa", new Double(596.33215)).equals(pair2)
        );
        KeyValuePair<String, String> pair3 = new KeyValuePair<String, String>("null", null);

        assertTrue(pair3.getValue() == null);
        assertTrue(pair3.equals(new KeyValuePair("null", null)));
    }
}