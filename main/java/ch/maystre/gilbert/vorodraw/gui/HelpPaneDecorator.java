package ch.maystre.gilbert.vorodraw.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import ch.maystre.gilbert.vorodraw.helpers.NiftyAndroid;

public class HelpPaneDecorator extends View {

    private float marginTop = 12;
    private float gradientHeight = 40;
    private float marginBottom = 8;

    private Paint gradientPaint;

    private void init(){
        marginTop = NiftyAndroid.pixelFromDp(getContext(), marginTop);
        gradientHeight = NiftyAndroid.pixelFromDp(getContext(), gradientHeight);
        marginBottom = NiftyAndroid.pixelFromDp(getContext(), marginBottom);

        gradientPaint = new Paint();
        gradientPaint.setShader(new LinearGradient(this.getWidth()/2, marginTop, this.getWidth()/2, marginTop + gradientHeight, Color.BLACK, Color.TRANSPARENT, Shader.TileMode.CLAMP));
    }

    @Override
    public void onDraw(Canvas canvas){
        canvas.drawPaint(gradientPaint);
    }

    private int measureDimension(int desiredSize, int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if(specMode == MeasureSpec.EXACTLY)
            result = specSize;
        else {
            result = desiredSize;
            if(specMode == MeasureSpec.AT_MOST)
                result = Math.min(result, specSize);
        }

        return result;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int desiredWidth = getSuggestedMinimumWidth() + getPaddingLeft() + getPaddingRight();
        int desiredHeight = getSuggestedMinimumHeight() + getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(measureDimension(desiredWidth, widthMeasureSpec), measureDimension(desiredHeight, heightMeasureSpec));
    }

    @Override
    public int getSuggestedMinimumWidth(){
        return 0;
    }

    @Override
    public int getSuggestedMinimumHeight(){
        return (int) (gradientHeight + marginTop + marginBottom);
    }

    public HelpPaneDecorator(Context context) {
        super(context);
        init();
    }

    public HelpPaneDecorator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HelpPaneDecorator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public HelpPaneDecorator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

}
