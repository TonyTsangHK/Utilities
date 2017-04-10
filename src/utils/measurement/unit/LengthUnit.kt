package utils.measurement.unit

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2017-04-10
 * Time: 16:50
 */
enum class LengthUnit(override val unitName: String, override val symbol: String): MeasurementUnit {
    MM("Millimeter", "mm"), CM("Centimeter", "cm"), M("Meter", "m"), KM("Kilometer", "km"),
    IN("Inch", "in"), FT("Foot", "ft"), MI("Mile", "mi");

    override fun toString(): String {
        return unitName
    }
}