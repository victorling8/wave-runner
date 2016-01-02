package waverunner.example.victorling8.waverunner.ModesScreen;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;

import waverunner.example.victorling8.waverunner.PersistentState.AppData;
import waverunner.example.victorling8.waverunner.R;
import waverunner.example.victorling8.waverunner.GameAndTutorial.ScreenUtil;

public class ModesAdapter extends ArrayAdapter<Mode>
{
    private Context context;
    private ArrayList<Mode> modes;
    private ListView listView;

    private int height;

    private HashMap<Mode, Bitmap> bitmaps = new HashMap<>();

    int indexChosen = AppData.getMode();

    private View lastSelectedView;

    public ModesAdapter(Context context, int resource, ArrayList<Mode> objects, ListView listView)
    {
        super(context, resource, objects);

        this.context = context;
        this.listView = listView;
        modes = objects;

        height = Math.round(ScreenUtil.convertDpToPixel(120, context));
    }

    @Override
    public int getCount()
    {
        return modes.size();
    }

    public void setIndexChosen(int indexChosen)
    {
        this.indexChosen = indexChosen;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Mode mode = modes.get(position);

        View view = convertView;
        ImageView image = (convertView != null) ? (ImageView) convertView.findViewById(R.id.modes_list_item_image) : null;
        Bitmap bitmap = bitmaps.get(mode);

        if (bitmap == null)
        {
            bitmap = BitmapFactory.decodeResource(context.getResources(), mode.drawableId);

            int width = (int) (bitmap.getWidth() * ((double) height / bitmap.getHeight()));

            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

            bitmaps.put(mode, bitmap);
        }

        if (view == null)
        {
            view = LayoutInflater.from(context).inflate(R.layout.modes_list_item, null);

            image = (ImageView) view.findViewById(R.id.modes_list_item_image);
        }

        if (indexChosen == position)
        {
            Log.d("", "wednesday animation starting");
            startAnimation(view);
        }
        else
        {
            Log.d("", "wednesday animation clearing");
            clearAnimation(view);
        }

        image.setImageBitmap(bitmap);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) image.getLayoutParams();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;

        double ratio = (double) bitmap.getHeight() / bitmap.getWidth();

        params.width = (int) (width * ((double) 5 / 6));
        params.height = (int) (ratio * params.width);

        return view;
    }

    public void startAnimation(View view)
    {
        clearAllAnimations();

        lastSelectedView = view;

        view.setAlpha(1f);

        AlphaAnimation fadeIn = new AlphaAnimation(0.25f, 1f);
        fadeIn.setDuration(750);
        fadeIn.setFillAfter(true);

        view.startAnimation(fadeIn);

        listView.invalidateViews();
    }

    public void clearAnimation(View view)
    {
        if (lastSelectedView == view)
        {
            clearAllAnimations();
        }

        view.setAlpha(0.25f);
    }

    public void clearAllAnimations()
    {
        if (lastSelectedView != null)
        {
            lastSelectedView.clearAnimation();

            lastSelectedView.setAlpha(0.25f);
        }
    }
}
