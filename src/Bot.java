import java.util.HashMap;
import java.util.Random;

public class Bot extends PlayerAD{
    private Shot lastShot;
    private Shot lastHit;
    private Shot firstHitOnCurrentShip;
    private boolean directionKnown = false;
    private char direction = 'l';
    private HashMap<Character, Coordinate> directionToOffset = new HashMap<>();

    Random random = new Random();

    //TODO: Make Field enemy a class variable as its used almost everywhere anyway!
    Bot(String name) {
        this.name = Main.ANSI_RED + name + Main.ANSI_RESET;
        setOffsetMap();
    }

    private void setOffsetMap() {
        directionToOffset.put('l', new Coordinate(-1, 0));
        directionToOffset.put('u', new Coordinate(0, -1));
        directionToOffset.put('r', new Coordinate(1, 0));
        directionToOffset.put('d', new Coordinate(0, 1));
    }

    //TODO: What happens here?
    @Override
    public boolean inputShotTryExceptions(Field enemy) {
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

    @Override
    public boolean inputPickTryExceptions(Field enemy) throws InterruptedException {
        return randomPlaceShip(enemy);
    }

    private boolean inputTryBotFailureException(Field enemy) throws InterruptedException, BotFailureException {
        if(lastHit == null || enemy.shipAt(lastHit.getPosition()).isDestroyed()) {
            directionKnown = false;
            return shootRandomly(enemy);
        } else if (directionKnown) {
            if (!lastShot.isHit()) {
                reverseDirection();
            }
            return continueInCurrentDirection(enemy);
        } else {
            return findDirection(enemy);
        }
    }

    private boolean shootRandomly(Field enemy) {
        Coordinate validCoordinateForNewShot = getValidCoordinateForNewShot(enemy);
        Shot shot = new Shot(validCoordinateForNewShot, true, enemy);
        enemy.shots.add(shot);
        lastShot = shot;

        enemy.draw(false);

        if (shot.isHit()) {
            lastHit = shot;
            firstHitOnCurrentShip = shot;
            return true;
        }
        return false;
    }

    private Coordinate getValidCoordinateForNewShot(Field enemy) {
        boolean foundValidCoodinate = false;
        int randomX;
        int randomY;
        Coordinate resultCoordinate = new Coordinate(0,0);

        while (!foundValidCoodinate) {
            randomX = random.nextInt(enemy.getSize());
            randomY = random.nextInt(enemy.getSize());
            resultCoordinate = new Coordinate(randomX, randomY);
            if (!enemy.hasShotsAt(resultCoordinate)) {
                foundValidCoodinate = true;
            }
        }
        return resultCoordinate;
    }

    private boolean continueInCurrentDirection(Field enemy) {
        Shot shot = new Shot(lastHit.getPosition().withOffset(directionToOffset.get(direction)), true, enemy);
        enemy.shots.add(shot);
        lastShot = shot;

        enemy.draw(false);

        if (shot.isHit()) {
            lastHit = shot;
            return true;
        } else {
            return false;
        }
    }

    private void reverseDirection() {
        switch (direction) {
            case 'l' -> direction = 'r';
            case 'u' -> direction = 'd';
            case 'r' -> direction = 'l';
            case 'd' -> direction = 'u';
        }

        lastHit = firstHitOnCurrentShip;
    }

    private boolean findDirection(Field enemy) {
        while (!isValidTry(enemy)) {
            incrementDirection();
        }

        Shot shot = new Shot(lastHit.getPosition().withOffset(directionToOffset.get(direction)), true, enemy);
        enemy.shots.add(shot);
        lastShot = shot;

        enemy.draw(false);

        if (shot.isHit()) {
            lastHit = shot;
            directionKnown = true;
            return true;
        }
        return false;
    }

    private boolean isValidTry(Field enemy) {
        return (!enemy.hasShotsAt(lastHit.getPosition().withOffset(directionToOffset.get(direction))) && lastHit.getPosition().withOffset(directionToOffset.get(direction)).isWithinField(enemy.getSize())); //TODO: Can I make this sleaker?
    }

    private void incrementDirection() {
        switch (direction) {
            case 'l' -> direction = 'u';
            case 'u' -> direction = 'r';
            case 'r' -> direction = 'd';
            case 'd' -> direction = 'l';
        }
    }
}
