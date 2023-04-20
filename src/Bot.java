import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Bot extends Player{
//    private final Shot[] botSchuesse = new Shot[700]; //was initially not final //TODO: still needed?

    //Bot(){};

    Bot(String name) {
        this.name = Main.ANSI_RED + name + Main.ANSI_RESET;
    }

    @Override
    public boolean inputTryExceptions (Field enemy) {
        if(Main.status == Main.Status.PICKPHASE || Main.status == Main.Status.PICKPHASEAD){
            try {
                return randomPlaceShip(enemy);
            } catch (InterruptedException e) {
                System.out.println(name + " hat einen unerwarteten Fehler produziert. Es geht sofort weiter.");
                return false;
            }
        } else {
            try {
                return inputTryBotFailureException(enemy);
            } catch (InterruptedException | BotFailureException e) {
                System.out.println(name + " ist eingeschlafen. Versuche, das Spiel neu zu starten.");
                return false;
            }
        }
    }


    public boolean inputTryBotFailureException(Field enemy) throws InterruptedException, BotFailureException {
        int shipToSink = shipToSinkIndex(enemy);
        if(shipToSink == enemy.ships.length) {
            Random random = new Random();
            return tryShot(enemy, random.nextInt(enemy.getSize()), random.nextInt(enemy.getSize()));
        } else {
            int lastHit = getLastHit(enemy, shipToSink);
            return smartShot(enemy, enemy.shots[lastHit].getxCoord(), enemy.shots[lastHit].getyCoord());
        }
    }

    public int shipToSinkIndex(Field enemy){
        int result = enemy.ships.length;
        for(int i = 0; i < enemy.ships.length; i++) {
            if(!enemy.ships[i].destroyed() && enemy.ships[i].getSize() > enemy.ships[i].getLives()) {
                return i;
            }
        }
        return result;
    }

    public boolean smartShot(Field enemy, int x, int y) throws InterruptedException {
        //TODO: Das geht schöner!
        if(!tryShot(enemy, x - 1, y)) {
            return false;
        }
        if(!tryShot(enemy, x, y - 1)) {
            return false;
        }
        if(!tryShot(enemy, x + 1, y)) {
            return false;
        }
        return tryShot(enemy, x, y + 1);
    }

    @Override
    public boolean randomPlaceShip(Field enemy) throws InterruptedException {
//        int yCoord; //TODO: still needed?
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
            //TODO: Geht das schöner?
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

    public int getLastHit(Field enemy, int shipToSink) throws BotFailureException{
        for(int i = enemy.shotCounter - 1; i >= 0; i--) {
            if(enemy.ships[shipToSink].isAtToBool(enemy.shots[i].getxCoord(), enemy.shots[i].getyCoord())) {
                return i;
            }
        }
        throw new BotFailureException();
    }

    @Override
    public boolean tryShot(Field enemy, int x, int yCoord) throws InterruptedException {
        if(x < 0 || x >= enemy.getSize() || yCoord < 0 || yCoord >= enemy.getSize()) {
            return true;
        }
        for (int i = 0; i < enemy.shotCounter; i++) {
            if (enemy.shots[i].isAt(x, yCoord)) {
                return true;
            }
        }
        Shot schuss = new Shot(x, yCoord, true);
        enemy.shots[enemy.shotCounter] = schuss;
        enemy.shotCounter++;

        for(Ship schiff: enemy.ships) {
            if (schiff.isAtToBool(x, yCoord)) {
                schiff.getHit(enemy);
                enemy.draw(false);
                return true;
            }
        }
        enemy.draw(false);
        return false;
    }
}
