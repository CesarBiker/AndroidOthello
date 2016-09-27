package cesarbiker.xyz.othello;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

    private CanvasView gCanvasView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(new CanvasView(this));
        setContentView(R.layout.activity_main);


        //Set the height equal to the width
        //We get the View
        gCanvasView = (CanvasView) findViewById(R.id.view);

        //When the View is loaded (otherwise we can't get the width)
        gCanvasView.post(new Runnable() {
            @Override
            public void run() {
                //Get the params
                ViewGroup.LayoutParams layoutParams = gCanvasView.getLayoutParams();
                //Set the height to the View width
                layoutParams.height = gCanvasView.getWidth();
                //Set the params
                gCanvasView.setLayoutParams(layoutParams);
                //Invalidate the view to reload it
                gCanvasView.postInvalidate();

                gCanvasView.startGame();
            }
        });
    }

    public void buttonOnClick(View view) {
        switch (view.getId()) {
            case R.id.buttonChangeMode: {
                gCanvasView.changeMode();
            } break;
            case R.id.buttonReset: {
                gCanvasView.reset();
            } break;
        }
    }

}
