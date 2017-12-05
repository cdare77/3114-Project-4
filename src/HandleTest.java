import student.TestCase;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests the functionality of Handle on a dummy byte array by
 * inserting strings in a similar fashion to that of SongSearch.
 * All functions are tested against the desired outcome
 * 
 * @author Chris Dare (cdare77@vt.edu)
 * @author Kenneth Worden (kworden@vt.edu)
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
        assertEquals(0, nickelback.getOffset());
        assertEquals(10, nickelback.getLength());
        assertEquals(memory, nickelback.getMemory());

        assertEquals(10, photograph.getOffset());
        assertEquals(10, photograph.getLength());

        assertEquals(20, rockstar.getOffset());
        assertEquals(8, rockstar.getLength());

        assertEquals(28, savinMe.getOffset());
        assertEquals(9, savinMe.getLength());

        assertEquals(37, howYouRemindMe.getOffset());
        assertEquals(17, howYouRemindMe.getLength());

        assertEquals(54, farAway.getOffset());
        assertEquals(8, farAway.getLength());

        assertEquals(62, animals.getOffset());
        assertEquals(7, animals.getLength());
    }

    /**
     * Tests the functionality of the modifier methods for
     * offset, length, and memory. Ensures private variables are
     * modified through accessor methods
     */
    public void testModifier() {
        byte[] emptyArr = new byte[96];

        nickelback.setLength(-1);
        nickelback.setOffset(-1);
        nickelback.setMemory(emptyArr);
        assertEquals(-1, nickelback.getOffset());
        assertEquals(-1, nickelback.getLength());
        assertEquals(emptyArr, nickelback.getMemory());

        photograph.setLength(-1);
        photograph.setOffset(-1);
        photograph.setMemory(emptyArr);
        assertEquals(-1, photograph.getOffset());
        assertEquals(-1, photograph.getLength());
        assertEquals(emptyArr, photograph.getMemory());

        rockstar.setLength(-1);
        rockstar.setOffset(-1);
        rockstar.setMemory(emptyArr);
        assertEquals(-1, rockstar.getOffset());
        assertEquals(-1, rockstar.getLength());
        assertEquals(emptyArr, rockstar.getMemory());

        savinMe.setLength(-1);
        savinMe.setOffset(-1);
        savinMe.setMemory(emptyArr);
        assertEquals(-1, savinMe.getOffset());
        assertEquals(-1, savinMe.getLength());
        assertEquals(emptyArr, savinMe.getMemory());

        howYouRemindMe.setOffset(-1);
        howYouRemindMe.setLength(-1);
        howYouRemindMe.setMemory(emptyArr);
        assertEquals(-1, howYouRemindMe.getOffset());
        assertEquals(-1, howYouRemindMe.getLength());
        assertEquals(emptyArr, howYouRemindMe.getMemory());
    }

    /**
     * Tests the functionality of the accessor method for the
     * data pointed to by each handle
     */
    public void testGetStringAt() {
        assertEquals("Nickelback", nickelback.getStringAt());
        assertEquals("Photograph", photograph.getStringAt());
        assertEquals("Rockstar", rockstar.getStringAt());
        assertEquals("Savin' Me", savinMe.getStringAt());
        assertEquals("How You Remind Me", howYouRemindMe.getStringAt());
        assertEquals("Far Away", farAway.getStringAt());
        assertEquals("Animals", animals.getStringAt());
    }

    /**
     * Tests the functionality of the compareTo method based off
     * the strings stored by the handles
     */
    public void testCompareTo() {
        assertTrue(nickelback.compareTo(photograph) < 0);
        assertTrue(photograph.compareTo(rockstar) < 0);
        assertTrue(rockstar.compareTo(savinMe) < 0);
        assertTrue(savinMe.compareTo(howYouRemindMe) < 0);
        assertTrue(howYouRemindMe.compareTo(farAway) < 0);
        assertTrue(farAway.compareTo(animals) < 0);
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

        Handle animalsUnequal = new Handle(memory, 63, 8);
        assertNotEquals(animals, animalsUnequal);
        
        assertNotEquals(animals, howYouRemindMe);
    }

    /**
     * Tests the functionality of toString based on known offsets
     * and lengths
     */
    public void testToString() {
        assertEquals("0", nickelback.toString());
        assertEquals("10", photograph.toString());
        assertEquals("20", rockstar.toString());
        assertEquals("28", savinMe.toString());
        assertEquals("37", howYouRemindMe.toString());
        assertEquals("54", farAway.toString());
        assertEquals("62", animals.toString());
    } // end testToString
} // end HandleTest
