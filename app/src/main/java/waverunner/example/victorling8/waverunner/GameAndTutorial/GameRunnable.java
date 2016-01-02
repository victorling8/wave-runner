package waverunner.example.victorling8.waverunner.GameAndTutorial;

import android.graphics.Canvas;

public class GameRunnable implements Runnable
{
    private GameSurfaceView surface;
    private Thread thread;
    private boolean running;
    private boolean shouldDraw;
    private boolean canvasCreated;
    private boolean bottomBarInitialized;

    public GameRunnable(GameSurfaceView surface)
    {
        this.surface = surface;
        running = true;
        shouldDraw = true;
        canvasCreated = true;
        bottomBarInitialized = false;
    }

    @Override
    public void run()
    {
        Canvas canvas;

        surface.showBottomBar();

        while(running)
        {
            if (shouldDraw && canvasCreated && bottomBarInitialized)
            {
                canvas = surface.getHolder().lockCanvas();

                if(canvas != null)
                {
                    synchronized (surface.getHolder())
                    {
                        surface.updateScreen(canvas);
                    }
                    surface.getHolder().unlockCanvasAndPost(canvas);
                }
            }
            else
            {
                if (surface.haveFoundViews() && ! bottomBarInitialized)
                {
                    surface.initializeBottomBar();
                }
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

    public void setRunning(boolean state)
    {
        running = state;
    }

    public void setThread(Thread thread)
    {
        this.thread = thread;
    }

    public void setShouldDraw(boolean state)
    {
        shouldDraw = state;
    }

    public void setBottomBarInitialized(boolean state)
    {
        bottomBarInitialized = state;
    }

    public void setCanvasCreated(boolean state)
    {
        canvasCreated = state;
    }
}
