import student.TestCase;

/**
 * Tests the functionality of KVPair on Integer 
 * types in order to simplify compareTo results
 * 
 * @author Chris Dare (cdare77@vt.edu)
 * @version 11/26/2017
 */
public class KVPairTest extends TestCase {

    // ------------- PRIVATE VARIABLES --------------------
    private KVPair<Integer, Integer> first;
    private KVPair<Integer, Integer> second;
    private KVPair<Integer, Integer> third;
    private KVPair<Integer, Integer> fourth;
    private KVPair<Integer, Integer> fifth;
    
    // -------------- PUBLIC METHODS ----------------------
    @Override
    public void setUp() {
        first = new KVPair<Integer, Integer>(10, 5);
        second = new KVPair<Integer, Integer>(8, -2);
        third = new KVPair<Integer, Integer>(5, 10);
        fourth = new KVPair<Integer, Integer>(0, 5);
        fifth = new KVPair<Integer, Integer>(0, 3);
    }
    
    /**
     * Tests the functionality of the accessor methods
     * for both key and value
     */
    public void testAccessor() {
        assertEquals((int) first.getKey(), (int) 10);
        assertEquals((int) first.getValue(), (int) 5);
        assertEquals((int) second.getKey(), (int) 8);
        assertEquals((int) second.getValue(), (int) (-2));
        assertEquals((int) third.getKey(), (int) 5);
        assertEquals((int) third.getValue(), (int) 10);
        assertEquals((int) fourth.getKey(), (int) 0);
        assertEquals((int) fourth.getValue(), (int) 5);
        assertEquals((int) fifth.getKey(), (int) 0);
        assertEquals((int) fifth.getValue(), (int) 3);       
    }
    
    /**
     * Tests the functionality of the modifier methods
     * for both key and value
     */
    public void testModifier() {
        first.setKey(-1);
        first.setValue(-1);
        assertEquals((int) first.getKey(), (int) -1);
        assertEquals((int) first.getValue(), (int) -1);
        second.setKey(-1);
        second.setValue(-1);
        assertEquals((int) second.getKey(), (int) -1);
        assertEquals((int) second.getValue(), (int) -1);
        third.setKey(-1);
        third.setValue(-1);
        assertEquals((int) third.getKey(), (int) -1);
        assertEquals((int) third.getValue(), (int) -1);
        fourth.setKey(-1);
        fourth.setValue(-1);
        assertEquals((int) fourth.getKey(), (int) -1);
        assertEquals((int) fourth.getValue(), (int) -1);
        fifth.setKey(-1);
        fifth.setValue(-1);
        assertEquals((int) fifth.getKey(), (int) -1);
        assertEquals((int) fifth.getValue(), (int) -1);   
    }
    
    /**
     * Tests the functionality of the compare to method
     */
    public void testCompareTo() {
        // compare primary keys
        assertTrue(first.compareTo(second) > 0);
        assertTrue(first.compareTo(third) > 0);
        assertFalse(third.compareTo(second) > 0);
        // both elements have same key, so the compareTo
        // should compare values
        assertTrue(fourth.compareTo(fifth) > 0);
        assertTrue(fifth.compareTo(fourth) < 0);
    }
    
    /**
     * Tests the toString method against expected outcome
     */
    public void testToString() {
        assertEquals(first.toString(), "(10, 5)");
        assertEquals(second.toString(), "(8, -2)");
    }
} // end KVPairTest
