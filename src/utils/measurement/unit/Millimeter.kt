package utils.measurement.unit

import java.math.BigDecimal

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2017-04-10
 * Time: 15:28
 */
class Millimeter(v: BigDecimal): Length(v) {
    override val unit = LengthUnit.MM

    override fun toMeasurementUnit(targetUnit: LengthUnit): Length {
        return when(targetUnit) {
            LengthUnit.MM -> this
            LengthUnit.CM -> Centimeter(value.multiply(BigDecimal("0.1")))
            LengthUnit.M -> Meter(value.multiply(BigDecimal("0.001")))
            LengthUnit.KM -> Kilometer(value.multiply(BigDecimal("0.000001")))
            LengthUnit.IN -> Inch(value.multiply(BigDecimal("0.03937007874016")))
            LengthUnit.FT -> Foot(value.multiply(BigDecimal("0.003280839895013")))
            LengthUnit.MI -> Mile(value.multiply(BigDecimal("6.213711922373E-7")))
            else -> throw NotImplementedError("Conversion from $unit to $targetUnit not implemented yet")
        }
    }
}