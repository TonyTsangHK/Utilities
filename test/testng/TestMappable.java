package testng;

import static org.testng.Assert.*;

import data.TestMappableModel;
import org.testng.annotations.*;
import utils.data.DataComparator;
import utils.data.DataManipulator;
import utils.data.Mappable;
import utils.data.query.*;
import utils.math.MathUtil;
import utils.string.StringUtil;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2014-06-23
 * Time: 10:30
 */
public class TestMappable {
    private TestMappableModel testMappable;

    private List<TestMappableModel> mappables, randomMappables;

    private String randomMatchingQueryStringValue, randomMismatchingQueryStringValue;
    private int randomMatchingQueryIntegerValue, randomMismatchingQueryIntegerValue;
    private BigDecimal randomMatchingQueryBigDecimalValue, randomMismatchingQueryBigDecimalValue;

    @BeforeClass
    public void setupValues() {
        testMappable = new TestMappableModel("", 0, BigDecimal.ZERO);

        setupGeneralMappables();

        setupRandomMappables();
    }

    private void setupRandomMappables() {
        int randomMappableSize = 1000;

        randomMappables = new ArrayList<TestMappableModel>();

        randomMismatchingQueryStringValue = StringUtil.getRandomString(10);
        randomMismatchingQueryIntegerValue = MathUtil.randomInteger(10, 10000);
        randomMismatchingQueryBigDecimalValue = new BigDecimal(MathUtil.randomNumber(10, 10000));

        for (int i = 0; i < randomMappableSize; i++) {
            String sv = StringUtil.getRandomString(10);
            int iv = MathUtil.randomInteger(10, 10000);
            BigDecimal bv = new BigDecimal(MathUtil.randomNumber(10, 10000));

            while (sv.equals(randomMismatchingQueryStringValue)) {
                sv = StringUtil.getRandomString(10);
            }

            while (iv == randomMismatchingQueryIntegerValue) {
                iv = MathUtil.randomInteger(10, 10000);
            }

            while (bv.compareTo(randomMismatchingQueryBigDecimalValue) == 0) {
                bv = new BigDecimal(MathUtil.randomNumber(10, 10000));
            }

            randomMappables.add(new TestMappableModel(sv, iv, bv));
        }

        randomMatchingQueryStringValue =
            randomMappables.get(MathUtil.randomInteger(0, randomMappables.size()-1)).getStringValue();
        randomMatchingQueryIntegerValue =
            randomMappables.get(MathUtil.randomInteger(0, randomMappables.size()-1)).getIntegerValue();
        randomMatchingQueryBigDecimalValue =
            randomMappables.get(MathUtil.randomInteger(0, randomMappables.size()-1)).getBigDecimalValue();
    }

    private void verifyOrderedMappables(Collection<TestMappableModel> mappables, Comparator<TestMappableModel> comparator) {
        Iterator<TestMappableModel> mappableIterator = mappables.iterator();

        TestMappableModel previous = mappableIterator.next(), current;

        while (mappableIterator.hasNext()) {
            current = mappableIterator.next();

            assertTrue(
                comparator.compare(previous, current) <= 0,
                "Previous element should be smaller or equals to current element, " +
                "[prev, s: "+previous.getStringValue()+", i: "+previous.getIntegerValue()+", b: "+previous.getBigDecimalValue()+"] " +
                "[current, s: "+current.getStringValue()+", i: "+current.getIntegerValue()+", b: "+current.getBigDecimalValue()+"]"
            );
        }
    }

