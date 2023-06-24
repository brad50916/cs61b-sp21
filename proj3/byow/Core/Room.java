package byow.Core;
/***
 * The room object is used to represent a single room in the world.
 *
 * All room objects must have two position objects, one for bottom-left
 * position and another for top-right position.
 */
public class Room {
    private Position bottomLeft;
    private Position topRight;
    private int width;
    private int height;
    Room(Position a, Position b, int width, int height) {
        bottomLeft = a;
        topRight = b;
        this.width = width;
        this.height = height;
    }
    public Position getBottomLeft() {
        return bottomLeft;
    }
    public Position getTopRight() {
        return topRight;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
}
