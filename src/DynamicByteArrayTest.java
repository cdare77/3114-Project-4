import student.TestCase;

/**
 * Tests the functionality of our DynamicByteArray and especially
 * how it interacts with our two hash tables. Since inserting and
 * deleting in memory result in inserting and deleting in a hash
 * table, respectively, we expect to test all three data
 * structures
 * 
 * @author Chris Dare (cdare77@vt.edu)
 * @version 11/29/2017
 *
 */
public class DynamicByteArrayTest extends TestCase {

    // ---------------- PRIVATE VARIABLES ----------------

    private DynamicByteArray memory;
    private HashTable artistTable;
    private HashTable songTable;

    // ---------------- PUBLIC METHODS -------------------

    @Override
    public void setUp() {
        artistTable = new HashTable(8);
        songTable = new HashTable(8);
        memory = new DynamicByteArray(32, artistTable,
                songTable);
    }

    /**
     * Tests the functionality of insert, in both successful and
     * unsucessful quadratic probes, and how they affect both our
     * DynamicByteArray and our hash tables
     */
    public void testInsert() {
        memory.insert("Nickelback", true);
        memory.insert("Animals", false);
        memory.insert("Nickelback", true);
        memory.insert("Savin' Me", false);
        memory.insert("Nickelback", true);
        memory.insert("Photograph", false);
        memory.insert("Nickelback", true);
        // unsuccessful quadratic probe on
        // nickelback forces hash to expand
        memory.insert("How You Remind Me", false);
        memory.insert("Nickelback", true);
        memory.insert("Photograph", false);
        memory.insert("Nickelback", true);
        memory.insert("Rockstar", false);
        memory.insert("Linkin Park", true);
        memory.insert("Numb", false);
        memory.insert("Linkin Park", true);
        memory.insert("Shadow of the Day", false);

        assertEquals(
                "[Nickelback, Animals, Savin' Me, Photograph, "
                        + "How You Remind Me, Rockstar, Linkin"
                        + " Park, Numb, Shadow of the Day]",
                memory.toString());

        // examine hash tables
        assertEquals("|Nickelback| 6\n" +
                "|Linkin Park| 7\n",
                artistTable.toString());
        assertEquals(
                "|Animals| 2\n" +
                        "|Rockstar| 5\n" +
                        "|Savin' Me| 6\n" +
                        "|How You Remind Me| 7\n" +
                        "|Shadow of the Day| 10\n" +
                        "|Numb| 14\n" +
                        "|Photograph| 15\n",
                songTable.toString());
    }

    /**
     * Tests the functionality of remove on both our
     * DynamicByteArray and our hash tables
     */
    public void testRemove() {
        memory.insert("Nickelback", true);
        memory.insert("Animals", false);
        memory.insert("Nickelback", true);
        memory.insert("Savin' Me", false);
        memory.insert("Nickelback", true);
        memory.insert("Photograph", false);
        memory.insert("Nickelback", true);
        // unsuccessful quadratic probe
        memory.insert("How You Remind Me", false);
        memory.insert("Nickelback", true);
        memory.insert("Photograph", false);
        memory.insert("Nickelback", true);
        memory.insert("Rockstar", false);
        memory.insert("Linkin Park", true);
        memory.insert("Numb", false);
        memory.insert("Linkin Park", true);
        memory.insert("Shadow of the Day", false);

        Handle nickelback = artistTable.search("Nickelback");
        memory.delete(nickelback, true);
        Handle numb = songTable.search("Numb");
        memory.delete(numb, false);

        assertEquals(
                "[Animals, Savin' Me, Photograph, How You Remind"
                        + " Me, Rockstar, Linkin Park, Shadow of"
                        + " the Day]",
                memory.toString());

        // examine hash tables
        assertEquals("|Linkin Park| 7\n",
                artistTable.toString());
        assertEquals(
                "|Animals| 2\n" +
                        "|Rockstar| 5\n" +
                        "|Savin' Me| 6\n" +
                        "|How You Remind Me| 7\n" +
                        "|Shadow of the Day| 10\n" +
                        "|Photograph| 15\n",
                songTable.toString());
    }

    /**
     * Tests the functionality of toString on an empty table and
     * nonempty table, as well as the case that one of our
     * elements has a null flag
     */
    public void testToString() {
        assertEquals("[]", memory.toString());
        memory.insert("Nickelback", true);
        memory.insert("Animals", false);
        memory.insert("Nickelback", true);
        memory.insert("Savin' Me", false);
        memory.insert("Nickelback", true);
        memory.insert("Photograph", false);
        memory.insert("Nickelback", true);
        // unsuccessful quadratic probe
        memory.insert("How You Remind Me", false);

        assertEquals(
                "[Nickelback, Animals, Savin' Me, Photograph,"
                        + " How You Remind Me]",
                memory.toString());
    } // end testToString
} // end DynamicByteArrayTest
