package deque;

public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int nextfirst;
    private int nextlast;
    private int array_size = 8;
    public ArrayDeque() {
        items = (T[]) new Object[array_size];
        nextfirst = 4;
        nextlast = 5;
        size = 0;
    }

    public void addFirst(T item) {
        items[nextfirst--] = item;
        if(nextfirst < 0){
            nextfirst = array_size - 1;
        }
        size++;
    }

    public void addLast(T item) {
        items[nextlast++] = item;
        if(nextlast >= array_size){
            nextlast = 0;
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
        int cur = (nextfirst + 1) % array_size;
        while(cur != nextlast) {
            System.out.print(items[cur] + " ");
            cur = (cur + 1) % array_size;
        }
        System.out.print("\n");
    }

    public T removeFirst() {
        if(size == 0) return null;
        int temp = (nextfirst + 1) % array_size;
        T remove_item = items[temp];
        items[temp] = null;
        nextfirst = temp;
        size--;
        return remove_item;
    }

    public T removeLast() {
        if(size == 0) return null;
        int temp = (nextlast - 1 < 0) ? array_size - 1 : nextlast - 1;
        T remove_item = items[temp];
        items[temp] = null;
        nextlast = temp;
        size--;
        return remove_item;
    }

    public T get(int index) {
        int cur = (nextfirst + 1) % array_size;
        while(index-- > 0) {
            cur = (cur + 1) % array_size;
        }
        return items[cur];
    }
}
