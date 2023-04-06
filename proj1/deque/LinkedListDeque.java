package deque;

public class LinkedListDeque<Item> {
    private int size;
    private Node s1;
    private Node s2;
    public LinkedListDeque() {
        size = 0;
        s1 = new Node(null,null,null);
        s2 = new Node(null,null,null);
    }
    public class Node {
        public Item value;
        public Node next;
        public Node prev;
        public Node(Item i, Node n, Node p) {
            value = i;
            next = n;
            prev = p;
        }
    }

    public void addFirst(Item item) {
        if(size == 0) {
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

    public void addLast(Item item) {
        if(size == 0) {
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

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        Node temp = s1.next;
        while(temp != s2) {
            System.out.print(temp.value + " ");
            temp = temp.next;
        }
        System.out.print("\n");
    }

    public Item removeFirst() {
        if(size == 0) return null;
        Item temp = s1.next.value;
        if(size == 1) {
            s1.next = null;
            s2.prev = null;
        }else {
            s1.next.next.prev = s1;
            s1.next = s1.next.next;
        }
        size--;
        return temp;
    }

    public Item removeLast() {
        if(size == 0) return null;
        Item temp = s2.prev.value;
        if(size == 1) {
            s1.next = null;
            s2.prev = null;
        }else {
            s2.prev.prev.next = s2;
            s2.prev = s2.prev.prev;
        }
        size--;
        return temp;
    }

    public Item get(int index) {
        Node temp = s1;
        while(index-- >= 0) {
            if(temp == s2) return null;
            temp = temp.next;
        }
        return temp.value;
    }
}
