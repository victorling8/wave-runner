package waverunner.example.victorling8.waverunner.GameAndTutorial;

import android.content.res.Resources;
import android.graphics.Canvas;

import java.util.ArrayList;

public class PathShooter
{
    private int reloadTime = 150;

    public double x;
    public int y;
    private int reload = reloadTime;

    public PathShooter(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public void move()
    {
        x -= Dimensions.pathShooterSpeed;
    }

    public void fireIfPossible(ArrayList<Path> paths, int playerX, int playerY, Resources resources)
    {
        reload--;

        if (reload == 0)
        {
            reload = reloadTime;

            paths.add(new Path((int) x, y, playerX, playerY, resources));
        }
    }

    public void draw(Canvas canvas, int playerX, int playerY)
    {
        canvas.drawBitmap(Pictures.pathShooterBase,
                (int) x - Dimensions.turretBaseWidth / 2,
                y - Dimensions.turretBaseHeight / 2,
                Paints.blackPaint);

        canvas.save(Canvas.MATRIX_SAVE_FLAG);

        float theta;
        int differenceX = playerX - (int) x;
        int differenceY = y - playerY;

        if (differenceX == 0)
        {
            if (differenceY >= 0)
            {
                theta = 90;
            }
            else
            {
                theta = 270;
            }
        }
        else if (differenceX > 0)
        {
            if (differenceY >= 0)
            {
                theta = (float) Math.toDegrees(Math.atan((double) differenceY / differenceX));
            }
            else
            {
                theta = (float) (360 - Math.toDegrees(Math.atan(Math.abs((double) differenceY / differenceX))));
            }
        }
        else
        {
            if (differenceY >= 0)
            {
                theta = (float) (180 - Math.toDegrees(Math.atan(Math.abs((double) differenceY / differenceX))));
            }
            else
            {
                theta = (float) (180 + Math.toDegrees(Math.atan(Math.abs((double) differenceY / differenceX))));
            }
        }

        canvas.rotate(- theta + 180, (float) x, (float) y);

        canvas.drawBitmap(Pictures.turretGun,
                (int) x - Dimensions.turretBaseWidth / 2 - Dimensions.turretGunWidth / 2,
                y - Dimensions.turretGunHeight / 2,
                Paints.blackPaint);

        canvas.restore();
    }
}
