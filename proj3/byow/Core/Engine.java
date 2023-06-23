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
    public static final int ROOMLENGTHMIN = 2;
    public static final int NUMBEROFROOM = 50;

    private static final Random RANDOM = new Random();
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

        // Initialize the world
        ter.initialize(WIDTH, HEIGHT);

        // Initialize the 2D array
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        initialize(finalWorldFrame);

        // Generate rooms
        while (allRoom.size() < NUMBEROFROOM) {
            generateRoom();
        }

        // Draw room on final world frame
        drawRoom(finalWorldFrame, Tileset.WALL);

        drawroomFloor(finalWorldFrame, Tileset.FLOOR);
        // Render the world
        ter.renderFrame(finalWorldFrame);
        return finalWorldFrame;
    }

    /***
     * Initialize the given TETile 2D array with Tileset.NOTHING.
     * @param finalWorldFrame The TETile 2D array
     */
    private static void initialize(TETile[][] finalWorldFrame) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                finalWorldFrame[x][y] = Tileset.NOTHING;
            }
        }
    }
    private void drawroomFloor(TETile[][] world, TETile type) {
        for (Room r : allRoom) {
           for (int i = r.getBottomLeft().getX() + 1; i < r.getTopRight().getX(); i++) {
               for (int j = r.getBottomLeft().getY() + 1; j < r.getTopRight().getY(); j++) {
                   world[i][j] = type;
               }
           }
        }
    }
    /***
     * Draw vertical line.
     * @param world the TETile 2D array
     * @param type the type of TETile
     * @param start the start Position
     * @param end the end Position
     */
    private static void drawVerticalHallway(TETile[][] world, TETile type, Position start, Position end) {
        for (int i = end.getY() - 1; i > start.getY(); i--) {
            world[start.getX()][i] = type;
        }
    }
    /***
     * Draw horizontal line.
     * @param world the TETile 2D array
     * @param type the type of TETile
     * @param start the start Position
     * @param end the end Position
     */
    private static void drawHorizontalHallway(TETile[][] world, TETile type, Position start, Position end) {
        for (int i = end.getX() - 1; i > start.getX(); i--) {
            world[i][start.getY()] = type;
        }
    }

    /***
     * Draw corner
     * @param world the TETile 2D array
     * @param type the type of TETile
     * @param corner the corner Position
     */
    private static void drawCorner(TETile[][] world, TETile type, Position corner) {
        world[corner.getX()][corner.getY()] = type;
    }

    /***
     * Draw rooms on given TETile 2D array.
     * @param world The 2D array.
     * @param type The type of wall
     */
    private void drawRoom(TETile[][] world, TETile type) {
        for (Room r : allRoom) {
            Position x1 = r.getBottomLeft();
            Position y2 = r.getTopRight();
            Position x2 = new Position(y2.getX(), x1.getY());
            Position y1 = new Position(x1.getX(), y2.getY());
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

    /***
     * Generate rooms randomly.
     */
    private void generateRoom() {
        Position p = randomPoint();
        int width = randomLength();
        int height = randomLength();
        Room newRoom = createRoom(p, width, height);
        if (checkOverlap(newRoom) == false) {
            allRoom.add(newRoom);
        }
    }

    /***
     * Check if new Room have overlap with previous created room.
     * @param newRoom the new created room
     * @return {@code true} if have overlap
     * {@code false} if don't have overlap
     */
    private boolean checkOverlap(Room newRoom) {
        Position bl = newRoom.getBottomLeft();
        Position tr = newRoom.getTopRight();
        for (Room r : allRoom) {
            for (int i = bl.getX(); i < tr.getX(); i++) {
                Position p1 = new Position(i, bl.getY());
                Position p2 = new Position(i, tr.getY());
                if (checkOverlapHelp(p1, r) || checkOverlapHelp(p2, r)) {
                    return true;
                }
            }
            for (int j = bl.getY(); j < tr.getY(); j++) {
                Position p1 = new Position(bl.getX(), j);
                Position p2 = new Position(tr.getX(), j);
                if (checkOverlapHelp(p1, r) || checkOverlapHelp(p2, r)) {
                    return true;
                }
            }
        }
        return false;
    }

    /***
     * Check if the position have overlap with given room.
     * @param p the position which wants to check
     * @param r the given room r
     * @return {@code true} if have overlap
     * {@code false} if don't have overlap
     */
    private boolean checkOverlapHelp(Position p, Room r) {
        Position bl = r.getBottomLeft();
        Position tr = r.getTopRight();
        if (bl.getX() <= p.getX() && p.getX() <= tr.getX()
        && bl.getY() <= p.getY() && p.getY() <= tr.getY()) {
            return true;
        }
        return false;
    }

    /***
     * Generate a random Position
     * @return the random Position
     */
    private Position randomPoint() {
        int x = RandomUtils.uniform(RANDOM, 0, WIDTH - ROOMLENGTHMAX);
        int y = RandomUtils.uniform(RANDOM, 0, HEIGHT - ROOMLENGTHMAX);
        Position p = new Position(x, y);
        return p;
    }

    /***
     * Generate random number depends on ROOM LENGTH MIN and ROOM LENGTH MAX
     * @return the random number
     */
    private int randomLength() {
        return RandomUtils.uniform(RANDOM, ROOMLENGTHMIN, ROOMLENGTHMAX);
    }

    /***
     * Create new room
     * @param p the left-bottom position of the room
     * @param width the room's width
     * @param height the room's height
     * @return the new room
     */
    private Room createRoom(Position p, int width, int height) {
        Position p1 = new Position(p.getX() + width, p.getY() + height);
        Room r = new Room(p, p1);
        return r;
    }

    /***
     * The room object is used to represent a single room in the world.
     *
     * All room objects must have two position objects, one for bottom-left
     * position and another for top-right position.
     */
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

    /***
     * The Position object is used to represent a single position in the world.
     *
     * All Position objects have x-axis and y-axis values.
     */
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
