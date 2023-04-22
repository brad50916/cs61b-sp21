package deque;

import org.junit.Test;

import java.util.Comparator;


public class MaxArrayDequeTest {
    public class Dog {
        private class NameComparator implements Comparator<Integer> {
            public int compare(Integer a, Integer b) {
                return a.compareTo(b);
            }
        }
        public Comparator<Integer> getNameComparator() {
            return new NameComparator();
        }
    }
    @Test
    public void addIsEmptySizeTest() {
        Dog t = new Dog();
        MaxArrayDeque<Integer> lld1 = new MaxArrayDeque<>(t.getNameComparator());
        lld1.addFirst(3);
        lld1.addFirst(2);
        lld1.addFirst(1);
        System.out.println(lld1.max());
    }
}
