package utils.data

interface Cost: Comparable<Cost>, Cloneable {
    operator fun plus(cost: Cost): Cost
    operator fun minus(cost: Cost): Cost
    public override fun clone(): Cost
}
