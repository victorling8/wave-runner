package waverunner.example.victorling8.waverunner.GameAndTutorial;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;

public class PathBullet
{
    public int x;
    public int y;
    public int colour;

    public int lifeLength = 30;
    public int lifeCounter = lifeLength;

    public boolean alive = true;

    public PathBullet(int x, int y, int colour)
    {
        this.x = x;
        this.y = y;
        this.colour = colour;
    }

    public void weaken()
    {
        lifeCounter--;

        if (lifeCounter <= 0)
        {
            alive = false;
        }
    }

    public void draw(Canvas canvas)
    {
        int newColour = Color.argb((int) (255 * ((double) lifeCounter / lifeLength)), Color.red(colour), Color.green(colour), Color.blue(colour));

        Paints.anyPaint.setColor(newColour);

        RectF rectangle = new RectF(x - Dimensions.pathBulletRadius,
                y - Dimensions.pathBulletRadius,
                x + Dimensions.pathBulletRadius,
                y + Dimensions.pathBulletRadius);

        canvas.drawOval(rectangle, Paints.anyPaint);
    }

    public int getDamage()
    {
        return (Math.random() < 0.5) ? 1 : 2;
    }
}
