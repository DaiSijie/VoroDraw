package ch.maystre.gilbert.vorodraw.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import ch.maystre.gilbert.vorodraw.helpers.NiftyAndroid;

/**
 * A transparent object that spans the whole task bar if it is transparent
 *
 * should be used with match on width and wrap on height
 */
public class SmartTop extends View {

    private int measureDimension(int desiredSize, int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if(specMode == MeasureSpec.EXACTLY)
            result = specSize;
        else{
            result = desiredSize;
            if (specMode == MeasureSpec.AT_MOST)
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
        return NiftyAndroid.isFullScreen(getContext()) ? getRootWindowInsets().getStableInsetTop() : 0;
    }

    public SmartTop(Context context) {
        super(context);
    }

    public SmartTop(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartTop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SmartTop(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}
