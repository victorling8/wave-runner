package waverunner.example.victorling8.waverunner.GameAndTutorial;

public class FlyingCoin extends Coin
{
    public double doubleX;
    public double doubleY;

    public double speedX;
    public double speedY;

    public double gravity;

    public FlyingCoin(int x, int y, double speedX, double speedY, double gravity)
    {
        super(x, y);

        doubleX = x;
        doubleY = y;

        this.speedX = speedX;
        this.speedY = speedY;

        this.gravity = gravity;
    }

    public void move(int floating)
    {
        doubleX += speedX;
        doubleY -= speedY;

        speedY -= gravity;

        x = (int) doubleX;
        yIncludingFloat = (int) doubleY;
    }
}
