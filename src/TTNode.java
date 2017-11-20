import javax.management.InstanceNotFoundException;

/**
 * Implementation of a 2-3 tree which supports typical insertion,
 * deletion, search, range search, etc. Used in this class for
 * range searches on Song name and artist values
 * 
 * 
 * @author Chris Dare (cdare77@vt.edu)
 * @version 11/18/2017
 * @param <Key>
 * @param <Value>
 */
public class TTNode<Key extends Comparable<? super Key>, Value> {

    // -------------------PRIVATE VARIABLES------------------
    private Value lval; // The left record
    private Key lkey; // The node's left key
    private Value rval; // The right record
    private Key rkey; // The node's right key
    private TTNode<Key, Value> left; // Pointer to left child
    private TTNode<Key, Value> center; // Pointer to middle child
    private TTNode<Key, Value> right; // Pointer to right child

    /**
     * Default constructor
     */
    public TTNode() {
        center = left = right = null;
    }

    /**
     * Explicit constructor which sets all of our member
     * variables to the values passed in by the parameter
     * 
     * @param lk
     *            -- left key
     * @param lv
     *            -- left value
     * @param rk
     *            -- right key
     * @param rv
     *            -- right value
     * @param p1
     *            -- left child pointer
     * @param p2
     *            -- center child pointer
     * @param p3
     *            -- right child pointer
     */
    public TTNode(Key lk, Value lv, Key rk, Value rv,
            TTNode<Key, Value> p1, TTNode<Key, Value> p2,
            TTNode<Key, Value> p3) {
        lkey = lk;
        rkey = rk;
        lval = lv;
        rval = rv;
        left = p1;
        center = p2;
        right = p3;
    }

    /**
     * Indicates whether our node is a leaf
     * 
     * @return True if node is a leaf
     */
    public boolean isLeaf() {
        return left == null;
    }

    /**
     * Accessor method for our left child pointer
     * 
     * @return reference to left child
     */
    public TTNode<Key, Value> leftChild() {
        return left;
    }

    /**
     * Accessor method for our right child pointer
     * 
     * @return reference to right child
     */
    public TTNode<Key, Value> rightChild() {
        return right;
    }

    /**
     * Accessor method for our center child pointer
     * 
     * @return reference to center child
     */
    public TTNode<Key, Value> centerChild() {
        return center;
    }

    public Key lkey() {
        return lkey;
    } // Left key

    public Value lval() {
        return lval;
    } // Left value

    public Key rkey() {
        return rkey;
    } // Right key

    public Value rval() {
        return rval;
    } // Right value

    public void setLeft(Key k, Value e) {
        lkey = k;
        lval = e;
    }

    public void setRight(Key k, Value e) {
        rkey = k;
        rval = e;
    }

    public void setLeftChild(TTNode<Key, Value> it) {
        left = it;
    }

    public void setCenterChild(TTNode<Key, Value> it) {
        center = it;
    }

    public void setRightChild(TTNode<Key, Value> it) {
        right = it;
    }

    /**
     * Add a new key/value pair to the node. There might be a
     * subtree associated with the record being added. This
     * information comes in the form of a 2-3 tree node with one
     * key and a (possibly null) subtree through the center
     * pointer field.
     * 
     * @param node
     *            -- reference of node to add to current node
     * @return
     */
    public TTNode<Key, Value> add(TTNode<Key, Value> node) {
        if (rkey == null) { // Only one key, add here
            if (lkey.compareTo(node.lkey()) < 0) {
                rkey = node.lkey();
                rval = node.lval();
                center = node.leftChild();
                right = node.centerChild();
            }
            else {
                rkey = lkey;
                rval = lval;
                right = center;
                lkey = node.lkey();
                lval = node.lval();
                center = node.centerChild();
            }
            return this;
        }
        // node to add's left key is smaller
        else if (lkey.compareTo(node.lkey()) >= 0) {

            // create new node with node to add as left child and
            // this node as center child
            TTNode<Key, Value> newParent =
                    new TTNode<Key, Value>(lkey,
                            lval, null, null, node, this, null);

            // rotate pointers leftwards
            node.setLeftChild(left);
            left = center;
            center = right;
            right = null;
            // rotate key-value pair left
            lkey = rkey;
            lval = rval;
            rkey = null;
            rval = null;

            // return new parent
            return newParent;
        }
        else if (rkey.compareTo(node.lkey()) >= 0) { // Add
                                                     // center
            node.setCenterChild(
                    new TTNode<Key, Value>(rkey, rval,
                            null, null, node.centerChild(),
                            right, null));
            node.setLeftChild(this);
            rkey = null;
            rval = null;
            right = null;
            return node;
        }
        else { // Add right
            TTNode<Key, Value> newParent =
                    new TTNode<Key, Value>(rkey,
                            rval, null, null, this, node, null);
            node.setLeftChild(right);
            right = null;
            rkey = null;
            rval = null;
            return newParent;
        }
    }

