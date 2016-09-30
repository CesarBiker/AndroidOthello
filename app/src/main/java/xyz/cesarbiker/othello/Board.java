package xyz.cesarbiker.othello;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Copyright 2016 Àngel Mariages <angel[dot]mariages[at]gmail[dot]com>
 * <p>
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 **/
public class Board {
    private Piece[][] gameBoard = new Piece[8][8];

    public int[] countPieces = new int[2];
    public boolean boardFull = false;

    public Board() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                gameBoard[i][j] = new Piece(i, j);
            }
        }
        gameBoard[3][3].setPlayer(1);
        gameBoard[4][3].setPlayer(2);
        gameBoard[3][4].setPlayer(2);
        gameBoard[4][4].setPlayer(1);
    }

    public boolean addPiece(int x, int y, int player, boolean delay) {
        ArrayList<int[]> posToChange = new ArrayList<>();

        if (player == 3 || player == 0) {
            gameBoard[x][y].setPlayer(player);
            return true;
        } else if (checkMove(x, y, player, posToChange)) {
            if (delay) {
                Handler handler = new Handler(Looper.getMainLooper());
                Runnable runnable2 = () -> {
                    changePieces(posToChange, player);
                    countPieces();
                };
                Runnable runnable1 = () -> {
                    gameBoard[x][y].setPlayer(player);
                    gameBoard[x][y].blink();
                    handler.postDelayed(runnable2, 1000);
                };
                handler.postDelayed(runnable1, 1000);
            } else {
                gameBoard[x][y].setPlayer(player);
                changePieces(posToChange, player);
            }
            return true;
        } else
            return false;
    }

    public void changePieces(ArrayList<int[]> posToChange, int player) {
        for (int[] pos : posToChange) {
            gameBoard[pos[0]][pos[1]].setPlayer(player);
        }
    }

    public void update() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                gameBoard[i][j].update();
            }
        }
    }

    public Piece getPiece(int x, int y) {
        //TODO: check errors
        return gameBoard[x][y];
    }

    private int getPlayerFromField(int x, int y) {
        return gameBoard[x][y].getPlayer();
    }

    public Piece getPieceFromCoord(float x, float y) {
        //TODO: check errors
        int i = (int) (x / Game.fieldWidth);
        int j = (int) (y / Game.fieldWidth);
        if (i >= 0 && i < 8 && j >= 0 && j < 8)
            return gameBoard[i][j];
        return null;
    }

    public boolean checkMove(int x, int y, int player, ArrayList<int[]> posToChange) {
        int otherPlayer = player == 1 ? 2 : 1;
        boolean yPlus = y + 1 <= 7;
        boolean xPlus = x + 1 <= 7;
        boolean yMinus = y - 1 >= 0;
        boolean xMinus = x - 1 >= 0;

        ArrayList<int[]> tmpPosToChange = new ArrayList<>();

        if (yMinus && getPlayerFromField(x, y - 1) == otherPlayer) {
            for (int i = y - 1; i >= 0; i--) {
                if (getPlayerFromField(x, i) == 0)
                    break;
                if (getPlayerFromField(x, i) == otherPlayer)
                    tmpPosToChange.add(new int[]{x, i});
                if (getPlayerFromField(x, i) == player && tmpPosToChange.size() > 0) {
                    posToChange.addAll(tmpPosToChange);
                    break;
                }
            }
        }
        if (yPlus && getPlayerFromField(x, y + 1) == otherPlayer) {
            tmpPosToChange.clear();
            for (int i = y + 1; i <= 7; i++) {
                if (getPlayerFromField(x, i) == 0)
                    break;
                if (getPlayerFromField(x, i) == otherPlayer)
                    tmpPosToChange.add(new int[]{x, i});
                if (getPlayerFromField(x, i) == player && tmpPosToChange.size() > 0) {
                    posToChange.addAll(tmpPosToChange);
                    break;
                }
            }
        }
        if (xMinus && getPlayerFromField(x - 1, y) == otherPlayer) {
            tmpPosToChange.clear();
            for (int i = x - 1; i >= 0; i--) {
                if (getPlayerFromField(i, y) == 0)
                    break;
                if (getPlayerFromField(i, y) == otherPlayer)
                    tmpPosToChange.add(new int[]{i, y});
                if (getPlayerFromField(i, y) == player && tmpPosToChange.size() > 0) {
                    posToChange.addAll(tmpPosToChange);
                    break;
                }
            }
        }
        if (xPlus && getPlayerFromField(x + 1, y) == otherPlayer) {
            tmpPosToChange.clear();
            for (int i = x + 1; i <= 7; i++) {
                if (getPlayerFromField(i, y) == 0)
                    break;
                if (getPlayerFromField(i, y) == otherPlayer)
                    tmpPosToChange.add(new int[]{i, y});
                if (getPlayerFromField(i, y) == player && tmpPosToChange.size() > 0) {
                    posToChange.addAll(tmpPosToChange);
                    break;
                }
            }
        }
        if (xPlus && yPlus && getPlayerFromField(x + 1, y + 1) == otherPlayer) {
            tmpPosToChange.clear();
            for (int i = x + 1, j = y + 1; i <= 7 && j <= 7; i++, j++) {
                if (getPlayerFromField(i, j) == 0)
                    break;
                if (getPlayerFromField(i, j) == otherPlayer)
                    tmpPosToChange.add(new int[]{i, j});
                if (getPlayerFromField(i, j) == player && tmpPosToChange.size() > 0) {
                    posToChange.addAll(tmpPosToChange);
                    break;
                }
            }
        }
        if (xPlus && yMinus && getPlayerFromField(x + 1, y - 1) == otherPlayer) {
            tmpPosToChange.clear();
            for (int i = x + 1, j = y - 1; i <= 7 && j >= 0; i++, j--) {
                if (getPlayerFromField(i, j) == 0)
                    break;
                if (getPlayerFromField(i, j) == otherPlayer)
                    tmpPosToChange.add(new int[]{i, j});
                if (getPlayerFromField(i, j) == player && tmpPosToChange.size() > 0) {
                    posToChange.addAll(tmpPosToChange);
                    break;
                }
            }
        }
        if (xMinus && yMinus && getPlayerFromField(x - 1, y - 1) == otherPlayer) {
            tmpPosToChange.clear();
            for (int i = x - 1, j = y - 1; i >= 0 && j >= 0; i--, j--) {
                if (getPlayerFromField(i, j) == 0)
                    break;
                if (getPlayerFromField(i, j) == otherPlayer)
                    tmpPosToChange.add(new int[]{i, j});
                if (getPlayerFromField(i, j) == player && tmpPosToChange.size() > 0) {
                    posToChange.addAll(tmpPosToChange);
                    break;
                }
            }
        }
        if (xMinus && yPlus && getPlayerFromField(x - 1, y + 1) == otherPlayer) {
            tmpPosToChange.clear();
            for (int i = x - 1, j = y + 1; i >= 0 && j <= 7; i--, j++) {
                if (getPlayerFromField(i, j) == 0)
                    break;
                if (getPlayerFromField(i, j) == otherPlayer)
                    tmpPosToChange.add(new int[]{i, j});
                if (getPlayerFromField(i, j) == player && tmpPosToChange.size() > 0) {
                    posToChange.addAll(tmpPosToChange);
                    break;
                }
            }
        }
        //Log.d("TEST", "PiecesToChange: " + Arrays.deepToString(posToChange.toArray()));
        return posToChange.size() > 0;
    }


    public void debugBoard() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                str.append(gameBoard[j][i].getPlayer()).append(" - ");
            }
            str.append("\n");
        }
        Log.d("TEST", str.toString());
    }

    public int bestMove(ArrayList<int[]> moves) {
        int hard = -1;
        int[] currentMove;

        for (int i = 0; i < moves.size(); i++) {
            currentMove = moves.get(i);
            if (check(7,7,currentMove) ||
                    check(0,0,currentMove) ||
                    check(0,7,currentMove) ||
                    check(7,0,currentMove)) {
                hard = i;
                break;
            } else if(check(0,6,currentMove) || check(1,6,currentMove) || check(1,7,currentMove)
                    || check(0,1,currentMove) || check(1,1,currentMove) || check(1,0,currentMove)
                    || check(6,0,currentMove) || check(6,1,currentMove) || check(7,1,currentMove)
                    || check(6,7,currentMove) || check(6,6,currentMove) || check(7,6,currentMove)) {
            } else if (currentMove[2] > hard)
                hard = i;
        }
        if(hard == -1 && moves.size() > 0)
            hard = 0;
        return hard;
    }

    private boolean check(int x, int y, int[] move) {
        return move[0] == x && move[1] == y;
    }

    public boolean playerHasMoves(int currentPlayer, ArrayList<int[]> moves) {
        moves.clear();
        ArrayList<int[]> tmp = new ArrayList<>();
        int tmpPlayer;
        int[] tmpCount = new int[2];


        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                tmpPlayer = gameBoard[i][j].getPlayer();
                if (tmpPlayer == 0) {
                    checkMove(i, j, currentPlayer, tmp);
                    if (tmp.size() > 0) {
                        moves.add(new int[]{i, j, tmp.size()});
                        System.out.println("x: " + i + ", y:" + j + " - pieces: " + tmp.size());
                    }
                } else if (tmpPlayer == 1)
                    tmpCount[0]++;
                else if (tmpPlayer == 2)
                    tmpCount[1]++;
                tmp.clear();
            }
        }

        if ((tmpCount[0] + tmpCount[1]) == (gameBoard.length * gameBoard.length))
            boardFull = true;
        countPieces = tmpCount;

        return moves.size() > 0;
    }

    public void countPieces() {
        int[] tmpCount = new int[2];
        for (Piece[] pieces : gameBoard) {
            for (Piece piece : pieces) {
                if (piece.getPlayer() == 1)
                    tmpCount[0]++;
                else if(piece.getPlayer() == 2)
                    tmpCount[1]++;
            }
        }

        countPieces = tmpCount;

        Game.countText.setText("Blanques: " + countPieces[0] + " - Negres:" + countPieces[1]);
    }
}
