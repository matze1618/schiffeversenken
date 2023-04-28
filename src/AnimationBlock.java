import java.sql.Array;
import java.util.HashMap;
import java.util.Random;

public class AnimationBlock {
    private final Coordinate position;
    private boolean active;
    int frame;
    Random generator;

    AnimationBlock(int x, int y){
        this.frame = 0;
        this.active = true;
        this.position = new Coordinate(x, y);

        generator = new Random();
    }

    public void draw(){
        if (generator.nextInt(3) < 2) {
            System.out.print(Main.ANSI_YELLOW);
        } else {
            System.out.print(Main.ANSI_RED);
        }

        HashMap<Integer, String> randomSymbols = new HashMap<>();
        String[] animationArray = {"؎", "؏", "#", "༚༚", "࿀", "༞", "\"", "℺", "?", "§", "%", "&", "\\", ">", "<", "⌘", "°", "^"};
        randomSymbols.put(0, "؎");
        randomSymbols.put(1, "؏");
        randomSymbols.put(2, "#");
        randomSymbols.put(3, "༚༚");
        randomSymbols.put(4, "༜");
        randomSymbols.put(5, "࿀");
        randomSymbols.put(6, "༞");
        randomSymbols.put(7, "=");
        randomSymbols.put(8, "\"");
        randomSymbols.put(9, "℺");
        randomSymbols.put(10, "?");
        randomSymbols.put(11, "§");
        randomSymbols.put(12, "%");
        randomSymbols.put(13, "&");
        randomSymbols.put(14, "\\");
        randomSymbols.put(15, ">");
        randomSymbols.put(16, "<");
        randomSymbols.put(17, "⌘");
        randomSymbols.put(18, "°");
        randomSymbols.put(19, "^");

        System.out.print(randomSymbols.get(generator.nextInt(20)) + Main.ANSI_RESET);

        //TODO: Wieso?
        if(frame > 27){
            active = false;
        }
    }

    public boolean isAt(Coordinate coordinate){
        return position.equalTo(coordinate);
    }

    public boolean isActive(){
        return active;
    }
}
