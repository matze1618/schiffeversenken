import java.util.Objects;

public class Animation {
    public boolean active;
    private boolean lastFrame = false;
    AnimationBlock[] animationBlocks = new AnimationBlock[30];
    int animationCounter = 0;

//    TODO: Brauche ich spielfeld irgendwo? => (Wo) wird an die Größe des Spielfelds angepasst?
    Animation(Ship ship){ //, Field field){
        this.active = true;

        if(Objects.equals(ship.getOrientation(), "H")) {
            for (int i = 0; i < ship.getSize() + 2; i++) {
                AnimationBlock animationBlock = new AnimationBlock(ship.getXCoord()+i-1, ship.getYCoord() - 1);
                animationBlocks[animationCounter] = animationBlock;
                animationCounter++;

                animationBlock = new AnimationBlock(ship.getXCoord()+i-1, ship.getYCoord());
                animationBlocks[animationCounter] = animationBlock;
                animationCounter++;

                animationBlock = new AnimationBlock(ship.getXCoord()+i-1, ship.getYCoord() + 1);
                animationBlocks[animationCounter] = animationBlock;
                animationCounter++;
            }
        } else if(Objects.equals(ship.getOrientation(), "V")) {
            for (int i = 0; i < ship.getSize() + 2; i++) {
                AnimationBlock animationBlock = new AnimationBlock(ship.getXCoord() - 1, ship.getYCoord() + i - 1);
                animationBlocks[animationCounter] = animationBlock;
                animationCounter++;

                animationBlock = new AnimationBlock(ship.getXCoord(), ship.getYCoord() + i - 1);
                animationBlocks[animationCounter] = animationBlock;
                animationCounter++;

                animationBlock = new AnimationBlock(ship.getXCoord() + 1, ship.getYCoord() + i - 1);
                animationBlocks[animationCounter] = animationBlock;
                animationCounter++;
            }
        } else {
            //TODO: genauere/sprechendere Fehlermeldung!
            System.out.println("Fehler h oder v!");
        }
    }

    public boolean drawIfOn(int x, int y){
        boolean isRendered = false;
        if(!lastFrame) {
            for (int i = 0; i < animationCounter; i++) {
                active = false;
                if (animationBlocks[i].frame < 9) {
                    active = true;
                    if (animationBlocks[i].isAt(x, y) && animationBlocks[i].isActive()) {
                        animationBlocks[i].draw();
                        animationBlocks[i].draw();
                        animationBlocks[i].draw();
                        animationBlocks[i].frame++;
                        isRendered = true;
                    }
                } else {
                    lastFrame = true;
                }
            }
        }
        if(lastFrame){
            active = false;
        }
        return isRendered;
    }
    public boolean isActive() {
        return active;
    }
}