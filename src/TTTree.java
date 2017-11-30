import java.util.LinkedList;
import java.util.List;

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
public class TTTree<Key extends Comparable<? super Key>> {

    // ---------------- PRIVATE VARIABLES---------------------
    private TTNode<Key> root;
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
    public void insert(Key k) {
        if (this.isEmpty()) {
            // Empty tree: create a leaf node for root
            root = new TTNode<Key>(k, null, null, null, null);
        }
        else {
            root = root.insertHelp(k);
        }
        size++;
    } // end insert

    /**
     * removes the specified key from the tree by calling the
     * roots removeHelper method
     * 
     * @param k
     *            -- key of KV pair we wish to remove
     * 
     * @return reference to value stored in KV pair
     */
    public Key remove(Key k) {
        if (this.isEmpty()) {
            return null;
        }

        Key toRet = root.removeHelper(k);

        if (toRet == null) {
            // removeHelper returns null when instance
            // is not in tree. We continue this error handling
            // by throwing an exception
            return null;
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

    /**
     * Simply call our searchHelper method from the root to
     * ensure entire tree is traversed
     * 
     * @param toFind
     *            -- key we wish to search for
     * @return reference to key if in tree, null otherwise
     */
    public Key search(Key toFind) {
        if (this.isEmpty()) {
            return null;
        }
        return root.searchHelper(toFind);
    }

    /**
     * Inclusively searches the tree given a range of keys.
     * 
     * @param low
     *            - the low key
     * @param high
     *            - the high key
     * @return A list of the matching tree keys.
     */
    public List<Key> rangeSearch(Key low, Key high) {
        LinkedList<Key> toRet = new LinkedList<Key>();
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

    /**
     * Gets the String representation of this tree.
     * 
     * @return A String.
     */
    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "";
        }
        return root.printPreOrder(0);
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
