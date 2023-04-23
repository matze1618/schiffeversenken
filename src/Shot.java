public class Shot {
    private final Coordinate position;
    private final boolean placedManually;

    Shot(int x, int y, boolean placedManually){
        position = new Coordinate(x, y);
        this.placedManually = placedManually;
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    public boolean getPlacedManually() {
        return placedManually;
    }

    public boolean isHit(Field spielfeld){ //TODO: Make an attribute out of this to only check once. If it is a hit, it will always be a hit.
        for(int i = 0; i < spielfeld.addCounter; i++){
            if(spielfeld.ships[i].isAtToBool(position.getX(), position.getY())){
                spielfeld.ships[i].getHit(spielfeld);
                return !spielfeld.ships[i].isArmored() || spielfeld.ships[i].getLives() < spielfeld.ships[i].getSize() - 1;
            }
        }
        return false;
    }

    public boolean isAt(Coordinate coordinate){
        return coordinate.equalTo(position);
    }
}
//mergemania
