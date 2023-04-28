import java.util.Random;
//import java.util.concurrent.TimeUnit;

public class Bot extends Player{
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
            return smartShot(enemy, enemy.getLastHit().getX(), enemy.getLastHit().getY());
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
    public boolean tryShot(Field enemy, int x, int y) throws InterruptedException {
        if(x < 0 || x >= enemy.getSize() || y < 0 || y >= enemy.getSize()) {
            return true;
        }

        for (Shot shot : enemy.shots){
            if (shot.isAt(new Coordinate(x, y))) {
                return true;
            }
        }

        Shot shot = new Shot(x, y, true, enemy);
        enemy.shots.add(shot);

        for(Ship schiff: enemy.ships) {
            if (schiff.isAtToBool(x, y)) {
                schiff.getHit(enemy);
                enemy.draw(false);
                return true;
            }
        }
        enemy.draw(false);
        return false;
    }
}