    /**
     * Helper method which inserts a key-value pair into the root
     * of the specified sub-tree
     * 
     * @param root
     *            -- root of specified subtree
     * @param k
     *            -- key to insert
     * @param val
     *            -- value to insert
     * @return
     */
    public TTNode<Key, Value> insertHelp(Key k, Value val) {

        TTNode<Key, Value> retval;

        if (this.isLeaf()) { // At leaf node: insert here
            return this.add(new TTNode<Key, Value>(k, val,
                    null, null, null, null, null));
        }
        // Add to internal node
        if (k.compareTo(lkey) < 0) { // Insert left

            retval = left.insertHelp(k, val);

            if (retval == left)
                return this;
            else
                return this.add(retval);
        }
        else if (right == null || k.compareTo(rkey) < 0) {

            retval = center.insertHelp(k, val);

            if (retval == center)
                return this;
            else
                return this.add(retval);
        }
        else { // Insert right
            retval = right.insertHelp(k, val);
            if (retval == right)
                return this;
            else
                return this.add(retval);
        }

    } // end inserthelp

    public Value removeHelper(Key k) {

        // value to return
        Value toRet = null;

        if (lkey.compareTo(k) == 0) {
            // current node has desired key in the left
            toRet = lval;

            if (this.isLeaf()) {
                this.setLeft(rkey, rval);
                this.setRight(null, null);
            }
            else {
                TTNode<Key, Value> successor = this.center;
                while (!successor.isLeaf()) {
                    successor = successor.leftChild();
                }

                Key tempKey = lkey;

                lkey = successor.lkey();
                lval = successor.lval();

                successor.setLeft(tempKey, toRet);
                toRet = this.center.removeHelper(k);
            }
        }
        else if (rkey.compareTo(k) == 0) {
            // current node has desired key in right
            toRet = rval;

            if (this.isLeaf()) {
                this.setRight(null, null);
            }
            else {
                TTNode<Key, Value> successor = this.right;
                while (!successor.isLeaf()) {
                    successor = successor.leftChild();
                }

                Key tempKey = lkey;

                lkey = successor.lkey();
                lval = successor.lval();

                successor.setLeft(tempKey, toRet);
                toRet = this.right.removeHelper(k);
            }
        }
        else {
            // desired key is not in this node
            if (this.isLeaf()) {
                // node is not in SubTree
                return null;
            }
            else if (k.compareTo(lkey) < 0) {
                toRet = this.left.removeHelper(k);
            }
            else if (rkey == null || k.compareTo(lkey) > 0
                    && k.compareTo(rkey) < 0) {
                toRet = this.center.removeHelper(k);
            }
            else {
                toRet = this.right.removeHelper(k);
            }
        }

        this.rebalanceChildren();

        return toRet;
    }

