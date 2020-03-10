package ch.maystre.gilbert.vorodraw.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class VoronoiPane extends View {

    ArrayList<Float> centerX = new ArrayList<>();
    ArrayList<Float> centerY = new ArrayList<>();
    float currentX;
    float currentY;
    boolean currentlyTracking = false;

    public VoronoiPane(Context context) {
        super(context);
    }

    public VoronoiPane(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VoronoiPane(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public VoronoiPane(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    Paint centerPaint = new Paint();
    Paint cursorPaint = new Paint();


    @Override
    public void onDraw(Canvas c){
        centerPaint.setColor(Color.rgb(255, 0, 0));
        cursorPaint.setColor(Color.rgb(0, 255, 255));
        for(int i = 0; i < centerX.size(); i++)
            drawCircle(centerX.get(i), centerY.get(i), 10, c, centerPaint);

        if(currentlyTracking)
            drawCircle(currentX, currentY, 20, c,  cursorPaint);
    }

    public void drawCircle(float centerX, float centerY, float radius, Canvas c, Paint p){
        c.drawOval(centerX - radius, centerY - radius, centerX + radius, centerY + radius, p);
    }


    @Override
    public boolean onTouchEvent(MotionEvent e){
        currentX = e.getX();
        currentY = e.getY();
        if(e.getAction() == MotionEvent.ACTION_DOWN){
            currentlyTracking = true;
        }
        else if(e.getAction() == MotionEvent.ACTION_UP){
            currentlyTracking = false;
            centerX.add(e.getX());
            centerY.add(e.getY());
        }
        this.invalidate();
        return true;
    }
}
