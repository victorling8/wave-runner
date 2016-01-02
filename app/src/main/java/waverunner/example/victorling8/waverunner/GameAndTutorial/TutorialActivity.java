package waverunner.example.victorling8.waverunner.GameAndTutorial;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import waverunner.example.victorling8.waverunner.GameAndTutorial.GameSurfaceView;
import waverunner.example.victorling8.waverunner.GameAndTutorial.PowerUp;
import waverunner.example.victorling8.waverunner.R;

public class TutorialActivity extends AppCompatActivity
{
    private GameSurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.game_screen);

        int skinId = getIntent().getIntExtra("Skin", 0);
        int colour = getIntent().getIntExtra("Colour", 0x33ccff);
        boolean isSkin = getIntent().getBooleanExtra("isSkin", false);

        surfaceView = (GameSurfaceView) findViewById(R.id.game_screen);

        if (isSkin)
        {
            surfaceView.setSkinId(skinId, this);
        }

        surfaceView.setTutorial();

        surfaceView.setColour(colour);
        surfaceView.passPowerUpInfo(new PowerUp.PowerUpInfo(getIntent().getIntExtra("maxPowerUps", 3), getIntent().getIntExtra("startingPowerUps", 0)));

        surfaceView.canFindViews(this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public GameSurfaceView getSurfaceView()
    {
        return surfaceView;
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        surfaceView.setShouldDraw(true);
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        surfaceView.setShouldDraw(false);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        surfaceView.stopThreads();
    }
}
