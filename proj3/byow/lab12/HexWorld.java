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
    private static void addHexagon(int s, TETile[][] world, int x, int y) {
        int length = s + 2 * (s - 1);
        int count = 0;
        for (int j = y + s - 1; j >= y; j -= 1) {
            for (int i = x + count; i < x + length - count; i += 1) {
                world[i][j] = Tileset.FLOWER;
                world[i][j + 2 * count + 1] = Tileset.FLOWER;
            }
            count++;
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
        addHexagon(5, world, 20, 20);

        // draws the world to the screen
        ter.renderFrame(world);
    }
}
