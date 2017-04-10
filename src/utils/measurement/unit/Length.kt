package utils.measurement.unit

import utils.math.MathUtil
import java.math.BigDecimal

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2017-04-10
 * Time: 12:31
 */
abstract class Length(override val value: BigDecimal): Measurement<LengthUnit> {
    override fun toString(): String {
        return "$value ${unit.symbol}"
    }
}