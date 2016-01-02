package waverunner.example.victorling8.waverunner.GameAndTutorial;

import android.graphics.Canvas;

public class BossBullet extends Bullet
{
    public BossBullet(int x, int y, int playerX, int playerY)
    {
        super(x, y, playerX, playerY);
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(Pictures.bossBullet,
                x - Dimensions.bulletWidth / 2,
                y - Dimensions.bulletWidth / 2,
                Paints.blackPaint);
    }

    public int getDamage()
    {
        return 10;
    }
}
