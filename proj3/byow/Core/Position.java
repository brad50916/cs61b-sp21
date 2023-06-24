package byow.Core;
/***
 * The Position object is used to represent a single position in the world.
 *
 * All Position objects have x-axis and y-axis values.
 */
public class Position {
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
    public boolean equalsto(Position p) {
        if (p.getX() == this.x && p.getY() == this.y) {
            return true;
        }
        return false;
    }
}
