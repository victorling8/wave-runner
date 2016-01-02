package waverunner.example.victorling8.waverunner.GameAndTutorial;

import android.os.Handler;
import android.os.Looper;

public class LooperThread extends Thread
{
    private Handler handler;

    public Handler getHandler()
    {
        return handler;
    }

    @Override
    public void run()
    {
        Looper.prepare();

        handler = new Handler();

        Looper.loop();
    }
}
