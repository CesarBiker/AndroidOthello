package cesarbiker.xyz.othello;

import java.util.Random;

/**
 * Created by iam39418281 on 9/21/16.
 */
public class Piece {
    private int player = 0;
    private float[] center = new float[2];
    private boolean animate = false;
    private boolean isChanged = false;
    private float angle = 0f;
    private float sumAngle = 0f;
    private int x = -1, y = -1;

    public Piece(int x, int y) {
        Random random = new Random();
        this.x = x;
        this.y = y;
        sumAngle = random.nextFloat() * 30f + 20f;
    }

    public int[] getField() {
        return new int[]{x,y};
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        if(player == 2 || player == 1) {
            this.player = player;
            isChanged = true;
            animate = true;
            angle = 0f;
        }
    }

    public float[] getCenter() {
        float sep = Game.lineWidth / 2f + Game.linesSep / 2f;
        center[0] = sep + Game.linesSep * x;
        center[1] = sep + Game.linesSep * y;
        return center;
    }

    public void setCenter(float[] center) {
        if(center[0] >= 0 && center[0] <= Game.displayWidth && center[1] >= 0 && center[1] <= Game.displayWidth)
            this.center = center;
    }

    public void update() {
        if(animate)
            angle += sumAngle;
            if(angle >= 360f) {
                animate = false;
                isChanged = false;
            }
    }

    public float getAngle() {
        return angle;
    }

    public boolean shouldAnimate() {
        return animate;
    }

    public boolean hasChanged() {
        return isChanged;
    }

    public void setAnimate(boolean animate) {
        if(!animate)
            isChanged = false;
        this.animate = animate;
    }
}
