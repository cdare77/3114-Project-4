import java.util.Arrays;

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

}
