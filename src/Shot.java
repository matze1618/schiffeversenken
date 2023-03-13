public class Shot {
    private final int xCoord;
    private final int yCoord;
    private final boolean placedManually;

    Shot(int x, int y, boolean placedManually){
        xCoord = x;
        yCoord = y;
        this.placedManually = placedManually;
    }


    public int getxCoord() {
        return xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }



    public boolean getPlacedManually() {
        return placedManually;
    }

    public boolean checkHit(Field spielfeld) throws InterruptedException {
        for(int i = 0; i < spielfeld.addCounter; i++){
            if(spielfeld.schiffe[i].isAtToBool(xCoord, yCoord)){
                spielfeld.schiffe[i].getHit(spielfeld);
                return !spielfeld.schiffe[i].isArmored() || spielfeld.schiffe[i].getLeben() < spielfeld.schiffe[i].getLaenge() - 1;
            }
        }
        return false;
    }


    public boolean isAt(int x, int y){
        return x == xCoord && y == yCoord;
    }
}
//mergemania
