package waverunner.example.victorling8.waverunner.GameAndTutorial;

import android.graphics.Canvas;

public class Coin
{
    public double x;
    public int y;

    public int yIncludingFloat;

    public Coin(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public void move(int floating)
    {
        x -= Dimensions.normalMoveLeftSpeed;
        yIncludingFloat = y + floating;
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(Pictures.coin,
                (int) x - Dimensions.coinRadius / 2,
                yIncludingFloat - Dimensions.coinRadius / 2,
                Paints.blackPaint);
    }
}
