
public class KVPair<Key extends Comparable<Key>,
        Value extends Comparable<Value>>
        implements
            Comparable<KVPair<Key, Value>> {
    private Key key;
    private Value value;

    public KVPair(Key myKey, Value myValue) {
        this.key = myKey;
        this.value = myValue;
    }

    public Key getKey() {
        return key;
    }

    public Value getValue() {
        return value;
    }

    public void setKey(Key myKey) {
        this.key = myKey;
    }

    public void setValue(Value myValue) {
        this.value = myValue;
    }

    @Override
    public int compareTo(KVPair<Key, Value> other) {
        int compKeys = key.compareTo(other.getKey());
        if (compKeys == 0) {
            return value.compareTo(other.getValue());
        }
        return compKeys;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", key.toString(),
                value.toString());
    }

}
