import student.TestCase;

public class TTTreeTest extends TestCase {
    
    private TTTree<Integer, Integer> tree;
    
    @Override
    public void setUp() {
        tree = new TTTree<Integer, Integer>();
    }
    
    public void testInsert() {
        tree.insert(10, 10);
        tree.insert(20, 10);
        tree.insert(15, 10);
        tree.insert(16, 16);
        tree.insert(17, 1);
        tree.insert(5, 1);
        tree.insert(25, 1);
        tree.insert(16, 16);

        
        tree.printTree();
    }
}
