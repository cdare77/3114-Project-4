import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import student.TestCase;

/**
 * Allows us to test our driver class with the knowledge that all
 * our I/O functionalitiy works. Hence, any errors must be due to
 * the source code we have written.
 * 
 * @author Chris Dare (cdare77@vt.edu)
 * @version 11/29/2017
 */
public class SongSearchTest extends TestCase {

    // ------------------- PRIVATE VARIABLES -------------------

    // references to our command file and
    // the writer we will use to create commands
    private File commandFile;
    private FileWriter writer;

    // arguments for SongSearch.main
    private String[] args;

    // allow us to capture System.out into
    // a string by using an output stream
    private ByteArrayOutputStream baos;
    private PrintStream ps;
    private static final PrintStream old = System.out;

    // ------------------- PUBLIC METHODS ----------------------

    @Override
    public void setUp() {
        args = new String[3];
        // initial hash size is 64
        args[0] = "64";
        // initial block size is 256
        args[1] = "256";

        baos = new ByteArrayOutputStream();
        ps = new PrintStream(baos);
        System.setOut(ps);

        try {
            // set up file in runtime environment
            commandFile =
                    File.createTempFile("commandFile", ".txt");
            writer = new FileWriter(commandFile);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        args[2] = commandFile.getAbsolutePath();
    }

    /**
     * Tests the functionality of insert, and checks that it
     * properly inserts to both trees, both hash tables, as well
     * as memory
     */
    public void testInsert() {
        try {
            writer.write(
                    "insert Nickelback<SEP>Rockstar\n" +
                            "insert Nickelback<SEP>Animals\n" +
                            "insert Nickelback<SEP>Photograph\n"
                            +
                            "insert Nickelback<SEP>Savin’ Me\n" +
                            "insert Nickelback<SEP>How You Remind Me\n"
                            +
                            "insert Nickelback<SEP>Far Away\n" +
                            "insert Linkin Park<SEP>Numb\n" +
                            "insert Linkin Park<SEP>Crawling\n" +
                            "insert Linkin Park<SEP>One Step Closer\n"
                            +
                            "insert Linkin Park<SEP>In The End\n"
                            +
                            "insert Linkin Park<SEP>Points of Authority\n"
                            +
                            "insert Linkin Park<SEP>Breaking The Habit\n"
                            +
                            "insert Breaking Benjamin<SEP>The Diary of "
                            + "Jane\ninsert Breaking Benjamin<SEP>"
                            + "Breath\nprint artist\n" +
                            "print song\n" +
                            "print tree\n list artist Nickelback");
            // free system resources
            writer.close();

            // call driver
            SongSearch.main(args);

            ps.close();
            System.setOut(old);

//             System.out.print(baos.toString());
        } // end try
        catch (IOException e) {
            e.printStackTrace();
        } // end catch
    } // end testInsert

    /**
     * Tests the functionality of remove, checks that it properly
     * removes from its respective hash table, as well as the
     * value hash table if necessary
     */
    public void testRemove() {
        try {
            writer.write("insert Nickelback<SEP>Rockstar\n" +
                    "insert Nickelback<SEP>Animals\n" +
                    "insert Nickelback<SEP>Photograph\n" +
                    "insert Nickelback<SEP>Savin’ Me\n" +
                    "insert Nickelback<SEP>How You Remind Me\n" +
                    "insert Nickelback<SEP>Far Away\n" +
                    "insert Linkin Park<SEP>Numb\n" +
                    "insert Linkin Park<SEP>Crawling\n" +
                    "insert Linkin Park<SEP>One Step Closer\n" +
                    "insert Linkin Park<SEP>In The End\n" +
                    "insert Linkin Park<SEP>Points of Authority\n"
                    +
                    "insert Linkin Park<SEP>Breaking The Habit\n"
                    +
                    "insert Breaking Benjamin<SEP>The Diary of Jane\n"
                    +
                    "insert Breaking Benjamin<SEP>Breath\n" +
                    "insert Pearl Jam<SEP>Breath\n" +
                    "print artist\n" +
                    "print song\n" +
                    "remove song Breath\n" +
                    "print artist\n" +
                    "print song");

            // free system resources
            writer.close();

            // call driver
            SongSearch.main(args);

            ps.close();
            System.setOut(old);

//             System.out.print(baos.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        } // end catch
    } // end testRemove()

    /**
     * Tests the functionality of delete, and checks that under
     * special circumstances (last song by an artist, last artist
     * having written a specific song name) the hash table and
     * memory are affected as well
     */
    public void testDelete() {
        try {
            writer.write("insert Nickelback<SEP>Rockstar\n" +
                    "insert Nickelback<SEP>Animals\n" +
                    "insert Nickelback<SEP>Photograph\n" +
                    "insert Nickelback<SEP>Savin’ Me\n" +
                    "insert Nickelback<SEP>How You Remind Me\n" +
                    "insert Nickelback<SEP>Far Away\n" +
                    "insert Linkin Park<SEP>Numb\n" +
                    "insert Linkin Park<SEP>Crawling\n" +
                    "insert Linkin Park<SEP>One Step Closer\n" +
                    "insert Linkin Park<SEP>In The End\n" +
                    "insert Linkin Park<SEP>Points of Authority\n"
                    +
                    "insert Linkin Park<SEP>Breaking The Habit\n"
                    +
                    "insert Breaking Benjamin<SEP>The Diary of Jane\n"
                    +
                    "insert Breaking Benjamin<SEP>Breath\n" +
                    "insert Pearl Jam<SEP>Breath\n" +
                    "print artist\n" +
                    "print song\n" +
                    "delete Pearl Jam<SEP>Breath\n" +
                    "print artist\n" +
                    "print song\n" +
                    "delete Breaking Benjamin<SEP>Breath\n" +
                    "print artist\n" +
                    "print song\n" +
                    "delete Nickelback<SEP>Savin’ Me\n" +
                    "print artist\n" +
                    "print song\n" +
                    "delete Foo Fighters<SEP>Learning To Fly\n" +
                    "print artist\n" +
                    "print song");

            // free system resources
            writer.close();

            // call driver
            SongSearch.main(args);

            ps.close();
            System.setOut(old);

            System.out.print(baos.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        } // end catch
    } // end testDelete
} // end SongSearchTest
