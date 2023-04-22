package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    Comparator<T> original_com;
    public MaxArrayDeque(Comparator<T> c) {
        original_com = c;
    }
    public T max() {
        if(size() == 0) {
            return null;
        }
        T max = get(0);
        for(int i = 1; i < size(); i++) {
            if(original_com.compare(get(i), max) > 0) {
                max = get(i);
            }
        }
        return max;
    }
}
