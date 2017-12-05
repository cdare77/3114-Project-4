
/**
 * Represents a joining of two Comparable data types for balanced
 * structures which compare based off key values but may store
 * data, as represented by Value. Value should be comparable in
 * the instance that keys are equal, we dont want to treat two
 * unequal pairs as equal (i.e. the KVPairs [0, 10] and [0, 5]
 * should not be equal
 * 
 * @author Chris Dare (cdare77@vt.edu)
 * 
 * @version 12/3/2017
 *
 * @param <Key>
 *            generic reference to the primary comparison value
 * @param <Value>
 *            generic reference to the data stored, which can
 *            also be compared in case primary keys are equal
 */
public class KVPair<Key extends Comparable<Key>,
        Value extends Comparable<Value>>
        implements
            Comparable<KVPair<Key, Value>> {
    
    // ------------------- PRIVATE VARIABLES-------------------
    private Key key;
    private Value value;

    /**
     * Constructor, assigns key and value of KVPair
     * @param myKey -- key in KVPair
     * @param myValue -- value in KVPair
     */
    public KVPair(Key myKey, Value myValue) {
        this.key = myKey;
        this.value = myValue;
    }

    /**
     * Accessor method for key
     * @return key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Accessor method for value
     * @return value
     */
    public Value getValue() {
        return value;
    }

    /**
     * Modifier method for key
     * @param myKey -- desired key
     */
    public void setKey(Key myKey) {
        this.key = myKey;
    }

    /**
     * Modifier method for value
     * @param myValue -- desired value
     */
    public void setValue(Value myValue) {
        this.value = myValue;
    }

    @Override
    public int compareTo(KVPair<Key, Value> other) {
        int compKeys = key.compareTo(other.getKey());
        if (compKeys == 0) {
            // if primary keys are equal we must consider
            // the data (value) stored
            return value.compareTo(other.getValue());
        }
        // keys are equal
        return compKeys;
    }

    @Override
    public String toString() {
        return String.format("(%s,%s)", key.toString(),
                value.toString());
    }

} // end KVPair
