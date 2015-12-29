package testng;

import static org.testng.Assert.*;

import java.util.ArrayList;

import org.testng.annotations.Test;
import utils.data.Cost;
import utils.data.SearchTree;
import utils.data.SearchTreeNode;
import utils.math.MathUtil;

public class TestSearchTree {
    @Test
    public void testGetMinNode() {
        SearchTreeNode<Integer> rn1 = new SearchTreeNode<Integer>(5, new CostOfInteger(5)),
            rn2 = new SearchTreeNode<Integer>(6, new CostOfInteger(6)), rn3 = new SearchTreeNode<Integer>(8, new CostOfInteger(8));
        
        SearchTree<Integer> t1 = new SearchTree<Integer>(rn1),
            t2 = new SearchTree<Integer>(rn2), t3 = new SearchTree<Integer>(rn3);
        
        addNodes(4, 18, rn1);
        addNodes(4, 18, rn2);
        addNodes(4, 18, rn3);

        SearchTreeNode<Integer> oi1 = getMinCostNode(t1),
            oi2 = getMinCostNode(t2), oi3 = getMinCostNode(t3),
            moi1 = getMaxCostNode(t1), moi2 = getMaxCostNode(t2),
            moi3 = getMaxCostNode(t3);

        SearchTreeNode<Integer> ni1 = t1.getMinCostLeaf(),
            ni2 = t2.getMinCostLeaf(), ni3 = t3.getMinCostLeaf(),
            mni1 = t1.getMaxCostLeaf(), mni2 = t2.getMaxCostLeaf(),
            mni3 = t3.getMaxCostLeaf();

        assertEquals(getTotalCost(oi1), getTotalCost(ni1));
        assertEquals(getTotalCost(oi2), getTotalCost(ni2));
        assertEquals(getTotalCost(oi3), getTotalCost(ni3));
        assertEquals(getTotalCost(moi1), getTotalCost(mni1));
        assertEquals(getTotalCost(moi2), getTotalCost(mni2));
        assertEquals(getTotalCost(moi3), getTotalCost(mni3));
        
        assertEquals(oi1.getData(), ni1.getData());
        assertEquals(oi2.getData(), ni2.getData());
        assertEquals(oi3.getData(), ni3.getData());
        assertEquals(moi1.getData(), mni1.getData());
        assertEquals(moi2.getData(), mni2.getData());
        assertEquals(moi3.getData(), mni3.getData());
    }
    
    public void addNodes(int levelToGo, int childNodeCount, SearchTreeNode<Integer> node) {
        if (levelToGo <= 0) {
            return;
        }
        for (int i = 0; i < childNodeCount; i++) {
            int v = MathUtil.randomInteger(0, 10);
            addNodes(levelToGo-1, childNodeCount, node.addChildData(v, new CostOfInteger(v)));
        }
    }
    
    public int getTotalCost(SearchTreeNode<Integer> node) {
        int cost = 0;
        while (node != null) {
            cost += node.getData();
            node = node.getParentNode();
        }
        return cost;
    }
    
    public SearchTreeNode<Integer> getMinCostNode(SearchTree<Integer> tree) {
        int minCost = Integer.MAX_VALUE, minIndex = -1;
        
        ArrayList<SearchTreeNode<Integer>> nodes = tree.getAllLeafNode();
        
        for (int i = 0; i < nodes.size(); i++) {
            int cost = 0;
            SearchTreeNode<Integer> c = nodes.get(i);
            while (c != null) {
                cost += c.getData();
                c = c.getParentNode();
            }
            if (cost < minCost) {
                minCost = cost;
                minIndex = i;
            }
        }
        return nodes.get(minIndex);
    }
    
    public SearchTreeNode<Integer> getMaxCostNode(SearchTree<Integer> tree) {
        int maxCost = Integer.MIN_VALUE, maxIndex = -1;
        
        ArrayList<SearchTreeNode<Integer>> nodes = tree.getAllLeafNode();
        
        for (int i = 0; i < nodes.size(); i++) {
            int cost = 0;
            SearchTreeNode<Integer> c = nodes.get(i);
            while (c != null) {
                cost += c.getData();
                c = c.getParentNode();
            }
            if (cost > maxCost) {
                maxCost = cost;
                maxIndex = i;
            }
        }
        return nodes.get(maxIndex);
    }
    
    private class CostOfInteger implements Cost {
        private int cost;
        
        public CostOfInteger(int c) {
            cost = c;
        }
        
        public int getCost() {
            return cost;
        }

        @Override
        public Cost add(Cost cost) {
            return new CostOfInteger(this.cost + ((CostOfInteger)cost).getCost());
        }

        @Override
        public Cost minus(Cost cost) {
            return new CostOfInteger(this.cost - ((CostOfInteger)cost).getCost());
        }

        @Override
        public int compareTo(Cost o) {
            return cost - ((CostOfInteger)o).getCost();
        }
        
        @Override
        public CostOfInteger clone() {
            return new CostOfInteger(cost);
        }
    }
}
