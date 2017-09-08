package utils.data

import utils.data.annotation.MapField
import utils.data.annotation.MapObject
import java.lang.reflect.Modifier
import java.util.*

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2017-09-07
 * Time: 12:29
 */

/**
 * Convert MapObject to / from map
 */
object MapObjectHelper{
    /**
     * Class info key
     */
    val CLZ_FIELD = "_clz_"

    /**
     * Convert MapObject to map
     * 
     * @param mapObject target MapObject
     * @param withClassInfo whether to store the class info in map, map without class info may not be converted back to MapObject  
     */
    @JvmStatic
    @JvmOverloads
    fun toMap(mapObject: Any, withClassInfo: Boolean = true): Map<String, Any?> {
        val clz = mapObject.javaClass
        
        if (clz.isAnnotationPresent(MapObject::class.java)) {
            val dataMap = TreeMap<String, Any?>()
            
            if (withClassInfo) {
                dataMap[CLZ_FIELD] = clz.canonicalName
            }
            
            val fields = clz.declaredFields
            
            fields.forEach {
                if (it.isAnnotationPresent(MapField::class.java)) {
                    val mapField = it.getAnnotation(MapField::class.java)
                    val fieldName = mapField.fieldName
                    
                    if (it.modifiers.and(Modifier.PRIVATE) == Modifier.PRIVATE) {
                        it.isAccessible = true
                    }
                    
                    val fieldValue = it.get(mapObject)
                    
                    if (fieldValue != null) {
                        if (fieldValue is List<*>) {
                            val valueList = fieldValue as List<Any?>

                            val list = ArrayList<Any?>()

                            for (value in valueList) {
                                if (value != null) {
                                    if (value.javaClass.isAnnotationPresent(MapObject::class.java)) {
                                        list.add(toMap(value, withClassInfo))
                                    } else {
                                        list.add(value)
                                    }
                                } else {
                                    list.add(null)
                                }
                            }

                            dataMap[fieldName] = list
                        } else if (fieldValue is Map<*,*>) {
                            val valueMap = fieldValue as Map<String, Any?>

                            val map = TreeMap<String, Any?>()

                            for ((key, value) in valueMap) {
                                if (value != null) {
                                    if (value.javaClass.isAnnotationPresent(MapObject::class.java)) {
                                        map[key] = toMap(value, withClassInfo)
                                    } else {
                                        map[key] = value
                                    }
                                } else {
                                    map[key] = null
                                }
                            }

                            dataMap[fieldName] = map
                        } else {
                            if (fieldValue.javaClass.isAnnotationPresent(MapObject::class.java)) {
                                dataMap[fieldName] = toMap(fieldValue, withClassInfo)
                            } else {
                                dataMap[fieldName] = fieldValue
                            }
                        }
                    } else {
                        dataMap[fieldName] = null
                    }
                }
            }
            
            return dataMap
        } else {
            throw IllegalArgumentException("Not MapObject annotated class, ${clz.name}")
        }
    }

    /**
     * Convert data map to MapObject
     * 
     * @param dataMap data map
     * @param clz MapObject class
     */
    @JvmStatic
    fun <T> fromMap(dataMap: Map<String, Any?>, clz: Class<T>): T {
        if (clz.isAnnotationPresent(MapObject::class.java)) {
            val constructor = clz.getConstructor()
            
            val declaredFields = clz.declaredFields
            
            if (!constructor.isAccessible) {
                constructor.isAccessible = true
            }
            
            val obj = constructor.newInstance()
            
            declaredFields.forEach {
                if (it.isAnnotationPresent(MapField::class.java)) {
                    val mapField = it.getAnnotation(MapField::class.java)
                    val fieldName = mapField.fieldName
                    
                    if (dataMap.containsKey(fieldName)) {
                        val dataValue = dataMap[fieldName]
                        
                        val fieldValue: Any?
                        if (dataValue != null) {
                            if (dataValue is List<*>) {
                                fieldValue = _processList(dataValue)
                            } else if (dataValue is Map<*, *>) {
                                fieldValue = _processMap(dataValue as Map<String, Any?>)
                            } else {
                                fieldValue = dataValue
                            }
                        } else {
                            fieldValue = null
                        }
                        
                        val modifier = it.modifiers
                        if (Modifier.isPrivate(modifier) || Modifier.isFinal(modifier)) {
                            it.isAccessible = true
                        }
                        
                        it.set(obj, fieldValue)
                    }
                }
            }
            
            return obj
        } else {
            throw IllegalArgumentException("Not MapObject annotated class, ${clz.name}")
        }
    }

    /**
     * Convert map to MapObject, the map must contains class info field
     * 
     * @param dataMap data map with class info
     */
    @JvmStatic
    fun fromMap(dataMap: Map<String, Any?>): Any {
        if (dataMap.containsKey(CLZ_FIELD)) {
            return fromMap(dataMap, Class.forName(dataMap[CLZ_FIELD]!! as String))
        } else {
            throw IllegalArgumentException("Data map does not have class info")
        }
    }

    /**
     * Process list values to find possible MapObject
     */
    private fun _processList(dataList: List<Any?>): List<Any?> {
        val _list = ArrayList<Any?>()

        for (value in dataList) {
            val _value: Any?

            if (value is List<*>) {
                _value = _processList(value)
            } else if (value is Map<*, *>) {
                _value = _processMap(value as Map<String, Any?>)
            } else {
                _value = value
            }

            _list += _value
        }

        return _list
    }

    /**
     * Process map to find possible MapObject
     */
    private fun _processMap(dataMap: Map<String, Any?>): Any {
        if (dataMap.containsKey(CLZ_FIELD)) {
            return fromMap(dataMap)
        } else {
            val _map = TreeMap<String, Any?>()

            for ((key, value) in dataMap) {
                val _value: Any?
                if (value is List<*>) {
                    _value = _processList(value)
                } else if (value is Map<*, *>) {
                    _value = _processMap(value as Map<String, Any?>)
                } else {
                    _value = value
                }

                _map[key] = _value
            }

            return _map
        }
    }
}
