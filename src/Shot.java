public class Shot {
    private final Coordinate position;
    private final boolean isPlacedManually;
    private final boolean isHit;

    Shot(int x, int y, boolean isPlacedManually, Field field){
        position = new Coordinate(x, y);
        this.isPlacedManually = isPlacedManually;
        this.isHit = checkHit(field);
    }

    Shot(Coordinate position, boolean isPlacedManually, Field field){
        this.position = position;
        this.isPlacedManually = isPlacedManually;
        this.isHit = checkHit(field);
    }

    public Coordinate getPosition() {
        return position;
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    public boolean isPlacedManually() {
        return isPlacedManually;
    }

    public boolean isHit() {
        return isHit;
    }

    //TODO: This shouldn't decrement the ships lives. While it may reduce redundancies, it makes the code harder to understand. Refactor!
    private boolean checkHit(Field field){
        for(int i = 0; i < field.addCounter; i++){
            if(field.ships[i].isAtToBool(position)){
                field.ships[i].getHit(field);
                return !field.ships[i].isArmored() || field.ships[i].getLives() < field.ships[i].getSize() - 1;
            }
        }
        return false;
    }

    public boolean isAt(Coordinate coordinate){
        return coordinate.equalTo(position);
    }
}
//mergemania
