package waverunner.example.victorling8.waverunner.GameAndTutorial;

import android.graphics.Color;
import android.graphics.Rect;

public class Remnant
{
    private static double maxSpeedX = 20;
    private static double maxSpeedY = 20;

    private static double maxSpeedXShielded = 40;
    private static double maxSpeedYShielded = 40;

    private static double maxRotationSpeed = 5;

    public double x;
    public double y;

    public int colour;

    int length;
    int rotation;
    int angle;

    public double speedX;
    public double speedY;

    public Remnant(int x, int y, int length, int playerX, int playerY, int blockWidth, int blockHeight, boolean shielded)
    {
        this(x, y, length, playerX, playerY, blockWidth, blockHeight, shielded, Color.GRAY);
    }

    public Remnant(int x, int y, int length, int playerX, int playerY, int blockWidth, int blockHeight, boolean shielded, int colour)
    {
        this.x = x;
        this.y = y;
        this.length = (int) (length * lengthModifier());
        this.colour = colour;

        angle = 0;

        int differenceX = x - playerX;
        int differenceY = y - playerY;

        int maxPossibleDifference = (int) Math.sqrt(blockWidth * blockWidth + blockHeight * blockHeight);
        int actualDifference = (int) Math.sqrt(differenceX * differenceX + differenceY * differenceY);

        if (shielded)
        {
            speedX = (differenceX > 0 ? 1 : -1) * maxSpeedXShielded * ((double) Math.abs(differenceX) / blockWidth) * speedModifier();
            speedY = (differenceY > 0 ? 1 : -1) * maxSpeedYShielded * ((double) Math.abs(differenceY) / blockHeight) * speedModifier();
        }
        else
        {
            speedX = (differenceX > 0 ? 1 : -1) * maxSpeedX * ((double) Math.abs(differenceX) / blockWidth) * speedModifier();
            speedY = (differenceY > 0 ? 1 : -1) * maxSpeedY * ((double) Math.abs(differenceY) / blockHeight) * speedModifier();
        }

        rotation = (int) ((Math.random() > 0.5 ? 1 : -1) * maxRotationSpeed * (1 - actualDifference / maxPossibleDifference));
    }

    public void move()
    {
        x += speedX;
        y += speedY;

        angle += rotation;
    }

    public void gravitate(double gravity)
    {
        speedY += gravity;
    }

    public Rect getRectangle()
    {
        return new Rect((int) (x - length), (int) (y - length), (int) (x + length), (int) (y + length));
    }

    private double speedModifier()
    {
        return Math.random() / 2;
    }

    private double lengthModifier()
    {
        return 0.75 + Math.random() / 2;
    }
}
