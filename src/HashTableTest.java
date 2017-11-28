import student.TestCase;

/**
 * Tests the functionality of hash table against desired
 * functionality
 * 
 * @author Chris Dare (cdare77@vt.edu)
 * @version 11/24/2017
 *
 */
public class HashTableTest extends TestCase {

    // ----------------PRIVATE VARIABLES------------------
    private HashTable table;
    private byte[] memory;
    private Handle nickelback;
    private Handle photograph;
    private Handle rockstar;
    private Handle savinMe;
    private Handle howYouRemindMe;
    private Handle farAway;
    private Handle animals;

    // ---------------- PUBLIC METHODS -------------------
    @Override
    public void setUp() {
        byte[] temp;
        int offset = 0;
        memory = new byte[96];

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

        table = new HashTable(2);
    } // end setUp

    /**
     * Tests insert against desired functionality and asserts
     * that table is properly expanded
     */
    public void testInsert() {
        assertEquals(table.size(), 0);
        assertTrue(table.insert(animals));
        assertTrue(table.insert(farAway));
        assertTrue(table.insert(howYouRemindMe));

        // Quadratic probing leads to endless loop
        // of taken spots
        assertFalse(table.insert(nickelback));
        assertTrue(table.insert(photograph));
        assertTrue(table.insert(rockstar));
        assertTrue(table.insert(savinMe));

        // insert a duplicate into the table
        assertTrue(table.insert(farAway));

        assertEquals(table.toString(),
                "Animals @ 2, Rockstar @ 5, How You Remind Me @ 6, "
                        + "Far Away @ 7, Far Away @ 8, Savin' Me @ 10,"
                        + " Photograph @ 15\nNumber of elements: 7");

        table.clear();

        table.insert(rockstar);
        table.insert(farAway);
        table.insert(savinMe);
        table.insert(animals);
        table.insert(howYouRemindMe);
        // nickelback gets quadratic probed from 6 to 15
        table.insert(nickelback);
        table.insert(photograph);

        table.delete(savinMe);
        // insert over a tombstone
        table.insert(nickelback);
        assertEquals(table.toString(),
                "Photograph @ 0, Animals @ 2, Rockstar @ 5, Nickelback"
                        + " @ 6, Far Away @ 7, How You Remind Me @ 10,"
                        + " Nickelback @ 15\nNumber of elements: 7");

    } // endTestInsert

    /**
     * Tests the functionality of search on all elements
     * contained in table and on an element not contained in the
     * table
     */
    public void testSearch() {
        // order which does not induce unsuccessful
        // quadratic probing
        table.insert(nickelback);
        table.insert(savinMe);
        table.insert(animals);
        table.insert(farAway);
        table.insert(howYouRemindMe);
        table.insert(photograph);
        table.insert(rockstar);

        // Make sure all elements appear in a search
        assertEquals(table.search("Nickelback"), nickelback);
        assertEquals(table.search("How You Remind Me"),
                howYouRemindMe);
        assertEquals(table.search("Photograph"), photograph);
        assertEquals(table.search("Rockstar"), rockstar);
        assertEquals(table.search("Far Away"), farAway);
        assertEquals(table.search("Animals"), animals);
        assertEquals(table.search("Savin' Me"), savinMe);

        // try to search for invalid element
        assertEquals(table.search("Only the Good Die Young"),
                null);
    }

    /**
     * Tests the functionality of Delete on the table when the
     * element deleted has to be retrieved by quadratic probing.
     * Tests on both elements in the table and not in table
     */
    public void testDelete() {
        // Quadratic probing on nickelback. Savin' Me and Far
        // Away hash to 6 and 7, respectively, while Nickelback
        // hashes to 6. Note table size is 8.
        table.insert(savinMe);
        table.insert(farAway);
        table.insert(nickelback);
        table.insert(animals);

        assertEquals(table.size(), 4);
        assertEquals(table.toString(),
                "Nickelback @ 2, Animals @ 3, Savin' Me @ 6,"
                        + " Far Away @ 7\nNumber of elements: 4");
        assertEquals(table.delete(nickelback), nickelback);
        assertEquals(table.toString(),
                "Animals @ 3, Savin' Me @ 6, Far Away @ 7\n" +
                        "Number of elements: 3");

        // test removing an element not in table
        assertEquals(table.delete(nickelback), null);
    }

    /**
     * Tests functionality of toString on an empty table and
     * nonempty table
     */
    public void testToString() {
        assertEquals(table.toString(), "\n" +
                "Number of elements: 0");

        table.insert(nickelback);
        table.insert(savinMe);
        table.insert(animals);
        table.insert(farAway);
        table.insert(howYouRemindMe);
        table.insert(photograph);
        table.insert(rockstar);
        assertEquals(table.toString(),
                "Photograph @ 0, Animals @ 2, Rockstar @ 5,"
                        + " Nickelback @ 6, Far Away @ 7, Savin'"
                        + " Me @ 10, How You Remind Me @ 15\nNumber"
                        + " of elements: 7");

    }

    /**
     * Tests the functionality of the clear method to assure
     * there are no values in our table
     */
    public void testClear() {
        table.insert(nickelback);
        table.insert(savinMe);
        table.insert(animals);
        table.insert(farAway);
        table.insert(howYouRemindMe);
        table.insert(photograph);
        table.insert(rockstar);

        table.clear();
        assertEquals(table.size(), 0);
        assertEquals(table.toString(), "\n" +
                "Number of elements: 0");
    }
} // end HashTableTest
