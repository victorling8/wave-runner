package waverunner.example.victorling8.waverunner.GameAndTutorial;

import android.graphics.Rect;

public class Block
{
    protected Rect rectangle;

    private double doubleLeft;
    private double doubleRight;

    public Block(int left, int top, int right, int bottom)
    {
        rectangle = new Rect(left, top, right, bottom);

        doubleLeft = left;
        doubleRight = right;
    }

    public Rect getRectangle()
    {
        return rectangle;
    }

    public void move()
    {
        doubleLeft -= Dimensions.normalMoveLeftSpeed;
        doubleRight -= Dimensions.normalMoveLeftSpeed;

        int left = (int) Math.round(doubleLeft);
        int right = (int) Math.round(doubleRight);

        rectangle.set(left, rectangle.top, right, rectangle.bottom);
    }

    public int getDamage()
    {
        return 15;
    }
}
