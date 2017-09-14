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
                Assert.assertEquals(map[key], value)
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
    
    @Test
    fun testGetFieldValue() {
        expectedMap.forEach {
            key, value ->
            if (key != MapObjectHelper.CLZ_FIELD) {
                Assert.assertEquals(MapObjectHelper.getFieldValue(testObject, key), value)
            }
        }
    }
    
    @Test
    fun testSetFieldValue() {
        // Clone testObject before testing to avoid modifying testObject
        val clone = MapObjectHelper.clone(testObject)
        
        val newValueMap = mapOf(
            "strValue" to "new string value",
            "intValue" to 999,
            "decimalValue" to BigDecimal("123456789.0123456789")
        )
        
        newValueMap.forEach {
            key, value ->
            MapObjectHelper.setFieldValue(clone, key, value)
        }
        
        // Verify
        newValueMap.forEach {
            key, value ->
            Assert.assertEquals(value, MapObjectHelper.getFieldValue(clone, key))
        }
    }
    
    @Test
    fun testClone() {
        val clone = MapObjectHelper.clone(testObject)
        
        Assert.assertTrue(testObject.equals(clone))
    }
}