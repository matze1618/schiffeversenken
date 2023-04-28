import java.util.HashSet;
import java.util.Objects;

public class Ship {
    private Coordinate basePosition;
    private final HashSet<Coordinate> allPositions = new HashSet<>();
    private final HashSet<Coordinate> periphery = new HashSet<>();
    private final int size;
    private boolean isHorizontal;
    public int aniBlocks = 0;
    private int lives;
    private final boolean armored;

    public int getLives() {
        return lives;
    }

    public Coordinate getBasePosition() {
        return basePosition;
    }

    public HashSet<Coordinate> getPeriphery() {
        return periphery;
    }

    Ship(int x, int y, boolean isHorizontal, int size, boolean armored, Field field){
        this.basePosition = new Coordinate(x, y);
        this.size = size;
        this.lives = size;
        this.isHorizontal = isHorizontal;
        this.armored = armored;

        setAllPositions();
        setPeriphery(field);
    }

    public void setAllPositions(){
        allPositions.clear();
        if (isHorizontal){
            for(int i = 0; i < size; i++){
                allPositions.add(new Coordinate(basePosition.getX() + i, basePosition.getY()));
            }
        } else {
            for(int i = 0; i < size; i++){
                allPositions.add(new Coordinate(basePosition.getX(), basePosition.getY() + i));
            }
        }
    }

    public void setPeriphery(Field field){
        periphery.clear();
        HashSet<Coordinate> potentialPeriphery = new HashSet<>();

        if (isHorizontal) {
            potentialPeriphery.add(new Coordinate(basePosition.getX() - 1, basePosition.getY())); // links

            for (Coordinate coordinate : allPositions){
                potentialPeriphery.add(new Coordinate(coordinate.getX(), coordinate.getY() + 1)); // drunter
                potentialPeriphery.add(new Coordinate(coordinate.getX(), coordinate.getY() - 1)); // drüber
            }

            potentialPeriphery.add(new Coordinate(basePosition.getX() + size, basePosition.getY())); // rechts
        } else {
            potentialPeriphery.add(new Coordinate(basePosition.getX(), basePosition.getY() - 1)); // drüber

            for (Coordinate coordinate : allPositions){
                potentialPeriphery.add(new Coordinate(coordinate.getX() + 1, coordinate.getY())); // rechts
                potentialPeriphery.add(new Coordinate(coordinate.getX() - 1, coordinate.getY())); // links
            }

            potentialPeriphery.add(new Coordinate(basePosition.getX(), basePosition.getY() + size)); // drunter
        }
        for (Coordinate coordinate : potentialPeriphery){
            if (coordinate.isValid(field.getSize())){
                periphery.add(coordinate);
            }
        }
    }

    Ship(int size){
        this.size = size;
        this.lives = size;
        this.armored = false;
    }

    Ship(int size, boolean armored) {
        this.size = size;
        this.lives = size;
        this.armored = armored;
    }

    public void setShip(int xCoord, int yCoord, String orientation, Field field) {
        this.isHorizontal = (Objects.equals(orientation.toUpperCase(), "H"));
        this.basePosition = new Coordinate(xCoord, yCoord);

        setAllPositions();
        setPeriphery(field);
    }

    public void setShip(int xCoord, int yCoord, boolean isHorizontal, Field field) {
        this.isHorizontal = isHorizontal;
        this.basePosition = new Coordinate(xCoord, yCoord);

        setAllPositions();
        setPeriphery(field);
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
        return isHorizontal;
    }

    public HashSet<Coordinate> getAllPositions() {
        return allPositions;
    }

    //TODO Wird hier else{return 0;} erreicht?
    //TODO: Warum hat jeder Fall return 0?
    //TODO: Modellierung über Klasse oder Enum, statt Informationen durch Pseudo-Zahlen abzubilden?
    public int isAt(int x, int y) {
        if (isHorizontal && x >= basePosition.getX() && x <= basePosition.getX() + size -1 && y == basePosition.getY()) {

            if(x == basePosition.getX()) return 4;
            else if(x == basePosition.getX() + size -1) return 6;
            else if(x < basePosition.getX() + size -1) return 5;
            else{return 0;}


        } else if (!isHorizontal && y >= basePosition.getY() && y <= basePosition.getY() + size -1 && x == basePosition.getX()) {
            if(y == basePosition.getY()) return 1;
            else if(y == basePosition.getY() + size -1) return 3;
            else if(y < basePosition.getY() + size -1) return 2;
            else{return 0;}
        }
        return 0;
    }

    public boolean isAtToBool(int x, int y){
        return isAt(x, y) <= 6 && isAt(x, y) >= 1;
    }

    public void getHit(Field field){
        lives--;
        if(destroyed()){
            try {
                field.drawWithAnimation(this);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Du hast ein Schiff mit der Länge " + getSize() + " zerstört!");
            field.shotsInPeriphery(this);
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

    public boolean isAllowedOn(Field field){
        if (Objects.isNull(isHorizontal) || Objects.isNull(basePosition)){
            return false;
        }
        for (Coordinate position : periphery){
            for (Coordinate coordinate: field.getCoordinatesOccupiedByShips()){
                if (coordinate.equalTo(position)) {
                    return false;
                }
            }
        }
        for (Coordinate position : allPositions) {
            for (Coordinate coordinate : field.getCoordinatesOccupiedByShips()) {
                if (coordinate.equalTo(position)) {
                    return false;
                }
            }
        }
        return true;
    }
}
// gibt es merch für merge?
