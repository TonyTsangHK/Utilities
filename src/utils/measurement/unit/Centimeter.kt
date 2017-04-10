package utils.measurement.unit

import java.math.BigDecimal

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2017-04-10
 * Time: 15:34
 */
class Centimeter(v: BigDecimal): Length(v) {
    override val unit = LengthUnit.CM
    
    override fun toMeasurementUnit(targetUnit: LengthUnit): Length {
        return when (targetUnit) {
            LengthUnit.MM -> Millimeter(value.multiply(BigDecimal("10")))
            LengthUnit.CM -> this
            LengthUnit.M -> Meter(value.multiply(value.multiply(BigDecimal("0.01"))))
            LengthUnit.KM -> Kilometer(value.multiply(BigDecimal("0.00001")))
            LengthUnit.IN -> Inch(value.multiply(BigDecimal("0.3937007874016")))
            LengthUnit.FT -> Foot(value.multiply(BigDecimal("0.03280839895013")))
            LengthUnit.MI -> Mile(value.multiply(BigDecimal("0.000006213711922373")))
            else -> throw NotImplementedError("Conversion from $unit to $targetUnit not implemented yet!")
        }
    }
}