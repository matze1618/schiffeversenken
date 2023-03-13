import java.util.Random;

public class AnimationBlock {
    private int posX;
    private int posY;
    private boolean active;
    int frame;
    Random generator;

    AnimationBlock(int x, int y){
        this.frame = 0;
        this.active = true;
        this.posX = x;
        this.posY = y;

        generator = new Random();
    }

    public void draw(){
        int randomColor = generator.nextInt(3);
        int randomSymbol = generator.nextInt(20); //was 19
        switch (randomColor) {
            case 0, 1 -> System.out.print(Main.ANSI_YELLOW);
            case 2 -> System.out.print(Main.ANSI_RED);
        }

        switch (randomSymbol) {
            case 0 -> System.out.print("؎" + Main.ANSI_RESET);
            case 1 -> System.out.print("؏" + Main.ANSI_RESET);
            case 2 -> System.out.print("߶" + Main.ANSI_RESET);
            case 3 -> System.out.print("༚" + Main.ANSI_RESET);
            case 4 -> System.out.print("༜" + Main.ANSI_RESET);
            case 5 -> System.out.print("࿀" + Main.ANSI_RESET);
            case 6 -> System.out.print("༞" + Main.ANSI_RESET);
            case 7, 8 -> System.out.print("࿏" + Main.ANSI_RESET);
            case 9 -> System.out.print("℺" + Main.ANSI_RESET);
            case 10 -> System.out.print("⅏" + Main.ANSI_RESET);
            case 11 -> System.out.print("↜" + Main.ANSI_RESET);
            case 12 -> System.out.print("↟" + Main.ANSI_RESET);
            case 13 -> System.out.print("↭" + Main.ANSI_RESET);
            case 14 -> System.out.print("↯" + Main.ANSI_RESET);
            case 15 -> System.out.print("⇝" + Main.ANSI_RESET);
            case 16 -> System.out.print("⌀" + Main.ANSI_RESET);
            case 17 -> System.out.print("⌘" + Main.ANSI_RESET);
            case 18 -> System.out.print("⌔" + Main.ANSI_RESET);
            case 19 -> System.out.print("⌾" + Main.ANSI_RESET);
        }
        if(frame > 27){
            active = false;
        }
    }

    public boolean isAt(int x, int y){
        return (x == posX && y == posY);
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }


    public void change(int x, int y){
        posX = x;
        posY = y;
    }

    public boolean isActive(){
        return active;
    }
}
