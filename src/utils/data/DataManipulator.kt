package utils.data

import utils.constants.MathValueType
import utils.date.DateCalendar
import utils.date.DateTimeParser
import utils.math.MathUtil
import utils.string.StringUtil
import java.math.BigDecimal
import java.util.*

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2016-09-14
 * Time: 14:31
 */
object DataManipulator {
    @JvmStatic
    fun <E> swapData(array: Array<E>, f: Int, t: Int): Boolean {
        if (array.indices.contains(t) && array.indices.contains(t)) {
            val o = array[f]
            array[f] = array[t]
            array[t] = o
            return true
        } else {
            return false
        }
    }

    @JvmStatic
    // All swap data method will not check index!
    fun swapData(a: BooleanArray, i: Int, j: Int) {
        val v = a[i]
        a[i] = a[j]
        a[j] = v
    }

    @JvmStatic
    fun swapData(a: ByteArray, i: Int, j: Int) {
        val v = a[i]
        a[i] = a[j]
        a[j] = v
    }

    @JvmStatic
    fun swapData(a: CharArray, i: Int, j: Int) {
        val v = a[i]
        a[i] = a[j]
        a[j] = v
    }

    @JvmStatic
    fun swapData(a: ShortArray, i: Int, j: Int) {
        val v = a[i]
        a[i] = a[j]
        a[j] = v
    }

    @JvmStatic
    fun swapData(a: FloatArray, i: Int, j: Int) {
        val v = a[i]
        a[i] = a[j]
        a[j] = v
    }

    @JvmStatic
    fun swapData(a: DoubleArray, i: Int, j: Int) {
        val v = a[i]
        a[i] = a[j]
        a[j] = v
    }

    @JvmStatic
    fun swapData(a: IntArray, i: Int, j: Int) {
        val v = a[i]
        a[i] = a[j]
        a[j] = v
    }

    @JvmStatic
    fun swapData(a: LongArray, i: Int, j: Int) {
        val v = a[i]
        a[i] = a[j]
        a[j] = v
    }

    @JvmStatic
    fun <E> swapData(l: MutableList<E>, f: Int, t: Int): Boolean {
        if (l.indices.contains(f) && l.indices.contains(t) && f != t) {
            val o = l[f]
            l[f] = l[t]
            l[t] = o
            return true
        } else {
            return false
        }
    }

    @JvmStatic
    fun <E> swapData(l: List<MutableList<E>>, f: Int, t: Int, coi: IntArray): Boolean {
        if (l.indices.contains(f) && l.indices.contains(t) && f != t) {
            val fl = l[f]
            val tl = l[t]
            for (k in coi) {
                val fVal = fl[k]
                fl[k] = tl[k]
                tl[k] = fVal
            }
            return true
        } else {
            return false
        }
    }

    @JvmStatic
    fun <E> moveData(l: MutableList<E>, f: Int, t: Int): Boolean {
        if (l.indices.contains(f) && l.indices.contains(t) && f != t) {
            val o = l[f]
            if (f < t) {
                for (i in f..t - 1) {
                    l[i] = l[i + 1]
                }
            } else {
                for (i in f downTo t + 1) {
                    l[i] = l[i - 1]
                }
            }
            l[t] = o
            return true
        } else {
            return false
        }
    }

    @JvmStatic
    fun <E> swapDatas(l: MutableList<E>, fs: Int, fe: Int, es: Int, ee: Int): Boolean {
        var localFs = fs
        var localFe = fe
        var localEs = es
        var localEe = ee
        if (localFs > localEs) {
            var t = localFs
            localFs = localEs
            localEs = t
            t = localFe
            localFe = localEe
            localEe = t
        }
        if (
            l.indices.contains(localFs) && l.indices.contains(localFe) &&
            l.indices.contains(localEs) && l.indices.contains(localEe) &&
            localFe < localEs && localFs <= localFe && localEs <= localEe
        ) {
            val ll = LinkedList<E>()
            for (i in localEs..localEe) {
                ll.add(l[i])
            }
            for (i in localFe + 1..localEs - 1) {
                ll.add(l[i])
            }
            for (i in localFs..localFe) {
                ll.add(l[i])
            }
            var i = localFs
            var k = 0
            while (i <= localEe) {
                l[i] = ll.removeFirst()
                i++
                k++
            }
            return true
        } else {
            return false
        }
    }

