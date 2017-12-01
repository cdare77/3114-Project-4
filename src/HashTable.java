
/**
 * Handle implementation of a dynamic hash table. Uses the string
 * folding method provided by OpenDSA 10.03.03 to hash the
 * strings pointed to by the handles and then find the correct
 * placement using quadratic probing. Supports insertion,
 * deletion, and searching using a String representing either an
 * artists name or a song. Dynamically resizes whenever the table
 * becomes half full by reallocating a table of twice the size
 * and rehashing all elements.
 * 
 * @author Chris Dare (cdare77@vt.edu)
 * @version 11/23/2017
 */
public class HashTable {

    // ---------------- PRIVATE VARIABLES ----------------

    private Handle[] table; // array containing data
    private int logicalSize; // current number of elements
    private int defaultSize;
    private static final Handle GRAVESTONE =
            new Handle(new byte[0], -1, -1); // Gravestone
                                             // pointer

    // ---------------- CONSTRUCTOR ---------------------
    /**
     * Constructor for our hash table. Requires a default size to
     * be specified.
     * 
     * @param size
     *            -- default size of our hash table
     */
    public HashTable(int size) {
        logicalSize = 0;
        defaultSize = size;
        table = new Handle[size];
    }

    // ---------------- PUBLIC METHODS -------------------

    /**
     * Dereferences all objects in our table and creates a new
     * table with the same initial size as the previous table
     */
    public void clear() {
        table = new Handle[defaultSize];
        logicalSize = 0;
    }

    /**
     * Searches our hashtable for a specific string by hashing
     * the string, and then searching down the probe sequence. We
     * pass over gravestones since the element may be further
     * along our probe sequence. If the string is found we return
     * a reference to the Handle pointing to that string. If the
     * string is not found, then we simply return a null
     * reference.
     * 
     * @param name
     *            -- string to hash
     * @return reference to handle found, null if not found
     */
    public Handle search(String name) {
        int homePos = hash(table, name);
        int pos;

        // QUADRATIC PROBE
        for (int i = 0; i < table.length; i++) {
            // possibly iterate over entire table, but will
            // likely stop before then
            pos = (homePos + i * i) % table.length;
            if (table[pos] == null) {
                break;
            }
            else if (table[pos] != GRAVESTONE
                    && table[pos].getStringAt().equals(name)) {
                return table[pos];
            }
        } // end for

        return null;
    }

    /**
     * Public method to insert into our table. If the number of
     * elements exceeds half the logical size of our current
     * table, we expand the table by a factor of 2.
     * 
     * @param handle
     *            -- handle we wish to insert
     */
    public void insert(Handle handle) {
        if (logicalSize + 1 > table.length / 2) {
            table = this.expandTable(table);
        }
        table = this.insertHelper(table, handle);
        logicalSize++;
    }

    /**
     * Removes a specified handle from the hash table by looking
     * at the home position then moving along the probe sequence
     * until either the element or null is found. If the element
     * is not in the hash table then we return null.
     * 
     * @param handle
     *            -- handle to search for
     * @return reference to removed handle. Null if not found
     */
    public Handle delete(Handle handle) {

        int homePos = hash(table, handle.getStringAt());
        int pos;

        // QUADRATIC PROBE
        for (int i = 0; i < table.length; i++) {
            pos = (homePos + i * i) % table.length;

            if (table[pos] == null) {
                // element not in table
                break;
            }
            else if (table[pos].equals(handle)) {
                // we are currently at the element
                Handle toRet = table[pos];
                table[pos] = GRAVESTONE;
                logicalSize--;
                return toRet;
            }
        } // end for-loop

        return null; // could not find element
    } // end delete()

    /**
     * Accessor method for our logicalSize
     * 
     * @return logicalSize (i.e. number of elements currently in
     *         hash table)
     */
    public int size() {
        return logicalSize;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        int length = table.length;

        for (int i = 0; i < length; i++) {
            if (table[i] != null && table[i] != GRAVESTONE) {
                builder.append(String.format("|%s| %d\n",
                        table[i].getStringAt(), i));
            } // end outer if
        } // end for loop

        return builder.toString();
    } // end toString()

    // ---------------- PRIVATE METHODS -------------------

    /**
     * Uses a modified string folding method on the string
     * pointed to by our handle class. Looks at the size of our
     * table in order to determine proper wrapping.
     * 
     * @param myTable
     *            -- pointer to table to hash into
     * @param toHash
     *            -- String we wish to hash
     * @return home position in table we should insert at
     */
    private int hash(Handle[] myTable,
            String toHash) {

        int intLength = toHash.length() / 4;
        long sum = 0;
        for (int j = 0; j < intLength; j++) {
            char[] c = toHash.substring(j * 4, (j * 4) + 4)
                    .toCharArray();
            long mult = 1;
            for (int k = 0; k < c.length; k++) {
                sum += c[k] * mult;
                mult *= 256;
            } // end inner for
        } // end outer for
        char[] c = toHash.substring(intLength * 4).toCharArray();
        long mult = 1;
        for (int k = 0; k < c.length; k++) {
            sum += c[k] * mult;
            mult *= 256;
        }
        return (int) (Math.abs(sum) % myTable.length);
    } // end hash()

    /**
     * Simply inserts a handle into the hash table. The function
     * will continue probing until the handle is inserted into
     * the table specified, expanding if necessary
     * 
     * @param handle
     *            -- handle to insert
     * @return reference to table after successful insertion
     */
    private Handle[] insertHelper(Handle[] myTable,
            Handle handle) {

        int homePos = hash(myTable, handle.getStringAt());
        int pos;

        // QUADRATIC PROBE
        for (int i = 0; i < myTable.length; i++) {
            pos = (homePos + i * i) % myTable.length;
            if (myTable[pos] == null
                    || myTable[pos] == GRAVESTONE) {
                myTable[pos] = handle;
                // break from loop
                return myTable;
            } // end if
        } // end for loop

        // we have gone through an entire iteration of the hash
        // table and still cannot find a spot. Thus, we expand
        // the table and retry
        myTable = this.expandTable(myTable);

        return insertHelper(myTable, handle);
    }

    /**
     * Private helper method which creates a new table twice the
     * size of our previous table and inserts all valid records.
     * Gravestone does not count as a valid record.
     * 
     * @param myTable
     *            -- table we wish to expand
     * @return reference to new expanded table
     */
    private Handle[] expandTable(Handle[] myTable) {
        Handle[] newTable = new Handle[myTable.length * 2];
        // iterate sequentially through previous table
        for (int i = 0; i < myTable.length; i++) {
            // copy over all non-null elements that are not our
            // gravestone
            if (myTable[i] != null && myTable[i] != GRAVESTONE) {
                // valid element to copy over
                newTable =
                        this.insertHelper(newTable, myTable[i]);
            }
        } // end for-loop
        return newTable;
    } // end expandTable
} // end HashTable
