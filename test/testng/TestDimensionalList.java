/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package testng;

import org.testng.annotations.Test;
import utils.data.DimensionalIndex;
import utils.data.DimensionalList;

import java.util.ArrayList;

import static org.testng.Assert.assertEquals;

/**
 *
 * @author Tony Tsang
 */
public class TestDimensionalList {
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testAll() {
        DimensionalList<Integer> list = new DimensionalList(3);
        
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 4; k++) {
                    Integer v = i * 100 + j * 10 + k;
                    list.setData(v, i, j, k);
                }
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 4; k++) {
                    Integer v = list.getData(i, j, k);
                    assertEquals(new Integer(i*100 + j*10 + k), v);
                }
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 4; k++) {
                    Integer v = list.getData(i, j, k);
                    DimensionalIndex index = list.indexOf(v), createdIndex = new DimensionalIndex(i, j, k);
                    assertEquals(index, createdIndex);
                }
            }
        }

        assertEquals(new Integer(list.getLength()), new Integer(2));
        for (int i = 0; i < 2; i++) {
            assertEquals(new Integer(list.getLength(i)), new Integer(3));
            for (int j = 0; j < 3; j++) {
                assertEquals(new Integer(list.getLength(i, j)), new Integer(4));
                for (int k = 0; k < 4; k++) {
                    assertEquals(new Integer(list.getLength(i, j, k)), new Integer(0));
                }
            }
        }

        assertEquals(list.removeData(0, 1, 2), new Integer(12));
        assertEquals(new Integer(list.getLength()), new Integer(2));
        for (int i = 0; i < 2; i++) {
            assertEquals(new Integer(list.getLength(i)), new Integer(3));
            for (int j = 0; j < 3; j++) {
                if (i == 0 && j == 1) {
                    assertEquals(new Integer(list.getLength(i, j)), new Integer(3));
                } else {
                    assertEquals(new Integer(list.getLength(i, j)), new Integer(4));
                }
                for (int k = 0; k < 4; k++) {
                    if (i == 0 && j == 1 && k == 3) {
                        continue;
                    }
                    assertEquals(new Integer(list.getLength(i, j, k)), new Integer(0));
                }
            }
        }

        DimensionalList<Integer> l;
        ArrayList<Integer> integers = new ArrayList<Integer>();

        l = new DimensionalList<Integer>(3);
        for (int i = 0; i < 51; i++) {
            integers.add(i);
        }
        l.setDatas(integers, 0, 0);

        for (int i = 0; i < 51; i++) {
            integers.set(i, i * 2);
        }
        l.setDatas(integers, 0, 1);

        for (int i = 0; i < 51; i++) {
            integers.set(i, i * 3);
        }
        l.setDatas(integers, 0, 2);

        for (int i = 0; i < 51; i++) {
            integers.set(i, i * 4);
        }
        l.setDatas(integers, 1, 0);

        for (int i = 0; i < 51; i++) {
            integers.set(i, i * 5);
        }
        l.setDatas(integers, 1, 1);

        for (int i = 0; i < 51; i++) {
            integers.set(i, i * 6);
        }
        l.setDatas(integers, 1, 2);

        for (int i  = 0; i < l.getLength(); i++) {
            for (int j  = 0; j < l.getLength(i); j++) {
                for (int k = 0; k < l.getLength(i, j); k++) {
                    assertEquals(l.getData(i, j, k), new Integer(k*(i*3+1+j)));
                }
            }
        }

        for (int i = 0; i < 51; i ++) {
            integers.set(i, i);
        }
        l.setDatas(integers, 0, 0, 3);

        for (int i = 0; i < 51; i ++) {
            integers.set(i, i * 2);
        }
        l.setDatas(integers, 0, 1, 3);

        for (int i = 0; i < 51; i ++) {
            integers.set(i, i * 3);
        }
        l.setDatas(integers, 0, 2, 3);

        for (int i = 0; i < 51; i ++) {
            integers.set(i, i * 4);
        }
        l.setDatas(integers, 1, 0, 3);

        for (int i = 0; i < 51; i ++) {
            integers.set(i, i * 5);
        }
        l.setDatas(integers, 1, 1, 3);

        for (int i = 0; i < 51; i ++) {
            integers.set(i, i * 6);
        }
        l.setDatas(integers, 1, 2, 3);

        for (int i  = 0; i < l.getLength(); i++) {
            for (int j  = 0; j < l.getLength(i); j++) {
                for (int k = 0; k < l.getLength(i, j); k++) {
                    if (k < 3) {
                        assertEquals(l.getData(i, j, k), new Integer(k*(i*3+1+j)));
                    } else {
                        assertEquals(l.getData(i, j, k), new Integer((k-3)*(i*3+1+j)));
                    }
                }
            }
        }
    }
}
