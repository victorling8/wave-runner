package waverunner.example.victorling8.waverunner.GameAndTutorial;

import android.graphics.Canvas;
import android.graphics.Point;

public class AllAroundShooterBullet extends Bullet
{
    public AllAroundShooterBullet(int x, int y, int theta)
    {
        super(x, y, 0, 0);

        Point p = getPointOnCircle(Dimensions.bulletSpeed, theta);

        incrementX = p.x;
        incrementY = - p.y;
    }

    private Point getPointOnCircle(double radius, double theta)
    {
        return new Point((int) (radius * Math.cos(Math.toRadians(theta))), (int) (radius * Math.sin(Math.toRadians(theta))));
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(Pictures.allAroundShooterBullet,
                x - Dimensions.bulletWidth / 2,
                y - Dimensions.bulletWidth / 2,
                Paints.blackPaint);
    }

    public int getDamage()
    {
        return 15;
    }
}
