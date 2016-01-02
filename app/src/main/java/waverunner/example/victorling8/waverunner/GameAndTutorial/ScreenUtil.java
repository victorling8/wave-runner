package waverunner.example.victorling8.waverunner.GameAndTutorial;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import waverunner.example.victorling8.waverunner.R;

public class ScreenUtil
{
    public static float convertDpToPixel(float dp, Context context)
    {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static boolean isTablet(Activity activity)
    {
        return ! activity.getResources().getBoolean(R.bool.isPhone);
    }

    public static boolean isLandScape(Activity activity)
    {
        return activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}
