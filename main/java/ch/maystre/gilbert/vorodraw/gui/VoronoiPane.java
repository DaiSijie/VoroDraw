package ch.maystre.gilbert.vorodraw.gui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashSet;
import java.util.List;

import ch.maystre.gilbert.vorodraw.R;
import ch.maystre.gilbert.vorodraw.geometry.BoundedPolygon;
import ch.maystre.gilbert.vorodraw.geometry.Point;
import ch.maystre.gilbert.vorodraw.helpers.NiftyAndroid;

import static ch.maystre.gilbert.vorodraw.MainActivity.RANDOM;

public class VoronoiPane extends View {

    private VoronoiPanePresenter presenter;

    private Palette currentPalette = new Palette.BubbleGumPalette(); // was bubble gum
    private int currentPaletteIndex = 2;

    private void init(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        this.presenter = new VoronoiPanePresenter(this, width, height);
    }

    @Override
    public Parcelable onSaveInstanceState(){
        Bundle bundle = new Bundle();
        bundle.putParcelable("SUPER_STATE", super.onSaveInstanceState());
        bundle.putParcelable("PANE_STATE", presenter.saveState());
        bundle.putInt("COLOR_PALETTE", currentPaletteIndex);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state){
        if(state instanceof Bundle){
            Bundle bundle = (Bundle) state;
            currentPaletteIndex = bundle.getInt("COLOR_PALETTE");
            currentPalette = Palette.getPaletteForInt(currentPaletteIndex);
            presenter.restoreState(bundle.getParcelable("PANE_STATE"));
            state = bundle.getParcelable("SUPER_STATE");
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    public void onDraw(Canvas canvas){
        for(Point center : presenter.getCenters()) {
            drawPolygon(presenter.getRegionForCenter(center), currentPalette.getColorFor(presenter.getColorForCenter(center)), canvas);
            drawCircle(center, NiftyAndroid.pixelFromDp(getContext(), 3), Color.BLACK, canvas);
        }

        if(presenter.peekMode())
            drawPolygon(presenter.getPeekRegion(), Color.WHITE, canvas);

        if(presenter.getCenters().isEmpty())
            drawStartMessage(canvas);

        if(presenter.peekMode())
            drawCircle(presenter.getPeekPoint(), NiftyAndroid.pixelFromDp(getContext(), 5), Color.BLACK, canvas);
    }

    private void drawStartMessage(Canvas c){
        String message = getContext().getString(R.string.start_message);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setTextSize(NiftyAndroid.pixelFromSp(getContext(), 40));

        paint.setTextAlign(Paint.Align.CENTER);

        String[] splat = message.split("\n");
        float delta = paint.descent() - paint.ascent();

        float startY = (this.getHeight() - splat.length * delta)/2;
        for(int i = 0; i < splat.length; i++){
            c.drawText(splat[i], this.getWidth()/2, startY + i * delta, paint);
        }
    }

    public void drawCircle(Point p, float radius, int color, Canvas c){
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        c.drawOval((float) p.x - radius, (float) p.y - radius, (float) p.x + radius, (float) p.y + radius, paint);
    }

    public void drawPolygon(BoundedPolygon p, int color, Canvas c){
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        Path path = new Path();

        List<Point> points = p.getPolygonNodes();
        path.moveTo((float) points.get(0).x, (float) points.get(0).y);
        for(int i = 1; i < points.size(); i++){
            path.lineTo((float) points.get(i).x, (float) points.get(i).y);
        }
        path.lineTo((float) points.get(0).x, (float) points.get(0).y);

        c.drawPath(path, paint);

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(NiftyAndroid.pixelFromDp(getContext(), 1.5f));
        paint.setAntiAlias(true);
        c.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        return presenter.onTouchEvent(e);
    }

    public void changePalette(){
        int next = RANDOM.nextInt(4);
        while(next == currentPaletteIndex)
            next = RANDOM.nextInt(4);

        currentPaletteIndex = next;
        currentPalette = Palette.getPaletteForInt(currentPaletteIndex);
        this.invalidate();
    }

    public void restart(){
        presenter.restart();
    }

    public boolean isEmpty(){
        return presenter.getCenters().isEmpty();
    }

    public VoronoiPane(Context context) {
        super(context);
        init();
    }

    public VoronoiPane(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VoronoiPane(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public VoronoiPane(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

}
