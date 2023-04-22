package deque;

import org.junit.Test;

import java.util.Comparator;


public class MaxArrayDequeTest {

    public class GreaterComparator implements Comparator<Integer> {
        public int compare(Integer a, Integer b) {
            return a.compareTo(b);
        }
    }
    public Comparator<Integer> getGreaterComparator() {
        return new GreaterComparator();
    }

    public class smallerComparator implements Comparator<Integer> {
        public int compare(Integer a, Integer b) {
            return -a.compareTo(b);
        }
    }
    public Comparator<Integer> getsmallerComparator() {
        return new smallerComparator();
    }

    @Test
    public void comparatorTest() {
        MaxArrayDeque<Integer> lld1 = new MaxArrayDeque<>(getGreaterComparator());
        lld1.addFirst(3);
        lld1.addFirst(2);
        lld1.addFirst(1);
        lld1.addLast(10);
        lld1.addLast(0);
        lld1.addLast(20);
        System.out.println(lld1.max());
        System.out.println(lld1.max(getsmallerComparator()));
    }
}
