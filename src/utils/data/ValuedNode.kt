package utils.data;

class ValuedNode<T: Comparable<T>>: Comparable<ValuedNode<T>> {
    var parent: ValuedNode<T>?
        private set
    var data: T
        private set
    private val children: MinMaxQueue<ValuedNode<T>>
    
    var culminlativeCost: Cost
        private set
    
    constructor(data: T) {
        this.data = data
        this.parent = null
        children = MinMaxQueue<ValuedNode<T>>()
        culminlativeCost = Cost()
    }
    
    override fun compareTo(other: ValuedNode<T>): Int {
        return data.compareTo(other.data)
    }
    
    fun getParentData(): T? {
        return parent?.data
    }
    
    fun hasChild(): Boolean {
        return children.isNotEmpty()
    }
    
    fun getChildSize(): Int {
        return children.size
    }
    
    fun addChild(data: T) {
        val n = ValuedNode<T>(data)
        n.parent = this
        children.add(n)
    }
    
    fun addChild(node: ValuedNode<T>) {
        node.parent = this
        children.add(node)
    }
    
    fun getChild(index: Int): ValuedNode<T>? {
        if (index >= 0 && index < children.size) {
            return children.get(index)
        } else {
            return null
        }
    }
    
    fun getMinChild(): ValuedNode<T> {
        return children.getMin()
    }
    
    fun getMaxChild(): ValuedNode<T> {
        return children.getMax()
    }
    
    fun getChildData(index: Int): T? {
        if (index >= 0 && index < children.size) {
            return children.get(index).data
        } else {
            return null
        }
    }
    
    class Cost : Comparable<Cost> {
        var cost: Double = 0.0

        fun increment(c: Double) {
            cost += c;
        }
        
        override fun compareTo(other: Cost): Int {
            return (cost - other.cost).toInt()
        }

        init {
            cost = 0.0
        }
    }
}
