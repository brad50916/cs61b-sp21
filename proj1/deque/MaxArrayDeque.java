package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> originalCom;
    public MaxArrayDeque(Comparator<T> c) {
        originalCom = c;
    }
    public T max() {
        if (size() == 0) {
            return null;
        }
        T max = get(0);
        for (int i = 1; i < size(); i++) {
            if (originalCom.compare(get(i), max) > 0) {
                max = get(i);
            }
        }
        return max;
    }
    public T max(Comparator<T> c) {
        if (size() == 0) {
            return null;
        }
        T max = get(0);
        for (int i = 1; i < size(); i++) {
            if (c.compare(get(i), max) > 0) {
                max = get(i);
            }
        }
        return max;
    }
}
