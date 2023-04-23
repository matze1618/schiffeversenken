public class Animation {
    public boolean active;
    private boolean lastFrame = false;
    AnimationBlock[] animationBlocks = new AnimationBlock[30];
    int animationCounter = 0;

    Animation(Ship ship){
        this.active = true;

        if(ship.isHorizontal()) {
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
        } else {
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
        }
    }

    public boolean drawIfOn(Coordinate coordinate){
        boolean isRendered = false;
        if(!lastFrame) {
            for (int i = 0; i < animationCounter; i++) {
                active = false;
                if (animationBlocks[i].frame < 9) {
                    active = true;
                    if (animationBlocks[i].isAt(coordinate) && animationBlocks[i].isActive()) {
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