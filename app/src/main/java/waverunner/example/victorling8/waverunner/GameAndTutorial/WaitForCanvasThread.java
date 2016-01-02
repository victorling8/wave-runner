package waverunner.example.victorling8.waverunner.GameAndTutorial;

import android.graphics.Canvas;

public class WaitForCanvasThread extends Thread
{
    private GameSurfaceView game;

    public WaitForCanvasThread(GameSurfaceView game)
    {
        this.game = game;
    }

    @Override
    public void run()
    {
        Canvas canvas;

        while ((canvas = game.getHolder().lockCanvas()) == null)
        {
            try
            {
                sleep(20);
            }
            catch (InterruptedException ex)
            {

            }
        }

        game.getHolder().unlockCanvasAndPost(canvas);
    }
}
