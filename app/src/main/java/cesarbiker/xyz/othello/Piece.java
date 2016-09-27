package cesarbiker.xyz.othello;

import android.util.Log;

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
    private boolean wrong;
    private int sumTime;
    public boolean draw = true;

    public Piece(int x, int y) {
        Random random = new Random();
        this.x = x;
        this.y = y;
        //sumAngle = random.nextFloat() * 30f + 20f;
        sumAngle = 30;
        if(Game.animationSpeed != 0)
            sumAngle = sumAngle / Game.animationSpeed;
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
            wrong = false;
        }
        else if(player == 3 || player == 0) {
            this.player = player;
            wrong = true;
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
        if(wrong) {
            sumTime++;
            if(sumTime > 4)
                draw = false;
            if(sumTime > 8) {
                draw = true;
                sumTime = 0;
            }
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
