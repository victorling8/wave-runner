package waverunner.example.victorling8.waverunner.MainScreen;

import android.content.Context;

import waverunner.example.victorling8.waverunner.R;

public class Skin
{
    public boolean isSkin;
    public String string;
    public int id;
    public int colour;

    public Skin(boolean isSkin, String string, int id, int colour)
    {
        this.isSkin = isSkin;
        this.string = string;
        this.id = id;
        this.colour = colour;
    }

    public static int getColourFromSkin(int skinId, Context context)
    {
        int colourArray[] = context.getResources().getIntArray(R.array.colour_array);

        switch(skinId)
        {
            case R.drawable.goose_skin:
                return colourArray[1];
            case R.drawable.pink_tiger_skin:
                return colourArray[11];
            case R.drawable.code_skin:
                return colourArray[8];
            case R.drawable.wave_skin:
                return colourArray[6];
            case R.drawable.nyan_cat_skin:
                return colourArray[2];
            case R.drawable.space_skin:
                return colourArray[8];
            case R.drawable.rainbow_skin:
                return colourArray[5];
            case R.drawable.stars_skin:
                return colourArray[6];
            case R.drawable.blocks_skin:
                return colourArray[7];
            case R.drawable.galaxy_skin:
                return colourArray[8];
            case R.drawable.fibre_optic_skin:
                return colourArray[3];
            default:
                return 0xffffffff;
        }
    }
}
