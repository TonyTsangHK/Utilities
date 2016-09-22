package utils.data

import java.io.Serializable

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2016-09-22
 * Time: 16:19
 */
class DoublyNode<V>: Serializable{
    var data: V
    var previous: DoublyNode<V>? = null
        internal set(node: DoublyNode<V>?) {
            field = node
            if (node != null && node.next != this) {
                node.next = this
            }
        }
    var next:DoublyNode<V>? = null
        internal set(node: DoublyNode<V>?) {
            field = node
            if (node != null && node.previous != this) {
                node.previous = this
            }
        }

    constructor(data: V): this(null, null, data)

    constructor(p: DoublyNode<V>?, n: DoublyNode<V>?, data: V) {
        this.data = data
        this.previous = p
        this.next = n
    }

    fun getNode(shift: Int): DoublyNode<V>? {
        if (shift == 0) {
            return this
        } else if (shift < 0) {
            if (previous != null) {
                return previous!!.getNode(shift + 1)
            } else {
                return null
            }
        } else {
            if (next != null) {
                return next!!.getNode(shift - 1)
            } else {
                return null
            }
        }
    }

    fun locateNode(i: Int): DoublyNode<V>? {
        var i = i
        var n: DoublyNode<V>? = this
        if (i > 0) {
            while (i > 0) {
                n = n!!.next
                if (n == null) {
                    return null
                }
                i--
            }
            return n
        } else if (i < 0) {
            while (i < 0) {
                n = n!!.previous
                if (n == null) {
                    return null
                }
                i++
            }
            return n
        } else {
            return this
        }
    }

    fun hasPrevious(): Boolean {
        return previous != null
    }

    operator fun hasNext(): Boolean {
        return next != null
    }
}