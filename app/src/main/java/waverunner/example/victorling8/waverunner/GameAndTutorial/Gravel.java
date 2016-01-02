package waverunner.example.victorling8.waverunner.GameAndTutorial;

public class Gravel
{
    public int x;
    public int y;

    public static int gravelRadius = 3;

    public double speedX;
    public double speedY;

    private double maxSpeedX = 10;
    private double minSpeedUpY = 4;
    private double maxSpeedUpY = 10;
    private double maxSpeedDownY = 4;

    public Gravel(int x, int y, boolean up)
    {
        this.x = x;
        this.y = y;

        speedX = 2 * Math.random() * maxSpeedX - maxSpeedX;

        if (up)
        {
            speedY = - (Math.random() * (maxSpeedUpY - minSpeedUpY) + minSpeedUpY);
        }
        else
        {
            speedY = Math.random() * maxSpeedDownY;
        }
    }

    public void move()
    {
        x += speedX;
        y += speedY;
    }

    public void gravitate(double gravity)
    {
        speedY += gravity;
    }
}
