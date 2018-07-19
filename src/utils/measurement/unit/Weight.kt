package utils.measurement.unit

import java.math.BigDecimal

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2017-04-10
 * Time: 12:31
 */
abstract class Weight(override val value: BigDecimal): Measurement<WeightUnit> {
    override fun toString(): String {
        return "$value ${unit.symbol}"
    }
}