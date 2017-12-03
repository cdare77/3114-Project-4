import student.TestCase;

/**
 * Tests the functionality of hash table against desired
 * functionality
 * 
 * @author Chris Dare (cdare77@vt.edu)
 * @author Kenneth Worden (kworden@vt.edu)
 * @version 11/24/2017
 *
 */
public class HashTableTest extends TestCase {

    // ----------------PRIVATE VARIABLES------------------
    private HashTable table;
    private Handle nickelback;
    private Handle photograph;
    private Handle rockstar;
    private Handle savinMe;
    private Handle howYouRemindMe;
    private Handle farAway;
    private Handle animals;

    // ---------------- PUBLIC METHODS -------------------

    /**
     * Sets up the test fixture.
     */
    @Override
    public void setUp() {
        byte[] temp;
        int offset = 0;
        byte[] memory = new byte[96];

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
        assertEquals(0, table.size());
        table.insert(animals);
        table.insert(farAway);
        table.insert(howYouRemindMe);

        assertEquals(
                "|Animals| 2\n" +
                        "|How You Remind Me| 6\n" +
                        "|Far Away| 7\n",
                table.toString());

        // Force an unsuccessful quadratic probe
        table.insert(nickelback);
        table.insert(photograph);
        table.insert(rockstar);
        table.insert(savinMe);

        // insert a duplicate into the table
        table.insert(farAway);

        assertEquals(
                "|Animals| 2\n" +
                        "|Rockstar| 5\n" +
                        "|How You Remind Me| 6\n" +
                        "|Far Away| 7\n" +
                        "|Far Away| 8\n" +
                        "|Savin' Me| 10\n" +
                        "|Photograph| 15\n" +
                        "|Nickelback| 22\n",
                table.toString());

        // start with a new table
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

        assertEquals(
                "|Photograph| 0\n" +
                        "|Animals| 2\n" +
                        "|Rockstar| 5\n" +
                        "|Nickelback| 6\n" +
                        "|Far Away| 7\n" +
                        "|How You Remind Me| 10\n" +
                        "|Nickelback| 15\n",
                table.toString());
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
        assertEquals(nickelback, table.search("Nickelback"));
        assertEquals(howYouRemindMe,
                table.search("How You Remind Me"));
        assertEquals(photograph, table.search("Photograph"));
        assertEquals(rockstar, table.search("Rockstar"));
        assertEquals(farAway, table.search("Far Away"));
        assertEquals(animals, table.search("Animals"));
        assertEquals(savinMe, table.search("Savin' Me"));

        // try to search for invalid element
        assertNull(table.search("Only the Good Die Young"));
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

        assertEquals(4, table.size());
        assertEquals(
                "|Nickelback| 2\n" +
                        "|Animals| 3\n" +
                        "|Savin' Me| 6\n" +
                        "|Far Away| 7\n",
                table.toString());
        assertEquals(nickelback, table.delete(nickelback));
        assertEquals(
                "|Animals| 3\n" +
                        "|Savin' Me| 6\n" +
                        "|Far Away| 7\n",
                table.toString());

        assertEquals(3, table.size());

        // test removing an element not in table
        assertNull(table.delete(nickelback));
    }

    /**
     * Tests functionality of toString on an empty table and
     * nonempty table
     */
    public void testToString() {
        assertEquals("",
                table.toString());

        table.insert(nickelback);
        table.insert(savinMe);
        table.insert(animals);
        table.insert(farAway);
        table.insert(howYouRemindMe);
        table.insert(photograph);
        table.insert(rockstar);

        assertEquals("|Photograph| 0\n" +
                "|Animals| 2\n" +
                "|Rockstar| 5\n" +
                "|Nickelback| 6\n" +
                "|Far Away| 7\n" +
                "|Savin' Me| 10\n" +
                "|How You Remind Me| 15\n",
                table.toString());

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
        assertEquals(0, table.size());
        assertEquals("", table.toString());
    }
} // end HashTableTest
