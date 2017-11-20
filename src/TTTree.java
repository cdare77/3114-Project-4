
public class TTTree<Key extends Comparable<? super Key>, Value> {

    private TTNode<Key, Value> root;

    public TTTree() {
        root = null;
    }

    public void insert(Key k, Value val) {
        if (root == null) {
            // Empty tree: create a leaf node for
            // root
            root = new TTNode<Key, Value>(k, val, null, null,
                    null, null, null);
        }
        else {
            root = root.insertHelp(k, val);
        }
    }
    
    public Value remove(Key k) {
        Value toRet = root.removeHelper(k);
        if (root.lkey() == null) {
            root = root.leftChild();
        }
        return toRet;
    }

    public void printTree() {
        int treeHeight = root.height();
        root.printDepthFirstHelper(treeHeight, true);
    }
}
