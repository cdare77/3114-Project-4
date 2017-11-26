import java.util.Arrays;

/**
 * Class which represents a pointer to a record in our memory
 * 
 * @author Chris Dare (cdare77@vt.edu)
 * @version 11/23/2017
 *
 */
public class Handle implements Comparable<Handle> {

    private int startingLocation;
    private int length;
    private byte[] memory;

    public Handle(byte[] mem, int start, int len) {
        this.memory = mem;
        this.startingLocation = start;
        this.length = len;
    }

    public int getLength() {
        return length;
    }

    public int getStartingLocation() {
        return startingLocation;
    }

    public String getStringAt() {
        return new String(Arrays.copyOfRange(memory,
                startingLocation, startingLocation + length));
    }

    @Override
    public int compareTo(Handle o) {
        String thisName = this.getStringAt();
        String otherName = o.getStringAt();

        return thisName.compareTo(otherName);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        else if (!other.getClass().equals(this.getClass())) {
            return false;
        }
        else {
            Handle otherHandle = (Handle) other;
            if (this.getLength() == otherHandle.getLength()
                    && this.getStartingLocation() == otherHandle
                            .getStartingLocation()) {
                return true;
            }
            return false;
        } // end else
    } // end equals

} // end Handle class
