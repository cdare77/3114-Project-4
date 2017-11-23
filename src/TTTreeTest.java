import java.util.List;
import javax.management.InstanceNotFoundException;

import org.junit.Before;
import org.junit.Test;

import student.TestCase;

/**
 * Tests the functionality of our 2-3+ Tree to ensure that the
 * methods work as intended.
 * 
 * @author Chris Dare (cdare77@vt.edu)
 * @version 11/21/2017
 */
public class TTTreeTest extends TestCase {

    // ------------ PRIVATE VARIABLES ----------
    private TTTree<Integer, Integer> tree;

    
    
    // ------------ PUBLIC METHODS -------------
    @Before
    @Override
    public void setUp() {
        tree = new TTTree<Integer, Integer>();
    }

    @Test
    /**
     * Tests the insert method against the desired functionality
     */
    public void testInsert() {
        assertTrue(tree.isEmpty());
        tree.insert(10, 10);
        assertFalse(tree.isEmpty());
        tree.insert(20, 10);
        tree.insert(15, 10);
        tree.insert(16, 16);
        tree.insert(17, 1);
        tree.insert(5, 1);
        tree.insert(25, 1);
        tree.insert(16, 16);
        
        assertEquals(tree.toString(), "[15, 17] \n" + 
                "[5, 10] [16, 16] [20, 25] \n");
        assertEquals(tree.size(), 8);
    }

    @Test
    /**
     * Tests the remove method against the desired functionality
     */
    public void testRemove() {
        Exception ex = null;
        try {
            tree.remove(20);
        }
        catch (Exception e) {
            ex = e;
        }
        assertTrue(ex instanceof InstanceNotFoundException);
        
        
        tree.insert(10, 10);
        tree.insert(20, 10);
        tree.insert(15, 10);
        tree.insert(16, 16);
        tree.insert(17, 1);
        tree.insert(5, 1);
        tree.insert(25, 1);
        tree.insert(16, 16);
        assertEquals(tree.toString(), "[15, 17] \n" + 
                "[5, 10] [16, 16] [20, 25] \n");
        assertEquals(tree.size(), 8);

        try {
            tree.remove(16);
            assertEquals(tree.toString(), "[15, 17] \n" + 
                    "[5, 10] [16, null] [20, 25] \n");
            tree.remove(16);
            assertEquals(tree.toString(), "[10, 17] \n" + 
                    "[5, null] [15, null] [20, 25] \n");
            tree.remove(5);
            assertEquals(tree.toString(), "[15, 20] \n" + 
                    "[10, null] [17, null] [25, null] \n");
            assertEquals(tree.size(), 5);
        }
        catch (InstanceNotFoundException e) {
            e.printStackTrace();
        }
        
        tree.clear();
        assertTrue(tree.isEmpty());
        
        tree.insert(7, 7);
        tree.insert(9, 9);
        tree.insert(4, 4);
        assertEquals(tree.toString(), "[7, null] \n" + 
                "[4, null] [9, null] \n");
        try {
            tree.remove(7);
            assertEquals(tree.toString(), "[4, 9] \n");
        }
        catch (InstanceNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    /**
     * Tests the functionality of height after each insertion
     */
    public void testHeight() {
        assertEquals(tree.height(), 0);
        tree.insert(10, 10);
        assertEquals(tree.height(), 1);
        tree.insert(20, 10);
        assertEquals(tree.height(), 1);
        tree.insert(15, 10);
        assertEquals(tree.height(), 2);
        tree.insert(16, 16);
        assertEquals(tree.height(), 2);
        tree.insert(17, 1);
        assertEquals(tree.height(), 2);
        tree.insert(5, 1);
        assertEquals(tree.height(), 2);
        tree.insert(25, 1);
        assertEquals(tree.height(), 2);
        tree.insert(16, 16);
        assertEquals(tree.height(), 2);
        tree.insert(2, 2);
        assertEquals(tree.height(), 3);
        tree.insert(30, 30);
        assertEquals(tree.height(), 3);
        tree.insert(6, 6);
        assertEquals(tree.height(), 3);
        tree.insert(4, 4);
        assertEquals(tree.height(), 3);
        tree.insert(7, 7);
        assertEquals(tree.height(), 3);
        tree.insert(6, 6);
        assertEquals(tree.height(), 3);
        tree.insert(21, 21);
        assertEquals(tree.height(), 3);
        tree.insert(40, 40);
        assertEquals(tree.height(), 3);
        tree.insert(35, 35);
        assertEquals(tree.height(), 3);
        tree.insert(6, 6);
        assertEquals(tree.height(), 4);
    } // end testInsert

    @Test
    /**
     * Tests the functionality of Range search works and that it
     * holds inclusive relations
     */
    public void testRangeSearch() {
        tree.insert(10, 10);
        tree.insert(20, 10);
        tree.insert(15, 10);
        tree.insert(16, 16);
        tree.insert(17, 1);
        tree.insert(5, 1);
        tree.insert(25, 1);
        tree.insert(16, 16);
        tree.insert(2, 2);
        tree.insert(30, 30);
        tree.insert(6, 4);
        tree.insert(4, 4);
        tree.insert(7, 7);
        tree.insert(6, 6);
        tree.insert(21, 21);
        tree.insert(40, 40);
        tree.insert(35, 35);

        List<Integer> list = tree.rangeSearch(16, 40);
        assertEquals(list.toString(),
                "[16, 16, 1, 10, 21, 1, 30, 35, 40]");

        list = tree.rangeSearch(6, 6);
        assertEquals(list.toString(), "[4, 6]");
        
        list = tree.rangeSearch(100, 101);
        assertEquals(list.toString(), "[]");
        
        list = tree.rangeSearch(1, 2);
        assertEquals(list.toString(), "[2]");
    }

    @Test
    /**
     * Tests that toString returns a representation
     * of the tree in depth-first order
     */
    public void testToString() {
        tree.insert(10, 10);
        tree.insert(20, 10);
        tree.insert(15, 10);
        tree.insert(16, 16);
        tree.insert(17, 1);
        tree.insert(5, 1);
        tree.insert(25, 1);
        tree.insert(16, 16);
        tree.insert(2, 2);
        tree.insert(30, 30);
        tree.insert(6, 4);
        tree.insert(4, 4);
        tree.insert(7, 7);
        tree.insert(6, 6);
        tree.insert(21, 21);
        tree.insert(40, 40);
        tree.insert(35, 35);

        assertEquals(tree.toString(), "[15, 25] \n" +
                "[5, 7] [17, null] [35, null] \n" +
                "[2, 4] [6, 6] [10, null] [16, 16] "
                + "[20, 21] [30, null] [40, null] \n");
    }
    
    @Test
    public void testClear() {
        tree.insert(10, 10);
        tree.insert(20, 10);
        tree.insert(15, 10);
        tree.insert(16, 16);
        tree.insert(17, 1);
        tree.insert(5, 1);
        tree.insert(25, 1);
        tree.insert(16, 16);
        tree.insert(2, 2);
        tree.insert(30, 30);
        tree.insert(6, 4);
        tree.insert(4, 4);
        tree.insert(7, 7);
        tree.insert(6, 6);
        tree.insert(21, 21);
        tree.insert(40, 40);
        tree.insert(35, 35);
        
        assertFalse(tree.isEmpty());
        tree.clear();
        assertTrue(tree.isEmpty());
        assertEquals(tree.size(), 0);
    }
} // TTTreeTest
