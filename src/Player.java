import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Player { //TODO: Neue Klasse PayerAD hiervon erben lassen? => Yes, please
    public boolean switchDraw = true;
    protected String name = "";
    public Field field = new Field();
    Random random = new Random();
    boolean botSetShips = true;
    byte powerup = 0; //0 = nichts, 1 = big shot, 2 = line shot, 3 = radar //TODO: Enum?
    Player() {}
    Player(String name) {this.name = name;}
    public Ship[] ships = {new Ship(5), new Ship(4), new Ship(4), new Ship(3), new Ship(3), new Ship(3), new Ship(2), new Ship(2), new Ship(2), new Ship(1)};
    public Ship[] schiffeAD = {new Ship(6), new Ship(5), new Ship(5), new Ship(4), new Ship(4, true), new Ship(3), new Ship(3), new Ship(3, true), new Ship(2), new Ship(2)};

    Scanner scan = new Scanner(System.in);

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
        System.out.println("Wollt ihr den Normalen oder Advanced Modus spielen? N/A");
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

    public void setNameInput(int spielerNummer) {
        System.out.println("Spieler " + spielerNummer + ", gib deinen Namen ein!");
        String name = scan.next();
        System.out.println(name + ", gib deine Wunschfarbe, in der dein Name angezeigt werden soll, aus den Farben (rot, grün, gelb, blau, lila, cyan oder weiß) ein!");
        scan.next();
        String colour = scan.next().toLowerCase();
        colour = switch (colour) {
            case "rot" -> Main.ANSI_RED;
            case "grün" -> Main.ANSI_GREEN;
            case "gelb" -> Main.ANSI_YELLOW;
            case "blau" -> Main.ANSI_BLUE;
            case "lila" -> Main.ANSI_PURPLE;
            case "cyan" -> Main.ANSI_CYAN;
            case "weiß" -> Main.ANSI_BG_WHITE;
            default -> scan.next().toLowerCase();
        };
        setName(name, colour);
    }

    public void setName(String name, String colour) {
        this.name = colour + name + Main.ANSI_RESET;
    }

    public boolean randomPlaceShip(Field enemy) throws InterruptedException {
        int x;
        int y;
        boolean isHorizontal;
        Random random = new Random();

        if (Main.status == Main.Status.PICKPHASEAD) {
            System.arraycopy(schiffeAD, 0, ships, 0, schiffeAD.length);
        }

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

            field.placeShip(ship);
            field.addCounter++;
        }

        field.shipsAreSet = true;

        //TODO: Only print field once
        field.draw(true);
        flush();
        System.out.println("Drücke ENTER um fortzufahren!");
        scan.nextLine();
        if (enemy.shipsAreSet) {
            if(Main.status == Main.Status.PICKPHASEAD){Main.status = Main.Status.ATCKAD;}
            else{Main.status = Main.Status.ATCK;}
            flush();
            field.draw(true);
        } else {
            field.draw(true);
            TimeUnit.MILLISECONDS.sleep(1500);
            flush();
        }
        return false; // TODO: break;
    }

    public void flush() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public boolean inputTryExceptions(Field enemy) {
        try {
            return input(enemy);
        } catch (InterruptedException | InputMismatchException | IllegalArgumentException e) {
            System.out.println("Die Eingabe war ungültig, versuche es noch einmal!");
            scan.nextLine();
            return true;
        }
    }

    //TODO: Das muss schöner
    public boolean input(Field enemy) throws InterruptedException, IllegalArgumentException {
        if(botSetShips && (Main.status == Main.Status.PICKPHASE || Main.status == Main.Status.PICKPHASEAD)) {
            System.out.println("Willst Du die Schiffe zufällig plazieren lassen, " + name + "? j/n");
            String temp = scan.next();
            if("J".equalsIgnoreCase(temp)) {
                return randomPlaceShip(enemy);
            } else if("N".equalsIgnoreCase(temp)) {
                botSetShips = false;
            } else {
                throw new IllegalArgumentException();
            }
        }

        int yCoord;
        if (Main.status == Main.Status.PICKPHASE || Main.status == Main.Status.PICKPHASEAD) {
            if(Main.status == Main.Status.PICKPHASEAD){
                System.arraycopy(schiffeAD, 0, ships, 0, schiffeAD.length);
            }

            field.draw(true);

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
                field.placeShip(new Ship(x - 1, stringToYCoord(y.toUpperCase()), Objects.equals(orientation.toUpperCase(), "H"), ships[9 - field.addCounter].getSize() , ships[9- field.addCounter].isArmored(), field));
                field.addCounter++;
                if(field.addCounter == 10) {
                    field.draw(true);
                    flush();
                    System.out.println("Drücke ENTER um fortzufahren!");
                    scan.nextLine();
                    if (enemy.addCounter == 10) {
                        if(Main.status == Main.Status.PICKPHASEAD){Main.status = Main.Status.ATCKAD;}
                        else{Main.status = Main.Status.ATCK;}
                        flush();
                        field.draw(true);
                        return false;
                    } else{
                        field.draw(true);
                        TimeUnit.MILLISECONDS.sleep(1500);
                        flush();
                        return false;
                    }
                }
                else{
                    flush();
                    return true;
                }
            } else {
                System.out.println("Bitte wähle eine neue Position, " + name + "!");
                TimeUnit.MILLISECONDS.sleep(1500);
                flush();
                return true;
            }
        }

        //TODO: Geht diese Methode schöner?
        else if (Main.status == Main.Status.ATCK || Main.status == Main.Status.ATCKAD) {
            if(switchDraw){
                flush();
                enemy.draw(false);
                switchDraw = false;
            }

            if(powerup == 0) {
                System.out.println("Gib die Koordinaten an, an die du schießen möchtest, " + name + ".");
                int x = scan.nextInt();
                String y = scan.next();

                try {
                    yCoord = stringToYCoord(y.toUpperCase());
                } catch (IllegalArgumentException | NullPointerException e) {
                    flush();
                    enemy.draw(false);
                    System.out.println("Die Eingabe ist ungültig. Gib etwas anderes ein!");
                    return true;
                }

                if (x >= 1 && x <= enemy.getSize() && yCoord >= 0 && yCoord < enemy.getSize()) { //////////////////Innerhalb des Spielfeldes
                    if(tryShot(enemy, x, yCoord)) {
                        System.out.println("Ey... da haste schon hingeschossen!");
                        return true;
                    }

                    if (enemy.getLastShot().isHit()) { ///////////////////////Getroffen
                        do{
                            flush();
                            enemy.draw(false);
                        } while(enemy.showAnimation);

                        flush();
                        enemy.draw(false);

                        System.out.println("Getroffen!");
                        enemy.checkGameOver(name);
                        return true;
                    } else { /////////////////////////////// nicht getroffen
                        flush();
                        enemy.draw(false);

                        if (Main.status == Main.Status.ATCKAD){
                            getPowerUps();
                        }
                    }

                    TimeUnit.MILLISECONDS.sleep(1500);
                    return false;
                } else {///////////////////////außerhalb des spielfeldes
                    flush();
                    enemy.draw(false);
                    System.out.println("Da können garkeine Schiffe sein... denk nochmal drüber nach!");
                    enemy.checkGameOver(name);
                    TimeUnit.MILLISECONDS.sleep(1500);
                    return true;
                }
            }
            else if(powerup == 1){
                System.out.println("Wohin möchtest du einen \"Big-Shot\" setzen? Gib die zentralen Koordinaten an " + name + ".");
                int x = scan.nextInt();
                String y = scan.next();
                placeBigShot(x ,y, enemy);
                //TODO flush?
                return false;
            }
            else if(powerup == 2){
                System.out.println("Auf welche Zeile/Spalte möchtest du schießen " + name + "?");
                String input = scan.next();
                placeLineShot(input, enemy);
                //TODO flush?
                return false;
            }

        }
        return true;
    }

    public int stringToYCoord(String yCoord) {
        if (yCoord.length() == 1 && Character.isUpperCase(yCoord.charAt(0))){
            return yCoord.charAt(0) - 'A';
        }else {
            throw new IllegalArgumentException();
        }
    }

    public boolean tryShot(Field enemy, int x, int yCoord) throws InterruptedException {
        for (Shot shot : enemy.shots) {
            if (shot.isAt(new Coordinate(x - 1, yCoord))) {
                do{
                    flush();
                    enemy.draw(false);
                } while(enemy.showAnimation);

                flush();
                enemy.draw(false);
                return true;
            }
        }

        Shot shot = new Shot(x - 1, yCoord, true, enemy);

        enemy.placeShot(shot);
        return false;
    }

    ////Start of AD-Mode

    public void setFieldSizeCatch(Field enemy) {
        try {
            setFieldSize(enemy);
        } catch (InputMismatchException | IllegalArgumentException e) {
            System.out.println("Die Eingabe war ungültig, gib etwas anderes ein!");
            setFieldSizeCatch(enemy);
        }
    }

    public void setFieldSize(Field enemy) throws IllegalArgumentException{
        System.out.println("Wie lang soll die Seitenlänge eures Spielfeldes sein? (mindestend 10 und maximal 26)");
        int input = scan.nextInt();
        if(input > 26 || input < 10) {
            throw new IllegalArgumentException();
        } else {
            this.field.setSize(input);
            enemy.setSize(input);
        }
    }

    public void getPowerUps() {
        if (random.nextInt(100) >= 40) {
            int random = this.random.nextInt(100);
            if (random >= 66) {
                powerup = 1;
            } else if (random >= 33) {
                powerup = 2;
            } else {
                powerup = 2;
            }
        } else {
            powerup = 1;
        }
        System.out.println("Ins Wasser geschossen!");
        switch (powerup) {
            //TODO: HashMap? => Enum?
            case 1 -> System.out.println("Du hast einen Big Shot als nächsten Schuss!");
            case 2 -> System.out.println("Du hast einen Line Shot als nächsten Schuss!");
            case 3 -> System.out.println("Du hast ein Radar bekommen!");
        }
    }

    //TODO: Geht diese Methode schöner?
    void placeLineShot(String coord, Field enemy) throws InterruptedException {
        boolean clear;

        if(coord.matches("[A-Za-z]+")){
            for(int i=0; i <= enemy.getSize(); i++){
                clear = true;
                for (Shot shot : enemy.shots){
                    if(shot.isAt(new Coordinate(i, stringToYCoord(coord.toUpperCase())))){
                        clear = false;
                    }
                }

                if(clear) {
                    Shot shot = new Shot(i, stringToYCoord(coord.toUpperCase()), true, enemy);
                    enemy.placeShot(shot);

                    if(shot.isHit()){
                        do{
                            flush();
                            enemy.draw(false);
                        } while(enemy.showAnimation);
                        enemy.checkGameOver(name);
                    }
                }
            }
            flush();
            enemy.draw(false);
            powerup = 0;
            TimeUnit.MILLISECONDS.sleep(1500);
        }
        else{
            for(int j=0; j < enemy.getSize(); j++){
                clear = true;
                for (Shot shot : enemy.shots){
                    if(shot.isAt(new Coordinate(Integer.parseInt(coord)-1, j))) {
                        clear = false;
                    }
                }

                if(clear) {
                    Shot shot = new Shot(Integer.parseInt(coord) - 1, j, true, enemy);
                    enemy.placeShot(shot);
                    if (shot.isHit()) {
                        do {
                            flush();
                        } while (enemy.showAnimation);
                        enemy.checkGameOver(name);
                    }
                }
            }
            flush();
            enemy.draw(false);
            powerup = 0;
            TimeUnit.MILLISECONDS.sleep(1500);
        }
    }

    //TODO: Geht diese Methode schöner?
    void placeBigShot(int x, String y, Field enemy) throws InterruptedException {
        boolean clear;
        for(int j = stringToYCoord(y.toUpperCase()) - 1; j <= stringToYCoord(y.toUpperCase()) + 1; j++){
            for(int i = x-1; i <= x+1; i++){
                clear = true;
                if(i > 0 && i <= enemy.getSize() && j >= 0 && j < enemy.getSize()) {
                    for (Shot shot : enemy.shots) {
                        if (shot.isAt(new Coordinate(i - 1, j))) {
                            clear = false;
                        }
                    }

                    if (clear) {
                        Shot shot = new Shot(i - 1, j, true, enemy);
                        enemy.placeShot(shot);

                        if (shot.isHit()) {
                            do {
                                flush();
                                enemy.draw(false);
                            } while (enemy.showAnimation);
                            enemy.checkGameOver(name);
                        }
                    }
                }
            }
        }
        flush();
        enemy.draw(false);
        powerup = 0;
        TimeUnit.MILLISECONDS.sleep(1500);
    }
}