    @JvmStatic
    fun <E> swapDatas(l: List<MutableList<E>>, fs: Int, fe: Int, es: Int, ee: Int, coi: IntArray): Boolean {
        var localFs = fs
        var localFe = fe
        var localEs = es
        var localEe = ee
        if (localFs > localEs) {
            var t = localFs
            localFs = localEs
            localEs = t
            t = localFe
            localFe = localEe
            localEe = t
        }
        if (
            l.indices.contains(localFs) && l.indices.contains(localFe) && 
            l.indices.contains(localEs) && l.indices.contains(localEe) &&
            localFe < localEs && localFs <= localFe && localEs <= localEe
        ) {
            val ll = LinkedList<List<E>>()
            for (i in localEs..localEe) {
                ll.add(cloneList(l[i])!!)
            }
            for (i in localFe + 1..localEs - 1) {
                ll.add(cloneList(l[i])!!)
            }
            for (i in localFs..localFe) {
                ll.add(cloneList(l[i])!!)
            }
            for (i in localFs..localEe) {
                val vals = ll.removeFirst()
                val oVals = l[i]
                for (index in coi) {
                    val tVal = vals[index]
                    oVals[index] = tVal
                }
            }
            return true
        } else {
            return false
        }
    }

    @JvmStatic
    fun <E> moveDatas(list: MutableList<E>, start: Int, end: Int, to: Int) {
        if (start != to) {
            val first: Int
            var last: Int

            val partition = ArrayList<E>()

            if (start < to) {
                first = start
                last = to + end - start

                if (last >= list.size) {
                    last = list.size - 1
                }

                for (i in end + 1..last) {
                    partition.add(list[i])
                }

                for (i in start..end) {
                    partition.add(list[i])
                }
            } else {
                first = to
                last = end

                for (i in start..end) {
                    partition.add(list[i])
                }

                for (i in first..start - 1) {
                    partition.add(list[i])
                }
            }

            var i = first
            var c = 0
            while (i <= last) {
                list[i] = partition[c]
                i++
                c++
            }
        }
    }

    @JvmStatic
    fun <E> addDatas(list: MutableList<E>, vararg objs: E) {
        list.addAll(objs)
    }

    @JvmStatic
    fun <E> addDatas(list: MutableList<E>, objs: Collection<E>) {
        list.addAll(objs)
    }

    @JvmStatic
    fun <E> removeDatas(list: MutableList<E>, vararg objs: E) {
        list.removeAll(objs)
    }

    @JvmStatic
    fun <E> removeDatas(list: MutableList<E>, dataToBeRemoved: Collection<E>) {
        dataToBeRemoved.forEach({ list.remove(it) })
    }

    @JvmStatic
    fun <E> removeDatas(list: MutableList<E>, indexes: IntArray) {
        Arrays.sort(indexes)
        for (i in indexes.indices.reversed()) {
            val index = indexes[i]
            list.removeAt(index)
        }
    }

    @JvmStatic
    fun <E> reverseList(list: MutableList<E>) {
        var i = 0
        var j = list.size - 1
        while (j > i) {
            val t = list[i]
            list[i] = list[j]
            list[j] = t
            i++
            j--
        }
    }

