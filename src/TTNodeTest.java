import student.TestCase;

/**
 * Tests the functionality of TTNodes methods, including the
 * private helper methods and all of their respective cases
 * 
 * @author Chris Dare (cdare77@vt.edu)
 * @version 12/4/2017
 */
public class TTNodeTest extends TestCase {

    private TTNode<Integer> root;
    
    @Override
    public void setUp() {
        root = new TTNode<Integer>(15, 25, null, null, null);
    }
    
    /**
     * tests the accessor methods for children and keys
     */
    public void testAccessor() {
        assertEquals((int) root.lkey(), 15);
        assertEquals((int) root.rkey(), 25);
        
        assertNull(root.leftChild());
        assertNull(root.rightChild());
        assertNull(root.centerChild());
    }
    
    /**
     * Tests the modifier methods for children and keys
     */
    public void testModifier() {
        TTNode<Integer> flyweightChild = new TTNode<Integer>();
        
        root.setLeftKey(16);
        assertEquals((int) root.lkey(), 16);
        
        root.setRightKey(17);
        assertEquals((int) root.rkey(), 17);
        
        root.setLeftChild(flyweightChild);
        root.setCenterChild(flyweightChild);
        root.setRightChild(flyweightChild);
        
        assertEquals(root.leftChild(), flyweightChild);
        assertEquals(root.rightChild(), flyweightChild);
        assertEquals(root.centerChild(), flyweightChild);
    }
    
    /**
     * Tests the functionality of toString in the case of an
     * empty node and nonempty node
     */
    public void testToString() {
        assertEquals("15 25", root.toString());
        TTNode<Integer> empty = new TTNode<Integer>();
        assertEquals("null null", empty.toString());
    }
    
    /**
     * Specifically aims at testing the rebalancing of the tree
     */
    public void testRemove() {
        root = root.insertHelp(20);
        root = root.insertHelp(30);
        root = root.insertHelp(27);
        root = root.insertHelp(26);

        assertEquals(30, (int) root.removeHelper(30));        
        assertEquals(27, (int) root.removeHelper(27));
        assertEquals(15, (int) root.removeHelper(15));
        assertEquals(20, (int) root.removeHelper(20));
        
        root = root.leftChild();
        root.setRightKey(30);
        root = root.insertHelp(27);
        
        assertEquals(30, (int) root.removeHelper(30));
        
        root = root.leftChild();     
    }
}
