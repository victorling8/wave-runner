package waverunner.example.victorling8.waverunner.GameAndTutorial;

import android.graphics.Canvas;

public class RaceBlock extends Block
{
    public RaceBlock(int left, int top, int right, int bottom)
    {
        super(left, top, right, bottom);
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(Pictures.raceBlock, rectangle.left, rectangle.top, Paints.blackPaint);
    }
}
