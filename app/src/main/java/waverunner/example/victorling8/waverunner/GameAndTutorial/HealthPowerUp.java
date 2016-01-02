package waverunner.example.victorling8.waverunner.GameAndTutorial;

public class HealthPowerUp extends PowerUp
{
    public HealthPowerUp(int x, int y)
    {
        super(x, y);

        pictureOnScreen = Pictures.healthPowerUpOnScreen;
        pictureBottomBar = Pictures.healthPowerUpBottomBar;
    }
}
