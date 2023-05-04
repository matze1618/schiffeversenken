import java.util.Random;
//import java.util.concurrent.TimeUnit;

public class Bot extends PlayerAD{
    private Shot lastShot;
    private Shot lastHit;
    private Coordinate baseOfCurrentShip;
    private boolean shipFound = false;

    //TODO: Wird wahrscheinlich irgendwo fälschlicherweise nicht zurück auf false gesetzt!
    private boolean directionFound = false;
    private char direction = 'l';
    private boolean switchDirection = false;

    Random random = new Random();
    Bot(String name) {
        this.name = Main.ANSI_RED + name + Main.ANSI_RESET;
    }

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


    public boolean inputTryBotFailureException(Field enemy) throws InterruptedException, BotFailureException {
        if(shipFound) {
            return tryShotWithShipFound(enemy);
        } else {
            return tryRandomShot(enemy, new Coordinate(random.nextInt(enemy.getSize()), random.nextInt(enemy.getSize())));
        }
    }

    public boolean tryShotWithShipFound(Field enemy) throws BotFailureException {
        if (!directionFound){
            return tryFindDirection(enemy);
        } else {
            return tryShotWithDirection(enemy);
        }
    }

    private boolean tryFindDirection(Field enemy) throws BotFailureException {
        switch (direction){
            case 'l':
                if (!isValidTry(new Coordinate(lastHit.getX() - 1, lastHit.getY()), enemy)) {
                    direction = 'u';
                    return true;
                }
                if(trySmartShot(enemy, lastHit.getX() - 1, lastHit.getY())){
                    directionFound = true;
                    return true;
                } else {
                    direction = 'u';
                    return false;
                }
            case 'u':
                if (!isValidTry(new Coordinate(lastHit.getX(), lastHit.getY() - 1), enemy)) {
                    direction = 'r';
                    return true;
                }
                if (trySmartShot(enemy, lastHit.getX(), lastHit.getY() - 1)) {
                    directionFound = true;
                    return true;
                } else {
                    direction = 'r';
                    return false;
                }
            case 'r':
                if (!isValidTry(new Coordinate(lastHit.getX() + 1, lastHit.getY()), enemy)) {
                    direction = 'd';
                    return true;
                }
                if (trySmartShot(enemy, lastHit.getX() + 1, lastHit.getY())) {
                    directionFound = true;
                    return true;
                } else {
                    direction = 'd';
                    return false;
                }
            case 'd':
                if (!isValidTry(new Coordinate(lastHit.getX(), lastHit.getY() + 1), enemy)) {
                    throw new BotFailureException();
                }
                if (trySmartShot(enemy, lastHit.getX(), lastHit.getY() + 1)) {
                    directionFound = true;
                    return true;
                } else {
                    throw new BotFailureException();
                }
            default:
                throw new BotFailureException();
        }
    }

    private boolean tryShotWithDirection(Field enemy) throws BotFailureException {
        if(!switchDirection) {
            switch (direction) {
                case 'l':
                    if (!isValidTry(new Coordinate(lastHit.getX() - 1, lastHit.getY()), enemy)) {
                        direction = 'r';
                        return true;
                    }
                    return trySmartShot(enemy, lastHit.getX() - 1, lastHit.getY());
                case 'u':
                    if (!isValidTry(new Coordinate(lastHit.getX(), lastHit.getY() - 1), enemy)) {
                        direction = 'd';
                        return true;
                    }
                    return trySmartShot(enemy, lastHit.getX(), lastHit.getY() - 1);
                case 'r':
                    if (!isValidTry(new Coordinate(lastHit.getX() + 1, lastHit.getY()), enemy)) {
                        direction = 'l';
                        return true;
                    }
                    return trySmartShot(enemy, lastHit.getX() + 1, lastHit.getY());
                case 'd':
                    if (!isValidTry(new Coordinate(lastHit.getX(), lastHit.getY() + 1), enemy)) {
                        direction = 'u';
                        return true;
                    }
                    return trySmartShot(enemy, lastHit.getX(), lastHit.getY() + 1);
                default:
                    throw new BotFailureException();
            }
        } else {
            switch (direction){
                case 'l':
                    if (!isValidTry(new Coordinate(baseOfCurrentShip.getX() - 1, baseOfCurrentShip.getY()), enemy)) {
                        throw new BotFailureException();
                    }
                    return trySmartShot(enemy, baseOfCurrentShip.getX() - 1, baseOfCurrentShip.getY());
                case 'u':
                    if (!isValidTry(new Coordinate(baseOfCurrentShip.getX(), baseOfCurrentShip.getY() - 1), enemy)) {
                        throw new BotFailureException();
                    }
                    return trySmartShot(enemy, baseOfCurrentShip.getX(), baseOfCurrentShip.getY() - 1);
                case 'r':
                    if (!isValidTry(new Coordinate(baseOfCurrentShip.getX() + 1, baseOfCurrentShip.getY()), enemy)) {
                        throw new BotFailureException();
                    }
                    return trySmartShot(enemy, baseOfCurrentShip.getX() + 1, baseOfCurrentShip.getY());
                case 'd':
                    if (!isValidTry(new Coordinate(baseOfCurrentShip.getX(), baseOfCurrentShip.getY() + 1), enemy)) {
                        throw new BotFailureException();
                    }
                    return trySmartShot(enemy, baseOfCurrentShip.getX(), baseOfCurrentShip.getY() + 1);
                default:
                    throw new BotFailureException();
            }

        }
    }

    public boolean tryRandomShot(Field enemy, Coordinate coordinate) {
        if (!isValidTry(coordinate, enemy)) {
            return true;
        }

        Shot shot = new Shot(coordinate, true, enemy);
        enemy.shots.add(shot);
        lastShot = shot;
        enemy.draw(false);

        if (shot.isHit()){
            switchDirection = false;
            for(Ship ship: enemy.ships) {
                if (ship.isAtToBool(coordinate)) {
                    if(!ship.destroyed()) {
                        lastHit = shot;
                        baseOfCurrentShip = lastHit.getPosition();
                        shipFound = true;
                    } else {
                        enemy.checkGameOver(name);
                        shipFound = false;
                        directionFound = false;
                        direction = 'l';
                    }
                    enemy.draw(false);
                    return true;
                }
            }
        }
        return false;
    }

    //here false indicates a miss or outOfBounds
    private boolean trySmartShot(Field enemy, int x, int y) {
        Coordinate coordinate = new Coordinate(x, y);

        if (!coordinate.isValid(enemy.getSize())) {
            return false;
        }

        Shot shot = new Shot(coordinate, true, enemy);
        enemy.shots.add(shot);
        lastShot = shot;
        enemy.draw(false);

        if (shot.isHit()) {
            switchDirection = false;
            //TODO: über Instanzvariable Ship currentShip lösen
            for(Ship ship: enemy.ships) {
                if (ship.isAtToBool(coordinate)) {
                    lastHit = shot;
                    if(ship.destroyed()) {
                        enemy.checkGameOver(name);
                        shipFound = false;
                        directionFound = false;
                        direction = 'l';
                    }
                    enemy.draw(false);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isValidTry(Coordinate coordinate, Field enemy){
        if(!coordinate.isValid(enemy.getSize())) {
            switchDirection = true;
            return false;
        }

        for (Shot shot : enemy.shots){
            if (shot.isAt(coordinate)) {
                switchDirection = true;
                return false;
            }
        }
        //TODO: Ist das richtig?
        switchDirection = false;
        return true;
    }
}