    private void setupGeneralMappables() {
        mappables = new ArrayList<TestMappableModel>();
        mappables.add(new TestMappableModel("abc", 1, new BigDecimal("10.10")));
        mappables.add(new TestMappableModel("abc", 1, new BigDecimal("11.11")));
        mappables.add(new TestMappableModel("abc", 1, new BigDecimal("12.12")));

        mappables.add(new TestMappableModel("abc", 2, new BigDecimal("10.10")));
        mappables.add(new TestMappableModel("abc", 2, new BigDecimal("11.11")));
        mappables.add(new TestMappableModel("abc", 2, new BigDecimal("12.12")));

        mappables.add(new TestMappableModel("abc", 3, new BigDecimal("10.10")));
        mappables.add(new TestMappableModel("abc", 3, new BigDecimal("11.11")));
        mappables.add(new TestMappableModel("abc", 3, new BigDecimal("12.12")));

        mappables.add(new TestMappableModel("bcd", 1, new BigDecimal("10.10")));
        mappables.add(new TestMappableModel("bcd", 1, new BigDecimal("11.11")));
        mappables.add(new TestMappableModel("bcd", 1, new BigDecimal("12.12")));

        mappables.add(new TestMappableModel("bcd", 2, new BigDecimal("10.10")));
        mappables.add(new TestMappableModel("bcd", 2, new BigDecimal("11.11")));
        mappables.add(new TestMappableModel("bcd", 2, new BigDecimal("12.12")));

        mappables.add(new TestMappableModel("bcd", 3, new BigDecimal("10.10")));
        mappables.add(new TestMappableModel("bcd", 3, new BigDecimal("11.11")));
        mappables.add(new TestMappableModel("bcd", 3, new BigDecimal("12.12")));

        mappables.add(new TestMappableModel("cde", 1, new BigDecimal("10.10")));
        mappables.add(new TestMappableModel("cde", 1, new BigDecimal("11.11")));
        mappables.add(new TestMappableModel("cde", 1, new BigDecimal("12.12")));

        mappables.add(new TestMappableModel("cde", 2, new BigDecimal("10.10")));
        mappables.add(new TestMappableModel("cde", 2, new BigDecimal("11.11")));
        mappables.add(new TestMappableModel("cde", 2, new BigDecimal("12.12")));

        mappables.add(new TestMappableModel("cde", 3, new BigDecimal("10.10")));
        mappables.add(new TestMappableModel("cde", 3, new BigDecimal("11.11")));
        mappables.add(new TestMappableModel("cde", 3, new BigDecimal("12.12")));
    }

    @AfterClass
    public void clearValues() {
        testMappable = null;
        mappables = null;
    }

    @Test(groups = {"simple"})
    public void testGetSet() {
        assertEquals(testMappable.getAsMapValue("stringValue"), "", "stringValue should be empty [\"\"]");
        assertEquals(testMappable.getAsMapValue("integerValue"), 0, "integerValue should be [0]");
        assertEquals(testMappable.getAsMapValue("bigDecimalValue"), BigDecimal.ZERO, "bigDecimal should be [0]");

        testMappable.putAsMapValue("stringValue", "new value");
        testMappable.putAsMapValue("integerValue", 10);
        testMappable.putAsMapValue("bigDecimalValue", MathUtil.SIXTEEN);

        assertNotEquals("", testMappable.getAsMapValue("stringValue"), "stringValue is no longer empty");
        assertNotEquals(0, testMappable.getAsMapValue("integerValue"), "integerValue is no longer zero");
        assertNotEquals(BigDecimal.ZERO, testMappable.getAsMapValue("bigDecimalValue"), "bigDecimalValue is no longer zero");

        assertEquals(testMappable.getAsMapValue("stringValue"), "new value", "stringValue should be empty [\"\"]");
        assertEquals(testMappable.getAsMapValue("integerValue"), 10, "integerValue should be [0]");
        assertEquals(testMappable.getAsMapValue("bigDecimalValue"), MathUtil.SIXTEEN, "bigDecimal should be [0]");

        assertEquals(testMappable.getStringValue(), testMappable.getAsMapValue("stringValue"), "stringValue should match");
        assertEquals(testMappable.getIntegerValue(), testMappable.getAsMapValue("integerValue"), "integerValue should match");
        assertEquals(testMappable.getBigDecimalValue(), testMappable.getAsMapValue("bigDecimalValue"), "bigDecimalValue should match");
    }

    @Test(groups={"matcher"})
    public void testEqualsMatcher() {
        MappableQueryMatcher<TestMappableModel, String, Object> matcher = MappableEqualMatcher.getInstance();

        Integer searchValue = 1;

        List<TestMappableModel> results = MappableQueryer.query(mappables, "integerValue", searchValue, matcher);

        assertTrue(results.size() > 0, "Results size should not be empty");

        for (TestMappableModel mappable : results) {
            assertEquals(
                mappable.getIntegerValue(), (Object)searchValue,
                "Result integerValue should be [" + searchValue + "]"
            );
        }

        results = MappableQueryer.query(mappables, "integerValue", 999, matcher);

        assertTrue(results.size() == 0, "Results size should be empty");
    }

