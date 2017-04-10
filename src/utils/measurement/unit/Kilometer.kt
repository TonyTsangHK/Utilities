package utils.measurement.unit

import java.math.BigDecimal

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2017-04-10
 * Time: 15:35
 */
class Kilometer(v: BigDecimal): Length(v) {
    override val unit = LengthUnit.KM

    override fun toMeasurementUnit(targetUnit: LengthUnit): Length {
        return when (targetUnit) {
            LengthUnit.MM -> Millimeter(value.multiply(BigDecimal("1000000")))
            LengthUnit.CM -> Centimeter(value.multiply(BigDecimal("100000")))
            LengthUnit.M -> Meter(value.multiply(value.multiply(BigDecimal("1000"))))
            LengthUnit.KM -> this
            LengthUnit.IN -> Inch(value.multiply(BigDecimal("39370.07874016")))
            LengthUnit.FT -> Foot(value.multiply(BigDecimal("3280.839895013")))
            LengthUnit.MI -> Mile(value.multiply(BigDecimal("0.6213711922373")))
            else -> throw NotImplementedError("Conversion from $unit to $targetUnit not implemented yet!")
        }
    }
}