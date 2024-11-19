package ch.maystre.gilbert.vorodraw;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Random;

import ch.maystre.gilbert.vorodraw.gui.HelpPane;
import ch.maystre.gilbert.vorodraw.gui.HelpPaneListener;
import ch.maystre.gilbert.vorodraw.gui.ShakeDetector;
import ch.maystre.gilbert.vorodraw.gui.VoronoiPane;

import static android.view.View.GONE;

public class MainActivity extends Activity {

    public static final Random RANDOM = new Random();

    private VoronoiPane voronoiPane;
    private ImageButton helpButton;
    private ImageButton restartButton;
    private HelpPane helpPane;
    private WebView helpView;

    private boolean helpMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        voronoiPane = findViewById(R.id.voronoi_pane);
        helpButton = findViewById(R.id.help_button);
        restartButton = findViewById(R.id.restart_button);
        helpPane = findViewById(R.id.help_pane);
        helpView = findViewById(R.id.help_view);

        registerListeners();
    }

    private void registerListeners(){
        helpPane.registerListener(new HelpPaneListener() {
            @Override
            public void closeButtonPressed() {
                helpMode = false;
                helpPane.setVisibility(GONE);
                helpView.scrollTo(0,0);
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpMode = true;
                helpPane.setAlpha(0f);
                helpPane.setVisibility(View.VISIBLE);
                helpPane.animate().alpha(1f).setDuration(150).setListener(null);
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(voronoiPane.isEmpty()){
                    // make a toast
                    Toast.makeText(getApplicationContext(),"Please add points first!", Toast.LENGTH_SHORT/2).show();
                }
                else{
                    // empty it
                    restartButton.setRotation(0f);
                    restartButton.animate().rotation(-360f).setDuration(500).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            restartButton.setPivotY(restartButton.getHeight()/2f + 8);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            voronoiPane.restart();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {}

                        @Override
                        public void onAnimationRepeat(Animator animation) {}
                    });
                }
            }
        });

        // ShakeDetector initialization
        SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ShakeDetector detector = new ShakeDetector();
        detector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                if(!helpMode)
                    voronoiPane.changePalette();
            }
        });

        manager.registerListener(detector, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onBackPressed() {
        if(helpMode){
            helpMode = false;
            helpPane.setVisibility(GONE);
            helpView.scrollTo(0,0);
        }
        else {
            voronoiPane.restart();
            super.onBackPressed();
        }
    }

}
