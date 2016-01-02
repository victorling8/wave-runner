package waverunner.example.victorling8.waverunner.GameAndTutorial;

import android.app.Activity;

public class ClockAnimationRequest
{
    private ClockManager clockManager;
    private boolean isRed;
    private boolean fadeIn;
    private boolean hasStarted = false;
    private boolean requestDone = false;

    public ClockAnimationRequest(ClockManager clockManager, boolean isRed, boolean fadeIn)
    {
        this.clockManager = clockManager;
        this.isRed = isRed;
        this.fadeIn = fadeIn;
    }

    public void tryToStartAnimation(Activity activity)
    {
        if (hasStarted) return;

        hasStarted = true;

        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                clockManager.startAnimation(isRed, fadeIn, new Runnable()
                {
                    @Override
                    public void run()
                    {
                        requestDone = true;
                    }
                });
            }
        });
    }

    public boolean isRequestDone()
    {
        return requestDone;
    }
}