    @Test(groups={"matcher"})
    public void testGreaterOrEqualsMatcher() {
        MappableQueryMatcher<TestMappableModel, String, Object> matcher =
            MappableGreaterOrEqualMatcher.getInstance();

        int searchValue = 2;

        List<TestMappableModel> results = MappableQueryer.query(mappables, "integerValue", searchValue, matcher);

        assertTrue(results.size() > 0, "Results size should not be empty");

        for (TestMappableModel mappable : results) {
            assertTrue(mappable.getIntegerValue() >= searchValue, "Result integerValue should be greater or equals to ["+searchValue+"]");
        }

        results = MappableQueryer.query(mappables, "integerValue", 100, matcher);

        assertTrue(results.size() == 0, "Results size should be empty");
    }

    @Test(groups={"matcher"})
    public void testSmallerOrEqualsMatcher() {
        MappableQueryMatcher<TestMappableModel, String, Object> matcher = MappableSmallerOrEqualMatcher.getInstance();

        int searchValue = 2;

        List<TestMappableModel> results = MappableQueryer.query(mappables, "integerValue", searchValue, matcher);

        assertTrue(results.size() > 0, "Results size should not be empty");

        for (TestMappableModel mappable : results) {
            assertTrue(mappable.getIntegerValue() <= searchValue, "Result integerValue should be smaller or equals to ["+searchValue+"]");
        }

        results = MappableQueryer.query(mappables, "integerValue", 0, matcher);

        assertTrue(results.size() == 0, "Results size should be empty");
    }

    @Test(groups = {"matcher"})
    public void testGreaterMatcher() {
        MappableQueryMatcher<TestMappableModel, String, Object> matcher = MappableGreaterMatcher.getInstance();

        int searchValue = 2;

        List<TestMappableModel> results = MappableQueryer.query(mappables, "integerValue", searchValue, matcher);

        assertTrue(results.size() > 0, "Results size should not be empty");

        for (TestMappableModel mappable : results) {
            assertTrue(mappable.getIntegerValue() > searchValue, "Result integerValue should be greater than ["+searchValue+"]");
        }

        results = MappableQueryer.query(mappables, "integerValue", 999, matcher);

        assertTrue(results.size() == 0, "Results size should be empty");
    }

    @Test(groups = {"matcher"})
    public void testSmallerMatcher() {
        MappableQueryMatcher<TestMappableModel, String, Object> matcher = MappableSmallerMatcher.getInstance();

        int searchValue = 2;

        List<TestMappableModel> results = MappableQueryer.query(mappables, "integerValue", searchValue, matcher);

        assertTrue(results.size() > 0, "Results size should not be empty");

        for (TestMappableModel mappable : results) {
            assertTrue(mappable.getIntegerValue() < searchValue, "Result integerValue should be smaller then ["+searchValue+"]");
        }

        results = MappableQueryer.query(mappables, "integerValue", 1, matcher);

        assertTrue(results.size() == 0, "Results size should be empty");
    }

    @Test(groups = {"matcher"})
    public void testLikeMatcher() {
        MappableQueryMatcher<TestMappableModel, String, Object> matcher = MappableLikeMatcher.getInstance();

        String singleWildcard = "?b?", multipleWildcard = "a*";

        List<TestMappableModel> results;

        // single wildcard results
        results = MappableQueryer.query(mappables, "stringValue", singleWildcard, matcher);
        assertTrue(results.size() > 0, "Results size should not be empty");

        for (TestMappableModel mappable : results) {
            assertTrue(mappable.getStringValue().charAt(1) == singleWildcard.charAt(1), "Result second char should be ["+singleWildcard.charAt(1)+"]");
        }

        // multiple wildcard results
        results = MappableQueryer.query(mappables, "stringValue", multipleWildcard, matcher);
        assertTrue(results.size() > 0, "Results size should not be empty");

        for (TestMappableModel mappable : results) {
            assertTrue(mappable.getStringValue().startsWith(multipleWildcard.substring(0, 1)), "Result should be start with ["+multipleWildcard.substring(0, 1)+"]");
        }

        // empty results
        results = MappableQueryer.query(mappables, "stringValue", "?aaa*", matcher);

        assertTrue(results.isEmpty(), "Results size should be empty");
    }

