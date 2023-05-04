public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m"; //TODO: War auskommentiert => Wieso?
    public static final String ANSI_CYAN = "\u001B[36m";
    //public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BG_WHITE = "\u001b[47m";

    enum Status {PICKPHASE, PICKPHASEAD, ATCK, ATCKAD}
    static Status status = Status.PICKPHASE;
    public static boolean gameOver = false;

    public static void main(String[] args) throws InterruptedException {

        Player player1 = new Player(ANSI_GREEN + "Frederik" + ANSI_RESET);
        Player player2 = new Player();

        player1.setGameMode(); //TODO: Nicht in Player definieren?

        if(player1.opponentIsBotTry()) {
            player2 = new Bot("ShipGPT");
        } else if (status == Status.PICKPHASEAD) {
            player2 = new PlayerAD();
            player2.setNameInput(2);
        }

        if(status == Status.PICKPHASEAD && player2 instanceof PlayerAD) {
            player1.field.setSize(0);
            player2.field.setSize(0);
            ((PlayerAD)player2).setFieldSizeCatch(player1.field);
        }


        //TODO: Testing verbessern!!!
        //test(player1, player2);

        while (status == status.PICKPHASE || status == status.PICKPHASEAD){
            while (player1.inputPickTryExceptions(player2.field)) {
            }
            player2.switchActivePlayer = true;
            while (player2.inputPickTryExceptions(player1.field)) {
            }
        }

        while (!gameOver) {
            while (player1.inputShotTryExceptions(player2.field)) {
            }
            player2.switchActivePlayer = true;
            while (player2.inputShotTryExceptions(player1.field)) {
            }
            player1.switchActivePlayer = true;
        }
    }

    static void test(Player player1, Player player2) throws InterruptedException {
        player1.field.placeShipForTest(0, 0, true, player1.ships[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;
        player1.field.placeShipForTest(0, 2, true, player1.ships[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;
        player1.field.placeShipForTest(0, 4, true, player1.ships[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;
        player1.field.placeShipForTest(0, 6, true, player1.ships[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;
        player1.field.placeShipForTest(0, 8, true, player1.ships[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;

        player1.field.placeShipForTest(7, 9, true, player1.ships[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;
        player1.field.placeShipForTest(8, 7, true, player1.ships[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;
        player1.field.placeShipForTest(8, 5, true, player1.ships[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;
        player1.field.placeShipForTest(8, 3, true, player1.ships[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;
        player1.field.placeShipForTest(9, 1, true, player1.ships[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;
        player1.field.draw(true);


        player2.field.placeShipForTest(0, 0, true, player1.ships[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;
        player2.field.placeShipForTest(0, 2, true, player1.ships[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;
        player2.field.placeShipForTest(0, 4, true, player1.ships[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;
        player2.field.placeShipForTest(0, 6, true, player1.ships[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;
        player2.field.placeShipForTest(0, 8, true, player1.ships[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;

        player2.field.placeShipForTest(7, 9, true, player1.ships[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;
        player2.field.placeShipForTest(8, 7, true, player1.ships[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;
        player2.field.placeShipForTest(8, 5, true, player1.ships[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;
        player2.field.placeShipForTest(8, 3, true, player1.ships[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;
        player2.field.placeShipForTest(9, 1, true, player1.ships[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;
        player2.field.draw(true);

        status = Status.ATCK;
    }
}
