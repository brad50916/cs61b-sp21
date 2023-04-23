package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {
    private int size;
    private Node s1;
    private Node s2;
    public LinkedListDeque() {
        size = 0;
        s1 = new Node(null, null, null);
        s2 = new Node(null, null, null);
    }
    private class Node {
        private T value;
        private Node next;
        private Node prev;
        Node(T i, Node n, Node p) {
            value = i;
            next = n;
            prev = p;
        }
    }
    @Override
    public void addFirst(T item) {
        if (size == 0) {
            Node temp = new Node(item, s2, s1);
            s1.next = temp;
            s2.prev = temp;
        } else {
            Node temp = new Node(item, s1.next, s1);
            s1.next.prev = temp;
            s1.next = temp;
        }
        size++;
    }
    @Override
    public void addLast(T item) {
        if (size == 0) {
            Node temp = new Node(item, s2, s1);
            s1.next = temp;
            s2.prev = temp;
        } else {
            Node temp = new Node(item, s2, s2.prev);
            s2.prev.next = temp;
            s2.prev = temp;
        }
        size++;
    }
    @Override
    public int size() {
        return size;
    }
    @Override
    public void printDeque() {
        Node temp = s1.next;
        while (temp != s2) {
            System.out.print(temp.value + " ");
            temp = temp.next;
        }
        System.out.print("\n");
    }
    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T temp = s1.next.value;
        if (size == 1) {
            s1.next = null;
            s2.prev = null;
        } else {
            s1.next.next.prev = s1;
            s1.next = s1.next.next;
        }
        size--;
        return temp;
    }
    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T temp = s2.prev.value;
        if (size == 1) {
            s1.next = null;
            s2.prev = null;
        } else {
            s2.prev.prev.next = s2;
            s2.prev = s2.prev.prev;
        }
        size--;
        return temp;
    }
    @Override
    public T get(int index) {
        if (size == 0) {
            return null;
        }
        Node temp = s1;
        while (index-- >= 0) {
            if (temp == s2) {
                return null;
            }
            temp = temp.next;
        }
        return temp.value;
    }

    public T getRecursive(int index) {
        if (size == 0) {
            return null;
        }
        return getRecursiveHelper(index, s1.next);
    }

    private T getRecursiveHelper(int index, Node s) {
        if (s == s2) {
            return null;
        }
        if (index == 0) {
            return s.value;
        }
        return getRecursiveHelper(--index, s.next);
    }

    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private int wizPos;
        LinkedListDequeIterator() {
            wizPos = 0;
        }
        public boolean hasNext() {
            return wizPos < size;
        }
        public T next() {
            T returnItem = get(wizPos);
            wizPos += 1;
            return returnItem;
        }
    }

//    @Override
//    public boolean equals(Object o) {
//        if (o == null) {
//            return false;
//        }
//        if (this == o) {
//            return true;
//        }
//        if (this.getClass().getInterfaces()[1] != o.getClass().getInterfaces()[1]) {
//            return false;
//        }
//        Deque<T> oas = (Deque<T>) o;
//        for (int i = 0; i < this.size; i++) {
//            if (this.get(i) != oas.get(i)) {
//                return false;
//            }
//        }
//        return true;
//    }
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (o instanceof Deque oas) {
            if (this.size != oas.size()) {
                return false;
            }
            for (int i = 0; i < this.size; i++) {
                if(this.get(i) != oas.get(i)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
