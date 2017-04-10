package utils.measurement.unit

import java.math.BigDecimal

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2017-04-10
 * Time: 12:53
 */
class Gram(v: BigDecimal): Weight(v) {
    override val unit = WeightUnit.G
    
    override fun toMeasurementUnit(targetUnit: WeightUnit): Weight {
        return when (targetUnit) {
            WeightUnit.G -> this
            WeightUnit.KG -> Kilogram(value.multiply(BigDecimal("0.001")))
            WeightUnit.LB -> Pound(value.multiply(BigDecimal("0.03527396194958")))
            WeightUnit.OZ -> Ounce(value.multiply(BigDecimal("0.002204622621849")))
            else -> throw NotImplementedError("Conversion from $unit to $targetUnit not implemented yet")
        }
    }
}