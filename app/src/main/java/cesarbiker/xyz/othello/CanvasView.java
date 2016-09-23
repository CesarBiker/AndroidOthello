package cesarbiker.xyz.othello;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewTreeObserver;

/**
 * Created by iam39418281 on 9/19/16.
 */
public class CanvasView extends SurfaceView {
    SurfaceHolder surfaceHolder;

    Canvas testCanvas;
    SurfaceHolder testHolder;

    Game testGame = new Game();

    /*@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        testGame.setCanvas(canvas);
        testGame.render();
    }*/

    public CanvasView(Context context) {
        super(context);
        init();
    }

    public CanvasView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public CanvasView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        init();
    }

    public void init() {
        getViewSize();
        //-- Init SurfaceView
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback2() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                testHolder = holder;
                testCanvas = testHolder.lockCanvas(null);
                testGame.setCanvas(testCanvas);
                testHolder.unlockCanvasAndPost(testCanvas);
                Log.d("TEST","surfaceCreated()");
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.d("TEST","surfaceChanged()");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.d("TEST","surfaceDestroyed()");
            }

            @Override
            public void surfaceRedrawNeeded(SurfaceHolder holder) {
                Log.d("TEST","surfaceRedrawNeeded()");
            }
        });
        //--
        update.run();
    }

    Handler timer = new Handler(Looper.getMainLooper());
    Runnable update = new Runnable() {
        @Override
        public void run() {
            if (testCanvas != null) {
                testCanvas = testHolder.lockCanvas(null);
                testGame.setCanvas(testCanvas);
                testGame.update();
                testHolder.unlockCanvasAndPost(testCanvas);
                //invalidate();
            }

            timer.postDelayed(update, 50);
        }
    };

    private void getViewSize() {
        ViewTreeObserver observer = this.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);//TODO: change
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
        if(event.getAction() == MotionEvent.ACTION_MOVE)
            testGame.gameStarted = true;
        else if(event.getAction() == MotionEvent.ACTION_DOWN)
            testGame.touch(x, y);
        return true;
    }
}
