package waverunner.example.victorling8.waverunner.MainScreen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

import waverunner.example.victorling8.waverunner.R;
import waverunner.example.victorling8.waverunner.GameAndTutorial.ScreenUtil;

public class ColourAdapter extends ArrayAdapter<Skin>
{
    private Context context;
    private int resource;
    private ArrayList<Skin> skins;
    private GridView gridView;

    private View animatedView;

    private static float MIN_PULSE_ALPHA = 0.5f;
    private static float MAX_PULSE_ALPHA = 0.9f;

    private int imageWidth;

    ScaleAnimation smallPulseAnimation;
    ScaleAnimation largePulseAnimation;

    AnimationSet touchPulseAnimation;

    HashMap<Integer, Bitmap> skinBitmaps = new HashMap<>();

    private int indexChosen = -1;

    public ColourAdapter(Context context, int resource, ArrayList<Skin> objects, int indexChosen, GridView gridView)
    {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        skins = objects;
        this.indexChosen = indexChosen;
        this.gridView = gridView;

        imageWidth = Math.round(ScreenUtil.convertDpToPixel(40, context));
    }

    private static class SkinTag
    {
        public boolean isSkin;
        public int id;
        public int colour;

        public SkinTag(boolean isSkin, int id, int colour)
        {
            this.isSkin = isSkin;
            this.id = id;
            this.colour = colour;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view;
        SkinTag tag;

        Skin skin = skins.get(position);

        if (convertView != null)
        {
            view = convertView;
        }
        else
        {
            view = LayoutInflater.from(getContext()).inflate(R.layout.colour_circle, parent, false);
        }

        tag = (SkinTag) view.getTag();

        ImageView image = (ImageView) view.findViewById(R.id.colourCircle);
        ImageView smallPulse = (ImageView) view.findViewById(R.id.small_pulse);
        ImageView largePulse = (ImageView) view.findViewById(R.id.large_pulse);
        ImageView touchPulse = (ImageView) view.findViewById(R.id.touch_pulse);

        if (skin.isSkin)
        {
            if (tag == null || ! tag.isSkin || skin.id != tag.id)
            {
                image.clearColorFilter();

                Bitmap bitmap;

                if (skinBitmaps.get(skin.id) != null)
                {
                    bitmap = skinBitmaps.get(skin.id);
                }
                else
                {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), skin.id);
                    bitmap = Bitmap.createScaledBitmap(bitmap, imageWidth, imageWidth, false);

                    skinBitmaps.put(skin.id, bitmap);
                }

                image.setImageBitmap(bitmap);

                smallPulse.getBackground().setColorFilter(getMostlyOpaque(skin.colour), PorterDuff.Mode.SRC_ATOP);
                largePulse.getBackground().setColorFilter(getMostlyTransparent(skin.colour), PorterDuff.Mode.SRC_ATOP);
                touchPulse.getBackground().setColorFilter(getMostlyTransparent(skin.colour), PorterDuff.Mode.SRC_ATOP);
            }

            view.setTag(new SkinTag(true, skin.id, skin.colour));
        }
        else
        {
            if (tag == null || tag.isSkin || skin.colour != tag.colour)
            {
                image.setImageBitmap(null);
                image.setBackgroundResource(R.drawable.colour_circle);

                image.getBackground().setColorFilter(skin.colour, PorterDuff.Mode.SRC_ATOP);

                smallPulse.getBackground().setColorFilter(getMostlyOpaque(skin.colour), PorterDuff.Mode.SRC_ATOP);
                largePulse.getBackground().setColorFilter(getMostlyTransparent(skin.colour), PorterDuff.Mode.SRC_ATOP);
                touchPulse.getBackground().setColorFilter(getMostlyTransparent(skin.colour), PorterDuff.Mode.SRC_ATOP);
            }

            view.setTag(new SkinTag(false, -5, skin.colour));
        }

        if (indexChosen == position)
        {
            startAnimation(view, false);
        }
        else
        {
            clearAnimation(view);
        }

