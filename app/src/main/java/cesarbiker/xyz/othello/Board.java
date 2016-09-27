package cesarbiker.xyz.othello;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

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
public class Board {
    private Piece[][] gameBoard = new Piece[8][8];

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

    public boolean addPiece(int x, int y, int player) {
        ArrayList<int[]> posToChange = new ArrayList<>();

        if(player == 3 || player == 0) {
            gameBoard[x][y].setPlayer(player);
            return true;
        } else if (checkMove(x, y, player, posToChange)) {
            gameBoard[x][y].setPlayer(player);
            changePieces(posToChange, player);
            return true;
        }
        else
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
        int i = (int)(x / Game.fieldWidth);
        int j = (int)(y / Game.fieldWidth);
        if(i >= 0 && i < 8 && j >= 0 && j < 8)
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

        if(yMinus && getPlayerFromField(x,y - 1)== otherPlayer) {
            for(int i = y - 1; i >= 0; i--) {
                if(getPlayerFromField(x,i) == otherPlayer)
                    tmpPosToChange.add(new int[]{x,i});
                if(getPlayerFromField(x,i) == player && tmpPosToChange.size() > 0) {
                    posToChange.addAll(tmpPosToChange);
                    break;
                }
            }
        }
        if(yPlus && getPlayerFromField(x,y + 1) == otherPlayer) {
            tmpPosToChange.clear();
            for (int i = y + 1; i <= 7; i++) {
                if(getPlayerFromField(x,i) == otherPlayer)
                    tmpPosToChange.add(new int[]{x,i});
                if(getPlayerFromField(x,i)== player && tmpPosToChange.size() > 0) {
                    posToChange.addAll(tmpPosToChange);
                    break;
                }
            }
        }
        if(xMinus && getPlayerFromField(x - 1,y) == otherPlayer) {
            tmpPosToChange.clear();
            for (int i = x - 1; i >= 0; i--) {
                if(getPlayerFromField(i,y) == otherPlayer)
                    tmpPosToChange.add(new int[]{i,y});
                if(getPlayerFromField(i,y) == player && tmpPosToChange.size() > 0) {
                    posToChange.addAll(tmpPosToChange);
                    break;
                }
            }
        }
        if(xPlus && getPlayerFromField(x + 1,y) == otherPlayer) {
            tmpPosToChange.clear();
            for (int i = x + 1; i <= 7 ; i++) {
                if(getPlayerFromField(i,y) == otherPlayer)
                    tmpPosToChange.add(new int[]{i,y});
                if(getPlayerFromField(i,y) == player && tmpPosToChange.size() > 0) {
                    posToChange.addAll(tmpPosToChange);
                    break;
                }
            }
        }
        if(xPlus && yPlus && getPlayerFromField(x + 1, y + 1) == otherPlayer) {
            tmpPosToChange.clear();
            for (int i = x + 1, j = y + 1; i <= 7 && j <= 7; i++,j++) {
                if(getPlayerFromField(i,j) == otherPlayer)
                    tmpPosToChange.add(new int[]{i,j});
                if(getPlayerFromField(i,j) == player && tmpPosToChange.size() > 0) {
                    posToChange.addAll(tmpPosToChange);
                    break;
                }
            }
        }
        if(xPlus && yMinus && getPlayerFromField(x + 1, y - 1) == otherPlayer) {
            tmpPosToChange.clear();
            for (int i = x + 1, j = y - 1; i <= 7 && j >= 0; i++,j--) {
                if(getPlayerFromField(i,j) == otherPlayer)
                    tmpPosToChange.add(new int[]{i,j});
                if(getPlayerFromField(i,j) == player && tmpPosToChange.size() > 0) {
                    posToChange.addAll(tmpPosToChange);
                    break;
                }
            }
        }
        if(xMinus && yMinus && getPlayerFromField(x - 1, y - 1) == otherPlayer) {
            tmpPosToChange.clear();
            for (int i = x - 1, j = y - 1; i >= 0 && j >= 0; i--,j--) {
                if(getPlayerFromField(i,j) == otherPlayer)
                    tmpPosToChange.add(new int[]{i,j});
                if(getPlayerFromField(i,j) == player && tmpPosToChange.size() > 0) {
                    posToChange.addAll(tmpPosToChange);
                    break;
                }
            }
        }
        if(xMinus && yPlus && getPlayerFromField(x - 1,y + 1) == otherPlayer) {
            tmpPosToChange.clear();
            for (int i = x - 1, j = y + 1; i >= 0 && j <= 7; i--,j++) {
                if(getPlayerFromField(i,j) == otherPlayer)
                    tmpPosToChange.add(new int[]{i,j});
                if(getPlayerFromField(i,j)== player && tmpPosToChange.size() > 0) {
                    posToChange.addAll(tmpPosToChange);
                    break;
                }
            }
        }
        Log.d("TEST", "PiecesToChange: " + Arrays.deepToString(posToChange.toArray()));
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

    public void bestMove() {

    }

    public void playerHasMoves(int currentPlayer) {
        int x = 2, y = 2;
        int currentWidth = 4;
        //currentWidth + x?
        for (int i = 0; i < ; i++) {

        }
    }
}
