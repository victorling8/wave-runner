package waverunner.example.victorling8.waverunner.GameAndTutorial;

import java.util.ArrayList;

public class Boss
{
    public double x;
    public double y;

    public boolean alive = true;

    private int standStillDuration = 500;
    private int moveOutCounter = standStillDuration;

    private boolean shouldMoveIn = true;
    private boolean shouldMoveOut = false;
    private boolean movedIn = false;

    boolean escapeUp;

    private static final int reloadTime = 70;
    private int reload = reloadTime;
    private static final int bulletsPerFire = 8;

    private ColourCircle colourCircle;

    public Boss(int x, int y, ColourCircle colourCircle)
    {
        this.x = x;
        this.y = y;
        this.colourCircle = colourCircle;

        escapeUp = (Math.random() < 0.5);
    }

    public void moveIn()
    {
        if (shouldMoveIn)
        {
            x -= Dimensions.bossSpeed;

            if (x <= Dimensions.width - 2 * Dimensions.bossBaseWidth)
            {
                x = Dimensions.width - 2 * Dimensions.bossBaseWidth;

                shouldMoveIn = false;
                movedIn = true;
            }

            colourCircle.notifyBossUpdate((int) x, (int) y);
        }
    }

    public void getCloserToMovingOut()
    {
        if (! movedIn) return;

        moveOutCounter--;

        shouldMoveOut = (moveOutCounter <= 0);
    }

    public void moveOut()
    {
        if (shouldMoveOut)
        {
            if (escapeUp)
            {
                y -= Dimensions.bossEscapeSpeed;
            }
            else
            {
                y += Dimensions.bossEscapeSpeed;
            }
        }

        colourCircle.notifyBossUpdate((int) x, (int) y);

        checkIfAlive();
    }

    public void checkIfAlive()
    {
        alive = ! (y < - Dimensions.bossBaseWidth / 2 || y > Dimensions.height + Dimensions.bossBaseWidth / 2);

        if (! alive)
        {
            colourCircle.notifyShouldDisappear();
        }
    }

    public void fireIfPossible(ArrayList<Bullet> bullets, int playerX)
    {
        if (reload == 0)
        {
            reload = reloadTime;

            int height = getRandomHeight();

            for (int i = 0; i < bulletsPerFire; i++)
            {
                bullets.add(new BossBullet((int) x, (int) y, playerX, getRandomAdjustedHeight(height)));
            }
        }
        else
        {
            reload--;
        }
    }

    public int getRandomHeight()
    {
        return (int) (Math.random() * Dimensions.height);
    }

    public int getRandomAdjustedHeight(int height)
    {
        return (int) (height + (Dimensions.height / 2 * Math.random() - Dimensions.height / 4));
    }
}
