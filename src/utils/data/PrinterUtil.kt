package utils.data

import java.awt.geom.Line2D
import java.awt.geom.Rectangle2D

object PrinterUtil {
    @JvmStatic
    fun getDoubleArrString(arr: DoubleArray?): String {
        if (arr == null) {
            return "NULL"
        }
        val builder = StringBuilder(arr.size * 5)
        builder.append('[')
        for (i in arr.indices) {
            if (i > 0) {
                builder.append(',')
            }
            builder.append(i)
            builder.append(':')
            builder.append(arr[i])
        }
        builder.append(']')
        return builder.toString()
    }

    @JvmStatic
    fun getFloatArrString(arr: FloatArray?): String {
        if (arr == null) {
            return "NULL"
        }
        val builder = StringBuilder(arr.size * 5)
        builder.append('[')
        for (i in arr.indices) {
            if (i > 0) {
                builder.append(',')
            }
            builder.append(i)
            builder.append(':')
            builder.append(arr[i])
        }
        builder.append(']')
        return builder.toString()
    }

    @JvmOverloads 
    @JvmStatic 
    fun getIntArrString(arr: IntArray?, printIndex: Boolean = true): String {
        if (arr == null) {
            return "NULL"
        }
        val builder = StringBuilder(arr.size * 5)
        builder.append('[')
        for (i in arr.indices) {
            if (i > 0) {
                builder.append(',')
            }
            if (printIndex) {
                builder.append(i)
                builder.append(':')
            }
            builder.append(arr[i])
        }
        builder.append(']')
        return builder.toString()
    }

    @JvmOverloads 
    @JvmStatic 
    fun getIntArrString(arr: Array<Int>?, printIndex: Boolean = true): String {
        if (arr == null) {
            return "NULL"
        }

        val builder = StringBuilder(arr.size * 5)
        builder.append('[')
        for (i in arr.indices) {
            if (i > 0) {
                builder.append(',')
            }
            if (printIndex) {
                builder.append(i)
                builder.append(':')
            }
            builder.append(arr[i])
        }
        builder.append(']')
        return builder.toString()
    }

    @JvmOverloads 
    @JvmStatic 
    fun getStringArrString(arr: Array<String>?, showIndex: Boolean = false): String {
        if (arr == null) {
            return "NULL"
        }
        val builder = StringBuilder(arr.size * 5)
        builder.append('[')
        for (i in arr.indices) {
            if (i > 0) {
                builder.append(',')
            }
            if (showIndex) {
                builder.append(i)
                builder.append(':')
            }
            builder.append(arr[i])
        }
        builder.append(']')
        return builder.toString()
    }

    @JvmStatic
    fun getCollectionString(collection: Collection<*>?): String {
        if (collection == null) {
            return "NULL"
        }

        val builder = StringBuilder(collection.size * 5)
        builder.append('[')
        for (obj in collection) {
            if (builder.length > 1) {
                builder.append(',')
            }
            if (obj is Collection<*>) {
                builder.append(getCollectionString(obj as List<*>))
            } else if (obj is Map<*, *>) {
                builder.append(getMapString(obj))
            } else {
                builder.append(obj.toString())
            }
        }
        builder.append(']')
        return builder.toString()
    }

    @JvmStatic
    fun getMapString(map: Map<*, *>): String {
        val builder = StringBuilder(map.size * 15)
        val keySet = map.keys
        for (k in keySet) {
            builder.append(k.toString())
            builder.append(" : ")
            val obj = map[k]
            if (obj != null) {
                builder.append(obj.toString())
            } else {
                builder.append("[Empty object value]")
            }
            builder.append('\n')
        }
        return builder.toString()
    }

    @JvmStatic
    fun getListString(list: List<*>?): String {
        if (list == null) {
            return "NULL"
        } else if (list.isEmpty()) {
            return "[]"
        }

        val builder = StringBuilder(list.size * 15)
        builder.append('[')
        for (i in list.indices) {
            if (i > 0) {
                builder.append(',')
            }
            builder.append(i)
            builder.append(':')
            val obj = list[i]
            builder.append(if (obj != null) list[i].toString() else "{Empty object value}")
        }
        builder.append(']')
        return builder.toString()
    }

    @JvmStatic
    fun getLine2DString(line: Line2D): String {
        return "[x1: " + line.x1 + ", y1: " + line.y1 +
                ", x2: " + line.x2 + ", y2: " + line.y2
    }

    @JvmStatic
    fun getRectangle2DString(rect: Rectangle2D): String {
        return "[maxX: " + rect.maxX + ", maxY: " + rect.maxY +
                ", minX: " + rect.minX + ", minY: " + rect.minY +
                ", width: " + rect.width + ", height: " + rect.height + "]"
    }

    @JvmStatic
    fun printDoubleArray(arr: DoubleArray) {
        print(getDoubleArrString(arr))
    }

    @JvmStatic
    fun printFloatArray(arr: FloatArray) {
        print(getFloatArrString(arr))
    }

    @JvmOverloads
    @JvmStatic
    fun printIntArray(arr: IntArray, printIndex: Boolean = true) {
        print(getIntArrString(arr, printIndex))
    }

    @JvmStatic 
    fun printStringArray(arr: Array<String>) {
        print(getStringArrString(arr))
    }

    @JvmStatic
    fun printLine2D(line: Line2D) {
        print(getLine2DString(line))
    }

    @JvmStatic
    fun printRectangle2D(rect: Rectangle2D) {
        print(getRectangle2DString(rect))
    }

    @JvmStatic
    fun printMap(map: Map<*, *>) {
        print(getMapString(map))
    }

    @JvmStatic
    fun printList(list: List<*>) {
        print(getListString(list))
    }

    @JvmStatic
    fun printCollectionString(list: List<*>) {
        print(getCollectionString(list))
    }
}
