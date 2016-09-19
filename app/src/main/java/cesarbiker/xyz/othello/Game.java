package cesarbiker.xyz.othello;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by iam39418281 on 9/19/16.
 */
public class Game {

    boolean test = false;
    Paint testPaint = new Paint();
    Paint testPaint2 = new Paint();

    Paint linesPaint = new Paint();
    float lineWidth = 30f;
    float linesSep = 0f;

    private int displayWidth = 0, displayHeight = 0;

    public Game() {
        linesPaint.setColor(Color.BLACK);
        linesPaint.setStrokeWidth(lineWidth);
        linesPaint.setAntiAlias(true);
        testPaint.setColor(Color.RED);
        testPaint2.setColor(Color.BLUE);
    }

    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
        linesSep = this.displayWidth / 8f - (lineWidth / 8f);
    }

    public void setDisplayHeight(int displayHeight) {
        this.displayHeight = displayHeight;
    }

    public void update() {
       test = !test;
    }

    public void render(Canvas canvas) {
        /*if(test) {
            canvas.drawRect(50f, 50f, displayWidth - 50f, displayHeight - 50f, testPaint);
            canvas.drawRect(100f, 100f,  displayWidth - 100f, displayHeight - 100f, testPaint2);
        } else {
            canvas.drawRect(50f, 50f, displayWidth - 50f, displayHeight - 50f, testPaint2);
            canvas.drawRect(100f, 100f, displayWidth - 100f, displayHeight - 100f, testPaint);
        }*/
        drawLines(canvas);
    }

    private void drawLines(Canvas canvas) {
        float halfLine = lineWidth / 2;
        for(int i = 0; i < 9; i++) {
            canvas.drawLine(halfLine + linesSep * i, 0, halfLine + linesSep * i, halfLine + linesSep * 8, linesPaint);
        }
        for(int i = 0; i < 9; i++) {
            canvas.drawLine(0, halfLine + linesSep * i, displayWidth, halfLine + linesSep * i, linesPaint);
        }
    }
}
