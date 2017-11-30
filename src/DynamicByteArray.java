import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents our dynamic byte array which holds all records of
 * songs and artists in main memory.
 * 
 * @author Chris Dare (cdare77@vt.edu)
 * @version 11/29/2017
 */
public class DynamicByteArray {

    // ---------------- PRIVATE VARIABLES ----------------

    private byte[] memory; // actual array storing records
    private int blockSize; // initial size of array; will
                           // affect how much we expand the
                           // table by
    private int offset;
    // references to the hash tables we will be affecting
    private HashTable artistTable;
    private HashTable songTable;

    // ---------------- CONSTRUCTOR ---------------------

    /**
     * Default constructor for DynamicByteArray. Specifies the
     * initial blockSize which will ultimately affect the amount
     * by which we expand the table for insertions. Requires
     * references to an artist table and song table for clean
     * insertions and deletions/
     * 
     * @param initialSize
     *            -- initial size of array
     * @param aTable
     *            -- reference to artist hash table
     * @param sTable
     *            -- reference to song hash table
     */
    public DynamicByteArray(int initialSize, HashTable aTable,
            HashTable sTable) {
        blockSize = initialSize;
        memory = new byte[initialSize];
        offset = 0;
        artistTable = aTable;
        songTable = sTable;
    }

    // ---------------- PUBLIC METHODS -------------------

    /**
     * Inserts a given string into our byte array, preceded by
     * the length of the string in byte format and a nonzero flag
     * marking the record as active. Creates a handle reference
     * pointing back to the record and returns it for later use
     * in our data structures.
     * 
     * @param name
     *            -- string to insert into memory
     * @param isArtist
     *            -- flag which indicates whether current record
     *            being inserted is an artist
     * @return a handle reference pointing to the object just
     *         inserted
     */
    public Handle insert(String name, boolean isArtist) {

        Handle toRet;

        // To prevent inserting duplicates, we check
        // if the element is already in the hash table
        if (isArtist) {
            // look in artist table
            toRet = artistTable.search(name);
        }
        else {
            // look in song table
            toRet = songTable.search(name);
        }
        if (toRet != null) {
            // the current name is in our table, and thus
            // in memory as well. Return a reference to this
            // element
            return toRet;
        }

        // find number of bytes to insert into memory
        byte[] nameArr = name.getBytes();
        short length = (short) nameArr.length;
        // Expand memory whenever current array size is not
        // enough
        if (offset + length + 3 >= memory.length) {
            this.expandMemory();
        }
        // mark flag as active since we just inserted it
        memory[offset++] = 0x01;
        // store length in little endian order
        memory[offset++] = (byte) (length & 0xff);
        memory[offset++] = (byte) ((length >> 8) & 0xff);
        // copy string into next 'length' bytes
        System.arraycopy(nameArr, 0, memory, offset,
                length);

        // place our new handle into respective hash table
        toRet = new Handle(memory, offset, length);
        if (isArtist) {
            artistTable.insert(toRet);
        }
        else {
            songTable.insert(toRet);
        }

        // update pointer to offset
        offset += length;

        // return new handle pointing to record just inserted
        return toRet;
    } // end insert

    /**
     * Marks the flag of the record we are searching for as 0x00
     * in our byte array
     * 
     * @param handle
     *            -- pointer to record we wish to remove
     * @param isArtist
     *            -- flag which indicates whether record we are
     *            removing is an artist
     */
    public void delete(Handle handle, boolean isArtist) {
        // make corresponding marks to memory (null
        // out the flag)
        int offset = handle.getOffset();
        memory[offset - 3] = 0x00;

        if (isArtist) {
            // handle points to an artist; thus remove from
            // artist table
            artistTable.delete(handle);
        }
        else {
            // handle points to a song; thus remove from song
            // table
            songTable.delete(handle);
        }
    } // end delete from memory

    @Override
    public String toString() {
        short length;
        List<String> stringList = new LinkedList<String>();

        for (int i = 0; i < memory.length - 2; i += length + 3) {

            // get length from second and third byte after offset
            ByteBuffer bb = ByteBuffer.allocate(2);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            bb.put(memory[i + 1]);
            bb.put(memory[i + 2]);
            length = bb.getShort(0);

            if (memory[i] != 0x00) {
                // is a valid element
                String searchString = new String(
                        Arrays.copyOfRange(memory, i + 3,
                                i + 3 + length));
                stringList.add(searchString);
            } // end if
        } // end for loop

        return stringList.toString();
    } // end toString()

    // ---------------- PRIVATE METHODS -------------------

    /**
     * Private helper method which lets our 'memory' byte array
     * to be dynamic, expanding whenever there is not enough
     * space to insert. We expand by the initial size of our
     * memory, and copy over valid elements (ones with nonzero
     * flag)
     */
    private void expandMemory() {
        // replaces our current reference to memory
        byte[] newMemory = new byte[memory.length + blockSize];
        // length of every record we insert
        short length;
        // since we dont insert records with zero flag, our
        // offset and newOffset may not always coincide
        int newOffset = 0;

        // iterate over our current memory
        for (int i = 0; i < memory.length - 2; i += length + 3) {

            // get length from second and third byte after offset
            ByteBuffer bb = ByteBuffer.allocate(2);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            bb.put(memory[i + 1]);
            bb.put(memory[i + 2]);
            length = bb.getShort(0);

            if (memory[i] != 0x00) {
                // Flag is nonzero which means that we should
                // copy data over to new array
                String searchString = new String(
                        Arrays.copyOfRange(memory, i + 3,
                                i + 3 + length));

                // Move data over to new array, including flag
                // and length
                System.arraycopy(memory, i, newMemory, newOffset,
                        length + 3);

                // modify the handle pointing to this data
                // so that it points to new location
                Handle artist = artistTable.search(searchString);
                Handle song = songTable.search(searchString);

                if (song != null && song.getOffset() == i + 3) {
                    // data pointed to is a song
                    song.setOffset(newOffset + 3);
                    song.setMemory(newMemory);
                }
                else {
                    // data pointed to is an artist
                    artist.setOffset(newOffset + 3);
                    artist.setMemory(newMemory);
                }
                // update offset pointer
                newOffset += length + 3;
            } // end if (valid element to copy)
        } // end for

        memory = newMemory;
        offset = newOffset;
    } // end expandMemory

} // end dynamicByteArray
