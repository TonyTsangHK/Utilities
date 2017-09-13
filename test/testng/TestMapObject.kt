package testng

import data.TestMapObjectModel
import org.testng.Assert
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test
import utils.data.MapObjectHelper
import java.math.BigDecimal

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2017-09-13
 * Time: 17:32
 */
class TestMapObject {
    private lateinit var testObject: TestMapObjectModel
    private lateinit var expectedMap: Map<String, Any?>
    
    @BeforeTest
    fun setup() {
        val strValue = "test string"
        val intValue = 100
        val decimalValue = BigDecimal("100.001")
        
        testObject = TestMapObjectModel(strValue, intValue, decimalValue)
        
        expectedMap = mapOf(
            MapObjectHelper.CLZ_FIELD to TestMapObjectModel::class.java.canonicalName,
            "strValue" to strValue,
            "intValue" to intValue,
            "decimalValue" to decimalValue
        )
    }
    
    @Test
    fun testToMap() {
        val map = MapObjectHelper.toMap(testObject)
        
        expectedMap.forEach {
            key, value ->
            if (key != MapObjectHelper.CLZ_FIELD) {
                Assert.assertTrue(map.containsKey(key), "Missing key: $key")
                Assert.assertEquals(map[key], value, "Value mismatch, expected: $value, actual: ${map[key]}")
            }
        }
    }
    
    @Test
    fun testFromMap() {
        val resultWithClassInfo = MapObjectHelper.fromMap(expectedMap) as TestMapObjectModel
        
        Assert.assertNotNull(resultWithClassInfo)
        Assert.assertTrue(testObject.equals(resultWithClassInfo))
        
        val mapWithoutClassInfo = expectedMap.filter {
            it.key != MapObjectHelper.CLZ_FIELD
        }
        
        val resultWithoutClassInfo = MapObjectHelper.fromMap(mapWithoutClassInfo, TestMapObjectModel::class.java)
        
        Assert.assertNotNull(resultWithoutClassInfo)
        Assert.assertTrue(testObject.equals(resultWithoutClassInfo))
    }
}