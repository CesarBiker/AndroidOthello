package cesarbiker.xyz.othello;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

public class MainActivity extends AppCompatActivity {

    private CanvasView gCanvasView;
    private Button gChangeModeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(new CanvasView(this));
        setContentView(R.layout.activity_main);

        gChangeModeButton = (Button) findViewById(R.id.buttonChangeMode);


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
                if (gCanvasView.gameMode == 0) {
                    gChangeModeButton.setText("Singleplayer");
                } if (gCanvasView.gameMode == 1) {
                    gChangeModeButton.setText("Multiplayer");
                }
            } break;
            case R.id.buttonReset: {
                gCanvasView.reset();
            } break;
        }
    }

}
