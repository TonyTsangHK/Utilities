package utils.measurement.unit

import utils.math.MathUtil
import java.math.BigDecimal

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2017-04-10
 * Time: 15:35
 */
class Inch(v: BigDecimal): Length(v) {
    override val unit = LengthUnit.IN

    override fun toMeasurementUnit(targetUnit: LengthUnit): Length {
        return when (targetUnit) {
            LengthUnit.MM -> Millimeter(value.multiply(BigDecimal("25.4")))
            LengthUnit.CM -> Centimeter(value.multiply(BigDecimal("2.54")))
            LengthUnit.M -> Meter(value.multiply(value.multiply(BigDecimal("0.0254"))))
            LengthUnit.KM -> Kilometer(value.multiply(BigDecimal("0.0000254")))
            LengthUnit.IN -> this
            LengthUnit.FT -> Foot(MathUtil.divide(value, MathUtil.TWELVE))
            LengthUnit.MI -> Mile(MathUtil.divide(value, BigDecimal("63360")))
            else -> throw NotImplementedError("Conversion from $unit to $targetUnit not implemented yet!")
        }
    }
}