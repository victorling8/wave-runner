package waverunner.example.victorling8.waverunner.GameAndTutorial;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

public class Turret
{
    private int reloadTime = 50;

    public double x;
    public double y;
    private double speedY;
    private int reload;

    public Turret(int x, int y, boolean goingUp)
    {
        this.x = x;
        this.y = y;

        speedY = (goingUp? -1 : 1) * Dimensions.turretSpeed * speedModifier();

        reload = reloadTime;
    }

    private double speedModifier()
    {
        return Math.random() / 2 + 0.75;
    }

    public void move()
    {
        y += speedY;
    }

    public void fireIfPossible(ArrayList<Bullet> bullets, int playerX, int playerY)
    {
        if (reload == 0)
        {
            reload = reloadTime;

            bullets.add(new Bullet((int) x, (int) y, playerX, playerY));
        }
        else
        {
            reload--;
        }
    }

    public void draw(Canvas canvas, int playerX, int playerY)
    {
        Bitmap turretBase = Pictures.turretBase;
        Bitmap turretGun = Pictures.turretGun;

        int turretBaseWidth = Dimensions.turretBaseWidth;
        int turretBaseHeight = Dimensions.turretBaseHeight;
        int turretGunWidth = Dimensions.turretGunWidth;
        int turretGunHeight = Dimensions.turretGunHeight;

        canvas.drawBitmap(turretBase,
                (int) x - turretBaseWidth / 2,
                (int) y - turretBaseHeight / 2,
                Paints.blackPaint);


        int directionX = playerX - (int) x;
        int directionY = playerY - (int) y;

        double theta = Math.atan((double) directionY / directionX);
        theta = Math.toDegrees(theta);

        canvas.save(Canvas.MATRIX_SAVE_FLAG);

        canvas.rotate((float) theta, (int) x + turretGunWidth / 2, (int) y);

        canvas.drawBitmap(turretGun,
                (int) x - turretBaseWidth / 2 - turretGunWidth / 2,
                (int) y - turretGunHeight / 2,
                Paints.blackPaint);

        canvas.restore();
    }
}