    @Test(groups = {"matcher"})
    public void testMatcherGroup() {
        MappableQueryMatcherGroup<String, MappableQueryMatcher> matcherGroup =
            new MappableQueryMatcherGroup<String, MappableQueryMatcher>();

        matcherGroup.setKeyMatcher("stringValue", MappableLikeMatcher.getInstance());
        matcherGroup.setKeyMatcher("integerValue", MappableGreaterMatcher.getInstance());
        matcherGroup.setKeyMatcher("bigDecimalValue", MappableSmallerMatcher.getInstance());
        matcherGroup.setJoinCondition(QueryJoinCondition.AND);

        Map<String, Object> sample = new HashMap<String, Object>();
        sample.put("stringValue", "?b?");
        sample.put("integerValue", 2);
        sample.put("bigDecimalValue", new BigDecimal("11.11"));

        List<TestMappableModel> results;

        // Test and condition with matches
        results = MappableQueryer.query(mappables, sample, matcherGroup);
        assertTrue(results.size() > 0, "Results size should not be empty");

        for (TestMappableModel mappable : results) {
            assertTrue(mappable.getStringValue().charAt(1) == sample.get("stringValue").toString().charAt(1), "Result second char should be [" + sample.get("stringValue").toString().charAt(1) + "], stringValue: " + mappable.getStringValue());
            assertTrue(mappable.getIntegerValue() > (Integer)sample.get("integerValue"), "Result integerValue should be greater than ["+sample.get("integerValue")+"]");
            assertTrue(mappable.getBigDecimalValue().compareTo((BigDecimal)sample.get("bigDecimalValue")) < 0, "Result bigDecimalValue should smaller than ["+sample.get("bigDecimalValue")+"]");
        }

        // Test or condition with matches
        matcherGroup.setJoinCondition(QueryJoinCondition.OR);

        results = MappableQueryer.query(mappables, sample, matcherGroup);
        assertTrue(results.size() > 0, "Results size should not be empty");

        for (TestMappableModel mappable : results) {

            assertTrue(mappable.getStringValue().charAt(1) == sample.get("stringValue").toString().charAt(1)||mappable.getIntegerValue() > (Integer)sample.get("integerValue")||mappable.getBigDecimalValue().compareTo((BigDecimal)sample.get("bigDecimalValue")) < 0, "Result should match at least one of the matcher");
        }

        // Test and condition no matches
        matcherGroup.setJoinCondition(QueryJoinCondition.AND);
        sample.put("integerValue", 999);
        results = MappableQueryer.query(mappables, sample, matcherGroup);

        assertEquals(results.size(), 0, "Results size should be empty");

        // Test or condition no matches
        matcherGroup.setJoinCondition(QueryJoinCondition.OR);
        sample.put("stringValue", "?z?");
        sample.put("integerValue", 999);
        sample.put("bigDecimalValue", new BigDecimal("10.10"));
        results = MappableQueryer.query(mappables, sample, matcherGroup);

        assertEquals(results.size(), 0, "Results size should be empty");
    }

    @Test(groups = {"query"})
    public void testKeyValueQuery() {
        List<TestMappableModel> results = MappableQueryer.query(mappables, "stringValue", "abc");

        assertTrue(results.size() > 0, "Size should not be empty!");

        for (TestMappableModel m : results) {
            assertEquals("abc", m.getAsMapValue("stringValue"), "stringValue should be [a]");
        }

        results = MappableQueryer.query(mappables, "stringValue", "d");

        assertTrue(results.size() == 0, "Size should be zero!");
    }

    @Test(groups = {"query"})
    public void testSampleQuery() {
        List<TestMappableModel> results = MappableQueryer.query(
            mappables, DataManipulator.createMap(
                Arrays.asList("stringValue", "integerValue"),
                Arrays.<Object>asList("abc", 1)
            )
        );

        assertTrue(results.size() > 0, "Size should not be empty!");

        for (TestMappableModel m : results) {
            assertEquals("abc", m.getAsMapValue("stringValue"), "stringValue should be [a]");
            assertEquals(1, m.getAsMapValue("integerValue"), "integerValue should be [1]");
        }

        results = MappableQueryer.query(
            mappables, DataManipulator.createMap(
                Arrays.asList("stringValue", "integerValue"),
                Arrays.<Object>asList("d", 1)
            )
        );

        assertTrue(results.size() == 0, "Size should be zero!");
    }

