package cesarbiker.xyz.othello;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;

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
    private float fieldWidth = 0f;

    Canvas gCanvas;

    ArrayList<float[]> circles = new ArrayList<>();

    public Game() {
        linesPaint.setColor(Color.BLACK);
        linesPaint.setStrokeWidth(lineWidth);
        linesPaint.setAntiAlias(true);
        testPaint.setColor(Color.RED);
        testPaint2.setColor(Color.BLUE);
    }

    public void setCanvas(Canvas canvas) {
        gCanvas = canvas;
    }

    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
        fieldWidth = this.displayWidth / 8f;
        linesSep = this.displayWidth / 8f - (lineWidth / 8f);
    }

    public void setDisplayHeight(int displayHeight) {
        this.displayHeight = displayHeight;
    }

    public void update() {
       test = !test;
    }

    public void render() {
        drawLines();
        for (float[] circle : circles) {
            gCanvas.drawCircle(circle[0], circle[1], fieldWidth / 2 - lineWidth / 2, testPaint2);
        }
    }

    public void touch(float x, float y) {
        int coord[] = new int[2];
        getFieldFromCoord(x, y, coord);
        Log.d("TEST", "X: " + x + " Y: " + y);
        Log.d("TEST", "CX: " + coord[0] + " CY: " + coord[1]);
    }

    public boolean getFieldFromCoord(float x, float y, int[] field) {
        field[0] = (int)(x / fieldWidth);
        field[1] = (int)(y / fieldWidth);
        getCenterFromField(field);
        return !(field[0] < 0 || field[0] > 7 || field[1] < 0 || field[1] > 7);
    }

    public boolean getCenterFromField(int[] field) {
        float x = fieldWidth / 2 + fieldWidth * field[0] + lineWidth / 2;
        float y = fieldWidth / 2 + fieldWidth * field[1] + lineWidth / 2;
        circles.add(new float[]{x,y});
        Log.d("TEST", "FX: " + x + " FY: " + y);
        return !(field[0] < 0 || field[0] > 7 || field[1] < 0 || field[1] > 7);
    }

    private void drawLines() {
        float halfLine = lineWidth / 2;
        for(int i = 0; i < 9; i++) {
            gCanvas.drawLine(halfLine + linesSep * i, 0, halfLine + linesSep * i, halfLine + linesSep * 8, linesPaint);
        }
        for(int i = 0; i < 9; i++) {
            gCanvas.drawLine(0, halfLine + linesSep * i, displayWidth, halfLine + linesSep * i, linesPaint);
        }
    }
}
