import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Player {
    public boolean switchDraw = true;
    protected String name = "";
    public Field field = new Field();

    Random generator = new Random();

    boolean botSetShips = true;

    byte powerup = 0; //0 = nichts, 1 = big shot, 2 = line shot, 3 = radar

    Player() {}
    Player(String name) {this.name = name;}

    public Ship[] schiffe = {new Ship(1), new Ship(2), new Ship(2), new Ship(2), new Ship(3), new Ship(3), new Ship(3), new Ship(4), new Ship(4), new Ship(5)};
    public Ship[] schiffeAD = {new Ship(2), new Ship(2), new Ship(3), new Ship(3), new Ship(3, true), new Ship(4), new Ship(4, true), new Ship(5), new Ship(5), new Ship(6)};

    //public int[] schiffe = {1, 2, 2, 2, 3, 3, 3, 4, 4, 5};

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

    //TODO: Hat diese Lücke eine Bedeutung?




    public void setSpielfeldSizeCatch(Field enemy) {
        try {
            setSpielfeldSize(enemy);
        } catch (InputMismatchException | IllegalArgumentException e) {
            System.out.println("Die Eingabe war ungültig, gib etwas anderes ein!");
            setSpielfeldSizeCatch(enemy);
        }
    }

    //TODO: Hat diese Lücke eine Bedeutung?


    public void setSpielfeldSize(Field enemy) throws IllegalArgumentException{
        System.out.println("Wie lang soll die Seitenlänge eures Spielfeldes sein? (mindestend 10 und maximal 26)");
        int input = scan.nextInt();
        if(input > 26 || input < 10) {
            throw new IllegalArgumentException();
        } else {
            this.field.setSize(input);
            enemy.setSize(input);
        }
    }

    //TODO: Hat diese Lücke eine Bedeutung?


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

    //TODO: Hat diese Lücke eine Bedeutung?

//    TODO: Geht diese Methode schöner?
    public boolean randomPlaceShip(Field enemy) throws InterruptedException { //TODO: hier scheint ein Fehler zu sein. Ein Schiffe hat anscheinend die Coordinaten von zwei Schiffen bekommen.
//        int yCoord;
        if (Main.status == Main.Status.PICKPHASEAD) {
            System.arraycopy(schiffeAD, 0, schiffe, 0, schiffeAD.length);
        }

        int x;
        int y;
        String orientation;

        while(true) {
            Random random = new Random();
            x = random.nextInt(enemy.getSize());
            y = random.nextInt(enemy.getSize());
            if(random.nextBoolean()){
                orientation = "H";
            } else {
                orientation = "V";
            }

            schiffe[9 - field.addCounter].setShip(x - 1, y, orientation.toUpperCase());

            if (schiffe[9 - field.addCounter].isAllowed(field.getSize())) {
                field.placeShip(x - 1, y, orientation, schiffe[9 - field.addCounter].getSize(), schiffe[9- field.addCounter].isArmored());
                field.addCounter++;
                if (field.addCounter == 10) {
                    field.draw(true);
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    System.out.println("Drücke ENTER um fortzufahren!");
                    scan.nextLine();
                    if (enemy.addCounter == 10) {
                        if(Main.status == Main.Status.PICKPHASEAD){Main.status = Main.Status.ATCKAD;}
                        else{Main.status = Main.Status.ATCK;}
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        field.draw(true);
                    } else {
                        field.draw(true);
                        TimeUnit.MILLISECONDS.sleep(1500);
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                    }
                    return false;
                } else {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                }
            }
        }
    }

    //TODO: Hat diese Lücke eine Bedeutung?




    public boolean inputTryExceptions(Field enemy) {
        try {
            return input(enemy);
        } catch (InterruptedException | InputMismatchException | IllegalArgumentException e) {
            System.out.println("Die Eingabe war ungültig, versuche es noch einmal!");
            scan.nextLine();
            return true;
        }
    }

    //TODO: Hat diese Lücke eine Bedeutung?


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
                System.arraycopy(schiffeAD, 0, schiffe, 0, schiffeAD.length);
            }

            field.draw(true);

            System.out.println("Gib die Spalte und Zeile für den Startpunkt und die Orientierung für dein Schiff mit der Länge " + schiffe[9 - field.addCounter].getSize() + " ein, " + name + ".");

            int x = scan.nextInt();
            String y = scan.next();
            String orientation = scan.next();

            try{
                yCoord = enemy.stringToYCoord(y.toUpperCase());
            } catch (IllegalArgumentException | NullPointerException e){
                System.out.println("Die Eingabe ist ungültig. Gib etwas anderes ein!");
                return true;
            }

            schiffe[9 - field.addCounter].setShip(x - 1, yCoord, orientation.toUpperCase());

            if(schiffe[9 - field.addCounter].isAllowed(field.getSize())) {
                field.placeShip(x - 1, field.stringToYCoord(y.toUpperCase()), orientation.toUpperCase(), schiffe[9 - field.addCounter].getSize(), schiffe[9- field.addCounter].isArmored());
                field.addCounter++;
                if(field.addCounter == 10) {
                    field.draw(true);
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    System.out.println("Drücke ENTER um fortzufahren!");
                    scan.nextLine();
                    if (enemy.addCounter == 10) {
                        if(Main.status == Main.Status.PICKPHASEAD){Main.status = Main.Status.ATCKAD;}
                        else{Main.status = Main.Status.ATCK;}
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        field.draw(true);
                        return false;
                    } else{ field.draw(true); TimeUnit.MILLISECONDS.sleep(1500); System.out.print("\033[H\033[2J"); System.out.flush(); return false;}
                }
                else{System.out.print("\033[H\033[2J"); System.out.flush(); return true;  }
            } else {
                System.out.println("Bitte wähle eine neue Position, " + name + "!");
                TimeUnit.MILLISECONDS.sleep(1500);
                System.out.print("\033[H\033[2J");
                System.out.flush();
                return true;
            }
        }

        //TODO: Hat diese Lücke eine Bedeutung?

        //TODO: Geht diese Methode schöner?
        else if (Main.status == Main.Status.ATCK || Main.status == Main.Status.ATCKAD) {
            if(switchDraw){
                System.out.print("\033[H\033[2J");
                System.out.flush();
                enemy.draw(false);
                switchDraw = false;
            }

            if(powerup == 0) {
                System.out.println("Gib die Koordinaten an, an die du schießen möchtest, " + name + ".");
                int x = scan.nextInt();
                String y = scan.next();

                try {
                    yCoord = enemy.stringToYCoord(y.toUpperCase());
                } catch (IllegalArgumentException | NullPointerException e) {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    enemy.draw(false);
                    System.out.println("Die Eingabe ist ungültig. Gib etwas anderes ein!");
                    return true;
                }

                if (x >= 1 && x <= enemy.getSize() && yCoord >= 0 && yCoord < enemy.getSize()) { //////////////////Innerhalb des Spielfeldes
                    if(tryShot(enemy, x, yCoord)) {
                        System.out.println("Ey... da haste schon hingeschossen!");
                        return true;
                    }

                    if (enemy.shots[enemy.shotCounter - 1].checkHit(enemy)) { ///////////////////////Getroffen
                        do{
                            System.out.print("\033[H\033[2J");
                            System.out.flush();
                            enemy.draw(false);
                        } while(enemy.showAnimation);

                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        enemy.draw(false);

                        System.out.println("Getroffen!");
                        enemy.checkGameOver(name);
                        return true;
                    } else { /////////////////////////////// nicht getroffen
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        enemy.draw(false);

                        if (Main.status == Main.Status.ATCKAD){
                            getPowerUps();
                        }
                    }

                    TimeUnit.MILLISECONDS.sleep(1500);
                    return false;
                } else {///////////////////////außerhalb des spielfeldes
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
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

    public boolean tryShot(Field enemy, int x, int yCoord) throws InterruptedException {
        for (int i = 0; i < enemy.shotCounter; i++) {
            if (enemy.shots[i].isAt(x - 1, yCoord)) {
                do{
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    enemy.draw(false);
                } while(enemy.showAnimation);

                System.out.print("\033[H\033[2J");
                System.out.flush();
                enemy.draw(false);
                return true;
            }
        }
        Shot schuss = new Shot(x - 1, yCoord, true);

        enemy.shots[enemy.shotCounter] = schuss;
        enemy.shotCounter++;
        return false;
    }

    public void getPowerUps() {
        if (generator.nextInt(100) >= 40) {
            int random = generator.nextInt(100);
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
            //TODO: HashMap?
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
                for(int j = 0; j < enemy.shotCounter; j++){
                    if(enemy.shots[j].isAt(i, enemy.stringToYCoord(coord.toUpperCase()))){
                        clear = false;
                    }
                }
                if(clear) {
                    Shot schuss = new Shot(i, enemy.stringToYCoord(coord.toUpperCase()), true);
                    enemy.shots[enemy.shotCounter] = schuss;
                    enemy.shotCounter++;
                    if(schuss.checkHit(enemy)){
                        do{
                            System.out.print("\033[H\033[2J");
                            System.out.flush();
                            enemy.draw(false);
                        } while(enemy.showAnimation);
                        enemy.checkGameOver(name);
                    }
                }
            }
            System.out.print("\033[H\033[2J");
            System.out.flush();
            enemy.draw(false);
            powerup = 0;
            TimeUnit.MILLISECONDS.sleep(1500);
        }
        else{
            for(int j=0; j < enemy.getSize(); j++){
                clear = true;
                for(int i = 0; i < enemy.shotCounter; i++){
                    if(enemy.shots[i].isAt(Integer.parseInt(coord)-1, j)){
                        clear = false;
                    }
                }
                if(clear) {
                    Shot schuss = new Shot(Integer.parseInt(coord) - 1, j, true);
                    enemy.shots[enemy.shotCounter] = schuss;
                    enemy.shotCounter++;
                    if (schuss.checkHit(enemy)) {
                        do {
                            System.out.print("\033[H\033[2J");
                            System.out.flush();
                            enemy.draw(false);
                        } while (enemy.showAnimation);
                        enemy.checkGameOver(name);
                    }
                }
            }
            System.out.print("\033[H\033[2J");
            System.out.flush();
            enemy.draw(false);
            powerup = 0;
            TimeUnit.MILLISECONDS.sleep(1500);
        }
    }

    //TODO: Geht diese Methode schöner?
    void placeBigShot(int x, String y, Field enemy) throws InterruptedException {
        boolean clear;
        for(int j = enemy.stringToYCoord(y.toUpperCase()) - 1; j <= enemy.stringToYCoord(y.toUpperCase()) + 1; j++){
            for(int i = x-1; i <= x+1; i++){
                clear = true;
                if(i > 0 && i <= enemy.getSize() && j >= 0 && j < enemy.getSize()) {
                    for (int u = 0; u < enemy.shotCounter; u++) {
                        if (enemy.shots[u].isAt(i - 1, j)) {
                            clear = false;
                        }
                    }
                    if (clear) {
                        Shot schuss = new Shot(i - 1, j, true);
                        enemy.shots[enemy.shotCounter] = schuss;
                        enemy.shotCounter++;

                        if (schuss.checkHit(enemy)) {
                            do {
                                System.out.print("\033[H\033[2J");
                                System.out.flush();
                                enemy.draw(false);
                            } while (enemy.showAnimation);
                            enemy.checkGameOver(name);
                        }
                    }
                }
            }
        }
        System.out.print("\033[H\033[2J");
        System.out.flush();
        enemy.draw(false);
        powerup = 0;
        TimeUnit.MILLISECONDS.sleep(1500);
    }
}