    @Test(groups = {"matcher"})
    public void testQueryMatcher() {
        List<TestMappableModel> results = MappableQueryer.query(
            mappables, "integerValue", 1,
            new MappableQueryMatcher<TestMappableModel, String, Object>() {
                @Override
                public boolean match(TestMappableModel mappable, String key, Object value) {
                    Integer v = (Integer) mappable.getAsMapValue(key), iValue = (Integer) value;

                    return v > iValue;
                }
            }
        );

        assertTrue(results.size() > 0, "Size should not be empty!");

        for (TestMappableModel m : results) {
            assertTrue(((Integer) m.getAsMapValue("integerValue")) > 1, "integerValue should be greater than 1");
        }
    }

    @Test(groups = {"matcher"})
    public void testSampleQueryMatcher() {
        MappableSampleQueryMatcher<String, Object> intBigDecimalGreaterMatcher = new MappableSampleQueryMatcher<String, Object>() {
            @Override
            public boolean match(Mappable<String, Object> mappable, Map<String, Object> sample) {
                boolean match = true;
                for (String key : sample.keySet()) {
                    if (key.equals("bigDecimalValue")) {
                        if (((BigDecimal)mappable.getAsMapValue(key)).compareTo((BigDecimal)sample.get(key)) <= 0) {
                            match = false;
                            break;
                        }
                    } else if (key.equals("integerValue")) {
                        if ((Integer)mappable.getAsMapValue(key) <= (Integer) sample.get(key)) {
                            match = false;
                            break;
                        }
                    }
                }
                return match;
            }
        };

        List<TestMappableModel> results = MappableQueryer.query(
            mappables, DataManipulator.createMap(
                Arrays.asList("integerValue", "bigDecimalValue"),
                Arrays.<Object>asList(1, new BigDecimal("10.10"))
            ),
            intBigDecimalGreaterMatcher
        );

        assertTrue(results.size() > 0, "Size should not be empty!");

        for (TestMappableModel m : results) {
            assertTrue(((Integer)m.getAsMapValue("integerValue")) > 1, "integerValue should be greater than 1");
            assertTrue(((BigDecimal)m.getAsMapValue("bigDecimalValue")).compareTo(new BigDecimal("10.10")) > 0, "bigDecimalValue should be greater than 10.10");
        }

        results = MappableQueryer.query(
            mappables, DataManipulator.createMap(
                Arrays.asList("integerValue", "bigDecimalValue"),
                Arrays.<Object>asList(1, new BigDecimal("20.10"))
            ),
            intBigDecimalGreaterMatcher
        );

        assertTrue(results.size() == 0, "Size should be zero!");
    }

