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
    private TTTree<KVPair<Integer, Integer>> tree;

    // ------------ PUBLIC METHODS -------------
    @Before
    @Override
    public void setUp() {
        tree = new TTTree<KVPair<Integer, Integer>>();
    }

    @Test
    /**
     * Tests the insert method against the desired functionality
     */
    public void testInsert() {
        assertTrue(tree.isEmpty());
        tree.insert(new KVPair<Integer, Integer>(10, 10));
        assertFalse(tree.isEmpty());
        tree.insert(new KVPair<Integer, Integer>(20, 10));
        tree.insert(new KVPair<Integer, Integer>(15, 10));
        tree.insert(new KVPair<Integer, Integer>(16, 16));
        tree.insert(new KVPair<Integer, Integer>(17, 1));
        tree.insert(new KVPair<Integer, Integer>(5, 1));
        tree.insert(new KVPair<Integer, Integer>(25, 1));
        tree.insert(new KVPair<Integer, Integer>(16, 16));
        System.out.print(tree);
        assertEquals(tree.toString(), "[(15, 10), (17, 1)] \n" +
                "[(5, 1), (10, 10)] [(16, 16), (16, 16)] [(20, 10),"
                + " (25, 1)] \n");
        assertEquals(tree.size(), 8);
    }

    @Test
    /**
     * Tests the remove method against the desired functionality
     */
    public void testRemove() {
        Exception ex = null;
        try {
            tree.remove(new KVPair<Integer, Integer>(20, 20));
        }
        catch (Exception e) {
            ex = e;
        }
        assertTrue(ex instanceof InstanceNotFoundException);

        tree.insert(new KVPair<Integer, Integer>(10, 10));
        tree.insert(new KVPair<Integer, Integer>(20, 10));
        tree.insert(new KVPair<Integer, Integer>(15, 10));
        tree.insert(new KVPair<Integer, Integer>(16, 16));
        tree.insert(new KVPair<Integer, Integer>(17, 1));
        tree.insert(new KVPair<Integer, Integer>(5, 1));
        tree.insert(new KVPair<Integer, Integer>(25, 1));
        tree.insert(new KVPair<Integer, Integer>(16, 16));

        assertEquals(tree.toString(), "[(15, 10), (17, 1)] \n" +
                "[(5, 1), (10, 10)] [(16, 16), (16, 16)] [(20, 10),"
                + " (25, 1)] \n");
        assertEquals(tree.size(), 8);

        try {
            tree.remove(new KVPair<Integer, Integer>(16, 16));
            assertEquals(tree.toString(),
                    "[(15, 10), (17, 1)] \n" +
                            "[(5, 1), (10, 10)] [(16, 16), null] "
                            + "[(20, 10), (25, 1)] \n");
            tree.remove(new KVPair<Integer, Integer>(16, 16));
            assertEquals(tree.toString(),
                    "[(10, 10), (17, 1)] \n" +
                            "[(5, 1), null] [(15, 10), null] "
                            + "[(20, 10), (25, 1)] \n");
            tree.remove(new KVPair<Integer, Integer>(5, 1));
            assertEquals(tree.toString(),
                    "[(15, 10), (20, 10)] \n" +
                            "[(10, 10), null] [(17, 1), null]"
                            + " [(25, 1), null] \n");
            assertEquals(tree.size(), 5);
        }
        catch (InstanceNotFoundException e) {
            e.printStackTrace();
        }

        ex = null;
        try {
            tree.remove(new KVPair<Integer, Integer>(100, 100));
        }
        catch (InstanceNotFoundException e) {
            ex = e;
        }
        assertTrue(ex instanceof InstanceNotFoundException);

        tree.clear();
        assertTrue(tree.isEmpty());

        tree.insert(new KVPair<Integer, Integer>(7, 7));
        tree.insert(new KVPair<Integer, Integer>(9, 9));
        tree.insert(new KVPair<Integer, Integer>(4, 4));
        assertEquals(tree.toString(), "[(7, 7), null] \n" +
                "[(4, 4), null] [(9, 9), null] \n");
        try {
            tree.remove(new KVPair<Integer, Integer>(7, 7));
            assertEquals(tree.toString(), "[(4, 4), (9, 9)] \n");
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
        tree.insert(new KVPair<Integer, Integer>(10, 10));
        assertEquals(tree.height(), 1);
        tree.insert(new KVPair<Integer, Integer>(20, 10));
        assertEquals(tree.height(), 1);
        tree.insert(new KVPair<Integer, Integer>(15, 10));
        assertEquals(tree.height(), 2);
        tree.insert(new KVPair<Integer, Integer>(16, 16));
        assertEquals(tree.height(), 2);
        tree.insert(new KVPair<Integer, Integer>(17, 1));
        assertEquals(tree.height(), 2);
        tree.insert(new KVPair<Integer, Integer>(5, 1));
        assertEquals(tree.height(), 2);
        tree.insert(new KVPair<Integer, Integer>(25, 1));
        assertEquals(tree.height(), 2);
        tree.insert(new KVPair<Integer, Integer>(16, 16));
        assertEquals(tree.height(), 2);
        tree.insert(new KVPair<Integer, Integer>(2, 2));
        assertEquals(tree.height(), 3);
        tree.insert(new KVPair<Integer, Integer>(30, 30));
        assertEquals(tree.height(), 3);
        tree.insert(new KVPair<Integer, Integer>(6, 6));
        assertEquals(tree.height(), 3);
        tree.insert(new KVPair<Integer, Integer>(4, 4));
        assertEquals(tree.height(), 3);
        tree.insert(new KVPair<Integer, Integer>(7, 7));
        assertEquals(tree.height(), 3);
        tree.insert(new KVPair<Integer, Integer>(6, 6));
        assertEquals(tree.height(), 3);
        tree.insert(new KVPair<Integer, Integer>(21, 21));
        assertEquals(tree.height(), 3);
        tree.insert(new KVPair<Integer, Integer>(40, 40));
        assertEquals(tree.height(), 3);
        tree.insert(new KVPair<Integer, Integer>(35, 35));
        assertEquals(tree.height(), 3);
        tree.insert(new KVPair<Integer, Integer>(6, 6));
        assertEquals(tree.height(), 4);
    } // end testInsert

    @Test
    /**
     * Tests the functionality of Range search works and that it
     * holds inclusive relations
     */
    public void testRangeSearch() {
        tree.insert(new KVPair<Integer, Integer>(10, 10));
        tree.insert(new KVPair<Integer, Integer>(20, 10));
        tree.insert(new KVPair<Integer, Integer>(15, 10));
        tree.insert(new KVPair<Integer, Integer>(16, 16));
        tree.insert(new KVPair<Integer, Integer>(17, 1));
        tree.insert(new KVPair<Integer, Integer>(5, 1));
        tree.insert(new KVPair<Integer, Integer>(25, 1));
        tree.insert(new KVPair<Integer, Integer>(16, 16));
        tree.insert(new KVPair<Integer, Integer>(2, 2));
        tree.insert(new KVPair<Integer, Integer>(30, 30));
        tree.insert(new KVPair<Integer, Integer>(6, 4));
        tree.insert(new KVPair<Integer, Integer>(4, 4));
        tree.insert(new KVPair<Integer, Integer>(7, 7));
        tree.insert(new KVPair<Integer, Integer>(6, 6));
        tree.insert(new KVPair<Integer, Integer>(21, 21));
        tree.insert(new KVPair<Integer, Integer>(40, 40));
        tree.insert(new KVPair<Integer, Integer>(35, 35));

        List<KVPair<Integer, Integer>> list = tree.rangeSearch(
                new KVPair<Integer, Integer>(16, 0),
                new KVPair<Integer, Integer>(40, 0));
        assertEquals(list.toString(),
                "[(16, 16), (16, 16), (17, 1), (20, 10),"
                        + " (21, 21), (25, 1), (30, 30), (35, 35)]");

        list = tree.rangeSearch(
                new KVPair<Integer, Integer>(6, 0),
                new KVPair<Integer, Integer>(6, 100));
        assertEquals(list.toString(), "[(6, 4), (6, 6)]");

        list = tree.rangeSearch(
                new KVPair<Integer, Integer>(100, 0),
                new KVPair<Integer, Integer>(101, 0));
        assertEquals(list.toString(), "[]");

        list = tree.rangeSearch(
                new KVPair<Integer, Integer>(1, 0),
                new KVPair<Integer, Integer>(2, 100));
        assertEquals(list.toString(), "[(2, 2)]");
    }

    @Test
    /**
     * Tests that toString returns a representation of the tree
     * in depth-first order
     */
    public void testToString() {
        tree.insert(new KVPair<Integer, Integer>(10, 10));
        tree.insert(new KVPair<Integer, Integer>(20, 10));
        tree.insert(new KVPair<Integer, Integer>(15, 10));
        tree.insert(new KVPair<Integer, Integer>(16, 16));
        tree.insert(new KVPair<Integer, Integer>(17, 1));
        tree.insert(new KVPair<Integer, Integer>(5, 1));
        tree.insert(new KVPair<Integer, Integer>(25, 1));
        tree.insert(new KVPair<Integer, Integer>(16, 16));
        tree.insert(new KVPair<Integer, Integer>(2, 2));
        tree.insert(new KVPair<Integer, Integer>(30, 30));
        tree.insert(new KVPair<Integer, Integer>(6, 4));
        tree.insert(new KVPair<Integer, Integer>(4, 4));
        tree.insert(new KVPair<Integer, Integer>(7, 7));
        tree.insert(new KVPair<Integer, Integer>(6, 6));
        tree.insert(new KVPair<Integer, Integer>(21, 21));
        tree.insert(new KVPair<Integer, Integer>(40, 40));
        tree.insert(new KVPair<Integer, Integer>(35, 35));

        assertEquals(tree.toString(), "[(15, 10), (25, 1)] \n" +
                "[(5, 1), (7, 7)] [(17, 1), null] [(35, 35), null] \n"
                + "[(2, 2), (4, 4)] [(6, 4), (6, 6)] [(10, 10), null] "
                + "[(16, 16), (16, 16)] [(20, 10), (21, 21)] [(30, 30),"
                + " null] [(40, 40), null] \n");
    }

    @Test
    public void testClear() {
        tree.insert(new KVPair<Integer, Integer>(10, 10));
        tree.insert(new KVPair<Integer, Integer>(20, 10));
        tree.insert(new KVPair<Integer, Integer>(15, 10));
        tree.insert(new KVPair<Integer, Integer>(16, 16));
        tree.insert(new KVPair<Integer, Integer>(17, 1));
        tree.insert(new KVPair<Integer, Integer>(5, 1));
        tree.insert(new KVPair<Integer, Integer>(25, 1));
        tree.insert(new KVPair<Integer, Integer>(16, 16));
        tree.insert(new KVPair<Integer, Integer>(2, 2));
        tree.insert(new KVPair<Integer, Integer>(30, 30));
        tree.insert(new KVPair<Integer, Integer>(6, 4));
        tree.insert(new KVPair<Integer, Integer>(4, 4));
        tree.insert(new KVPair<Integer, Integer>(7, 7));
        tree.insert(new KVPair<Integer, Integer>(6, 6));
        tree.insert(new KVPair<Integer, Integer>(21, 21));
        tree.insert(new KVPair<Integer, Integer>(40, 40));
        tree.insert(new KVPair<Integer, Integer>(35, 35));

        assertFalse(tree.isEmpty());
        tree.clear();
        assertTrue(tree.isEmpty());
        assertEquals(tree.size(), 0);
    }
} // TTTreeTest
