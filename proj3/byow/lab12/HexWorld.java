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
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);
    private static void addHexagon(int s, TETile[][] world, int x, int y, TETile patern) {
        int length = s + 2 * (s - 1);
        int count = 0;
        for (int j = y + s - 1; j >= y; j -= 1) {
            for (int i = x + count; i < x + length - count; i += 1) {
                world[i][j] = patern;
                world[i][j + 2 * count + 1] = patern;
//                Random r = new Random();
//                world[i][j] = TETile.colorVariant(Tileset.FLOWER, 200, 200, 200, r);
//                world[i][j + 2 * count + 1] = TETile.colorVariant(Tileset.FLOWER, 200, 200, 200, r);
            }
            count++;
        }
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
        addHexagon(5, world, 20, 20, randomTile());

        // draws the world to the screen
        ter.renderFrame(world);
    }
}
