
//import java.awt.*;

import java.util.concurrent.TimeUnit;

public class Field {
    private int width = 10;
    private int height = 10;

    boolean showAnimation = false;

    Ship[] schiffe;
    int addCounter = 0;

    Shot[] schuesse;
    int schussCounter = 0;

    Animation[] animations;
    int aniCounter = 0;

    public Field() {
        this.schiffe = new Ship[10];
        this.schuesse = new Shot[150];
        animations = new Animation[60];
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    void draw(boolean showShips) throws InterruptedException {

        char buchstabe = 'A';

        System.out.print("   | 1 ");
        for (int i = 2; i <= width - 1; i++) {
            if (i < 10) {
                System.out.print(" " + i + " ");
            } else {
                System.out.print(" " + i);
            }
        }
        if (width >= 10) {
            System.out.println(" " + width + "|");
        } else {
            System.out.println(" " + width + " |");
        }


        System.out.print("―――+");
        for (int i = 1; i <= width; i++) {
            System.out.print("―――");
        }
        System.out.println("|");


        for (int j = 0; j < height; j++) {
            System.out.print(" " + buchstabe + " \u007C");
            buchstabe++;
            for (int i = 0; i < width; i++) {
                boolean isRendered = false;




                showAnimation = false;
                for(int a = 0; a < aniCounter; a++){
                    if(!isRendered){
                        if(animations[a].isActive()){showAnimation = true;}
                        isRendered = animations[a].drawIfOn(i, j);
                    }
                }





                if(!isRendered) {
                    for (int u = 0; u < addCounter; u++) {
                        if ((showShips && !schiffe[u].zerstoert()) || (!showShips && schiffe[u].zerstoert())) {
                            if ((!showShips && schiffe[u].zerstoert())) {
                                System.out.print(Main.ANSI_RED);
                            }
                            if (schiffe[u].getLaenge() > 1) {
                                switch (schiffe[u].isAt(i, j)) {
                                    case 0:
                                        break;
                                    case 1:
                                        if (schiffe[u].getLaenge() == 5 || schiffe[u].getLaenge() == 3) {
                                            System.out.print(" ▲ ");
                                        } else {
                                            System.out.print(" ▲ ");
                                        }
                                        isRendered = true;
                                        break;
                                    case 2:
                                        if (schiffe[u].getLaenge() == 5 || schiffe[u].getLaenge() == 3) {
                                            System.out.print(" \u2588 ");
                                        } else {
                                            System.out.print(" \u2588 ");
                                        }
                                        isRendered = true;
                                        break;
                                    case 3:
                                        System.out.print(" ╩ ");
                                        isRendered = true;
                                        break;
                                    case 4:
                                        if (schiffe[u].getLaenge() == 5 || schiffe[u].getLaenge() == 3) {
                                            System.out.print("╠■■");
                                        } else {
                                            System.out.print("╠■■");
                                        }
                                        isRendered = true;
                                        break;
                                    case 5:
                                        if (schiffe[u].getLaenge() == 5 || schiffe[u].getLaenge() == 3) {
                                            System.out.print("■■■");
                                        } else {
                                            System.out.print("■■■");
                                        }
                                        isRendered = true;
                                        break;
                                    case 6:
                                        if (schiffe[u].getLaenge() == 5 || schiffe[u].getLaenge() == 3) {
                                            System.out.print("■■▶");
                                        } else {
                                            System.out.print("■■▶");
                                        }
                                        isRendered = true;
                                        break;
                                }
                            } else if (schiffe[u].isAtToBool(i, j)) {
                                System.out.print("╠■▶");
                                isRendered = true;
                            }

                            if ((!showShips && schiffe[u].zerstoert())) {
                                System.out.print(Main.ANSI_RESET);
                            }
                        }
                    }
                }


                for (int o = 0; o < schussCounter; o++) {
                    if (schuesse[o].isAt(i, j) && !isRendered) {
                        for (int u = 0; u < addCounter; u++) {
                            if (schiffe[u].isAtToBool(i, j) && !isRendered && (schiffe[u].getLeben() < schiffe[u].getLaenge() - 1 || (!schiffe[u].isArmored()))) {
                                System.out.print(Main.ANSI_RED + " X " + Main.ANSI_RESET);
                                isRendered = true;
                            } else if (!schuesse[o].getPlacedManually() && !isRendered) {
                                System.out.print(Main.ANSI_CYAN + " X " + Main.ANSI_RESET);
                                isRendered = true;
                            }
                        }
                        if (!isRendered) {
                            System.out.print(" X ");
                            isRendered = true;
                        }
                    }
                }


                if (!isRendered) {
                    System.out.print(Main.ANSI_BLUE + "~~~" + Main.ANSI_RESET);
                }
            }
            System.out.print("\u007C");
            if (Main.status == Main.Status.ATCK) {
                switch (j) {
                    case 0:
                        System.out.print("   Noch zuzerstörende Schiffe:");
                        break;
                    case 2:
                        System.out.print("    ");
                        for (int i = 0; i < shipsAliveByLength(5); i++) {
                            System.out.print("╠■■■■■■■■■■■■■▶ ");
                        }
                        break;
                    case 4:
                        System.out.print("    ");

                        for (int i = 0; i < shipsAliveByLength(4); i++) {
                            System.out.print("╠■■■■■■■■■■▶ ");
                        }
                        break;
                    case 6:
                        System.out.print("    ");

                        for (int i = 0; i < shipsAliveByLength(3); i++) {
                            System.out.print("╠■■■■■■■▶ ");
                        }
                        break;
                    case 8:
                        System.out.print("    ");

                        for (int i = 0; i < shipsAliveByLength(2); i++) {
                            System.out.print("╠■■■■▶ ");
                        }
                        for (int i = 0; i < shipsAliveByLength(1); i++) {
                            System.out.print("╠■▶ ");
                        }
                        break;
                }
            }
            if (Main.status == Main.Status.ATCKAD) {
                switch (j) {
                    case 0:
                        System.out.print("   Noch zuzerstörende Schiffe:");
                        break;
                    case 2:
                        System.out.print("    ");
                        for (int i = 0; i < shipsAliveByLength(6); i++) {
                            System.out.print("╠■■■■■■■■■■■■■■■■▶ ");
                        }
                        break;
                    case 4:
                        System.out.print("    ");

                        for (int i = 0; i < shipsAliveByLength(5); i++) {
                            System.out.print("╠■■■■■■■■■■■■■▶ ");
                        }
                        break;
                    case 6:
                        System.out.print("    ");

                        for (int i = 0; i < shipsAliveByLength(4); i++) {
                            if (i == 0 && !schiffe[6].zerstoert()) {
                                System.out.print(Main.ANSI_BG_WHITE + "╠■■■■■■■■■■▶" + Main.ANSI_RESET + " ");
                                i++;
                            }
                            if(i < shipsAliveByLength(4)) {
                                System.out.print("╠■■■■■■■■■■▶ ");
                            }
                        }
                        break;
                    case 8:
                        System.out.print("    ");

                        for (int i = 0; i < shipsAliveByLength(3); i++) {
                            if (i == 0 && !schiffe[4].zerstoert()) {
                                System.out.print(Main.ANSI_BG_WHITE + "╠■■■■■■■▶" + Main.ANSI_RESET + " ");
                                i++;
                            }
                            if(i < shipsAliveByLength(3)) {
                                System.out.print("╠■■■■■■■▶ ");
                            }
                        }
                        break;
                    case 10:
                        System.out.print("    ");
                        for (int i = 0; i < shipsAliveByLength(2); i++) {
                            System.out.print("╠■■■■▶ ");
                        }
                        break;
                }
            }
            System.out.println();
        }
        System.out.print("―――+");
        for (int i = 1; i <= width; i++) {
            System.out.print("―――");
        }
        System.out.println("|");
        if (showAnimation) {
            TimeUnit.MILLISECONDS.sleep(220);
        }
    }










    void placeShip(int x, int y, String orientation, int length, boolean armored) {
        schiffe[addCounter] = new Ship(x, y, length, orientation, armored);
    }

    int getWidth () {
        return width;
    }

    int getHeight () {
        return height;
    }

    public boolean isClear (Ship schiff){
        for (int i = 0; i < addCounter; i++) {
            if (schiffe[i].isBlocked(schiff)) {
                return false;
            }
        }
        return true;
    }

    public int stringToYCoord(String character){
        switch (character) {
            case ("A"):
                return 0;
            case ("B"):
                return 1;
            case ("C"):
                return 2;
            case ("D"):
                return 3;
            case ("E"):
                return 4;
            case ("F"):
                return 5;
            case ("G"):
                return 6;
            case ("H"):
                return 7;
            case ("I"):
                return 8;
            case ("J"):
                return 9;
            case ("K"):
                return 10;
            case ("L"):
                return 11;
            case ("M"):
                return 12;
            case ("N"):
                return 13;
            case ("O"):
                return 14;
            case ("P"):
                return 15;
            case ("Q"):
                return 16;
            case ("R"):
                return 17;
            case ("S"):
                return 18;
            case ("T"):
                return 19;
            case ("U"):
                return 20;
            case ("V"):
                return 21;
            case ("W"):
                return 22;
            case ("X"):
                return 23;
            case ("Y"):
                return 24;
            case ("Z"):
                return 25;
        }
        throw new IllegalArgumentException();
    }

    boolean isAllowed (Ship schiff){
        if ("H".equalsIgnoreCase(schiff.getOrientation()) && ((schiff.getXCoord() >= 0) && (schiff.getXCoord() <= width) && (schiff.getXCoord() + schiff.getLaenge() <= width) && (schiff.getYCoord() >= 0) && (schiff.getYCoord() < height))) {
            return isClear(schiff);
        } else if("V".equalsIgnoreCase(schiff.getOrientation()) && ((schiff.getYCoord() >= 0) && (schiff.getYCoord() <= height) && (schiff.getYCoord() + schiff.getLaenge() <= height) && (schiff.getXCoord() >= 0) && (schiff.getXCoord() < width))) {
            return isClear(schiff);
        } else {
            return false;
        }
    }


    public void checkGameOver(String winnerName) {
        if (!Main.gameOver) {
            Main.gameOver = true;
            for (int i = 0; i < addCounter; i++) {
                if (!schiffe[i].zerstoert()) {
                    Main.gameOver = false;
                }
            }
            if (Main.gameOver) {
                System.out.println(winnerName + " hat gewonnen!");
                System.exit(0);
            }
        }
    }


    int shipsAliveByLength(int laenge){
        int counter = 0;
        for(int i = 0; i < schiffe.length; i++){
            if(schiffe[i].getLaenge() == laenge && !schiffe[i].zerstoert()){
                counter++;
            }
        }
        return counter;
    }

    void shotsInBarrier() {
        for(int i=0; i < schiffe.length; i++) {
            if(schiffe[i].zerstoert()) {
                if("H".equalsIgnoreCase(schiffe[i].getOrientation())) {
                    placeShotAutomated(schiffe[i].getXCoord() - 1, schiffe[i].getYCoord());
                    placeShotAutomated(schiffe[i].getXCoord() + schiffe[i].getLaenge(), schiffe[i].getYCoord());
                    for (int j=0; j < schiffe[i].getLaenge(); j++){
                        placeShotAutomated(schiffe[i].getXCoord() + j, schiffe[i].getYCoord() + 1);
                        placeShotAutomated(schiffe[i].getXCoord() + j, schiffe[i].getYCoord() - 1);
                    }
                } else {
                    placeShotAutomated(schiffe[i].getXCoord(), schiffe[i].getYCoord() - 1);
                    placeShotAutomated(schiffe[i].getXCoord(), schiffe[i].getYCoord() + schiffe[i].getLaenge());
                    for (int j=0; j < schiffe[i].getLaenge(); j++){
                        placeShotAutomated(schiffe[i].getXCoord() + 1, schiffe[i].getYCoord() + j);
                        placeShotAutomated(schiffe[i].getXCoord() - 1, schiffe[i].getYCoord() + j);
                    }
                }

            }
        }
    }

    boolean placeShotAutomated(int x, int y) {
        if(x >= 0 && x <= width - 1 && y >= 0 && y <= height - 1) {
            for(int i=0; i < schussCounter; i++) {
                if(schuesse[i].isAt(x, y)) {
                    return false;
                }
            }
            schuesse[schussCounter] = new Shot(x, y, false);
            schussCounter++;
        }
        return false;
    }
}
//new Schuss(x, y, false);
