package utils.measurement.unit

import utils.math.MathUtil
import java.math.BigDecimal

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2017-04-10
 * Time: 14:28
 */
class Pound(v: BigDecimal): Weight(v) {
    override val unit = WeightUnit.LB
    
    override fun toMeasurementUnit(targetUnit: WeightUnit): Weight {
        return when(targetUnit) {
            WeightUnit.G -> Gram(value.multiply(BigDecimal("453.59237")))
            WeightUnit.KG -> Kilogram(value.multiply(BigDecimal("0.45359237")))
            WeightUnit.LB -> this
            WeightUnit.OZ -> Ounce(value.multiply(MathUtil.SIXTEEN))
            else -> throw NotImplementedError("Conversion from $unit to $targetUnit not implemented yet")
        }
    }
}