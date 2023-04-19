public class Shot {
    private final Coordinate position;
    private final boolean placedManually;

    Shot(int x, int y, boolean placedManually){
        position = new Coordinate(x, y);
        this.placedManually = placedManually;
    }

    public int getxCoord() {
        return position.getX();
    }

    public int getyCoord() {
        return position.getY();
    }

    public boolean getPlacedManually() {
        return placedManually;
    }

    public boolean checkHit(Field spielfeld) throws InterruptedException {
        for(int i = 0; i < spielfeld.addCounter; i++){
            if(spielfeld.ships[i].isAtToBool(position.getX(), position.getY())){
                spielfeld.ships[i].getHit(spielfeld);
                return !spielfeld.ships[i].isArmored() || spielfeld.ships[i].getLives() < spielfeld.ships[i].getSize() - 1;
            }
        }
        return false;
    }

    public boolean isAt(int x, int y){
        return x == position.getX() && y == position.getY();
    }
}
//mergemania
