package waverunner.example.victorling8.waverunner.GameAndTutorial;

public class TutorialTipRequest
{
    public int type;
    public Object object;
    public int counter;

    public TutorialTipRequest(int type, Object object, int counter)
    {
        this.type = type;
        this.object = object;
        this.counter = counter;
    }

    public void getCloser()
    {
        counter--;
    }

    public boolean shouldShow()
    {
        return counter <= 0;
    }
}
