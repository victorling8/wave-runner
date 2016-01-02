package waverunner.example.victorling8.waverunner.GameAndTutorial;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class PowerUp
{
    public double x;
    public int y;

    public int yIncludingFloat;

    protected Bitmap pictureOnScreen;
    protected Bitmap pictureBottomBar;

    public ImageView image;

    private Activity activity;

    private GameSurfaceView game;

    private RelativeLayout container;

    private int destination;

    ValueAnimator animator;

    private Animator.AnimatorListener nullifyOnEndListener = new Animator.AnimatorListener()
    {
        @Override
        public void onAnimationStart(Animator animation)
        {

        }

        @Override
        public void onAnimationEnd(Animator animation)
        {
            animator = null;
        }

        @Override
        public void onAnimationCancel(Animator animation)
        {

        }

        @Override
        public void onAnimationRepeat(Animator animation)
        {

        }
    };

    private View.OnClickListener onClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            game.usePowerUp(PowerUp.this);
        }
    };

    public PowerUp(int x, int y)
    {
        this.x = x;
        this.y = y;
    }


    public void move(int floating)
    {
        x -= Dimensions.normalMoveLeftSpeed;
        yIncludingFloat = y + floating;
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(pictureOnScreen,
                (int) x - Dimensions.powerUpWidthOnScreen / 2,
                yIncludingFloat - Dimensions.powerUpWidthOnScreen / 2,
                Paints.blackPaint);
    }

    public void addViewNoAnimation(Activity activity,
                                   final int destination,
                                   final RelativeLayout container,
                                   GameSurfaceView game)
    {
        addViewToBottomBar(activity, destination, container, game, false);
    }

    public void addViewAndAnimate(Activity activity,
                                  final int destination,
                                  final RelativeLayout container,
                                  GameSurfaceView game)
    {
        addViewToBottomBar(activity, destination, container, game, true);
    }

    private void addViewToBottomBar(Activity activity,
                                    final int destination,
                                    final RelativeLayout container,
                                    GameSurfaceView game,
                                    final boolean shouldAnimate)
    {
        image = new ImageView(activity);
        image.setImageBitmap(pictureBottomBar);

        image.setOnClickListener(onClick);

        this.container = container;
        this.destination = destination;
        this.activity = activity;
        this.game = game;

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                lp.rightMargin = -Dimensions.powerUpWidthBottomBar;

                image.setLayoutParams(lp);

                container.addView(image, lp);

                int duration = shouldAnimate ? 750 : 0;

                animator = ValueAnimator.ofInt(-Dimensions.powerUpWidthBottomBar, Dimensions.width - (destination + Dimensions.powerUpWidthBottomBar));
                animator.setDuration(duration);
                animator.setRepeatCount(0);
                animator.setInterpolator(new DecelerateInterpolator());

                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        lp.rightMargin = (Integer) animation.getAnimatedValue();
                    }
                });

                animator.addListener(nullifyOnEndListener);

                animator.start();
            }
        });
    }

    public void moveToLeft()
    {
        if (animator != null)
        {
            animator.end();
            image.clearAnimation();
            animator = null;
        }

        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                final RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) image.getLayoutParams();

                int marginEnd = lp.rightMargin;

                destination -= Dimensions.powerUpWidthBottomBar;

                animator = ValueAnimator.ofInt(marginEnd, Dimensions.width - (destination + Dimensions.powerUpWidthBottomBar));
                animator.setDuration(500);
                animator.setRepeatCount(0);
                animator.setInterpolator(new LinearInterpolator());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
                {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        lp.rightMargin = (Integer) animation.getAnimatedValue();
                    }
                });

                animator.addListener(nullifyOnEndListener);

                animator.start();
            }
        });
    }

    public void removeView()
    {
        container.removeView(image);
    }

    public static class PowerUpInfo
    {
        public int maxNumber;
        public int startingNumber;

        public PowerUpInfo(int maxNumber, int startingNumber)
        {
            this.maxNumber = maxNumber;
            this.startingNumber = startingNumber;
        }
    }
}
