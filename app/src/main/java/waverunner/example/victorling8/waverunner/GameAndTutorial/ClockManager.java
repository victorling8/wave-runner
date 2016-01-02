package waverunner.example.victorling8.waverunner.GameAndTutorial;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class ClockManager
{
    private Activity activity;
    private ValueAnimator animator;
    private ImageView image;
    RelativeLayout container;

    private ArrayList<ClockAnimationRequest> requests;

    private int x;
    private int y;

    private int damage;
    private int counter = DAMAGE_DURATION;

    private boolean isAlive = true;
    private boolean healingStageStarted = false;

    private double lastHealthPointReached;

    private static final int FADE_RED_OUT_POINT = 100;

    private static final int DAMAGE_DURATION = 800;
    private static final int HEAL_DURATION = 800;

    private static final int RED = 0xff0000;
    private static final int GREEN = 0x00ff00;

    private static final float MIN_ALPHA = 0;
    private static final float MAX_ALPHA = 0.5f;

    private static final int DURATION = 2000;

    public ClockManager(Activity activity, RelativeLayout container, ArrayList<ClockAnimationRequest> requests, int x, int y)
    {
        image = new ImageView(activity);

        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        image.setVisibility(View.INVISIBLE);

        this.activity = activity;
        this.container = container;
        this.requests = requests;
        this.x = x;
        this.y = y;

        image.setImageBitmap(Pictures.redClock);

        damage = Factory.getRandomInt(100, 250);
    }

    public void addViewAndStart()
    {
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Dimensions.clockWidth, Dimensions.clockHeight);
                image.setLayoutParams(params);

                container.addView(image, params);

                image.setX(x - Dimensions.clockWidth / 2);
                image.setY(y - Dimensions.clockHeight / 2);

                synchronized (GameSurfaceView.clockLock)
                {
                    addClockAnimationRequest(true, true);
                }
            }
        });
    }

    public void removeView()
    {
        try
        {
            container.removeView(image);
        }
        catch (Exception ex)
        {

        }
    }

    public int getX()
    {
        return x;
    }

    private void addClockAnimationRequest(boolean isRed, boolean fadeIn)
    {
        synchronized (GameSurfaceView.clockLock)
        {
            requests.add(new ClockAnimationRequest(this, isRed, fadeIn));
        }
    }

    public boolean isAlive()
    {
        return isAlive;
    }

    public int getHealthChange()
    {
        if (! isAlive) return 0;

        counter--;

        if (counter == FADE_RED_OUT_POINT && ! healingStageStarted)
        {
            addClockAnimationRequest(true, false);
        }

        if (counter == 0)
        {
            lastHealthPointReached = 0;

            if (healingStageStarted)
            {
                isAlive = false;

                addClockAnimationRequest(false, false);

                return 0;
            }
            else
            {
                counter = HEAL_DURATION;

                addClockAnimationRequest(false, true);

                activity.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        image.setImageBitmap(Pictures.greenClock);
                    }
                });

                healingStageStarted = true;
            }
        }

        if (healingStageStarted)
        {
            if ((HEAL_DURATION - counter) >= lastHealthPointReached + (double) HEAL_DURATION / (2 * damage))
            {
                lastHealthPointReached += (double) HEAL_DURATION / (2 * damage);

                return 1;
            }
        }
        else
        {
            if ((DAMAGE_DURATION - counter) >= lastHealthPointReached + (double) DAMAGE_DURATION / damage)
            {
                lastHealthPointReached += (double) DAMAGE_DURATION / damage;

                return -1;
            }
        }

        return 0;
    }

    public void startAnimation(boolean isRed, boolean fadeIn, final Runnable animationEndListener)
    {
        int colour;
        float start, end;

        if (isRed)
        {
            colour = RED;
        }
        else
        {
            colour = GREEN;
        }

        if (fadeIn)
        {
            start = MIN_ALPHA;
            end = MAX_ALPHA;
        }
        else
        {
            start = MAX_ALPHA;
            end = MIN_ALPHA;
        }

        image.setVisibility(View.VISIBLE);
        image.setAlpha(1f);
        image.setBackgroundColor(colour);

        animator = ValueAnimator.ofFloat(start, end);
        animator.setDuration(DURATION);
        animator.setRepeatCount(0);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                image.setAlpha((Float) animation.getAnimatedValue());
            }
        });

        animator.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {

            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                animationEndListener.run();
            }

            @Override
            public void onAnimationCancel(Animator animation)
            {

            }

            @Override
            public void onAnimationRepeat(Animator animation)
            {

            }
        });

        animator.start();
    }
}
