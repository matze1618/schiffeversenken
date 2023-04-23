import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class Field {
    private int size = 10;
    boolean showAnimation = false;
    Ship[] ships;
    int addCounter = 0;
    HashSet<Shot> shots = new HashSet<>();
    private Shot lastHit;
    private Shot lastShot;
    Animation[] animations;
    int aniCounter = 0;
    private HashSet<Coordinate> coordinatesOccupiedByShips = new HashSet<>();
    private String[][] visualRepresentation;

    public Field() {
        this.ships = new Ship[10];
        animations = new Animation[60];
    }

    public Shot getLastShot() {
        return lastShot;
    }

    public Shot getLastHit() {
        return lastHit;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public HashSet<Coordinate> getCoordinatesOccupiedByShips() {
        return coordinatesOccupiedByShips;
    }

    void draw(boolean showShips) throws InterruptedException { //TODO: does hasChange ever make sense?
        setVisualRepresentation(showShips);
        for (int row = 0; row <= (size + 2); row++) {
            for (int column = 0; column <=  (size + 1); column++) {
                System.out.print(visualRepresentation[row][column]);
            }
            System.out.println();
        }
    }

    private void setVisualRepresentation(boolean showShips) {
        setEmptyField();
        if (showShips){
            setShips();
        }
        setHits();
    }

    private void setEmptyField(){
        visualRepresentation = new String[size + 3][size + 2]; //TODO: Ist diese Einteilung sinnvoll hinsichtlich der Definition von x und y?

        setFirstLines();
        setMiddle();
        setLastLine();
    }

    private void setFirstLines(){
        visualRepresentation[0][0] = "   |";
        for (int column = 1; column <= size; column++){
            visualRepresentation[0][column] = "  " + column;
        }
        visualRepresentation[0][size + 1] = " |";

        visualRepresentation[1][0] = "---+-";
        for (int column = 1; column <= size; column++){
            visualRepresentation[1][column] = "---";
        }
        visualRepresentation[1][size + 1] = "-|";
    }

    private void setMiddle(){
        char letter = 'A';

        for (int row = 2; row <= size + 1; row++){
            visualRepresentation[row][0] = " " + letter + " | ";
            letter++;
            for (int column = 1; column <= size; column++){
                visualRepresentation[row][column] = Main.ANSI_BLUE + "~~~" + Main.ANSI_RESET;
            }
            visualRepresentation[row][size + 1] = " |";
        }
    }

    private void setLastLine(){
        visualRepresentation[size + 2][0] = "---+-";
        for (int column = 1; column <= size; column++) {
            visualRepresentation[size + 2][column] = "---";
        }
        visualRepresentation[size + 2][size + 1] = "-|";
    }

    private void setShips(){
        for (Ship ship : ships) {
            //TODO: Show ships including front and end

            if (ship.isHorizontal()){
                visualRepresentation[ship.getBasePosition().getY() + 2][ship.getBasePosition().getX() + 1] = "╠■■";
                for (int i = 2; i < ship.getSize(); i++){
                    visualRepresentation[ship.getBasePosition().getY() + 2][ship.getBasePosition().getX() + i] = "■■■";
                }
                visualRepresentation[ship.getBasePosition().getY() + 2][ship.getBasePosition().getX() + ship.getSize()] = "■■▶";
            } else {
                visualRepresentation[ship.getBasePosition().getY() + 2][ship.getBasePosition().getX() + 1] = " ▲ ";
                for (int i = 2; i < ship.getSize(); i++){
                    visualRepresentation[ship.getBasePosition().getY() + i + 1][ship.getBasePosition().getX() + 1] = " \u2588 ";
                }
                visualRepresentation[ship.getBasePosition().getY() + ship.getSize() + 1][ship.getBasePosition().getX() + 1] = " ╩ ";

            }
        }
    }

    private void setHits(){
        for (Shot shot : shots){
            if (shot.isHit(this)){
                visualRepresentation[shot.getX() + 1][shot.getY()] = Main.ANSI_RED + " X " + Main.ANSI_RESET;
            } else if (!shot.getPlacedManually()) {
                visualRepresentation[shot.getX() + 1][shot.getY()] = Main.ANSI_CYAN + " X " + Main.ANSI_RESET;
            } else {
                visualRepresentation[shot.getX() + 1][shot.getY()] = " X ";
            }
        }
    }

    void placeShip(int x, int y, boolean isHorizontal, int length, boolean armored) {
        Ship ship = new Ship(x, y, length, isHorizontal, armored);
        ships[addCounter] = ship;
        updateOccupiedCoordinates(ship);
    }

    void placeShip(Ship ship) {
        ships[addCounter] = ship; //TODO: Ist die Reihenfolge irgendwo wichtig oder kann ich hier eine HashMap draus machen?
        updateOccupiedCoordinates(ship);
    }

    void placeShot(Shot shot){
        shots.add(shot);
        lastShot = shot;
        if (shot.isHit(this)){
            lastHit = shot;
        }
    }
    private void updateOccupiedCoordinates(Ship ship){
        for (Coordinate coordinate : ship.getAllPositions()){
            coordinatesOccupiedByShips.add(coordinate);
        }
    }

    public boolean isClear (Ship schiff){
        for (int i = 0; i < addCounter; i++) {
            if (ships[i].isBlocked(schiff)) {
                return false;
            }
        }
        return true;
    }

    public void checkGameOver(String winnerName) {
        if (!Main.gameOver) {
            Main.gameOver = true;
            for (int i = 0; i < addCounter; i++) {
                if (!ships[i].destroyed()) {
                    Main.gameOver = false;
                }
            }
            if (Main.gameOver) {
                System.out.println(winnerName + " hat gewonnen!");
                System.exit(0);
            }
        }
    }

    int shipsAliveByLength(int laenge){
        int counter = 0;
        for (Ship ship : ships) {
            if (ship.getSize() == laenge && !ship.destroyed()) {
                counter++;
            }
        }
        return counter;
    }

    void shotsInBarrier() {
        for (Ship ship : ships) {
            if (ship.destroyed()) {
                if (ship.isHorizontal()) {
                    placeShotAutomated(ship.getXCoord() - 1, ship.getYCoord());
                    placeShotAutomated(ship.getXCoord() + ship.getSize(), ship.getYCoord());
                    for (int j = 0; j < ship.getSize(); j++) {
                        placeShotAutomated(ship.getXCoord() + j, ship.getYCoord() + 1);
                        placeShotAutomated(ship.getXCoord() + j, ship.getYCoord() - 1);
                    }
                } else {
                    placeShotAutomated(ship.getXCoord(), ship.getYCoord() - 1);
                    placeShotAutomated(ship.getXCoord(), ship.getYCoord() + ship.getSize());
                    for (int j = 0; j < ship.getSize(); j++) {
                        placeShotAutomated(ship.getXCoord() + 1, ship.getYCoord() + j);
                        placeShotAutomated(ship.getXCoord() - 1, ship.getYCoord() + j);
                    }
                }

            }
        }
    }

    boolean placeShotAutomated(int x, int y) {
        if(x >= 0 && x <= size - 1 && y >= 0 && y <= size - 1) {
            for (Shot shot : shots) {
                if(shot.isAt(new Coordinate(x, y))) {
                    return false;
                }
            }
            Shot shot = new Shot(x, y, false);
            shots.add(shot);
            if (shot.isHit(this)){
                lastHit = shot;
            }
//            shotCounter++;
        }
        return false;
    }
}
