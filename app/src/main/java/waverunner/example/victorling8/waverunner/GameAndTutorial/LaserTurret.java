package waverunner.example.victorling8.waverunner.GameAndTutorial;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class LaserTurret
{
    private static final int notShootTime = 100;
    private static final int shootTime = 50;

    public int x;
    public double y;

    private boolean laserShooting = false;
    private Rect laser;
    private double speedY;
    private int reload;

    public LaserTurret(int x, int y, boolean goingUp)
    {
        this.x = x;
        this.y = y;

        speedY = (goingUp? -1 : 1) * Dimensions.laserTurretSpeed * speedModifier();

        reload = notShootTime;

        laser = new Rect(0, y - Dimensions.laserTurretHeight / 6, Dimensions.width, y + Dimensions.laserTurretHeight / 6);
    }

    private double speedModifier()
    {
        return Math.random() / 2 + 0.75;
    }

    public void move()
    {
        y += speedY;

        laser.top = (int) y - Dimensions.laserTurretHeight / 6;
        laser.bottom = (int) y + Dimensions.laserTurretHeight / 6;

        changeStateIfPossible();
    }

    public void changeStateIfPossible()
    {
        reload--;

        if (reload == 0)
        {
            if (laserShooting)
            {
                reload = notShootTime;
            }
            else
            {
                reload = shootTime;
            }

            laserShooting = ! laserShooting;
        }
    }

    public void draw(Canvas canvas, Bitmap loading)
    {
        canvas.drawBitmap(Pictures.laserTurretRightSide,
                x - Dimensions.laserTurretWidth / 2,
                (int) y - Dimensions.laserTurretHeight / 2,
                Paints.blackPaint);

        canvas.drawBitmap(loading,
                x + 4 * Dimensions.laserLoadingWidth / 5 - Dimensions.laserLoadingWidth,
                (int) y - Dimensions.laserLoadingHeight / 2,
                Paints.blackPaint);
    }

    public boolean isLaserShooting()
    {
        return laserShooting;
    }

    public Rect getLaser()
    {
        return laser;
    }

    public int getDamage()
    {
        return 1;
    }
}
