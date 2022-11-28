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
    };

    public void draw(){
        int randomColor = generator.nextInt(3);
        int randomSymbol = generator.nextInt(19);
        switch(randomColor){
            case 0:
                System.out.print(Main.ANSI_YELLOW);
                break;
            case 1:
                System.out.print(Main.ANSI_YELLOW);
                break;
            case 2:
                System.out.print(Main.ANSI_RED);
                break;
        }

        switch(randomSymbol){
            case 0:
                System.out.print("؎" + Main.ANSI_RESET);
                break;
            case 1:
                System.out.print("؏" + Main.ANSI_RESET);
                break;
            case 2:
                System.out.print("߶" + Main.ANSI_RESET);
                break;
            case 3:
                System.out.print("༚" + Main.ANSI_RESET);
                break;
            case 4:
                System.out.print("༜" + Main.ANSI_RESET);
                break;
            case 5:
                System.out.print("࿀" + Main.ANSI_RESET);
                break;
            case 6:
                System.out.print("༞" + Main.ANSI_RESET);
                break;
            case 7:
                System.out.print("࿏" + Main.ANSI_RESET);
                break;
            case 8:
                System.out.print("࿏" + Main.ANSI_RESET);
                break;
            case 9:
                System.out.print("℺" + Main.ANSI_RESET);
                break;
            case 10:
                System.out.print("⅏" + Main.ANSI_RESET);
                break;
            case 11:
                System.out.print("↜" + Main.ANSI_RESET);
                break;
            case 12:
                System.out.print("↟" + Main.ANSI_RESET);
                break;
            case 13:
                System.out.print("↭" + Main.ANSI_RESET);
                break;
            case 14:
                System.out.print("↯" + Main.ANSI_RESET);
                break;
            case 15:
                System.out.print("⇝" + Main.ANSI_RESET);
                break;
            case 16:
                System.out.print("⌀" + Main.ANSI_RESET);
                break;
            case 17:
                System.out.print("⌘" + Main.ANSI_RESET);
                break;
            case 18:
                System.out.print("⌔" + Main.ANSI_RESET);
                break;
            case 19:
                System.out.print("⌾" + Main.ANSI_RESET);
                break;
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
