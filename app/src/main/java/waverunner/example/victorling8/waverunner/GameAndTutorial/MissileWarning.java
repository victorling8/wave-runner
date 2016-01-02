package waverunner.example.victorling8.waverunner.GameAndTutorial;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;

public class MissileWarning
{
    public int x;
    public int y;
    public float alpha;

    public boolean animationStarted = false;
    public boolean completed = false;

    public MissileWarning(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public void startAnimation()
    {
        animationStarted = true;

        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(1000);
        animator.setRepeatCount(0);
        animator.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                if (input <= 0.5) {
                    return 2 * input;
                } else {
                    return (float) (1 - ((input - 0.5) / 0.5));
                }
            }
        });

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                alpha = (Float) animation.getAnimatedValue();
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
                completed = true;
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
