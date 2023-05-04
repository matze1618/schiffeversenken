import java.util.InputMismatchException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PlayerAD extends Player{
    byte powerup = 0; //0 = nichts, 1 = big shot, 2 = line shot, 3 = radar //TODO: Enum?
    public Ship[] ships = {new Ship(6), new Ship(5), new Ship(5), new Ship(4), new Ship(4, true), new Ship(3), new Ship(3), new Ship(3, true), new Ship(2), new Ship(2)};


    @Override
    public boolean inputShot(Field enemy) throws InterruptedException, IllegalArgumentException {
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
                        return false;
                    } else{
                        field.draw(true);
                        TimeUnit.MILLISECONDS.sleep(1500);
                        return false;
                    }
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

        //TODO: Geht diese Methode schöner?
        else if (Main.status == Main.Status.ATCK || Main.status == Main.Status.ATCKAD) {
            if(switchActivePlayer){
                enemy.draw(false);
                switchActivePlayer = false;
            }

            if(powerup == 0) {
                System.out.println("Gib die Koordinaten an, an die du schießen möchtest, " + name + ".");
                int x = scan.nextInt();
                String y = scan.next();

                try {
                    yCoord = stringToYCoord(y.toUpperCase());
                } catch (IllegalArgumentException | NullPointerException e) {
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
                        enemy.draw(false);

                        System.out.println("Getroffen!");
                        enemy.checkGameOver(name);
                        return true;
                    } else { /////////////////////////////// nicht getroffen
                        enemy.draw(false);

                        if (Main.status == Main.Status.ATCKAD){
                            getPowerUps();
                        }
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
                        enemy.checkGameOver(name);
                    }
                }
            }
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
                        enemy.checkGameOver(name);
                    }
                }
            }
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
                            enemy.checkGameOver(name);
                        }
                    }
                }
            }
        }
        enemy.draw(false);
        powerup = 0;
        TimeUnit.MILLISECONDS.sleep(1500);
    }
}
