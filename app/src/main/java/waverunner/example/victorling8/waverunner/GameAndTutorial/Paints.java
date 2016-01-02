package waverunner.example.victorling8.waverunner.GameAndTutorial;

import android.graphics.Color;
import android.graphics.Paint;

public class Paints
{
    public static Paint transparentPaint = new Paint();
    public static Paint blackPaint = new Paint();
    public static Paint touchLinePaint= new Paint();
    public static Paint anyPaint = new Paint();

    public static void initializePaints()
    {
        transparentPaint.setColor(0);
        blackPaint.setColor(Color.BLACK);
        touchLinePaint.setColor(Color.BLACK);
    }
}