        return view;
    }

    @Override
    public int getCount()
    {
        return skins.size();
    }

    public void setSkinsAndUpdate(ArrayList<Skin> skins)
    {
        this.skins.clear();

        for (Skin skin : skins)
        {
            this.skins.add(skin);
        }

        notifyDataSetChanged();
    }

    public void setIndexChosen(int index)
    {
        indexChosen = index;
    }

    public void startAnimation(View view, boolean shouldPulse)
    {
        animatedView = view;

        clearAnimations();

        ImageView smallPulse = (ImageView) view.findViewById(R.id.small_pulse);
        ImageView largePulse = (ImageView) view.findViewById(R.id.large_pulse);
        final ImageView touchPulse = (ImageView) view.findViewById(R.id.touch_pulse);

        Interpolator interpolator = new Interpolator()
        {
            @Override
            public float getInterpolation(float input)
            {
                if (input <= 0.5)
                {
                    return 2 * input;
                }
                else
                {
                    return (float) (1 - ((input - 0.5) / 0.5));
                }
            }
        };

        float smallPulseMax = (float) ((double) 50 / 35);
        float largePulseMax = (float) ((double) 60 / 35);

        smallPulseAnimation = new ScaleAnimation(1f, smallPulseMax, 1f, smallPulseMax,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        smallPulseAnimation.setDuration(1000);
        smallPulseAnimation.setRepeatCount(Animation.INFINITE);
        smallPulseAnimation.setInterpolator(interpolator);

        largePulseAnimation = new ScaleAnimation(1f, largePulseMax, 1f, largePulseMax,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        largePulseAnimation.setDuration(1000);
        largePulseAnimation.setRepeatCount(Animation.INFINITE);
        largePulseAnimation.setInterpolator(interpolator);

        smallPulse.startAnimation(smallPulseAnimation);
        largePulse.startAnimation(largePulseAnimation);

        if (shouldPulse)
        {
            touchPulse.setVisibility(View.VISIBLE);
            touchPulse.setAlpha(1f);

            touchPulseAnimation = new AnimationSet(true);

            float touchPulseMax = (float) ((double) 60 / 25);

            ScaleAnimation expand = new ScaleAnimation(1f, touchPulseMax, 1f, touchPulseMax,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);;
            expand.setDuration(500);

            expand.setAnimationListener(new Animation.AnimationListener()
            {
                @Override
                public void onAnimationStart(Animation animation)
                {

                }

                @Override
                public void onAnimationEnd(Animation animation)
                {
                    touchPulse.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation)
                {

                }
            });

            AlphaAnimation fadeIn = new AlphaAnimation(MIN_PULSE_ALPHA, MAX_PULSE_ALPHA);
            fadeIn.setDuration(500);

            touchPulseAnimation.addAnimation(expand);
            touchPulseAnimation.addAnimation(fadeIn);

            touchPulse.startAnimation(touchPulseAnimation);
        }
    }

    public static int getColour(int colour)
    {
        float[] hsv = new float[3];
        Color.colorToHSV(colour, hsv);
        hsv[2] *= 1f; // value component
        colour = Color.HSVToColor(hsv);

        return colour;
    }

    public static int getMostlyOpaque(int colour)
    {
        float[] hsv = new float[3];
        Color.colorToHSV(colour, hsv);
        hsv[2] *= 0.8f; // value component
        colour = Color.HSVToColor(hsv);

        return colour;
    }

    public static int getMostlyTransparent(int colour)
    {
        float[] hsv = new float[3];
        Color.colorToHSV(colour, hsv);
        hsv[2] *= 0.5f; // value component
        colour = Color.HSVToColor(hsv);

        return colour;
    }

    private void clearAnimation(View view)
    {
        if (animatedView != null && animatedView == view)
        {
            clearAnimations();
        }
    }

    public void clearAnimations()
    {
        if (smallPulseAnimation != null)
        {
            smallPulseAnimation.cancel();
        }

        if (largePulseAnimation != null)
        {
            largePulseAnimation.cancel();
        }

        if (touchPulseAnimation != null)
        {
            touchPulseAnimation.cancel();
        }
    }
}
