package utils.measurement.unit

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2017-04-10
 * Time: 17:01
 */
enum class WeightUnit(override val unitName: String, override val symbol: String): MeasurementUnit {
    G("Gram", "g"), KG("Kilogram", "kg"), OZ("Ounce", "oz"), LB("Pound", "lb");

    override fun toString(): String {
        return unitName
    }
}