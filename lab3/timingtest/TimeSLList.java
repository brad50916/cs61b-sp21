package timingtest;
//import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stopwatch;
//import org.checkerframework.checker.units.qual.A;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opcounts = new AList<>();
        int base = 1000;
        for(int i=0;i<8;i++){
            Ns.addLast(base);
            SLList<Integer> S = new SLList<>();
            int j=0;
            while(j<base){
                S.addLast(j++);
            }
            int m = 10000;
            int k = 0;
            opcounts.addLast(m);
            Stopwatch sw = new Stopwatch();
            while(k++<m) {
                S.getLast();
            }
            double timeInSeconds = sw.elapsedTime();
            times.addLast(timeInSeconds);
            base*=2;
        }
        printTimingTable(Ns,times,opcounts);

    }

}
