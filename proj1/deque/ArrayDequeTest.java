package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class ArrayDequeTest {
    @Test
    public void randomizedTest(){
        LinkedListDeque<Integer> L = new LinkedListDeque<>();
        ArrayDeque<Integer> B = new ArrayDeque<>();

        int N = 10000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 7);
            if (operationNumber == 0) {
                // addFirst
                int randVal = StdRandom.uniform(0, 100);
                L.addFirst(randVal);
                B.addFirst(randVal);
            } else if (operationNumber == 1) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
            } else if (operationNumber == 2) {
                // isEmpty
                assertEquals("isEmpty should be equal", L.isEmpty(), B.isEmpty());
            } else if (operationNumber == 3) {
                // size
                assertEquals("size should be equal", L.size(), B.size());
            } else if (operationNumber == 4) {
                // removeFirst
                assertEquals("removefirst should be equal", L.removeFirst(), B.removeFirst());
            } else if (operationNumber == 5) {
                // removeLast
                assertEquals("removeLast should be equal", L.removeLast(), B.removeLast());
            } else if (operationNumber == 6 && L.size() != 0){
                // get
                int randVal = StdRandom.uniform(0, L.size());
                assertEquals("get should be equal", L.get(randVal), B.get(randVal));
            }
        }
    }
    @Test
    public void addIsEmptySizeTest() {

        ArrayDeque<String> lld1 = new ArrayDeque<String>();
        assertEquals(0, lld1.size());
        assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
        lld1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

        lld1.addLast("middle");
        assertEquals(2, lld1.size());

        lld1.addLast("back");
        assertEquals(3, lld1.size());

//        System.out.println("Printing out deque: ");
//        lld1.printDeque();
//
//        System.out.println("Printing index 0");
//        System.out.println(lld1.get(0));
    }

    @Test
    public void equalTest() {

        ArrayDeque<String> lld1 = new ArrayDeque<String>();
        lld1.addFirst("front");
        lld1.addLast("middle");
        lld1.addLast("back");

        ArrayDeque<String> lld2 = new ArrayDeque<String>();
        lld2.addFirst("front");
        lld2.addLast("middle");
        lld2.addLast("back");

        ArrayDeque<String> lld3 = new ArrayDeque<String>();
        lld3.addLast("back");
        lld3.addLast("middle");
        lld3.addFirst("front");

        ArrayDeque<String> lld4 = new ArrayDeque<String>();
        lld4.addFirst("front");
        lld4.addLast("middle");
        lld4.addLast("middle");

        assertTrue("lld1 should be equal to lld2", lld1.equals(lld2));

        assertFalse("lld1 should not be equal to lld3", lld1.equals(lld3));

        assertFalse("lld1 should not be equal to lld4", lld1.equals(lld4));
    }
    @Test
    public void iteratorTest() {

        ArrayDeque<Integer> lld1 = new ArrayDeque<Integer>();

        lld1.addFirst(1);
        lld1.addFirst(2);
        lld1.addFirst(3);
        lld1.addLast(4);

//        Iterator<String> aseer = lld1.iterator();
//        while(aseer.hasNext()) {
//            String i = aseer.next();
//            System.out.println(i);
//        }
//        for(Integer s : lld1) {
//            System.out.println(s);
//        }
    }
    @Test
    public void resizeTest() {
        ArrayDeque<Integer> lld1 = new ArrayDeque<Integer>();
        for(int i = 0; i < 64; i++) {
            lld1.addFirst(i);
            lld1.addLast(i);
        }
        for(int i = 0; i < 96; i++) {
            lld1.removeFirst();
        }
        assertEquals("array size should be 128", lld1.array_size(), 128);
        lld1.removeLast();
        assertEquals("array size should be 128", lld1.array_size(), 64);
        for(int i = 0; i < 98; i++) {
            lld1.addFirst(i);
        }
        for(int i = 0; i < 65; i++) {
            lld1.removeLast();
        }
        assertEquals("array size should be 128", lld1.array_size(), 256);
        lld1.removeFirst();
        assertEquals("array size should be 128", lld1.array_size(), 128);
//        for(Integer s : lld1) {
//            System.out.println(s);
//        }
    }
    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

        ArrayDeque<Integer> lld1 = new ArrayDeque<Integer>();
        // should be empty
        assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

        lld1.addFirst(10);
        // should not be empty
        assertFalse("lld1 should contain 1 item", lld1.isEmpty());

        lld1.removeFirst();
        // should be empty
        assertTrue("lld1 should be empty after removal", lld1.isEmpty());

    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);

    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {


        LinkedListDeque<String>  lld1 = new LinkedListDeque<String>();
        LinkedListDeque<Double>  lld2 = new LinkedListDeque<Double>();
        LinkedListDeque<Boolean> lld3 = new LinkedListDeque<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();

    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());


    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }


    }
}
