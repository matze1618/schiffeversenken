
public class Ship {
    private int xCoord;
    private int yCoord;
    private final int laenge;
    private String orientation;
    public int aniBlocks=0;

    public int getLeben() {
        return leben;
    }

    private int leben;
    private final boolean armored;
    //
    Ship(int xCoord, int yCoord, int laenge, String orientation, boolean armored){
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.laenge = laenge;
        this.leben = laenge;
        this.orientation = orientation.toUpperCase();
        this.armored = armored;
    }

    Ship(int laenge) {
        this.laenge = laenge;
        this.armored = false;
    }
    Ship(int laenge, boolean armored) {
        this.laenge = laenge;
        this.armored = armored;
    }

    public void setShip(int xCoord, int yCoord, String orientation) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.orientation = orientation;
    }

    public int getXCoord() {
        return xCoord;
    }

    public int getYCoord() {
        return yCoord;
    }

    public int getLaenge() {
        return laenge;
    }

    public String getOrientation() {
        return orientation;
    }


    //TODO Wird hier else{return 0;} erreicht?
    public int isAt(int x, int y) {
        if ("H".equalsIgnoreCase(orientation) && x >= xCoord && x <= xCoord + laenge-1 && y == yCoord) {

            if(x == xCoord) return 4;
            else if(x == xCoord + laenge-1) return 6;
            else if(x < xCoord +laenge-1) return 5;
            else{return 0;}


        } else if ("V".equalsIgnoreCase(orientation) && y >= yCoord && y <= yCoord + laenge-1 && x == xCoord) {
            if(y == yCoord) return 1;
            else if(y == yCoord + laenge-1) return 3;
            else if(y < yCoord +laenge-1) return 2;
            else{return 0;}
        }
        return 0;
    }



    public boolean isBlocked(Ship schiff) {
        if ("H".equalsIgnoreCase(schiff.orientation)) {
            for (int i = 0; i < schiff.getLaenge(); i++) {
                if (isAtToBool(schiff.getXCoord() + i, schiff.getYCoord()) || isAtToBool(schiff.getXCoord() - 1 + i, schiff.getYCoord()) || isAtToBool(schiff.getXCoord() + 1 + i, schiff.getYCoord()) || isAtToBool(schiff.getXCoord() + i, schiff.getYCoord() - 1) || isAtToBool(schiff.getXCoord() + i, schiff.getYCoord() + 1)) {
                    return true;
                }
            }
            return false;
        }
        else{
            for (int i = 0; i < schiff.getLaenge(); i++) {
                if (isAtToBool(schiff.getXCoord(), schiff.getYCoord() + i) || isAtToBool(schiff.getXCoord() - 1, schiff.getYCoord() + i) || isAtToBool(schiff.getXCoord() + 1, schiff.getYCoord() + i) || isAtToBool(schiff.getXCoord(), schiff.getYCoord() - 1 + i) || isAtToBool(schiff.getXCoord(), schiff.getYCoord() + 1 + i)) {
                    return true;
                }
            }
            return false;
        }
    }


//

    public boolean isAtToBool(int x, int y){
        return isAt(x, y) <= 6 && isAt(x, y) >= 1;
    }


    public void getHit(Field spielfeld) throws InterruptedException {
        leben--;
        if(zerstoert()){
            System.out.println("Du hast ein Schiff mit der Länge " + getLaenge() + " zerstört!");
            //TimeUnit.MILLISECONDS.sleep(1000);
            spielfeld.shotsInBarrier();
            createAnimation(spielfeld);
            aniBlocks = 0;
        }
    }
    public boolean zerstoert(){
        return leben <= 0;
    }


    public boolean isArmored(){
        return armored;
    }

    public void createAnimation(Field spielfeld){
        Animation animation = new Animation(this, spielfeld);
        spielfeld.animations[spielfeld.aniCounter] = animation;
        spielfeld.aniCounter++;
    }
}
// gibt es merch für merge?

