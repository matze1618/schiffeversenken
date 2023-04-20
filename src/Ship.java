import java.util.HashSet;
import java.util.Objects;

public class Ship {
    private Coordinate basePosition;
    private final HashSet<Coordinate> allPositions = new HashSet<>();
    private final HashSet<Coordinate> periphery = new HashSet<>();
    private final int size;
//    private String orientation;
    private boolean horizontal;
    public int aniBlocks = 0;
    private int lives;

    public int getLives() {
        return lives;
    }

    private final boolean armored;

    Ship(int x, int y, int size, String orientation, boolean armored){
        this.basePosition = new Coordinate(x, y);
        this.size = size;
        this.lives = size;
//        this.orientation = orientation.toUpperCase();
        this.horizontal = (Objects.equals(orientation.toUpperCase(), "H"));
        this.armored = armored;

        setAllPositions();
        setPeriphery();
    }

    private void setAllPositions(){
        if (horizontal){
            for(int i = 0; i < size; i++){
                allPositions.add(new Coordinate(basePosition.getX() + i, basePosition.getY()));
            }
        } else {
            for(int i = 0; i < size; i++){
                allPositions.add(new Coordinate(basePosition.getX(), basePosition.getY() + i));
            }
        }
    }

    private void setPeriphery(){
        if (horizontal) {
            if (!(basePosition.getX() == 0)) {
                periphery.add(new Coordinate(basePosition.getX() - 1, basePosition.getY()));
            }
            if (basePosition.getY() == 0) {
                for (Coordinate coordinate : allPositions) {
                    periphery.add(new Coordinate(coordinate.getX(), coordinate.getY() + 1));
                }
            } else if (basePosition.getY() == 9) { //TODO: Das muss eigentlich field.getY - 1 sein
                for (Coordinate coordinate : allPositions) {
                    periphery.add(new Coordinate(coordinate.getX(), coordinate.getY() - 1));
                }
            } else {
                for (Coordinate coordinate : allPositions) {
                    periphery.add(new Coordinate(coordinate.getX(), coordinate.getY() - 1));
                    periphery.add(new Coordinate(coordinate.getX(), coordinate.getY() + 1));
                }
            }
            if (!(basePosition.getX() == 9)) { //TODO: Das muss eigentlich field.getX - 1 sein
                periphery.add(new Coordinate(basePosition.getX() + 1, basePosition.getY()));
            }
        } else {
            if (!(basePosition.getY() == 0)) {
                periphery.add(new Coordinate(basePosition.getX(), basePosition.getY() - 1));
            }
            if (basePosition.getX() == 0) {
                for (Coordinate coordinate : allPositions) {
                    periphery.add(new Coordinate(coordinate.getX() + 1, coordinate.getY()));
                }
            } else if (basePosition.getY() == 9) { //TODO: Das muss eigentlich field.getX - 1 sein
                for (Coordinate coordinate : allPositions) {
                    periphery.add(new Coordinate(coordinate.getX() - 1, coordinate.getY()));
                }
            } else {
                for (Coordinate coordinate : allPositions) {
                    periphery.add(new Coordinate(coordinate.getX() - 1, coordinate.getY()));
                    periphery.add(new Coordinate(coordinate.getX() + 1, coordinate.getY()));
                }
            }
            if (!(basePosition.getY() == 9)) { //TODO: Das muss eigentlich field.getY - 1 sein
                periphery.add(new Coordinate(basePosition.getX(), basePosition.getY() + 1));
            }
        }
    }

    Ship(int size){
        this.size = size;
        this.armored = false;
    }
    Ship(int size, boolean armored) {
        this.size = size;
        this.armored = armored;
    }

    public void setShip(int xCoord, int yCoord, String orientation) {
//        this.orientation = orientation;
        this.horizontal = (Objects.equals(orientation.toUpperCase(), "H"));
        this.basePosition = new Coordinate(xCoord, yCoord);

        setAllPositions();
        setPeriphery();
    }

    public int getXCoord() {
        return basePosition.getX();
    }

    public int getYCoord() {
        return basePosition.getY();
    }

    public int getSize() {
        return size;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    //    public String getOrientation() {
//        return orientation;
//    }


    //TODO Wird hier else{return 0;} erreicht?
    //TODO: Warum hat jeder Fall return 0?
    //TODO: Modellierung über Klasse oder Enum, statt Informationen durch Pseudo-Zahlen abzubilden?
    public int isAt(int x, int y) {
        if (horizontal && x >= basePosition.getX() && x <= basePosition.getX() + size -1 && y == basePosition.getY()) {

            if(x == basePosition.getX()) return 4;
            else if(x == basePosition.getX() + size -1) return 6;
            else if(x < basePosition.getX() + size -1) return 5;
            else{return 0;}


        } else if (!horizontal && y >= basePosition.getY() && y <= basePosition.getY() + size -1 && x == basePosition.getX()) {
            if(y == basePosition.getY()) return 1;
            else if(y == basePosition.getY() + size -1) return 3;
            else if(y < basePosition.getY() + size -1) return 2;
            else{return 0;}
        }
        return 0;
    }

    public boolean isBlocked(Ship ship) {
        if (horizontal) {
            for (int i = 0; i < ship.getSize(); i++) {
                if (isAtToBool(ship.getXCoord() + i, ship.getYCoord()) || isAtToBool(ship.getXCoord() - 1 + i, ship.getYCoord()) || isAtToBool(ship.getXCoord() + 1 + i, ship.getYCoord()) || isAtToBool(ship.getXCoord() + i, ship.getYCoord() - 1) || isAtToBool(ship.getXCoord() + i, ship.getYCoord() + 1)) {
                    return true;
                }
            }
            return false;
        }
        else{
            for (int i = 0; i < ship.getSize(); i++) {
                if (isAtToBool(ship.getXCoord(), ship.getYCoord() + i) || isAtToBool(ship.getXCoord() - 1, ship.getYCoord() + i) || isAtToBool(ship.getXCoord() + 1, ship.getYCoord() + i) || isAtToBool(ship.getXCoord(), ship.getYCoord() - 1 + i) || isAtToBool(ship.getXCoord(), ship.getYCoord() + 1 + i)) {
                    return true;
                }
            }
            return false;
        }
    }


    //TODO: Hat diese Lücke eine Bedeutung?

    public boolean isAtToBool(int x, int y){
        return isAt(x, y) <= 6 && isAt(x, y) >= 1;
    }

    public void getHit(Field field){
        lives--;
        if(destroyed()){
            System.out.println("Du hast ein Schiff mit der Länge " + getSize() + " zerstört!");
            field.shotsInBarrier();
            createAnimation(field);
            aniBlocks = 0;
        }
    }
    public boolean destroyed(){
        return lives <= 0;
    }

    public boolean isArmored(){
        return armored;
    }

    public void createAnimation(Field field){
        Animation animation = new Animation(this); //, field);
        field.animations[field.aniCounter] = animation;
        field.aniCounter++;
    }

    public boolean isAllowed(int size){
        for (Coordinate coordinate : allPositions){
            if (!coordinate.isValid(size)){
                return false;
            }
        }
        return true;
    }
}
// gibt es merch für merge?
