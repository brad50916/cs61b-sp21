package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Engine {
    TERenderer ter = new TERenderer();
    List<Room> allRoom = new ArrayList<>();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    public static final int ROOMLENGTHMAX = 10;
    public static final int ROOMLENGTHMIN = 3;
    private static final Random RANDOM = new Random(0);
    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        initialize(finalWorldFrame);
        generateRoom();
        drawRoom(finalWorldFrame);

        ter.renderFrame(finalWorldFrame);
        return finalWorldFrame;
    }
    private static void initialize(TETile[][] finalWorldFrame) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                finalWorldFrame[x][y] = Tileset.NOTHING;
            }
        }
    }
    private static void drawVerticalHallway(TETile[][] world, TETile type, Position start, Position end) {
        for (int i = end.getY() - 1; i > start.getY(); i--) {
            world[start.getX()][i] = type;
        }
    }

    private static void drawHorizontalHallway(TETile[][] world, TETile type, Position start, Position end) {
        for (int i = end.getX() - 1; i > start.getX(); i--) {
            world[i][start.getY()] = type;
        }
    }

    private static void drawCorner(TETile[][] world, TETile type, Position corner) {
        world[corner.getX()][corner.getY()] = type;
    }
    private void drawRoom(TETile[][] world) {
        for (Room r : allRoom) {
            Position x1 = r.getBottomLeft();
            Position y2 = r.getTopRight();
            Position x2 = new Position(y2.getX(), x1.getY());
            Position y1 = new Position(x1.getX(), y2.getY());
            TETile type = Tileset.WALL;
            drawCorner(world, type, x1);
            drawCorner(world, type, x2);
            drawCorner(world, type, y1);
            drawCorner(world, type, y2);
            drawHorizontalHallway(world, type, x1, x2);
            drawHorizontalHallway(world, type, y1, y2);
            drawVerticalHallway(world, type, x1, y1);
            drawVerticalHallway(world, type, x2, y2);
        }
    }
    private void generateRoom() {
        Position p = randomPoint();
        int width = randomLength();
        int height = randomLength();
        allRoom.add(createRoom(p, width, height));
    }
    private Position randomPoint() {
        int x = RandomUtils.uniform(RANDOM, 0, WIDTH - ROOMLENGTHMAX);
        int y = RandomUtils.uniform(RANDOM, 0, HEIGHT - ROOMLENGTHMAX);
        Position p = new Position(x, y);
        return p;
    }

    private int randomLength() {
        return RandomUtils.uniform(RANDOM, ROOMLENGTHMIN, ROOMLENGTHMAX);
    }

    private Room createRoom(Position p, int width, int height) {
        Position p1 = new Position(p.getX() + width, p.getY() + height);
        Room r = new Room(p, p1);
        return r;
    }

    private class Room {
        Position bottomLeft;
        Position topRight;
        Room(Position a, Position b) {
            bottomLeft = a;
            topRight = b;
        }
        public Position getBottomLeft() {
            return bottomLeft;
        }
        public Position getTopRight() {
            return topRight;
        }
    }
    private static boolean overLap(Room a, Room b) {
        return false;
    }

    private class Position {
        private int x;
        private int y;
        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public int getX () {
            return x;
        }
        public int getY () {
            return y;
        }
    }
}
