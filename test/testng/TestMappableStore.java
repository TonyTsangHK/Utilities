package testng;

import static org.testng.Assert.*;

import data.TestMappableModel;
import org.testng.annotations.Test;
import utils.data.DataManipulator;
import utils.data.query.DefaultMappableIndexComparator;
import utils.data.query.MappableIndexComparator;
import utils.data.query.MappableStore;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2014-06-30
 * Time: 10:50
 */
public class TestMappableStore {
    @Test
    public void testMappableStoreNotSupportedExceptionWithoutIndexComparator() {
        MappableStore<TestMappableModel, String, Object> mappableStore = new MappableStore();

        mappableStore.add(new TestMappableModel("s1", 1, new BigDecimal("10")));

        try {
            mappableStore.binarySearch("stringValue", "s1");
            throw new IndexOutOfBoundsException("Just a test");
            //fail("Expected UnsupportedOperationException but none is thrown!");
        } catch (UnsupportedOperationException uoe) {
            // Pass
        } catch (Exception e) {
            fail("Expected UnsupportedOperationException but found [" + e.getClass().getName() + "]", e);
        }

        try {
            mappableStore.binarySearch(DataManipulator.<String, Object>createSimpleMap("stringValue", "s1"));
            fail("Expected UnsupportedOperationException but none is thrown!");
        } catch (UnsupportedOperationException uoe) {
            // Pass
        } catch (Exception e) {
            fail("Expected UnsupportedOperationException but found [" + e.getClass().getName() + "]", e);
        }
    }

    @Test
    public void testMappableIndexComparatorUsageKeys() {
        MappableIndexComparator<String, Object> indexComparator =
            new DefaultMappableIndexComparator<String, Object>("i", "b", "v");

        assertTrue(indexComparator.usableForKey("i"), "Usable for key [i] is expected, but result is not!");
        assertFalse(indexComparator.usableForKey("b"), "Not usable for key [b] is expected, but result is not!");

        assertTrue(
            indexComparator.usableForKeys(DataManipulator.createList("i", "b", "v")) &&
            indexComparator.usableForKeys(DataManipulator.createList("i", "b"))
        );
        assertTrue(
            indexComparator.usableForKeys(DataManipulator.createList("v", "i", "b")) &&
            indexComparator.usableForKeys(DataManipulator.createList("b", "i")),
            "Order should not matter, as long as it match the first few element keys!"
        );
    }

    @Test
    public void testMappableAddWithIndexComparator() {
        MappableStore<TestMappableModel, String, Object> mappableStore = createOrderedMappableStore();

        MappableIndexComparator indexComparator = mappableStore.getIndexComparator();

        assertNotNull(indexComparator, "MappableIndexComparator should not be null");

        assertTrue(mappableStore.size() > 0, "Store should not be empty!");

        for (int i = 1; i < mappableStore.size(); i++) {
            TestMappableModel prevMappable = mappableStore.get(i-1), currMappable = mappableStore.get(i);

            assertTrue(
                indexComparator.compare(prevMappable, currMappable) <= 0,
                "Previouis element ["+prevMappable.toString()+"] " +
                    "should be smaller or equals to " +
                "current element ["+currMappable+"]."
            );
        }
    }

    private boolean isOrdered(
        MappableStore<TestMappableModel, String, Object> mappableStore, MappableIndexComparator<String, Object> indexComparator
    ) {
        boolean ordered = true;

        for (int i = 1; i < mappableStore.size(); i++) {
            TestMappableModel prevMappable = mappableStore.get(i-1), currMappable = mappableStore.get(i);

            if (indexComparator.compare(prevMappable, currMappable) > 0) {
                ordered = false;
                break;
            }
        }

        return ordered;
    }

    @Test
    public void testDefferedSorting() {
        MappableStore<TestMappableModel, String, Object> mappableStore = createInitialUnorderedMappableStore();

        MappableIndexComparator<String, Object> indexComparator = createMappableIndexComparator();

        assertNull(mappableStore.getIndexComparator(), "MappableIndexComparator should be null");

        assertTrue(mappableStore.size() > 0, "Store should not be empty!");

        boolean ordered;

        ordered = isOrdered(mappableStore, indexComparator);

        assertFalse(ordered, "Store element should not be ordered for testing purpose!");

        mappableStore.setIndexComparator(indexComparator);

        ordered = isOrdered(mappableStore, indexComparator);

        assertTrue(ordered, "Store element should be ordered after setting the index comparator");
    }

    @Test
    public void testBinarySearchRandomAccess() {
        MappableStore<TestMappableModel, String, Object> mappableStore = createOrderedMappableStore();

        int existIndex;

        existIndex = mappableStore.binarySearch("integerValue", 2);

        assertTrue(existIndex >= 0, "Expected integerValue: 2 exist in store!");

        existIndex = mappableStore.binarySearch(
            DataManipulator.createMap(
                Arrays.asList("integerValue", "bigDecimalValue"),
                Arrays.<Object>asList(2, new BigDecimal("10"))
            )
        );

        assertTrue(existIndex >= 0, "Expected integerValue: 2, bigDecimalValue: 10 exist in store!");

        int nonExistIndex;

        nonExistIndex = mappableStore.binarySearch("integerValue", 100);

        assertTrue(nonExistIndex < 0, "Expected integerValue: 100 not exist in store!");

        nonExistIndex = mappableStore.binarySearch(
            DataManipulator.createMap(
                Arrays.asList("integerValue", "bigDecimalValue"),
                Arrays.<Object>asList(2, new BigDecimal("99"))
            )
        );

        assertTrue(nonExistIndex < 0, "Expected integerValue: 2, bigDecimalValue: 99 not exist in store!");
    }

