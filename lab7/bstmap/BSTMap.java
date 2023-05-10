package bstmap;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class BSTMap<K extends Comparable<K>,V> implements Map61B<K,V> {
    BSTNode root;
    public BSTMap () {
        root = null;
    }
    private class BSTNode {
        private K key;
        private V value;
        private BSTNode left;
        private BSTNode right;
        public BSTNode(K key, V value, BSTNode left, BSTNode right) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
        }
        public BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        root = putRecursive(key, value, root);
    };
    private BSTNode putRecursive(K key, V value, BSTNode cur) {
        if (cur == null) {
            return new BSTNode(key, value, null, null);
        }
        if (key.compareTo(cur.key) < 0) {
            cur.left = putRecursive(key, value, cur.left);
        } else if (key.compareTo(cur.key) > 0) {
            cur.right = putRecursive(key, value, cur.right);
        }
        return cur;
    }

    /** Removes all of the mappings from this map. */
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    };

    /* Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        throw new UnsupportedOperationException();
    };

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        throw new UnsupportedOperationException();
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        throw new UnsupportedOperationException();
    };

    /* Returns a Set view of the keys contained in this map. Not required for Lab 7.
     * If you don't implement this, throw an UnsupportedOperationException. */
    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    };

    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 7. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    };

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 7. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    };

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    public void printInOrder() {
        printInorderHelp(root);
    }

    private void printInorderHelp(BSTNode node) {
        if (node == null) return;
        System.out.println(node.value);
        printInorderHelp(node.left);
        printInorderHelp(node.right);
    }
}
