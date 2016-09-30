package xyz.cesarbiker.othello;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Copyright 2016 Àngel Mariages <angel[dot]mariages[at]gmail[dot]com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 **/
public class Game {
    private final Context gContext;
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
    private int gameMode = 0;//0 -- Multiplayer - 1 -- SinglePlayer
    private Board currentBoard;

    private int currentPlayer = 1;
    private Piece wrongPiece = null;

    public static float animationSpeed;
    private Activity mainActivity;
    static public TextView countText;

    public Game(Context context) {
        gContext = context;
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
                    if(currentPiece.shouldAnimate() && !currentPiece.blink) {
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
                        if (currentPlayer == 1 && currentPiece.draw) {
                            gCanvas.drawCircle(coord[0], coord[1], radius, pPlayerB);
                            gCanvas.drawCircle(coord[0], coord[1], radius - outlineW, pPlayerW);
                        } else if (currentPlayer == 2 && currentPiece.draw) {
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
            Piece currentPiece = currentBoard.getPieceFromCoord(x,y);

            if(currentPiece != null) {
                if(currentPiece.getPlayer() == 0) {
                    if(gameMode == 0) {// - Singleplayer
                        touchMultiplayer(currentPiece);
                    } else if(gameMode == 1) {//- Multiplayer
                        touchSinglePlayer(currentPiece);
                    }
                    //TODO: remove this
                    countText.setText("Blanques: " + currentBoard.countPieces[0] + " - Negres:" + currentBoard.countPieces[1]);
                }
            }
        }
    }

    private void touchSinglePlayer(Piece currentPiece) {
        int bestMove;
        int[] field = currentPiece.getField();
        ArrayList<int[]> moves = new ArrayList<>();

        if(currentPlayer == 1) {
            if(currentBoard.addPiece(field[0], field[1], 1, false)) {
                if(wrongPiece != null)
                    deleteWrongPiece();

                while(!currentBoard.playerHasMoves(1,moves)) {
                    if(currentBoard.playerHasMoves(2,moves)) {
                        bestMove = currentBoard.bestMove(moves);
                        if(bestMove != -1) {
                            currentBoard.addPiece(moves.get(bestMove)[0], moves.get(bestMove)[1], 2, true);
                        }
                    } else {
                        //TODO: strings
                        Toast.makeText(gContext.getApplicationContext(), "Les negres no tenen lloc on tirar!", Toast.LENGTH_LONG).show();
                        if(!currentBoard.playerHasMoves(1, moves)) {
                            if(currentBoard.countPieces[0] > currentBoard.countPieces[1]) {
                                Toast.makeText(gContext.getApplicationContext(), "Guanyen les blanques!!", Toast.LENGTH_LONG).show();
                            } else if(currentBoard.countPieces[0] < currentBoard.countPieces[1]) {
                                Toast.makeText(gContext.getApplicationContext(), "Guanyen les negres!!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(gContext.getApplicationContext(), "Taules!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }

                if(currentBoard.playerHasMoves(2,moves)) {
                    bestMove = currentBoard.bestMove(moves);
                    if(bestMove != -1) {
                        currentBoard.addPiece(moves.get(bestMove)[0], moves.get(bestMove)[1], 2, true);
                    }
                } else {
                    //TODO: strings
                    Toast.makeText(gContext.getApplicationContext(), "Les negres no tenen lloc on tirar!", Toast.LENGTH_LONG).show();
                    if(!currentBoard.playerHasMoves(1, moves)) {
                        if(currentBoard.countPieces[0] > currentBoard.countPieces[1]) {
                            Toast.makeText(gContext.getApplicationContext(), "Guanyen les blanques!!", Toast.LENGTH_LONG).show();
                        } else if(currentBoard.countPieces[0] < currentBoard.countPieces[1]) {
                            Toast.makeText(gContext.getApplicationContext(), "Guanyen les negres!!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(gContext.getApplicationContext(), "Taules!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            } else {
                currentBoard.addPiece(field[0], field[1], 3, false);
                if (wrongPiece == null)
                    wrongPiece = currentPiece;
                else {
                    deleteWrongPiece();
                    wrongPiece = currentPiece;
                }
            }
        }
    }

    private void touchMultiplayer(Piece currentPiece) {
        int[] field = currentPiece.getField();
        ArrayList<int[]> moves = new ArrayList<>();

        if (currentPlayer == 1) {
            if (currentBoard.addPiece(field[0], field[1], 1, false)) {
                currentPlayer = 2;
                if (wrongPiece != null) {
                    deleteWrongPiece();
                }
                if(!currentBoard.playerHasMoves(currentPlayer, moves)) {
                    Toast.makeText(gContext.getApplicationContext(), "Les negres no poden tirar!", Toast.LENGTH_LONG).show();
                    currentPlayer = 1;
                }
            } else {
                currentBoard.addPiece(field[0], field[1], 3, false);
                if (wrongPiece == null)
                    wrongPiece = currentPiece;
                else {
                    deleteWrongPiece();
                    wrongPiece = currentPiece;
                }
            }
        } else {
            if (currentBoard.addPiece(field[0], field[1], 2, false)) {
                currentPlayer = 1;
                if (wrongPiece != null) {
                    deleteWrongPiece();
                }
                if(!currentBoard.playerHasMoves(currentPlayer, moves)) {
                    Toast.makeText(gContext.getApplicationContext(), "Les blanques no poden tirar!", Toast.LENGTH_LONG).show();
                    currentPlayer = 2;
                }
            } else {
                currentBoard.addPiece(field[0], field[1], 3, false);
                if (wrongPiece == null)
                    wrongPiece = currentPiece;
                else {
                    deleteWrongPiece();
                    wrongPiece = currentPiece;
                }
            }
        }
    }

    private void deleteWrongPiece() {
        int wField[] = wrongPiece.getField();
        currentBoard.addPiece(wField[0], wField[1], 0, false);
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

    public void changeMode() {
        gameMode = gameMode == 0 ? 1 : 0;
        reset();
    }

    public void setMainActivity(Activity mainActivity) {
        this.mainActivity = mainActivity;
        countText = (TextView) mainActivity.findViewById(R.id.countPiecesText);
    }
}
