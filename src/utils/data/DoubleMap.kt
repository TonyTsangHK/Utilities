package utils.data

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
    private lateinit var valueReferencedMap: MutableMap<V, MutableSet<K>>

    constructor(initialCapacity: Int) {
        keyReferencedMap = HashMap(initialCapacity)
        valueReferencedMap = HashMap(initialCapacity)
    }

    constructor() {
        keyReferencedMap = HashMap()
        valueReferencedMap = HashMap()
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

    fun getByValue(value: V): Set<K>? {
        return valueReferencedMap[value]
    }
    
    override fun put(key: K, value: V): V? {
        if (keyReferencedMap.containsKey(key)) {
            val oldValue = keyReferencedMap[key]
            val keyList = valueReferencedMap[oldValue]!!
            
            if (keyList.size > 1) {
                keyList.remove(key)
            } else {
                valueReferencedMap.remove(oldValue)
            }
            
            if (valueReferencedMap.containsKey(value)) {
                valueReferencedMap[value]!!.add(key)
            } else {
                valueReferencedMap[value] = mutableSetOf(key)
            }
            return keyReferencedMap.put(key, value)
        } else {
            keyReferencedMap[key] = value
            
            if (valueReferencedMap.containsKey(value)) {
                valueReferencedMap[value]!!.add(key)
            } else {
                valueReferencedMap[value] = mutableSetOf(key)
            }
            
            return value
        }
    }

    override fun remove(key: K): V? {
        if (keyReferencedMap.containsKey(key)) {
            val v = keyReferencedMap.remove(key)
            
            val keyList = valueReferencedMap[v]!!
            
            if (keyList.size > 1) {
                keyList.remove(key)
            } else {
                valueReferencedMap.remove(v)
            }
            
            return v
        } else {
            return null
        }
    }

    fun removeValue(value: V): Set<K>? {
        if (valueReferencedMap.containsKey(value)) {
            val k = valueReferencedMap.remove(value)
            
            k!!.forEach {
                keyReferencedMap.remove(it)
            }
            
            return k
        } else {
            return null
        }
    }

    override fun putAll(from: Map<out K, V>) {
        from.forEach {
            put(it.key, it.value)
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
        return keyReferencedMap.keys
    }

    override fun equals(other: Any?): Boolean {
        return keyReferencedMap == other
    }

    override fun hashCode(): Int {
        return keyReferencedMap.hashCode()
    }
}
