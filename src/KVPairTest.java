import student.TestCase;

/**
 * Tests the functionality of KVPair on Integer 
 * types in order to simplify compareTo results
 * 
 * @author Chris Dare (cdare77@vt.edu)
 * @author Kenneth Worden (kworden@vt.edu)
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
        assertEquals((int) 10, (int) first.getKey());
        assertEquals((int) 5, (int) first.getValue());
        assertEquals((int) 8, (int) second.getKey());
        assertEquals((int) (-2), (int) second.getValue());
        assertEquals((int) 5, (int) third.getKey());
        assertEquals((int) 10, (int) third.getValue());
        assertEquals((int) 0, (int) fourth.getKey());
        assertEquals((int) 5, (int) fourth.getValue());
        assertEquals((int) 0, (int) fifth.getKey());
        assertEquals((int) 3, (int) fifth.getValue());       
    }
    
    /**
     * Tests the functionality of the modifier methods
     * for both key and value
     */
    public void testModifier() {
        first.setKey(-1);
        first.setValue(-1);
        assertEquals((int) -1, (int) first.getKey());
        assertEquals((int) -1, (int) first.getValue());
        
        first.setKey(3);
        first.setValue(5);
        assertEquals((int) 3, (int) first.getKey());
        assertEquals((int) 5, (int) first.getValue());
        assertNotSame((int) first.getKey(), (int) -1);
        assertNotSame((int) first.getValue(), (int) -1);
        
        second.setKey(-1);
        second.setValue(-1);
        assertEquals((int) -1, (int) second.getKey());
        assertEquals((int) -1, (int) second.getValue());
        
        third.setKey(-1);
        third.setValue(-1);
        assertEquals((int) -1, (int) third.getKey());
        assertEquals((int) -1, (int) third.getValue());
        
        fourth.setKey(-1);
        fourth.setValue(-1);
        assertEquals((int) -1, (int) fourth.getKey());
        assertEquals((int) -1, (int) fourth.getValue());
        
        fifth.setKey(-1);
        fifth.setValue(-1);
        assertEquals((int) -1, (int) fifth.getKey());
        assertEquals((int) -1, (int) fifth.getValue());   
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
        assertEquals("(10,5)", first.toString());
        assertEquals("(8,-2)", second.toString());
    }
} // end KVPairTest
