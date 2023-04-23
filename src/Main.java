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
//        String[] signsArray = {"\\", ">", "<", "°", "^", "⌘"};
//
//        for (String sign : signsArray) {
//            for (int i = 0; i < 10; i++){
//                System.out.print(sign);
//            }
//            System.out.println();
//            for (int i = 0; i < 10; i++){
//                System.out.print("~");
//            }
//            System.out.println();
//        }

        Player player1 = new Player(ANSI_GREEN + "Frederik" + ANSI_RESET);
        Player player2;

        if(player1.opponentIsBotTry()) {
            player2 = new Bot("Robby");
        } else {
            player2 = new Player();
            player2.setNameInput(2);
        }

        player1.setGameMode(); //TODO: Nicht in Player definieren?

        if(status == Status.PICKPHASEAD) {
            player1.field.setSize(0);
            player2.field.setSize(0);
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
        player1.field.placeShip(0, 0, true, player1.ships[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;
        player1.field.placeShip(0, 2, true, player1.ships[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;
        player1.field.placeShip(0, 4, true, player1.ships[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;
        player1.field.placeShip(0, 6, true, player1.ships[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;
        player1.field.placeShip(0, 8, true, player1.ships[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;

        player1.field.placeShip(7, 9, true, player1.ships[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;
        player1.field.placeShip(8, 7, true, player1.ships[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;
        player1.field.placeShip(8, 5, true, player1.ships[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;
        player1.field.placeShip(8, 3, true, player1.ships[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;
        player1.field.placeShip(9, 1, true, player1.ships[9 - player1.field.addCounter].getSize(), false);
        player1.field.addCounter++;
        player1.field.draw(true);


        player2.field.placeShip(0, 0, true, player1.ships[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;
        player2.field.placeShip(0, 2, true, player1.ships[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;
        player2.field.placeShip(0, 4, true, player1.ships[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;
        player2.field.placeShip(0, 6, true, player1.ships[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;
        player2.field.placeShip(0, 8, true, player1.ships[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;

        player2.field.placeShip(7, 9, true, player1.ships[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;
        player2.field.placeShip(8, 7, true, player1.ships[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;
        player2.field.placeShip(8, 5, true, player1.ships[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;
        player2.field.placeShip(8, 3, true, player1.ships[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;
        player2.field.placeShip(9, 1, true, player1.ships[9 - player2.field.addCounter].getSize(), false);
        player2.field.addCounter++;
        player2.field.draw(true);

        status = Status.ATCK;
    }
}
