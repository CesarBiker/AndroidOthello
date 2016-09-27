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
 * Created by iam39418281 on 9/19/16.
 */
public class CanvasView extends View {
    private Game currentGame;

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

    }

    public void startGame() {
        currentGame.gameStarted = true;
    }
}
