package waverunner.example.victorling8.waverunner.GameAndTutorial;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;

public class Explosion
{
    public int x;
    public int y;

    ValueAnimator animator;
    public int index;

    public boolean finished = false;

    public Explosion(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public void startAnimation()
    {
        animator = ValueAnimator.ofInt(0, 7);
        animator.setDuration(750);
        animator.setRepeatCount(0);
        animator.setInterpolator(new LinearInterpolator());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                index = (Integer) animation.getAnimatedValue();
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
                finished = true;
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
