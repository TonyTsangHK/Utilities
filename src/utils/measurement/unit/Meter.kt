package utils.measurement.unit

import utils.math.MathUtil
import java.math.BigDecimal

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2017-04-10
 * Time: 15:35
 */
class Meter(v: BigDecimal): Length(v) {
    override val unit = LengthUnit.M

    override fun toMeasurementUnit(targetUnit: LengthUnit): Length {
        return when (targetUnit) {
            LengthUnit.MM -> Millimeter(value.multiply(BigDecimal("1000")))
            LengthUnit.CM -> Centimeter(value.multiply(MathUtil.HUNDRED))
            LengthUnit.M -> this
            LengthUnit.KM -> Kilometer(value.multiply(BigDecimal("0.001")))
            LengthUnit.IN -> Inch(value.multiply(BigDecimal("39.37007874016")))
            LengthUnit.FT -> Foot(value.multiply(BigDecimal("3.280839895013")))
            LengthUnit.MI -> Mile(value.multiply(BigDecimal("0.0006213711922373")))
            else -> throw NotImplementedError("Conversion from $unit to $targetUnit not implemented yet!")
        }
    }
}