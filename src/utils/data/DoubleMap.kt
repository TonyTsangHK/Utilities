package utils.data

import utils.exception.DuplicateReferenceException
import java.util.HashMap

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2013-10-02
 * Time: 09:25
 */
class DoubleMap<K, V> : MutableMap<K, V> {
    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = keyReferencedMap.entries
    override val keys: MutableSet<K>
        get() = keyReferencedMap.keys
    override val size: Int
        get() = keyReferencedMap.size
    override val values: MutableCollection<V>
        get() = keyReferencedMap.values

    private lateinit var keyReferencedMap: MutableMap<K, V>
    private lateinit var valueReferencedMap: MutableMap<V, K>

    constructor(initialCapacity: Int) {
        keyReferencedMap = HashMap<K, V>(initialCapacity)
        valueReferencedMap = HashMap<V, K>(initialCapacity)
    }

    constructor() {
        keyReferencedMap = HashMap<K, V>()
        valueReferencedMap = HashMap<V, K>()
    }

    constructor(m: Map<out K, V>) : this() {
        putAll(m)
    }

    override fun isEmpty(): Boolean {
        return keyReferencedMap.isEmpty()
    }

    override fun containsKey(key: K): Boolean {
        return keyReferencedMap.containsKey(key)
    }

    override fun containsValue(value: V): Boolean {
        return valueReferencedMap.containsKey(value)
    }

    override operator fun get(key: K): V? {
        return keyReferencedMap[key]
    }

    fun getByValue(value: V): K? {
        return valueReferencedMap[value]
    }
    
    override fun put(key: K, value: V): V? {
        if (keyReferencedMap.containsKey(key)) {
            valueReferencedMap.remove(keyReferencedMap[key])
            valueReferencedMap.put(value, key)
            return keyReferencedMap.put(key, value)
        } else {
            if (valueReferencedMap.containsKey(value)) {
                throw DuplicateReferenceException(
                        value.toString() + " is referencing " + valueReferencedMap[value] + ", key: " + key + " reference duplicated!")
            } else {
                keyReferencedMap.put(key, value)
                valueReferencedMap.put(value, key)
                return value
            }
        }
    }

    override fun remove(key: K): V? {
        if (keyReferencedMap.containsKey(key)) {
            val v = keyReferencedMap.remove(key)
            valueReferencedMap.remove(v)
            return v
        } else {
            return null
        }
    }

    fun removeValue(value: V): K? {
        if (valueReferencedMap.containsKey(value)) {
            val k = valueReferencedMap.remove(value)
            keyReferencedMap.remove(k)
            return k
        } else {
            return null
        }
    }

    override fun putAll(from: Map<out K, V>) {
        for ((key, value) in from) {
            put(key, value)
        }
    }

    override fun clear() {
        keyReferencedMap.clear()
        valueReferencedMap.clear()
    }

    fun valueSet(): Set<V> {
        return valueReferencedMap.keys
    }

    fun keys(): Collection<K> {
        return valueReferencedMap.values
    }

    fun valueEntrySet(): MutableSet<MutableMap.MutableEntry<V, K>> {
        return valueReferencedMap.entries
    }

    override fun equals(other: Any?): Boolean {
        return keyReferencedMap == other
    }

    override fun hashCode(): Int {
        return keyReferencedMap.hashCode()
    }
}