    @Test(groups = {"matcher"})
    public void testMatcherGroupCollection() {
        MappableSampleQueryMatcherCollections matcherCollections = new MappableSampleQueryMatcherCollections();

        Map<String, Object> sample = new HashMap<String, Object>();

        // A > stringValue like ?b?
        MappableLikeMatcher matcherA = MappableLikeMatcher.getInstance();
        sample.put("stringValue", "?b?");
        // B > integerValue > 2
        MappableGreaterMatcher matcherB = MappableGreaterMatcher.getInstance();
        sample.put("integerValue", 2);
        // C < bigDecimalValue < 11.11
        MappableSmallerMatcher matcherC = MappableSmallerMatcher.getInstance();
        sample.put("bigDecimalValue", new BigDecimal("11.11"));

        MappableSampleQueryMatcherCollections andCollections = new MappableSampleQueryMatcherCollections(QueryJoinCondition.AND);
        andCollections.addMatcher("stringValue", matcherA);
        andCollections.addMatcher("integerValue", matcherB);

        matcherCollections.addMatcher(andCollections);
        matcherCollections.setJoinCondition(QueryJoinCondition.OR);
        matcherCollections.addMatcher("bigDecimalValue", matcherC);

        List<TestMappableModel> results;

        // Test (A & B) | C
        results = MappableQueryer.query(mappables, sample, matcherCollections);

        assertTrue(results.size() > 0, "Results size should not be empty");

        for (TestMappableModel mappable : results) {
            String stringValue = mappable.getStringValue(), sampleStringValue = sample.get("stringValue").toString();
            int integerValue = mappable.getIntegerValue(), sampleIntegerValue = (Integer)sample.get("integerValue");
            BigDecimal bigDecimalValue = mappable.getBigDecimalValue(), sampleBigDecimalValue = (BigDecimal) sample.get("bigDecimalValue");

            assertTrue(
                (stringValue.charAt(1) == sampleStringValue.charAt(1) && integerValue > sampleIntegerValue) ||
                    bigDecimalValue.compareTo(sampleBigDecimalValue) < 0,
                "Result should match (A & B) | C"
            );
        }

        // Test (A & !B) | C
        andCollections.clearMatchers();
        andCollections.addMatcher("stringValue", matcherA);
        andCollections.addNotMatcher("integerValue", matcherB);

        results = MappableQueryer.query(mappables, sample, matcherCollections);

        assertTrue(results.size() > 0, "Results size should not be empty");

        for (TestMappableModel mappable : results) {
            String stringValue = mappable.getStringValue(), sampleStringValue = sample.get("stringValue").toString();
            int integerValue = mappable.getIntegerValue(), sampleIntegerValue = (Integer)sample.get("integerValue");
            BigDecimal bigDecimalValue = mappable.getBigDecimalValue(), sampleBigDecimalValue = (BigDecimal) sample.get("bigDecimalValue");

            assertTrue(
                (stringValue.charAt(1) == sampleStringValue.charAt(1) && !(integerValue > sampleIntegerValue)) ||
                    bigDecimalValue.compareTo(sampleBigDecimalValue) < 0,
                "Result should match (A & B) | C"
            );
        }

        // Test (A & !B) | C with no matches
        sample.put("integerValue", 0);
        sample.put("bigDecimalValue", BigDecimal.ZERO);

        results = MappableQueryer.query(mappables, sample, matcherCollections);

        assertTrue(results.size() == 0, "Results size should be empty");
    }

