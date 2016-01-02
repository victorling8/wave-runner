package waverunner.example.victorling8.waverunner.GameAndTutorial;

import android.graphics.Canvas;
import android.graphics.Rect;

public class Missile
{
    public double x;
    public int y;

    public Missile(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public void move()
    {
        x -= Dimensions.missileSpeed;
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(Pictures.missile1,
                (int) x - Dimensions.missileWidth / 2,
                y - Dimensions.missileHeight / 2,
                Paints.blackPaint);
    }

    public Rect getRectangle()
    {
        return new Rect((int) x - Dimensions.missileWidth / 2,
                y - Dimensions.missileHeight / 2,
                (int) x,
                y + Dimensions.missileHeight / 2);
    }

    public int getDamage()
    {
        return 30;
    }
}
