package deque;

public class ArrayDeque<Item> {
    private Item[] items;
    private int size;
    private int nextfirst;
    private int nextlast;
    public ArrayDeque() {
        items = (Item[]) new Object[8];
        nextfirst = 4;
        nextlast = 5;
        size = 0;
    }

//    public void addFirst(Item item) {
//
//    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}