    @Test
    public void testQuery() {
        MappableStore<TestMappableModel, String, Object> mappableStore = createOrderedMappableStore();

        List<TestMappableModel> results;

        results = mappableStore.query("integerValue", 2);

        assertEquals(results.size(), 6, "Expected size is 6, but actual is: ["+results.size()+"]!");

        results = mappableStore.query("integerValue", 100);

        assertEquals(results.size(), 0, "Expected empty results, but actual is ["+results.size()+"]");

        // Test for sample map
        results = mappableStore.query(
            DataManipulator.createMap(
                Arrays.asList("integerValue", "bigDecimalValue"),
                Arrays.<Object>asList(2, new BigDecimal("20"))
            )
        );

        assertEquals(results.size(), 2, "Expected size is 6, but actual is: ["+results.size()+"]!");

        results = mappableStore.query(
            DataManipulator.createMap(
                Arrays.asList("integerValue", "bigDecimalValue"),
                Arrays.<Object>asList(100, new BigDecimal("20"))
            )
        );

        assertEquals(results.size(), 0, "Expected empty results, but actual is ["+results.size()+"]");
    }

    private MappableIndexComparator<String, Object> createMappableIndexComparator() {
        return new DefaultMappableIndexComparator<String, Object>("integerValue", "bigDecimalValue");
    }

    private MappableStore<TestMappableModel, String, Object> createOrderedMappableStore() {
        MappableStore<TestMappableModel, String, Object> mappableStore =
            new MappableStore<TestMappableModel, String, Object>(
            createMappableIndexComparator()
        );

        mappableStore.add(new TestMappableModel("2, 20", 2, new BigDecimal("20")));
        mappableStore.add(new TestMappableModel("1, 10", 1, new BigDecimal("10")));
        mappableStore.add(new TestMappableModel("3, 30", 3, new BigDecimal("30")));
        mappableStore.add(new TestMappableModel("2, 10", 2, new BigDecimal("10")));
        mappableStore.add(new TestMappableModel("2, 30", 2, new BigDecimal("30")));
        mappableStore.add(new TestMappableModel("1, 30", 1, new BigDecimal("30")));
        mappableStore.add(new TestMappableModel("3, 10", 3, new BigDecimal("10")));
        mappableStore.add(new TestMappableModel("1, 20", 1, new BigDecimal("20")));
        mappableStore.add(new TestMappableModel("3, 20", 3, new BigDecimal("20")));

        mappableStore.add(new TestMappableModel("2, 20", 2, new BigDecimal("20")));
        mappableStore.add(new TestMappableModel("1, 10", 1, new BigDecimal("10")));
        mappableStore.add(new TestMappableModel("3, 30", 3, new BigDecimal("30")));
        mappableStore.add(new TestMappableModel("2, 10", 2, new BigDecimal("10")));
        mappableStore.add(new TestMappableModel("2, 30", 2, new BigDecimal("30")));
        mappableStore.add(new TestMappableModel("1, 30", 1, new BigDecimal("30")));
        mappableStore.add(new TestMappableModel("3, 10", 3, new BigDecimal("10")));
        mappableStore.add(new TestMappableModel("1, 20", 1, new BigDecimal("20")));
        mappableStore.add(new TestMappableModel("3, 20", 3, new BigDecimal("20")));

        return mappableStore;
    }

    private MappableStore<TestMappableModel, String, Object> createInitialUnorderedMappableStore() {
        MappableStore<TestMappableModel, String, Object> mappableStore =
            new MappableStore<TestMappableModel, String, Object>();

        mappableStore.add(new TestMappableModel("2, 20", 2, new BigDecimal("20")));
        mappableStore.add(new TestMappableModel("1, 10", 1, new BigDecimal("10")));
        mappableStore.add(new TestMappableModel("3, 30", 3, new BigDecimal("30")));
        mappableStore.add(new TestMappableModel("2, 10", 2, new BigDecimal("10")));
        mappableStore.add(new TestMappableModel("2, 30", 2, new BigDecimal("30")));
        mappableStore.add(new TestMappableModel("1, 30", 1, new BigDecimal("30")));
        mappableStore.add(new TestMappableModel("3, 10", 3, new BigDecimal("10")));
        mappableStore.add(new TestMappableModel("1, 20", 1, new BigDecimal("20")));
        mappableStore.add(new TestMappableModel("3, 20", 3, new BigDecimal("20")));

        mappableStore.add(new TestMappableModel("2, 20", 2, new BigDecimal("20")));
        mappableStore.add(new TestMappableModel("1, 10", 1, new BigDecimal("10")));
        mappableStore.add(new TestMappableModel("3, 30", 3, new BigDecimal("30")));
        mappableStore.add(new TestMappableModel("2, 10", 2, new BigDecimal("10")));
        mappableStore.add(new TestMappableModel("2, 30", 2, new BigDecimal("30")));
        mappableStore.add(new TestMappableModel("1, 30", 1, new BigDecimal("30")));
        mappableStore.add(new TestMappableModel("3, 10", 3, new BigDecimal("10")));
        mappableStore.add(new TestMappableModel("1, 20", 1, new BigDecimal("20")));
        mappableStore.add(new TestMappableModel("3, 20", 3, new BigDecimal("20")));

        return mappableStore;
    }
}
