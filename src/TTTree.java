import java.util.LinkedList;
import java.util.List;
import javax.management.InstanceNotFoundException;

/**
 * Standard implementation of a 2-3+ tree. Since our 2-3+ node
 * handles most of the operations for its local subtree, we
 * simply extend the operations by defining a fixed starting
 * point (i.e. the root) and performing all operations on such
 * root.
 * 
 * @author Chris Dare (cdare77@vt.edu)
 * @version 11/21/2017
 * @param <Key>
 *            -- generic reference for comparable
 * @param <Value>
 *            -- generic reference for data stored
 */
public class TTTree<Key extends Comparable<? super Key>,
        Value extends Comparable<? super Value>> {

    // ---------------- PRIVATE VARIABLES---------------------
    private TTNode<Key, Value> root;
    private int size;

    // ---------------- CONSTRUCTOR---------------------------

    /**
     * Default constructor for 2-3+ Tree
     */
    public TTTree() {
        root = null;
        size = 0;
    }

    // ---------------- PUBLIC METHODS------------------------

    /**
     * Inserts a specified Key-Value pair into the tree by
     * calling the insertHelper method on the tree's root
     * 
     * @param k
     *            -- key to insert corresponding to KV pair
     * @param val
     *            -- value to insert corresponding to KV pair
     */
    public void insert(Key k, Value val) {
        if (this.isEmpty()) {
            // Empty tree: create a leaf node for root
            root = new TTNode<Key, Value>(k, val, null, null,
                    null, null, null);
        }
        else {
            root = root.insertHelp(k, val);
        }
        size++;
    } // end insert

    /**
     * removes the specified key from the tree by calling the
     * roots removeHelper method
     * 
     * @param k
     *            -- key of KV pair we wish to remove
     * @return reference to value stored in KV pair
     * @throws InstanceNotFoundException
     *             if key is not in tree
     */
    public Value remove(Key k) throws InstanceNotFoundException {
        if (this.isEmpty()) {
            throw new InstanceNotFoundException();
        }

        Value toRet = root.removeHelper(k);

        if (toRet == null) {
            // removeHelper returns null when instance
            // is not in tree. We continue this error handling
            // by throwing an exception
            throw new InstanceNotFoundException();
        }
        if (root.lkey() == null) {
            // instance where removal caused imbalance
            // that could not be contained at a lower
            // level, and thus the imbalance worked its
            // way up to root. It is safe to throw away this
            // root now (i.e. go to next balanced node).
            root = root.leftChild();
        }
        size--;
        return toRet;
    }

    public List<Value> rangeSearch(Key low, Key high) {
        LinkedList<Value> toRet = new LinkedList<Value>();
        if (!this.isEmpty()) {
            root.rangeSearchHelper(low, high, toRet);
        }
        return toRet;
    }

    /**
     * Gives the height of the tree by calling on the height of
     * the root. An empty tree is regarded as having height 0.
     * 
     * @return distance from the root + 1, or 0 if tree is empty
     */
    public int height() {
        if (this.isEmpty()) {
            return 0;
        }
        return root.height();
    } // end height

    /**
     * Accessor method for the number of elements in the tree
     * 
     * @return number of elements in the tree
     */
    public int size() {
        return size;
    } // end size()

    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "";
        }
        return root.printDepthFirst();
    }

    /**
     * Logically clears the tree and leaves previous nodes to be
     * handled by Java garbage collector
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Indicates whether our tree is now empty
     * 
     * @return True if tree is empty, false otherwise
     */
    public boolean isEmpty() {
        return root == null;
    } // end isEmpty
} // end TTTree
