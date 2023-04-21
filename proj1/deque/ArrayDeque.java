package deque;

import java.util.Iterator;
public class ArrayDeque<T> implements Iterable<T> {
    private T[] items;
    private int size;
    private int nextfirst;
    private int nextlast;
    private int SIZE = 8;
    public ArrayDeque() {
        items = (T[]) new Object[SIZE];
        nextfirst = 4;
        nextlast = 5;
        size = 0;
    }

    public void addFirst(T item) {
        items[nextfirst--] = item;
        if(nextfirst < 0){
            nextfirst = SIZE - 1;
        }
        size++;
    }

    public void addLast(T item) {
        items[nextlast++] = item;
        if(nextlast >= SIZE){
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
        int cur = (nextfirst + 1) % SIZE;
        while(cur != nextlast) {
            System.out.print(items[cur] + " ");
            cur = (cur + 1) % SIZE;
        }
        System.out.print("\n");
    }

    public T removeFirst() {
        if(size == 0) return null;
        int temp = (nextfirst + 1) % SIZE;
        T remove_item = items[temp];
        items[temp] = null;
        nextfirst = temp;
        size--;
        return remove_item;
    }

    public T removeLast() {
        if(size == 0) return null;
        int temp = (nextlast - 1 < 0) ? SIZE - 1 : nextlast - 1;
        T remove_item = items[temp];
        items[temp] = null;
        nextlast = temp;
        size--;
        return remove_item;
    }

    public T get(int index) {
        int cur = (nextfirst + 1) % SIZE;
        while(index-- > 0) {
            cur = (cur + 1) % SIZE;
        }
        return items[cur];
    }

    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int wizPos;
        public ArrayDequeIterator() {
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

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(this == o) return true;
        if(o instanceof ArrayDeque oas) {
            if(this.size != oas.size){
                return false;
            }
            Iterator<T> aseer1 = this.iterator();
            Iterator<T> aseer2 = oas.iterator();
            while(aseer1.hasNext()) {
                T i = aseer1.next();
                T y = aseer2.next();
                if(i != y) return false;
            }
            return true;
        }
        return false;
    }
}
