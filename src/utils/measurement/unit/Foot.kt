package utils.measurement.unit

import utils.math.MathUtil
import java.math.BigDecimal

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2017-04-10
 * Time: 15:35
 */
class Foot(v: BigDecimal): Length(v) {
    override val unit = LengthUnit.FT

    override fun toMeasurementUnit(targetUnit: LengthUnit): Length {
        return when (targetUnit) {
            LengthUnit.MM -> Millimeter(value.multiply(BigDecimal("304.8")))
            LengthUnit.CM -> Centimeter(value.multiply(BigDecimal("30.48")))
            LengthUnit.M -> Meter(value.multiply(value.multiply(BigDecimal("0.3048"))))
            LengthUnit.KM -> Kilometer(value.multiply(BigDecimal("0.0003048")))
            LengthUnit.IN -> Inch(value.multiply(MathUtil.TWELVE))
            LengthUnit.FT -> this
            LengthUnit.MI -> Mile(MathUtil.divide(value, BigDecimal("5280")))
            else -> throw NotImplementedError("Conversion from $unit to $targetUnit not implemented yet!")
        }
    }
}