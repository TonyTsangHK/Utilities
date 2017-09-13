package utils.data

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2017-09-13
 * Time: 10:55
 */
data class ActionStatus(
    val success: Boolean, val statusDescription: String, val extraDataMap: Map<String, Any?>? = null
)