package waverunner.example.victorling8.waverunner.GameAndTutorial;

import android.graphics.Canvas;

public class Bullet
{
    public int x;
    public int y;

    private double doubleX;
    private double doubleY;

    public double incrementX;
    public double incrementY;

    public boolean bouncedOffShield = false;

    public Bullet(int x, int y, int playerX, int playerY)
    {
        this.x = x;
        this.y = y;

        doubleX = x;
        doubleY = y;

        int directionX = playerX - x;
        int directionY = playerY - y;

        double theta = Math.atan((double) directionY / directionX);

        incrementX = - Dimensions.bulletSpeed * Math.cos(theta);
        incrementY = - Dimensions.bulletSpeed * Math.sin(theta);
    }

    public void move()
    {
        doubleX += incrementX;
        doubleY += incrementY;

        x = (int) Math.round(doubleX);
        y = (int) Math.round(doubleY);
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(Pictures.turretBullet,
                x - Dimensions.bulletWidth / 2,
                y - Dimensions.bulletWidth / 2,
                Paints.blackPaint);
    }

    public int getDamage()
    {
        return 4;
    }
}