    private void rebalanceChildren() {

        // First case is that the empty node has a sibling with
        // two keys. We can simply borrow a key from the sibling
        // with two keys
        boolean leftIsFull = left != null && left.lkey() != null
                && left.rkey() != null;
        boolean centerIsFull =
                center != null && center.lkey() != null
                        && center.rkey() != null;
        boolean rightIsFull = right != null
                && right.lkey() != null && right.rkey() != null;

        // Second case is that parent has two keys and two
        // children with only one key (singletons). We borrow
        // a key from the parent with the help of siblings
        boolean leftCenterSingletons = rkey != null
                && left != null && center != null
                && left.lkey() != null && center.lkey() != null;
        boolean leftRightSingletons = left != null
                && right != null
                && left.lkey() != null && right.lkey() != null;
        boolean centerRightSingletons = center != null
                && right != null && center.lkey() != null
                && right.lkey() != null;

        // The last case is that the empty node has only one 
        // singleton sibling. We merge the sibling and the parent
        // and push the empty node up one level to either be solved
        // recursively or eventually find its way into the root.
        boolean leftSingletonOnly = left != null
                && center != null && left.lval() != null
                && center.lval() == null;
        boolean centerSingletonOnly = left != null
                && center != null && left.lval() == null
                && center.lval() != null;

        // Sift down cases. Case 1 takes first priority with 
        // case 3 as last priority since it does not actually 
        // rebalance the full tree.
        if (leftIsFull || centerIsFull || rightIsFull) {
            this.balanceSiblingWithTwoKeys();
        }
        else if (leftCenterSingletons || leftRightSingletons
                || centerRightSingletons) {
            this.balanceTwoSingletonSiblings();
        }
        else if (leftSingletonOnly || centerSingletonOnly) {
            this.balanceOneSingletonSibling();
        }
        else {
            // TODO: SubTree is balances
        }
    }

    private void balanceSiblingWithTwoKeys() {

        boolean leftToCenter = center != null && left != null
                && center.lkey() == null
                && left.lkey() != null && left.rkey() != null;
        boolean centerToLeft = left != null && center != null
                && left.lkey() == null && center.lkey() != null
                && center.rkey() != null;
        boolean rightToCenter = right != null && center != null
                && center.lkey() == null
                && right.lkey() != null & right.rkey() != null;
        boolean centerToRight = right != null && center != null
                && right.lkey() == null
                && center.lkey() != null & center.rkey() != null;
        boolean rightToLeft = left != null && center != null
                && right != null && left.lkey() == null
                && right.lkey() != null && right.rkey() != null;
        boolean leftToRight = left != null && center != null
                && right != null && right.lkey() == null
                && left.lkey() != null && left.rkey() != null;

        if (leftToCenter) {
            this.rotateClockwise(false);
        }
        else if (centerToLeft) {
            this.rotateCounterClockwise(true);
        }
        else if (rightToCenter) {
            this.rotateCounterClockwise(false);
        }
        else if (centerToRight) {
            this.rotateClockwise(true);
        }
        else if (rightToLeft) {
            this.rotateCounterClockwise(true);
            this.balanceSiblingWithTwoKeys();
        }
        else if (leftToRight) {
            this.rotateClockwise(true);
            this.balanceSiblingWithTwoKeys();
        }
        else {
            // TODO: SubTree is balanced
        }
    }

    private void balanceTwoSingletonSiblings() {

        boolean middleUnbalanced = left != null && center != null
                && right != null && right.lval() != null
                && left.lval() != null && center.lval() == null;
        boolean rightUnbalanced = left != null && center != null
                && right != null && left.lval() != null
                && center.lval() != null && right.lval() == null;
        boolean leftUnbalanced = left != null && right != null
                && center != null && center.lval() != null
                && right.lval() != null && left.lval() == null;

        if (middleUnbalanced) {
            left.setRight(lkey, lval);
            this.setLeft(rkey, rval);
            this.setRight(null, null);

            left.setRightChild(center.leftChild());
            center = right;
            right = null;
        }
        else if (rightUnbalanced) {
            this.rotateClockwise(true);
            this.balanceTwoSingletonSiblings();
        }
        else if (leftUnbalanced) {
            this.rotateCounterClockwise(true);
            this.balanceTwoSingletonSiblings();
        }
        else {
            // TODO: SubTree is balanced
        }
    }

