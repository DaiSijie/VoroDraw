package ch.maystre.gilbert.vorodraw.helpers;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.view.WindowManager;

public final class NiftyAndroid {

    private NiftyAndroid(){}

    public static boolean isFullScreen(Context context){
        Activity activity = getActivityFromContext(context);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && activity != null) {
            return activity.getWindow().getAttributes().layoutInDisplayCutoutMode == WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        return false;
    }

    public static Activity getActivityFromContext(Context context){
        if (context == null) return null;
        if (context instanceof Activity) return (Activity) context;
        return null;
    }

    public static float pixelFromSp(Context context, float sp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static float pixelFromDp(Context context, float dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
