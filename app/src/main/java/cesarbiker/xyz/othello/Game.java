package cesarbiker.xyz.othello;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

/**
 * Created by iam39418281 on 9/19/16.
 */
public class Game {
    Paint backgroundPaint = new Paint();

    Paint linesPaint = new Paint();
    float lineWidth = 30f;
    float linesSep = 0f;

    Paint pPlayerB = new Paint();
    Paint pPlayerW =  new Paint();
    float outlineW = 6f;

    private int displayWidth = 0;
    private float fieldWidth = 0f;

    Canvas gCanvas;
    boolean gameStarted = false;

    Board currentBoard;
    int currentPlayer = 1;

    int angle = 0;
    boolean[][] shouldAnimate = new boolean[8][8];
    int[][] lastBoard = new int[8][8];

    public Game() {
        linesPaint.setColor(Color.BLACK);
        linesPaint.setStrokeWidth(lineWidth);
        linesPaint.setAntiAlias(true);

        pPlayerB.setColor(Color.BLACK);
        pPlayerW.setColor(Color.WHITE);
        pPlayerB.setAntiAlias(true);
        pPlayerW.setAntiAlias(true);

        backgroundPaint.setColor(Color.parseColor("#339966"));

        currentBoard = new Board();
    }

    public void setCanvas(Canvas canvas) {
        gCanvas = canvas;
    }

    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
        fieldWidth = this.displayWidth / 8f;
        linesSep = this.displayWidth / 8f - (lineWidth / 8f);
    }

    public void update() {
        angle += 30;
        if(angle >= 360) {
            angle = 0;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (shouldAnimate[i][j]) {
                        shouldAnimate[i][j] = false;
                    }
                }
            }
        }
    }

    public void render() {
        if(gameStarted) {
            gCanvas.drawRect(0,0,displayWidth, lineWidth / 2 + linesSep * 8, backgroundPaint);
            drawLines();
            float[] coord = new float[2];
            int[][] gameBoard = currentBoard.gameBoard;
            float radius = fieldWidth / 2 - lineWidth / 2 - 8f;

            for (int i = 0; i < gameBoard.length; i++) {
                for (int j = 0; j < gameBoard.length; j++) {
                    if(gameBoard[i][j] == 1) {
                        getCenterFromField(new int[]{i, j}, coord);
                        if(lastBoard[i][j] != gameBoard[i][j]) {
                            shouldAnimate[i][j] = true;
                            angle = 0;
                        }
                        if(shouldAnimate[i][j]) {
                            RectF r = new RectF(coord[0] - radius, coord[1] - radius, coord[0] + radius, coord[1] + radius);
                            gCanvas.drawArc(r,0, angle, true, pPlayerB);
                            RectF r2 = new RectF(coord[0] - radius + outlineW,
                                    coord[1] - radius + outlineW,
                                    coord[0] + radius - outlineW,
                                    coord[1] + radius - outlineW);
                            gCanvas.drawArc(r2,0, angle, true, pPlayerW);
                            lastBoard[i][j] = gameBoard[i][j];
                        } else {
                            gCanvas.drawCircle(coord[0], coord[1], radius, pPlayerB);
                            gCanvas.drawCircle(coord[0], coord[1], radius - outlineW, pPlayerW);
                        }
                    } else if(gameBoard[i][j] == 2) {
                        getCenterFromField(new int[]{i, j}, coord);
                        if(lastBoard[i][j] != gameBoard[i][j]) {
                            shouldAnimate[i][j] = true;
                            angle = 0;
                        }
                        if(shouldAnimate[i][j]) {
                            RectF r = new RectF(coord[0] - radius, coord[1] - radius, coord[0] + radius, coord[1] + radius);
                            gCanvas.drawArc(r,0, angle, true, pPlayerB);
                            lastBoard[i][j] = gameBoard[i][j];
                        } else {
                            gCanvas.drawCircle(coord[0], coord[1], fieldWidth / 2 - lineWidth / 2 - 8f, pPlayerB);
                        }
                    }
                }
            }
        }
    }

    public void touch(float x, float y) {
        if(gameStarted) {
            int field[] = new int[2];
            float coord[] = new float[2];
            getFieldFromCoord(x, y, field);
            if (getCenterFromField(field, coord))
                if(currentPlayer == 1) {
                    if(currentBoard.addPiece(field[0], field[1], 1))
                        currentPlayer = 2;
                    else
                        Log.d("TEST", "No pots tirar aqui!");
                } else {
                    if(currentBoard.addPiece(field[0], field[1], 2))
                        currentPlayer = 1;
                    else
                        Log.d("TEST", "No pots tirar aqui!");
                }
        }
    }

    public boolean getFieldFromCoord(float x, float y, int[] field) {
        field[0] = (int)(x / fieldWidth);
        field[1] = (int)(y / fieldWidth);
        return !(field[0] < 0 || field[0] > 7 || field[1] < 0 || field[1] > 7);
    }

    public boolean getCenterFromField(int[] field, float[] coord) {
        float sep = lineWidth / 2f + linesSep / 2f;
        coord[0] = sep + linesSep * field[0];
        coord[1] = sep + linesSep * field[1];
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
