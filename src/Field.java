import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;

public class Field {
    private int size = 10;
    Ship[] ships;
    int addCounter = 0;
    HashSet<Shot> shots = new HashSet<>();
    private Shot lastHit;
    private Shot lastShot;
    private HashSet<Coordinate> coordinatesOccupiedByShips = new HashSet<>();
    private String[][] visualRepresentation;
    public boolean shipsAreSet = false;

    private final String[] animationArray = {"؎", "؏", "#", "༚༚", "࿀", "༞", "\"", "?", "§", "%", "&", "\\", ">", "<", "⌘", "°", "^"};


    public Field() {
        this.ships = new Ship[10];
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

    public Shot getLastShot() {
        return lastShot;
    }

    public void drawWithAnimation(Ship ship) throws InterruptedException{
        setVisualRepresentation(false);
        for (int i = 0; i < 40; i++){
            setAnimation(ship);
            flush();
            System.out.println(visualRepresentationToString());
            Thread.sleep(25);
        }
    }

    void flush(){
        for (int i = 0; i < 20; i++){
            System.out.println();
        }
    }

    void draw(boolean showShips) {
        setVisualRepresentation(showShips);
        System.out.println(visualRepresentationToString());
    }

    String visualRepresentationToString(){
        String resultString = "";

        for (int row = 0; row <= (size + 2); row++) {
            for (int column = 0; column <=  (size + 2); column++) {
                resultString = resultString + visualRepresentation[row][column];
            }
            resultString = resultString + "\n";
        }
        return resultString;
    }

    private void setVisualRepresentation(boolean showShips) {
        setEmptyField();
        if (showShips){
            setShips();
        } else {
            setShipsToHit();
        }
        setHits();
    }

    private void setEmptyField(){
        visualRepresentation = new String[size + 3][size + 3]; //TODO: Ist diese Einteilung sinnvoll hinsichtlich der Definition von x und y?

        setFirstLines();
        setMiddle();
        setLastLine();
    }

    private void setFirstLines(){
        visualRepresentation[0][0] = "   |";
        visualRepresentation[0][size + 2] = "";
        for (int column = 1; column <= size; column++){
            visualRepresentation[0][column] = "  " + column;
        }
        visualRepresentation[0][size + 1] = " |";

        visualRepresentation[1][0] = "---+-";
        for (int column = 1; column <= size; column++){
            visualRepresentation[1][column] = "---";
        }
        visualRepresentation[1][size + 1] = "-|";
        visualRepresentation[1][size + 2] = "";
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
            visualRepresentation[row][size + 2] = "";
        }
    }

    private void setLastLine(){
        visualRepresentation[size + 2][0] = "---+-";
        for (int column = 1; column <= size; column++) {
            visualRepresentation[size + 2][column] = "---";
        }
        visualRepresentation[size + 2][size + 1] = "-|";
        visualRepresentation[size + 2][size + 2] = "";
    }

    private void setShipsToHit(){
        HashMap<Integer, String> sizeToString = new HashMap<>();
        sizeToString.put(1, "   ╠■▶");
        sizeToString.put(2, "   ╠■■■■▶");
        sizeToString.put(3, "   ╠■■■■■■■▶");
        sizeToString.put(4, "   ╠■■■■■■■■■■▶");
        sizeToString.put(5, "   ╠■■■■■■■■■■■■■▶");
        sizeToString.put(6, "   ╠■■■■■■■■■■■■■■■■▶");


        visualRepresentation[1][size + 2] = "   Diese Schiffe musst Du noch treffen:";
        for (Ship ship : ships) {
            if (!ship.destroyed()){
                visualRepresentation[ship.getSize() + 2][size + 2] = visualRepresentation[ship.getSize() + 2][size + 2] + sizeToString.get(ship.getSize());
            }
        }
    }

    private void setShips(){
        for (Ship ship : ships) {
            if (Objects.isNull(ship)){
                break;
            }

            if (ship.getSize() == 1) {
                visualRepresentation[ship.getBasePosition().getY() + 2][ship.getBasePosition().getX() + 1] = "╠■▶";
            } else {
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
    }

    private void setHits(){
        for (Shot shot : shots){
            if (shot.isHit()){
                visualRepresentation[shot.getY() + 2][shot.getX() + 1] = Main.ANSI_RED + " X " + Main.ANSI_RESET;
            } else if (!shot.isPlacedManually()) {
                visualRepresentation[shot.getY() + 2][shot.getX() + 1] = Main.ANSI_CYAN + " X " + Main.ANSI_RESET;
            } else {
                visualRepresentation[shot.getY() + 2][shot.getX() + 1] = " X ";
            }
        }
    }

    private void setAnimation(Ship ship){
        for (Coordinate coordinate : ship.getAllPositions()) {
            setAnimationBlock(coordinate);
        }
        for (Coordinate coordinate : ship.getPeriphery()) {
            setAnimationBlock(coordinate);
        }
    }

    private void setAnimationBlock(Coordinate coordinate){
        Random random = new Random();
        String colour0 = (random.nextInt(3) < 2 ? Main.ANSI_YELLOW : Main.ANSI_RED);
        String colour1 = (random.nextInt(3) < 2 ? Main.ANSI_YELLOW : Main.ANSI_RED);
        String colour2 = (random.nextInt(3) < 2 ? Main.ANSI_YELLOW : Main.ANSI_RED);
        String symbol0 = animationArray[random.nextInt(animationArray.length)];
        String symbol1 = animationArray[random.nextInt(animationArray.length)];
        String symbol2 = animationArray[random.nextInt(animationArray.length)];
        String reset = Main.ANSI_RESET;
        String animationBlock = colour0 + symbol0 + reset + colour1 + symbol1 + reset + colour2 + symbol2 + reset;

        visualRepresentation[coordinate.getY() + 2][coordinate.getX() + 1] = animationBlock;
    }

    void placeShipForTest(Ship ship) {
        ships[addCounter] = ship; //TODO: Ist die Reihenfolge irgendwo wichtig oder kann ich hier eine HashMap draus machen? => Ist relevant für die Eingabe und randomPlaceShip
        updateOccupiedCoordinates(ship);
    }

    void placeShot(Shot shot){
        shots.add(shot);
        lastShot = shot;
        if (shot.isHit()){
            lastHit = shot;
        }
    }

    private void updateOccupiedCoordinates(Ship ship){
        coordinatesOccupiedByShips.addAll(ship.getAllPositions());
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

    void shotsInPeriphery(Ship ship) {
        for (Coordinate coordinate : ship.getPeriphery()){
            placeShotAutomated(coordinate);
        }
    }

    boolean placeShotAutomated(Coordinate coordinate) {
        if(coordinate.isValid(this.size)) {
            for (Shot shot : shots) {
                if(shot.isAt(coordinate)) {
                    return false;
                }
            }
            Shot shot = new Shot(coordinate, false, this);
            shots.add(shot);
            if (shot.isHit()){
                lastHit = shot;
            }
        }
        //TODO: false either way?
        return false;
    }

    void placeShipForTest(int x, int y, boolean isHorizontal, int size, boolean armored){
        placeShipForTest(new Ship(x, y, isHorizontal, size, armored, this));
    }
}