    @Test(groups = {"query"})
    public void testQueryMinMax() {
        int maxIntegerValue = Integer.MIN_VALUE, maxCount = 0;
        int minIntegerValue = Integer.MAX_VALUE, minCount = 0;

        for (TestMappableModel mappable : mappables) {
            if (maxIntegerValue < mappable.getIntegerValue()) {
                maxIntegerValue = mappable.getIntegerValue();
                maxCount = 1;
            } else if (maxIntegerValue == mappable.getIntegerValue()){
                maxCount++;
            }

            if (minIntegerValue > mappable.getIntegerValue()) {
                minIntegerValue = mappable.getIntegerValue();
                minCount = 1;
            } else if (minIntegerValue == mappable.getIntegerValue()) {
                minCount++;
            }
        }

        List<TestMappableModel> results;

        // Test queryMax without comparator
        results = MappableQueryer.queryMax(mappables, "integerValue");

        assertTrue(results.size() > 0, "Results size should not be empty");

        assertEquals(results.size(), maxCount, "Max count mismatch");

        for (TestMappableModel mappable : results) {
            assertEquals(mappable.getIntegerValue(), maxIntegerValue, "Max value mismatch");
        }

        // Test queryMax with comparator
        results = MappableQueryer.queryMax(mappables, "integerValue", new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                Integer i1 = (Integer) o1, i2 = (Integer) o2;

                return -i1.compareTo(i2);
            }
        });

        assertTrue(results.size() > 0, "Results size should not be empty");

        assertEquals(results.size(), minCount, "Max (min) count mismatch");

        for (TestMappableModel mappable : results) {
            assertEquals(mappable.getIntegerValue(), minIntegerValue, "Min value mismatch");
        }

        // Test queryMin without comparator
        results = MappableQueryer.queryMin(mappables, "integerValue");

        assertTrue(results.size() > 0, "Results size should not be empty");

        assertEquals(results.size(), minCount, "Min count mismatch");

        for (TestMappableModel mappable : results) {
            assertEquals(mappable.getIntegerValue(), minIntegerValue, "Min value mismatch");
        }

        // Test queryMax with comparator reversed
        results = MappableQueryer.queryMin(mappables, "integerValue", new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                Integer i1 = (Integer) o1, i2 = (Integer) o2;

                return -i1.compareTo(i2);
            }
        });

        assertTrue(results.size() > 0, "Results size should not be empty");

        assertEquals(results.size(), maxCount, "Min (max) count mismatch");

        for (TestMappableModel mappable : results) {
            assertEquals(mappable.getIntegerValue(), maxIntegerValue, "Min (max) value mismatch");
        }
    }

    @Test(groups = {"query", "sort"})
    public void testQuerySort() {
        int randomTestSize = 100;

        List<TestMappableModel> mismatchResults, sortedResults;

        Comparator<TestMappableModel> mappableIntegerComparator = new Comparator<TestMappableModel>() {
            @Override
            public int compare(TestMappableModel o1, TestMappableModel o2) {
                return o1.getIntegerValue() - o2.getIntegerValue();
            }
        };

        // test mismatch
        mismatchResults = MappableQueryer.query(randomMappables, "stringValue", randomMismatchingQueryStringValue, true);

        assertTrue(mismatchResults.size() == 0, "Mismatch results size should be empty");

        // test query with sort
        for (int i = 0; i < randomTestSize; i++) {
            sortedResults = MappableQueryer.query(randomMappables, "integerValue", randomMatchingQueryIntegerValue, true);

            assertTrue(sortedResults.size() > 0, "Sorted results size should not be empty");

            verifyOrderedMappables(sortedResults, mappableIntegerComparator);

            sortedResults = MappableQueryer.query(randomMappables, "integerValue", randomMatchingQueryIntegerValue, mappableIntegerComparator);

            assertTrue(sortedResults.size() > 0, "Sorted results size should not be empty");

            verifyOrderedMappables(sortedResults, mappableIntegerComparator);

            sortedResults = MappableQueryer.query(
                randomMappables, "integerValue", Integer.MIN_VALUE,
                new MappableQueryMatcher<TestMappableModel, String, Object>() {
                    @Override
                    public boolean match(TestMappableModel mappable, String key, Object value) {
                        return DataComparator.compare(mappable.getAsMapValue("integerValue"), value, true, true) > 0;
                    }
                }, true
            );

            assertTrue(sortedResults.size() > 0, "Sorted results size should not be empty");

            verifyOrderedMappables(sortedResults, mappableIntegerComparator);

            sortedResults = MappableQueryer.query(
                randomMappables, "integerValue", Integer.MIN_VALUE,
                new MappableQueryMatcher<TestMappableModel, String, Object>() {
                    @Override
                    public boolean match(TestMappableModel mappable, String key, Object value) {
                        return DataComparator.compare(mappable.getAsMapValue("integerValue"), value, true, true) > 0;
                    }
                }, mappableIntegerComparator
            );

            assertTrue(sortedResults.size() > 0, "Sorted results size should not be empty");

            verifyOrderedMappables(sortedResults, mappableIntegerComparator);

            sortedResults = MappableQueryer.query(
                randomMappables, DataManipulator.<String, Object>createSimpleMap("integerValue", randomMatchingQueryIntegerValue),
                mappableIntegerComparator
            );

            assertTrue(sortedResults.size() > 0, "Sorted results size should not be empty");

            verifyOrderedMappables(sortedResults, mappableIntegerComparator);

            sortedResults = MappableQueryer.query(
                randomMappables, DataManipulator.<String, Object>createSimpleMap("integerValue", randomMatchingQueryIntegerValue),
                new MappableSampleQueryMatcher<String, Object>() {
                    @Override
                    public boolean match(Mappable<String, Object> mappable, Map<String, Object> sample) {
                        return sample.get("integerValue").equals(mappable.getAsMapValue("integerValue"));
                    }
                },
                mappableIntegerComparator
            );

            assertTrue(sortedResults.size() > 0, "Sorted results size should not be empty");

            verifyOrderedMappables(sortedResults, mappableIntegerComparator);
        }
    }
}
