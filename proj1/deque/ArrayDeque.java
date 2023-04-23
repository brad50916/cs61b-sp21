package deque;

import java.util.Iterator;
public class ArrayDeque<T> implements Iterable<T>, Deque<T> {
    private T[] items;
    private int size;
    private int nextfirst;
    private int nextlast;
    private int SIZE = 8;
    private static final int MINSIZE = 16;
    public ArrayDeque() {
        items = (T[]) new Object[SIZE];
        nextfirst = 4;
        nextlast = 5;
        size = 0;
    }
//    public ArrayDeque(int newSize) {
//        items = (T[]) new Object[newSize];
//        SIZE = newSize;
//        nextfirst = 1;
//        nextlast = 2;
//        size = 0;
//    }
    private void multipySize() {
        nextfirst = (nextfirst + 1) % SIZE;
        nextlast = (nextlast - 1 < 0) ? SIZE - 1 : nextlast - 1;
        T[] itemsNew = (T[]) new Object[SIZE * 2];
        int i = 0;
        while (nextfirst != nextlast) {
            itemsNew[i++] = items[nextfirst];
            nextfirst = (nextfirst + 1) % SIZE;
        }
        itemsNew[i] = items[nextfirst];
        items = itemsNew;
        SIZE *= 2;
        nextfirst = SIZE - 1;
        nextlast = size;
    }

    private void divideSize() {
        nextfirst = (nextfirst + 1) % SIZE;
        nextlast = (nextlast - 1 < 0) ? SIZE - 1 : nextlast - 1;
        T[] itemsNew = (T[]) new Object[SIZE / 2];
        int i = 0;
        while (nextfirst != nextlast) {
            itemsNew[i++] = items[nextfirst];
            nextfirst = (nextfirst + 1) % SIZE;
        }
        itemsNew[i] = items[nextfirst];
        items = itemsNew;
        SIZE /= 2;
        nextfirst = SIZE - 1;
        nextlast = size;
    }
    @Override
    public void addFirst(T item) {
        if (size == SIZE) {
            multipySize();
        }
        items[nextfirst--] = item;
        if (nextfirst < 0) {
            nextfirst = SIZE - 1;
        }
        size++;
    }
    @Override
    public void addLast(T item) {
        if (size == SIZE) {
            multipySize();
        }
        items[nextlast++] = item;
        if (nextlast >= SIZE) {
            nextlast = 0;
        }
        size++;
    }
    @Override
    public int size() {
        return size;
    }

    private int arraySize() {
        return SIZE;
    }
    @Override
    public void printDeque() {
        int cur = (nextfirst + 1) % SIZE;
        while (cur != nextlast) {
            System.out.print(items[cur] + " ");
            cur = (cur + 1) % SIZE;
        }
        System.out.print("\n");
    }
    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        double percent = (double) (size - 1) / SIZE;
        if (SIZE >= MINSIZE &&  percent < 0.25) {
            divideSize();
        }
        int temp = (nextfirst + 1) % SIZE;
        T removeItem = items[temp];
        items[temp] = null;
        nextfirst = temp;
        size--;
        return removeItem;
    }
    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        double percent = (double) (size - 1) / SIZE;
        if (SIZE >= MINSIZE && percent < 0.25) {
            divideSize();
        }
        int temp = (nextlast - 1 < 0) ? SIZE - 1 : nextlast - 1;
        T removeItem = items[temp];
        items[temp] = null;
        nextlast = temp;
        size--;
        return removeItem;
    }
    @Override
    public T get(int index) {
        int cur = (nextfirst + 1) % SIZE;
        while (index-- > 0) {
            cur = (cur + 1) % SIZE;
        }
        return items[cur];
    }

    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int wizPos;
        ArrayDequeIterator() {
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
//        if (this.size != oas.size()) {
//            return false;
//        }
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
