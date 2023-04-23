package gh2;
import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

public class GuitarHero {
    static String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
    public static final double CONCERT_A = 440.0;
    public static final double CONCERT_C = CONCERT_A * Math.pow(2, 3.0 / 12.0);
    public static final int TOTALPITCH = 37;

    public static void main(String[] args) {
        /* create two guitar strings, for concert A and C */
        GuitarString stringA = new GuitarString(CONCERT_A);
        GuitarString[] string = new GuitarString[TOTALPITCH];
        int index = -1;
        for (int i = 0; i < TOTALPITCH; i++) {
            double currentConcert = CONCERT_A * Math.pow(2, (i - 24) / 12.0);
            string[i] = new GuitarString(currentConcert);
        }
        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                index = keyboard.indexOf(key);

                if (index == -1) {
                    System.out.println("Out of range");
                    continue;
                }
                string[index].pluck();
            }
            if (index != -1) {
                /* compute the superposition of samples */
                double sample = string[index].sample();

                /* play the sample on standard audio */
                StdAudio.play(sample);

                /* advance the simulation of each guitar string by one step */
                string[index].tic();
            }
        }
    }
}
