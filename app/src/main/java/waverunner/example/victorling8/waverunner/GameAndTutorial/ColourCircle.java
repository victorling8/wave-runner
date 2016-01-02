package waverunner.example.victorling8.waverunner.GameAndTutorial;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

public class ColourCircle
{
    public final static int ATTACK_STAGE_1 = 1;
    public final static int ATTACK_STAGE_2 = 2;
    public final static int COMEBACK_STAGE_1 = 3;
    public final static int COMEBACK_STAGE_2 = 4;

    public int x;
    public int y;

    public boolean alive = true;
    private boolean shouldDisappear = false;

    private double doubleX;

    private double speed;

    private int playerX;
    private int playerY;

    private int stageCounter = COMEBACK_STAGE_2_LENGTH;

    public final static int ATTACK_STAGE_1_LENGTH = 100;
    public final static int ATTACK_STAGE_2_LENGTH = 100;
    public final static int COMEBACK_STAGE_1_LENGTH = 70;
    public final static int COMEBACK_STAGE_2_LENGTH = 100;

    public int stage = COMEBACK_STAGE_2;
    private boolean readyForNextStage;

    public int radius;

    public boolean attacking = false;

    private ValueAnimator alphaAnimator;
    private ValueAnimator rotationAnimator;
    private ValueAnimator radiusAnimator;

    public float alpha = 1;

    public float theta = 0;

    public static final float attackRotationSpeed = 10;
    public static final float cameBackRotationSpeed = 5;
    public static final float fadingOutRotationSpeed = 2;

    public float rotationSpeed = cameBackRotationSpeed;

    private int bossX;
    private int bossY;

    public ColourCircle(int x, int y)
    {
        this.x = x;
        this.y = y;

        radius = Dimensions.minColourCircleComebackRadius;
    }

    public void rotate()
    {
        theta += rotationSpeed;
    }

    public void tryToChangeStage(int playerX, int playerY, Activity activity)
    {
        stageCounter--;

        readyForNextStage = (stageCounter == 0);

        startAppropriateStage(playerX, playerY, activity);
    }

    public void startAppropriateStage(final int playerX, final int playerY, Activity activity)
    {
        if (! readyForNextStage) return;

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ColourCircle.this.playerX = playerX;
                ColourCircle.this.playerY = playerY;

                readyForNextStage = false;

                cancelAnimators();

                switch (stage) {
                    case ATTACK_STAGE_1:
                        initializeAttackStage2();
                        break;
                    case ATTACK_STAGE_2:
                        initializeComebackStage1();
                        break;
                    case COMEBACK_STAGE_1:
                        initializeComebackStage2();
                        break;
                    case COMEBACK_STAGE_2:
                        initializeAttackStage1();
                        break;
                    default:
                        break;
                }

                resetStageCounter();
                startAnimators();
            }
        });
    }

    private void cancelAnimators()
    {
        if (alphaAnimator != null)
        {
            alphaAnimator.cancel();
        }

        if (rotationAnimator != null)
        {
            rotationAnimator.cancel();
        }

        if (radiusAnimator != null)
        {
            radiusAnimator.cancel();
        }
    }

    private void startAnimators()
    {
        alphaAnimator.start();
        rotationAnimator.start();
        radiusAnimator.start();
    }

    private void initializeAttackStage1()
    {
        stage = ATTACK_STAGE_1;

        alphaAnimator = getAlphaAnimator(1, 0);
        rotationAnimator = getRotationAnimator(cameBackRotationSpeed, fadingOutRotationSpeed);
        radiusAnimator = getRadiusAnimator(Dimensions.minColourCircleComebackRadius, Dimensions.maxColourCircleComebackRadius);
    }

    private void initializeAttackStage2()
    {
        stage = ATTACK_STAGE_2;

        attacking = true;

        setSpeed();

        doubleX = Dimensions.width / 2;
        x = Dimensions.width / 2;
        y = playerY;

        alphaAnimator = getAlphaAnimator(0, 1);
        rotationAnimator = getRotationAnimator(fadingOutRotationSpeed, attackRotationSpeed);
        radiusAnimator = getRadiusAnimator(Dimensions.minColourCircleAttackRadius, Dimensions.maxColourCircleAttackRadius);
    }

    private void initializeComebackStage1()
    {
        stage = COMEBACK_STAGE_1;

        alphaAnimator = getAlphaAnimator(1, 0);
        rotationAnimator = getRotationAnimator(attackRotationSpeed, fadingOutRotationSpeed);
        radiusAnimator = getRadiusAnimator(Dimensions.maxColourCircleAttackRadius, Dimensions.minColourCircleAttackRadius);
    }

    private void initializeComebackStage2()
    {
        stage = COMEBACK_STAGE_2;

        attacking = false;

        alphaAnimator = getAlphaAnimator(0, 1);
        rotationAnimator = getRotationAnimator(fadingOutRotationSpeed, cameBackRotationSpeed);
        radiusAnimator = getRadiusAnimator(Dimensions.maxColourCircleComebackRadius, Dimensions.minColourCircleComebackRadius);
    }

    private void resetStageCounter()
    {
        switch(stage)
        {
            case ATTACK_STAGE_1:
                stageCounter = ATTACK_STAGE_1_LENGTH;
                break;
            case ATTACK_STAGE_2:
                stageCounter = ATTACK_STAGE_2_LENGTH;
                break;
            case COMEBACK_STAGE_1:
                stageCounter = COMEBACK_STAGE_1_LENGTH;
                break;
            case COMEBACK_STAGE_2:
                stageCounter = COMEBACK_STAGE_2_LENGTH;
                break;
            default:
                break;
        }
    }

    private void setSpeed()
    {
        speed = (double) ((Dimensions.width / 2) - (Dimensions.width / 6)) / ATTACK_STAGE_2_LENGTH;
    }

    public void move()
    {
        if (stage == ATTACK_STAGE_1 || stage == COMEBACK_STAGE_2)
        {
            x = bossX;
            y = bossY;
        }
        else if (stage == ATTACK_STAGE_2)
        {
            doubleX -= speed;
            x = (int) Math.round(doubleX);
        }

        if (shouldDisappear)
        {
            if (stage == COMEBACK_STAGE_2 || stage == ATTACK_STAGE_1)
            {
                alive = false;
            }
        }
    }

    private ValueAnimator getAlphaAnimator(float start, float end)
    {
        ValueAnimator animator = ValueAnimator.ofFloat(start, end);
        animator.setDuration(750);
        animator.setRepeatCount(0);
        animator.setInterpolator(new LinearInterpolator());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                alpha = (Float) animation.getAnimatedValue();
            }
        });

        return animator;
    }

    private ValueAnimator getRotationAnimator(float start, float end)
    {
        ValueAnimator animator = ValueAnimator.ofFloat(start, end);
        animator.setDuration(750);
        animator.setRepeatCount(0);
        animator.setInterpolator(new DecelerateInterpolator());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                rotationSpeed = (Float) animation.getAnimatedValue();
            }
        });

        return animator;
    }

    private ValueAnimator getRadiusAnimator(int start, int end)
    {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(750);
        animator.setRepeatCount(0);
        animator.setInterpolator(new LinearInterpolator());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                radius = (Integer) animation.getAnimatedValue();
            }
        });

        return animator;
    }

    public void notifyBossUpdate(int bossX, int bossY)
    {
        this.bossX = bossX;
        this.bossY = bossY;
    }

    public void notifyShouldDisappear()
    {
        shouldDisappear = true;
    }

    public int getDamage()
    {
        return 4;
    }
}
