

public class HashTable {

    private Handle[] table;
    private byte[] memory;
    private int logicalSize;
    private int physicalSize;
    private static final Handle GRAVESTONE =
            new Handle(new byte[0], -1, -1);;

    public HashTable(byte[] mem, int size) {
        memory = mem;
        logicalSize = 0;
        physicalSize = size;
        table = new Handle[physicalSize];
    }
    
    public boolean insert(Handle handle) {
        if (logicalSize + 1 > physicalSize / 2) {
            expandTable();
        }
        int pos = hash(table, handle);
        if (pos == -1) {
            return false;
        }
        table[pos] = handle;
        logicalSize++;
        return true;
    }
    

    private int hash(Handle[] myTable, Handle handle) {
        int homePos = hashHelper(myTable, handle);
        return quadraticProbe(myTable, homePos);
    }

    private int hashHelper(Handle[] myTable,
            Handle toInsert) {
        String name = toInsert.getStringAt();
        
        int intLength = name.length() / 4;
        long sum = 0;
        for (int j = 0; j < intLength; j++)
        {
            char[] c = name.substring(j * 4, (j * 4) + 4).toCharArray();
            long mult = 1;
            for (int k = 0; k < c.length; k++)
            {
                sum += c[k] * mult;
                mult *= 256;
            }
        }
        char[] c = name.substring(intLength * 4).toCharArray();
        long mult = 1;
        for (int k = 0; k < c.length; k++)
        {
            sum += c[k] * mult;
            mult *= 256;
        }
        return (int)(Math.abs(sum) % myTable.length);
    }

    private int quadraticProbe(Handle[] myTable, int homePos) {
        int M = myTable.length;
        int pos;
        
        for (int i = 0; i < M; i++) {
            pos = (homePos + i*i) % M;
            if (myTable[pos] == null) {
                return pos;
            }
        }
        
        return -1;
    }
    
    private void expandTable() {
        Handle[] newTable = new Handle[physicalSize * 2];
        for (int i = 0; i < physicalSize; i++) {
            if (table[i] != null && table[i] != GRAVESTONE) {
                int newPos = hash(newTable, table[i]);
                newTable[newPos] = table[i];
            }
        }
        physicalSize *= 2;
        table = newTable;
    }

}
