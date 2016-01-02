package waverunner.example.victorling8.waverunner.GameAndTutorial;

import android.content.res.Resources;

import java.util.ArrayList;

import waverunner.example.victorling8.waverunner.R;

public class Path
{
    public int x;
    public int y;
    public int colour;

    public double slope;
    public double perpendicular;

    public double theta;
    public double normalTheta;

    public int playerX;
    public int playerY;

    public double distanceTravelled;

    public int amplitude;
    public int period;

    public int reloadTime = 2;
    public int reloadCounter = reloadTime;

    public int currentX;
    public int currentY;

    public Path(int x, int y, int playerX, int playerY, Resources resources)
    {
        this.x = x;
        this.y = y;
        this.playerX = playerX;
        this.playerY = playerY;

        if (playerX == x)
        {
            slope = 10000;
        }
        else
        {
            slope = (double) - (playerY - y) / (playerX - x);
        }

        if (slope == 0)
        {
            perpendicular = 10000;
        }
        else
        {
            perpendicular = - 1 / slope;
        }

        if (slope >= 0)
        {
            if (playerX >= x)
            {
                theta = Math.toDegrees(Math.atan(slope));
            }
            else
            {
                theta = 180 + Math.toDegrees(Math.atan(slope));
            }
        }
        else
        {
            if (playerX >= x)
            {
                theta = 360 - Math.toDegrees(Math.atan(Math.abs(slope)));
            }
            else
            {
                theta = 180 - Math.toDegrees(Math.atan(Math.abs(slope)));
            }
        }

        normalTheta = theta + 90;

        amplitude = Factory.getRandomInt(Dimensions.minPathAmplitude, Dimensions.maxPathAmplitude);
        period = Factory.getRandomInt(Dimensions.minPathPeriod, Dimensions.maxPathPeriod);

        int colours[] = resources.getIntArray(R.array.colour_array);

        colour = colours[Factory.getRandomInt(0, colours.length - 1)];
    }

    public void fireIfPossible(ArrayList<PathBullet> pathBullets)
    {
        reloadCounter--;

        if (reloadCounter == 0)
        {
            reloadCounter = reloadTime;

            double newX = x + Math.cos(Math.toRadians(theta)) * distanceTravelled;
            double newY = y - Math.sin(Math.toRadians(theta)) * distanceTravelled;

            double displacement = getDisplacement(distanceTravelled);

            double displacementX = Math.cos(Math.toRadians(normalTheta)) * displacement;
            double displacementY = - Math.sin(Math.toRadians(normalTheta)) * displacement;

            pathBullets.add(new PathBullet((int) Math.round(newX + displacementX), (int) Math.round(newY + displacementY), colour));

            currentX = (int) Math.round(newX + displacementX);
            currentY = (int) Math.round(newY + displacementY);

            distanceTravelled += Dimensions.pathBulletDistance;
        }
    }

    public double getDisplacement(double distance)
    {
        return Math.sin(2 * Math.PI * distance / period) * amplitude;
    }
}
