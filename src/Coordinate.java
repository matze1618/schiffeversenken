public class Coordinate {
    private final int x;
    private final int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean equalTo(Coordinate coordinate){
        return (x == coordinate.getX() && y == coordinate.getY());
    }

    public boolean isWithinField(int size){
        return (x >= 0 && x < size && y >= 0 && y < size);
    }

    public Coordinate withOffset(Coordinate offset) {
        return new Coordinate(x + offset.getX(), y + offset.getY());
    }
}
