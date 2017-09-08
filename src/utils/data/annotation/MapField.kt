package utils.data.annotation

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2017-09-07
 * Time: 12:25
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class MapField(val fieldName: String)