    private void balanceOneSingletonSibling() {
        boolean centerUnbalanced = left != null && center != null
                && left.lval() != null && center.lval() == null;
        boolean leftUnbalanced = left != null && center != null
                && left.lval() == null && center.lval() != null;

        if (centerUnbalanced) {
            left.setRight(lkey, lval);
            this.setLeft(null, null);

            left.setRightChild(center.leftChild());
            this.setCenterChild(null);
        }
        else if (leftUnbalanced) {
            center.setRight(center.lkey(), center.lval());
            center.setLeft(lkey, lval);
            this.setLeft(null, null);

            center.setRightChild(center.centerChild());
            center.setCenterChild(center.leftChild());
            center.setLeftChild(left.leftChild());
            this.setLeftChild(center);
            this.setCenterChild(null);
        }
        else {
            // TODO: SubTree is balanced
        }
    }

    private void rotateCounterClockwise(boolean aboutCenter) {
        if (aboutCenter) {
            // update key-value pairs
            left.setLeft(lkey, lval);
            this.setLeft(center.lkey(), center.lval());
            center.setLeft(center.rkey(), center.rval());
            center.setRight(null, null);

            // update children references
            left.setCenterChild(center.leftChild());
            center.setLeftChild(center.centerChild());
            center.setCenterChild(center.rightChild());
            center.setRightChild(null);
        }
        else {
            // update key-value pairs
            center.setLeft(rkey, rval);
            this.setRight(right.lkey(), right.lval());
            right.setLeft(right.rkey(), right.rval());
            right.setRight(null, null);

            // update children references
            center.setCenterChild(right.leftChild());
            right.setLeftChild(right.centerChild());
            right.setCenterChild(right.rightChild());
            right.setRightChild(null);
        }
    }

    private void rotateClockwise(boolean aboutCenter) {

        if (aboutCenter) {

            right.setLeft(rkey, rval);

            // now must take into account size of center child
            if (center.rkey != null) {
                // center child has 2 keys
                this.setRight(center.rkey(), center.rval());
                center.setRight(null, null);

                right.setCenterChild(right.leftChild());
                right.setLeftChild(center.rightChild());
                center.setRightChild(null);
            }
            else {
                // center child only has 1 key
                this.setRight(center.lkey(), center.lval());
                center.setLeft(null, null);

                right.setCenterChild(right.leftChild());
                right.setLeftChild(center.centerChild());
                center.setCenterChild(null);
            }
        }
        else {
            // update key-value pairs
            center.setLeft(this.lkey(), this.lval());
            this.setLeft(left.rkey(), left.rval());
            left.setRight(null, null);

            // update children references
            center.setCenterChild(center.leftChild());
            center.setLeftChild(left.rightChild());
            left.setRightChild(null);
        }
    }

    public int height() {
        if (this.isLeaf()) {
            return 1;
        }
        if (center == null) {
            return 1 + left.height();
        }
        else if (right == null) {
            return 1 + Math.max(left.height(), center.height());
        }
        else {
            int maxLC = Math.max(left.height(), center.height());
            return Math.max(maxLC, right.height()) + 1;
        }
    }

    public void printDepthFirstHelper(int height,
            boolean linebreak) {
        int whitespace = (int) Math.pow(2, height);
        for (int i = 0; i < whitespace; i++) {
            System.out.print(" ");
        }
        System.out.printf("[%s, %s]", lkey, rkey);
        for (int i = 0; i < whitespace; i++) {
            System.out.print(" ");
        }
        if (linebreak) {
            System.out.println();
        }

        // recursive call to (potentially) all three children
        if (this.isLeaf()) {
            return;
        }
        else if (center == null) {
            left.printDepthFirstHelper(height - 1,
                    true);
        }
        else if (right == null) {
            left.printDepthFirstHelper(height - 1, false);
            center.printDepthFirstHelper(height - 1, true);
        }
        else {
            left.printDepthFirstHelper(height - 1, false);
            center.printDepthFirstHelper(height - 1, false);
            right.printDepthFirstHelper(height - 1, true);
        }
    }

}