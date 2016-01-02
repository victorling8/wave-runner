package waverunner.example.victorling8.waverunner.GameAndTutorial;

public class MyThread extends Thread
{
    private Runnable runnable;

    public MyThread(Runnable runnable)
    {
        super(runnable);

        this.runnable = runnable;
    }

    public Runnable getRunnable()
    {
        return runnable;
    }
}
