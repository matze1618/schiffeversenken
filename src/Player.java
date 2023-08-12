import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Player {
    public boolean switchActivePlayer = true;
    protected String name = "";
    public Field field = new Field();
    Random random = new Random();
    boolean botSetShips = true;
    public Ship[] ships = {new Ship(5), new Ship(4), new Ship(4), new Ship(3), new Ship(3), new Ship(3), new Ship(2), new Ship(2), new Ship(2), new Ship(1)};
    Scanner scan = new Scanner(System.in);

    Player() {}
    Player(String name) {this.name = name;}

    public boolean opponentIsBotTry() {
        try {
            return opponentIsBot();
        } catch (IllegalArgumentException e) {
            System.out.println("Die Eingabe war ungültig, gib etwas anderes ein.");
            return opponentIsBotTry();
        }
    }

    public boolean opponentIsBot() throws IllegalArgumentException{
        System.out.println("Willst Du gegen den Computer spielen? j/n");
        String temp = scan.next();
        if("J".equalsIgnoreCase(temp)) {
            return true;
        } else if("N".equalsIgnoreCase(temp)) {
            return false;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setGameMode() {
        System.out.println("Willst Du den Normalen oder Advanced Modus spielen? N/A");
        String input = scan.next();
        if("n".equalsIgnoreCase(input)) {
            Main.status = Main.Status.PICKPHASE;
        } else if("a".equalsIgnoreCase(input)){
            Main.status = Main.Status.PICKPHASEAD;
        } else {
            System.out.println("Die Eingabe war ungültig, versucht es nochmal!");
            setGameMode();
        }
    }

    public boolean setNameInput(int spielerNummer) {
        System.out.println("Spieler " + spielerNummer + ", gib deinen Namen ein!");
        String name = scan.next();
        System.out.println(name + ", gib deine Wunschfarbe, in der dein Name angezeigt werden soll, aus den Farben (rot, grün, gelb, blau, lila, cyan oder weiß) ein!");
        scan.nextLine();
        String colour = scan.nextLine();
        switch (colour.toLowerCase()) {
            case "rot" -> colour = Main.ANSI_RED;
            case "grün" -> colour = Main.ANSI_GREEN;
            case "gelb" -> colour = Main.ANSI_YELLOW;
            case "blau" -> colour = Main.ANSI_BLUE;
            case "lila" -> colour = Main.ANSI_PURPLE;
            case "cyan" -> colour = Main.ANSI_CYAN;
            case "weiß" -> colour = Main.ANSI_BG_WHITE;
            default -> {
                System.out.println("Die Eingabe war ungültig. Versuche es erneut.");
                return false;
            }
        };
        setName(name, colour);
        return true;
    }

    public void setName(String name, String colour) {
        this.name = colour + name + Main.ANSI_RESET;
    }

    public boolean inputPickTryExceptions(Field enemy) throws InterruptedException, IllegalArgumentException {
        if (getInputForRandomPlaceShip()){
            return randomPlaceShip(enemy);
        }

        field.draw(true);
        return setShipWithUserInput(enemy);
    }

    private boolean setShipWithUserInput(Field enemy) throws InterruptedException {
        int yCoord;

        System.out.println("Gib die Spalte und Zeile für den Startpunkt und die Orientierung für dein Schiff mit der Länge " + ships[9 - field.addCounter].getSize() + " ein, " + name + ".");

        int x = scan.nextInt();
        String y = scan.next();
        String orientation = scan.next();

        try{
            yCoord = stringToYCoord(y.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e){
            System.out.println("Die Eingabe ist ungültig. Gib etwas anderes ein!");
            return true;
        }

        ships[9 - field.addCounter].setShip(x - 1, yCoord, orientation.toUpperCase(), field);

        if(ships[9 - field.addCounter].isAllowedOn(field)) {
            field.placeShipForTest(new Ship(x - 1, stringToYCoord(y.toUpperCase()), Objects.equals(orientation.toUpperCase(), "H"), ships[9 - field.addCounter].getSize() , ships[9- field.addCounter].isArmored(), field));
            field.addCounter++;
            if(field.addCounter == 10) {
                field.draw(true);
                System.out.println("Drücke ENTER um fortzufahren!");
                scan.nextLine();
                if (enemy.addCounter == 10) {
                    if(Main.status == Main.Status.PICKPHASEAD){Main.status = Main.Status.ATCKAD;}
                    else{Main.status = Main.Status.ATCK;}
                    field.draw(true);
                } else{
                    field.draw(true);
                    TimeUnit.MILLISECONDS.sleep(1500);
                }
                return false;
            }
            else{
                return true;
            }
        } else {
            System.out.println("Bitte wähle eine neue Position, " + name + "!");
            TimeUnit.MILLISECONDS.sleep(1500);
            return true;
        }

    }

    public boolean inputShotTryExceptions(Field enemy) {
        try {
            return inputShot(enemy);
        } catch (InterruptedException | InputMismatchException | IllegalArgumentException e) {
            System.out.println("Die Eingabe war ungültig, versuche es noch einmal!");
            scan.nextLine();
            return true;
        }
    }

    //TODO: Das muss schöner
    public boolean inputShot(Field enemy) throws InterruptedException, IllegalArgumentException {
        if(switchActivePlayer){
            enemy.draw(false);
            switchActivePlayer = false;
        }

        Coordinate coordinate = getCoordinateInput(enemy);

        if (Objects.isNull(coordinate)) {
            return true;
        }

        return tryExecuteShot(coordinate.getX(), coordinate.getY(), enemy);
    }

    private Coordinate getCoordinateInput(Field enemy){
        int y;

        System.out.println("Gib die Koordinaten an, an die du schießen möchtest, " + name + ".");
        int x = scan.nextInt();
        String yString = scan.next();

        try {
            y = stringToYCoord(yString.toUpperCase());
            return new Coordinate(x, y);
        } catch (IllegalArgumentException | NullPointerException e) {
            enemy.draw(false);
            System.out.println("Die Eingabe ist ungültig. Gib etwas anderes ein!");
            return null;
        }
    }

    private boolean tryExecuteShot(int x, int y, Field enemy) throws InterruptedException {
        if (x >= 1 && x <= enemy.getSize() && y >= 0 && y < enemy.getSize()) { //////////////////Innerhalb des Spielfeldes
            if(tryShot(enemy, x, y)) {
                System.out.println("Ey... da haste schon hingeschossen!");
                return true;
            }
            if (enemy.getLastShot().isHit()) { ///////////////////////Getroffen
                enemy.draw(false);
                System.out.println("Getroffen!");
                enemy.checkGameOver(name);
                return true;
            } else { /////////////////////////////// nicht getroffen
                enemy.draw(false);
            }

            TimeUnit.MILLISECONDS.sleep(1500);
            return false;
        } else {///////////////////////außerhalb des spielfeldes
            enemy.draw(false);
            System.out.println("Da können garkeine Schiffe sein... denk nochmal drüber nach!");
            enemy.checkGameOver(name);
            TimeUnit.MILLISECONDS.sleep(1500);
            return true;
        }
    }

    private boolean getInputForRandomPlaceShip(){
        System.out.println("Willst Du die Schiffe zufällig plazieren lassen, " + name + "? j/n");
        String temp = scan.next();
        if("J".equalsIgnoreCase(temp)) {
            return true;
        } else if("N".equalsIgnoreCase(temp)) {
            botSetShips = false;
            return false;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public boolean randomPlaceShip(Field enemy) throws InterruptedException {
        int x;
        int y;
        boolean isHorizontal;
        Random random = new Random();

        for (Ship ship : ships) {
            while (!ship.isAllowedOn(field)) {
                isHorizontal = random.nextBoolean();
                x = random.nextInt(isHorizontal ? (enemy.getSize() - ship.getSize()) : enemy.getSize());
                // true: 10 - 1 - 1 = 8
                // false: 9
                y = random.nextInt(isHorizontal ? enemy.getSize() : (enemy.getSize() - ship.getSize()));
                // true: 9
                // false: 8
                ship.setShip(x, y, isHorizontal, field);
            }

            field.placeShipForTest(ship);
            field.addCounter++;
        }

        field.shipsAreSet = true;

        if (enemy.shipsAreSet) {
            if(Main.status == Main.Status.PICKPHASEAD){Main.status = Main.Status.ATCKAD;}
            else{Main.status = Main.Status.ATCK;}
            field.draw(true);
            System.out.println("Drücke ENTER um fortzufahren!");
            scan.nextLine();
        } else {
            field.draw(true);
            TimeUnit.MILLISECONDS.sleep(1500);
        }
        return false; // TODO: break;
    }

    public int stringToYCoord(String yCoord) {
        if (yCoord.length() == 1 && Character.isUpperCase(yCoord.charAt(0))){
            return yCoord.charAt(0) - 'A';
        } else {
            throw new IllegalArgumentException();
        }
    }

    public boolean tryShot(Field enemy, int x, int yCoord) {
        for (Shot shot : enemy.shots) {
            if (shot.isAt(new Coordinate(x - 1, yCoord))) {
                enemy.draw(false);
                return true;
            }
        }

        Shot shot = new Shot(x - 1, yCoord, true, enemy);

        enemy.placeShot(shot);
        return false;
    }
}
