package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;
//import static org.junit.Assert.assertEquals;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
  @Test
    public void testThreeAddThreeRemove(){
      AListNoResizing<Integer> A = new AListNoResizing<>();
      BuggyAList<Integer> B = new BuggyAList<>();
      int t=3;
      for(int i=0;i<t;i++){
          A.addLast(i);
          B.addLast(i);
      }
      assertEquals(A.size(),B.size());
      for(int j=0;j<t;j++){
          assertEquals(A.removeLast(),B.removeLast());
      }
    }

    @Test
    public void randomizedTest(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                int size1 = B.size();
                assertEquals(size,size1);
            } else if (operationNumber == 2) {
                // get last
                if(L.size()==0) continue;
                int last = L.getLast();
                int last1 = B.getLast();
                assertEquals(last,last1);
            } else if (operationNumber == 3) {
                // remove last
                if(L.size()==0) continue;
                int r = L.removeLast();
                int r1 = B.removeLast();
                assertEquals(r,r1);
            }
        }
    }
}
