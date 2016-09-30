package xyz.cesarbiker.othello;

import java.util.Random;

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
    public boolean blink = false;

    public Piece(int x, int y) {
        this.x = x;
        this.y = y;
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

    public void update() {
        if(animate) {
            angle += sumAngle;
            if (angle >= 360f) {
                animate = false;
                isChanged = false;
            }
        } else if(wrong) {
            sumTime++;
            if(sumTime > 4)
                draw = false;
            if(sumTime > 8) {
                draw = true;
                sumTime = 0;
            }
        } else if(blink) {
            sumTime++;
            draw = sumTime % 4 != 0;
            if(sumTime > 16)
                blink = false;
        } else {
            draw = true;
        }
    }

    public void blink() {
        blink = true;
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