    @JvmStatic
    fun within(arr: IntArray, i: Int): Boolean {
        for (v in arr) {
            if (i == v) {
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun within(arr: IntArray, i: Int, s: Int, e: Int): Boolean {
        var j = s
        while (j <= e && j < arr.size && j >= 0) {
            if (i == arr[j]) {
                return true
            }
            j++
        }
        return false
    }

    @JvmStatic
    fun within(arr: Array<String>, v: String): Boolean {
        for (sv in arr) {
            if (sv == v) {
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun within(arr: Array<String>, v: String, s: Int, e: Int): Boolean {
        var j = s
        while (j <= e && j < arr.size && j >= 0) {
            if (v == arr[j]) {
                return true
            }
            j++
        }
        return false
    }

    @JvmStatic
    fun within(arr: BooleanArray, i: Boolean): Boolean {
        for (bv in arr) {
            if (i == bv) {
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun within(arr: BooleanArray, i: Boolean, s: Int, e: Int): Boolean {
        var j = s
        while (j <= e && j < arr.size && j >= 0) {
            if (i == arr[j]) {
                return true
            }
            j++
        }
        return false
    }

    @JvmStatic
    fun within(arr: CharArray, i: Char): Boolean {
        for (bv in arr) {
            if (i == bv) {
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun within(arr: CharArray, i: Char, s: Int, e: Int): Boolean {
        var j = s
        while (j <= e && j < arr.size && j >= 0) {
            if (i == arr[j]) {
                return true
            }
            j++
        }
        return false
    }

    @JvmStatic
    fun within(arr: ShortArray, i: Short): Boolean {
        for (anArr in arr) {
            if (i == anArr)
                return true
        }
        return false
    }

    @JvmStatic
    fun within(arr: ShortArray, i: Short, s: Int, e: Int): Boolean {
        var j = s
        while (j <= e && j < arr.size && j >= 0) {
            if (i == arr[j]) {
                return true
            }
            j++
        }
        return false
    }

    @JvmStatic
    fun within(arr: LongArray, i: Long): Boolean {
        for (anArr in arr) {
            if (i == anArr)
                return true
        }
        return false
    }

    @JvmStatic
    fun within(arr: LongArray, i: Long, s: Int, e: Int): Boolean {
        var j = s
        while (j <= e && j < arr.size && j >= 0) {
            if (i == arr[j]) {
                return true
            }
            j++
        }
        return false
    }

    @JvmStatic
    fun within(arr: ByteArray, i: Byte): Boolean {
        for (anArr in arr) {
            if (i == anArr) {
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun within(arr: ByteArray, i: Byte, s: Int, e: Int): Boolean {
        var j = s
        while (j <= e && j < arr.size && j >= 0) {
            if (i == arr[j]) {
                return true
            }
            j++
        }
        return false
    }

    @JvmStatic
    fun within(arr: DoubleArray, i: Double): Boolean {
        for (anArr in arr) {
            if (i == anArr) {
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun within(arr: DoubleArray, i: Double, s: Int, e: Int): Boolean {
        var j = s
        while (j <= e && j < arr.size && j >= 0) {
            if (i == arr[j]) {
                return true
            }
            j++
        }
        return false
    }

    @JvmStatic
    fun within(arr: FloatArray, i: Float): Boolean {
        for (anArr in arr) {
            if (i == anArr) {
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun within(arr: FloatArray, i: Float, s: Int, e: Int): Boolean {
        var j = s
        while (j <= e && j < arr.size && j >= 0) {
            if (i == arr[j]) {
                return true
            }
            j++
        }
        return false
    }

    @JvmStatic
    fun <E, V> cloneMap(map: Map<E, V?>?): Map<E, V?>? {
        return cloneMap(map, false)
    }

    @JvmStatic
    fun <E, V> cloneMap(map: Map<E, V>?, deepClone: Boolean): Map<E, V?>? {
        if (map != null) {
            val newMap = HashMap<E, V?>(map.size)

            for ((k, v) in map) {
                if (deepClone) {
                    if (v is List<*>) {
                        newMap.put(k, cloneList(v, deepClone) as V?)
                    } else if (v is Map<*, *>) {
                        newMap.put(k, cloneMap(v, deepClone) as V?)
                    } else {
                        newMap.put(k, v)
                    }
                } else {
                    newMap.put(k, v)
                }
            }

            return newMap
        } else {
            return null
        }
    }

    @JvmStatic
    fun <E> cloneList(list: List<E>?): List<E>? {
        return cloneList(list, false)
    }

    @JvmStatic
    fun <E> cloneList(list: List<E>?, deepClone: Boolean): List<E>? {
        if (list != null) {
            val vect = Vector<E>(list.size)
            for (o in list) {
                if (deepClone) {
                    if (o is List<*>) {
                        vect.add(cloneList(o, deepClone) as E?)
                    } else if (o is Map<*, *>) {
                        vect.add(cloneMap(o, deepClone) as E?)
                    }
                } else {
                    vect.add(o)
                }
            }
            return vect
        } else {
            return null
        }
    }

    @JvmStatic
    fun indexOf(c: Array<String>, k: String): Int {
        for (i in c.indices) {
            if (c[i] == k) {
                return i
            }
        }
        return -1
    }

    @JvmStatic
    fun indexOf(s: ShortArray, k: Short): Int {
        for (i in s.indices) {
            if (s[i] == k) {
                return i
            }
        }
        return -1
    }

    @JvmStatic
    fun indexOf(l: LongArray, k: Long): Int {
        for (i in l.indices) {
            if (l[i] == k) {
                return i
            }
        }
        return -1
    }

    @JvmStatic
    fun indexOf(b: ByteArray, k: Byte): Int {
        for (i in b.indices) {
            if (b[i] == k) {
                return i
            }
        }
        return -1
    }

    @JvmStatic
    fun indexOf(f: FloatArray, k: Float): Int {
        for (i in f.indices) {
            if (f[i] == k) {
                return i
            }
        }
        return -1
    }

    @JvmStatic
    fun indexOf(b: BooleanArray, k: Boolean): Int {
        for (i in b.indices) {
            if (b[i] == k) {
                return i
            }
        }
        return -1
    }

    @JvmStatic
    fun indexOf(c: IntArray, k: Int): Int {
        for (i in c.indices) {
            if (c[i] == k) {
                return i
            }
        }
        return -1
    }

    @JvmStatic
    fun indexOf(c: CharArray, k: Char): Int {
        for (i in c.indices) {
            if (c[i] == k) {
                return i
            }
        }
        return -1
    }

    @JvmStatic
    fun indexOf(c: DoubleArray, k: Double): Int {
        for (i in c.indices) {
            if (c[i] == k) {
                return i
            }
        }
        return -1
    }

    @JvmStatic
    fun findValue(intArr: IntArray?, findType: MathValueType): Int {
        val isMin = findType == MathValueType.MIN
        var value = if (isMin) Integer.MAX_VALUE else Integer.MIN_VALUE
        if (intArr == null) {
            return if (isMin) Integer.MIN_VALUE else Integer.MAX_VALUE
        } else {
            for (v in intArr) {
                if (isMin && value > v) {
                    value = v
                } else if (!isMin && value < v) {
                    value = v
                }
            }
            return value
        }
    }

    @JvmStatic
    fun findMin(intArr: IntArray): Int {
        return findValue(intArr, MathValueType.MIN)
    }

    @JvmStatic
    fun findMax(intArr: IntArray): Int {
        return findValue(intArr, MathValueType.MAX)
    }

    @JvmStatic
    fun <E> addAllListDatas(datas: Collection<E>, target: MutableList<E>, distinct: Boolean) {
        for (data in datas) {
            if (distinct && !target.contains(data)) {
                target.add(data)
            } else {
                target.add(data)
            }
        }
    }

    @JvmStatic
    fun extractByte(o: Any?, defaultValue: Byte): Byte {
        val v = extractByte(o)

        return v ?: defaultValue
    }

    @JvmStatic
    fun extractByte(o: Any?): Byte? {
        if (o == null) {
            return null
        } else if (o is Byte) {
            return o
        } else if (o is ValueHolder<*>) {
            return extractByte(o.value)
        } else if (o is String) {
            val s = o.toLowerCase()

            if (s.length == 4 && s.startsWith("0x")) {
                return ((Character.digit(s[2], 16) shl 4) + Character.digit(s[3], 16)).toByte()
            } else if (s.length == 2) {
                return ((Character.digit(s[0], 16) shl 4) + Character.digit(s[1], 16)).toByte()
            } else {
                return null
            }
        } else {
            return null
        }
    }

    @JvmStatic
    fun extractShort(o: Any?, defaultValue: Short): Short {
        val v = extractShort(o)

        return v ?: defaultValue
    }

    @JvmStatic
    fun extractShort(o: Any?): Short? {
        if (o == null) {
            return null
        } else if (o is Short) {
            return o
        } else if (o is Number) {
            return o.toShort()
        } else if (o is ValueHolder<*>) {
            return extractShort(o.value)
        } else if (o is String || o.toString() != "") {
            val s = o.toString()
            try {
                return s.toShort()
            } catch (nfe: NumberFormatException) {
                return null
            }

        } else {
            return null
        }
    }

    @JvmStatic
    fun extractInteger(o: Any?, defaultValue: Int): Int {
        val v = extractInteger(o)

        return v ?: defaultValue
    }

    @JvmStatic
    fun extractInteger(o: Any?): Int? {
        if (o == null) {
            return null
        } else if (o is Int) {
            return o
        } else if (o is Number) {
            return o.toInt()
        } else if (o is ValueHolder<*>) {
            return extractInteger(o.value)
        } else if (o is String || o.toString() != "") {
            val s = o.toString()
            try {
                return s.toInt()
            } catch (nfe: NumberFormatException) {
                return null
            }

        } else {
            return null
        }
    }

    @JvmStatic
    fun extractLong(o: Any?, defaultValue: Long): Long {
        val v = extractLong(o)

        return v ?: defaultValue
    }

    @JvmStatic
    fun extractLong(o: Any?): Long? {
        if (o == null) {
            return null
        } else if (o is Long) {
            return o
        } else if (o is Number) {
            return o.toLong()
        } else if (o is ValueHolder<*>) {
            return extractLong(o.value)
        } else if (o is String || o.toString() != "") {
            val s = o.toString()
            try {
                return s.toLong()
            } catch (nfe: NumberFormatException) {
                return null
            }

        } else {
            return null
        }
    }

    @JvmStatic
    fun extractFloat(o: Any?, defaultValue: Float): Float {
        val v = extractFloat(o)

        return v ?: defaultValue
    }

    @JvmStatic
    fun extractFloat(o: Any?): Float? {
        if (o == null) {
            return null
        } else if (o is Float) {
            return o
        } else if (o is Number) {
            return o.toFloat()
        } else if (o is ValueHolder<*>) {
            return extractFloat(o.value)
        } else if (o is String || o.toString() != "") {
            val s = o.toString()

            try {
                return s.toFloat()
            } catch (nfe: NumberFormatException) {
                return null
            }

        } else {
            return null
        }
    }

    @JvmStatic
    fun extractDouble(o: Any?, defaultValue: Double): Double {
        val v = extractDouble(o)

        return v ?: defaultValue
    }

    @JvmStatic
    fun extractDouble(o: Any?): Double? {
        if (o == null) {
            return null
        } else if (o is Double) {
            return o
        } else if (o is Number) {
            return o.toDouble()
        } else if (o is ValueHolder<*>) {
            return extractDouble(o.value)
        } else if (o is String || o.toString() != "") {
            val s = o.toString()
            try {
                return s.toDouble()
            } catch (nfe: NumberFormatException) {
                return null
            }

        } else {
            return null
        }
    }

    @JvmStatic
    fun extractBigDecimal(o: Any?, defaultValue: BigDecimal): BigDecimal {
        val v = extractBigDecimal(o)

        return v ?: defaultValue
    }

    @JvmStatic
    fun extractBigDecimal(o: Any?): BigDecimal? {
        if (o == null) {
            return null
        } else if (o is BigDecimal) {
            return o
        } else if (o is Number) {
            return BigDecimal(o.toString())
        } else if (o is ValueHolder<*>) {
            return extractBigDecimal(o.value)
        } else if (o is String || o.toString() != "") {
            val s = o.toString().replace(",".toRegex(), "")
            try {
                return BigDecimal(s)
            } catch (nfe: NumberFormatException) {
                return null
            }

        } else {
            return null
        }
    }

    @JvmStatic
    fun extractBoolean(o: Any?, defaultValue: Boolean): Boolean {
        return extractBoolean(o) ?: defaultValue
    }

    @JvmStatic
    fun extractBoolean(o: Any?): Boolean? {
        if (o == null) {
            return null
        } else if (o is Boolean) {
            return o
        } else if (o is ValueHolder<*>) {
            return extractBoolean(o.value)
        } else if (o is String || o.toString() != "") {
            val s = o.toString()
            if (StringUtil.stringMatchOnceIgnoreCase(s, "y", "yes", "true")) {
                return true
            } else if (StringUtil.stringMatchOnceIgnoreCase(s, "n", "no", "false")) {
                return false
            } else {
                return null
            }
        } else {
            return null
        }
    }

    @JvmStatic
    fun extractString(o: Any?, defaultValue: String): String {
        val v = extractString(o)

        return v ?: defaultValue
    }

    @JvmStatic
    fun extractString(o: Any?): String? {
        if (o == null) {
            return null
        } else if (o is String) {
            return o
        } else if (o is ValueHolder<*>) {
            return extractString(o.value)
        } else {
            return o.toString()
        }
    }

    @JvmStatic
    fun DateCalendar(o: Any?, defaultValue: DateCalendar): DateCalendar {
        val v = extractDateCalendar(o)

        return v ?: defaultValue
    }

    @JvmStatic
    fun extractDateCalendar(o: Any?): DateCalendar? {
        if (o == null) {
            return null
        } else if (o is DateCalendar) {
            return o
        } else if (o is Date) {
            return DateCalendar(o)
        } else if (o is ValueHolder<*>) {
            return extractDateCalendar(o.value)
        } else if (o is String || o.toString() != "") {
            val s = o.toString()
            if (s.trim { it <= ' ' } == "") {
                return null
            } else {
                return DateCalendar(s, DateCalendar.DEFAULT_DATE_FORMAT)
            }
        } else {
            return null
        }
    }

    @JvmStatic
    fun extractDate(o: Any, defaultValue: Date): Date {
        val v = extractDate(o)

        return v ?: defaultValue
    }

    @JvmStatic
    fun extractDate(o: Any?): Date? {
        if (o == null) {
            return null
        } else if (o is Date) {
            return o
        } else if (o is ValueHolder<*>) {
            return extractDate(o.value)
        } else if (o is String || o.toString() != "") {
            val s = o.toString()
            if ("" == s.trim { it <= ' ' }) {
                return null
            } else {
                return DateTimeParser.parseTime(s, DateTimeParser.NORMAL_DATE_FORMAT)
            }
        } else {
            return null
        }
    }

    @JvmStatic
    fun extractDateTime(o: Any, defaultValue: Date): Date {
        val v = extractDateTime(o)

        return v ?: defaultValue
    }

    @JvmStatic
    fun extractDateTime(o: Any): Date? {
        return extractDate(o)
    }

    @JvmStatic
    fun <K, V> joinHashMap(a: Map<K, V>, b: Map<K, V>): Map<K, V> {
        val c = HashMap<K, V>()
        c.putAll(a)
        c.putAll(b)
        return c
    }

    @JvmStatic
    fun <K, E> getBigDecimal(map: Map<K, E>, key: K): BigDecimal? {
        return getBigDecimal(map, key, BigDecimal.ZERO)
    }

    @JvmStatic
    fun <K, E> getBigDecimal(map: Map<K, E>?, key: K, defaultValue: BigDecimal?): BigDecimal? {
        if (map == null || !map.containsKey(key) || map[key] == null) {
            return defaultValue
        } else {
            val v = map[key]

            if (v is BigDecimal) {
                return v
            } else {
                try {
                    return BigDecimal(v.toString())
                } catch (nfe: NumberFormatException) {
                    return defaultValue
                }

            }
        }
    }

    @JvmStatic
    fun <K, E> getDate(map: Map<K, E>, key: K): Date? {
        return getDate(map, key, null)
    }

    @JvmStatic
    fun <K, E> getDate(map: Map<K, E>?, key: K, defaultValue: Date?): Date? {
        if (map == null || !map.containsKey(key)) {
            return defaultValue
        } else {
            val v = map[key]

            if (v == null) {
                return defaultValue
            } else if (v is Date) {
                return v
            } else if (v is String) {
                val dateValue = DateTimeParser.parse(v as String?, DateTimeParser.NORMAL_DATE_FORMAT)

                if (dateValue == null) {
                    return defaultValue
                } else {
                    return dateValue
                }
            } else {
                return defaultValue
            }
        }
    }

    @JvmStatic
    fun <K, E> getIntValue(map: Map<K, E>, key: K): Int {
        return getIntValue(map, key, 10, -1)
    }

    @JvmStatic
    fun <K, E> getIntValue(map: Map<K, E>, key: K, radix: Int): Int {
        return getIntValue(map, key, radix, -1)
    }

    @JvmStatic
    fun <K, E> getIntValue(map: Map<K, E>?, key: K, radix: Int, defaultValue: Int): Int {
        if (map == null || !map.containsKey(key) || map[key] == null) {
            return defaultValue
        } else {
            val v = map[key]

            if (v is Int) {
                return v
            } else if (v is Long) {
                return v.toInt()
            } else if (v is Float) {
                return v.toInt()
            } else if (v is Double) {
                return v.toInt()
            } else {
                return MathUtil.parseInt(v.toString(), radix, defaultValue)
            }
        }
    }

    @JvmStatic
    fun <K, E> getBooleanValue(map: Map<K, E>, key: K): Boolean {
        return getBooleanValue(map, key, false)
    }

    @JvmStatic
    fun <K, E> getBooleanValue(map: Map<K, E>?, key: K, defaultValue: Boolean): Boolean {
        if (map == null || !map.containsKey(key) || map[key] == null) {
            return defaultValue
        } else {
            val v = map[key]

            if (v is Boolean) {
                return v
            } else {
                return java.lang.Boolean.parseBoolean(v.toString())
            }
        }
    }

    @JvmStatic
    fun <K, E> getDoubleValue(map: Map<K, E>, key: K): Double {
        return getDoubleValue(map, key, -1.0)
    }

    @JvmStatic
    fun <K, E> getDoubleValue(map: Map<K, E>?, key: K, defaultValue: Double): Double {
        if (map == null || !map.containsKey(key) || map[key] == null) {
            return defaultValue
        } else {
            val v = map[key]

            if (v is Int) {
                return v.toDouble()
            } else if (v is Long) {
                return v.toDouble()
            } else if (v is Float) {
                return v.toDouble()
            } else if (v is Double) {
                return v
            } else {
                return MathUtil.parseDouble(v.toString(), defaultValue)
            }
        }
    }

    @JvmStatic
    fun <K, E> getLongValue(map: Map<K, E>, key: K): Long {
        return getLongValue(map, key, 10, -1)
    }

    @JvmStatic
    fun <K, E> getLongValue(map: Map<K, E>, key: K, radix: Int): Long {
        return getLongValue(map, key, radix, -1)
    }

    @JvmStatic
    fun <K, E> getLongValue(map: Map<K, E>?, key: K, radix: Int, defaultValue: Long): Long {
        if (map == null || !map.containsKey(key) || map[key] == null) {
            return defaultValue
        } else {
            val v = map[key]
            if (v is Int) {
                return v.toLong()
            } else if (v is Long) {
                return v
            } else if (v is Float) {
                return v.toLong()
            } else if (v is Double) {
                return v.toLong()
            } else {
                return MathUtil.parseLong(v.toString(), radix, defaultValue)
            }
        }
    }

    @JvmStatic
    fun <E, C> getMapString(map: Map<E, C>): String {
        val builder = StringBuilder()

        builder.append("{")

        var first = true

        for ((key, value) in map) {
            if (first) {
                first = false
            } else {
                builder.append(", ")
            }

            builder.append(key.toString() + ": ")

            if (value == null) {
                builder.append("null")
            } else {
                builder.append(value.toString())
            }
        }

        builder.append("}")

        return builder.toString()
    }

    @JvmStatic
    fun <E, C> containOneKey(map: Map<E, C>, vararg keys: E): Boolean {
        for (key in keys) {
            if (map.containsKey(key)) {
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun <E, C> containAllKeys(map: Map<E, C>, vararg keys: E): Boolean {
        for (key in keys) {
            if (!map.containsKey(key)) {
                return false
            }
        }
        return true
    }

    @JvmStatic
    fun <E> getListString(list: List<E>): String {
        val builder = StringBuilder()

        builder.append('[')

        var first = true

        for (ele in list) {
            if (first) {
                first = false
            } else {
                builder.append(", ")
            }

            if (ele == null) {
                builder.append("null")
            } else {
                builder.append(ele.toString())
            }
        }

        builder.append(']')

        return builder.toString()
    }

    @JvmStatic
    fun <K, V> getOrDefault(map: Map<K, V>, key: K, defaultValue: V?): V? {
        if (map.containsKey(key)) {
            val v = map[key]
            return v ?: defaultValue
        } else {
            return defaultValue
        }
    }

    @JvmStatic
    fun <K, V> getStringValue(map: Map<K, V>, key: K, defaultValue: String?): String? {
        if (map.containsKey(key)) {
            val value = map[key]

            return if (value != null) value.toString() else defaultValue
        } else {
            return defaultValue
        }
    }

    @JvmStatic
    fun <E> getValue(map: Map<String, E>, key: String, defaultValue: E?): E? {
        if (map.containsKey(key)) {
            val v = map[key]
            if (v == null) {
                return defaultValue
            } else {
                return v
            }
        } else {
            return defaultValue
        }
    }

    @JvmStatic
    fun <E> setListData(list: MutableList<E?>, index: Int, value: E) {
        setListData(list, index, value, null)
    }

    @JvmStatic
    fun <E> setListData(list: MutableList<E?>, index: Int, value: E, fillValue: E?) {
        if (index < 0) {
            return
        }
        if (index < list.size) {
            list[index] = value
        } else {
            while (list.size <= index) {
                list.add(fillValue)
            }
            list.add(value)
        }
    }

    @JvmStatic
    fun <E> copyArrayToList(list: MutableList<E>, array: Array<E>) {
        val iter = list.listIterator()
        for (anArray in array) {
            iter.next()
            iter.set(anArray)
        }
    }

    @JvmStatic
    fun hashCode(vararg objs: Any): Int {
        var hashCode = 0
        for (obj in objs) {
            hashCode = 31 * hashCode + obj.hashCode()
        }
        return hashCode
    }

    @JvmStatic
    fun <E> hasDuplicates(list: List<E>): Boolean {
        for (i in 0..list.size - 1 - 1) {
            for (j in i + 1..list.size - 1) {
                if (list[i] == list[j]) {
                    return true
                }
            }
        }

        return false
    }

    @JvmStatic
    fun <K, V> createSimpleMap(k: K, v: V): Map<K, V> {
        val map = HashMap<K, V>()
        map.put(k, v)
        return map
    }

    @JvmStatic
    fun <K, V> createMap(vararg pairs: KeyValuePair<K, V>): Map<K, V> {
        val map = HashMap<K, V>(pairs.size)

        for (pair in pairs) {
            map.put(pair.key, pair.value)
        }

        return map
    }

    @JvmStatic
    fun <K, V> copyMapByKeys(sourceMap: Map<K, V?>?, targetMap: MutableMap<K, V?>?, vararg keys: K) {
        if (sourceMap != null && targetMap != null) {
            for (key in keys) {
                targetMap.put(key, sourceMap[key])
            }
        }
    }

    @JvmStatic
    fun <K, V> createLinkedMap(vararg pairs: KeyValuePair<K, V>): Map<K, V> {
        val map = LinkedHashMap<K, V>(pairs.size)

        for (pair in pairs) {
            map.put(pair.key, pair.value)
        }

        return map
    }

    @JvmStatic
    fun <K, V> createMap(mapClass: Class<out MutableMap<*, *>>, keys: List<K>, values: List<V>): Map<K, V>? {
        try {
            val resultMap = mapClass.newInstance() as MutableMap<K, V>

            val keyIter = keys.listIterator()
            val valueIter = values.listIterator()

            while (keyIter.hasNext() && valueIter.hasNext()) {
                resultMap.put(keyIter.next(), valueIter.next())
            }

            return resultMap
        } catch (e: InstantiationException) {
            e.printStackTrace()
            return null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return null
        }

    }

    @JvmStatic
    fun <E> createListWrapper(element: E): List<E> {
        val list = ArrayList<E>()
        list.add(element)
        return list
    }

    @JvmStatic
    fun createSequence(start: Int, end: Int): List<Int> {
        var localStart = start
        var localEnd = end
        if (localEnd < localStart) {
            val t = localEnd
            localEnd = localStart
            localStart = t
        }
        val seq = ArrayList<Int>()

        for (i in localStart..localEnd) {
            seq.add(i)
        }

        return seq
    }

    @JvmStatic
    fun <E> createList(list: List<E>, indexes: List<Int>): List<E> {
        val r = ArrayList<E>(indexes.size)

        for (index in indexes) {
            r.add(list[index])
        }

        return r
    }

    @JvmStatic
    fun <E> createDefaultList(length: Int, defaultValue: E): List<E> {
        val list = ArrayList<E>(length)

        for (i in 0..length - 1) {
            list.add(defaultValue)
        }

        return list
    }

    @JvmStatic
    fun <E> createList(vararg values: E): List<E> {
        val list = ArrayList<E>(values.size)

        Collections.addAll(list, *values)

        return list
    }

    @JvmStatic
    fun <E> createList(listclass: Class<out MutableList<*>>, vararg values: E): List<E>? {
        try {
            val list = listclass.newInstance() as MutableList<E>

            for (value in values) {
                list.add(value)
            }

            return list
        } catch (e: InstantiationException) {
            e.printStackTrace()
            return null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return null
        }

    }

    @JvmStatic
    fun <K, V> sortListOfMaps(maps: List<Map<K, V>>, vararg orderKeys: K) {
        sortListOfMaps(maps, true, true, *orderKeys)
    }

    @JvmStatic
    fun <K, V> sortListOfMaps(
            maps: List<Map<K, V>>, asc: Boolean, nullAsSmaller: Boolean, vararg orderKeys: K
    ) {
        Collections.sort(maps) 
        comparator@{ 
            o1, o2 ->
                for (orderKey in orderKeys) {
                    val v1 = o1[orderKey]
                    val v2 = o2[orderKey]
    
                    if (v1 != v2) {
                        return@comparator DataComparator.compare<V>(v1, v2, asc, nullAsSmaller)
                    }
                }
    
                return@comparator 0
        }
    }

    @JvmStatic
    fun <K> adjustMapIntegerValue(map: MutableMap<K, Any>, key: K, adjustValue: Int): Int {
        var result = getIntValue(map, key, 10, 0)

        result += adjustValue

        map.put(key, result)

        return result
    }

    @JvmStatic
    fun <K> adjustMapBigDecimalValue(map: MutableMap<K, Any>, key: K, adjustValue: BigDecimal): BigDecimal {
        var result = getBigDecimal(map, key, BigDecimal.ZERO)!!

        result = result.add(adjustValue)

        map.put(key, result)

        return result
    }
    
    @JvmStatic
    fun integerSumOf(list: Collection<Int>): Int {
        var result = 0

        for (v in list) {
            result += v
        }

        return result
    }

    @JvmStatic
    fun integerSumOfList(list: List<Int>, start: Int, end: Int): Int {
        var result = 0

        var i = start
        while (i <= end && i >= 0 && i < list.size) {
            result += list[i].toInt()
            i++
        }

        return result
    }

    @JvmStatic
    fun bigDecimalSumOf(list: Collection<BigDecimal>): BigDecimal {
        var result = BigDecimal.ZERO

        for (v in list) {
            result = result.add(v)
        }

        return result
    }

    @JvmStatic
    fun bigDecimalSumOfList(list: List<BigDecimal>, start: Int, end: Int): BigDecimal {
        var result = BigDecimal.ZERO

        var i = start
        while (i <= end && i >= 0 && i < list.size) {
            result = result.add(list[i])
            i++
        }

        return result
    }

    @JvmStatic
    fun <K> integerSumOfMapData(
            map: Map<K, Any>, vararg keys: K): Int {
        var result = 0

        for (key in keys) {
            result += getIntValue(map, key, 10, 0)
        }

        return result
    }

    @JvmStatic
    fun <K> bigDecimalSumOfMapData(
            map: Map<K, Any>, vararg keys: K): BigDecimal {
        var result = BigDecimal.ZERO

        for (key in keys) {
            result = result.add(getBigDecimal(map, key, BigDecimal.ZERO))
        }

        return result
    }

    @JvmStatic
    fun <K> adjustMapLongValue(map: MutableMap<K, Any>, key: K, adjustValue: Long?): Long {
        var result = getLongValue(map, key, 10, 0)

        result += adjustValue!!

        map.put(key, result)

        return result
    }
    
    @JvmStatic 
    fun <K, V> createMap(keys: List<K>, values: List<V>): Map<K, V> {
        var i = 0
        
        val result = HashMap<K, V>()
        
        while (i < keys.size && i < values.size) {
            result[keys[i]] = values[i]
            i++
        }
        
        return result
    }

    @JvmStatic
    fun <K, V> createMapList(keys: List<K>, vararg valueLists: List<V>): List<Map<K, V>> {
        val mapList = ArrayList<Map<K, V>>()

        for (values in valueLists) {
            mapList.add(createMap(keys, values))
        }

        return mapList
    }

    @JvmStatic
    fun <K, V> createMapList(keys: List<K>, valueLists: List<List<V>>): List<Map<K, V>> {
        val mapList = ArrayList<Map<K, V>>()

        for (values in valueLists) {
            mapList.add(createMap(keys, values))
        }

        return mapList
    }

    @JvmStatic
    fun <K, V> retainMapKeys(map: MutableMap<K, V>, vararg keys: K) {
        val iter = map.iterator()
        
        while (iter.hasNext()) {
            val entry = iter.next()
            
            if (!keys.contains(entry.key)) {
                iter.remove()
            }
        }
    }
    
    @JvmStatic
    @JvmOverloads
    fun <E> shuffle(list: MutableList<E>, start: Int = 0, end: Int = list.size-1) {
        for (i in end downTo start + 1) {
            swapData(list, i, MathUtil.randomInteger(start, i))
        }
    }
    
    @JvmStatic
    @JvmOverloads
    fun <E> shuffle(array: Array<E>, start: Int = 0, end: Int = array.size-1) {
        for (i in end downTo start + 1) {
            swapData(array, i, MathUtil.randomInteger(start, i))
        }
    }
}