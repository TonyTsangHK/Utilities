package utils.measurement.unit

import java.math.BigDecimal

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2017-04-10
 * Time: 15:35
 */
class Mile(v: BigDecimal): Length(v) {
    override val unit = LengthUnit.MI

    override fun toMeasurementUnit(targetUnit: LengthUnit): Length {
        return when (targetUnit) {
            LengthUnit.MM -> Millimeter(value.multiply(BigDecimal("1609344")))
            LengthUnit.CM -> Centimeter(value.multiply(BigDecimal("160934.4")))
            LengthUnit.M -> Meter(value.multiply(value.multiply(BigDecimal("1609.344"))))
            LengthUnit.KM -> Kilometer(value.multiply(BigDecimal("1.609344")))
            LengthUnit.IN -> Inch(value.multiply(BigDecimal("63360")))
            LengthUnit.FT -> Foot(value.multiply(BigDecimal("5280")))
            LengthUnit.MI -> this
            else -> throw NotImplementedError("Conversion from $unit to $targetUnit not implemented yet!")
        }
    }
}