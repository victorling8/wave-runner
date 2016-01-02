package waverunner.example.victorling8.waverunner.GameAndTutorial;

import android.graphics.Canvas;

public class BeforeGameRunnable implements Runnable
{
    private GameSurfaceView surface;
    private Thread thread;
    private boolean running;
    private boolean shouldDraw;
    private boolean canvasCreated;

    public BeforeGameRunnable(GameSurfaceView surface)
    {
        this.surface = surface;
        running = true;
        shouldDraw = true;
        canvasCreated = true;
    }

    @Override
    public void run()
    {
        Canvas canvas = surface.getHolder().lockCanvas();

        surface.initializeDimensions(canvas);

        surface.getHolder().unlockCanvasAndPost(canvas);

        surface.initializeRaceBar();
        surface.makeCounterVisible();
        surface.hideBottomBar();

        while(running)
        {
            if (shouldDraw && canvasCreated && surface.haveFoundViews())
            {
                canvas = surface.getHolder().lockCanvas();

                if(canvas != null)
                {
                    synchronized (surface.getHolder())
                    {
                        surface.updateScreenBeforeGame(canvas);
                    }
                    surface.getHolder().unlockCanvasAndPost(canvas);
                }

                try
                {
                    thread.sleep(10);
                }
                catch (InterruptedException e)
                {

                }
            }
        }

        surface.getGameThread().start();
    }

    public void setRunning(boolean state)
    {
        running = state;
    }

    public void setThread(Thread thread)
    {
        this.thread = thread;
    }

    public void setCanvasCreated(boolean state)
    {
        canvasCreated = state;
    }

    public void setShouldDraw(boolean state)
    {
        shouldDraw = state;
    }
}
