package waverunner.example.victorling8.waverunner.ScoresScreen;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import waverunner.example.victorling8.waverunner.GameAndTutorial.GameOverDialogFragment;
import waverunner.example.victorling8.waverunner.MainScreen.ColourAdapter;
import waverunner.example.victorling8.waverunner.ModesScreen.ModesFragment;
import waverunner.example.victorling8.waverunner.R;
import waverunner.example.victorling8.waverunner.GameAndTutorial.ScreenUtil;
import waverunner.example.victorling8.waverunner.MainScreen.Skin;

public class HighScoreAdapter extends ArrayAdapter<HighScore>
{
    Context context;
    int resource;
    ArrayList<HighScore> highScores;

    private int imageWidth;

    private HashMap<Integer, Bitmap> skinBitmaps = new HashMap<>();

    ValueAnimator smallPulseAnimator;
    ValueAnimator largePulseAnimator;

    private int smallPulseMinRadius;
    private int smallPulseMaxRadius;
    private int largePulseMinRadius;
    private int largePulseMaxRadius;

    private View animatedView;

    private int indexChosen = -1;
    private int mode = ModesFragment.NORMAL_MODE;

    public HighScoreAdapter(Context context, int resource, ArrayList<HighScore> objects)
    {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        highScores = objects;

        imageWidth = Math.round(ScreenUtil.convertDpToPixel(40, context));

        smallPulseMinRadius = Math.round(ScreenUtil.convertDpToPixel(35, context));
        smallPulseMaxRadius = Math.round(ScreenUtil.convertDpToPixel(50, context));
        largePulseMinRadius = Math.round(ScreenUtil.convertDpToPixel(35, context));
        largePulseMaxRadius = Math.round(ScreenUtil.convertDpToPixel(60, context));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view;
        ViewHolder holder;

        if (convertView == null)
        {
            view = LayoutInflater.from(context).inflate(R.layout.high_score_list_item, parent, false);

            holder = new ViewHolder();

            holder.distance = (TextView) view.findViewById(R.id.high_score_item_distance);
            holder.coins = (TextView) view.findViewById(R.id.high_score_item_coins);

            holder.medal = (ImageView) view.findViewById(R.id.high_score_medal);

            holder.circle = (ImageView) view.findViewById(R.id.colourCircle);
            holder.smallPulse = (ImageView) view.findViewById(R.id.small_pulse);
            holder.largePulse = (ImageView) view.findViewById(R.id.large_pulse);

            view.setTag(holder);
        }
        else
        {
            view = convertView;
            holder = (ViewHolder) convertView.getTag();
        }

        HighScore highScore = highScores.get(position);

        holder.medal.setBackgroundResource(0);

        GameOverDialogFragment.setMedalBackground(holder.medal, highScore.distance, mode);

        holder.distance.setText(Integer.toString(highScore.distance));
        holder.coins.setText(Integer.toString(highScore.coins));

        boolean isSkin = highScore.skinEnabled;

        int skinId = 0;
        int colour;

        if (isSkin)
        {
            skinId = highScore.skinOrColourValue;
            colour = Skin.getColourFromSkin(skinId, context);
        }
        else
        {
            colour = highScore.skinOrColourValue;
        }

        holder.smallPulse.getBackground().setColorFilter(ColourAdapter.getMostlyOpaque(colour), PorterDuff.Mode.SRC_ATOP);
        holder.largePulse.getBackground().setColorFilter(ColourAdapter.getMostlyTransparent(colour), PorterDuff.Mode.SRC_ATOP);

        if (isSkin)
        {
            holder.circle.clearColorFilter();

            Bitmap bitmap;

            if (skinBitmaps.get(skinId) != null)
            {
                bitmap = skinBitmaps.get(skinId);
            }
            else
            {
                bitmap = BitmapFactory.decodeResource(context.getResources(), skinId);
                bitmap = Bitmap.createScaledBitmap(bitmap, imageWidth, imageWidth, false);

                skinBitmaps.put(skinId, bitmap);
            }

            holder.circle.setImageBitmap(bitmap);
        }
        else
        {
            holder.circle.setImageBitmap(null);
            holder.circle.setBackgroundResource(R.drawable.colour_circle);

            holder.circle.getBackground().setColorFilter(colour, PorterDuff.Mode.SRC_ATOP);
        }

        if (indexChosen == position)
        {
            animateView(view);
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
        return highScores.size();
    }

    public void setIndexChosen(int index)
    {
        indexChosen = index;
    }

    public void setMode(int mode)
    {
        this.mode = mode;
    }

    public void animateView(View view)
    {
        clearAnimations();

        final ImageView smallPulse = ((ViewHolder) view.getTag()).smallPulse;
        final ImageView largePulse = ((ViewHolder) view.getTag()).largePulse;

        TimeInterpolator interpolator = new TimeInterpolator()
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

        smallPulseAnimator = ValueAnimator.ofInt(smallPulseMinRadius, smallPulseMaxRadius);
        smallPulseAnimator.setDuration(1000);
        smallPulseAnimator.setRepeatCount(ValueAnimator.INFINITE);
        smallPulseAnimator.setInterpolator(interpolator);
        smallPulseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) smallPulse.getLayoutParams();
                params.width = params.height = value;

                smallPulse.setLayoutParams(params);
            }
        });

        largePulseAnimator = ValueAnimator.ofInt(largePulseMinRadius, largePulseMaxRadius);
        largePulseAnimator.setDuration(1000);
        largePulseAnimator.setRepeatCount(ValueAnimator.INFINITE);
        largePulseAnimator.setInterpolator(interpolator);
        largePulseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) largePulse.getLayoutParams();
                params.width = params.height = value;

                largePulse.setLayoutParams(params);
            }
        });

        smallPulseAnimator.start();
        largePulseAnimator.start();

        animatedView = view;
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
        if (smallPulseAnimator != null)
        {
            smallPulseAnimator.end();
        }

        if (largePulseAnimator != null)
        {
            largePulseAnimator.end();
        }
    }

    private static class ViewHolder
    {
        public TextView distance;
        public TextView coins;

        public ImageView medal;

        public ImageView circle;
        public ImageView smallPulse;
        public ImageView largePulse;
    }
}
