package byow.Core;

import byow.TileEngine.TETile;

import java.util.Random;

public class testMain {
    private static final Random RANDOM = new Random(0);
    public static void main(String[] args) {
        if (args.length > 1) {
            System.out.println("Can only have one argument - the flag and input string");
            System.exit(0);
        } else if (args.length == 1) {
            Engine engine = new Engine();
            TETile[][] t = engine.interactWithInputString(args[0]);

//            System.out.println(t);
//            System.out.println(args[0]);
        }
    }
}
