import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


/**
 * This class implements SkipList data structure and contains an inner SkipNode
 * class which the SkipList will make an array of to store data.
 * 
 * @author CS Staff
 * 
 * @version 2021-08-23
 * @param <K>
 *            Key
 * @param <V>
 *            Value
 */
public class SkipList<K extends Comparable<? super K>, V>
    implements Iterable<KVPair<K, V>> {
    private SkipNode head; // First element of the top level
    private int size; // number of entries in the Skip List
    private int level;
    /**
     * Initializes the fields head, size and level
     */
    public SkipList() {
        head = new SkipNode(null, 0);
        size = 0;
        level = -1;
    }


    /**
     * Returns a random level number which is used as the depth of the SkipNode
     * 
     * @return a random level number
     */
    int randomLevel() {
        int lev;
        Random value = new Random();
        for (lev = 0; Math.abs(value.nextInt()) % 2 == 0; lev++) {
            // Do nothing
        }
        return lev; // returns a random level
    }


    /**
     * Searches for the KVPair using the key which is a Comparable object.
     * 
     * @param key
     *            key to be searched for
     */
    public ArrayList<KVPair<K, V>> search(K key) {
        SkipNode x = head; // Dummy header node
        for (int i = level; i >= 0; i--) { // For each level...
            while ((x.forward[i] != null) && (x.forward[i].pair.getKey()
                .compareTo(key) < 0)) { // go forward
                x = x.forward[i]; // Go one last step
            }
        }
        x = x.forward[0]; // Move to actual record, if it exists
        ArrayList<KVPair<K, V>> arr = new ArrayList<KVPair<K, V>>();
        while ((x != null) && (x.pair.getKey().compareTo(key) == 0)) {
            arr.add(x.pair);
            x = x.forward[0];
        } // Got it
        return arr; // Resulting array of KVPair
    }


    /**
     * @return the size of the SkipList
     */
    public int size() {
        return size;
    }


    /**
     * Inserts the KVPair in the SkipList at its appropriate spot as designated
     * by its lexicoragraphical order.
     * 
     * @param it
     *            the KVPair to be inserted
     */
    @SuppressWarnings("unchecked")
    public void insert(KVPair<K, V> it) {	
    	int newLevel = randomLevel(); // New node's level
        if (newLevel > head.level) { // If new node is deeper
          adjustHead(newLevel); // adjust the header
        }
        
        // Track end of level
        SkipNode[] update = (SkipNode[])Array.newInstance(SkipList.SkipNode.class,
                newLevel + 1);
        SkipNode a = head; // Start at header node
        for (int i = newLevel; i >= 0; i--) { // Find insert position
          while ((a.forward[i] != null) && (a.forward[i].element().getKey().compareTo(it.getKey()) < 0))
            a = a.forward[i];
          update[i] = a; // Track end at level i
        }
    	 
        a = new SkipNode(it, newLevel);
        for (int i = 0; i <= newLevel; i++) { // Splice into list
          a.forward[i] = update[i].forward[i]; // Who a points to
          update[i].forward[i] = a; // Who points to a
        }
        size++; // Increment dictionary size
    }


    /**
     * Increases the number of levels in head so that no element has more
     * indices than the head.
     * 
     * @param newLevel
     *            the number of levels to be added to head
     */
    @SuppressWarnings("unchecked")
    private void adjustHead(int newLevel) {
    	SkipNode temp = head;
    head = new SkipNode(null, newLevel);
    for (int i = 0; i <= level; i++) {
      head.forward[i] = temp.forward[i];
    }
    head.level = newLevel;
    }


    /**
     * Removes the KVPair that is passed in as a parameter and returns true if
     * the pair was valid and false if not.
     * 
     * @param pair
     *            the KVPair to be removed
     * @return returns the removed pair if the pair was valid and null if not
     */

    @SuppressWarnings("unchecked")
    public KVPair<K, V> remove(K key) {
    	SkipNode[] update = (SkipNode[])Array.newInstance(SkipList.SkipNode.class, head.level + 1);
        boolean removed = false;
        SkipNode current = head;
        SkipNode a = head;
        // access the SkipList starting from the head node at the highest level
        for (int i = head.level; i >= 0; i--) {
            // Move down the levels until the target key is found or a larger key is encountered
            while (current.forward[i] != null && current.forward[i].pair.getKey().compareTo(key) < 0) {
                current = current.forward[i];
                update[i] = current;
            }
            // If the target key is found,
            if (current.forward[i] != null && current.forward[i].pair.getKey().compareTo(key) == 0) {
                removed = true;
                a = current.forward[i];
                for (int j = 0; j <= level; j++) { // Splice into list
                    update[j].forward[j] = a.forward[j]; // What a points to
                }
                size--;
                break;
            }
        }
        // Return null if the target key is not found
        if (!removed) {
            return null;
        }
        else {
            KVPair<K, V> myPair = new KVPair<>(key, a.pair.getValue());
            return myPair;
        }

    }


    /**
     * Removes a KVPair with the specified value.
     * 
     * @param val
     *            the value of the KVPair to be removed
     * @return returns true if the removal was successful
     */
    public KVPair<K, V> removeByValue(V val) {
    	// Search for the value to remove
        SkipNode[] update = (SkipNode[])Array.newInstance(SkipList.SkipNode.class, head.level + 1);
        boolean removed = false;
        SkipNode current = head;
        SkipNode a = head;

        // access the SkipList starting from the head node at the highest level
        for (int i = head.level; i >= 0; i--) {
            // Move down the levels until the target key is found or a larger key is encountered
            while (current.forward[i] != null && current.forward[i].pair.getValue().equals(val) == false) {
                current = current.forward[i];
                update[i] = current;
            }
            // If the target key is found,
            if (current.forward[i] != null && current.forward[i].pair.getValue().equals(val)) {
                removed = true;
                a = current.forward[i];
                for (int j = 0; j <= level; j++) { // Splice into list
                    update[j].forward[j] = a.forward[j]; // what a points to
                }
                size--;
                break;
            }
        }
        // Return null if the target key is not found
        if (!removed) {
            return null;
        }
        else {
            KVPair<K, V> myPair = new KVPair<>(a.pair.getKey(), a.pair.getValue());
            return myPair;
        }
    }


    /**
     * Prints out the SkipList in a human readable format to the console.
     */
    public void dump() {
        System.out.println("SkipList dump:");
        System.out.println("Node has depth " + this.head.forward.length
            + ", Value (null)");
        SkipNode start = head;
        while (start.forward[0] != null) {
            start = start.forward[0];
            System.out.println("Node has depth " + start.forward.length
                + ", Value " + start.pair.toString());
        }
        System.out.println("SkipList size is: " + this.size);
    }

    /**
     * This class implements a SkipNode for the SkipList data structure.
     * 
     * @author CS Staff
     * 
     * @version 2016-01-30
     */
    private class SkipNode {

        // the KVPair to hold
        private KVPair<K, V> pair;
        // what is this
        private SkipNode [] forward;
        // the number of levels
        private int level;

        /**
         * Initializes the fields with the required KVPair and the number of
         * levels from the random level method in the SkipList.
         * 
         * @param tempPair
         *            the KVPair to be inserted
         * @param level
         *            the number of levels that the SkipNode should have
         */
        @SuppressWarnings("unchecked")
        public SkipNode(KVPair<K, V> tempPair, int level) {
            pair = tempPair;
            forward = (SkipNode[])Array.newInstance(SkipList.SkipNode.class,
                level + 1);
            this.level = level;
        }


        /**
         * Returns the KVPair stored in the SkipList.
         * 
         * @return the KVPair
         */
        public KVPair<K, V> element() {
            return pair;
        }

    }


    private class SkipListIterator implements Iterator<KVPair<K, V>> {
        private SkipNode current;

        public SkipListIterator() {
            current = head;
        }


        @Override
        public boolean hasNext() {
            // TODO Auto-generated method stub
            return false;
        }


        @Override
        public KVPair<K, V> next() {
            // TODO Auto-generated method stub

            return null;
        }

    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // TODO Auto-generated method stub
        return new SkipListIterator();
    }

}
