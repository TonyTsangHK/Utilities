package testng

import org.testng.Assert.*
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import utils.data.CountingDataHolder
import utils.data.ListUtil

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2017-11-07
 * Time: 10:33
 */
class TestCountingDataHolder {
    private lateinit var countingDataHolder: CountingDataHolder<String>
    private lateinit var testData: Array<String>
    
    @BeforeMethod
    fun setUp() {
        countingDataHolder = CountingDataHolder<String>()
        testData = arrayOf(
            "A", // 1, min
            "A1", // 1 min
            "B", "B", // 2
            "C", "C", "C", // 3, max
            "C1", "C1", "C1" // 3, max
        )
    }
    
    private fun countTestData() {
        testData.forEach {
            countingDataHolder.incrementCounter(it)
        }
    }

    @Test
    fun testIncrementCounter() {
        countTestData()
        
        assertEquals(countingDataHolder.getCounter("A"), 1)
        assertEquals(countingDataHolder.getCounter("A1"), 1)
        assertEquals(countingDataHolder.getCounter("B"), 2)
        assertEquals(countingDataHolder.getCounter("C"), 3)
        assertEquals(countingDataHolder.getCounter("C1"), 3)

        val majorityValues = countingDataHolder.getMajorityValues()
        val minorityValues = countingDataHolder.getMinorityValues()

        assertNotNull(majorityValues)
        assertNotNull(minorityValues)

        assertTrue(ListUtil.compareList(majorityValues!!, listOf("C", "C1")).isSame)
        assertTrue(ListUtil.compareList(minorityValues!!, listOf("A", "A1")).isSame)

        assertEquals(countingDataHolder.getMaximumCounter(), 3)
        assertEquals(countingDataHolder.getMinimumCounter(), 1)
    }
    
    @Test
    fun testDecrement() {
        countTestData()
        
        countingDataHolder.decrementCounter("Non-exists")
        
        assertEquals(countingDataHolder.getCounter("Non-exists"), 0)
        
        countingDataHolder.decrementCounter("B")
        
        assertEquals(countingDataHolder.getCounter("B"), 1)
        
        countingDataHolder.decrementCounter("A1")
        
        assertEquals(countingDataHolder.getCounter("A1"), 0)
        
        countingDataHolder.decrementCounter("C1")

        val majorityValues = countingDataHolder.getMajorityValues()
        val minorityValues = countingDataHolder.getMinorityValues()

        assertNotNull(majorityValues)
        assertNotNull(minorityValues)

        assertTrue(ListUtil.compareList(majorityValues!!, listOf("C")).isSame)
        assertTrue(ListUtil.compareList(minorityValues!!, listOf("A", "B")).isSame)

        assertEquals(countingDataHolder.getMaximumCounter(), 3)
        assertEquals(countingDataHolder.getMinimumCounter(), 1)
    }
}