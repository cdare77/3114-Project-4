import java.util.List;

/**
 * Implementation of a 2-3+ node which supports typical
 * insertion, deletion, search, range search, etc. Most of its
 * methods are extended to the 2-3+ tree so that we can
 * holistically operate on a collection of nodes with a fixed
 * starting point (which will be our root). However, minimal
 * implementation will be needed for our tree since we define all
 * our operations to work on subtrees, and the entire 2-3+ tree
 * is considered a subtree of the root.
 * 
 * @author Chris Dare (cdare77@vt.edu)
 * @version 11/18/2017
 * @param <Key>
 *            -- generic reference for comparable
 * @param <Value>
 *            -- generic for data stored
 */
public class TTNode<Key extends Comparable<? super Key>,
        Value extends Comparable<? super Value>> {

    // -------------------PRIVATE VARIABLES------------------
    private Value lval; // The left record
    private Key lkey; // The node's left key
    private Value rval; // The right record
    private Key rkey; // The node's right key
    private TTNode<Key, Value> left; // Pointer to left child
    private TTNode<Key, Value> center; // Pointer to middle child
    private TTNode<Key, Value> right; // Pointer to right child

    // -------------------CONSTRUCTOR------------------

    /**
     * Default constructor. Pointers are set to null
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

    // -------------------PUBLIC METHODS------------------

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

    /**
     * Accessor method for left key
     * 
     * @return reference for left key
     */
    public Key lkey() {
        return lkey;
    } // Left key

    /**
     * Accessor method fro left value
     * 
     * @return reference for left value
     */
    public Value lval() {
        return lval;
    } // Left value

    /**
     * Accessor method for right key
     * 
     * @return reference for right key
     */
    public Key rkey() {
        return rkey;
    } // Right key

    /**
     * Accessor method for right value
     * 
     * @return reference to right value
     */
    public Value rval() {
        return rval;
    } // Right value

    /**
     * Modifier method for left KV-pair
     * 
     * @param k
     *            -- new left key
     * @param val
     *            -- new left value
     */
    public void setLeft(Key k, Value val) {
        lkey = k;
        lval = val;
    }

    /**
     * Modifier method for right KV-pair
     * 
     * @param k
     *            -- new right key
     * @param val
     *            -- new right value
     */
    public void setRight(Key k, Value val) {
        rkey = k;
        rval = val;
    }

    /**
     * Modifier method for this.left
     * 
     * @param it
     *            -- new left child
     */
    public void setLeftChild(TTNode<Key, Value> it) {
        left = it;
    }

    /**
     * Modifier method for this.center
     * 
     * @param it
     *            -- new center child
     */
    public void setCenterChild(TTNode<Key, Value> it) {
        center = it;
    }

    /**
     * Modifier method for this.right
     * 
     * @param it
     *            -- new right child
     */
    public void setRightChild(TTNode<Key, Value> it) {
        right = it;
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
        if (compareKVPair(k, val, lkey, lval) <= 0) { // Insert
                                                      // left

            retval = left.insertHelp(k, val);

            if (retval == left)
                return this;
            else
                return this.add(retval);
        }
        else if (right == null
                || compareKVPair(k, val, rkey, rval) <= 0) {

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

    /**
     * Recursive helper method which performs a standard tree
     * removal procedure given a key k to remove. As in most
     * trees, we simply swap this node with the next greatest
     * element in one of the leaves so that the removal problem
     * can be simplified to only removing from the leaves.
     * 
     * @param k
     *            -- key to remove
     * @return -- value of KV pair removed
     */
    public Value removeHelper(Key k) {

        // value to return
        Value toRet = null;

        if (lkey.compareTo(k) == 0) {
            // current node has desired key in the left
            toRet = lval;

            if (this.isLeaf()) {
                // key to remove is in leaf.. merely rotate
                // elements
                // downwards
                this.setLeft(rkey, rval);
                this.setRight(null, null);
            }
            else {
                // find the next greatest leaf in the tree
                // and swap in order to move problem to
                // leaf
                TTNode<Key, Value> successor = this.center;
                while (!successor.isLeaf()) {
                    successor = successor.leftChild();
                }

                // swap key-value pair in current node
                // and successor
                Key tempKey = lkey;

                lkey = successor.lkey();
                lval = successor.lval();

                successor.setLeft(tempKey, toRet);
                toRet = this.center.removeHelper(k);
            }
        }
        else if (rkey != null && rkey.compareTo(k) == 0) {
            // current node has desired key in right
            toRet = rval;

            if (this.isLeaf()) {
                // key to remove is in right node of leaf..
                // simply remove
                this.setRight(null, null);
            }
            else {
                // find the next greatest leaf in the tree
                // and swap in order to move problem to
                // leaf
                TTNode<Key, Value> successor = this.right;
                while (!successor.isLeaf()) {
                    successor = successor.leftChild();
                }

                // swap key-value pair in current node
                // and successor
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

        // we removed a key from one of the leaves or
        // children, so we must handle the imbalance up
        // the recursion stack
        this.rebalanceChildren();

        return toRet;
    } // end removeHelper

    /**
     * Finds the hight of our given subtree by recursively
     * summing the hights of its children and finding the maximum
     * 
     * @return integer representing the distance from the root +
     *         1 (since the root is considered as height 1).
     */
    public int height() {

        if (this.isLeaf()) {
            // the height of a leaf is defined as 1
            return 1;
        }
        else {
            // since this the 2-3+ tree is a complete
            // tree, we only need to traverse the
            // left node, as any other leaf nodes
            // will be on the same level.
            return 1 + left.height();
        }
    } // end height()

    public void rangeSearchHelper(Key lower, Key higher,
            List<Value> list) {
        if (this.isLeaf()) {
            // we cant recursively traverse the tree any
            // more, so we simply check if we can add the
            // values stored in the node
            if (lower.compareTo(lkey) <= 0
                    && higher.compareTo(lkey) >= 0) {
                list.add(lval);
            }
            if (rkey != null && lower.compareTo(rkey) <= 0
                    && higher.compareTo(rkey) >= 0) {
                list.add(rval);
            }
        }
        else if (lower.compareTo(lkey) <= 0) {
            // we will have to traverse left child regardless of
            // upper bound (higher)
            left.rangeSearchHelper(lower, higher, list);
            if (higher.compareTo(lkey) >= 0) {
                // lkey falls in range [lower, higher]
                // (inclusive)
                list.add(lval);
                center.rangeSearchHelper(lower, higher, list);
                if (rkey != null
                        && higher.compareTo(rkey) >= 0) {
                    // [lkey, rkey] is contained in [lower,
                    // higher]
                    list.add(rval);
                    right.rangeSearchHelper(lower, higher, list);
                }
            } // end outer if
        } // end else if
        else if (rkey == null || lower.compareTo(rkey) <= 0) {
            // lower bound (lower) is strictly greater than lkey
            // and but there could still be elements in
            // [lower, higher]
            center.rangeSearchHelper(lower, higher, list);
            if (rkey != null
                    && higher.compareTo(rkey) >= 0) {
                // rkey falls within range
                list.add(rval);
                right.rangeSearchHelper(lower, higher, list);
            }
        } // end else if
        else {
            // there is a right child, and rkey is strictly
            // less than lower
            right.rangeSearchHelper(lower, higher, list);
        } // end else
    } // end findRangeHelper

    /**
     * Uses a StringBuilder to compose a string representation of
     * the current subtree in a depth-first visualisation (the
     * way most people view trees with root on top)
     * 
     * @return string representation of subtree
     */
    public String printDepthFirst() {
        StringBuilder builder = new StringBuilder();
        int h = this.height();

        // print each level of the tree in order
        for (int i = 1; i <= h; i++) {
            this.printGivenLevel(builder, i);
            builder.append("\n");
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", lkey, rkey);
    }

    // -------------------PRIVATE METHODS------------------------

    /**
     * Add a new key/value pair to the node. There might be a
     * subtree associated with the record being added. This
     * information comes in the form of a 2-3 tree node with one
     * key and a (possibly null) subtree through the center
     * pointer field.
     * 
     * @param node
     *            -- reference of node to add to current node
     * @return reference to new root with node in subtree.
     *         Subtree is no deeper than 3 nodes
     */
    private TTNode<Key, Value> add(TTNode<Key, Value> node) {
        if (rkey == null) { // Only one key, add here
            if (compareKVPair(lkey, lval, node.lkey(),
                    node.lval()) < 0) {
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
        } // end else
          // node to add's left key is smaller
        else if (compareKVPair(lkey, lval, node.lkey(),
                node.lval()) >= 0) {

            // create new node with node to add as left child and
            // this node as center child
            TTNode<Key, Value> newParent =
                    new TTNode<Key, Value>(lkey,
                            lval, null, null, node, this, null);

            // rotate pointers leftwards
            // node.setLeftChild(left);
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
        else if (compareKVPair(rkey, rval, node.lkey(),
                node.lval()) >= 0) { // Add
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
    } // end add

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
        // and push the empty node up one level to either be
        // solved
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

        // Tree is balanced. There is nothing to do.
    } // end rebalanceChildren

    /**
     * Private method which handles case where an unbalaned node
     * is in a child who has a sibling containing 2 nonnull keys.
     * Hence, we can balance the unbalanced node by "stealing" a
     * key from the unbalanced node. By "stealing" we actually
     * rotate key values from child to parent to child in order
     * to preserve key structure, either once or recursively many
     * times.
     */
    private void balanceSiblingWithTwoKeys() {

        // indicates whether unbalanced is in center and
        // two keys are in the left child
        boolean leftToCenter = center != null && left != null
                && center.lkey() == null
                && left.lkey() != null && left.rkey() != null;
        // indicates whether unbalanced is in left and
        // two keys are in the center child
        boolean centerToLeft = left != null && center != null
                && left.lkey() == null && center.lkey() != null
                && center.rkey() != null;
        // indicates whether unbalanced is in center and
        // two keys are in the right child
        boolean rightToCenter = right != null && center != null
                && center.lkey() == null
                && right.lkey() != null & right.rkey() != null;
        // indicates whether unbalanced is in right and
        // two keys are in the center child
        boolean centerToRight = right != null && center != null
                && right.lkey() == null
                && center.lkey() != null & center.rkey() != null;
        // indicates whether unbalanced is in left and
        // two keys are in the right child
        boolean rightToLeft = left != null && center != null
                && right != null && left.lkey() == null
                && right.lkey() != null && right.rkey() != null;
        // indicates whether unbalanced is in right and
        // two keys are in the left child
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
            // we merely move the problem to
            // the center node
            this.rotateCounterClockwise(true);
            this.balanceSiblingWithTwoKeys();
        }
        else if (leftToRight) {
            // we merely move the problem to
            // the center node
            this.rotateClockwise(true);
            this.balanceSiblingWithTwoKeys();
        } // end else if

        // else subtree is balanced and there is nothing
        // to do.

    } // end balanceSiblingWithTwoKeys

    /**
     * Private method which handles case where unbalanced child
     * has two siblings that each have one node. Since there are
     * three children, the properties of the 2-3+ tree tell us
     * that the parent has two children. Thus, there are 4 nodes
     * total and 4 nodes to split it between, so we can "steal"
     * one from the parent. By "steal" we mean rotate where the
     * keys are moved from child to parent to child in fashion
     * that preserves 2-3+ structure.
     */
    private void balanceTwoSingletonSiblings() {

        // indicates situation where unbalanced child is in
        // center
        boolean middleUnbalanced = left != null && center != null
                && right != null && right.lval() != null
                && left.lval() != null && center.lval() == null;
        // indicates situation where unbalanced child is in right
        boolean rightUnbalanced = left != null && center != null
                && right != null && left.lval() != null
                && center.lval() != null && right.lval() == null;
        // indicates situation where unbalanced child is in left
        boolean leftUnbalanced = left != null && right != null
                && center != null && center.lval() != null
                && right.lval() != null && left.lval() == null;

        if (middleUnbalanced) {
            // update key-value pairs
            left.setRight(lkey, lval);
            this.setLeft(rkey, rval);
            this.setRight(null, null);

            // update children
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
        // everything is balanced. There is nothing to do.
    } // end balanceTwoSingletonSibling

    /**
     * Private method which handles case in which the unbalanced
     * child has one sibling with one key. However, since this
     * means that there are 3 nodes with 2 children. Thus, one
     * node is always going to be unbalanced. Since there are not
     * enough nodes here, we rotate the unbalance up to the root
     * to see if the next level can handle the problem (and in
     * the majority of cases it does).
     */
    private void balanceOneSingletonSibling() {
        // indicates situation in which the center child is
        // unbalanced
        boolean centerUnbalanced = left != null && center != null
                && left.lval() != null && center.lval() == null;
        // indicates situation in which the left child is
        // unbalanced
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

        // all the children are balanced and there is nothing to
        // do
    }

    /**
     * Helper method used in certain cases of rebalancing our 2-3
     * tree.
     * 
     * For instance, if our center child has no key values while
     * our right and left children have keys and possibly one of
     * them has two keys, then we would like to "rotate" in a
     * sense the key closest to the center child from the child
     * that has the two keys.
     * 
     * i.e. if we have the setup [10, 20] [3, null] [null, null]
     * [30, 40]
     * 
     * we would like that calling rotateCounterClockwise on [10,
     * 20] will rebalance its children to: [10, 30] [3, null]
     * [20, null] [40, null]
     * 
     * note that the 2-3+ properties still hold by our choice of
     * what integers to swap.
     * 
     * 
     * @param aboutCenter
     *            -- indicates whether we are rotating the center
     *            child or the right child. Note that we cannot
     *            rotate the left child since values are already
     *            as far left as they can go.
     */
    private void rotateCounterClockwise(boolean aboutCenter) {
        if (aboutCenter) {
            // rotate values from center child into left child

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
            // rotate values from right child into center child

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
        } // end else
    } // end rotateRight

    /**
     * Helper method used in certain cases of rebalancing our 2-3
     * tree.
     * 
     * For instance, if our center child has no key values while
     * our right and left children have keys and possibly one of
     * them has two keys, then we would like to "rotate" in a
     * sense the key closest to the center child from the child
     * that has the two keys.
     * 
     * i.e. if we have the setup [10, 20] [3, 5] [null, null]
     * [30, null]
     * 
     * we would like that calling rotateClockwise on [10, 20]
     * will rebalance its children to: [5, 20] [3, null] [10,
     * null] [30, null]
     * 
     * note that the 2-3+ properties still hold by our choice of
     * what integers to swap.
     * 
     * 
     * @param aboutCenter
     *            -- indicates whether we are rotating the center
     *            child or the left child. Note that we cannot
     *            rotate the right child since values are already
     *            as far right as they can go.
     */
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
        } // end else
    } // end rotateClockwise

    private void printGivenLevel(StringBuilder builder,
            int level) {
        if (level == 1) {
            builder.append(this.toString());
            builder.append(" ");
        }
        else if (center == null) {
            left.printGivenLevel(builder, level - 1);
        }
        else if (right == null) {
            left.printGivenLevel(builder, level - 1);
            center.printGivenLevel(builder, level - 1);
        }
        else {
            left.printGivenLevel(builder, level - 1);
            center.printGivenLevel(builder, level - 1);
            right.printGivenLevel(builder, level - 1);
        }
    } // end printGivenLevel

    /**
     * Private helper method used to build 2-3+ tree whenever two
     * keys have equal values. We compare the values in order to
     * break the tie so that values with equal keys are printed
     * in proper order. In cases that keys are nonidentical, this
     * is the same as calling
     * 
     * thisKey.compareTo(otherKey);
     * 
     * @param thisKey
     *            -- key of first KVPair. Equivalent of caller of
     *            compareTo
     * @param thisVal
     *            -- value of first KVPair
     * @param otherKey
     *            -- key of second KVPair. Equivalent of argument
     *            in compareTo
     * @param otherVal
     *            -- value of second KVPair
     * @return integer analogous to thisKey - otherKey (in
     *         circumstance of integers)
     */
    private int compareKVPair(Key thisKey, Value thisVal,
            Key otherKey, Value otherVal) {
        int compKeys = thisKey.compareTo(otherKey);
        if (compKeys == 0) {
            return thisVal.compareTo(otherVal);
        }
        return compKeys;
    } // end compareKVPair
} // end TTNode