package waverunner.example.victorling8.waverunner.GameAndTutorial;

public class TouchPowerUp extends PowerUp
{
    public TouchPowerUp(int x, int y)
    {
        super(x, y);

        pictureOnScreen = Pictures.touchPowerUpOnScreen;
        pictureBottomBar = Pictures.touchPowerUpBottomBar;
    }
}
