package waverunner.example.victorling8.waverunner.GameAndTutorial;

import android.graphics.Canvas;
import android.graphics.Point;

import java.util.ArrayList;

public class AllAroundShooter
{
    private int reloadTime = 50;

    public double x;
    public double y;
    private double speedY;
    private int reload;

    int circleTheta = 0;
    int squareTheta = 0;
    int circleRotationSpeed = 3;
    int squareRotationSpeed = - 8;

    public AllAroundShooter(int x, int y, boolean goingUp)
    {
        this.x = x;
        this.y = y;

        speedY = (goingUp? -1 : 1) * Dimensions.allAroundShooterSpeed * speedModifier();

        reload = reloadTime;
    }

    private double speedModifier()
    {
        return Math.random() / 2 + 0.75;
    }

    public void move()
    {
        y += speedY;
    }

    public void fireIfPossible(ArrayList<Bullet> bullets)
    {
        if (reload == 0)
        {
            reload = reloadTime;

            fireAllAround(bullets);
        }
        else
        {
            reload--;
        }
    }

    private void fireAllAround(ArrayList<Bullet> bullets)
    {
        ArrayList<Point> points = getRandomBulletPositions();

        for (Point point : points)
        {
            bullets.add(new AllAroundShooterBullet((int) x, (int) y, (int) getAngle(point.x, point.y)));
        }
    }

    private ArrayList<Point> getRandomBulletPositions()
    {
        ArrayList<Point> points = new ArrayList<>();

        int radius = 100;

        int numberOfBullets = Factory.getRandomInt(15, 30);

        double theta;
        double increment = (double) 360 / numberOfBullets;

        for (int i = 0; i < numberOfBullets; i++)
        {
            theta = i * increment;

            points.add(new Point(getPointOnCircle(radius, theta)));
        }

        return points;
    }

    private double getAngle(double x, double y)
    {
        if (x == 0)
        {
            if (y > 0)
            {
                return 90;
            }
            else
            {
                return 270;
            }
        }
        else if (y == 0)
        {
            if (x > 0)
            {
                return 0;
            }
            else
            {
                return 180;
            }
        }

        if (x > 0 && y > 0)
        {
            return Math.toDegrees(Math.atan(y / x));
        }
        else if (x > 0 && y < 0)
        {
            return 360 - Math.toDegrees(Math.atan(Math.abs(y) / x));
        }
        else if (x < 0 && y > 0)
        {
            return 180 - Math.toDegrees(Math.atan(y / Math.abs(x)));
        }
        else
        {
            return 180 + Math.toDegrees(Math.atan(y / x));
        }
    }

    private Point getPointOnCircle(double radius, double theta)
    {
        return new Point((int) (radius * Math.cos(Math.toRadians(theta))), (int) (radius * Math.sin(Math.toRadians(theta))));
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(Pictures.allAroundShooterBase,
                (int) x - Dimensions.allAroundShooterBaseWidth / 2,
                (int) y - Dimensions.allAroundShooterBaseWidth / 2,
                Paints.blackPaint);

        circleTheta += circleRotationSpeed;
        squareTheta += squareRotationSpeed;

        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.rotate(circleTheta, (int) x, (int) y);
        canvas.drawBitmap(Pictures.allAroundShooterCircle,
                (int) x - Dimensions.allAroundShooterCircleRadius,
                (int) y - Dimensions.allAroundShooterCircleRadius,
                Paints.blackPaint);
        canvas.restore();

        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.rotate(squareTheta, (int) x, (int) y);
        canvas.drawBitmap(Pictures.allAroundShooterSquare,
                (int) x - Dimensions.allAroundShooterSquareWidth / 2,
                (int) y - Dimensions.allAroundShooterSquareWidth / 2,
                Paints.blackPaint);
        canvas.restore();
    }
}
