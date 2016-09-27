package cesarbiker.xyz.othello;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by iam39418281 on 9/19/16.
 */
public class Game {
    private Paint backgroundPaint = new Paint();

    private Paint linesPaint = new Paint();
    static float lineWidth = 30f;
    static float linesSep = 0f;

    private Paint pPlayerB = new Paint();
    private Paint pPlayerW =  new Paint();
    private Paint pWrong = new Paint();

    static int displayWidth = 0;

    static float fieldWidth = 0f;
    private Canvas gCanvas;

    boolean gameStarted = false;
    private Board currentBoard;

    private int currentPlayer = 1;
    private Piece wrongPiece = null;

    public static float animationSpeed;

    public Game(Context context) {
        linesPaint.setColor(Color.BLACK);
        linesPaint.setStrokeWidth(lineWidth);
        linesPaint.setAntiAlias(true);

        pPlayerB.setColor(Color.BLACK);
        pPlayerW.setColor(Color.WHITE);
        pWrong.setColor(Color.RED);
        pPlayerB.setAntiAlias(true);
        pPlayerW.setAntiAlias(true);
        pWrong.setAntiAlias(true);

        backgroundPaint.setColor(Color.parseColor("#339966"));

        currentBoard = new Board();

        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                animationSpeed = Settings.System.getFloat(context.getContentResolver(),
                        Settings.Global.ANIMATOR_DURATION_SCALE);
            } else {
                animationSpeed = Settings.System.getFloat(context.getContentResolver(),
                        Settings.System.ANIMATOR_DURATION_SCALE);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setCanvas(Canvas canvas) {
        gCanvas = canvas;
    }

    public void setDisplayWidth(int displayWidth) {
        Game.displayWidth = displayWidth;
        fieldWidth = Game.displayWidth / 8f;
        linesSep = Game.displayWidth / 8f - (lineWidth / 8f);
    }

    public void update() {
        currentBoard.update();
    }

    public void render() {
        if(gameStarted) {
            gCanvas.drawRect(0,0,displayWidth, lineWidth / 2 + linesSep * 8, backgroundPaint);
            drawLines();
            float radius = fieldWidth / 2 - lineWidth / 2 - 8f;

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    Piece currentPiece = currentBoard.getPiece(i,j);
                    float coord[] = currentPiece.getCenter();
                    int currentPlayer = currentPiece.getPlayer();

                    if (currentPiece.hasChanged() && !currentPiece.shouldAnimate()) {
                        currentPiece.setAnimate(true);
                    }
                    float outlineW = 6f;
                    if(currentPiece.shouldAnimate()) {
                        RectF r = new RectF(coord[0] - radius, coord[1] - radius, coord[0] + radius, coord[1] + radius);
                        if(currentPlayer == 1) {
                            gCanvas.drawArc(r, 0, currentPiece.getAngle(), true, pPlayerB);
                            RectF r2 = new RectF(coord[0] - radius + outlineW,
                                    coord[1] - radius + outlineW,
                                    coord[0] + radius - outlineW,
                                    coord[1] + radius - outlineW);
                            gCanvas.drawArc(r2, 0, currentPiece.getAngle(), true, pPlayerW);
                        } else if(currentPlayer == 2){
                            gCanvas.drawArc(r,0, currentPiece.getAngle(), true, pPlayerB);
                        }
                    } else {
                        if (currentPlayer == 1) {
                            gCanvas.drawCircle(coord[0], coord[1], radius, pPlayerB);
                            gCanvas.drawCircle(coord[0], coord[1], radius - outlineW, pPlayerW);
                        } else if (currentPlayer == 2) {
                            gCanvas.drawCircle(coord[0], coord[1], radius, pPlayerB);
                        } else if(currentPlayer == 3 && currentPiece.draw) {
                            gCanvas.drawCircle(coord[0], coord[1], radius, pPlayerB);
                            gCanvas.drawCircle(coord[0], coord[1], radius - outlineW, pWrong);
                        }
                    }
                }
            }
        }
    }

    public void touch(float x, float y) {
        if(gameStarted) {
            int field[];
            Piece currentPiece = currentBoard.getPieceFromCoord(x,y);
            if(currentPiece != null) {
                field = currentPiece.getField();

                if (currentPlayer == 1) {
                    if (currentBoard.addPiece(field[0], field[1], 1)) {
                        currentPlayer = 2;
                        if(wrongPiece != null) {
                            deleteWrongPiece();
                        }
                    }
                    else {
                        currentBoard.addPiece(field[0], field[1], 3);
                        if(wrongPiece == null)
                            wrongPiece = currentPiece;
                        else {
                            deleteWrongPiece();
                            wrongPiece = currentPiece;
                        }
                    }
                } else {
                    if (currentBoard.addPiece(field[0], field[1], 2)) {
                        currentPlayer = 1;
                        if(wrongPiece != null) {
                            deleteWrongPiece();
                        }
                    }
                    else {
                        currentBoard.addPiece(field[0], field[1], 3);
                        if(wrongPiece == null)
                            wrongPiece = currentPiece;
                        else {
                            deleteWrongPiece();
                            wrongPiece = currentPiece;
                        }
                    }
                }
            }
            currentBoard.debugBoard();
        }
    }

    private void deleteWrongPiece() {
        int wField[] = wrongPiece.getField();
        currentBoard.addPiece(wField[0], wField[1], 0);
        wrongPiece = null;
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

    public void reset() {
        currentBoard = new Board();
        currentPlayer = 1;
        wrongPiece = null;
    }
}
