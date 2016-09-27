package cesarbiker.xyz.othello;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

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
public class CanvasView extends View {
    private Game currentGame;
    public int gameMode = 0;

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        currentGame = new Game(context);
        getViewSize();
        update.run();
    }

    public CanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        currentGame = new Game(context);
        getViewSize();
        update.run();
    }

    public CanvasView(Context context) {
        super(context);
        currentGame = new Game(context);
        getViewSize();
        update.run();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        currentGame.setCanvas(canvas);
        currentGame.render();
    }

    Handler timer = new Handler(Looper.getMainLooper());
    Runnable update = new Runnable() {
        @Override
        public void run() {
            currentGame.update();
            invalidate();
            timer.postDelayed(update, 50);
        }
    };

    private void getViewSize() {
        ViewTreeObserver observer = this.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);//TODO: change
                currentGame.setDisplayWidth(getMeasuredWidth());
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int coord[] = new int[2];
        this.getLocationInWindow(coord);

        float x = event.getRawX() - coord[0];
        float y = event.getRawY() - coord[1];
        if(event.getAction() == MotionEvent.ACTION_DOWN)
            currentGame.touch(x, y);
        return true;
    }

    public void reset() {
        currentGame.reset();
    }

    public void changeMode() {
        currentGame.changeMode();
        gameMode = gameMode == 0 ? 1 : 0;
    }

    public void startGame() {
        currentGame.gameStarted = true;
    }
}
