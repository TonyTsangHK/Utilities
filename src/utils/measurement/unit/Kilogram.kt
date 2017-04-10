package utils.measurement.unit

import utils.math.MathUtil
import java.math.BigDecimal

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2017-04-10
 * Time: 14:23
 */
class Kilogram(v: BigDecimal): Weight(v) {
    override val unit = WeightUnit.KG
    
    override fun toMeasurementUnit(targetUnit: WeightUnit): Weight {
        return when (targetUnit) {
            WeightUnit.G -> Gram(value.multiply(MathUtil.THOUSAND))
            WeightUnit.KG -> this
            WeightUnit.LB -> Pound(value.multiply(BigDecimal("2.204622621849")))
            WeightUnit.OZ -> Ounce(value.multiply(BigDecimal("35.27396194958")))
            else -> throw NotImplementedError("Conversion from $unit to $targetUnit not implemented yet")
        }
    }
}