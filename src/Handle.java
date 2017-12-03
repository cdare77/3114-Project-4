import java.util.Arrays;

/**
 * Class which represents a pointer to a record in the byte array
 * specified
 * 
 * @author Chris Dare (cdare77@vt.edu)
 * @version 11/23/2017
 *
 */
public class Handle implements Comparable<Handle> {

    // ---------------- PRIVATE VARIABLES ---------------------
    private int offset;
    private int length;
    private byte[] memory;

    // ---------------- PUBLIC METHODS ---------------------
    /**
     * Standard constructor, requires byte array within which
     * object will be pointed to, offset of pointer, and length
     * of record
     * 
     * @param mem
     *            -- byte array within which object is pointed to
     * @param start
     *            -- offset of start of record
     * @param len
     *            -- length of record
     */
    public Handle(byte[] mem, int start, int len) {
        this.memory = mem;
        this.offset = start;
        this.length = len;
    }

    /**
     * Accessor method for length
     * 
     * @return length
     */
    public int getLength() {
        return length;
    }

    /**
     * Accessor method for starting location
     * 
     * @return offset
     */
    public int getOffset() {
        return offset;
    }
    
    /**
     * Accessor method for the byte array
     * @return reference to the byte array
     */
    public byte[] getMemory() {
        return memory;
    }

    /**
     * Modifier method for reference to memory
     * @param mem -- new byte array to point to
     */
    public void setMemory(byte[] mem) {
        memory = mem;
    }
    
    /**
     * Modifier method for length
     * @param len -- new length
     */
    public void setLength(int len) {
        length = len;
    }
    
    /**
     * Modifier method for offset
     * @param off -- new offset
     */
    public void setOffset(int off) {
        offset = off;
    }
    
    /**
     * Accessor method for the data pointed to by Handle.
     * Equivalent to the *(pointer) functionality in C.
     * 
     * @return data pointed to by Handle
     */
    public String getStringAt() {
        return new String(Arrays.copyOfRange(memory,
                offset, offset + length));
    }

    @Override
    public int compareTo(Handle other) {
//        String thisName = this.getStringAt();
//        String otherName = other.getStringAt();
//
//        return thisName.compareTo(otherName);
        return this.getOffset() - other.getOffset();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            // this cannot be null by object oriented design
            return false;
        }
        else if (!other.getClass().equals(this.getClass())) {
            // two instances of different classes cannot be equal
            return false;
        }
        else {
            // if handles point to same record, they are likely equal
            Handle otherHandle = (Handle) other;
            return this.getLength() == otherHandle.getLength()
                    && this.getOffset() == otherHandle
                    .getOffset();
        } // end else
    } // end equals
    
    @Override
    public String toString() {
        return String.format("%d", offset);
//        return this.getStringAt();
    }

} // end Handle class
