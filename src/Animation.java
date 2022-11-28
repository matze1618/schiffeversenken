public class Animation {

    public int frame = 0;
    public boolean active;
    private boolean lastFrame = false;

    AnimationBlock[] animationBlocks = new AnimationBlock[30];
    int aniCounter=0;



    Animation(Ship schiff, Field spielfeld){
        this.active = true;

        if(schiff.getOrientation() == "H") {
            for (int i = 0; i < schiff.getLaenge() + 2; i++) {
                AnimationBlock animationBlock = new AnimationBlock(schiff.getXCoord()+i-1, schiff.getYCoord() - 1);
                animationBlocks[aniCounter] = animationBlock;
                aniCounter++;

                animationBlock = new AnimationBlock(schiff.getXCoord()+i-1, schiff.getYCoord());
                animationBlocks[aniCounter] = animationBlock;
                aniCounter++;

                animationBlock = new AnimationBlock(schiff.getXCoord()+i-1, schiff.getYCoord() + 1);
                animationBlocks[aniCounter] = animationBlock;
                aniCounter++;

            }
        }
        else if(schiff.getOrientation() == "V") {
            for (int i = 0; i < schiff.getLaenge() + 2; i++) {
                AnimationBlock animationBlock = new AnimationBlock(schiff.getXCoord() - 1, schiff.getYCoord() + i - 1);
                animationBlocks[aniCounter] = animationBlock;
                aniCounter++;

                animationBlock = new AnimationBlock(schiff.getXCoord(), schiff.getYCoord() + i - 1);
                animationBlocks[aniCounter] = animationBlock;
                aniCounter++;

                animationBlock = new AnimationBlock(schiff.getXCoord() + 1, schiff.getYCoord() + i - 1);
                animationBlocks[aniCounter] = animationBlock;
                aniCounter++;
            }
        }
        else {
            System.out.println("Fehler h oder v!");
        }
    }

    public boolean drawIfOn(int x, int y){
        boolean isRendered = false;
        if(!lastFrame) {
            for (int i = 0; i < aniCounter; i++) {
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
        if(lastFrame){active = false;}
        return isRendered;
    }

    public boolean isActive() {
        return active;
    }
}