/** Class that prints the Collatz sequence starting from a given number.
 *  @author YOUR NAME HERE
 */
public class Collatz {

    /** This method generates number from n. If n is even, it returns n/2. If n is odd, it returns 3n+1 */
    /** public static int nextNumber(int n) {
        if (n % 2 == 1) {
            return 3 * n + 1;
        } else {
            return n / 2;
        }
    } */

	/** Buggy implementation of nextNumber! */
    public static int nextNumber(int n) {
        if (n  == 128) {
            return 1;
        } else if (n == 5) {
            return 3 * n + 1;
        } else {
            return n * 2;
        }
    }

    public static void main(String[] args) {
        int n = 5;
        System.out.print(n + " ");
        while (n != 1) {
            n = nextNumber(n);
            System.out.print(n + " ");
        }
        System.out.println();
    }
}

