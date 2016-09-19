package cesarbiker.xyz.othello;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by iam39418281 on 9/19/16.
 */
public class CanvasView extends View {
    Game testGame = new Game();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        testGame.setCanvas(canvas);
        testGame.render();
    }

    public CanvasView(Context context) {
        super(context);
        getViewSize();
        update.run();
    }

    Handler timer = new Handler(Looper.getMainLooper());
    Runnable update = new Runnable() {
        @Override
        public void run() {
            testGame.update();
            invalidate();
            timer.postDelayed(update, 500);
        }
    };

    private void getViewSize() {
        ViewTreeObserver observer = this.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);//TODO: change
                testGame.setDisplayHeight(getMeasuredHeight());
                testGame.setDisplayWidth(getMeasuredWidth());
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
            testGame.touch(x, y);
        return true;
    }
}
