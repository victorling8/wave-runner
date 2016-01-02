package waverunner.example.victorling8.waverunner.PersistentState;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil
{
    public static void putBoolean(Context context, String key, boolean value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences("app", Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue)
    {
        return context.getSharedPreferences("app", Context.MODE_PRIVATE).getBoolean(key, defaultValue);
    }

    public static void putInt(Context context, String key, int value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences("app", Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(Context context, String key, int defaultValue)
    {
        return context.getSharedPreferences("app", Context.MODE_PRIVATE).getInt(key, defaultValue);
    }

    public static void putString(Context context, String key, String value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences("app", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key, String defaultValue)
    {
        return context.getSharedPreferences("app", Context.MODE_PRIVATE).getString(key, defaultValue);
    }

    public static boolean hasBoughtShopItem(Context context, String key)
    {
        return getBoolean(context, key, false);
    }

    public static void boughtShopItem(Context context, String key)
    {
        putBoolean(context, key, true);
    }
}
