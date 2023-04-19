import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Bot extends Player{
    private final String name;
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
        if(shipToSink == enemy.schiffe.length) {
            Random random = new Random();
            return trySchuss(enemy, random.nextInt(enemy.getWidth()), random.nextInt(enemy.getHeight()));
        } else {
            int lastHit = getLastHit(enemy, shipToSink);
            return smartSchuss(enemy, enemy.schuesse[lastHit].getxCoord(), enemy.schuesse[lastHit].getyCoord());
        }
    }

    public int shipToSinkIndex(Field enemy){
        int result = enemy.schiffe.length;
        for(int i=0; i < enemy.schiffe.length; i++) {
            if(!enemy.schiffe[i].zerstoert() && enemy.schiffe[i].getLaenge() > enemy.schiffe[i].getLeben()) {
                return i;
            }
        }
        return result;
    }

    public boolean smartSchuss(Field enemy, int x, int y) throws InterruptedException {
        //TODO: Das geht schöner!
        if(!trySchuss(enemy, x - 1, y)) {
            return false;
        }
        if(!trySchuss(enemy, x, y - 1)) {
            return false;
        }
        if(!trySchuss(enemy, x + 1, y)) {
            return false;
        }
        return trySchuss(enemy, x, y + 1);
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
            x = random.nextInt(enemy.getWidth());
            y = random.nextInt(enemy.getHeight());
            if(random.nextBoolean()){
                orientation = "H";
            } else {
                orientation = "V";
            }

            schiffe[9 - spielfeld.addCounter].setShip(x - 1, y, orientation.toUpperCase());
            //TODO: Geht das schöner?
            if (spielfeld.isAllowed(schiffe[9 - spielfeld.addCounter])) {
                spielfeld.placeShip(x - 1, y, orientation, schiffe[9 - spielfeld.addCounter].getLaenge(), schiffe[9-spielfeld.addCounter].isArmored());
                spielfeld.addCounter++;
                if (spielfeld.addCounter == 10) {
                    spielfeld.draw(true);
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    System.out.println("Drücke ENTER um fortzufahren!");
                    scan.nextLine();
                    if (enemy.addCounter == 10) {
                        if(Main.status == Main.Status.PICKPHASEAD){Main.status = Main.Status.ATCKAD;}
                        else{Main.status = Main.Status.ATCK;}
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        spielfeld.draw(true);
                    } else {
                        spielfeld.draw(true);
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
        for(int i = enemy.schussCounter - 1; i >= 0; i--) {
            if(enemy.schiffe[shipToSink].isAtToBool(enemy.schuesse[i].getxCoord(), enemy.schuesse[i].getyCoord())) {
                return i;
            }
        }
        throw new BotFailureException();
    }

    @Override
    public boolean trySchuss(Field enemy, int x, int yCoord) throws InterruptedException {
        if(x < 0 || x >= enemy.getWidth() || yCoord < 0 || yCoord >= enemy.getHeight()) {
            return true;
        }
        for (int i = 0; i < enemy.schussCounter; i++) {
            if (enemy.schuesse[i].isAt(x, yCoord)) {
                return true;
            }
        }
        Shot schuss = new Shot(x, yCoord, true);
        enemy.schuesse[enemy.schussCounter] = schuss;
        enemy.schussCounter++;

        for(Ship schiff: enemy.schiffe) {
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
