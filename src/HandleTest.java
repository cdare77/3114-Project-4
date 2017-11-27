import student.TestCase;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests the functionality of Handle on a dummy byte array by
 * inserting strings in a similar fashion to that of SongSearch.
 * All functions are tested against the desired outcome
 * 
 * @author Chris Dare (cdare77@vt.edu)
 * @version 11/26/2017
 */
public class HandleTest extends TestCase {

    // ------------------- PRIVATE VARIABLES --------------------
    private byte[] memory;
    private Handle nickelback;
    private Handle photograph;
    private Handle rockstar;
    private Handle savinMe;
    private Handle howYouRemindMe;
    private Handle farAway;
    private Handle animals;

    // ------------------- PUBLIC METHODS -----------------------

    @Override
    public void setUp() {
        byte[] temp; // temporary pointer to the bytes of our
                     // strings
        int offset = 0;
        memory = new byte[96];

        // inserts 1 artist and 6 songs into our byte array
        // for memory using similar techniques to that
        // of SongSearch
        temp = "Nickelback".getBytes();
        System.arraycopy(temp, 0, memory, offset, temp.length);
        nickelback = new Handle(memory, offset, temp.length);
        offset += temp.length;

        temp = "Photograph".getBytes();
        System.arraycopy(temp, 0, memory, offset, temp.length);
        photograph = new Handle(memory, offset, temp.length);
        offset += temp.length;

        temp = "Rockstar".getBytes();
        System.arraycopy(temp, 0, memory, offset, temp.length);
        rockstar = new Handle(memory, offset, temp.length);
        offset += temp.length;

        temp = "Savin' Me".getBytes();
        System.arraycopy(temp, 0, memory, offset, temp.length);
        savinMe = new Handle(memory, offset, temp.length);
        offset += temp.length;

        temp = "How You Remind Me".getBytes();
        System.arraycopy(temp, 0, memory, offset, temp.length);
        howYouRemindMe = new Handle(memory, offset, temp.length);
        offset += temp.length;

        temp = "Far Away".getBytes();
        System.arraycopy(temp, 0, memory, offset, temp.length);
        farAway = new Handle(memory, offset, temp.length);
        offset += temp.length;

        temp = "Animals".getBytes();
        System.arraycopy(temp, 0, memory, offset, temp.length);
        animals = new Handle(memory, offset, temp.length);
        offset += temp.length;
    } // end setUp

    /**
     * Tests the functionality of the accessor methods for 
     * offset, length, and memory
     */
    public void testAccessor() {
        assertEquals(nickelback.getOffset(), 0);
        assertEquals(nickelback.getLength(), 10);
        assertEquals(nickelback.getMemory(), memory);
        
        assertEquals(photograph.getOffset(), 10);
        assertEquals(photograph.getLength(), 10);
        
        assertEquals(rockstar.getOffset(), 20);
        assertEquals(rockstar.getLength(), 8);
        
        assertEquals(savinMe.getOffset(), 28);
        assertEquals(savinMe.getLength(), 9);
        
        assertEquals(howYouRemindMe.getOffset(), 37);
        assertEquals(howYouRemindMe.getLength(), 17);
        
        assertEquals(farAway.getOffset(), 54);
        assertEquals(farAway.getLength(), 8);
        
        assertEquals(animals.getOffset(), 62);
        assertEquals(animals.getLength(), 7);
    }
    
    /**
     * Tests the functionality of the modifier methods for
     * offset, length, and memory. Ensures private variables
     * are modified through accessor methods
     */
    public void testModifier() {
        byte[] emptyArr = new byte[96];
        
        nickelback.setLength(-1);
        nickelback.setOffset(-1);
        nickelback.setMemory(emptyArr);
        assertEquals(nickelback.getOffset(), -1);
        assertEquals(nickelback.getLength(), -1);
        assertEquals(nickelback.getMemory(), emptyArr);
        
        photograph.setLength(-1);
        photograph.setOffset(-1);
        photograph.setMemory(emptyArr);
        assertEquals(photograph.getOffset(), -1);
        assertEquals(photograph.getLength(), -1);
        assertEquals(photograph.getMemory(), emptyArr);

        rockstar.setLength(-1);
        rockstar.setOffset(-1);
        rockstar.setMemory(emptyArr);
        assertEquals(rockstar.getOffset(), -1);
        assertEquals(rockstar.getLength(), -1);
        assertEquals(rockstar.getMemory(), emptyArr);
        
        savinMe.setLength(-1);
        savinMe.setOffset(-1);
        savinMe.setMemory(emptyArr);
        assertEquals(savinMe.getOffset(), -1);
        assertEquals(savinMe.getLength(), -1);
        assertEquals(savinMe.getMemory(), emptyArr);
        
        howYouRemindMe.setOffset(-1);
        howYouRemindMe.setLength(-1);
        howYouRemindMe.setMemory(emptyArr);
        assertEquals(howYouRemindMe.getOffset(), -1);
        assertEquals(howYouRemindMe.getLength(), -1);
        assertEquals(howYouRemindMe.getMemory(), emptyArr);
    }
    

    /**
     * Tests the functionality of the accessor method for the
     * data pointed to by each handle
     */
    public void testGetStringAt() {
        assertEquals(nickelback.getStringAt(), "Nickelback");
        assertEquals(photograph.getStringAt(), "Photograph");
        assertEquals(rockstar.getStringAt(), "Rockstar");
        assertEquals(savinMe.getStringAt(), "Savin' Me");
        assertEquals(howYouRemindMe.getStringAt(),
                "How You Remind Me");
        assertEquals(farAway.getStringAt(), "Far Away");
        assertEquals(animals.getStringAt(), "Animals");
    }

    /**
     * Tests the functionality of the compareTo method based off
     * the strings stored by the handles
     */
    public void testCompareTo() {
        assertTrue(nickelback.compareTo(photograph) < 0);
        assertTrue(photograph.compareTo(rockstar) < 0);
        assertTrue(rockstar.compareTo(savinMe) < 0);
        assertTrue(savinMe.compareTo(howYouRemindMe) > 0);
        assertTrue(howYouRemindMe.compareTo(farAway) > 0);
        assertTrue(farAway.compareTo(animals) > 0);
    }

    /**
     * tests the functionality of Equals in the 4 cases that the
     * other object is null, the other object is not of the same
     * class, the other object is equal to the one being
     * compared, and the other object is a handle but not equal
     */
    public void testEquals() {
        Handle nullHandle = null;
        assertNotEquals(animals, nullHandle);

        Integer notHandle = 1;
        assertNotEquals(animals, notHandle);

        Handle animalsCopy = new Handle(memory, 62, 7);
        assertEquals(animals, animalsCopy);

        assertNotEquals(animals, howYouRemindMe);
    }

    /**
     * Tests the functionality of toString based on known offsets
     * and lengths
     */
    public void testToString() {
        assertEquals(nickelback.toString(),
                "(offset: 0, len: 10)");
        assertEquals(photograph.toString(),
                "(offset: 10, len: 10)");
        assertEquals(rockstar.toString(),
                "(offset: 20, len: 8)");
        assertEquals(savinMe.toString(), "(offset: 28, len: 9)");
        assertEquals(howYouRemindMe.toString(),
                "(offset: 37, len: 17)");
        assertEquals(farAway.toString(), "(offset: 54, len: 8)");
        assertEquals(animals.toString(), "(offset: 62, len: 7)");
    } // end testToString
} // end HandleTest
