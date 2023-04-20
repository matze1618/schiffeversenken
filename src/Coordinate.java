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

    public boolean isValid(int size){
        return (x > 0 && x < size && y > 0 && y < size);
    }
}
