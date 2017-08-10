package testng

import org.testng.Assert.*

import java.util.ArrayList

import org.testng.annotations.Test
import utils.data.Cost
import utils.data.SearchTree
import utils.data.SearchTreeNode
import utils.math.MathUtil

class TestSearchTree {
    @Test
    fun testGetMinNode() {
        val rn1 = SearchTreeNode<Int?>(5, CostOfInteger(5))
        val rn2 = SearchTreeNode<Int?>(6, CostOfInteger(6))
        val rn3 = SearchTreeNode<Int?>(8, CostOfInteger(8))

        val t1 = SearchTree<Int?>(rn1)
        val t2 = SearchTree<Int?>(rn2)
        val t3 = SearchTree<Int?>(rn3)

        addNodes(4, 18, rn1)
        addNodes(4, 18, rn2)
        addNodes(4, 18, rn3)

        val oi1 = getMinCostNode(t1)
        val oi2 = getMinCostNode(t2)
        val oi3 = getMinCostNode(t3)
        val moi1 = getMaxCostNode(t1)
        val moi2 = getMaxCostNode(t2)
        val moi3 = getMaxCostNode(t3)

        val ni1 = t1.minCostLeaf
        val ni2 = t2.minCostLeaf
        val ni3 = t3.minCostLeaf
        val mni1 = t1.maxCostLeaf
        val mni2 = t2.maxCostLeaf
        val mni3 = t3.maxCostLeaf

        assertEquals(getTotalCost(oi1), getTotalCost(ni1))
        assertEquals(getTotalCost(oi2), getTotalCost(ni2))
        assertEquals(getTotalCost(oi3), getTotalCost(ni3))
        assertEquals(getTotalCost(moi1), getTotalCost(mni1))
        assertEquals(getTotalCost(moi2), getTotalCost(mni2))
        assertEquals(getTotalCost(moi3), getTotalCost(mni3))

        assertEquals(oi1.getData(), ni1!!.getData())
        assertEquals(oi2.getData(), ni2!!.getData())
        assertEquals(oi3.getData(), ni3!!.getData())
        assertEquals(moi1.getData(), mni1!!.getData())
        assertEquals(moi2.getData(), mni2!!.getData())
        assertEquals(moi3.getData(), mni3!!.getData())
    }

    fun addNodes(levelToGo: Int, childNodeCount: Int, node: SearchTreeNode<Int?>) {
        if (levelToGo <= 0) {
            return
        }
        for (i in 0..childNodeCount - 1) {
            val v = MathUtil.randomInteger(0, 10)
            addNodes(levelToGo - 1, childNodeCount, node.addChildData(v, CostOfInteger(v)))
        }
    }

    fun getTotalCost(node: SearchTreeNode<Int?>?): Int {
        var localNode = node
        var cost = 0
        while (localNode !== null) {
            cost += localNode.getData()!!
            localNode = localNode.getParentNode()
        }
        return cost
    }

    fun getMinCostNode(tree: SearchTree<Int?>): SearchTreeNode<Int?> {
        var minCost = Integer.MAX_VALUE
        var minIndex = -1

        val nodes = tree.getAllLeafNode()!!
        
        for (i in nodes.indices) {
            var cost = 0
            var c: SearchTreeNode<Int?>? = nodes[i]
            while (c !== null) {
                cost += c.getData()!!
                c = c.getParentNode()
            }
            if (cost < minCost) {
                minCost = cost
                minIndex = i
            }
        }
        return nodes[minIndex]
    }

    fun getMaxCostNode(tree: SearchTree<Int?>): SearchTreeNode<Int?> {
        var maxCost = Integer.MIN_VALUE
        var maxIndex = -1

        val nodes = tree.getAllLeafNode()

        for (i in nodes!!.indices) {
            var cost = 0
            var c: SearchTreeNode<Int?>? = nodes[i]
            while (c !== null) {
                cost += c.getData()!!
                c = c.getParentNode()
            }
            if (cost > maxCost) {
                maxCost = cost
                maxIndex = i
            }
        }
        return nodes[maxIndex]
    }

    private inner class CostOfInteger(val cost: Int) : Cost {
        override fun plus(cost: Cost): Cost {
            return CostOfInteger(this.cost + (cost as CostOfInteger).cost)
        }

        override fun minus(cost: Cost): Cost {
            return CostOfInteger(this.cost - (cost as CostOfInteger).cost)
        }

        override fun compareTo(other: Cost): Int {
            return cost - (other as CostOfInteger).cost
        }

        override fun clone(): CostOfInteger {
            return CostOfInteger(cost)
        }
    }
}
