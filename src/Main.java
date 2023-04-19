public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    //public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    //public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    //public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BG_WHITE = "\u001b[47m";

    enum Status {PICKPHASE, PICKPHASEAD, ATCK, ATCKAD}
    static Status status = Status.PICKPHASE;
    public static boolean gameOver = false;

    public static void main(String[] args) throws InterruptedException {
        Player player1 = new Player(ANSI_GREEN + "Frederik" + ANSI_RESET);
        //Player player2;

        //player1.setNameInput(1);
        //player2.setNameInput(2);

        Player player2;

        if(player1.opponentIsBotTry()) {
            player2 = new Bot("Robby");
        } else {
            player2 = new Player(ANSI_RED + "Mattis" + ANSI_RESET);
        }


        player1.setGameMode();

        if(status == Status.PICKPHASEAD) {
            player1.field.setHeight(0);
            player1.field.setWidth(0);
            player2.field.setHeight(0);
            player2.field.setWidth(0);
            player1.setSpielfeldSizeCatch(player2.field);
        }


        //TODO: Testing verbessern!!!
        //test(player1, player2);

        while (!gameOver) {
            while (player1.inputTryExceptions(player2.field)) {
            }
            player2.switchDraw = true;
            while (player2.inputTryExceptions(player1.field)) {
            }
            player1.switchDraw = true;
        }
    }

    static void test(Player player1, Player player2) throws InterruptedException {
        player1.field.placeShip(0, 0, "H", player1.schiffe[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;
        player1.field.placeShip(0, 2, "H", player1.schiffe[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;
        player1.field.placeShip(0, 4, "H", player1.schiffe[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;
        player1.field.placeShip(0, 6, "H", player1.schiffe[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;
        player1.field.placeShip(0, 8, "H", player1.schiffe[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;

        player1.field.placeShip(7, 9, "H", player1.schiffe[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;
        player1.field.placeShip(8, 7, "H", player1.schiffe[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;
        player1.field.placeShip(8, 5, "H", player1.schiffe[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;
        player1.field.placeShip(8, 3, "H", player1.schiffe[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;
        player1.field.placeShip(9, 1, "H", player1.schiffe[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;
        player1.field.draw(true);


        player2.field.placeShip(0, 0, "H", player1.schiffe[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;
        player2.field.placeShip(0, 2, "H", player1.schiffe[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;
        player2.field.placeShip(0, 4, "H", player1.schiffe[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;
        player2.field.placeShip(0, 6, "H", player1.schiffe[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;
        player2.field.placeShip(0, 8, "H", player1.schiffe[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;

        player2.field.placeShip(7, 9, "H", player1.schiffe[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;
        player2.field.placeShip(8, 7, "H", player1.schiffe[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;
        player2.field.placeShip(8, 5, "H", player1.schiffe[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;
        player2.field.placeShip(8, 3, "H", player1.schiffe[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;
        player2.field.placeShip(9, 1, "H", player1.schiffe[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;
        player2.field.draw(true);

        status = Status.ATCK;
    }
}
