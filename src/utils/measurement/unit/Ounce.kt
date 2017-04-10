package utils.measurement.unit

import java.math.BigDecimal

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2017-04-10
 * Time: 14:32
 */
class Ounce(v: BigDecimal): Weight(v) {
    override val unit = WeightUnit.OZ
    
    override fun toMeasurementUnit(targetUnit: WeightUnit): Weight {
        return when(targetUnit) {
            WeightUnit.G -> Gram(value.multiply(BigDecimal("28.349523125")))
            WeightUnit.KG -> Kilogram(value.multiply(BigDecimal("0.028349523125")))
            WeightUnit.LB -> Pound(value.multiply(BigDecimal("0.0625")))
            WeightUnit.OZ -> this
            else -> throw NotImplementedError("Conversion from $unit to $targetUnit not implemented yet!")
        }
    }
}