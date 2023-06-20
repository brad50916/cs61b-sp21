package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 60;
    private static final Random RANDOM = new Random();
    private static class Position {
        private int x;
        private int y;
        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public void update(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }
    private static void addHexagon(int size, TETile[][] world, Position p, TETile patern) {
        int length = size + 2 * (size - 1);
        int count = 0;
        for (int j = p.y + size - 1; j >= p.y; j -= 1) {
            for (int i = p.x + count; i < p.x + length - count; i += 1) {
                world[i][j] = patern;
                world[i][j + 2 * count + 1] = patern;
//                Random r = new Random();
//                world[i][j] = TETile.colorVariant(Tileset.FLOWER, 200, 200, 200, r);
//                world[i][j + 2 * count + 1] = TETile.colorVariant(Tileset.FLOWER, 200, 200, 200, r);
            }
            count++;
        }
    }
    private static Position getBottomNeighbor(Position p, int size) {
        Position p1 = new Position(p.x, p.y - size * 2);
        return p1;
    }
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(11);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.AVATAR;
            case 3: return Tileset.FLOOR;
            case 4: return Tileset.GRASS;
            case 5: return Tileset.WATER;
            case 6: return Tileset.LOCKED_DOOR;
            case 7: return Tileset.UNLOCKED_DOOR;
            case 8: return Tileset.SAND;
            case 9: return Tileset.MOUNTAIN;
            case 10: return Tileset.TREE;
            default: return Tileset.NOTHING;
        }
    }
    private static void addHexColumn(int size, TETile[][] world, Position p, int nums) {
        if (nums < 1) return;

        addHexagon(size, world, p, randomTile());

        if (nums > 1) {
            Position bottomNeighbor = getBottomNeighbor(p, size);
            addHexColumn(size, world, bottomNeighbor, nums - 1);
        }
    }
    private static void drawWorld(int size, TETile[][] world, int hexSize) {
        Position p = new Position(0, 35);
        for (int i = 0; i < hexSize; i++) {
            p.update(p.x + size * 2 - 1, p.y + size);
            addHexColumn(size, world, p, hexSize + i);
        }
        for (int j = 0; j < hexSize - 1; j++) {
            p.update(p.x + size * 2 - 1, p.y - size);
            addHexColumn(size, world, p, hexSize * 2 - 2 - j);
        }

    }
    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        // draw hexagon
        drawWorld(4, world, 3);

        // draws the world to the screen
        ter.renderFrame(world);
    }
}
