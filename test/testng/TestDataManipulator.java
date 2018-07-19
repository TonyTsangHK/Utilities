package testng;

import org.testng.annotations.Test;
import utils.data.DataManipulator;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class TestDataManipulator {
    @Test
    public void testReverseList() {
        List<Integer> list1 = new ArrayList<Integer>(),
            list2 = new ArrayList<Integer>();
        
        for (int i = 1; i <= 12; i++) {
            if (i > 9) {
                list1.add(new Integer(i));
            } else {
                list1.add(new Integer(i));
                list2.add(new Integer(i));
            }
        }
        DataManipulator.reverseList(list1);
        DataManipulator.reverseList(list2);
        
        int c = 12;
        for (int i = 0; i < list1.size(); i++) {
            assertEquals(list1.get(i), new Integer(c--));
        }
        
        c = 9;
        for (int i = 0; i < list2.size(); i++) {
            assertEquals(list2.get(i), new Integer(c--));
        }
    }
}
