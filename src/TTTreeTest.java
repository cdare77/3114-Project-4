import java.util.List;
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
        assertEquals(tree.toString(), "15 10 17 1\n" +
                "  5 1 10 10\n" +
                "  16 16 16 16\n" +
                "  20 10 25 1\n");
        assertEquals(8, tree.size());
    }

    @Test
    /**
     * Tests the remove method against the desired functionality
     */
    public void testRemove() {

        // remove from an empty tree
        assertNull(tree
                .remove(new KVPair<Integer, Integer>(20, 20)));

        tree.insert(new KVPair<Integer, Integer>(10, 10));
        tree.insert(new KVPair<Integer, Integer>(20, 10));
        tree.insert(new KVPair<Integer, Integer>(15, 10));
        tree.insert(new KVPair<Integer, Integer>(16, 16));
        tree.insert(new KVPair<Integer, Integer>(17, 1));
        tree.insert(new KVPair<Integer, Integer>(5, 1));
        tree.insert(new KVPair<Integer, Integer>(25, 1));
        tree.insert(new KVPair<Integer, Integer>(16, 16));

        assertEquals("15 10 17 1\n" +
                "  5 1 10 10\n" +
                "  16 16 16 16\n" +
                "  20 10 25 1\n", tree.toString());
        assertEquals(8, tree.size());

        tree.remove(new KVPair<Integer, Integer>(16, 16));
        assertEquals(
                "15 10 17 1\n" +
                        "  5 1 10 10\n" +
                        "  16 16\n" +
                        "  20 10 25 1\n",
                tree.toString());
        tree.remove(new KVPair<Integer, Integer>(16, 16));
        assertEquals(
                "10 10 17 1\n" +
                        "  5 1\n" +
                        "  15 10\n" +
                        "  20 10 25 1\n",
                tree.toString());
        tree.remove(new KVPair<Integer, Integer>(5, 1));
        assertEquals(
                "15 10 20 10\n" +
                        "  10 10\n" +
                        "  17 1\n" +
                        "  25 1\n",
                tree.toString());
        assertEquals(5, tree.size());

        // remove an element not in the tree
        assertNull(tree
                .remove(new KVPair<Integer, Integer>(100, 100)));

        // start over with new tree
        tree.clear();
        assertTrue(tree.isEmpty());

        tree.insert(new KVPair<Integer, Integer>(7, 7));
        tree.insert(new KVPair<Integer, Integer>(9, 9));
        tree.insert(new KVPair<Integer, Integer>(4, 4));
        assertEquals("7 7\n" +
                "  4 4\n" +
                "  9 9\n", tree.toString());

        tree.remove(new KVPair<Integer, Integer>(7, 7));

        assertEquals("4 4 9 9\n", tree.toString());

    }

    @Test
    /**
     * Tests the functionality of height after each insertion
     */
    public void testHeight() {
        assertEquals(0, tree.height());
        tree.insert(new KVPair<Integer, Integer>(10, 10));
        assertEquals(1, tree.height());
        tree.insert(new KVPair<Integer, Integer>(20, 10));
        assertEquals(1, tree.height());
        tree.insert(new KVPair<Integer, Integer>(15, 10));
        assertEquals(2, tree.height());
        tree.insert(new KVPair<Integer, Integer>(16, 16));
        assertEquals(2, tree.height());
        tree.insert(new KVPair<Integer, Integer>(17, 1));
        assertEquals(2, tree.height());
        tree.insert(new KVPair<Integer, Integer>(5, 1));
        assertEquals(2, tree.height());
        tree.insert(new KVPair<Integer, Integer>(25, 1));
        assertEquals(2, tree.height());
        tree.insert(new KVPair<Integer, Integer>(16, 16));
        assertEquals(2, tree.height());
        tree.insert(new KVPair<Integer, Integer>(2, 2));
        assertEquals(3, tree.height());
        tree.insert(new KVPair<Integer, Integer>(30, 30));
        assertEquals(3, tree.height());
        tree.insert(new KVPair<Integer, Integer>(6, 6));
        assertEquals(3, tree.height());
        tree.insert(new KVPair<Integer, Integer>(4, 4));
        assertEquals(3, tree.height());
        tree.insert(new KVPair<Integer, Integer>(7, 7));
        assertEquals(3, tree.height());
        tree.insert(new KVPair<Integer, Integer>(6, 6));
        assertEquals(3, tree.height());
        tree.insert(new KVPair<Integer, Integer>(21, 21));
        assertEquals(3, tree.height());
        tree.insert(new KVPair<Integer, Integer>(40, 40));
        assertEquals(3, tree.height());
        tree.insert(new KVPair<Integer, Integer>(35, 35));
        assertEquals(3, tree.height());
        tree.insert(new KVPair<Integer, Integer>(6, 6));
        assertEquals(4, tree.height());
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
        assertEquals("[16 16, 16 16, 17 1, 20 10, 21 21, 25 1"
                + ", 30 30, 35 35]",
                list.toString());

        list = tree.rangeSearch(
                new KVPair<Integer, Integer>(6, 0),
                new KVPair<Integer, Integer>(6, 100));
        assertEquals("[6 4, 6 6]", list.toString());

        list = tree.rangeSearch(
                new KVPair<Integer, Integer>(100, 0),
                new KVPair<Integer, Integer>(101, 0));
        assertEquals("[]", list.toString());

        list = tree.rangeSearch(
                new KVPair<Integer, Integer>(1, 0),
                new KVPair<Integer, Integer>(2, 100));
        assertEquals("[2 2]", list.toString());
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

        assertEquals("15 10 25 1\n" +
                "  5 1 7 7\n" +
                "    2 2 4 4\n" +
                "    6 4 6 6\n" +
                "    10 10\n" +
                "  17 1\n" +
                "    16 16 16 16\n" +
                "    20 10 21 21\n" +
                "  35 35\n" +
                "    30 30\n" +
                "    40 40\n", tree.toString());
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
        assertEquals(0, tree.size());
    }

    @Test
    public void testSearch() {
        KVPair<Integer, Integer> tens =
                new KVPair<Integer, Integer>(10, 10);
        KVPair<Integer, Integer> sixteens =
                new KVPair<Integer, Integer>(16, 16);
        KVPair<Integer, Integer> fourties =
                new KVPair<Integer, Integer>(40, 40);
        KVPair<Integer, Integer> thirtyFives =
                new KVPair<Integer, Integer>(35, 35);
        KVPair<Integer, Integer> thirtys =
                new KVPair<Integer, Integer>(30, 30);
        KVPair<Integer, Integer> fours =
                new KVPair<Integer, Integer>(4, 4);
        KVPair<Integer, Integer> sevens =
                new KVPair<Integer, Integer>(7, 7);
        KVPair<Integer, Integer> twentyOnes =
                new KVPair<Integer, Integer>(21, 21);

        assertNull(tree.search(twentyOnes));
        
        tree.insert(tens);
        tree.insert(new KVPair<Integer, Integer>(20, 10));
        tree.insert(new KVPair<Integer, Integer>(15, 10));
        tree.insert(sixteens);
        tree.insert(new KVPair<Integer, Integer>(17, 1));
        tree.insert(new KVPair<Integer, Integer>(5, 1));
        tree.insert(new KVPair<Integer, Integer>(25, 1));
        tree.insert(sixteens);
        tree.insert(new KVPair<Integer, Integer>(2, 2));
        tree.insert(thirtys);
        tree.insert(new KVPair<Integer, Integer>(6, 4));
        tree.insert(fours);
        tree.insert(sevens);
        tree.insert(new KVPair<Integer, Integer>(6, 6));
        tree.insert(twentyOnes);
        tree.insert(fourties);
        tree.insert(thirtyFives);

        assertEquals(tens, tree
                .search(new KVPair<Integer, Integer>(10, 10)));
        assertEquals(sixteens, tree.search(sixteens =
                new KVPair<Integer, Integer>(16, 16)));
        assertEquals(fourties, tree
                .search(new KVPair<Integer, Integer>(40, 40)));
        assertEquals(thirtyFives, tree
                .search(new KVPair<Integer, Integer>(35, 35)));
        assertEquals(thirtys, tree
                .search(new KVPair<Integer, Integer>(30, 30)));
    }
} // TTTreeTest
