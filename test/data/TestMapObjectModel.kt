package data

import utils.data.annotation.MapField
import utils.data.annotation.MapObject
import java.math.BigDecimal

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2017-09-13
 * Time: 17:28
 */
@MapObject
class TestMapObjectModel(
    @field:MapField(fieldName = "strValue")
    val strValue: String, 
    @field:MapField(fieldName = "intValue")
    val intValue: Int,
    @field:MapField(fieldName = "decimalValue")
    val decimalValue: BigDecimal) {
    
    // for map object
    constructor(): this("", 0, BigDecimal.ZERO)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TestMapObjectModel) return false

        if (strValue != other.strValue) return false
        if (intValue != other.intValue) return false
        if (decimalValue != other.decimalValue) return false

        return true
    }

    override fun hashCode(): Int {
        var result = strValue.hashCode()
        result = 31 * result + intValue
        result = 31 * result + decimalValue.hashCode()
        return result
    }


}