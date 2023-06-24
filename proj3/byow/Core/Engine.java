package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

public class Engine {
    TERenderer ter = new TERenderer();
    List<Room> allRoom = new ArrayList<>();
    QuickFind disjointSet;
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    public static final int ROOMLENGTHMAX = 10;
    public static final int ROOMLENGTHMIN = 3;
    public static final int NUMBEROFROOM = 20;

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
        // Draw room floor
        drawroomFloor(finalWorldFrame, Tileset.FLOOR);

        disjointSet = new QuickFind(allRoom);

//        for (int i = 0; i < allRoom.size(); i++) {
//            generateHallway(finalWorldFrame, allRoom.get(i));
//        }
//        generateDoor(finalWorldFrame, allRoom.get(0));
        for (int i = 0; i < 5; i++) {
            generateHallway(finalWorldFrame, allRoom.get(i));
        }

        // Render the world
        ter.renderFrame(finalWorldFrame);
        return finalWorldFrame;
    }
    private void generateHallway(TETile[][] world, Room start) {
        Room end = getclosetRoom(start);
        Position closetPosition = getclosetPosition(start, end);
        HashSet<Position> path = gethallwayPath(closetPosition, start, end);
        for (Position p : path) {
            drawCorner(world, Tileset.FLOOR, p);
        }
    }
    private HashSet<Position> gethallwayPath(Position start, Room startRoom, Room endRoom) {
        HashSet<Position> path = new HashSet<>();
        Position[] list = new Position[4];
        list[0] = new Position(start.getX() + 1, start.getY());
        list[1] = new Position(start.getX(), start.getY() + 1);
        list[2] = new Position(start.getX() - 1, start.getY());
        list[3] = new Position(start.getX(), start.getY() - 1);
        Position second = null;
        Position first = null;
        for (int i = 0; i < 4; i++) {
            if (checkOverlapHelp(list[i], startRoom) == false) {
                second = list[i];
                if (i == 0) first = new Position(second.getX() + 1, second.getY());
                else if (i == 1) first = new Position(second.getX(), second.getY() + 1);
                else if (i == 2) first = new Position(second.getX() - 1, second.getY());
                else if (i == 3) first = new Position(second.getX(), second.getY() - 1);
            }
        }
        path.add(start);
        path.add(second);
        while (checkPositionoverlap(first) == false) {
            path.add(first);
            Position[] list1 = getDirection(first, endRoom);
            for (int i = 0; i < 4; i++) {
                if (checkOverlapHelp(list1[i], startRoom) || path.contains(list1[i])) {
                    continue;
                } else {
                    first = list1[i];
                    break;
                }
            }
        }
        path.add(first);
        return path;
    }
    private Position[] getDirection(Position start, Room end) {
        Position[] list = new Position[4];
        list[0] = new Position(start.getX() + 1, start.getY());
        list[1] = new Position(start.getX(), start.getY() + 1);
        list[2] = new Position(start.getX() - 1, start.getY());
        list[3] = new Position(start.getX(), start.getY() - 1);

        int endX = end.getBottomLeft().getX() + end.getWidth() / 2;
        int endY = end.getBottomLeft().getY() + end.getHeight() / 2;
        Arrays.sort(list, Comparator.comparingDouble(p -> Math.pow(p.getX() - endX, 2)
                + Math.pow(p.getY() - endY, 2)));
        return list;
    }
    private Room getclosetRoom(Room room) {
        Room closetRoom = null;
        double min = 1000;
        for (Room r : allRoom) {
            int x = r.getBottomLeft().getX() + r.getWidth() / 2;
            int y = r.getBottomLeft().getY() + r.getHeight() / 2;
            int x1 = room.getBottomLeft().getX() + room.getWidth() / 2;
            int y1 = room.getBottomLeft().getY() + room.getHeight() / 2;
            if (r == room) continue;
            double dist = Math.sqrt(Math.pow(x1 - x, 2)
                    + Math.pow(y1 - y, 2));
            if (dist < min) {
                min = dist;
                closetRoom = r;
            }
        }
        return closetRoom;
    }

    /***
     * Get closet position on start room to end room
     * @param start the start room
     * @param end the target room
     * @return
     */
    private Position getclosetPosition(Room start, Room end) {
        List<Position> allPosition = getallPosition(start);
        double min = 10000;
        Position closetPosition = null;
        for (Position p : allPosition) {
            int x = end.getBottomLeft().getX() + end.getWidth() / 2;
            int y = end.getBottomLeft().getY() + end.getHeight() / 2;
            double dist = Math.pow(p.getX() - x, 2)
                    + Math.pow(p.getY() - y, 2);
            if (dist < min) {
                min = dist;
                closetPosition = p;
            }
        }
        return closetPosition;
    }

    private List<Position> getallPosition(Room r) {
        List<Position> allPosition = new ArrayList<>();
        for (int i = r.getBottomLeft().getX(); i <= r.getTopRight().getX(); i++) {
            allPosition.add(new Position(i, r.getBottomLeft().getY()));
            allPosition.add(new Position(i, r.getTopRight().getY()));
        }
        for (int j = r.getBottomLeft().getY(); j <= r.getTopRight().getY(); j++) {
            allPosition.add(new Position(r.getBottomLeft().getX(), j));
            allPosition.add(new Position(r.getTopRight().getX(), j));
        }
        return allPosition;
    }

    /***
     * Generate door on the room's wall
     * @param world the 2d array
     * @param room the room wants to create door
     * @return the door position
     */
    private Position generateDoor(TETile[][] world, Room room) {
        int width = RandomUtils.uniform(RANDOM, 0, room.getWidth());
        int height = RandomUtils.uniform(RANDOM, 0, room.getHeight());
        int direction = RandomUtils.uniform(RANDOM, 4);
        int x, y;
        if (direction == 0) {
            x = room.getBottomLeft().getX() + width;
            y = room.getBottomLeft().getY();
        } else if (direction == 1) {
            x = room.getTopRight().getX();
            y = room.getBottomLeft().getY() + height;
        } else if (direction == 2) {
            x = room.getBottomLeft().getX() + width;
            y = room.getTopRight().getY();
        } else {
            x = room.getBottomLeft().getX();
            y = room.getBottomLeft().getY() + height;
        }

        Position randomPosition = new Position(x, y);
        drawCorner(world, Tileset.FLOOR, randomPosition);
        return randomPosition;
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

    /***
     * Draw room floor
     * @param world The TETile 2D array
     * @param type The type of floor
     */
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

    private boolean checkPositionoverlap(Position p) {
        for (Room r : allRoom) {
            if(checkOverlapHelp(p, r)) return true;
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
        Room r = new Room(p, p1, width, height);
        return r;
    }
}
